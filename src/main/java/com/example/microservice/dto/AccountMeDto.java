package com.example.microservice.dto;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMeDto {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String photo;
    private String profileCover;
    private String about;
    private String city;
    private String country;
    private String statusCode;
    private String regDate;
    private String birthDate;
    private String messagePermission;
    private String lastOnlineTime;
    private String emojiStatus;
    private String createdOn;
    private String updatedOn;
    private String deletionTimestamp;
    private Boolean deleted;
    private Boolean blocked;
    private Boolean isOnline;
    private Set<String> roles;

}
