package com.qr_auth.demo.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransfertTicketRequest {
    private String mailSender;
    private String mailReceiver;
    private Integer QttTicketDej;
    private Integer QttTicketPetitDej;
}
