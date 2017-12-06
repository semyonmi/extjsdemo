package ru.semyonmi.extjsdemo.service;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import ru.semyonmi.extjsdemo.domain.User;
import ru.semyonmi.extjsdemo.exception.BadRequestException;

public interface UserService {

    /**
     *
     * @param name
     * @param password
     * @return User
     * @throws UsernameNotFoundException if user not found
     * @throws BadCredentialsException if password incorrect
     */
    User logon(String name, String password) throws UsernameNotFoundException, BadCredentialsException;

    List<User> get();

    /**
     *
     * @param id
     * @return User
     * @throws BadRequestException - if user not found by id
     */
    User get(Optional<Long> id) throws BadRequestException;

    User save(Optional<User> userOptional) throws BadRequestException;

    void delete(Optional<List<Long>> ids) throws BadRequestException;
}
