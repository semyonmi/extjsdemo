package ru.semyonmi.extjsdemo.controller.v1;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiOperation;
import ru.semyonmi.extjsdemo.domain.Access;
import ru.semyonmi.extjsdemo.domain.Role;
import ru.semyonmi.extjsdemo.domain.User;
import ru.semyonmi.extjsdemo.dto.RoleDto;
import ru.semyonmi.extjsdemo.dto.UserDto;
import ru.semyonmi.extjsdemo.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
@PostAuthorize("#oauth2.isAdmin()")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @ApiOperation(value = "Get users", notes = "Return users")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<UserDto> get() {
        List<User> users = userService.get();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @ApiOperation(value = "Get user by id", notes = "Return user by id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserDto getById(@PathVariable("id") Long id) {
        return convertToDto(userService.get(Optional.ofNullable(id)));
    }

    @ApiOperation(value = "Create user", notes = "Create user")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserDto save(@RequestBody UserDto user) {
        User savedUser = userService.save(Optional.ofNullable(convertToEntity(user)));
        return convertToDto(savedUser);
    }

    @ApiOperation(value = "Update user", notes = "Update user")
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody UserDto user) {
        userService.save(Optional.ofNullable(convertToEntity(user)));
    }

    @ApiOperation(value = "Delete user", notes = "Delete user")
    @RequestMapping(method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam List<Long> id) {
        userService.delete(Optional.ofNullable(id));
    }

    private UserDto convertToDto(User user) {
        if (user == null)
            return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPassword(user.getPassword());
        dto.setRole(modelMapper.map(user.getRole(), RoleDto.class));
        dto.setLocked(user.isLocked());
        dto.setAccess(user.getAccess().getMask());
        return dto;
    }

    private User convertToEntity(UserDto dto) {
        if (dto == null)
            return null;
        User user = new User();
        user.setId(dto.getId());
        user.setLogin(dto.getLogin());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(dto.getPassword());
        user.setRole(modelMapper.map(dto.getRole(), Role.class));
        user.setLocked(dto.isLocked());
        user.setAccess(new Access(dto.getAccess()));
        return user;
    }
}
