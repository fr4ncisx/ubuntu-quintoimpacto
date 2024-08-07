package com.ubuntu.ubuntu_app.infra.statuses;

public class MicroResponseDoc {
    
    public final static String micro_deleted = "{\r\n" + //
                "\t\"Estado\": \"El microemprendimiento fue borrado correctamente\"\r\n" + //
                "}";
    public final static String micro_hide_ok = "{\r\n" + //
                "\t\"Estado\": \"El microemprendimiento fue ocultado\"\r\n" + //
                "}";    
    public final static String micro_not_found_one = "{\r\n" + //
                "\t\"Error\": \"No se encontro microemprendimiento\"\r\n" + //
                "}";
    
    public final static String micro_created = "{\r\n" + //
                "\"Estado\": \"Creado exitosamente\"\r\n" + //
                "}";
    public final static String micro_format_error = "{\r\n" + //
                "\"error\": \"Error de formato en la solicitud\"\r\n" + //
                "}";
    public final static String micro_edit_ok = "{\r\n" + //
                "\"Estado\": \"La edición del microemprendimiento fue correcta\"\r\n" + //
                "}";
    public final static String micro_found_searchbar = "[\r\n" + //
            "{\r\n" + //
            "\t\"nombre\": \"Eco Emprendimiento\",\r\n" + //
            "\t\"descripcion\": \"Descripción del emprendimiento\",\r\n" + //
            "\t\"masInformacion\": \"Mas información emprendimiento\",\r\n" + //
            "\t\"pais\": \"Argentina\",\r\n" + //
            "\t\"provincia\": \"Santa Fe\",\r\n" + //
            "\t\"ciudad\": \"Rosario\",\r\n" + //
            "\t\"categoria\": {\r\n" + //
            "\t\t\"nombre\": \"Agroecología/Orgánicos/Alimentación saludable\"\r\n" + //
            "\t},\r\n" + //
            "\t\"subcategoria\": \"{optional}\",\r\n" + //
            "\t\"imagenes\": \"null\",\r\n" + //
            "\t\"mensajeDeContacto\": \"null\",\r\n" + //
            "\t}\r\n" + //
            "]";
    public final static String micro_not_found_searchbar = "{\r\n" + //
            "\"Error\": \"El nombre empezado por '{nombre_buscado}' no ha arrojado resultados\"\r\n" + //
            "}";

