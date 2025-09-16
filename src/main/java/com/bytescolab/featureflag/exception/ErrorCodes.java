package com.bytescolab.featureflag.exception;

public final class ErrorCodes {
    // Auth/JWT errors
    public static final String TOKEN_EXPIRADO = "AUTH_001";
    public static final String TOKEN_EXPIRADO_MSG = "El token ha expirado";

    public static final String TOKEN_MALFORMADO = "AUTH_002";
    public static final String TOKEN_MALFORMADO_MSG = "Token mal formado";

    public static final String TOKEN_INVALIDO = "AUTH_003";
    public static final String TOKEN_INVALIDO_MSG = "Token inválido";

    public static final String INVALID_CREDENTIALS = "AUTH_004";
    public static final String INVALID_CREDENTIALS_MSG ="Credenciales no válidas";

    // Feature errors (agrega más según necesites)
    public static final String FEATURE_NOT_FOUND = "FF_001";
    public static final String FEATURE_NOT_FOUND_MSG = "Feature no encontrada";

    public static final String FEATURE_EXISTS = "FF_002";
    public static final String FEATURE_EXISTS_MSG = "La feature ya existe";

    // User errors
    public static final String USER_NOT_FOUND = "USER_001";
    public static final String USER_NOT_FOUND_MSG = "Usuario no encontrado";

    public static final String USER_EXISTS = "USER_002";
    public static final String USER_EXISTS_MSG = "Nombre de Usuario ya existe";

    //Genericos
    public static final String BAD_PARAMS = "PARAMS_001";
    public static final String BAD_PARAMS_MSG = "Parámetros no aceptados";

    private ErrorCodes() {}
}
