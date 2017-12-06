package ru.semyonmi.extjsdemo.service.impl;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import ru.semyonmi.extjsdemo.domain.Company;
import ru.semyonmi.extjsdemo.domain.Country;
import ru.semyonmi.extjsdemo.exception.BadRequestException;
import ru.semyonmi.extjsdemo.repository.CompanyRepository;
import ru.semyonmi.extjsdemo.repository.CountryRepository;
import ru.semyonmi.extjsdemo.service.EdService;

@Service
@Transactional
public class EdServiceImpl implements EdService{

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<Country> getCountries() {
        return Lists.newArrayList(countryRepository.findAll());
    }

    @Override
    public Country getCountry(Optional<Long> optionalId) {
        Long id = optionalId.orElseThrow(() -> new BadRequestException("Id cannot be null"));
        return Optional.ofNullable(countryRepository.findOne(id)).orElseThrow(
                () -> new BadRequestException(String.format("Country \"%s\" not found", id)));
    }

    @Override
    public Country saveCountry(Optional<Country> country) {
        return countryRepository.save(country.orElseThrow(
                () -> new BadRequestException(String.format("User cannot be null"))));
    }

    @Override
    public void deleteCountries(Optional<List<Long>> ids) {
        ids.orElseThrow(() -> new BadRequestException("Id cannot be null"))
                .forEach(id -> countryRepository.delete(id));
    }

    @Override
    public List<Company> getCompanies() {
        return Lists.newArrayList(companyRepository.findAll());
    }

    @Override
    public Company getCompany(Optional<Long> optionalId) {
        Long id = optionalId.orElseThrow(() -> new BadRequestException("Id cannot be null"));
        return Optional.ofNullable(companyRepository.findOne(id)).orElseThrow(
                () -> new BadRequestException(String.format("Company \"%s\" not found", id)));
    }

    @Override
    public Company saveCompany(Optional<Company> company) {
        return companyRepository.save(company.orElseThrow(
                () -> new BadRequestException(String.format("User cannot be null"))));
    }

    @Override
    public void deleteCompanies(Optional<List<Long>> ids) {
        ids.orElseThrow(() -> new BadRequestException("Id cannot be null"))
                .forEach(id -> companyRepository.delete(id));
    }
}