    public final static String micro_validation_error = "{\r\n" + //
            "\"Error\": \"El nombre no debe estar vacio\"\r\n" + //
            "}";
    public final static String micro_edit_validation_error = "{\r\n" + //
                "\"campo\": \"nombre del campo\",\r\n" + //
                "\"error\": \"no debe estar vacío\"\r\n" + //
                "}";
    public final static String micro_error = "{\r\n" + //
            "\t\"Error\": \"No se encontaron emprendimientos en la base de datos\"\r\n" + //
            "}";
    public final static String micro_error_notFound = "{\r\n" + //
            "\"Error\": \"Microemprendimiento no existe en la base de datos\"\r\n" + //
            "}";
    public final static String micro_found_category = "[\r\n" + //
            "\t{\r\n" + //
            "\t\t\"nombre\": \"Impacto Positivo\",\r\n" + //
            "\t\t\"descripcion\": \"Promovemos la inclusión financiera y el desarrollo local a través de microfinanzas.\",\r\n"
            + //
            "\t\t\"masInformacion\": \"Facilitamos el acceso a servicios financieros para comunidades desatendidas.\",\r\n"
            + //
            "\t\t\"pais\": \"Perú\",\r\n" + //
            "\t\t\"provincia\": \"Lima\",\r\n" + //
            "\t\t\"ciudad\": \"Lima\",\r\n" + //
            "\t\t\"subcategoria\": \"Microfinanzas\",\r\n" + //
            "\t\t\"imagenes\": \"null\"\r\n" + //
            "\t},\r\n" + //
            "\t{\r\n" + //
            "\t\t\"nombre\": \"Crecimiento Sostenible\",\r\n" + //
            "\t\t\"descripcion\": \"Promovemos el desarrollo local a través de proyectos de inclusión financiera.\",\r\n"
            + //
            "\t\t\"masInformacion\": \"Nuestros programas están diseñados para empoderar a comunidades a través de servicios financieros accesibles.\",\r\n"
            + //
            "\t\t\"pais\": \"Ecuador\",\r\n" + //
            "\t\t\"provincia\": \"Pichincha\",\r\n" + //
            "\t\t\"ciudad\": \"Quito\",\r\n" + //
            "\t\t\"subcategoria\": \"Empoderamiento comunitario\",\r\n" + //
            "\t\t\"imagenes\": \"null\"\r\n" + //
            "\t}\r\n" + //
            "]";
    public final static String micro_found = "[\r\n" + //
            "\t{\r\n" + //
            "\t\t\"id\": 1,\r\n" + //
            "\t\t\"nombre\": \"Ciclo Verde\",\r\n" + //
            "\t\t\"descripcion\": \"Promovemos prácticas de economía circular en la gestión de residuos.\",\r\n" + //
            "\t\t\"masInformacion\": \"Desarrollamos soluciones innovadoras para transformar residuos en recursos.\",\r\n"
            + //
            "\t\t\"pais\": \"España\",\r\n" + //
            "\t\t\"provincia\": \"Madrid\",\r\n" + //
            "\t\t\"ciudad\": \"Acala de henares\",\r\n" + //
            "\t\t\"categoria\": {\r\n" + //
            "\t\t\t\"nombre\": \"Empresas/Organismos de impacto/Economía circular\"\r\n" + //
            "\t\t},\r\n" + //
            "\t\t\"subcategoria\": \"Gestión de residuos\",\r\n" + //
            "\t\t\"imagenes\": [\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"http://imagen1.png\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"http://imagen2.png\"\r\n" + //
            "\t\t\t}\r\n" + //
            "\t\t]\r\n" + //
            "\t},\r\n" + //
            "\t{\r\n" + //
            "\t\t\"id\": 2,\r\n" + //
            "\t\t\"nombre\": \"Impacto Corp\",\r\n" + //
            "\t\t\"descripcion\": \"Consultoría en sostenibilidad y economía circular.\",\r\n" + //
            "\t\t\"masInformacion\": \"ImpactCorp ofrece servicios de consultoría para empresas que desean adoptar prácticas de economía circular.\",\r\n"
            + //
            "\t\t\"pais\": \"Chile\",\r\n" + //
            "\t\t\"provincia\": \"Santiago\",\r\n" + //
            "\t\t\"ciudad\": \"Valparaíso\",\r\n" + //
            "\t\t\"categoria\": {\r\n" + //
            "\t\t\t\"nombre\": \"Empresas/Organismos de impacto/Economía circular\"\r\n" + //
            "\t\t},\r\n" + //
            "\t\t\"subcategoria\": \"Consultoría empresarial\",\r\n" + //
            "\t\t\"imagenes\": [\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp1.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp2.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp3.jpg\"\r\n" + //
            "\t\t\t}\r\n" + //
            "\t\t]\r\n" + //
            "\t},\r\n" + //
            "\t{\r\n" + //
            "\t\t\"id\": 3,\r\n" + //
            "\t\t\"nombre\": \"Impacto Corp\",\r\n" + //
            "\t\t\"descripcion\": \"Consultoría en sostenibilidad y economía circular.\",\r\n" + //
            "\t\t\"masInformacion\": \"ImpactCorp ofrece servicios de consultoría para empresas que desean adoptar prácticas de economía circular.\",\r\n"
            + //
            "\t\t\"pais\": \"Chile\",\r\n" + //
            "\t\t\"provincia\": \"Santiago\",\r\n" + //
            "\t\t\"ciudad\": \"Valparaíso\",\r\n" + //
            "\t\t\"categoria\": {\r\n" + //
            "\t\t\t\"nombre\": \"Empresas/Organismos de impacto/Economía circular\"\r\n" + //
            "\t\t},\r\n" + //
            "\t\t\"subcategoria\": \"Consultoría empresarial\",\r\n" + //
            "\t\t\"imagenes\": [\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp1.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp2.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp3.jpg\"\r\n" + //
            "\t\t\t}\r\n" + //
            "\t\t]\r\n" + //
            "\t},\r\n" + //
            "\t{\r\n" + //
            "\t\t\"id\": 4,\r\n" + //
            "\t\t\"nombre\": \"Impacto Corp\",\r\n" + //
            "\t\t\"descripcion\": \"Consultoría en sostenibilidad y economía circular.\",\r\n" + //
            "\t\t\"masInformacion\": \"ImpactCorp ofrece servicios de consultoría para empresas que desean adoptar prácticas de economía circular.\",\r\n"
            + //
            "\t\t\"pais\": \"Chile\",\r\n" + //
            "\t\t\"provincia\": \"Santiago\",\r\n" + //
            "\t\t\"ciudad\": \"Valparaíso\",\r\n" + //
            "\t\t\"categoria\": {\r\n" + //
            "\t\t\t\"nombre\": \"Empresas/Organismos de impacto/Economía circular\"\r\n" + //
            "\t\t},\r\n" + //
            "\t\t\"subcategoria\": \"Consultoría empresarial\",\r\n" + //
            "\t\t\"imagenes\": [\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp1.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp2.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp3.jpg\"\r\n" + //
            "\t\t\t}\r\n" + //
            "\t\t]\r\n" + //
            "\t},\r\n" + //
            "\t{\r\n" + //
            "\t\t\"id\": 5,\r\n" + //
            "\t\t\"nombre\": \"Impacto Corp\",\r\n" + //
            "\t\t\"descripcion\": \"Consultoría en sostenibilidad y economía circular.\",\r\n" + //
            "\t\t\"masInformacion\": \"ImpactCorp ofrece servicios de consultoría para empresas que desean adoptar prácticas de economía circular.\",\r\n"
            + //
            "\t\t\"pais\": \"Chile\",\r\n" + //
            "\t\t\"provincia\": \"Santiago\",\r\n" + //
            "\t\t\"ciudad\": \"Valparaíso\",\r\n" + //
            "\t\t\"categoria\": {\r\n" + //
            "\t\t\t\"nombre\": \"Empresas/Organismos de impacto/Economía circular\"\r\n" + //
            "\t\t},\r\n" + //
            "\t\t\"subcategoria\": \"Consultoría empresarial\",\r\n" + //
            "\t\t\"imagenes\": [\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp1.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp2.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp3.jpg\"\r\n" + //
            "\t\t\t}\r\n" + //
            "\t\t]\r\n" + //
            "\t},\r\n" + //
            "\t{\r\n" + //
            "\t\t\"id\": 6,\r\n" + //
            "\t\t\"nombre\": \"Impacto Corp\",\r\n" + //
            "\t\t\"descripcion\": \"Consultoría en sostenibilidad y economía circular.\",\r\n" + //
            "\t\t\"masInformacion\": \"ImpactCorp ofrece servicios de consultoría para empresas que desean adoptar prácticas de economía circular.\",\r\n"
            + //
            "\t\t\"pais\": \"Chile\",\r\n" + //
            "\t\t\"provincia\": \"Santiago\",\r\n" + //
            "\t\t\"ciudad\": \"Valparaíso\",\r\n" + //
            "\t\t\"categoria\": {\r\n" + //
            "\t\t\t\"nombre\": \"Empresas/Organismos de impacto/Economía circular\"\r\n" + //
            "\t\t},\r\n" + //
            "\t\t\"subcategoria\": \"Consultoría empresarial\",\r\n" + //
            "\t\t\"imagenes\": [\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp1.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp2.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp3.jpg\"\r\n" + //
            "\t\t\t}\r\n" + //
            "\t\t]\r\n" + //
            "\t},\r\n" + //
            "\t{\r\n" + //
            "\t\t\"id\": 7,\r\n" + //
            "\t\t\"nombre\": \"Impacto Corp\",\r\n" + //
            "\t\t\"descripcion\": \"Consultoría en sostenibilidad y economía circular.\",\r\n" + //
            "\t\t\"masInformacion\": \"ImpactCorp ofrece servicios de consultoría para empresas que desean adoptar prácticas de economía circular.\",\r\n"
            + //
            "\t\t\"pais\": \"Chile\",\r\n" + //
            "\t\t\"provincia\": \"Santiago\",\r\n" + //
            "\t\t\"ciudad\": \"Valparaíso\",\r\n" + //
            "\t\t\"categoria\": {\r\n" + //
            "\t\t\t\"nombre\": \"Empresas/Organismos de impacto/Economía circular\"\r\n" + //
            "\t\t},\r\n" + //
            "\t\t\"subcategoria\": \"Consultoría empresarial\",\r\n" + //
            "\t\t\"imagenes\": [\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp1.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp2.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp3.jpg\"\r\n" + //
            "\t\t\t}\r\n" + //
            "\t\t]\r\n" + //
            "\t},\r\n" + //
            "\t{\r\n" + //
            "\t\t\"id\": 8,\r\n" + //
            "\t\t\"nombre\": \"Impacto Corp\",\r\n" + //
            "\t\t\"descripcion\": \"Consultoría en sostenibilidad y economía circular.\",\r\n" + //
            "\t\t\"masInformacion\": \"ImpactCorp ofrece servicios de consultoría para empresas que desean adoptar prácticas de economía circular.\",\r\n"
            + //
            "\t\t\"pais\": \"Chile\",\r\n" + //
            "\t\t\"provincia\": \"Santiago\",\r\n" + //
            "\t\t\"ciudad\": \"Valparaíso\",\r\n" + //
            "\t\t\"categoria\": {\r\n" + //
            "\t\t\t\"nombre\": \"Empresas/Organismos de impacto/Economía circular\"\r\n" + //
            "\t\t},\r\n" + //
            "\t\t\"subcategoria\": \"Consultoría empresarial\",\r\n" + //
            "\t\t\"imagenes\": [\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp1.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp2.jpg\"\r\n" + //
            "\t\t\t},\r\n" + //
            "\t\t\t{\r\n" + //
            "\t\t\t\t\"url\": \"https://example.com/impactcorp3.jpg\"\r\n" + //
            "\t\t\t}\r\n" + //
            "\t\t]\r\n" + //
            "\t}\r\n" + //
            "]";

}
