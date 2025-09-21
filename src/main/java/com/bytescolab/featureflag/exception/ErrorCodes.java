package com.bytescolab.featureflag.exception;
/**
 * Clase de utilidades que centraliza los códigos y mensajes de error
 * utilizados en la aplicación Feature Flag API.
 *
 * <p>Contiene constantes estáticas que representan diferentes categorías
 * de errores (autenticación, features, usuarios y parámetros genéricos).</p>
 *
 * <p>Los códigos de error están organizados con prefijos para facilitar
 * su identificación:</p>
 * <ul>
 *   <li><b>AUTH_XXX</b>: errores relacionados con autenticación y JWT.</li>
 *   <li><b>FF_XXX</b>: errores relacionados con la gestión de features.</li>
 *   <li><b>USER_XXX</b>: errores relacionados con usuarios.</li>
 *   <li><b>PARAMS_XXX</b>: errores de parámetros inválidos.</li>
 * </ul>
 *
 * Esta clase no puede instanciarse ya que está declarada como {@code final}
 * y posee un constructor privado.
 *
 * @author Bytes
 * @see com.bytescolab.featureflag.exception.ApiException
 */
public final class ErrorCodes {

    // ======================
    // Auth/JWT errors
    // ======================

    /** Código de error cuando el token JWT ha expirado. */
    public static final String TOKEN_EXPIRADO = "AUTH_001";
    /** Mensaje descriptivo para {@link #TOKEN_EXPIRADO}. */
    public static final String TOKEN_EXPIRADO_MSG = "El token ha expirado";

    /** Código de error cuando el token JWT tiene un formato inválido. */
    public static final String TOKEN_MALFORMADO = "AUTH_002";
    /** Mensaje descriptivo para {@link #TOKEN_MALFORMADO}. */
    public static final String TOKEN_MALFORMADO_MSG = "Token mal formado";

    /** Código de error cuando el token JWT no es válido. */
    public static final String TOKEN_INVALIDO = "AUTH_003";
    /** Mensaje descriptivo para {@link #TOKEN_INVALIDO}. */
    public static final String TOKEN_INVALIDO_MSG = "Token inválido";

    /** Código de error para credenciales incorrectas. */
    public static final String INVALID_CREDENTIALS = "AUTH_004";
    /** Mensaje descriptivo para {@link #INVALID_CREDENTIALS}. */
    public static final String INVALID_CREDENTIALS_MSG ="Credenciales no válidas";

    // ======================
    // Feature errors
    // ======================

    /** Código de error cuando no se encuentra una feature. */
    public static final String FEATURE_NOT_FOUND = "FF_001";
    /** Mensaje descriptivo para {@link #FEATURE_NOT_FOUND}. */
    public static final String FEATURE_NOT_FOUND_MSG = "Feature no encontrada";

    /** Código de error cuando ya existe una feature con el mismo nombre. */
    public static final String FEATURE_EXISTS = "FF_002";
    /** Mensaje descriptivo para {@link #FEATURE_EXISTS}. */
    public static final String FEATURE_EXISTS_MSG = "La feature ya existe";

    /** Código de error cuando ya está activada la feature. */
    public static final String FEATURE_ENABLE = "FF_003";
    /** Mensaje descriptivo para {@link #FEATURE_ENABLE}. */
    public static final String FEATURE_ENABLE_MSG = "La feature ya está activada en ese entorno/cliente";

    /** Código de error cuando ya está desactivada la feature. */
    public static final String FEATURE_DISABLE = "FF_004";
    /** Mensaje descriptivo para {@link #FEATURE_DISABLE}. */
    public static final String FEATURE_DISABLE_MSG = "La feature ya está desactivada en ese entorno/cliente";

    /** Código de error cuando ya está existe una config para la feature. */
    public static final String FEATURE_EXISTS_CONFIG = "FF_005";
    /**Mensaje descriptivo para {@link #FEATURE_EXISTS_CONFIG}*/
    public static final String FEATURE_EXISTS_CONFIG_MSG = "Ya existe una configuración para feature";


    // ======================
    // User errors
    // ======================

    /** Código de error cuando no se encuentra un usuario. */
    public static final String USER_NOT_FOUND = "USER_001";
    /** Mensaje descriptivo para {@link #USER_NOT_FOUND}. */
    public static final String USER_NOT_FOUND_MSG = "Usuario no encontrado";

    /** Código de error cuando ya existe un usuario con el mismo nombre. */
    public static final String USER_EXISTS = "USER_002";
    /** Mensaje descriptivo para {@link #USER_EXISTS}. */
    public static final String USER_EXISTS_MSG = "Nombre de Usuario ya existe";

    // ======================
    // Parámetros genéricos
    // ======================

    /** Código de error cuando los parámetros recibidos no son aceptados. */
    public static final String BAD_PARAMS = "PARAMS_001";
    /** Mensaje descriptivo para {@link #BAD_PARAMS}. */
    public static final String BAD_PARAMS_MSG = "Parámetros no aceptados";

    /** Código de error cuando los parámetros recibidos no son aceptados. */
    public static final String BAD_REQUEST = "PARAMS_002";
    /** Mensaje descriptivo para {@link #BAD_REQUEST}. */
    public static final String BAD_REQUEST_MSG = "Parámetros no aceptados";

    /**
     * Constructor privado para evitar la instanciación de esta clase de utilidades.
     */
    private ErrorCodes() {}
}
