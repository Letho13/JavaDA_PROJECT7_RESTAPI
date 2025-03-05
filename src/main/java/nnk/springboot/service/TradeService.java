package nnk.springboot.service;

import nnk.springboot.domain.Trade;
import nnk.springboot.repositories.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public List<Trade> getAllTrade() {
        return tradeRepository.findAll();
    }

    public void addTrade(Trade trade) {
        tradeRepository.save(trade);
    }

    public Trade getTradeById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Trade avec l'ID " + id + " introuvable !"));
    }

    public void updateTrade(Integer id, Trade newTrade) {
        Trade existingTrade = tradeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Trade avec l'ID " + id + " introuvable !"));
        existingTrade.setAccount(newTrade.getAccount());
        existingTrade.setType(newTrade.getType());
        existingTrade.setBuyQuantity(newTrade.getBuyQuantity());

        tradeRepository.save(existingTrade);
    }

    public void deleteById (Integer id){
        if (!tradeRepository.existsById(id)) {
            throw new NoSuchElementException("Rating avec l'ID " + id + " introuvable !");
        }
        tradeRepository.deleteById(id);
    }

}
