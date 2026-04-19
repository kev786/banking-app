package com.banking.app.dtos;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

public class BankDtos {

    public record CreateBankRequest(
            @NotBlank(message = "Le nom de la banque est obligatoire")
            @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
            @Schema(description = "Nom de l'institution bancaire", example = "Banque Centrale")
            String name,

            @NotBlank(message = "Le code de la banque est obligatoire")
            @Size(min = 3, max = 20, message = "Le code doit contenir entre 3 et 20 caractères")
            @Schema(description = "Code unique de la banque", example = "BC001")
            String code,

            @Size(max = 255)
            @Schema(description = "Adresse du siège social", example = "123 Rue de la Finance")
            String address,

            @Size(max = 50)
            @Schema(description = "Pays d'origine", example = "France")
            String country
    ) {}

    public record BankResponse(
            @Schema(description = "ID unique de la banque", example = "1")
            Long id,
            @Schema(description = "Nom de la banque", example = "Banque Centrale")
            String name,
            @Schema(description = "Code de la banque", example = "BC001")
            String code,
            @Schema(description = "Adresse", example = "123 Rue de la Finance")
            String address,
            @Schema(description = "Pays", example = "France")
            String country,
            @Schema(description = "Nombre de comptes rattachés", example = "5")
            int accountCount
    ) {}
}
