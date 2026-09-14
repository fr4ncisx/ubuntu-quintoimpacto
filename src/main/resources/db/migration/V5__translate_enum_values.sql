-- V5: valores persistidos del enum ChatbotCategory a inglés.
-- Va después de V4 (la columna ya se llama category).

ALTER TABLE "chatbot_questions" DROP CONSTRAINT IF EXISTS "chatbot_questions_categoria_check";

UPDATE "chatbot_questions" SET "category" = 'INSTITUTIONAL' WHERE "category" = 'INSTITUCIONAL';
UPDATE "chatbot_questions" SET "category" = 'MICROBUSINESS' WHERE "category" = 'MICROEMPRENDIMIENTOS';
UPDATE "chatbot_questions" SET "category" = 'FREQUENT_QUESTIONS' WHERE "category" = 'PREGUNTAS_FRECUENTES';

ALTER TABLE "chatbot_questions"
    ADD CONSTRAINT "chatbot_questions_category_check"
    CHECK ("category" IN ('INSTITUTIONAL', 'MICROBUSINESS', 'FREQUENT_QUESTIONS'));
