package ru.tiresexplorer.tiresexplorerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tiresexplorer.tiresexplorerservice.data.Assortment;
import ru.tiresexplorer.tiresexplorerservice.data.AssortmentData;
import ru.tiresexplorer.tiresexplorerservice.data.Availability;
import ru.tiresexplorer.tiresexplorerservice.data.AvailabilityData;
import ru.tiresexplorer.tiresexplorerservice.data.Filter;
import ru.tiresexplorer.tiresexplorerservice.singleton.Cash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataFetcher {
    private static final String ASSORTMENT_URL = "https://lk.selectyre.ru/export/json-assortment/";
    private static final String AVAILABILITY_URL = "https://lk.selectyre.ru/export/json-availability/";
    //private static final String STOCKS_URL = "https://lk.selectyre.ru/export/json-stocks/";

    @Value("${api.uuid}")
    private String uuid;

    String fetchData() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        Cash cash = Cash.getInstance();
        LocalDateTime now = LocalDateTime.now();

        if (cash.getLastUpdated() == null || ChronoUnit.MINUTES.between(cash.getLastUpdated(), now) >= 30) {
            try {
                // Fetch assortment data
                request = HttpRequest.newBuilder()
                        .uri(URI.create(ASSORTMENT_URL + "?uuid=" + uuid))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                cash.setAssortment(performAssortment(response.body()));
            } catch (IOException | InterruptedException e) {
                return "Произошла ошибка при выгрузке ассортимента \n" + e.getMessage();
            }
            try {
                request = HttpRequest.newBuilder()
                        // Fetch availability data
                        .uri(URI.create(AVAILABILITY_URL + "?uuid=" + uuid))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                cash.setAvailability(performAvailability(response.body()));

            } catch (IOException | InterruptedException e) {
                return "Произошла ошибка при выгрузке остатков \n" + e.getMessage();
            }

//        try {
//            // Fetch stocks data
//            request = HttpRequest.newBuilder()
//                    .uri(URI.create(STOCKS_URL + "?uuid=" + uuid))
//                    .GET()
//                    .build();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            Stocks stocks = Stocks.getInstance();
//            stocks.setStock(response.body());
//        } catch (IOException | InterruptedException e) {
//            System.out.println(e.getMessage() + " Stocks");
//        }
            if (cash.getAvailability() == null || cash.getAssortment() == null)
                return "Ничего не выгрузилось, что-то пошло не так";

            cash.setLastUpdated(now);
            return "";
        } else {
            return "";
        }

    }

    private List<Assortment> performAssortment(String response) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            AssortmentData data = objectMapper.readValue(response, AssortmentData.class);
            return data.getTires();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Availability> performAvailability(String response) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            AvailabilityData data = objectMapper.readValue(response, AvailabilityData.class);
            return data.getTires();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    String performData(Filter filter) {
        Cash cash = Cash.getInstance();

        List<Assortment> assortments = new ArrayList<>(cash.getAssortment());

        assortments.removeIf(x -> x.getP_runflat() != filter.isRunflat() ||
                x.getP_cargo() != filter.isLightweight() ||
                x.getP_mud_terrain() != filter.isMt() ||
                x.getP_all_terrain() != filter.isAt()
        );
        if (filter.getWidth() != null) {
            assortments.removeIf(x -> !x.getP_width().equals(filter.getWidth() + ".00"));
        }
        if (filter.getHeight() != null) {
            assortments.removeIf(x -> !x.getP_height().equals(filter.getHeight() + ".00"));
        }
        if (filter.getDiameter() != null) {
            assortments.removeIf(x -> !x.getP_diameter().equals(filter.getDiameter() + ".00"));
        }
        if (!filter.getSeason().equals("")) {
            assortments.removeIf(x -> !x.getP_season().equals(filter.getSeason()));
        }

        switch (filter.getSpikes()) {
            case "Шип.":
                assortments.removeIf(x -> !x.getP_thorn());
                break;
            case "Нешип.":
                assortments.removeIf(Assortment::getP_thorn);
                break;
        }
        if (assortments.size() > 0) {
            List<Availability> availabilities = new ArrayList<>();

            for (Assortment assortment : assortments) {
                cash.getAvailability().forEach(x -> {
                    if (x.getCode().equals(assortment.getCode())) {
                        availabilities.add(x);
                    }
                });
            }

            //availabilities.removeIf(x -> x.getQuantity() < 4);

            availabilities.forEach(x -> x.setPrice(round25(x.getPrice()) + ""));

            StringBuilder result = new StringBuilder();

            List<Availability> uniqueList = eliminateDuplicates(availabilities);

            uniqueList.sort(Comparator.comparing(x -> Integer.parseInt(x.getPrice())));

            for (Availability availability : uniqueList) {

                assortments.forEach(x -> {
                    if (x.getCode().equals(availability.getCode())) {
                        result.append(x.getP_full_name()).append(" - ");
                        if (availability.getPrice().split(",").length > 1) {
                            for (int i = 0; i < availability.getPrice().split(",").length; i++) {
                                String[] prices = availability.getPrice().split(",");
                                result.append(prices[i]).append(" ₽/шт. ");
                                if (i + 1 < prices.length) {
                                    result.append(" ;");
                                }

                            }
                        } else {
                            result.append(availability.getPrice()).append(" ₽/шт. ");
                        }
                    }
                });
                result.append("\n");
            }
            return result.toString();
        }

        return "Ничего не найдено по заданным параметрам";
    }

    private int round25(String number) {
        int remainder = Integer.parseInt(number) % 25;

        if (remainder != 0) {
            return Integer.parseInt(number) + (25 - remainder);
        }
        else {
            return Integer.parseInt(number);
        }
    }

    private static List<Availability> eliminateDuplicates(List<Availability> availabilityList) {
        Map<String, Availability> map = new HashMap<>();

        for (Availability availability : availabilityList) {
            String code = availability.getCode();
            if (map.containsKey(code)) {
                Availability existing = map.get(code);
                existing.setPrice(Integer.parseInt(availability.getPrice()) < Integer.parseInt(existing.getPrice()) ? availability.getPrice() : existing.getPrice());
            } else {
                map.put(code, new Availability(availability.getCode(), availability.getStock_name(), availability.getQuantity(), availability.getPrice()));
            }
        }

        return new ArrayList<>(map.values());
    }
}
