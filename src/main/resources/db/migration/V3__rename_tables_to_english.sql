-- V3: tablas y constraints a inglés. Los FK se renombran con nombres
-- determinísticos (adiós fkXXXX de Hibernate). Los datos se conservan:
-- RENAME no toca filas.

ALTER TABLE IF EXISTS "usuarios" RENAME TO "users";
ALTER TABLE IF EXISTS "microemprendimientos" RENAME TO "microbusinesses";
ALTER TABLE IF EXISTS "categorias" RENAME TO "categories";
ALTER TABLE IF EXISTS "pais" RENAME TO "countries";
ALTER TABLE IF EXISTS "provincia" RENAME TO "provinces";
ALTER TABLE IF EXISTS "contacto" RENAME TO "contact_requests";
ALTER TABLE IF EXISTS "publicaciones" RENAME TO "publications";
ALTER TABLE IF EXISTS "publicaciones_estadisticas" RENAME TO "publication_views";
ALTER TABLE IF EXISTS "imagenes" RENAME TO "images";
ALTER TABLE IF EXISTS "chatbot" RENAME TO "chatbot_responses";

ALTER TABLE "users" RENAME CONSTRAINT "usuarios_pkey" TO "users_pkey";
ALTER TABLE "microbusinesses" RENAME CONSTRAINT "microemprendimientos_pkey" TO "microbusinesses_pkey";
ALTER TABLE "categories" RENAME CONSTRAINT "categorias_pkey" TO "categories_pkey";
ALTER TABLE "countries" RENAME CONSTRAINT "pais_pkey" TO "countries_pkey";
ALTER TABLE "provinces" RENAME CONSTRAINT "provincia_pkey" TO "provinces_pkey";
ALTER TABLE "contact_requests" RENAME CONSTRAINT "contacto_pkey" TO "contact_requests_pkey";
ALTER TABLE "publications" RENAME CONSTRAINT "publicaciones_pkey" TO "publications_pkey";
ALTER TABLE "publication_views" RENAME CONSTRAINT "publicaciones_estadisticas_pkey" TO "publication_views_pkey";
ALTER TABLE "images" RENAME CONSTRAINT "imagenes_pkey" TO "images_pkey";
ALTER TABLE "images" RENAME CONSTRAINT "uktan72v3xiado790egx4g48b3x" TO "uk_images_public_id";
ALTER TABLE "chatbot_responses" RENAME CONSTRAINT "chatbot_pkey" TO "chatbot_responses_pkey";

ALTER TABLE "contact_requests" RENAME CONSTRAINT "fk6xjx5p7c690bo8t58ka8uhorh" TO "fk_contact_requests_microbusiness";
ALTER TABLE "images" RENAME CONSTRAINT "fkb7b5ka5ew9lyh5okldnmq6lwx" TO "fk_images_publication";
ALTER TABLE "images" RENAME CONSTRAINT "fke3wk22ne7ass5ayg1ldrh70bf" TO "fk_images_uploaded_by";
ALTER TABLE "images" RENAME CONSTRAINT "fksekvs4hcteofvaln6lwmagtx4" TO "fk_images_microbusiness";
ALTER TABLE "microbusinesses" RENAME CONSTRAINT "fk2s9ucw3bp1xjo9f63gk88kxaw" TO "fk_microbusinesses_category";
ALTER TABLE "provinces" RENAME CONSTRAINT "fkp37jnxd9htn7o8v195jm4uvre" TO "fk_provinces_country";
ALTER TABLE "publication_views" RENAME CONSTRAINT "fk48nyeo567a6mh0po3lu04uhof" TO "fk_publication_views_publication";
ALTER TABLE "chatbot_questions" RENAME CONSTRAINT "fk4g31da9xvmy7s66e88swungwi" TO "fk_chatbot_questions_answer";
