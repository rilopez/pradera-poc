package com.pradera.poc.web.rest;

import com.pradera.poc.service.BlockService;
import com.pradera.poc.web.rest.errors.BadRequestAlertException;
import com.pradera.poc.service.dto.BlockDTO;
import com.pradera.poc.service.dto.BlockCriteria;
import com.pradera.poc.service.BlockQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

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

    private final BlockQueryService blockQueryService;

    public BlockResource(BlockService blockService, BlockQueryService blockQueryService) {
        this.blockService = blockService;
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
        return ResponseEntity.created(new URI("/api/blocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /blocks} : Updates an existing block.
     *
     * @param blockDTO the blockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blockDTO,
     * or with status {@code 400 (Bad Request)} if the blockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/blocks")
    public ResponseEntity<BlockDTO> updateBlock(@Valid @RequestBody BlockDTO blockDTO) throws URISyntaxException {
        log.debug("REST request to update Block : {}", blockDTO);
        if (blockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BlockDTO result = blockService.save(blockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blockDTO.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/blocks?query=:query} : search for the block corresponding
     * to the query.
     *
     * @param query the query of the block search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/blocks")
    public ResponseEntity<List<BlockDTO>> searchBlocks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Blocks for query {}", query);
        Page<BlockDTO> page = blockService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
