package ru.tiresexplorer.tiresexplorerservice;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tiresexplorer.tiresexplorerservice.data.Filter;

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
}
