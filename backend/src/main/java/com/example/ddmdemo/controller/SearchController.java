package com.example.ddmdemo.controller;

import com.example.ddmdemo.dto.GeolocationQueryDTO;
import com.example.ddmdemo.dto.SearchQueryDTO;
import com.example.ddmdemo.indexmodel.DocumentIndex;
import com.example.ddmdemo.service.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/simple")
    public Page<DocumentIndex> simpleSearch(@RequestBody SearchQueryDTO simpleSearchQuery, Pageable pageable) {
        return searchService.simpleSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/advanced")
    public Page<DocumentIndex> advancedSearch(@RequestBody SearchQueryDTO advancedSearchQuery, Pageable pageable) {
        return searchService.advancedSearch(advancedSearchQuery.keywords(), pageable);
    }

    @PostMapping("/phrase")
    public Page<DocumentIndex> phraseSearch(@RequestBody String phraseSearchQuery, Pageable pageable) {
        return searchService.phraseSearch(phraseSearchQuery, pageable);
    }

    @PostMapping("/geolocation")
    public Page<DocumentIndex> geolocationSearch(@RequestBody GeolocationQueryDTO geolocationQueryDTO, Pageable pageable) {
        return searchService.geolocationSearch(geolocationQueryDTO.address(), geolocationQueryDTO.radius(), pageable);
    }
}
