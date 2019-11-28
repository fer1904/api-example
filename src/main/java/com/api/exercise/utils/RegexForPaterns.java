package com.api.exercise.utils;

public interface RegexForPaterns {
  String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=(?:.*?[0-9]){2})[a-zA-Z\\d]{4,}$";
  String EMAIL = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
}
