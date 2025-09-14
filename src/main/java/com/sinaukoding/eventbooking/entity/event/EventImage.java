package com.sinaukoding.eventbooking.entity.event;

import com.sinaukoding.eventbooking.entity.app.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "t_event_image",
        indexes = {
                @Index(name = "idx_event_image_event", columnList = "event_id"),
                @Index(name = "idx_event_image_primary", columnList = "is_primary")
        }
)
public class EventImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, length = 300)
    private String url;

    @Column(length = 150)
    private String altText;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary = false;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public EventImage(String url, String altText, boolean primary) {
        this.url = url;
        this.altText = altText;
        this.isPrimary = primary;
        this.uploadedAt = LocalDateTime.now();
    }
}
