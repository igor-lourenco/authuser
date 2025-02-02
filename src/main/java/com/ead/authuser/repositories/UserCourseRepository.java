package com.ead.authuser.repositories;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID>, JpaSpecificationExecutor<UserCourseModel> {
    boolean existsByUserAndCourseId(UserModel userModel, UUID courseId);

    @Query(value = "SELECT * FROM TB_USERS_COURSES where user_user_id = :userId", nativeQuery = true)
    List<UserCourseModel> findAllUserCourseIntoUser(@Param("userId") UUID userId);
}
