package com.waste.wastemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waste.wastemanagement.entity.Zone;

public interface ZoneRepository extends JpaRepository<Zone, Integer> {

}