package com.sinaukoding.eventbooking;

import com.sinaukoding.eventbooking.service.managementuser.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void addUserTest() {
        RoleUpdateRequestRecord request = new RoleUpdateRequestRecord(null,
                "Fariz",
                "fariz",
                "fariz@yopmail.com",
                "fariz123",
                BookingStatus.AKTIF,
                PaymentStatus.PEMBELI
        );
        userService.add(request);
    }

}
