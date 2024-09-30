package com.seweryn.spring_6_restmvc_api.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private String customerName;
    private String email;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
