package com.api.exercise.dto;

import com.api.exercise.utils.RegexForPaterns;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
public class UserInput {

    private String name;

    @Pattern(regexp = RegexForPaterns.EMAIL, message = "Email inválido")
    private String email;

    @Pattern(regexp = RegexForPaterns.PASSWORD, message = "El password debe incluir al menos 1 letras Mayúsculas, 1 letras minúsculas y 2 números")
    private String password;

    @Singular
    private List<PhoneInput> phones;
}
