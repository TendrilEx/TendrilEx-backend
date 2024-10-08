package com.example.parcel_delivery.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerResDTO {

    private Long userId;
    private Long CustomerId;
    private String username;
    private String city;

}
