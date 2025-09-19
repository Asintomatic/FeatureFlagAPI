package com.bytescolab.featureflag.utils.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FeatureAuditLogger {

    private static final Logger featureLogger = LoggerFactory.getLogger("FeatureLogger");

    private FeatureAuditLogger(){}
    private LocalDateTime now = LocalDateTime.now();

    public void logCreation(String featureName, String user) {
        featureLogger.info("Feature creada | Nombre: '{}' | Usuario: '{}', Fecha: '{}'", featureName, user, now);
    }

    public void logActivation(String featureName, String env, String client, String user){
        featureLogger.info("Feature activada | Nombre: '{}' | Entorno: '{}' | Cliente: '{}' | Usuario: '{}', Fecha: '{}'",featureName, env, client, user, now);
    }

    public void logDesactivation(String featureName, String env, String client, String user){
        featureLogger.info("Feature desactivada | Nombre: '{}' | Entorno: '{}' | Cliente: '{}' | Usuario: '{}, Fecha: '{}''",featureName, env, client, user, now);
    }
}
