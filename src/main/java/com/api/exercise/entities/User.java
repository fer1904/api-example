package com.api.exercise.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    private String name;

    //@Pattern(regexp = RegexForPaterns.EMAIL, message = "Invalid Email")
    private String email;

    //@Pattern(regexp = RegexForPaterns.PASSWORD, message = "Invalid password")
    private String password;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    private List<Phone> phones;
}
