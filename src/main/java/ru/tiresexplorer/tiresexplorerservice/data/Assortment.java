package ru.tiresexplorer.tiresexplorerservice.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public
class Assortment {
    @JsonProperty("code")
    private String code;

    @JsonProperty("p_full_name")
    private String fullName;

    @JsonProperty("p_brand")
    private String brand;

    @JsonProperty("p_width")
    private String width;

    @JsonProperty("p_height")
    private String height;

    @JsonProperty("p_diameter")
    private String diameter;

    @JsonProperty("p_season")
    private String season;

    @JsonProperty("p_category")
    private String category;

    @JsonProperty("p_mud_terrain")
    private Boolean mudTerrain;

    @JsonProperty("p_all_terrain")
    private Boolean allTerrain;

    @JsonProperty("p_cargo")
    private Boolean cargo;

    @JsonProperty("p_thorn")
    private Boolean thorn;

    @JsonProperty("p_can_thorn")
    private Boolean canThorn;

    @JsonProperty("p_runflat")
    private Boolean runflat;
//    private String p_protection;
//    private String  p_omologation;
//    private String p_side;
//    private String p_axis;
//    private Integer p_layering;
//    private String p_appointment;
//    private String p_info_last_modified;
//    private String p_photo_last_modified;
//    private String p_photo;
//    private String p_shipping_weight;
//    private String p_shipping_volume;
//    private String p_shipping_length;
//    private String p_shipping_width;
//    private String p_shipping_height;
//    @JsonIgnore
//    private String p_completeness;
//    @JsonIgnore
//    private String p_design;
}
