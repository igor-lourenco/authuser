package com.ead.authuser.utils;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class RequestClientUtil {


    public static String createUrlGETAllCoursesByUser(UUID userId, Pageable pageable){

        String url = "/courses?userId=" + userId + paginationParameters(pageable);

        return url;
    }

    private static String paginationParameters(Pageable pageable){

        StringBuilder builder = new StringBuilder();
        builder.append("&page=" + pageable.getPageNumber());
        builder.append("&size=" + pageable.getPageSize());
        builder.append("&sort=" + pageable.getSort().toString().replaceAll(": ", ","));

        return builder.toString();
    }
}
