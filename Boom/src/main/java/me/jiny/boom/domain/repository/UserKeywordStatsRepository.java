package me.jiny.boom.domain.repository;

import me.jiny.boom.domain.entity.User;
import me.jiny.boom.domain.entity.UserKeywordStats;
import me.jiny.boom.domain.entity.UserKeywordStatsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserKeywordStatsRepository extends JpaRepository<UserKeywordStats, UserKeywordStatsId> {
    @Query("SELECT uks FROM UserKeywordStats uks " +
           "WHERE uks.user = :user " +
           "ORDER BY uks.usageCount DESC")
    List<UserKeywordStats> findByUserOrderByUsageCountDesc(@Param("user") User user);
}

