package com.pradera.poc.service;

import com.pradera.poc.domain.*; // for static metamodels
import com.pradera.poc.domain.Flow;
import com.pradera.poc.repository.FlowRepository;
import com.pradera.poc.service.criteria.FlowCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Flow} entities in the database.
 * The main input is a {@link FlowCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Flow} or a {@link Page} of {@link Flow} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FlowQueryService extends QueryService<Flow> {

    private final Logger log = LoggerFactory.getLogger(FlowQueryService.class);

    private final FlowRepository flowRepository;

    public FlowQueryService(FlowRepository flowRepository) {
        this.flowRepository = flowRepository;
    }

    /**
     * Return a {@link List} of {@link Flow} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Flow> findByCriteria(FlowCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Flow> specification = createSpecification(criteria);
        return flowRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Flow} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Flow> findByCriteria(FlowCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Flow> specification = createSpecification(criteria);
        return flowRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FlowCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Flow> specification = createSpecification(criteria);
        return flowRepository.count(specification);
    }

    /**
     * Function to convert {@link FlowCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Flow> createSpecification(FlowCriteria criteria) {
        Specification<Flow> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Flow_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Flow_.name));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getUserId(), root -> root.join(Flow_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getBookId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getBookId(), root -> root.join(Flow_.book, JoinType.LEFT).get(Book_.id)));
            }
            if (criteria.getBlocksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBlocksId(), root -> root.join(Flow_.blocks, JoinType.LEFT).get(FlowBlock_.id))
                    );
            }
        }
        return specification;
    }

    public List<Flow> findByUserId(Long userId) {
        log.debug("find by userId : {}", userId);
        return flowRepository.findByUserId(userId);
    }
}
