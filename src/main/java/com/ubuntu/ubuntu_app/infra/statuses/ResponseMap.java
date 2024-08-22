package com.ubuntu.ubuntu_app.infra.statuses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.ubuntu.ubuntu_app.model.dto.ResponseCategories.MultiResponseDTO;
import com.ubuntu.ubuntu_app.model.dto.ResponseCategories.MultiResponseList;

public class ResponseMap {
    /*
     * Método para crear respuestas de operaciones HTTP
     */
    public static Map<String, String> createResponse(String message) {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("Estado", message);
        return responseMap;
    }

    public static Map<String, String> botResponse(String message) {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("Respuesta", message);
        return responseMap;
    }

    public static <T, G> Map<T, G> responseGeneric(T mapKey, G tValue) {
        Map<T, G> responseMap = new HashMap<>();
        responseMap.put(mapKey, tValue);
        return responseMap;
    }

    public static MultiResponseList MultiBotResponse(Set<String> messages) {
        List<MultiResponseDTO> responseList = new ArrayList<>();        
        for (String message : messages) {
            responseList.add(new MultiResponseDTO(message));
        }
        return new MultiResponseList(responseList);
    }
}
