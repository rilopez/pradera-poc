package com.pradera.poc.web.rest;

import com.pradera.poc.repository.BlockRepository;
import com.pradera.poc.service.BlockQueryService;
import com.pradera.poc.service.BlockService;
import com.pradera.poc.service.criteria.BlockCriteria;
import com.pradera.poc.service.dto.BlockDTO;
import com.pradera.poc.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pradera.poc.domain.Block}.
 */
@RestController
@RequestMapping("/api")
public class BlockResource {

    private final Logger log = LoggerFactory.getLogger(BlockResource.class);

    private static final String ENTITY_NAME = "block";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlockService blockService;

    private final BlockRepository blockRepository;

    private final BlockQueryService blockQueryService;

    public BlockResource(BlockService blockService, BlockRepository blockRepository, BlockQueryService blockQueryService) {
        this.blockService = blockService;
        this.blockRepository = blockRepository;
        this.blockQueryService = blockQueryService;
    }

    /**
     * {@code POST  /blocks} : Create a new block.
     *
     * @param blockDTO the blockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blockDTO, or with status {@code 400 (Bad Request)} if the block has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/blocks")
    public ResponseEntity<BlockDTO> createBlock(@Valid @RequestBody BlockDTO blockDTO) throws URISyntaxException {
        log.debug("REST request to save Block : {}", blockDTO);
        if (blockDTO.getId() != null) {
            throw new BadRequestAlertException("A new block cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlockDTO result = blockService.save(blockDTO);
        return ResponseEntity
            .created(new URI("/api/blocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /blocks/:id} : Updates an existing block.
     *
     * @param id the id of the blockDTO to save.
     * @param blockDTO the blockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blockDTO,
     * or with status {@code 400 (Bad Request)} if the blockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/blocks/{id}")
    public ResponseEntity<BlockDTO> updateBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BlockDTO blockDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Block : {}, {}", id, blockDTO);
        if (blockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BlockDTO result = blockService.save(blockDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blockDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /blocks/:id} : Partial updates given fields of an existing block, field will ignore if it is null
     *
     * @param id the id of the blockDTO to save.
     * @param blockDTO the blockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blockDTO,
     * or with status {@code 400 (Bad Request)} if the blockDTO is not valid,
     * or with status {@code 404 (Not Found)} if the blockDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the blockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/blocks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BlockDTO> partialUpdateBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BlockDTO blockDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Block partially : {}, {}", id, blockDTO);
        if (blockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BlockDTO> result = blockService.partialUpdate(blockDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blockDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /blocks} : get all the blocks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blocks in body.
     */
    @GetMapping("/blocks")
    public ResponseEntity<List<BlockDTO>> getAllBlocks(BlockCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Blocks by criteria: {}", criteria);
        Page<BlockDTO> page = blockQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /blocks/count} : count all the blocks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/blocks/count")
    public ResponseEntity<Long> countBlocks(BlockCriteria criteria) {
        log.debug("REST request to count Blocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(blockQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /blocks/:id} : get the "id" block.
     *
     * @param id the id of the blockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/blocks/{id}")
    public ResponseEntity<BlockDTO> getBlock(@PathVariable Long id) {
        log.debug("REST request to get Block : {}", id);
        Optional<BlockDTO> blockDTO = blockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blockDTO);
    }

    /**
     * {@code DELETE  /blocks/:id} : delete the "id" block.
     *
     * @param id the id of the blockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/blocks/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        log.debug("REST request to delete Block : {}", id);
        blockService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
