package nnk.springboot.service;

import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

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
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Le mot de passe ne respecte pas les critères de sécurité !");
        }
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

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

        log.info("Utilisateur trouvé : {} avec le rôle {}", user.getUsername(), user.getRole());
        return new UserDetailsImpl(user);
    }

    @Transactional
    public void createTemporaryUser() {
        String tempPassword = UUID.randomUUID().toString().substring(0, 8); // Génère un mot de passe aléatoire
        System.out.println("🔑 Mot de passe User temporaire : " + tempPassword); // Affiche le mot de passe dans la console

        User tempUser = new User();
        tempUser.setUsername("tempuser");
        tempUser.setFullname("Temporary User");
        tempUser.setPassword(passwordEncoder.encode(tempPassword));
        tempUser.setRole("USER");

        userRepository.findByUsername("tempuser").ifPresent(userRepository::delete);

        userRepository.save(tempUser);
    }

    @Transactional
    public void createTemporaryAdmin() {
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        System.out.println("🔑 Mot de passe Admin temporaire : " + tempPassword);

        User tempAdmin = new User();
        tempAdmin.setUsername("tempadmin");
        tempAdmin.setFullname("Temporary Admin");
        tempAdmin.setPassword(passwordEncoder.encode(tempPassword));
        tempAdmin.setRole("ADMIN");

        userRepository.findByUsername("tempadmin").ifPresent(userRepository::delete);

        userRepository.save(tempAdmin);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordRegex);
    }

}
