package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CurvePointService {

    private final CurvePointRepository curvePointRepository;

    public List<CurvePoint> getAllCurvePoint() {
        return curvePointRepository.findAll();
    }

    public void addCurvePoint(CurvePoint curvePoint) {
        curvePointRepository.save(curvePoint);
    }

    public CurvePoint getCurvePointById(Integer curveId) {
        return curvePointRepository.findById(curveId)
                .orElseThrow(() -> new NoSuchElementException("CurvePoint avec l'ID " + curveId + " introuvable !"));
    }

    public void updateCurvePoint(Integer id, CurvePoint newCurvePoint) {
        CurvePoint existingCurvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("CurvePoint avec l'ID " + id + " introuvable !"));

        existingCurvePoint.setTerm(newCurvePoint.getTerm());
        existingCurvePoint.setValue(newCurvePoint.getValue());

        curvePointRepository.save(existingCurvePoint);
    }

    public void deleteById(Integer id) {

        if (!curvePointRepository.existsById(id)) {
            throw new NoSuchElementException("CurvePoint avec l'ID " + id + " introuvable !");
        }
        curvePointRepository.deleteById(id);

    }


}
