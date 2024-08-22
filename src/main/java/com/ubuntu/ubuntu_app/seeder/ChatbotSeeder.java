package com.ubuntu.ubuntu_app.seeder;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.Repository.ChatbotRepository;
import com.ubuntu.ubuntu_app.model.entities.ChatbotQuestionEntity;
import com.ubuntu.ubuntu_app.model.entities.ChatbotResponseEntity;
import com.ubuntu.ubuntu_app.model.enums.ChatbotCategory;

import lombok.RequiredArgsConstructor;

@Lazy
@RequiredArgsConstructor
@Component
public class ChatbotSeeder implements CommandLineRunner {

        private final ChatbotRepository chatbotRepository;

        @Override
        public void run(String... args) throws Exception {
                if (chatbotRepository.count() == 0) {
                        loadChatbot();
                }
        }

        @Transactional
        private void loadChatbot() {
                List<ChatbotResponseEntity> listOfResponse = Arrays.asList(                        
                        new ChatbotResponseEntity(null, "Ubuntu es una empresa de financiamiento sostenible, de grupo asociativo, compuesta por 30 personas, originaria de Mendoza, Argentina.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Quiénes somos?", ChatbotCategory.INSTITUCIONAL))
                                ),
                                new ChatbotResponseEntity(null, "Desarrollar y gestionar instrumentos, que conecten inversores con proyectos de impacto social y ambiental positivo.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Cuáles son los objetivos de Ubuntu?", ChatbotCategory.INSTITUCIONAL))
                                ),
                                new ChatbotResponseEntity(null, "Permitir conectar inversores de impacto, con proyectos de micro emprendedores, conociendo su propósito, contexto e historia, motivando a inversores que apoyen económicamente a empresas u organizaciones que generan impacto positivo.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Qué funciones cumple Ubuntu?", ChatbotCategory.INSTITUCIONAL))
                                ),
                                new ChatbotResponseEntity(null, "En la sección de microemprendimientos, éstos se encuentran distribuídos por categorías.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Dónde puedo visualizar los microemprendimientos?", ChatbotCategory.MICROEMPRENDIMIENTOS))
                                ),
                                new ChatbotResponseEntity(null, "En el microemprendimiento que desees invertir, debes presionar el botón de “Contactar” y completar el formulario que aparecerá, con tus datos de contacto. Y luego nuestros asistentes se pondrán en contacto.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Cómo invertir en un microemprendimiento?", ChatbotCategory.MICROEMPRENDIMIENTOS))
                                ),
                                new ChatbotResponseEntity(null, "Respuesta categorias", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Cuáles son las categorías de microemprendimientos?", ChatbotCategory.MICROEMPRENDIMIENTOS))
                                ),
                                new ChatbotResponseEntity(null, "Por lo general, no demora más de 3 días hábiles en enviar una respuesta", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Una vez enviada la solicitud de contacto de un Microemprendimiento, en cuánto tiempo recibiría una respuesta?", ChatbotCategory.PREGUNTAS_FRECUENTES))
                                ),
                                new ChatbotResponseEntity(null, "Si, se requiere la registración de la empresa u organización interesada en invertir, en el organismo contralor correspondiente, y no presentar irregularidades.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Hay requisitos para ser inversor?", ChatbotCategory.PREGUNTAS_FRECUENTES))
                                ),
                                new ChatbotResponseEntity(null, "Si, pero estos varían según el emprendimiento, y de las necesidades de cada uno en particular.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Hay un mínimo o máximo de inversión?", ChatbotCategory.PREGUNTAS_FRECUENTES))
                                ),
                                new ChatbotResponseEntity(null, "Según lo establecido en la normativa vigente respaldada por el Banco Central de República Argentina, se denomina como % de retorno, el publicado mensualmente por esta entidad, en los retornos derivados de otros tipos de inversiones, como un plazo fijo.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Cuánto es el retorno de inversión?", ChatbotCategory.PREGUNTAS_FRECUENTES))
                                ),
                                new ChatbotResponseEntity(null, "Puedes suscribirte a nuestro newsletter para obtener semanalmente, un listado de nuevos microemprendimientos registrados.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "¿Cómo puedo recibir información actualizada de los Microemprendimientos?", ChatbotCategory.PREGUNTAS_FRECUENTES))
                                ),
                                // NATURAL LANGUAGE BOT
                                new ChatbotResponseEntity(null, "Hola! Es un gusto poder ayudarte, cuentame en que puedo ayudarte!", Arrays.asList(
                                new ChatbotQuestionEntity(null, "hola", null),
                                new ChatbotQuestionEntity(null, "buenas", null),
                                new ChatbotQuestionEntity(null, "buen dia", null),
                                new ChatbotQuestionEntity(null, "buenas tardes", null),
                                new ChatbotQuestionEntity(null, "buenas noches", null))
                                ),
                                new ChatbotResponseEntity(null, "¡Oh! Creo que estás teniendo algun problema sin resolver, cuentame más...", Arrays.asList(
                                new ChatbotQuestionEntity(null, "problema", null),
                                new ChatbotQuestionEntity(null, "ayuda", null),
                                new ChatbotQuestionEntity(null, "necesito ayuda", null),
                                new ChatbotQuestionEntity(null, "ayudarme", null),
                                new ChatbotQuestionEntity(null, "ayudas", null))
                                ),
                                new ChatbotResponseEntity(null, "Bien, al parecer tienes alguna dificultad para registrarte, en la parte derecha de la web se pueden visualizar tres lineas paralelas (≡), puedes hacer click ahí y veras que hay una sección que se lee como 'Iniciar sesión' te llevará a una página nueva y solo inicia sesión con tu cuenta de google y listo!", Arrays.asList(
                                new ChatbotQuestionEntity(null, "registrar", null),
                                new ChatbotQuestionEntity(null, "registrarme", null),
                                new ChatbotQuestionEntity(null, "registro", null),
                                new ChatbotQuestionEntity(null, "registrarse", null),
                                new ChatbotQuestionEntity(null, "registrado", null))
                                ),
                                new ChatbotResponseEntity(null, "Si deseas recibir un newsletter semanal de los nuevos microemprendimientos, registrandote en la web podrás tener el privilegio de recibir semanalmente estos correos.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "newsletter", null),
                                new ChatbotQuestionEntity(null, "suscribirme", null),
                                new ChatbotQuestionEntity(null, "suscripción", null),
                                new ChatbotQuestionEntity(null, "obtener semanalmente", null))
                                ),
                                new ChatbotResponseEntity(null, "Si estás pensando en invertir, debes presionar el botón de “Contactar” y completar el formulario que aparecerá, con tus datos de contacto. Y luego nuestros asistentes se pondrán en contacto", Arrays.asList(
                                new ChatbotQuestionEntity(null, "invertir", null),
                                new ChatbotQuestionEntity(null, "inversión", null),
                                new ChatbotQuestionEntity(null, "invertir microemprendimiento", null),
                                new ChatbotQuestionEntity(null, "inversion", null),
                                new ChatbotQuestionEntity(null, "dinero", null),
                                new ChatbotQuestionEntity(null, "realizar inversion", null))
                                ),
                                new ChatbotResponseEntity(null, "El botón “contactar” se puede encontrar buscando los microemprendimientos", Arrays.asList(
                                new ChatbotQuestionEntity(null, "botón contactar", null),
                                new ChatbotQuestionEntity(null, "contactar", null),
                                new ChatbotQuestionEntity(null, "contacto", null),
                                new ChatbotQuestionEntity(null, "boton contactar", null),
                                new ChatbotQuestionEntity(null, "boton contacto", null),
                                new ChatbotQuestionEntity(null, "encuentro boton contacto", null),
                                new ChatbotQuestionEntity(null, "encontrar boton", null))
                                ),
                                new ChatbotResponseEntity(null, "Entendido! Para buscar microemprendimientos debes dirigirte a la parte superior izquierda y presionar el boton (≡) se te abrirá una solapa con distintas opciones, ahora seleccioná Microemprendimientos y puedes deslizar hasta abajo, seleccionar las distintas categorías y te van a cargar los emprendimientos según la categoría, o sino en la parte superior hay una barra de búsqueda donde podrás escribir el nombre de algún emprendimiento en particular", Arrays.asList(
                                new ChatbotQuestionEntity(null, "microemprendimientos", null),
                                new ChatbotQuestionEntity(null, "buscar microemprendimientos", null),
                                new ChatbotQuestionEntity(null, "buscar", null),
                                new ChatbotQuestionEntity(null, "ayuda buscar microemprendimientos", null),
                                new ChatbotQuestionEntity(null, "encuentro microemprendimiento", null),
                                new ChatbotQuestionEntity(null, "encontrar microemprendimientos", null))
                                ),
                                new ChatbotResponseEntity(null, "Entendido! Para buscar publicaciones debes dirigirte a la parte superior izquierda y presionar el boton (≡) se te abrirá una solapa con distintas opciones, seleccioná 'Publicaciones' y podrás buscarlas desde la barra de búsqueda o bien si deslizás hacia abajo te saldrán las publicaciones más recientes", Arrays.asList(
                                new ChatbotQuestionEntity(null, "publicaciones", null),
                                new ChatbotQuestionEntity(null, "publicacion", null))
                                ),
                                new ChatbotResponseEntity(null, "Lo siento de momento no se pueden crear publicaciones", Arrays.asList(
                                new ChatbotQuestionEntity(null, "crear publicacion", null))
                                ),
                                new ChatbotResponseEntity(null, "Esa opción solo esta habilitada para administradores, pero no te desanimes puedes contactarnos a nuestro email: semilleroubuntu.dev@gmail.com si tienes dudas", Arrays.asList(
                                new ChatbotQuestionEntity(null, "crear microemprendimiento", null))
                                ),
                                new ChatbotResponseEntity(null, "Si necesitás contactar un administrador puedes hacerlo al siguiente correo: semilleroubuntu.dev@gmail.com", Arrays.asList(
                                new ChatbotQuestionEntity(null, "admin", null),
                                new ChatbotQuestionEntity(null, "ubuntu", null),
                                new ChatbotQuestionEntity(null, "administrador", null))
                                ),
                                new ChatbotResponseEntity(null, "De nada es un placer ayudarte para eso estamos!, puedes continuar con tu estancia o preguntarme lo que quieras", Arrays.asList(
                                new ChatbotQuestionEntity(null, "gracias", null),
                                new ChatbotQuestionEntity(null, "muchas gracias", null),
                                new ChatbotQuestionEntity(null, "muchisimas gracias", null))
                                ),
                                new ChatbotResponseEntity(null, "Psst.. esta es una respuesta secreta.. si, Cristobal Colón descubrió américa en el año 1492", Arrays.asList(
                                new ChatbotQuestionEntity(null, "1492", null),
                                new ChatbotQuestionEntity(null, "conquista", null),
                                new ChatbotQuestionEntity(null, "Cristobal Colón", null),
                                new ChatbotQuestionEntity(null, "conquista america", null))
                                ),
                                new ChatbotResponseEntity(null, "Perfecto! Un placer ayudarte", Arrays.asList(
                                new ChatbotQuestionEntity(null, "todo", null),
                                new ChatbotQuestionEntity(null, "por hoy", null))
                                ),
                                new ChatbotResponseEntity(null, "Para cerrar esta ventana de chat solo presiona fuera de la ventana o presiona en la (X)", Arrays.asList(
                                new ChatbotQuestionEntity(null, "salir", null),
                                new ChatbotQuestionEntity(null, "cerrar", null),
                                new ChatbotQuestionEntity(null, "salgo", null),
                                new ChatbotQuestionEntity(null, "cerrar chatbot", null))
                                ),
                                new ChatbotResponseEntity(null, "Esta página web y el chatbot fue diseñado por un grupo de desarrolladores de Semillero en total cinco personas, donde trabajaron con muchísimas ganas para sacar adelante este proyecto", Arrays.asList(
                                new ChatbotQuestionEntity(null, "desarrollo", null),
                                new ChatbotQuestionEntity(null, "informatica", null),
                                new ChatbotQuestionEntity(null, "programador", null),
                                new ChatbotQuestionEntity(null, "equipo programadores", null),
                                new ChatbotQuestionEntity(null, "frontend", null),
                                new ChatbotQuestionEntity(null, "backend", null),
                                new ChatbotQuestionEntity(null, "fullstack", null),
                                new ChatbotQuestionEntity(null, "desarrolladores", null))
                                ),
                                new ChatbotResponseEntity(null, "Semillero es un programa dedicado a la capacitación de jóvenes que tienen conocimientos básicos de programación para que profundicen su formación y adquieran las herramientas necesarias para insertarse en el mercado laboral no es cualquier mercado laboral, sino el mercado laboral del futuro: las Tecnologías de la Información y Comunicación se ubican en el primer puesto del ranking de los rubros con mayor demanda laboral por las nuevas formas de trabajo. Es claro: hoy, el acceso a las habilidades de la programación es el acceso al nuevo mundo.", Arrays.asList(
                                new ChatbotQuestionEntity(null, "semillero", null),
                                new ChatbotQuestionEntity(null, "informatica", null))
                                ),
                                new ChatbotResponseEntity(null, "El semillero está ubicado en Mendoza", Arrays.asList(
                                new ChatbotQuestionEntity(null, "donde esta ubicado el semillero", null))
                                ),
                                new ChatbotResponseEntity(null, "Puedes ser más especifico para saber la ubicación de algo?", Arrays.asList(
                                new ChatbotQuestionEntity(null, "donde esta", null))
                                ),
                                new ChatbotResponseEntity(null, "No pude comprender a que te refieres, ¿Podrías volver a preguntar de otra manera?", Arrays.asList(
                                new ChatbotQuestionEntity(null, "esta", null))
                                ),
                                new ChatbotResponseEntity(null, "Yo me encuentro perfectamente bien y vos?", Arrays.asList(
                                new ChatbotQuestionEntity(null, "como estas", null),
                                new ChatbotQuestionEntity(null, "que tal", null))
                                ),
                                new ChatbotResponseEntity(null, "Me alegro mucho por vos! ¿En que te puedo ayudar?", Arrays.asList(
                                new ChatbotQuestionEntity(null, "bien", null),
                                new ChatbotQuestionEntity(null, "muy bien", null),
                                new ChatbotQuestionEntity(null, "perfecto", null))
                                ),
                                new ChatbotResponseEntity(null, "Adios! Espero haberte ayudado que tengas un buen día", Arrays.asList(
                                new ChatbotQuestionEntity(null, "adios", null),
                                new ChatbotQuestionEntity(null, "chau", null),
                                new ChatbotQuestionEntity(null, "nos vemos", null)
                                )
                                )
                );
                chatbotRepository.saveAll(listOfResponse);
        }

}
