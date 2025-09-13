package com.sinaukoding.eventbooking.service.managementuser;

import com.sinaukoding.eventbooking.model.app.SimpleMap;
import com.sinaukoding.eventbooking.model.filter.UserFilterRequestRecord;
import com.sinaukoding.eventbooking.model.request.UserRequestRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void add(UserRequestRecord request);

    void edit(UserRequestRecord request);

    SimpleMap delete(String id);

    Page<SimpleMap> findAll(UserFilterRequestRecord filterRequest, Pageable pageable);

    SimpleMap findById(String id);

}
