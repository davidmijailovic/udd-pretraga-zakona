package com.example.ddmdemo.service.interfaces;

import com.example.ddmdemo.indexmodel.DocumentIndex;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SearchService {

    Page<DocumentIndex> simpleSearch(List<String> keywords, Pageable pageable);

    Page<DocumentIndex> advancedSearch(List<String> expression, Pageable pageable);

    Page<DocumentIndex> phraseSearch(String keyword, Pageable pageable);

    Page<DocumentIndex> geolocationSearch(String address, Integer radius, Pageable pageable);
}
