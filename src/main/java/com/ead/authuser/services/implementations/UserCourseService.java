package com.ead.authuser.services.implementations;

import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCourseService  implements UserCourseServiceInterface {

    @Autowired
    UserCourseRepository userCourseRepository;
}
