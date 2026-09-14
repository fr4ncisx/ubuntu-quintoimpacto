-- V4: columnas a inglés. RENAME COLUMN conserva los datos y actualiza
-- automáticamente las FKs dependientes.

-- users
ALTER TABLE "users" RENAME COLUMN "activo" TO "active";
ALTER TABLE "users" RENAME COLUMN "apellido" TO "last_name";
ALTER TABLE "users" RENAME COLUMN "imagen" TO "image";
ALTER TABLE "users" RENAME COLUMN "nombre" TO "first_name";
ALTER TABLE "users" RENAME COLUMN "rol" TO "role";
ALTER TABLE "users" RENAME COLUMN "suscribed" TO "subscribed";
ALTER TABLE "users" RENAME COLUMN "telefono" TO "phone";

-- microbusinesses
ALTER TABLE "microbusinesses" RENAME COLUMN "activo" TO "active";
ALTER TABLE "microbusinesses" RENAME COLUMN "ciudad" TO "city";
ALTER TABLE "microbusinesses" RENAME COLUMN "descripcion" TO "description";
ALTER TABLE "microbusinesses" RENAME COLUMN "enviado_por_mail" TO "mail_sent";
ALTER TABLE "microbusinesses" RENAME COLUMN "fecha_creacion" TO "created_at";
ALTER TABLE "microbusinesses" RENAME COLUMN "mas_informacion" TO "more_info";
ALTER TABLE "microbusinesses" RENAME COLUMN "nombre" TO "name";
ALTER TABLE "microbusinesses" RENAME COLUMN "pais" TO "country";
ALTER TABLE "microbusinesses" RENAME COLUMN "provincia" TO "province";
ALTER TABLE "microbusinesses" RENAME COLUMN "subcategoria" TO "subcategory";
ALTER TABLE "microbusinesses" RENAME COLUMN "id_categoria" TO "category_id";

-- categories / countries / provinces
ALTER TABLE "categories" RENAME COLUMN "nombre" TO "name";
ALTER TABLE "countries" RENAME COLUMN "nombre" TO "name";
ALTER TABLE "provinces" RENAME COLUMN "nombre" TO "name";
ALTER TABLE "provinces" RENAME COLUMN "id_pais" TO "country_id";

-- contact_requests
ALTER TABLE "contact_requests" RENAME COLUMN "fecha_creacion" TO "created_at";
ALTER TABLE "contact_requests" RENAME COLUMN "apellido_nombre" TO "full_name";
ALTER TABLE "contact_requests" RENAME COLUMN "mensaje" TO "message";
ALTER TABLE "contact_requests" RENAME COLUMN "telefono" TO "phone";
ALTER TABLE "contact_requests" RENAME COLUMN "gestionado" TO "reviewed";
ALTER TABLE "contact_requests" RENAME COLUMN "id_micro" TO "microbusiness_id";

-- publications
ALTER TABLE "publications" RENAME COLUMN "fecha_creacion" TO "created_at";

-- publication_views
ALTER TABLE "publication_views" RENAME COLUMN "fecha_visualizacion" TO "viewed_at";
ALTER TABLE "publication_views" RENAME COLUMN "id_publicacion" TO "publication_id";

-- images
ALTER TABLE "images" RENAME COLUMN "id_publicacion" TO "publication_id";
ALTER TABLE "images" RENAME COLUMN "id_micro" TO "microbusiness_id";
ALTER TABLE "images" RENAME COLUMN "fecha_subida" TO "uploaded_at";
ALTER TABLE "images" RENAME COLUMN "id_uploaded_by" TO "uploaded_by_id";

-- chatbot_questions
ALTER TABLE "chatbot_questions" RENAME COLUMN "categoria" TO "category";
ALTER TABLE "chatbot_questions" RENAME COLUMN "id_answer" TO "answer_id";
