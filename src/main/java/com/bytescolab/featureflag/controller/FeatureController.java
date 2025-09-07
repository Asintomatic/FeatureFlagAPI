package com.bytescolab.featureflag.controller;

import com.bytescolab.featureflag.dto.feature.FeatureDTO;
import com.bytescolab.featureflag.dto.feature.FeatureDetailDTO;
import com.bytescolab.featureflag.service.feature.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/feature")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    //Listar todas las features
    @GetMapping
    public ResponseEntity<List<FeatureDTO>> getAllFeatures(){
        List<FeatureDTO> features = featureService.getAllFeatures();
        return ResponseEntity.ok(features);
    }

    //Listar feature por id
    @GetMapping("/{id}")
    public ResponseEntity<FeatureDetailDTO> getFeatureById(@PathVariable UUID id){
        FeatureDetailDTO feature = featureService.getFeatureById(id);
        return ResponseEntity.ok(feature);
    }

}
