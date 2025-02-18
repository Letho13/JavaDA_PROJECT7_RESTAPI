package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BidListService {

    private final BidListRepository bidListRepository;

    public List<BidList> getAllBidList() {
        return bidListRepository.findAll();
    }

    public void addBid(BidList bid) {
        bidListRepository.save(bid);
    }

    public BidList getBidById(Integer bidListId) {
        return bidListRepository.findById(bidListId)
                .orElseThrow(() -> new NoSuchElementException("Bid avec l'ID " + bidListId + " introuvable !"));
    }

    public void updateBid(Integer id, BidList newBid) {
        BidList existingBid = bidListRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bid avec l'ID " + id + " introuvable !"));

        existingBid.setAccount(newBid.getAccount());
        existingBid.setType(newBid.getType());
        existingBid.setBidQuantity(newBid.getBidQuantity());

        bidListRepository.save(existingBid);
    }

    public void deleteById(Integer id) {

        if (!bidListRepository.existsById(id)) {
            throw new NoSuchElementException("Bid avec l'ID " + id + " introuvable !");
        }
        bidListRepository.deleteById(id);

    }

}
