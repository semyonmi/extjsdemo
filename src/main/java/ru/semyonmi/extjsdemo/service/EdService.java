package ru.semyonmi.extjsdemo.service;


import java.util.List;
import java.util.Optional;

import ru.semyonmi.extjsdemo.domain.Company;
import ru.semyonmi.extjsdemo.domain.Country;

public interface EdService {

    List<Country> getCountries();
    Country getCountry(Optional<Long> id);
    Country saveCountry(Optional<Country> country);
    void deleteCountries(Optional<List<Long>> ids);

    List<Company> getCompanies();
    Company getCompany(Optional<Long> id);
    Company saveCompany(Optional<Company> company);
    void deleteCompanies(Optional<List<Long>> ids);
}
