package com.banking.app.dtos;

import com.banking.app.enums.AccountStatus;
import com.banking.app.enums.AccountType;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// ── Request DTOs ─────────────────────────────────────────────────────────────

public class AccountDtos {

    public record CreateAccountRequest(
            @NotBlank(message = "Le nom du titulaire est obligatoire")
            @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
            @Schema(description = "Nom complet du titulaire du compte", example = "Ryan kevin")
            String ownerName,

            @NotBlank(message = "L'email du titulaire est obligatoire")
            @Email(message = "L'email fourni est invalide")
            @Schema(description = "Adresse email de contact", example = "ryankevin@gmail.com")
            String ownerEmail,

            @NotNull(message = "Le solde initial est obligatoire")
            @DecimalMin(value = "0.0", message = "Le solde initial ne peut pas être négatif")
            @Schema(description = "Montant initial à déposer", example = "1000.00")
            BigDecimal initialBalance,

            @NotNull(message = "Le type de compte est obligatoire")
            @Schema(description = "Type de compte : CHECKING (Courant), SAVINGS (Épargne), BUSINESS (Professionnel)", example = "SAVINGS")
            AccountType accountType,

            @NotNull(message = "L'identifiant de la banque est obligatoire")
            @Positive(message = "L'identifiant de la banque doit être positif")
            @Schema(description = "ID de la banque partenaire", example = "1")
            Long bankId
    ) {}

    // ── Response DTOs ─────────────────────────────────────────────────────────

    public record AccountResponse(
            @Schema(description = "Identifiant unique du compte", example = "1")
            Long id,
            @Schema(description = "Numéro de compte généré", example = "BA0123456789")
            String accountNumber,
            @Schema(description = "Nom du titulaire", example = "Ryan kevin")
            String ownerName,
            @Schema(description = "Email du titulaire", example = "ryan.kevin@email.com")
            String ownerEmail,
            @Schema(description = "Solde actuel", example = "1000.00")
            BigDecimal balance,
            @Schema(description = "Type de compte", example = "SAVINGS")
            AccountType accountType,
            @Schema(description = "Statut actuel du compte", example = "ACTIVE")
            AccountStatus status,
            BankSummary bank,
            @Schema(description = "Date de création")
            LocalDateTime createdAt,
            @Schema(description = "Date de dernière mise à jour")
            LocalDateTime updatedAt
    ) {}

    public record BankSummary(
            Long id,
            String name,
            String code,
            String country
    ) {}
}
