package com.sinaukoding.eventbooking.service.event;

import com.sinaukoding.eventbooking.model.app.AppPage;
import com.sinaukoding.eventbooking.model.app.SimpleMap;
import com.sinaukoding.eventbooking.model.request.EventRequestRecord;
import com.sinaukoding.eventbooking.model.filter.EventFilterRequestRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {


    void add(EventRequestRecord request);
    void edit(String id, EventRequestRecord request);
    SimpleMap delete(String id);
    AppPage<SimpleMap> findAll(EventFilterRequestRecord filterRequest, Pageable pageable);
    SimpleMap findById(String id);

}
