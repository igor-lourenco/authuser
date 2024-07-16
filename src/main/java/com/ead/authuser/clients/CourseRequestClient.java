package com.ead.authuser.clients;

import com.ead.authuser.DTOs.CourseDTO;
import com.ead.authuser.DTOs.ResponsePageDTO;
import com.ead.authuser.utils.LogUtils;
import com.ead.authuser.utils.RequestClientUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class CourseRequestClient {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LogUtils logUtils;

    @Value("${ead.api.url.course}")
    private String REQUEST_URL_COURSE;

    public Page<CourseDTO> getAllCoursesByUserPaged(UUID userId, Pageable pageable) {

        List<CourseDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;
        String url = REQUEST_URL_COURSE + RequestClientUtil.createUrlGETAllCoursesByUser(userId, pageable);

        try {
            var responseType = new ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>() {};

            log.info("REQUEST GET [getAllCoursesByUserPaged] - URL: {}", url);

            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            Page<CourseDTO> courseDTOPage = result.getBody();

            log.info("RESPONSE GET [getAllCoursesByUserPaged] - BODY: {}", logUtils.convertObjectToJson(courseDTOPage));

            searchResult = result.getBody().getContent();
            log.info("RESPONSE [getAllCoursesByUserPaged] - Number of Elements: {}", searchResult.size());

        } catch (HttpStatusCodeException e) {

            log.error("ERROR REQUEST [getAllCoursesByUserPaged] - url: {}", url);
            log.error("ERROR [getAllCoursesByUserPaged]: {}", e);
        }

        return result.getBody();
    }
}
