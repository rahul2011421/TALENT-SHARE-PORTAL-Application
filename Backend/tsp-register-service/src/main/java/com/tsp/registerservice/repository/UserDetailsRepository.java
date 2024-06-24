package com.tsp.registerservice.repository;

import com.tsp.registerservice.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    Optional<UserDetails> findByEmailIdAndStatusTrue(String emailId);

    Optional<UserDetails> findByEmailId(String emailId);

    List<UserDetails> findByCreatedDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("FROM UserDetails WHERE businessUnit = :departmentUnit AND createdDateTime BETWEEN :startDate AND :endDate")
    List<UserDetails> findUsersBetweenDateRangeAndBusinessUnit(LocalDateTime startDate, LocalDateTime endDate, String departmentUnit);

    List<UserDetails> findAllByStatusTrue();

    long countByStatusTrue();
}
