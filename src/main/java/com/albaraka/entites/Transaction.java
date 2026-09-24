package main.java.com.albaraka.entites;

import java.time.LocalDate;

public record Transaction(
        Long id,
        LocalDate date,
        double montant,
        TypeTransaction type,
        String lieu,
        Long idCompte
        ) {
}
