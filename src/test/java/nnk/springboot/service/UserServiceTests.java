package nnk.springboot.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private User user1;

    @Mock
    private User user2;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simuler le comportement des objets User
        when(user1.getId()).thenReturn(1);
        when(user1.getUsername()).thenReturn("user1");
        when(user1.getPassword()).thenReturn("Password1@");
        when(user1.getFullname()).thenReturn("User One");
        when(user1.getRole()).thenReturn("USER");

        when(user2.getId()).thenReturn(2);
        when(user2.getUsername()).thenReturn("user2");
        when(user2.getPassword()).thenReturn("Password2@");
        when(user2.getFullname()).thenReturn("User Two");
        when(user2.getRole()).thenReturn("ADMIN");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testAddUser() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(user1);

        // Act
        userService.addUser(user1);

        // Assert
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    public void testGetUserById() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        // Act
        User result = userService.getUserById(1);

        // Assert
        assertNotNull(result);
        assertEquals(user1, result);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        User updatedUser = mock(User.class);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        // Simuler que le `updatedUser` possède des données modifiées
        when(updatedUser.getFullname()).thenReturn("Updated User One");
        when(updatedUser.getUsername()).thenReturn("user1");
        when(updatedUser.getPassword()).thenReturn("UpdatedPassword1@");
        when(updatedUser.getRole()).thenReturn("USER");

        // Act
        userService.updateUser(1, updatedUser);

        // Assert
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        // Act
        userService.deleteById(1);

        // Assert
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    public void testLoadUserByUsername() {
        // Arrange
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));

        // Act
        UserDetailsImpl result = (UserDetailsImpl) userService.loadUserByUsername("user1");

        // Assert
        assertNotNull(result);
        assertEquals(user1.getUsername(), result.getUsername());  // Vérifier que le username est correct
        assertEquals(user1.getPassword(), result.getPassword());  // Vérifier que le mot de passe est correct
        assertEquals("ROLE_"+ user1.getRole(), result.getAuthorities().iterator().next().getAuthority());  // Vérifier le rôle
        verify(userRepository, times(1)).findByUsername("user1");
    }

    @Test
    public void testCreateTemporaryUser() {
        // Arrange
        when(userRepository.findByUsername("tempuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act
        userService.createTemporaryUser();

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateTemporaryAdmin() {
        // Arrange
        when(userRepository.findByUsername("tempadmin")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act
        userService.createTemporaryAdmin();

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }
}

