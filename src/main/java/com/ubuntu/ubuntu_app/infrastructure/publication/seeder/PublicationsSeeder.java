package com.ubuntu.ubuntu_app.infrastructure.publication.seeder;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.infrastructure.media.entity.ImageEntity;
import com.ubuntu.ubuntu_app.infrastructure.publication.entity.PublicationEntity;
import com.ubuntu.ubuntu_app.infrastructure.publication.repository.PublicationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Order(5)
@ConditionalOnProperty(name = "app.seeding.enabled", havingValue = "true", matchIfMissing = true)
public class PublicationsSeeder implements CommandLineRunner {

    private final PublicationRepository publicationRepository;

    @Override
    public void run(String... args) throws Exception {
        if (publicationRepository.count() == 0) {
            seedPublications();
        }
    }

    @Transactional
    private void seedPublications() {
        List<PublicationEntity> seedList = List.of(
                new PublicationEntity(
                        null,
                        "Finanzas sostenibles: El auge de la inversión con propósito",
                        "El ecosistema de inversiones de impacto experimenta un crecimiento récord en América Latina. Cada vez más fondos dirigen su capital hacia proyectos con rentabilidad financiera y retorno social medible.",
                        LocalDate.now().minusDays(2),
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1579621970563-ebec7560ff3e?w=800&auto=format&fit=crop&q=80"))),
                new PublicationEntity(
                        null,
                        "Microcréditos comunitarios: Transformando economías locales",
                        "Los microcréditos continúan siendo una herramienta fundamental para democratizar el acceso al capital productivo, impulsando a cooperativas y pequeños emprendimientos liderados por mujeres.",
                        LocalDate.now().minusDays(5),
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=800&auto=format&fit=crop&q=80"))),
                new PublicationEntity(
                        null,
                        "Agroecología y regeneración: Hacia un modelo productivo consciente",
                        "Prácticas agrícolas regenerativas restauran la salud del suelo y capturan carbono, generando alimentos limpios y asegurando precios justos para los productores familiares.",
                        LocalDate.now().minusDays(9),
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=800&auto=format&fit=crop&q=80"))),
                new PublicationEntity(
                        null,
                        "Economía circular: Reduciendo la huella ambiental en PyMEs",
                        "El rediseño de procesos industriales permite a empresas emergentes transformar residuos en materias primas de alto valor, cerrando ciclos y reduciendo emisiones de CO2.",
                        LocalDate.now().minusDays(14),
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1532996122724-e3c354a0b15b?w=800&auto=format&fit=crop&q=80"))),
                new PublicationEntity(
                        null,
                        "Transición energética y comunidades solares",
                        "Modelos de generación distribuida de energía renovable empoderan a barrios y comunidades rurales, garantizando autonomía energética y sostenibilidad a largo plazo.",
                        LocalDate.now().minusDays(20),
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1509391365360-2e959784a276?w=800&auto=format&fit=crop&q=80"))),
                new PublicationEntity(
                        null,
                        "Innovación y alianzas para el impacto colectivo",
                        "La articulación entre el sector privado, organizaciones sin fines de lucro y plataformas digitales como Ubuntu potencia el alcance y la escala de soluciones a desafíos socioambientales.",
                        LocalDate.now().minusDays(28),
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1556761175-5973dc0f32e7?w=800&auto=format&fit=crop&q=80")))
        );

        publicationRepository.saveAll(seedList);
    }
}
