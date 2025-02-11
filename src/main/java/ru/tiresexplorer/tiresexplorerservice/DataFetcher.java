package ru.tiresexplorer.tiresexplorerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tiresexplorer.tiresexplorerservice.data.Assortment;
import ru.tiresexplorer.tiresexplorerservice.data.AssortmentData;
import ru.tiresexplorer.tiresexplorerservice.data.Availability;
import ru.tiresexplorer.tiresexplorerservice.data.AvailabilityData;
import ru.tiresexplorer.tiresexplorerservice.data.Filter;
import ru.tiresexplorer.tiresexplorerservice.singleton.Cash;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DataFetcher {
    private static final String ASSORTMENT_URL = "https://lk.selectyre.ru/export/json-assortment/";
    private static final String AVAILABILITY_URL = "https://lk.selectyre.ru/export/json-availability/";
    //private static final String STOCKS_URL = "https://lk.selectyre.ru/export/json-stocks/";

    @Value("${api.uuid}")
    private String uuid;

    public void fetchData() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        Cash cash = Cash.getInstance();

        try {
            // Fetch assortment data
            request = HttpRequest.newBuilder()
                    .uri(URI.create(ASSORTMENT_URL + "?uuid=" + uuid))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            cash.setAssortment(performAssortment(response.body()));
        } catch (IOException | InterruptedException e) {
            System.out.println("Произошла ошибка при выгрузке ассортимента \n" + e.getMessage());
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
            System.out.println("Произошла ошибка при выгрузке остатков \n" + e.getMessage());
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
        if (cash.getAvailability() == null || cash.getAssortment() == null) {
            System.out.println("Ничего не выгрузилось, что-то пошло не так");
        }

        updateData("brands");
        updateData("width");
        updateData("height");
        updateData("diameter");
    }

    private void updateData(String txt) {
        Cash cash = Cash.getInstance();

        try {
            File data = new File("src/main/resources/static/" + txt  + ".txt");

            Set<String> uniqueData = new HashSet<>();

            switch (txt) {
                case "brands":
                    for (Assortment assortment : cash.getAssortment()) {
                        uniqueData.add(assortment.getBrand());
                    }
                    break;
                case "width":
                    for (Assortment assortment : cash.getAssortment()) {
                        uniqueData.add(toIntFormat(assortment.getWidth()));
//                        uniqueData.add(assortment.getP_width());
                    }
                    break;
                case "height":
                    for (Assortment assortment : cash.getAssortment()) {
                        uniqueData.add(toIntFormat(assortment.getHeight()));
                    }
                    break;
                case "diameter":
                    for (Assortment assortment : cash.getAssortment()) {
                        uniqueData.add(toIntFormat(assortment.getDiameter()));
                    }
                    break;

            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(data, false))) {
                for (String unique : uniqueData) {
                    writer.write(unique);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.printf("Файл %s с брендами не найден или его не существует", txt);
        }
    }

    private String toIntFormat(String str) {
        return str.substring(0, str.indexOf("."));
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

        assortments.removeIf(x -> x.getRunflat() != filter.isRunflat() ||
                x.getCargo() != filter.isLightweight() ||
                x.getMudTerrain() != filter.isMt() ||
                x.getAllTerrain() != filter.isAt()
        );
        if (filter.getWidth() != null) {
            assortments.removeIf(x -> !toIntFormat(x.getWidth()).equals(filter.getWidth().toString()));
        }
        if (filter.getHeight() != null) {
            assortments.removeIf(x -> !toIntFormat(x.getHeight()).equals(filter.getHeight().toString()));
        }
        if (filter.getDiameter() != null) {
            assortments.removeIf(x -> !toIntFormat(x.getDiameter()).equals(filter.getDiameter().toString()));
        }
        if (!filter.getSeason().equals("")) {
            assortments.removeIf(x -> !x.getSeason().equals(filter.getSeason()));
        }
        if (!filter.getBrand().equals("")) {
            assortments.removeIf(x -> !x.getBrand().equals(filter.getBrand()));
        }

        switch (filter.getSpikes()) {
            case "Шип.":
                assortments.removeIf(x -> !x.getThorn());
                break;
            case "Нешип.":
                assortments.removeIf(Assortment::getThorn);
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
                        result.append(x.getFullName()).append(" - ");
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
        } else {
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
