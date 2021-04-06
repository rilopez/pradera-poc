package com.pradera.poc.service;

import com.pradera.poc.domain.Block;
import com.pradera.poc.repository.BlockRepository;
import com.pradera.poc.service.dto.BlockDTO;
import com.pradera.poc.service.mapper.BlockMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Block}.
 */
@Service
@Transactional
public class BlockService {

    private final Logger log = LoggerFactory.getLogger(BlockService.class);

    private final BlockRepository blockRepository;

    private final BlockMapper blockMapper;

    public BlockService(BlockRepository blockRepository, BlockMapper blockMapper) {
        this.blockRepository = blockRepository;
        this.blockMapper = blockMapper;
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
        return blockMapper.toDto(block);
    }

    /**
     * Partially update a block.
     *
     * @param blockDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BlockDTO> partialUpdate(BlockDTO blockDTO) {
        log.debug("Request to partially update Block : {}", blockDTO);

        return blockRepository
            .findById(blockDTO.getId())
            .map(
                existingBlock -> {
                    blockMapper.partialUpdate(existingBlock, blockDTO);
                    return existingBlock;
                }
            )
            .map(blockRepository::save)
            .map(blockMapper::toDto);
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
        return blockRepository.findAll(pageable).map(blockMapper::toDto);
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
        return blockRepository.findById(id).map(blockMapper::toDto);
    }

    /**
     * Delete the block by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Block : {}", id);
        blockRepository.deleteById(id);
    }
}
