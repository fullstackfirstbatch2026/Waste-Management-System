package com.waste.wastemanagement.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.waste.wastemanagement.entity.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {

    // JOIN query - get complete collection details
    @Query(value = """
        SELECT
            c.collection_id AS collectionId,
            h.household_name AS householdName,
            h.address AS address,
            z.zone_name AS zoneName,
            w.worker_name AS workerName,
            c.collection_date AS collectionDate,
            c.waste_volume AS wasteVolume,
            c.status AS status
        FROM collections c
        JOIN households h
            ON c.household_id = h.household_id
        JOIN zones z
            ON c.zone_id = z.zone_id
        JOIN workers w
            ON c.worker_id = w.worker_id
        """, nativeQuery = true)
    List<Object[]> getCollectionDetails();


    // Subquery - find zones with above-average waste
    @Query(value = """
        SELECT
            z.zone_name AS zoneName,
            SUM(c.waste_volume) AS totalWaste
        FROM zones z
        JOIN collections c
            ON z.zone_id = c.zone_id
        GROUP BY z.zone_id, z.zone_name
        HAVING SUM(c.waste_volume) > (
            SELECT AVG(zone_total)
            FROM (
                SELECT SUM(waste_volume) AS zone_total
                FROM collections
                GROUP BY zone_id
            ) AS zone_totals
        )
        """, nativeQuery = true)
    List<Object[]> findZonesAboveAverage();


    // Stored procedure - get total waste for a zone
    @Query(
        value = "CALL get_total_waste_by_zone(:zoneId)",
        nativeQuery = true
    )
    BigDecimal getTotalWasteByZone(@Param("zoneId") Integer zoneId);
 // MySQL function - calculate total waste for a zone
    @Query(
        value = "SELECT total_waste_by_zone(:zoneId)",
        nativeQuery = true
    )
    BigDecimal getTotalWasteUsingFunction(
            @Param("zoneId") Integer zoneId);
}