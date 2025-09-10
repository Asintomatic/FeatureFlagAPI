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


import java.util.UUID;

@RestController
@Tag(name = "Role", description = "Role endpoints")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController (UserService userService){
        this.userService= userService;
    }

    @PutMapping("/{userId}/role")
    @Operation(summary = "Cambio rol", description = "Endpoint para el cambio de rol de un usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUserRole (@PathVariable UUID userId, @RequestBody @Valid UserRoleUpdateRequestDTO request){
    return ResponseEntity.ok(userService.updateUserRole(userId, request.getRole()));
    }

}
