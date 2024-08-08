package com.ubuntu.ubuntu_app.seeder;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.Repository.FAQRepository;
import com.ubuntu.ubuntu_app.model.entities.FAQEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ChatbotSeeder implements CommandLineRunner {

    private final FAQRepository faqRepository;

    @Override
    public void run(String... args) throws Exception {
        if (faqRepository.count() == 0) {
           loadChatbot();
        }
    }

    @Transactional
    private void loadChatbot() {
        List<FAQEntity> listSQL = Arrays.asList(
                //saludo
                new FAQEntity("hola",
                        "¡Bienvenido a Ubuntu! Estamos emocionados de ayudarte a encontrar oportunidades de inversión que no solo generen ganancias, sino que también promuevan un impacto positivo en el medio ambiente y la sociedad. ¿En qué tipo de proyectos sustentables estás interesado hoy? ¡Estoy aquí para ayudarte a explorar y encontrar las mejores opciones para ti!"),
                new FAQEntity("que tal",
                        "¡Bienvenido a Ubuntu! Estamos emocionados de ayudarte a encontrar oportunidades de inversión que no solo generen ganancias, sino que también promuevan un impacto positivo en el medio ambiente y la sociedad. ¿En qué tipo de proyectos sustentables estás interesado hoy? ¡Estoy aquí para ayudarte a explorar y encontrar las mejores opciones para ti!"),
                new FAQEntity("buenos dias",
                        "¡Bienvenido a Ubuntu! Estamos emocionados de ayudarte a encontrar oportunidades de inversión que no solo generen ganancias, sino que también promuevan un impacto positivo en el medio ambiente y la sociedad. ¿En qué tipo de proyectos sustentables estás interesado hoy? ¡Estoy aquí para ayudarte a explorar y encontrar las mejores opciones para ti!"),
               //objetivos empresa
                new FAQEntity("quienes son?",
                        "Somos un grupo de personas que brindamos la posibilidad de conectar a los inversores de impacto con los pryectos de micro emprendedores conociendo su propósito, contextom historia que motive a los inversores y con los proyectos para apalancar financieramente a empresas u organizaciones que busquen generar un impacto positivo"),
                new FAQEntity("que son?",
                        "Somos un grupo de personas que brindamos la posibilidad de conectar a los inversores de impacto con los pryectos de micro emprendedores conociendo su propósito, contextom historia que motive a los inversores y con los proyectos para apalancar financieramente a empresas u organizaciones que busquen generar un impacto positivo"),
                new FAQEntity("que hacen?",
                        "Impulsamos el desarrollo de finanzas de impacto, liderando la transición hacia un modelo financiero sostenible. Estamos emocionados de ayudarte a encontrar oportunidades de inversión que no solo generen ganancias, sino que también promuevan un impacto positivo en el medio ambiente y la sociedad"),
                new FAQEntity("a que se dedican",
                        "Impulsamos el desarrollo de finanzas de impacto, liderando la transición hacia un modelo financiero sostenible. Estamos emocionados de ayudarte a encontrar oportunidades de inversión que no solo generen ganancias, sino que también promuevan un impacto positivo en el medio ambiente y la sociedad"),
                new FAQEntity("cuales son sus objetivos?",
                        "Acercar a productores o microemprendedores, microcréditos que les permita desarrollar su emprendimiento.\n" +
                                " Financiamiento para empresas y organizaciones en lineas de proyectos socialm ambiental y cultural.\n" +
                                "Acercarles a potenciales inversores la posibilidad de invertir en proyectos de impacto."),
                new FAQEntity("a que rubro pertenecen",
                        "finanzas"),
                //categorias
                new FAQEntity("en que puedo invertir?",
                        "Tenemos microemprendimientos que estan compuestas por categorías estas pueden ser de  Economía social/Desarrollo local/Inclusión financiera\n" +
                                "\n" +
                                "Agroecología/Orgánicos/Alimentación saludable, Empresas/Organismos de impacto/Economía circular,  Conservación/Regeneración/Servicios ecosistémicos. Para saber mas pueder ir al siguiente enlace. "),
                new FAQEntity("invertir",
                        "Tenemos microemprendimientos que estan compuestas por categorías estas pueden ser de  Economía social/Desarrollo local/Inclusión financiera\n" +
                                "\n" +
                                "Agroecología/Orgánicos/Alimentación saludable, Empresas/Organismos de impacto/Economía circular,  Conservación/Regeneración/Servicios ecosistémicos. Para saber mas pueder ir al siguiente enlace. "),
                new FAQEntity("categorias",
                        "Tenemos microemprendimientos que estan compuestas por categorías estas pueden ser de  Economía social/Desarrollo local/Inclusión financiera\n" +
                                "\n" +
                                "Agroecología/Orgánicos/Alimentación saludable, Empresas/Organismos de impacto/Economía circular,  Conservación/Regeneración/Servicios ecosistémicos. Para saber mas pueder ir al siguiente enlace. "),
                new FAQEntity("donde puedo poner mi dinero para invertir",
                        "Tenemos microemprendimientos que estan compuestas por categorías estas pueden ser de  Economía social/Desarrollo local/Inclusión financiera\n" +
                                "\n" +
                                "Agroecología/Orgánicos/Alimentación saludable, Empresas/Organismos de impacto/Economía circular,  Conservación/Regeneración/Servicios ecosistémicos. Para saber mas pueder ir al siguiente enlace. "),
                //contacto emprendedores
                new FAQEntity("como puedo contactar con emprendedores?",
                        "Al ver el perfil de cada emprendedor, encontrarás un botón para CONECTAR con ellos."),
                new FAQEntity("emprendedores",
                        "Al ver el perfil de cada emprendedor, encontrarás un botón para CONECTAR con ellos."),
                new FAQEntity("como puedo comunicarme con emprendedores?",
                        "Al ver el perfil de cada emprendedor, encontrarás un botón para CONECTAR con ellos."),
                new FAQEntity("como puedo entablar comunicación con emprendedores?",
                        "Al ver el perfil de cada emprendedor, encontrarás un botón para CONECTAR con ellos."),
                //microemprendimientos
                new FAQEntity("como puedo agregar mi emprendimiento a Ubuntu?",
                        "En el siguiente enlace, encontrarás un formulario donde podrás ingresar tus datos, una descripción detallada de tu emprendimiento y fotos que resalten tu trabajo. Nos encantaría conocer más sobre lo que haces y cómo podemos colaborar juntos. ¡Esperamos ver tu información pronto!"),
                new FAQEntity("como agregar mi emprendimiento a Ubuntu",
                        "En el siguiente enlace, encontrarás un formulario donde podrás ingresar tus datos, una descripción detallada de tu emprendimiento y fotos que resalten tu trabajo. Nos encantaría conocer más sobre lo que haces y cómo podemos colaborar juntos. ¡Esperamos ver tu información pronto!"),
                new FAQEntity("quiero agregar mi microemprendimiento",
                        "En el siguiente enlace, encontrarás un formulario donde podrás ingresar tus datos, una descripción detallada de tu emprendimiento y fotos que resalten tu trabajo. Nos encantaría conocer más sobre lo que haces y cómo podemos colaborar juntos. ¡Esperamos ver tu información pronto!"),
                new FAQEntity("microemprendimientos",
                        "Puedes ver los microemprendimientos de microemprendimientos en la seccion o En el siguiente enlace, encontrarás un formulario donde podrás ingresar tus datos, una descripción detallada de tu emprendimiento y fotos que resalten tu trabajo. Nos encantaría conocer más sobre lo que haces y cómo podemos colaborar juntos. ¡Esperamos ver tu información pronto!")

                );
       faqRepository.saveAll(listSQL);
    }

}
