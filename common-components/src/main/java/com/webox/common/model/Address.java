package com.webox.common.model;

import lombok.Data;

@Data
public class Address {
    private String postalCode;
    private String streetName;
    private String streetNumber;
    private String state;
    private String country;
    private String city;

}