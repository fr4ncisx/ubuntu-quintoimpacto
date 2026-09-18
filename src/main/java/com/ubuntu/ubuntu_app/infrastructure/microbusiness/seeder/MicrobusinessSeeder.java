package com.ubuntu.ubuntu_app.infrastructure.microbusiness.seeder;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.infrastructure.category.entity.CategoryEntity;
import com.ubuntu.ubuntu_app.infrastructure.category.repository.CategoryRepository;
import com.ubuntu.ubuntu_app.infrastructure.media.entity.ImageEntity;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity.MicrobusinessEntity;
import com.ubuntu.ubuntu_app.infrastructure.microbusiness.repository.MicrobusinessRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Order(4)
@ConditionalOnProperty(name = "app.seeding.enabled", havingValue = "true", matchIfMissing = true)
public class MicrobusinessSeeder implements CommandLineRunner {

    private final MicrobusinessRepository microbusinessRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (microbusinessRepository.count() == 0) {
            seedMicrobusinesses();
        }
    }

    private void seedMicrobusinesses() {
        List<CategoryEntity> allCategories = categoryRepository.findAll();
        CategoryEntity ecoSocial = allCategories.size() > 0 ? allCategories.get(0) : null;
        CategoryEntity agroecologia = allCategories.size() > 1 ? allCategories.get(1) : ecoSocial;
        CategoryEntity ecosistemas = allCategories.size() > 2 ? allCategories.get(2) : ecoSocial;
        CategoryEntity economiaCircular = allCategories.size() > 3 ? allCategories.get(3) : ecoSocial;

        List<MicrobusinessEntity> seedList = List.of(
                new MicrobusinessEntity(
                        null,
                        "Tejiendo Raíces",
                        "Cooperativa textil de mujeres rurales dedicadas a la producción de prendas con materiales reciclados y tintes naturales.",
                        "Impacto local y capacitación continua para más de 20 familias.",
                        LocalDate.now().minusDays(10),
                        "Argentina",
                        "Mendoza",
                        "Maipú",
                        ecoSocial,
                        "Textil Sostenible",
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1596484552834-8a58f7009470?w=800&auto=format&fit=crop&q=80")),
                        false
                ),
                new MicrobusinessEntity(
                        null,
                        "Tierra Fértil Orgánicos",
                        "Producción de hortalizas y frutales bajo un estricto régimen agroecológico, sin pesticidas y promoviendo el consumo de cercanía.",
                        "Certificación orgánica en trámite, envíos directos a consumidores.",
                        LocalDate.now().minusDays(20),
                        "Argentina",
                        "Buenos Aires",
                        "Luján",
                        agroecologia,
                        "Agricultura",
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1542838132-92c53300491e?w=800&auto=format&fit=crop&q=80")),
                        false
                ),
                new MicrobusinessEntity(
                        null,
                        "Bosque Vivo",
                        "Proyecto de restauración de bosque nativo combinado con turismo sustentable y educación ambiental para escuelas locales.",
                        "Se han reforestado más de 5 hectáreas en los últimos dos años.",
                        LocalDate.now().minusDays(30),
                        "Argentina",
                        "Córdoba",
                        "Traslasierra",
                        ecosistemas,
                        "Turismo Sostenible",
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1511497584788-876760111969?w=800&auto=format&fit=crop&q=80")),
                        false
                ),
                new MicrobusinessEntity(
                        null,
                        "Recicla Tech",
                        "Empresa de recuperación de hardware y plásticos electrónicos para su transformación en mobiliario y materiales de construcción.",
                        "Evitamos que 10 toneladas de RAEE lleguen a basurales anualmente.",
                        LocalDate.now().minusDays(5),
                        "Argentina",
                        "Santa Fe",
                        "Rosario",
                        economiaCircular,
                        "Reciclaje",
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1532996122724-e3c354a0b15b?w=800&auto=format&fit=crop&q=80")),
                        false
                ),
                new MicrobusinessEntity(
                        null,
                        "Mieles del Valle",
                        "Apicultura biodinámica que preserva las abejas y la flora autóctona. Producción de miel cruda y derivados.",
                        "Método de extracción artesanal y libre de agroquímicos.",
                        LocalDate.now().minusDays(15),
                        "Argentina",
                        "Río Negro",
                        "El Bolsón",
                        agroecologia,
                        "Apicultura",
                        true,
                        List.of(new ImageEntity("https://images.unsplash.com/photo-1587049352847-81a56d773c1c?w=800&auto=format&fit=crop&q=80")),
                        false
                )
        );

        for(MicrobusinessEntity m : seedList) {
            if (m.getCategory() != null) {
                m.setCategory(categoryRepository.findById(m.getCategory().getId()).orElse(null));
            }
        }

        microbusinessRepository.saveAll(seedList);
    }
}
