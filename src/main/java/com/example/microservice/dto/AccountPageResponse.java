package com.example.microservice.dto;

import com.example.microservice.model.Account;
import lombok.Data;

import java.util.List;

@Data
public class AccountPageResponse {

    private List<Account> content;
    private boolean empty;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private PageableDto pageable;
    private long totalElements;
    private int totalPages;
    private int size;
    private int number;

}
