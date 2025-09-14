package com.sinaukoding.eventbooking.repository.event;

import com.sinaukoding.eventbooking.entity.event.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, String>, JpaSpecificationExecutor<EventImage> {

    @Modifying
    @Transactional
    @Query("UPDATE EventImage ei SET ei.isPrimary = :isPrimary WHERE ei.event.id = :eventId")
    void updatePrimaryStatusForEvent(@Param("eventId") String eventId, @Param("isPrimary") boolean isPrimary);
    void deleteByEventId(String eventId);
}
