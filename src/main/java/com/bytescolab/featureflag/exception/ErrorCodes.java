package com.bytescolab.featureflag.exception;

public final class ErrorCodes {
    // Auth/JWT errors
    public static final String TOKEN_EXPIRADO = "AUTH_001";
    public static final String TOKEN_EXPIRADO_MSG = "El token ha expirado";

    public static final String TOKEN_MALFORMADO = "AUTH_002";
    public static final String TOKEN_MALFORMADO_MSG = "Token mal formado";

    public static final String TOKEN_INVALIDO = "AUTH_003";
    public static final String TOKEN_INVALIDO_MSG = "Token inválido";

    // Feature errors (agrega más según necesites)
    public static final String FEATURE_NOT_FOUND = "FF_001";
    public static final String FEATURE_NOT_FOUND_MSG = "Feature no encontrada";

    // User errors
    public static final String USER_NOT_FOUND = "USER_001";
    public static final String USER_NOT_FOUND_MSG = "Usuario no encontrado";

    private ErrorCodes() {}
}
