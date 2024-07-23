package com.universityproject.backendproject.service.walkhistory;


import com.universityproject.backendproject.model.entity.WalkHistory;
import com.universityproject.backendproject.repository.WalkHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class WalkHistoryServiceImpl implements WalkHistoryService {

    private final WalkHistoryRepository walkHistoryRepository;


    @Override
    public Page<WalkHistory> findByUserId(Long id, int page) {
        PageRequest requestPage = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        LocalDate ld = LocalDate.now().minusMonths(3);
        return walkHistoryRepository.findAllByUserId(ld, id, requestPage);
    }
}
