package com.ubuntu.ubuntu_app.infrastructure.country.seeder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.infrastructure.country.repository.CountryRepository;
import com.ubuntu.ubuntu_app.infrastructure.country.entity.CountryEntity;
import com.ubuntu.ubuntu_app.infrastructure.province.entity.ProvinceEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Order(1)
@ConditionalOnProperty(name = "app.seeding.enabled", havingValue = "true", matchIfMissing = true)
public class CountriesSeeder implements CommandLineRunner {

    private final CountryRepository countryRepository;

    @Override
    public void run(String... args){
        if (countryRepository.count() == 0) {
            loadCountries();
        }
    }

    private void loadCountries() {
        List<CountryEntity> countryEntities = new ArrayList<>();
        List<ProvinceEntity> listOfArgentinaProvinces = Arrays.asList(
                new ProvinceEntity("Buenos Aires"),
                new ProvinceEntity("Santa Fe"),
                new ProvinceEntity("Córdoba"),
                new ProvinceEntity("Entre Rios"),
                new ProvinceEntity("Mendoza"),
                new ProvinceEntity("San Juan"),
                new ProvinceEntity("San Luis"),
                new ProvinceEntity("La Pampa"),
                new ProvinceEntity("Santa Cruz"),
                new ProvinceEntity("Tierra del fuego"),
                new ProvinceEntity("Ciudad Autónoma de Buenos Aires"));
        List<ProvinceEntity> listOfBrasilProvinces = Arrays.asList(
                new ProvinceEntity("Sao Paulo"),
                new ProvinceEntity("Rio de Janeiro"),
                new ProvinceEntity("Amazonas"),
                new ProvinceEntity("Pernambuco"),
                new ProvinceEntity("Mato Grosso"),
                new ProvinceEntity("Minas Gerais"),
                new ProvinceEntity("Rio Grande do Sul"),
                new ProvinceEntity("Goiás"),
                new ProvinceEntity("Bahía"),
                new ProvinceEntity("Paraíba"),
                new ProvinceEntity("Rio Grande do Norte"));
        List<ProvinceEntity> listOfUruguayProvinces = Arrays.asList(
                new ProvinceEntity("Montevideo"),
                new ProvinceEntity("Canelones"),
                new ProvinceEntity("Maldonado"),
                new ProvinceEntity("Salto"),
                new ProvinceEntity("Colonia"),
                new ProvinceEntity("Paysandú"),
                new ProvinceEntity("San José"),
                new ProvinceEntity("Rivera"),
                new ProvinceEntity("Tacuarembó"),
                new ProvinceEntity("Cerro Largo"),
                new ProvinceEntity("Artigas"));
        countryEntities.add(new CountryEntity("Argentina", listOfArgentinaProvinces));
        countryEntities.add(new CountryEntity("Brasil", listOfBrasilProvinces));
        countryEntities.add(new CountryEntity("Uruguay", listOfUruguayProvinces));
        countryRepository.saveAll(countryEntities);
    }

}
