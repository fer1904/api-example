package com.api.exercise.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneInput {
    private String number;
    private String cityCode;
    private String countryCode;
}
