package com.tutorial.demo.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path="api/v1/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/token")
    public LoginResponse token(@RequestBody LoginRequest userLogin) {
        logger.info(userLogin.toString());
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.username(), userLogin.password()));
        var userAsString = authentication.getPrincipal().toString();

        var jwtToken = tokenService.generateToken(authentication);
        var role = tokenService.getRoleFromToken(userAsString);
        var id = tokenService.getIdFromToken(userAsString);
        return new LoginResponse(jwtToken, role, id);
    }
}
