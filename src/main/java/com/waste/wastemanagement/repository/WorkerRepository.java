package com.waste.wastemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waste.wastemanagement.entity.Worker;

public interface WorkerRepository extends JpaRepository<Worker, Integer> {

}