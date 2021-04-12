package com.pradera.poc.repository;

import com.pradera.poc.domain.FlowBlock;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlowBlock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowBlockRepository extends JpaRepository<FlowBlock, Long>, JpaSpecificationExecutor<FlowBlock> {}
