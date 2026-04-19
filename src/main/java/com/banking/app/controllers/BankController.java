package com.banking.app.controllers;

import com.banking.app.dtos.BankDtos;
import com.banking.app.errors.ApiError;
import com.banking.app.services.BankService;
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
@RequestMapping("/api/v1/banks")
@RequiredArgsConstructor
@Tag(name = "Banques", description = "Gestion des banques partenaires")
public class BankController {

    private final BankService bankService;

    @PostMapping
    @Operation(
            summary = "Créer une banque",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Banque créée avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides",
                            content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "409", description = "Code banque déjà existant",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    public ResponseEntity<BankDtos.BankResponse> createBank(
            @Valid @RequestBody BankDtos.CreateBankRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bankService.createBank(request));
    }

    @GetMapping
    @Operation(
            summary = "Lister toutes les banques",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
            }
    )
    public ResponseEntity<List<BankDtos.BankResponse>> getAllBanks() {
        return ResponseEntity.ok(bankService.getAllBanks());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une banque par ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Banque trouvée"),
                    @ApiResponse(responseCode = "404", description = "Banque introuvable",
                            content = @Content(schema = @Schema(implementation = ApiError.class)))
            }
    )
    public ResponseEntity<BankDtos.BankResponse> getBankById(@PathVariable Long id) {
        return ResponseEntity.ok(bankService.getBankById(id));
    }
}
