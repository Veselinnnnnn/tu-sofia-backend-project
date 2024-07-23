package com.universityproject.backendproject.service.walkhistory;


import com.universityproject.backendproject.model.entity.WalkHistory;
import org.springframework.data.domain.Page;

import java.text.ParseException;

public interface WalkHistoryService {

    Page<WalkHistory> findByUserId(Long id, int page) throws ParseException;
}
