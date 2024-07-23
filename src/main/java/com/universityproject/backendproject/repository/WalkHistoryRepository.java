package com.universityproject.backendproject.repository;


import com.universityproject.backendproject.model.entity.WalkHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface WalkHistoryRepository extends JpaRepository<WalkHistory,Long> {

    @Query("select w from WalkHistory w where w.localDate >= :#{#localDate} AND w.user.id = :#{#userId}")
    Page<WalkHistory> findAllByUserId(LocalDate localDate, @Param("userId") Long userId, PageRequest pageable);
}
