package com.david.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SupplierRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    private String phone;

    @Size(max = 200)
    private String address;
}
