package com.bytescolab.featureflag.controller;

import com.bytescolab.featureflag.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.model.enums.Environment;
import com.bytescolab.featureflag.service.feature.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/features")
@Tag(name = "Feature", description = "Feature endpoints")
public class FeatureController {

    private final FeatureService featureService;

    public FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @PostMapping
    @Operation(summary = "Crear nueva feature", description = "Crea una nueva Feature con su respectiva información de FeatureConfig")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<FeatureDetailResponseDTO> createFeature(@Valid @RequestBody FeatureCreateRequestDTO dto) {
        return ResponseEntity.ok(featureService.createFeature(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todas las features", description = "Lista todas las features de todos los clientes y entornos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")

    public ResponseEntity<List<FeatureSummaryResponseDTO>> getAllFeatures(
            @RequestParam (required=false) Boolean enabled,
            @RequestParam (required=false) String name) {
        return ResponseEntity.ok(featureService.getAllFeatures(enabled, name));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Listar una feature", description = "Lista una feature definida por su ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<FeatureDetailResponseDTO> getFeatureById(@PathVariable UUID id) {
        return ResponseEntity.ok(featureService.getFeatureById(id));
    }

    @PostMapping("/{id}/enable")
    @Operation(summary = "Activar feature", description = "Se activa una feature por su ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> enableFeature(@PathVariable UUID id,
                                                @Valid @RequestBody FeatureActivationRequestDTO req) {
        return ResponseEntity.ok(featureService.enableFeatureForClientOrEnv(id, req));
    }

    @PostMapping("/{id}/disable")
    @Operation(summary = "Desactivar feature", description = "Se desactiva una feature por su ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> disableFeature(@PathVariable UUID id,
                                                 @Valid @RequestBody FeatureActivationRequestDTO req) {
        return ResponseEntity.ok(featureService.disableFeatureForClientOrEnv(id, req));
    }

    @GetMapping("/check")
    @Operation(summary = "Comprobar una feature", description = "Comprobar si una feature está activa o no")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Boolean> isFeatureActived(@RequestParam UUID featureId,
                                                    @RequestParam String clientId,
                                                    @RequestParam Environment env) {
        return ResponseEntity.ok(featureService.isFeatureActived(featureId, clientId, env));
    }
}
