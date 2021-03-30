package com.pradera.poc.repository;

import com.pradera.poc.domain.Block;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Block entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlockRepository extends JpaRepository<Block, Long>, JpaSpecificationExecutor<Block> {

    @Query("select block from Block block where block.user.login = ?#{principal.username}")
    List<Block> findByUserIsCurrentUser();
}
