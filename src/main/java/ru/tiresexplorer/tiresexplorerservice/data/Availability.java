package ru.tiresexplorer.tiresexplorerservice.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Availability {
    private String code;
    private String stock_name;
    //private String stock_name_ru;
    //private String wholesale_price;
    private Integer quantity;
//    private String recommended_retail_price;
//    private String minimal_internet_price;
   // private String provider_key;
    //private Integer year;
    //private Boolean sale;
    private String price;

    public Availability(){}

    public Availability(String code, String stock_name, Integer quantity, String price) {
        this.code = code;
        this.stock_name = stock_name;
        this.quantity = quantity;
        this.price = price;
    }
    //private Integer delivery_time;
   // private String cae;

}
