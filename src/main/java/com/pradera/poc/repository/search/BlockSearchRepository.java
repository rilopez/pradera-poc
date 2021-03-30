package com.pradera.poc.repository.search;

import com.pradera.poc.domain.Block;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Block} entity.
 */
public interface BlockSearchRepository extends ElasticsearchRepository<Block, Long> {
}
