package ru.tiresexplorer.tiresexplorerservice.singleton;

import lombok.Data;
import ru.tiresexplorer.tiresexplorerservice.data.Assortment;
import ru.tiresexplorer.tiresexplorerservice.data.Availability;


import java.time.LocalDateTime;
import java.util.List;

@Data
public class Cash {

    private static Cash instance;
    private List<Assortment> assortment;
    private List<Availability> availability;
    private LocalDateTime lastUpdated;

    public static Cash getInstance() {
        if (instance == null) {
            instance = new Cash();
        }
        return instance;
    }


}
