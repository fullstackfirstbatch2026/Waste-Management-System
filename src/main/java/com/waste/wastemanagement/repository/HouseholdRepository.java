package com.waste.wastemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waste.wastemanagement.entity.Household;

public interface HouseholdRepository extends JpaRepository<Household, Integer> {

}