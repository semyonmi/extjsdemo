package ru.semyonmi.extjsdemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.semyonmi.extjsdemo.domain.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    public Role findByIdent(String ident);
}
