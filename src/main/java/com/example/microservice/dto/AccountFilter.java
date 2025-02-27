package com.example.microservice.dto;

import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountFilter {

    private Integer size;
    private Integer page;
    private String author;
    private String ids;
    private String firstName;
    private String lastName;
    private Integer ageFrom;
    private Integer ageTo;
    private String country;
    private String city;
    private String statusCode;
    private Boolean deleted;

}
