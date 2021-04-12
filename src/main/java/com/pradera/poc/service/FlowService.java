package com.pradera.poc.service;

import com.pradera.poc.domain.Flow;
import com.pradera.poc.repository.FlowRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Flow}.
 */
@Service
@Transactional
public class FlowService {

    private final Logger log = LoggerFactory.getLogger(FlowService.class);

    private final FlowRepository flowRepository;

    public FlowService(FlowRepository flowRepository) {
        this.flowRepository = flowRepository;
    }

    /**
     * Save a flow.
     *
     * @param flow the entity to save.
     * @return the persisted entity.
     */
    public Flow save(Flow flow) {
        log.debug("Request to save Flow : {}", flow);
        return flowRepository.save(flow);
    }

    /**
     * Partially update a flow.
     *
     * @param flow the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Flow> partialUpdate(Flow flow) {
        log.debug("Request to partially update Flow : {}", flow);

        return flowRepository
            .findById(flow.getId())
            .map(
                existingFlow -> {
                    if (flow.getName() != null) {
                        existingFlow.setName(flow.getName());
                    }

                    return existingFlow;
                }
            )
            .map(flowRepository::save);
    }

    /**
     * Get all the flows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Flow> findAll(Pageable pageable) {
        log.debug("Request to get all Flows");
        return flowRepository.findAll(pageable);
    }

    /**
     * Get one flow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Flow> findOne(Long id) {
        log.debug("Request to get Flow : {}", id);
        return flowRepository.findById(id);
    }

    /**
     * Delete the flow by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Flow : {}", id);
        flowRepository.deleteById(id);
    }
}
