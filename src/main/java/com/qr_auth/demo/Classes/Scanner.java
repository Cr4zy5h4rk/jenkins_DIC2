package com.qr_auth.demo.Classes;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Scanner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
