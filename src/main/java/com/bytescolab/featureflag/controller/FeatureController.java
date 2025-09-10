package com.bytescolab.featureflag.controller;

import com.bytescolab.featureflag.dto.feature.FeatureDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDetailDTO;
import com.bytescolab.featureflag.dto.feature.FeatureEnableDTO;
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

    public FeatureController (FeatureService featureService){
        this.featureService= featureService;
    }

    @PostMapping
    @Operation(summary = "Crear nueva feature", description = "Crea una nueva Feature con su respectiva información de FeatureConfig")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<FeatureDetailDTO> createFeature(@Valid @RequestBody FeatureDetailDTO featureDetailDTO) {
        return ResponseEntity.ok(featureService.createFeature(featureDetailDTO));
    }

    @GetMapping
    @Operation(summary = "Listar todas las features", description = "Lista todas las features de todos los clientes y entornos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<FeatureDTO>> getAllFeatures(
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Listar una feature", description = "Lista una feature definida por su ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<FeatureDetailDTO> getFeatureById(@PathVariable UUID id){
        return ResponseEntity.ok(featureService.getFeatureById(id));
    }

    @PostMapping("/{id}/enable")
    @Operation(summary = "Activar feature", description = "Se activa una feature por su ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> enableFeature(@PathVariable UUID id, @RequestBody FeatureEnableDTO req) {
        return ResponseEntity.ok(featureService.enableFeatureForClientOrEnv(id, req.getEnvironment(), req.getClientId()));
    }

    @PostMapping("/{id}/disable")
    @Operation(summary = "Desactivar una feature", description = "Se desactiva una feature por su ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> disableFeature(@PathVariable UUID id, @RequestBody FeatureEnableDTO req) {
        return ResponseEntity.ok(featureService.disableFeatureForClientOrEnv(id, req.getClientId(), req.getEnvironment() ));
    }

    @GetMapping("/check")
    @Operation(summary = "Comprobar una feature", description = "Comprobar si una feature está activa o no")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Boolean> isFeatureActived(@RequestParam UUID featureId, @RequestParam String clientId, @RequestParam String env) {
        return ResponseEntity.ok(featureService.isFeatureActived(featureId, clientId, env));
    }

}
