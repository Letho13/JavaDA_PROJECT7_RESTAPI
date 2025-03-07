package nnk.springboot.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import nnk.springboot.domain.BidList;
import nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

public class BidListServiceTests {

    @Mock
    private BidListRepository bidListRepository;

    @Mock
    private BidList bid1;

    @Mock
    private BidList bid2;

    @InjectMocks
    private BidListService bidListService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // On peut simuler les comportements des objets BidList directement
        when(bid1.getId()).thenReturn(1);
        when(bid1.getAccount()).thenReturn("Account1");
        when(bid1.getType()).thenReturn("Type1");
        when(bid1.getBidQuantity()).thenReturn(100.0);

        when(bid2.getId()).thenReturn(2);
        when(bid2.getAccount()).thenReturn("Account2");
        when(bid2.getType()).thenReturn("Type2");
        when(bid2.getBidQuantity()).thenReturn(200.0);
    }

    @Test
    public void testGetAllBidList() {
        // Arrange
        when(bidListRepository.findAll()).thenReturn(Arrays.asList(bid1, bid2));

        // Act
        List<BidList> result = bidListService.getAllBidList();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bidListRepository, times(1)).findAll();
    }

    @Test
    public void testAddBid() {
        // Arrange
        // Nous mockons le comportement de la méthode save() pour qu'elle retourne un objet BidList sans rien faire
        when(bidListRepository.save(any(BidList.class))).thenReturn(bid1);

        // Act
        bidListService.addBid(bid1);

        // Assert
        verify(bidListRepository, times(1)).save(bid1);
    }

    @Test
    public void testGetBidById() {
        // Arrange
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid1));

        // Act
        BidList result = bidListService.getBidById(1);

        // Assert
        assertNotNull(result);
        assertEquals(bid1, result);
        verify(bidListRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateBid() {
        // Arrange
        BidList updatedBid = mock(BidList.class);
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid1));
        when(bidListRepository.save(any(BidList.class))).thenReturn(bid1);

        // Simule que le `updatedBid` possède des données modifiées
        when(updatedBid.getAccount()).thenReturn("UpdatedAccount");
        when(updatedBid.getType()).thenReturn("UpdatedType");
        when(updatedBid.getBidQuantity()).thenReturn(500.0);

        // Act
        bidListService.updateBid(1, updatedBid);

        // Assert
        verify(bidListRepository, times(1)).findById(1);
        verify(bidListRepository, times(1)).save(bid1);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        when(bidListRepository.existsById(1)).thenReturn(true);
        doNothing().when(bidListRepository).deleteById(1);

        // Act
        bidListService.deleteById(1);

        // Assert
        verify(bidListRepository, times(1)).deleteById(1);
    }
}
