package com.pradera.poc.service;

import com.pradera.poc.domain.*; // for static metamodels
import com.pradera.poc.domain.FlowBlock;
import com.pradera.poc.repository.FlowBlockRepository;
import com.pradera.poc.service.criteria.FlowBlockCriteria;
import com.pradera.poc.service.dto.FlowBlockDTO;
import com.pradera.poc.service.mapper.FlowBlockMapper;
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
 * Service for executing complex queries for {@link FlowBlock} entities in the database.
 * The main input is a {@link FlowBlockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FlowBlockDTO} or a {@link Page} of {@link FlowBlockDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FlowBlockQueryService extends QueryService<FlowBlock> {

    private final Logger log = LoggerFactory.getLogger(FlowBlockQueryService.class);

    private final FlowBlockRepository flowBlockRepository;

    private final FlowBlockMapper flowBlockMapper;

    public FlowBlockQueryService(FlowBlockRepository flowBlockRepository, FlowBlockMapper flowBlockMapper) {
        this.flowBlockRepository = flowBlockRepository;
        this.flowBlockMapper = flowBlockMapper;
    }

    /**
     * Return a {@link List} of {@link FlowBlockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FlowBlockDTO> findByCriteria(FlowBlockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FlowBlock> specification = createSpecification(criteria);
        return flowBlockMapper.toDto(flowBlockRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FlowBlockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FlowBlockDTO> findByCriteria(FlowBlockCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FlowBlock> specification = createSpecification(criteria);
        return flowBlockRepository.findAll(specification, page).map(flowBlockMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FlowBlockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FlowBlock> specification = createSpecification(criteria);
        return flowBlockRepository.count(specification);
    }

    /**
     * Function to convert {@link FlowBlockCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FlowBlock> createSpecification(FlowBlockCriteria criteria) {
        Specification<FlowBlock> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FlowBlock_.id));
            }
            if (criteria.getBlockOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBlockOrder(), FlowBlock_.blockOrder));
            }
            if (criteria.getFlowId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFlowId(), root -> root.join(FlowBlock_.flow, JoinType.LEFT).get(Flow_.id))
                    );
            }
            if (criteria.getBlockId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBlockId(), root -> root.join(FlowBlock_.block, JoinType.LEFT).get(Block_.id))
                    );
            }
        }
        return specification;
    }
}
