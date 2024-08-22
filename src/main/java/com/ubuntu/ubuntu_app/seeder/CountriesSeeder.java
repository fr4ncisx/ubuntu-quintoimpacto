package com.ubuntu.ubuntu_app.seeder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.Repository.PaisRepository;
import com.ubuntu.ubuntu_app.model.entities.PaisEntity;
import com.ubuntu.ubuntu_app.model.entities.ProvinciaEntity;

import lombok.RequiredArgsConstructor;

@Lazy
@RequiredArgsConstructor
@Component
public class CountriesSeeder implements CommandLineRunner {

    private final PaisRepository paisRepository;

    @Override
    public void run(String... args) throws Exception {
        if (paisRepository.count() == 0) {
            loadCountries();
        }
    }

    @Transactional
    private void loadCountries() {
        List<PaisEntity> paisEntity = new ArrayList<>();
        List<ProvinciaEntity> listOfArgentinaProvinces = Arrays.asList(
                new ProvinciaEntity("Buenos Aires"),
                new ProvinciaEntity("Santa Fe"),
                new ProvinciaEntity("Córdoba"),
                new ProvinciaEntity("Entre Rios"),
                new ProvinciaEntity("Mendoza"),
                new ProvinciaEntity("San Juan"),
                new ProvinciaEntity("San Luis"),
                new ProvinciaEntity("La Pampa"),
                new ProvinciaEntity("Santa Cruz"),
                new ProvinciaEntity("Tierra del fuego"),
                new ProvinciaEntity("Ciudad Autónoma de Buenos Aires"));
        List<ProvinciaEntity> listOfBrasilProvinces = Arrays.asList(
                new ProvinciaEntity("Sao Paulo"),
                new ProvinciaEntity("Rio de Janeiro"),
                new ProvinciaEntity("Amazonas"),
                new ProvinciaEntity("Pernambuco"),
                new ProvinciaEntity("Mato Grosso"),
                new ProvinciaEntity("Minas Gerais"),
                new ProvinciaEntity("Rio Grande do Sul"),
                new ProvinciaEntity("Goiás"),
                new ProvinciaEntity("Bahía"),
                new ProvinciaEntity("Paraíba"),
                new ProvinciaEntity("Rio Grande do Norte"));
        List<ProvinciaEntity> listOfUruguayProvinces = Arrays.asList(
                new ProvinciaEntity("Montevideo"),
                new ProvinciaEntity("Canelones"),
                new ProvinciaEntity("Maldonado"),
                new ProvinciaEntity("Salto"),
                new ProvinciaEntity("Colonia"),
                new ProvinciaEntity("Paysandú"),
                new ProvinciaEntity("San José"),
                new ProvinciaEntity("Rivera"),
                new ProvinciaEntity("Tacuarembó"),
                new ProvinciaEntity("Cerro Largo"),
                new ProvinciaEntity("Artigas"));
        paisEntity.add(new PaisEntity("Argentina", listOfArgentinaProvinces));
        paisEntity.add(new PaisEntity("Brasil", listOfBrasilProvinces));
        paisEntity.add(new PaisEntity("Uruguay", listOfUruguayProvinces));
        paisRepository.saveAll(paisEntity);
    }

}
