package ru.semyonmi.extjsdemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.semyonmi.extjsdemo.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public User findByLogin(String login);
}
