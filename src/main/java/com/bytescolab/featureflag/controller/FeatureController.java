package com.bytescolab.featureflag.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feature")
public class FeatureController {

    //Crear nueva feature
    //@PostMapping createFeature ("/api/features")
    //FIN Crear nueva feature

    //Listar features
    //@GetMapping findAllFeatures ("/api/features")
    //FIN Listar features

    //Detalle de feature
    //@GetMapping findDetailFeature ("/api/features/{id}")
    //FIN Detalle de feature

    //Activar feature para cliente/entorno
    //@PostMapping activeFeature ("/api/features/{id}/enable")
    //FIN Activar feature para cliente/entorno

    //Desactivar feature para cliente/entorno
    //@PostMapping disableFeature ("/api/features/{id}/disable")
    //FIN Desactivar feature para cliente/entorno

    //Verificar si una feature está activa
    //@GetMapping verifyFeature ("/api/features/check")

}
