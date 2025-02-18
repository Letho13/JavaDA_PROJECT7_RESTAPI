package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BidListService {

    private final BidListRepository bidListRepository;

    public List<BidList> getAllBidList(){
        return bidListRepository.findAll();
    }

    public void addBid(BidList bid){
        bidListRepository.save(bid);
    }

    public BidList getBidById(Integer bidListId){
        return bidListRepository.getReferenceById(bidListId);
    }

}
