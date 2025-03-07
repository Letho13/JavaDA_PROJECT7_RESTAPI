package nnk.springboot.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import nnk.springboot.domain.Trade;
import nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

public class TradeServiceTests {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private Trade trade1;

    @Mock
    private Trade trade2;

    @InjectMocks
    private TradeService tradeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simuler le comportement des objets Trade
        when(trade1.getId()).thenReturn(1);
        when(trade1.getAccount()).thenReturn("Account1");
        when(trade1.getType()).thenReturn("Type1");
        when(trade1.getBuyQuantity()).thenReturn(100.0);

        when(trade2.getId()).thenReturn(2);
        when(trade2.getAccount()).thenReturn("Account2");
        when(trade2.getType()).thenReturn("Type2");
        when(trade2.getBuyQuantity()).thenReturn(200.0);
    }

    @Test
    public void testGetAllTrade() {
        // Arrange
        when(tradeRepository.findAll()).thenReturn(Arrays.asList(trade1, trade2));

        // Act
        List<Trade> result = tradeService.getAllTrade();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tradeRepository, times(1)).findAll();
    }

    @Test
    public void testAddTrade() {
        // Arrange
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade1);

        // Act
        tradeService.addTrade(trade1);

        // Assert
        verify(tradeRepository, times(1)).save(trade1);
    }

    @Test
    public void testGetTradeById() {
        // Arrange
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade1));

        // Act
        Trade result = tradeService.getTradeById(1);

        // Assert
        assertNotNull(result);
        assertEquals(trade1, result);
        verify(tradeRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateTrade() {
        // Arrange
        Trade updatedTrade = mock(Trade.class);
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade1));
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade1);

        // Simuler que le `updatedTrade` possède des données modifiées
        when(updatedTrade.getAccount()).thenReturn("UpdatedAccount");
        when(updatedTrade.getType()).thenReturn("UpdatedType");
        when(updatedTrade.getBuyQuantity()).thenReturn(500.0);

        // Act
        tradeService.updateTrade(1, updatedTrade);

        // Assert
        verify(tradeRepository, times(1)).findById(1);
        verify(tradeRepository, times(1)).save(trade1);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        when(tradeRepository.existsById(1)).thenReturn(true);
        doNothing().when(tradeRepository).deleteById(1);

        // Act
        tradeService.deleteById(1);

        // Assert
        verify(tradeRepository, times(1)).deleteById(1);
    }
}
