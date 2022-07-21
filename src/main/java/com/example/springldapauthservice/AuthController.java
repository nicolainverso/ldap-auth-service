package com.example.ldapauthservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LdapService ldapService;

    @PostMapping("/user")
    public ResponseEntity<String> authenticate(@RequestParam(name = "username") String username,
                                               @RequestParam(name = "password") String password) throws Exception {
        String jwtString = ldapService.authenticate(username,password);
        return new ResponseEntity<String>(jwtString, HttpStatus.OK);
    }
}
