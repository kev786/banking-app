package com.banking.app.controllers;

import com.banking.app.dtos.TransactionDtos;
import com.banking.app.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "API pour les opérations bancaires (Dépôts, Retraits, Virements)")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    @Operation(summary = "Effectuer un dépôt", description = "Crédite le compte spécifié du montant indiqué")
    public ResponseEntity<TransactionDtos.TransactionResponse> deposit(@Valid @RequestBody TransactionDtos.DepositRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.deposit(request));
    }

    @PostMapping("/withdraw")
    @Operation(summary = "Effectuer un retrait", description = "Débite le compte spécifié si le solde est suffisant")
    public ResponseEntity<TransactionDtos.TransactionResponse> withdraw(@Valid @RequestBody TransactionDtos.WithdrawRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.withdraw(request));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Effectuer un virement", description = "Transfère de l'argent d'un compte source vers un compte destination")
    public ResponseEntity<TransactionDtos.TransactionResponse> transfer(@Valid @RequestBody TransactionDtos.TransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.transfer(request));
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Historique des transactions", description = "Récupère toutes les transactions liées à un compte (source ou destination)")
    public ResponseEntity<List<TransactionDtos.TransactionResponse>> getAccountHistory(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getAccountHistory(accountId));
    }
}
