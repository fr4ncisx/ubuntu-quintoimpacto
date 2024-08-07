package com.ubuntu.ubuntu_app.infra.statuses;

public class UserResponseDoc {
    public final static String user_register_ok = "{\r\n" + //
                "\"Estado\": \"Creado exitosamente\"\r\n" + //
                "}";
    public final static String user_validation_error = "{\r\n" + //
                "\"campo\": \"nombre del campo\",\r\n" + //
                "\"error\": \"no debe estar vacío\"\r\n" + //
                "}"; 
    public final static String user_format_error = "{\r\n" + //
                "\"error\": \"Error de formato en la solicitud\"\r\n" + //
                "}"; 
    public final static String user_fetch = "[\r\n" + //
                "\t{\r\n" + //
                "\t\t\"id\": 1,\r\n" + //
                "\t\t\"nombre\": \"Ubuntu\",\r\n" + //
                "\t\t\"apellido\": \"Administración\",\r\n" + //
                "\t\t\"email\": \"semilleroubuntu.dev@gmail.com\",\r\n" + //
                "\t\t\"activo\": true,\r\n" + //
                "\t\t\"rol\": \"ADMIN\",\r\n" + //
                "\t\t\"telefono\": \"+54 9 424 4525 4638\"\r\n" + //
                "\t},\r\n" + //
                "\t{\r\n" + //
                "\t\t\"id\": 2,\r\n" + //
                "\t\t\"nombre\": \"Francisco\",\r\n" + //
                "\t\t\"apellido\": \"Montoro\",\r\n" + //
                "\t\t\"email\": \"f.montoro@gmail.com\",\r\n" + //
                "\t\t\"activo\": true,\r\n" + //
                "\t\t\"rol\": \"ADMIN\",\r\n" + //
                "\t\t\"telefono\": \"+54 9 351 666674\"\r\n" + //
                "\t}\r\n" + //
                "]"; 
    public final static String user_not_found = "{\r\n" + //
                "\"Error\": \"El usuario no existe en la base de datos\"\r\n" + //
                "}"; 
    public final static String user_deactivated = "{\r\n" + //
                "\"Estado\": \"Usuario desactivado exitosamente\"\r\n" + //
                "}"; 
    public final static String user_already_deactivated = "{\r\n" + //
                "\"Estado\": \"El usuario no se puede desactivar porque ya está desactivado\"\r\n" + //
                "}"; 
    public final static String user_modified= "{\r\n" + //
                "\"Estado\": \"Usuario Modificado exitosamente\"\r\n" + //
                "}";
}
