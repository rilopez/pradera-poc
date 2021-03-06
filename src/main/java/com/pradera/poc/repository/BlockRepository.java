package com.pradera.poc.repository;

import com.pradera.poc.domain.Block;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Block entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlockRepository extends JpaRepository<Block, Long>, JpaSpecificationExecutor<Block> {
    @Query("select block from Block block where block.user.login = ?#{principal.username}")
    List<Block> findByUserIsCurrentUser();

    @Query(
        "select block " +
        "from Block block " +
        "inner join FlowBlock flowblock on block.id = flowblock.block.id " +
        " where flowblock.flow.id = :flowId order by flowblock.blockOrder"
    )
    List<Block> findByFlowId(@Param("flowId") Long flowId);
}
