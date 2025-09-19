package com.bytescolab.featureflag.controller;

import com.bytescolab.featureflag.repository.dto.feature.request.FeatureConfigCreateRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureCreateRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.request.FeatureActivationRequestDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureConfigResponseDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureDetailResponseDTO;
import com.bytescolab.featureflag.repository.dto.feature.response.FeatureSummaryResponseDTO;
import com.bytescolab.featureflag.model.enums.Environment;
import com.bytescolab.featureflag.service.feature.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la gestión de <b>features</b> en el sistema FeatureFlag API.
 * <p>
 * Expone endpoints para la creación, activación, desactivación, consulta y verificación
 * de <i>feature flags</i> en distintos entornos y clientes.
 * </p>
 *
 * <p><b>Endpoints principales:</b></p>
 * <ul>
 *   <li><b>POST /api/features</b>: Crea una nueva feature.</li>
 *   <li><b>GET /api/features</b>: Lista todas las features disponibles, con filtros opcionales.</li>
 *   <li><b>GET /api/features/{id}</b>: Obtiene los detalles de una feature por ID.</li>
 *   <li><b>POST /api/features/{id}/enable</b>: Activa una feature para un cliente o entorno específico.</li>
 *   <li><b>POST /api/features/{id}/disable</b>: Desactiva una feature para un cliente o entorno específico.</li>
 *   <li><b>GET /api/features/check</b>: Verifica si una feature está activa para un cliente y entorno.</li>
 * </ul>
 *
 * <p><b>Seguridad:</b></p>
 * <ul>
 *   <li>Los endpoints de administración (crear, activar y desactivar features) requieren rol <b>ADMIN</b>.</li>
 *   <li>Las consultas (listar y verificar estado) pueden ser accedidas por roles <b>ADMIN</b> y <b>USER</b>.</li>
 * </ul>
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.service.feature.FeatureService
 * @see FeatureCreateRequestDTO
 * @see FeatureActivationRequestDTO
 * @see FeatureDetailResponseDTO
 * @see FeatureSummaryResponseDTO
 */
@RestController
@RequestMapping("/api/features")
@Tag(name = "Feature", description = "Feature endpoints")
public class FeatureController {

    private final FeatureService featureService;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param featureService servicio encargado de la lógica de negocio relacionada con las features.
     */
    public FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    /**
     * Crea una nueva feature en el sistema.
     *
     * @param dto datos de la feature a crear.
     * @return {@link ResponseEntity} con los detalles de la feature creada y estado {@code 201 Created}.
     */
    @PostMapping
    @Operation(summary = "Crear nueva feature", description = "Crea una nueva Feature con su respectiva información de FeatureConfig")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<FeatureDetailResponseDTO> createFeature(@Valid @RequestBody FeatureCreateRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(featureService.createFeature(dto));
    }

    /**
     * Crea una nueva feature en el sistema.
     *
     * @param dto datos de la feature a crear.
     * @return {@link ResponseEntity} con los detalles de la feature creada y estado {@code 201 Created}.
     */
    @PostMapping("/{id}")
    @Operation(summary = "Crear nueva config para una feature", description = "Crea una nueva config para una Feature")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<FeatureConfigResponseDTO> createFeature(@PathVariable UUID id, @Valid @RequestBody FeatureConfigCreateRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(featureService.createConfigFeature(id, dto));
    }

    /**
     * Lista todas las features disponibles, con posibilidad de filtrar por nombre y estado.
     *
     * @param enabled valor opcional para filtrar por estado (activada o no por defecto).
     * @param name    valor opcional para filtrar por nombre.
     * @return lista de {@link FeatureSummaryResponseDTO}, o {@code 204 No Content} si no existen resultados.
     */
    @GetMapping
    @Operation(summary = "Listar todas las features", description = "Lista todas las features de todos los clientes y entornos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<FeatureSummaryResponseDTO>> getAllFeatures(
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) String name) {
        var featureList = featureService.getAllFeatures(enabled, name);
        if (featureList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(featureList);
    }

    /**
     * Obtiene los detalles de una feature específica.
     *
     * @param id identificador único de la feature.
     * @return {@link FeatureDetailResponseDTO} con información completa de la feature.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Listar una feature", description = "Lista una feature definida por su ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<FeatureDetailResponseDTO> getFeatureById(@PathVariable UUID id) {
        return ResponseEntity.ok(featureService.getFeatureById(id));
    }

    /**
     * Activa una feature para un cliente o entorno concreto.
     *
     * @param id  identificador único de la feature.
     * @param req objeto con la información del cliente y entorno.
     * @return mensaje de confirmación de activación.
     */
    @PostMapping("/{id}/enable")
    @Operation(summary = "Activar feature", description = "Se activa una feature por su ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> enableFeature(@PathVariable UUID id,
                                                @Valid @RequestBody FeatureActivationRequestDTO req) {
        return ResponseEntity.ok(featureService.enableFeatureForClientOrEnv(id, req));
    }

    /**
     * Desactiva una feature para un cliente o entorno concreto.
     *
     * @param id  identificador único de la feature.
     * @param req objeto con la información del cliente y entorno.
     * @return mensaje de confirmación de desactivación.
     */
    @PostMapping("/{id}/disable")
    @Operation(summary = "Desactivar feature", description = "Se desactiva una feature por su ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> disableFeature(@PathVariable UUID id,
                                                 @Valid @RequestBody FeatureActivationRequestDTO req) {
        return ResponseEntity.ok(featureService.disableFeatureForClientOrEnv(id, req));
    }

    /**
     * Verifica si una feature está activa para un cliente en un entorno específico.
     *
     * @param featureId identificador único de la feature.
     * @param clientId  identificador del cliente.
     * @param env       entorno a verificar.
     * @return {@code true} si la feature está activa, {@code false} en caso contrario.
     */
    @GetMapping("/check")
    @Operation(summary = "Comprobar una feature", description = "Comprobar si una feature está activa o no")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Boolean> isFeatureActived(@RequestParam UUID featureId,
                                                    @RequestParam String clientId,
                                                    @RequestParam Environment env) {
        return ResponseEntity.ok(featureService.isFeatureActived(featureId, clientId, env));
    }
}
