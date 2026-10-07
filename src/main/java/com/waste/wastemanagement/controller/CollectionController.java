package com.waste.wastemanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.waste.wastemanagement.entity.Collection;
import com.waste.wastemanagement.service.CollectionService;

@RestController
@RequestMapping("/api/collections")
@CrossOrigin(origins = "*")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    // GET all collections
    @GetMapping
    public List<Collection> getAllCollections() {
        return collectionService.getAllCollections();
    }

    // GET collection details
    @GetMapping("/details")
    public List<Object[]> getCollectionDetails() {
        return collectionService.getCollectionDetails();
    }

    // GET zones with above-average waste
    @GetMapping("/above-average")
    public List<Object[]> getZonesAboveAverage() {
        return collectionService.getZonesAboveAverage();
    }

    // GET total waste by zone using stored procedure
    @GetMapping("/total-waste/{zoneId}")
    public java.math.BigDecimal getTotalWasteByZone(@PathVariable Integer zoneId) {
        return collectionService.getTotalWasteByZone(zoneId);
    }
    
    // GET total waste by zone using MySQL function
    @GetMapping("/function/total-waste/{zoneId}")
    public java.math.BigDecimal getTotalWasteUsingFunction(
            @PathVariable Integer zoneId) {

        return collectionService.getTotalWasteUsingFunction(zoneId);
    }

    // GET collection by ID
    @GetMapping("/{id}")
    public ResponseEntity<Collection> getCollectionById(
            @PathVariable Integer id) {

        Optional<Collection> collection =
                collectionService.getCollectionById(id);

        if (collection.isPresent()) {
            return ResponseEntity.ok(collection.get());
        }

        return ResponseEntity.notFound().build();
    }

    // Schedule collection
 // Schedule collection using MySQL stored procedure
    @PostMapping("/procedure")
    public ResponseEntity<String> scheduleCollectionUsingProcedure(
            @RequestBody Collection collection) {

        collectionService.scheduleCollectionUsingProcedure(
                collection.getHouseholdId(),
                collection.getWorkerId(),
                collection.getZoneId(),
                collection.getCollectionDate(),
                collection.getWasteVolume()
        );

        return ResponseEntity.ok(
                "Collection scheduled successfully using stored procedure!"
        );
    }

    // Update collection
    @PutMapping("/{id}")
    public ResponseEntity<Collection> updateCollection(
            @PathVariable Integer id,
            @RequestBody Collection collection) {

        Collection updated =
                collectionService.updateCollection(id, collection);

        if (updated != null) {
            return ResponseEntity.ok(updated);
        }

        return ResponseEntity.notFound().build();
    }

    // Delete collection
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCollection(
            @PathVariable Integer id) {

        boolean deleted =
                collectionService.deleteCollection(id);

        if (deleted) {
            return ResponseEntity.ok(
                    "Collection deleted successfully!"
            );
        }

        return ResponseEntity.notFound().build();
    }
}