package com.bytescolab.featureflag.controller;

import com.bytescolab.featureflag.dto.feature.FeatureDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDetailDTO;
import com.bytescolab.featureflag.dto.feature.FeatureEnableDTO;
import com.bytescolab.featureflag.service.feature.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/feature")
@Tag(name = "Feature", description = "Feature endpoints")
@RequiredArgsConstructor
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    //Listar todas las features
    @GetMapping
    @Operation(summary = "Listar todas las features", description = "Lista todas las features de todos los clientes y entornos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<FeatureDTO>> getAllFeatures(){
        List<FeatureDTO> features = featureService.getAllFeatures();
        return ResponseEntity.ok(features);
    }

    //Listar feature por id
    @GetMapping("/{id}")
    @Operation(summary = "Listar una feature", description = "Lista una feature definida por su ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<FeatureDetailDTO> getFeatureById(@PathVariable UUID id){
        FeatureDetailDTO feature = featureService.getFeatureById(id);
        return ResponseEntity.ok(feature);
    }

    @PostMapping("/{id}/enable")
    @Operation(summary = "Activar feature", description = "Se activa una feature por su ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> enableFeature(@PathVariable UUID id, @RequestBody FeatureEnableDTO req) {
        featureService.enableFeatureForClientOrEnv(id, req.getEnvironment(), req.getClientId());
        return ResponseEntity.ok("Feature activada correctamente.");
    }

    @PostMapping("/{id}/disable")
    @Operation(summary = "Desactivar una feature", description = "Se desactiva una feature por su ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> disableFeature(@PathVariable UUID id, @RequestBody FeatureEnableDTO req) {
        featureService.disableFeatureForClientOrEnv(id, req.getEnvironment(), req.getClientId());
        return ResponseEntity.ok("Feature desactivada correctamente.");
    }

    @GetMapping("/check")
    @Operation(summary = "Comprobar una feature", description = "Comprobar si una feature está activa o no")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Boolean> isFeatureActived(@RequestParam UUID featureId, @RequestParam String clientId, @RequestParam String env) {
        boolean isEnabled = featureService.isFeatureActived(featureId, clientId, env);
        return ResponseEntity.ok(isEnabled);
    }

}
