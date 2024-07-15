package com.ead.authuser.clients;

import com.ead.authuser.DTOs.CourseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class UserRequestClient {

    @Autowired
    RestTemplate restTemplate;

    String REQUEST_URI = "http://localhost:8082";

    public Page<CourseDTO> getAllCoursesByUserPaged(UUID userId, Pageable pageable) {

        List<CourseDTO> searchResult = null;

        String url = REQUEST_URI + "/courses?userId=" + userId + paginationParameters(pageable);

        log.info("REQUEST - URL: {}", url);
        try {


            log.info("RESPONSE - Number of Elements: {}", searchResult.size());
        } catch (HttpStatusCodeException e) {

            log.error("ERROR REQUEST - /courses : {}", e);
        }


//        log.info("RESPONSE /courses BODY: {}", userId);
        return null;
    }


    private String paginationParameters(Pageable pageable){

        StringBuilder builder = new StringBuilder();
        builder.append("&page=" + pageable.getPageNumber());
        builder.append("&size=" + pageable.getPageSize());
        builder.append("&sort=" + pageable.getSort().toString().replaceAll(": ", ","));

        return builder.toString();
    }

}
