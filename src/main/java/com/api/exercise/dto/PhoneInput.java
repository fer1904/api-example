package com.api.exercise.dto;

import lombok.Data;

@Data
public class PhoneInput {
    private String number;
    private String cityCode;
    private String countryCode;
}
