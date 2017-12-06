package ru.semyonmi.extjsdemo.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import ru.semyonmi.extjsdemo.util.OAuthUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OAuthUtils oauthUtils;

    @ApiOperation(value = "Logout user")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout() {
        oauthUtils.removeToken();
    }
}
