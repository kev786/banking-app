package com.banking.app.dtos;

import com.banking.app.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDtos {

    public record DepositRequest(
            @NotNull(message = "L'ID du compte est obligatoire")
            @Schema(description = "ID du compte destinataire", example = "1")
            Long accountId,

            @NotNull(message = "Le montant est obligatoire")
            @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
            @Schema(description = "Montant à déposer", example = "500.00")
            BigDecimal amount,

            @Schema(description = "Description de l'opération", example = "Dépôt espèces")
            String description
    ) {}

    public record WithdrawRequest(
            @NotNull(message = "L'ID du compte est obligatoire")
            @Schema(description = "ID du compte source", example = "1")
            Long accountId,

            @NotNull(message = "Le montant est obligatoire")
            @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
            @Schema(description = "Montant à retirer", example = "200.00")
            BigDecimal amount,

            @Schema(description = "Description de l'opération", example = "Retrait distributeur")
            String description
    ) {}

    public record TransferRequest(
            @NotNull(message = "L'ID du compte source est obligatoire")
            @Schema(description = "ID du compte qui envoie l'argent", example = "1")
            Long sourceAccountId,

            @NotNull(message = "L'ID du compte destination est obligatoire")
            @Schema(description = "ID du compte qui reçoit l'argent", example = "2")
            Long destinationAccountId,

            @NotNull(message = "Le montant est obligatoire")
            @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
            @Schema(description = "Montant à transférer", example = "100.00")
            BigDecimal amount,

            @Schema(description = "Description du transfert", example = "Cadeau anniversaire")
            String description
    ) {}

    public record TransactionResponse(
            @Schema(description = "ID de la transaction", example = "10")
            Long id,
            @Schema(description = "Montant net", example = "100.00")
            BigDecimal amount,
            @Schema(description = "Frais appliqués", example = "2.50")
            BigDecimal fee,
            @Schema(description = "Type d'opération", example = "TRANSFER")
            TransactionType type,
            @Schema(description = "Description", example = "Cadeau anniversaire")
            String description,
            @Schema(description = "Date et heure")
            LocalDateTime timestamp,
            @Schema(description = "Numéro du compte source")
            String sourceAccountNumber,
            @Schema(description = "Numéro du compte destination")
            String destinationAccountNumber
    ) {}
}
