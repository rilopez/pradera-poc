package com.pradera.poc.service;

import com.pradera.poc.domain.Block;
import com.pradera.poc.repository.BlockRepository;
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

    public BlockService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    /**
     * Save a block.
     *
     * @param block the entity to save.
     * @return the persisted entity.
     */
    public Block save(Block block) {
        log.debug("Request to save Block : {}", block);
        return blockRepository.save(block);
    }

    /**
     * Partially update a block.
     *
     * @param block the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Block> partialUpdate(Block block) {
        log.debug("Request to partially update Block : {}", block);

        return blockRepository
            .findById(block.getId())
            .map(
                existingBlock -> {
                    if (block.getType() != null) {
                        existingBlock.setType(block.getType());
                    }
                    if (block.getContent() != null) {
                        existingBlock.setContent(block.getContent());
                    }
                    if (block.getCreatedDate() != null) {
                        existingBlock.setCreatedDate(block.getCreatedDate());
                    }
                    if (block.getHash() != null) {
                        existingBlock.setHash(block.getHash());
                    }

                    return existingBlock;
                }
            )
            .map(blockRepository::save);
    }

    /**
     * Get all the blocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Block> findAll(Pageable pageable) {
        log.debug("Request to get all Blocks");
        return blockRepository.findAll(pageable);
    }

    /**
     * Get one block by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Block> findOne(Long id) {
        log.debug("Request to get Block : {}", id);
        return blockRepository.findById(id);
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
