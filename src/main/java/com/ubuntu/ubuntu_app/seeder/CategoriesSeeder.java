package com.ubuntu.ubuntu_app.seeder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.Repository.CategoryRepository;
import com.ubuntu.ubuntu_app.model.CategoryEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CategoriesSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            loadCategories();
        }
    }

    @Transactional
    private void loadCategories() {
        List<CategoryEntity> categories = new ArrayList<>();
        categories.add(new CategoryEntity("Economía social/Desarrollo local/Inclusión financiera"));
        categories.add(new CategoryEntity("Agroecología/Orgánicos/Alimentación saludable"));
        categories.add(new CategoryEntity("Conservación/Regeneración/Servicios ecosistémicos"));
        categories.add(new CategoryEntity("Empresas/Organismos de impacto/Economía circular"));
        categoryRepository.saveAll(categories);
    }
}
