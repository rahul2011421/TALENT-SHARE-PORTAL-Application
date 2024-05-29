package com.maveric.tsp.mentorshipService.repositories;

import com.maveric.tsp.mentorshipService.entities.SessionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MentorshipServiceRepo extends JpaRepository<SessionDetails, Long> {
    List<SessionDetails> findByMenteeEmailId(String emailId);

    @Query("SELECT m FROM SessionDetails m WHERE m.menteeEmailId = :emailId AND m.fromDate BETWEEN :fromDate AND :toDate")
    List<SessionDetails> findByMenteeEmailIdAndFromDateBetween(String emailId, LocalDateTime fromDate, LocalDateTime toDate);

    @Query("SELECT m FROM SessionDetails m WHERE m.mentorEmailId = :emailId AND m.fromDate BETWEEN :fromDate AND :toDate")
    List<SessionDetails> findByMentorEmailIdAndFromDateBetween(String emailId, LocalDateTime fromDate, LocalDateTime toDate);
    List<SessionDetails> findByMentorEmailId(String mentorEmailId);
    List<SessionDetails> findByManagerEmailId(String managerEmailId);

    @Query("SELECT m FROM SessionDetails m WHERE m.managerEmailId = :emailId AND m.fromDate BETWEEN :fromDate AND :toDate")
    List<SessionDetails> findByManagerEmailIdAndFromDateBetween(String emailId, LocalDateTime fromDate, LocalDateTime toDate);

}
