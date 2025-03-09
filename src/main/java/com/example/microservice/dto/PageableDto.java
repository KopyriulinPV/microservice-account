package com.example.microservice.dto;

import lombok.Data;

@Data
public class PageableDto {
    private long offset;
    private int pageNumber;
    private int pageSize;
    private boolean paged;
    private boolean unpaged;
}
