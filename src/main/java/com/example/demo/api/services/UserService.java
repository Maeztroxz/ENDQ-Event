package com.example.demo.api.services;

import com.example.demo.api.dtos.LoginUserRequest;
import com.example.demo.api.dtos.UserRegisterResponse;
import com.example.demo.api.dtos.UserRequest;
import com.example.demo.api.exceptions.BusinessException;
import com.example.demo.api.exceptions.ForbiddenException;
import com.example.demo.api.models.User;
import com.example.demo.api.repositories.UserRepository;

import com.example.demo.api.security.UserData;
import com.example.demo.api.security.config.TokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service(value = "userService")
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptEncoder;




    public UserRegisterResponse addUser (UserRequest userComing){

        User userToSave = new User();
        Optional<User> userOptional = Optional.ofNullable(findOne(userComing.getEmail()));

        if(!userComing.getEmail().isEmpty()
                && !userComing.getFirstName().isEmpty()
                && !userComing.getLastName().isEmpty()
                && !userComing.getGender().isEmpty()
                && !userComing.getPassword().isEmpty()) {
                if (userOptional.isPresent()) {
                    throw new ForbiddenException("This email is already registered.");
                }
                if(!lettersValidation(userComing.getFirstName()) || !lettersValidation(userComing.getLastName())){
                    throw new BusinessException("First name and Last name fields only accept alphabet values.");}
            userToSave.setFirstName(userComing.getFirstName());

            userToSave.setLastName(userComing.getLastName());
                if(!emailValidation(userComing.getEmail())){
                    throw new BusinessException("Email invalid.");}
            userToSave.setEmail(userComing.getEmail());

                if(userComing.getPassword().length()>=8){
                    userToSave.setPassword(bCryptEncoder.encode(userComing.getPassword()));}
                else  throw new BusinessException("Password must be at least 8 characters.");
            userToSave.setGender(userComing.getGender());
            userRepository.save(userToSave);
            UserRegisterResponse userRegisterResponse = this.modelMapper.map(userToSave, UserRegisterResponse.class);
            return userRegisterResponse;
        }
        else
             throw new BusinessException("Some fields are empty.");

    }

    public String login(LoginUserRequest loginUserRequest) {

        Optional<User> userOptional = Optional.ofNullable(findOne(loginUserRequest.getEmail()));
        if (!userOptional.isPresent()) {
            throw new BusinessException("No existe el usuario.");
        } else {
            User user = userOptional.get();
            if (user.getPassword() != loginUserRequest.getPassword())
                return "Acceso consedido";
            else
                return "Password or email erroneo";
        }


    }

    public User findOne(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public boolean lettersValidation(String text)
    {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z]|[à-ú]|[À-Ú]|\\s)+$");
        Matcher mather = pattern.matcher(text);

        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean emailValidation(String email){
        // Patrón para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);

        if (mather.find() == true) {
            return true;
        } else {
           return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findOne(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new UserData(user.getEmail(), user.getPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGender()
        );
    }
}
