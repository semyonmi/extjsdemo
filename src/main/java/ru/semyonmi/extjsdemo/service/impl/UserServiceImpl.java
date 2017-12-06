package ru.semyonmi.extjsdemo.service.impl;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import ru.semyonmi.extjsdemo.domain.User;
import ru.semyonmi.extjsdemo.exception.BadRequestException;
import ru.semyonmi.extjsdemo.repository.UserRepository;
import ru.semyonmi.extjsdemo.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private BCryptPasswordEncoder passEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User logon(String name, String password) throws UsernameNotFoundException, BadCredentialsException {
        Optional<User> user = Optional.ofNullable(userRepository.findByLogin(name));
        if (!user.isPresent())
            throw new UsernameNotFoundException(String.format("User \"%s\" not found", name));
        if (!passEncoder.matches(password, user.get().getPassword()))
            throw new BadCredentialsException("Password incorrect");
        return user.get();
    }

    @Override
    public List<User> get() {
        return Lists.newArrayList(userRepository.findAll());
    }

    @Override
    public User get(Optional<Long> idOptional) throws BadRequestException {
        Long id = idOptional.orElseThrow(() -> new BadRequestException("Id cannot be null"));
        User user = Optional.ofNullable(userRepository.findOne(id)).orElseThrow(
                () -> new BadRequestException(String.format("User \"%s\" not found", id)));
        return user;
    }

    @Override
    public User save(Optional<User> userOptional) throws BadRequestException {
        User user = userOptional.orElseThrow(
                () -> new BadRequestException(String.format("User cannot be null")));
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(passEncoder.encode(user.getPassword()));
        } else if (user.getId() != null) {
            User original = userRepository.findOne(user.getId());
            user.setPassword(original.getPassword());
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public void delete(Optional<List<Long>> ids) throws BadRequestException {
        ids.orElseThrow(() -> new BadRequestException("Id cannot be null"))
                .forEach(id -> userRepository.delete(id));
    }
}
