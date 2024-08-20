package com.ubuntu.ubuntu_app.model.filters;

public class StringFilter {

    public static String getNormalizedInput(String text){
        StringBuilder sb = new StringBuilder();
        sb.append(text);
        var charAtIndex0 = sb.charAt(0);
        var toStringChar = String.valueOf(charAtIndex0).toUpperCase();
        return "%" + toStringChar + sb.substring(1, sb.length()).toLowerCase() + "%";
    }
}
