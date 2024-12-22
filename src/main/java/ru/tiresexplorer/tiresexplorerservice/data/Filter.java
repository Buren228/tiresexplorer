package ru.tiresexplorer.tiresexplorerservice.data;

import lombok.Data;

@Data
public class Filter {
    private Integer width;
    private Integer height;
    private Integer diameter;
    private String brand;
    private String season;
    private String spikes;
    private boolean runflat;
    private boolean lightweight;
    private boolean mt;
    private boolean at;
}
