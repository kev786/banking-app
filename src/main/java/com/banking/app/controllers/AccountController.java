package com.banking.app.controllers;

import com.banking.app.dtos.AccountDtos;
import com.banking.app.errors.ApiError;
import com.banking.app.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Comptes Bancaires", description = "Opérations sur les comptes bancaires")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(
            summary = "Créer un compte bancaire",
            description = "Crée un nouveau compte bancaire rattaché à une banque existante.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Compte créé avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides",
                            content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "404", description = "Banque introuvable",
                            content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "409", description = "Numéro de compte déjà existant",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    public ResponseEntity<AccountDtos.AccountResponse> createAccount(
            @Valid @RequestBody AccountDtos.CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(request));
    }

    @GetMapping
    @Operation(
            summary = "Lister tous les comptes",
            description = "Retourne la liste de tous les comptes de toutes les banques.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
            }
    )
    public ResponseEntity<List<AccountDtos.AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir un compte par ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Compte trouvé"),
                    @ApiResponse(responseCode = "404", description = "Compte introuvable",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    public ResponseEntity<AccountDtos.AccountResponse> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/bank/{bankId}")
    @Operation(
            summary = "Lister les comptes d'une banque",
            description = "Retourne tous les comptes appartenant à une banque donnée.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
                    @ApiResponse(responseCode = "404", description = "Banque introuvable",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    public ResponseEntity<List<AccountDtos.AccountResponse>> getAccountsByBank(
            @PathVariable Long bankId) {
        return ResponseEntity.ok(accountService.getAccountsByBank(bankId));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un compte",
            description = "Supprime définitivement un compte bancaire de la base de données.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Compte supprimé avec succès"),
                    @ApiResponse(responseCode = "404", description = "Compte introuvable",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
