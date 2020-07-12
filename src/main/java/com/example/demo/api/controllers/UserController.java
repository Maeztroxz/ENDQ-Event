package com.example.demo.api.controllers;
import javax.validation.Valid;

import com.example.demo.api.dtos.LoginUserRequest;
import com.example.demo.api.dtos.LoginUserResponse;
import com.example.demo.api.dtos.UserRequest;
import com.example.demo.api.exceptions.BusinessException;
import com.example.demo.api.models.User;
import com.example.demo.api.repositories.UserRepository;
import com.example.demo.api.security.UserContext;
import com.example.demo.api.security.UserData;
import com.example.demo.api.security.UserDetailsImpl;
import com.example.demo.api.security.config.TokenProvider;
import com.example.demo.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserContext userContext;


    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> AddUser(@Valid @RequestBody UserRequest userRequest) {

        return ResponseEntity.ok(userService.addUser(userRequest));
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> generate(@RequestBody LoginUserRequest loginUser) throws AuthenticationException {
        Optional<User> userOptional = Optional.ofNullable(userService.findOne(loginUser.getEmail()));
        // normal user
        // enable user when accept privacy
        if(!userOptional.isPresent()) {
            System.out.printf("No existe el usuario.");
        }

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail().toLowerCase(),
                        loginUser.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = tokenProvider.generateToken(authentication);

        LoginUserResponse tokenResponse = new LoginUserResponse(token);
        tokenResponse.setLastName(userOptional.get().getLastName());
        tokenResponse.setFirstName(userOptional.get().getFirstName());
        tokenResponse.setEmail(userOptional.get().getEmail());

        return ResponseEntity.ok(tokenResponse);
    }



}
