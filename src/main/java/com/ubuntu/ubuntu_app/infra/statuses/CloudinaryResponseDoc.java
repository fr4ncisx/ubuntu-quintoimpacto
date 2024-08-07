package com.ubuntu.ubuntu_app.infra.statuses;

public class CloudinaryResponseDoc {
    public static final String img_ok = "{\r\n" + //
            "\t\"Imagen 1\": \"https://res.cloudinary.com/dnf68vq7m/image/upload/v1722023731/ubuntu/aioros_de_sagitario_by_salvamakoto_ddn032l-fullview-821x1024.webp\",\r\n"
            + //
            "\t\"Imagen 2\": \"https://res.cloudinary.com/dnf68vq7m/image/upload/v1722023732/ubuntu/26_C343_shinji-grin.jpg\",\r\n"
            + //
            "\t\"Imagen 3\": \"https://res.cloudinary.com/dnf68vq7m/image/upload/v1723032810/ubuntu/Jughead_Button.png\"\r\n"
            + //
            "}";
    public static final String img_errors = "[\r\n" + //
            "\t{\r\n" + //
            "\t\t\"archivo\": \"{archivo}\",\r\n" + //
            "\t\t\"error\": \"Extensión inválida\",\r\n" + //
            "\t\t\"extension\": \"PDF\"\r\n" + //
            "\t}\r\n" + //
            "]" +
            "{\r\n" + //
            "\t\"Error\": \"Maximum upload size exceeded\"\r\n" + //
            "}" + "{\r\n" + //
            "\t\"Error\": \"Se requiere al menos una imagen para subir\"\r\n" + //
            "}" + "{\r\n" + //
            "\t\"Error\": \"Se excedió el limite de 3 archivos, se ingresaron 4 archivos\"\r\n" + //
            "}";

    public static final String img_deleted = "{\r\n" + //
            "\t\"result\": \"ok\"\r\n" + //
            "}";

    public static final String img_edit_fail = "{\r\n" + //
            "\t\"Estado\": \"Hubo un error al validar la extensión, comunicate con un desarrollador\"\r\n" + //
            "}";

    public static final String img_notFound = "{\r\n" + //
            "\t\"Error\": \"El public_id: '{public_id}' no existe en Cloudinary\"\r\n" + //
            "}";

    public static final String img_edit_ok = "{\r\n" + //
            "\t\"Imagen\": \"https://res.cloudinary.com/dnf68vq7m/image/upload/v1723033281/ubuntu/estrellas-en-el-universo-morado_1920x1080_xtrafondos.com.jpg\"\r\n"
            + //
            "}";
}
