package com.ead.authuser.services.implementations;

import com.ead.authuser.repositories.UserCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCourseService {

    @Autowired
    UserCourseRepository userCourseRepository;
}
