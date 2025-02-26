package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur avec l'ID " + id + " introuvable !"));
    }

    public void updateUser(Integer id, User newUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur avec l'ID " + id + " introuvable !"));

        existingUser.setFullname(newUser.getFullname());
        existingUser.setUsername(newUser.getUsername());
        if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newUser.getPassword())); // 🔹 Mise à jour sécurisée
        }
        existingUser.setRole(newUser.getRole());

        userRepository.save(existingUser);
    }


    public void deleteById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("Utilisateur avec l'ID " + id + " introuvable !");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        log.info("Utilisateur trouvé : {} avec le rôle {}", user.getUsername(), user.getRole());
        return new UserDetailsImpl(user);
    }

}
