package com.example.ddmdemo.respository;

import com.example.ddmdemo.model.DummyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticSearchRepository extends JpaRepository<DummyTable, Integer> {
}
