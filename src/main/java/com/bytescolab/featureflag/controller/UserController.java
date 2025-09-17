package com.bytescolab.featureflag.controller;


import com.bytescolab.featureflag.dto.auth.request.UserRoleUpdateRequestDTO;
import com.bytescolab.featureflag.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de roles de usuarios.
 * <p>
 * Expone endpoints relacionados con la administración de roles de usuarios dentro del sistema.
 * Actualmente, permite a un administrador modificar el rol de un usuario específico.
 * </p>
 *
 * <p>Características:</p>
 * <ul>
 *   <li>Todos los endpoints están bajo la ruta base <b>/api/users</b>.</li>
 *   <li>Requiere permisos de administrador ({@code hasRole('ADMIN')}).</li>
 *   <li>Valida los datos de entrada mediante anotaciones {@link jakarta.validation.Valid}.</li>
 * </ul>
 * @author Bytes
 * @see UserService
 * @see com.bytescolab.featureflag.dto.auth.request.UserRoleUpdateRequestDTO
 */
@RestController
@Tag(name = "Role", description = "Role endpoints")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor para inyectar el servicio de usuario.
     *
     * @param userService servicio encargado de la lógica de negocio relacionada con usuarios
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Actualiza el rol de un usuario identificado por su nombre.
     * <p>
     * Solo los usuarios con rol {@code ADMIN} pueden ejecutar esta operación.
     * </p>
     *
     * @param name    nombre del usuario al que se le quiere cambiar el rol
     * @param request DTO que contiene el nuevo rol a asignar
     * @return una respuesta HTTP 200 con un mensaje de confirmación
     */
    @PutMapping("/{name}/role")
    @Operation(summary = "Cambio rol", description = "Endpoint para el cambio de rol de un usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUserRole(@PathVariable String name,
                                                 @RequestBody @Valid UserRoleUpdateRequestDTO request) {
        return ResponseEntity.ok(userService.updateUserRole(name, request.getRole()));
    }
}
