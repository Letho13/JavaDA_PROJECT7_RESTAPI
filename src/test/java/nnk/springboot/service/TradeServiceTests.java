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
        when(tradeRepository.findAll()).thenReturn(Arrays.asList(trade1, trade2));

        List<Trade> result = tradeService.getAllTrade();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tradeRepository, times(1)).findAll();
    }

    @Test
    public void testAddTrade() {
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade1);

        tradeService.addTrade(trade1);

        verify(tradeRepository, times(1)).save(trade1);
    }

    @Test
    public void testGetTradeById() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade1));

        Trade result = tradeService.getTradeById(1);

        assertNotNull(result);
        assertEquals(trade1, result);
        verify(tradeRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateTrade() {
        Trade updatedTrade = mock(Trade.class);
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade1));
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade1);

        when(updatedTrade.getAccount()).thenReturn("UpdatedAccount");
        when(updatedTrade.getType()).thenReturn("UpdatedType");
        when(updatedTrade.getBuyQuantity()).thenReturn(500.0);

        tradeService.updateTrade(1, updatedTrade);

        verify(tradeRepository, times(1)).findById(1);
        verify(tradeRepository, times(1)).save(trade1);
    }

    @Test
    public void testDeleteById() {
        when(tradeRepository.existsById(1)).thenReturn(true);
        doNothing().when(tradeRepository).deleteById(1);

        tradeService.deleteById(1);

        verify(tradeRepository, times(1)).deleteById(1);
    }
}
