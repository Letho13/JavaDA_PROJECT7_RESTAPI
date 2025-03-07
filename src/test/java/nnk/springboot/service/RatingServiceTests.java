package nnk.springboot.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import nnk.springboot.domain.Rating;
import nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

public class RatingServiceTests {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private Rating rating1;

    @Mock
    private Rating rating2;

    @InjectMocks
    private RatingService ratingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simuler le comportement des objets Rating
        when(rating1.getId()).thenReturn(1);
        when(rating1.getMoodysRating()).thenReturn("AAA");
        when(rating1.getSandPRating()).thenReturn("AA+");
        when(rating1.getFitchRating()).thenReturn("A");
        when(rating1.getOrderNumber()).thenReturn(1);

        when(rating2.getId()).thenReturn(2);
        when(rating2.getMoodysRating()).thenReturn("AA");
        when(rating2.getSandPRating()).thenReturn("A");
        when(rating2.getFitchRating()).thenReturn("BBB+");
        when(rating2.getOrderNumber()).thenReturn(2);
    }

    @Test
    public void testGetAllRating() {
        // Arrange
        when(ratingRepository.findAll()).thenReturn(Arrays.asList(rating1, rating2));

        // Act
        List<Rating> result = ratingService.getAllRating();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    public void testAddRating() {
        // Arrange
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating1);

        // Act
        ratingService.addRating(rating1);

        // Assert
        verify(ratingRepository, times(1)).save(rating1);
    }

    @Test
    public void testGetRatingById() {
        // Arrange
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating1));

        // Act
        Rating result = ratingService.getRatingById(1);

        // Assert
        assertNotNull(result);
        assertEquals(rating1, result);
        verify(ratingRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateRating() {
        // Arrange
        Rating updatedRating = mock(Rating.class);
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating1));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating1);

        // Simuler que le `updatedRating` possède des données modifiées
        when(updatedRating.getMoodysRating()).thenReturn("AA+");
        when(updatedRating.getSandPRating()).thenReturn("A+");
        when(updatedRating.getFitchRating()).thenReturn("BB");
        when(updatedRating.getOrderNumber()).thenReturn(3);

        // Act
        ratingService.updateRating(1, updatedRating);

        // Assert
        verify(ratingRepository, times(1)).findById(1);
        verify(ratingRepository, times(1)).save(rating1);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        when(ratingRepository.existsById(1)).thenReturn(true);
        doNothing().when(ratingRepository).deleteById(1);

        // Act
        ratingService.deleteById(1);

        // Assert
        verify(ratingRepository, times(1)).deleteById(1);
    }
}

