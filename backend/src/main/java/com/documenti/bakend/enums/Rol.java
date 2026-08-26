package com.documenti.bakend.enums;

/**
 * Rol
 */
public enum Rol {

    SUPER_ADMIN, //gestiona TODO y invita usario
    ADMIN, //Gestiona su gripo y puede elminar usario
    EDITOR,//pude leer/edictar/crear Docuentnos
    LECTOR,//SOLO puede leer y nada mas
    CLIENTE,//pude leer de fomra permanete y sin cuneta
    //INIVTADO,// puede leer de fomraporal
}
