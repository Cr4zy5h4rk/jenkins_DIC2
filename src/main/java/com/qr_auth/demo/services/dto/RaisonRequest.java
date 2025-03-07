package com.qr_auth.demo.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaisonRequest {

    private String raison;
    private Long scanId;
}
