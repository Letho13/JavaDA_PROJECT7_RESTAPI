package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUserList (){
      return userRepository.findAll();
    }

    public void addUser (User user){
        userRepository.save(user);
    }

    public User getUserById (Integer id){
       return userRepository.findById(id)
               .orElseThrow(() -> new NoSuchElementException("User avec l'ID " + id + " introuvable !"));
    }

    public void updateUser (Integer id, User newUser){
       User existingUser = userRepository.findById(id)
               .orElseThrow(() -> new NoSuchElementException("User avec l'ID " + id + " introuvable !"));
       existingUser.setFullname(newUser.getFullname());
       existingUser.setUsername(newUser.getUsername());
       existingUser.setPassword(passwordEncode.encode(newUser.getPassword()));
       existingUser.setRole(newUser.getRole());

       userRepository.save(existingUser);
    }

    public void deleteById (Integer id){
        if (!userRepository.existsById(id)){
            throw new NoSuchElementException("User avec l'ID " + id + " introuvable !");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }


}
