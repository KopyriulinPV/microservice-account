package com.microservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateDto {

    private String firstName;
    private String lastName;
    private LocalDateTime birthDate;
    private String phone;
    private String about;
    private String city;
    private String country;
    private String emojiStatus;
    private String photo;
    private String profileCover;

}
