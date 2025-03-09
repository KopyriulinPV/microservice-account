package com.example.microservice.dto;

import com.example.microservice.model.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String photo;
    private String profileCover;
    private String about;
    private String city;
    private String country;
    private String statusCode;
    private ZonedDateTime regDate;
    private ZonedDateTime birthDate;
    private String messagePermission;
    private ZonedDateTime lastOnlineTime;
    private String emojiStatus;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
    private ZonedDateTime deletionTimestamp;
    private Boolean deleted;
    private Boolean blocked;
    private Boolean isOnline;
    private Set<Role> roles;
}
