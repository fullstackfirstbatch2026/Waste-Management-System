package com.waste.wastemanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.waste.wastemanagement.entity.Collection;
import com.waste.wastemanagement.repository.CollectionRepository;

import jakarta.persistence.EntityManager;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final EntityManager entityManager;

    public CollectionService(
            CollectionRepository collectionRepository,
            EntityManager entityManager) {

        this.collectionRepository = collectionRepository;
        this.entityManager = entityManager;
    }

    // Get all collections
    public List<Collection> getAllCollections() {
        return collectionRepository.findAll();
    }

    // Get collection by ID
    public Optional<Collection> getCollectionById(Integer id) {
        return collectionRepository.findById(id);
    }

    // Normal JPA schedule
    public Collection scheduleCollection(Collection collection) {

        collection.setStatus("Scheduled");

        return collectionRepository.save(collection);
    }

    // Schedule collection using MySQL stored procedure
    @Transactional
    public void scheduleCollectionUsingProcedure(
            Integer householdId,
            Integer workerId,
            Integer zoneId,
            java.time.LocalDate collectionDate,
            java.math.BigDecimal wasteVolume) {

        entityManager
                .createNativeQuery(
                    "CALL schedule_collection(:householdId, :workerId, :zoneId, :collectionDate, :wasteVolume)"
                )
                .setParameter("householdId", householdId)
                .setParameter("workerId", workerId)
                .setParameter("zoneId", zoneId)
                .setParameter("collectionDate", collectionDate)
                .setParameter("wasteVolume", wasteVolume)
                .executeUpdate();
    }

    // Update collection
    public Collection updateCollection(
            Integer id,
            Collection updatedCollection) {

        Optional<Collection> existing =
                collectionRepository.findById(id);

        if (existing.isPresent()) {

            Collection collection = existing.get();

            collection.setHouseholdId(
                    updatedCollection.getHouseholdId());

            collection.setWorkerId(
                    updatedCollection.getWorkerId());

            collection.setZoneId(
                    updatedCollection.getZoneId());

            collection.setCollectionDate(
                    updatedCollection.getCollectionDate());

            collection.setWasteVolume(
                    updatedCollection.getWasteVolume());

            collection.setStatus(
                    updatedCollection.getStatus());

            return collectionRepository.save(collection);
        }

        return null;
    }

    // Delete collection
    public boolean deleteCollection(Integer id) {

        if (collectionRepository.existsById(id)) {
            collectionRepository.deleteById(id);
            return true;
        }

        return false;
    }

    // Get collection details
    public List<Object[]> getCollectionDetails() {
        return collectionRepository.getCollectionDetails();
    }

    // Get zones with above-average waste
    public List<Object[]> getZonesAboveAverage() {
        return collectionRepository.findZonesAboveAverage();
    }

    // Get total waste by zone using stored procedure
    public java.math.BigDecimal getTotalWasteByZone(Integer zoneId) {
        return collectionRepository.getTotalWasteByZone(zoneId);
    }
    
    // Get total waste by zone using MySQL function
    public java.math.BigDecimal getTotalWasteUsingFunction(Integer zoneId) {
        return collectionRepository.getTotalWasteUsingFunction(zoneId);
    }
}