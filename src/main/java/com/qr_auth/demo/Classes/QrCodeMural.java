package com.qr_auth.demo.Classes;

import com.qr_auth.demo.enums.ERepas;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "qr_code_mural")
public class QrCodeMural {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERepas typeQr;
}
