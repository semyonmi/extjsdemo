package ru.semyonmi.extjsdemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.semyonmi.extjsdemo.domain.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
}
