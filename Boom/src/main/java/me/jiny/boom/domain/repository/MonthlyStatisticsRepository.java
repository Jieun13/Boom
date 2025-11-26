package me.jiny.boom.domain.repository;

import me.jiny.boom.domain.entity.MonthlyStatistics;
import me.jiny.boom.domain.entity.MonthlyStatisticsId;
import me.jiny.boom.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthlyStatisticsRepository extends JpaRepository<MonthlyStatistics, MonthlyStatisticsId> {
    List<MonthlyStatistics> findByUserOrderByIdPeriodDesc(User user);
}

