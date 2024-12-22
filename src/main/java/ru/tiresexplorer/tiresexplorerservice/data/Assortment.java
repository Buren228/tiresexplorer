package ru.tiresexplorer.tiresexplorerservice.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public
class Assortment {
    private String code;
    private String p_full_name;
    private String p_brand;
//    private String p_model;
    private String p_width;
    private String p_height;
    private String p_diameter;
    //    private String p_load_index;
//    private String p_speed_index;
    private String p_season;
    private String p_category;
    //    private String p_xl;
    private Boolean p_mud_terrain;
    private Boolean p_all_terrain;
    private Boolean p_cargo;
    private Boolean p_thorn;
    private Boolean p_can_thorn;
    private Boolean p_runflat;
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
