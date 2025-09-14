package com.sinaukoding.eventbooking.mapper.event;

import com.sinaukoding.eventbooking.entity.event.Event;
import com.sinaukoding.eventbooking.model.app.SimpleMap;
import com.sinaukoding.eventbooking.model.request.EventRequestRecord;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event requestToEntity(EventRequestRecord request) {
        return Event.builder()
                .title(request.title())
                .description(request.description())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .capacity(request.capacity())
                .location(request.location())
                .published(request.published())
                .price(request.price())
                // createdBy tidak di-set di sini, biar service yang inject
                .build();
    }

    public SimpleMap toSimpleMap(Event event, boolean b) {
        return SimpleMap.createMap()
                .add("id", event.getId())
                .add("title", event.getTitle())
                .add("description", event.getDescription())
                .add("location", event.getLocation())
                .add("startTime", event.getStartTime())
                .add("endTime", event.getEndTime())
                .add("price", event.getPrice())
                .add("published", event.isPublished())
                .add("capacity", event.getCapacity());
    }
}
