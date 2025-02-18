package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class RuleNameService {

    RuleNameRepository ruleNameRepository;

    public List<RuleName> getAllRuleName() {
        return ruleNameRepository.findAll();
    }

    public void addRuleName(RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    public RuleName getByIdRuleName(Integer id) {
       return ruleNameRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bid avec l'ID " + id + " introuvable !"));

    }

    public void updateRuleName(Integer id, RuleName newRuleName) {

        RuleName existingRuleName = ruleNameRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException("RuleName avec l'ID " + id + " introuvable !"));

        existingRuleName.setName(newRuleName.getName());
        existingRuleName.setDescription(newRuleName.getDescription());
        existingRuleName.setJson(newRuleName.getJson());
        existingRuleName.setTemplate(newRuleName.getTemplate());
        existingRuleName.setSqlStr(newRuleName.getSqlStr());
        existingRuleName.setSqlPart(newRuleName.getSqlPart());

        ruleNameRepository.save(existingRuleName);
    }

    public void deleteById(Integer id) {
        if (!ruleNameRepository.existsById(id)) {
            throw new NoSuchElementException("RuleName avec l'ID " + id + " introuvable !");
        }
        ruleNameRepository.deleteById(id);
    }

}
