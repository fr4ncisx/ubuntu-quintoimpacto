package com.ubuntu.ubuntu_app.model.filters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StopWords {
    private static final Set<String> LATIN_AMERICAN_SPANISH_STOP_WORDS = new HashSet<>(Arrays.asList(
    "al", "algo", "algunas", "algunos", "allá", "allí", "ambos", "ante", "antes", "aquel", "aquella",
    "aquellas", "aquello", "aquellos", "aquí", "arriba", "abajo", "así", "atrás", "aun", "aunque", "bajo",
    "bastante", "cabe", "cada", "casi", "cierto", "con", "conmigo", "conseguimos",
    "conseguir", "consigo", "consigue", "consiguen", "consigues", "contigo", "contra", "cual", "cuales",
    "cualquier", "cualquiera", "cuan", "cuando", "cuanto", "cuanta", "cuantas", "cuantos", "de", "dejar",
    "del", "demás", "demasiado", "dentro", "desde", "dos", "durante", "el", "él", "ella",
    "ellas", "ello", "ellos", "empleais", "emplean", "emplear", "empleas", "empleo", "en", "encima",
    "entonces", "entre", "era", "eramos", "eran", "eras", "eres", "es", "esa", "esas", "ese", "eso",
    "esos", "estaba", "estado", "estais", "estamos", "estan", "estar", "este", "esto", "estos",
    "estoy", "etc", "fin", "fue", "fueron", "fui", "fuimos", "gueno", "ha", "hace", "haceis", "hacemos",
    "hacen", "hacer", "haces", "hago", "hasta", "hay", "he", "hemos", "hicieron", "hizo", "la", "las",
    "le", "les", "lo", "los", "luego", "me", "mi", "mía", "mias", "mientras", "mio", "mios", "mis",
    "misma", "mismas", "mismo", "mismos", "modo", "muchísima","muchísimo", "muchísimos", "mucho", "muchos", "nada", "ni", "ningunos", "no", "nos",
    "nosotras", "nosotros", "nuestra", "nuestras", "nuestro", "nuestros", "nunca", "otra",
    "otras", "otro", "otros", "para", "parecer", "pero", "poca", "pocas", "poco", "pocos", "podeis",
    "podemos", "poder", "podria", "podriais", "podriamos", "podrian", "podrias", "por", "por qué",
    "porque", "primero", "puede", "pueden", "puedo", "pues", "que", "qué", "querer", "quien", "quién",
    "quienes", "quienesquiera", "quienquiera", "quizá", "quizás", "sabe", "sabeis", "sabemos", "saben",
    "saber", "sabes", "se", "segun", "ser", "si", "sí", "siempre", "siendo", "sin", "sino", "so", "sobre",
    "sois", "solamente", "solo", "somos", "soy", "sr", "sra", "sres", "sta", "su", "sus", "suya", "suyas",
    "suyo", "suyos", "tal", "tales", "también", "tampoco", "tan", "tanta", "tantas", "tanto", "tantos",
    "te", "teneis", "tenemos", "tener", "tengo", "ti", "tiempo", "tiene", "tienen", "toda", "todas",
    "todo", "todos", "tomar", "trabaja", "trabajais", "trabajamos", "trabajan", "trabajar", "trabajas",
    "trabajo", "tras", "tú", "tu", "tus", "tuya", "tuyo", "tuyos", "un", "una", "uno", "unos", "usa",
    "usais", "usamos", "usan", "usar", "usas", "uso", "usted", "ustedes", "va", "vais", "valor", "vamos",
    "van", "vaya", "verdad", "verdadera", "verdadero", "vosotras", "vosotros", "voy", "vuestra",
    "vuestras", "vuestro", "vuestros", "y", "ya", "yo", "?", "¿", "!", "¡", "*"));
    
    public static Set<String> getLatinAmericanSpanishStopWords() {
        return LATIN_AMERICAN_SPANISH_STOP_WORDS;
    }
}
