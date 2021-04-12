package com.pradera.poc.service;

import com.pradera.poc.domain.FlowBlock;
import com.pradera.poc.repository.FlowBlockRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FlowBlock}.
 */
@Service
@Transactional
public class FlowBlockService {

    private final Logger log = LoggerFactory.getLogger(FlowBlockService.class);

    private final FlowBlockRepository flowBlockRepository;

    public FlowBlockService(FlowBlockRepository flowBlockRepository) {
        this.flowBlockRepository = flowBlockRepository;
    }

    /**
     * Save a flowBlock.
     *
     * @param flowBlock the entity to save.
     * @return the persisted entity.
     */
    public FlowBlock save(FlowBlock flowBlock) {
        log.debug("Request to save FlowBlock : {}", flowBlock);
        return flowBlockRepository.save(flowBlock);
    }

    /**
     * Partially update a flowBlock.
     *
     * @param flowBlock the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FlowBlock> partialUpdate(FlowBlock flowBlock) {
        log.debug("Request to partially update FlowBlock : {}", flowBlock);

        return flowBlockRepository
            .findById(flowBlock.getId())
            .map(
                existingFlowBlock -> {
                    if (flowBlock.getBlockOrder() != null) {
                        existingFlowBlock.setBlockOrder(flowBlock.getBlockOrder());
                    }

                    return existingFlowBlock;
                }
            )
            .map(flowBlockRepository::save);
    }

    /**
     * Get all the flowBlocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FlowBlock> findAll(Pageable pageable) {
        log.debug("Request to get all FlowBlocks");
        return flowBlockRepository.findAll(pageable);
    }

    /**
     * Get one flowBlock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FlowBlock> findOne(Long id) {
        log.debug("Request to get FlowBlock : {}", id);
        return flowBlockRepository.findById(id);
    }

    /**
     * Delete the flowBlock by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FlowBlock : {}", id);
        flowBlockRepository.deleteById(id);
    }
}
