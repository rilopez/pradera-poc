package com.pradera.poc.repository;

import com.pradera.poc.domain.Flow;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Flow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowRepository extends JpaRepository<Flow, Long>, JpaSpecificationExecutor<Flow> {
    @Query("select flow from Flow flow where flow.user.login = ?#{principal.username}")
    List<Flow> findByUserIsCurrentUser();
}
