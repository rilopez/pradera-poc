package com.pradera.poc.repository;

import com.pradera.poc.domain.Flow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Flow entity.
 */
@Repository
public interface FlowRepository extends JpaRepository<Flow, Long>, JpaSpecificationExecutor<Flow> {
    @Query("select flow from Flow flow where flow.user.login = ?#{principal.username}")
    List<Flow> findByUserIsCurrentUser();

    @Query(
        value = "select distinct flow from Flow flow left join fetch flow.blocks",
        countQuery = "select count(distinct flow) from Flow flow"
    )
    Page<Flow> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct flow from Flow flow left join fetch flow.blocks")
    List<Flow> findAllWithEagerRelationships();

    @Query("select flow from Flow flow left join fetch flow.blocks where flow.id =:id")
    Optional<Flow> findOneWithEagerRelationships(@Param("id") Long id);
}
