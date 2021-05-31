package com.pradera.poc.service;

import com.pradera.poc.domain.*; // for static metamodels
import com.pradera.poc.domain.Block;
import com.pradera.poc.repository.BlockRepository;
import com.pradera.poc.service.criteria.BlockCriteria;
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
 * Service for executing complex queries for {@link Block} entities in the database.
 * The main input is a {@link BlockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Block} or a {@link Page} of {@link Block} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BlockQueryService extends QueryService<Block> {

    private final Logger log = LoggerFactory.getLogger(BlockQueryService.class);

    private final BlockRepository blockRepository;

    public BlockQueryService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    /**
     * Return a {@link List} of {@link Block} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Block> findByCriteria(BlockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Block> specification = createSpecification(criteria);
        return blockRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Block} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Block> findByCriteria(BlockCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Block> specification = createSpecification(criteria);
        return blockRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public List<Block> findByFlowId(Long flowId) {
        log.debug("find by flowId : {}", flowId);
        return blockRepository.findByFlowId(flowId);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BlockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Block> specification = createSpecification(criteria);
        return blockRepository.count(specification);
    }

    /**
     * Function to convert {@link BlockCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Block> createSpecification(BlockCriteria criteria) {
        Specification<Block> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Block_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Block_.type));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Block_.createdDate));
            }
            if (criteria.getHash() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHash(), Block_.hash));
            }
            if (criteria.getParentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParentId(), root -> root.join(Block_.parent, JoinType.LEFT).get(Block_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Block_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getParentBlocksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentBlocksId(),
                            root -> root.join(Block_.parentBlocks, JoinType.LEFT).get(Block_.id)
                        )
                    );
            }
            if (criteria.getFlowsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFlowsId(), root -> root.join(Block_.flows, JoinType.LEFT).get(FlowBlock_.id))
                    );
            }
        }
        return specification;
    }
}
