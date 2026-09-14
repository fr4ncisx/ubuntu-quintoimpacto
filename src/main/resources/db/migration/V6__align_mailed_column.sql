-- V6: alinea la columna de flag de newsletter con la entidad (mailed).

ALTER TABLE "microbusinesses" RENAME COLUMN "mail_sent" TO "mailed";
