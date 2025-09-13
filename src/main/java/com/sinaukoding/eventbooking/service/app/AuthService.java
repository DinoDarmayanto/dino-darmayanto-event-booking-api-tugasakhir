package com.sinaukoding.eventbooking.service.app;

import com.sinaukoding.eventbooking.entity.managementuser.User;
import com.sinaukoding.eventbooking.model.app.SimpleMap;
import com.sinaukoding.eventbooking.model.request.LoginRequestRecord;
import com.sinaukoding.eventbooking.model.request.RegisterRequestRecord;

public interface AuthService {

    // Login mengembalikan SimpleMap
    SimpleMap login(LoginRequestRecord request);
    void logout(User userLoggedIn);
    // Register juga mengembalikan SimpleMap supaya fleksibel
    SimpleMap register(RegisterRequestRecord request);
}
