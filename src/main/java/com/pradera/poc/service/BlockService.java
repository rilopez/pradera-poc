package com.pradera.poc.service;

import com.pradera.poc.domain.Block;
import com.pradera.poc.repository.BlockRepository;
import com.pradera.poc.repository.search.BlockSearchRepository;
import com.pradera.poc.service.dto.BlockDTO;
import com.pradera.poc.service.mapper.BlockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Block}.
 */
@Service
@Transactional
public class BlockService {

    private final Logger log = LoggerFactory.getLogger(BlockService.class);

    private final BlockRepository blockRepository;

    private final BlockMapper blockMapper;

    private final BlockSearchRepository blockSearchRepository;

    public BlockService(BlockRepository blockRepository, BlockMapper blockMapper, BlockSearchRepository blockSearchRepository) {
        this.blockRepository = blockRepository;
        this.blockMapper = blockMapper;
        this.blockSearchRepository = blockSearchRepository;
    }

    /**
     * Save a block.
     *
     * @param blockDTO the entity to save.
     * @return the persisted entity.
     */
    public BlockDTO save(BlockDTO blockDTO) {
        log.debug("Request to save Block : {}", blockDTO);
        Block block = blockMapper.toEntity(blockDTO);
        block = blockRepository.save(block);
        BlockDTO result = blockMapper.toDto(block);
        blockSearchRepository.save(block);
        return result;
    }

    /**
     * Get all the blocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BlockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Blocks");
        return blockRepository.findAll(pageable)
            .map(blockMapper::toDto);
    }


    /**
     * Get one block by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BlockDTO> findOne(Long id) {
        log.debug("Request to get Block : {}", id);
        return blockRepository.findById(id)
            .map(blockMapper::toDto);
    }

    /**
     * Delete the block by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Block : {}", id);
        blockRepository.deleteById(id);
        blockSearchRepository.deleteById(id);
    }

    /**
     * Search for the block corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BlockDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Blocks for query {}", query);
        return blockSearchRepository.search(queryStringQuery(query), pageable)
            .map(blockMapper::toDto);
    }
}
