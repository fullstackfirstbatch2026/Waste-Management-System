package com.waste.wastemanagement.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waste.wastemanagement.entity.Household;
import com.waste.wastemanagement.repository.HouseholdRepository;

@RestController
@RequestMapping("/api/households")
@CrossOrigin(origins = "*")
public class HouseholdController {

    private final HouseholdRepository householdRepository;

    public HouseholdController(HouseholdRepository householdRepository) {
        this.householdRepository = householdRepository;
    }

    @GetMapping
    public List<Household> getAllHouseholds() {
        return householdRepository.findAll();
    }
}