package com.api.exercise.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ApiError {
  @JsonProperty("mensaje")
  private String message;
}
