package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public List<Rating> getAllRating() {
        return ratingRepository.findAll();
    }

    public void addRating(Rating rating) {
        ratingRepository.save(rating);
    }

    public Rating getRatingById(Integer id) {

        return ratingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Rating avec l'ID " + id + " introuvable !"));
    }

    public void updateRating(Integer id, Rating newRating) {
        Rating existingRating = ratingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Rating avec l'ID " + id + " introuvable !"));

        existingRating.setMoodysRating(newRating.getMoodysRating());
        existingRating.setSandPRating(newRating.getSandPRating());
        existingRating.setFitchRating(newRating.getFitchRating());
        existingRating.setOrderNumber(newRating.getOrderNumber());

        ratingRepository.save(existingRating);
    }

    public void deleteById(Integer id) {

        if (!ratingRepository.existsById(id)) {
            throw new NoSuchElementException("Rating avec l'ID " + id + " introuvable !");
        }
        ratingRepository.deleteById(id);
    }

}
