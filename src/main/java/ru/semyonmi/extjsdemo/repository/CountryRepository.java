package ru.semyonmi.extjsdemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.semyonmi.extjsdemo.domain.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
}
