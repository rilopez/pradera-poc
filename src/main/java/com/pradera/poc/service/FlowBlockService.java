package com.pradera.poc.service;

import com.pradera.poc.domain.FlowBlock;
import com.pradera.poc.repository.FlowBlockRepository;
import com.pradera.poc.service.dto.FlowBlockDTO;
import com.pradera.poc.service.mapper.FlowBlockMapper;
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

    private final FlowBlockMapper flowBlockMapper;

    public FlowBlockService(FlowBlockRepository flowBlockRepository, FlowBlockMapper flowBlockMapper) {
        this.flowBlockRepository = flowBlockRepository;
        this.flowBlockMapper = flowBlockMapper;
    }

    /**
     * Save a flowBlock.
     *
     * @param flowBlockDTO the entity to save.
     * @return the persisted entity.
     */
    public FlowBlockDTO save(FlowBlockDTO flowBlockDTO) {
        log.debug("Request to save FlowBlock : {}", flowBlockDTO);
        FlowBlock flowBlock = flowBlockMapper.toEntity(flowBlockDTO);
        flowBlock = flowBlockRepository.save(flowBlock);
        return flowBlockMapper.toDto(flowBlock);
    }

    /**
     * Partially update a flowBlock.
     *
     * @param flowBlockDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FlowBlockDTO> partialUpdate(FlowBlockDTO flowBlockDTO) {
        log.debug("Request to partially update FlowBlock : {}", flowBlockDTO);

        return flowBlockRepository
            .findById(flowBlockDTO.getId())
            .map(
                existingFlowBlock -> {
                    flowBlockMapper.partialUpdate(existingFlowBlock, flowBlockDTO);
                    return existingFlowBlock;
                }
            )
            .map(flowBlockRepository::save)
            .map(flowBlockMapper::toDto);
    }

    /**
     * Get all the flowBlocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FlowBlockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FlowBlocks");
        return flowBlockRepository.findAll(pageable).map(flowBlockMapper::toDto);
    }

    /**
     * Get one flowBlock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FlowBlockDTO> findOne(Long id) {
        log.debug("Request to get FlowBlock : {}", id);
        return flowBlockRepository.findById(id).map(flowBlockMapper::toDto);
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
