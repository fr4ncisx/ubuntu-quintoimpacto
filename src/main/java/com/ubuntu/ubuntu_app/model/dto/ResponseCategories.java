package com.ubuntu.ubuntu_app.model.dto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResponseCategories {
    public static Set<String> response = new HashSet<>(Arrays.asList(
            "Economía social/Desarrollo local/Inclusión financiera: Microemprendimientos enfocados en fortalecer las comunidades a través de iniciativas que promueven la economía social, fomentan el desarrollo local y mejoran el acceso a servicios financieros para poblaciones marginadas, ayudando a reducir la desigualdad y a construir economías más inclusivas.",
            "Agroecología/Orgánicos/Alimentación Saludable: Emprendimientos que promueven prácticas agrícolas sostenibles, la producción de alimentos orgánicos y el acceso a una alimentación saludable, contribuyendo al bienestar de las personas y al cuidado del medio ambiente.",
            "Conservación/Regeneración/Servicios ecosistémicos: Proyectos dedicados a la protección y restauración de la biodiversidad, la preservación de recursos naturales, y la provisión de servicios ecosistémicos vitales, como la purificación del agua y la captura de carbono, para garantizar un futuro sostenible.",
            "Empresas/Organismos de impacto/Economía circular: Iniciativas empresariales que buscan generar un impacto positivo en la sociedad y el medio ambiente, promoviendo modelos de negocio basados en la economía circular, donde se minimiza el desperdicio y se maximiza la reutilización de recursos."));

    public record MultiResponseList(List<MultiResponseDTO> Respuestas) {
    }

    public record MultiResponseDTO(String Respuesta) {
    }
}
