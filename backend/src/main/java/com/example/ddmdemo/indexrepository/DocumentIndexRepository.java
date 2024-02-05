package com.example.ddmdemo.indexrepository;

import com.example.ddmdemo.indexmodel.DocumentIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentIndexRepository
    extends ElasticsearchRepository<DocumentIndex, String> {
}
