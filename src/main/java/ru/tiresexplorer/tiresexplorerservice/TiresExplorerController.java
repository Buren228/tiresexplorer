package ru.tiresexplorer.tiresexplorerservice;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tiresexplorer.tiresexplorerservice.data.Filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins ="*")
@RequestMapping("/")
public class TiresExplorerController {

    private final DataFetcher dataFetcher;

    @Autowired
    public TiresExplorerController(DataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    @PostMapping("/fetchData")
    public String fetchData(@RequestBody Filter filter) {
        return  dataFetcher.performData(filter);
    }

//    @GetMapping("/getBrands")
//    public List<String> getSearchSuggestions() throws IOException {
//        File file = new File("src/main/resources/static/brands.txt");
//
//        List<String> brands = new ArrayList<>();
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                brands.add(line);
//            }
//        }
//
//        return brands;
//    }

    @PostMapping("/getData")
    public List<String> getSearchSuggestions(@RequestBody String param) {
        List<String> data = new ArrayList<>();

        try {
            // Загрузка файла из classpath
            File file = new File("src/main/resources/static/"+ param +".txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    data.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + param, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + param, e);
        }

        return data;
    }
}
