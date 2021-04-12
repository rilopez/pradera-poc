package com.pradera.poc.web.rest;

import com.pradera.poc.repository.FlowBlockRepository;
import com.pradera.poc.service.FlowBlockQueryService;
import com.pradera.poc.service.FlowBlockService;
import com.pradera.poc.service.criteria.FlowBlockCriteria;
import com.pradera.poc.service.dto.FlowBlockDTO;
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
 * REST controller for managing {@link com.pradera.poc.domain.FlowBlock}.
 */
@RestController
@RequestMapping("/api")
public class FlowBlockResource {

    private final Logger log = LoggerFactory.getLogger(FlowBlockResource.class);

    private static final String ENTITY_NAME = "flowBlock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlowBlockService flowBlockService;

    private final FlowBlockRepository flowBlockRepository;

    private final FlowBlockQueryService flowBlockQueryService;

    public FlowBlockResource(
        FlowBlockService flowBlockService,
        FlowBlockRepository flowBlockRepository,
        FlowBlockQueryService flowBlockQueryService
    ) {
        this.flowBlockService = flowBlockService;
        this.flowBlockRepository = flowBlockRepository;
        this.flowBlockQueryService = flowBlockQueryService;
    }

    /**
     * {@code POST  /flow-blocks} : Create a new flowBlock.
     *
     * @param flowBlockDTO the flowBlockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flowBlockDTO, or with status {@code 400 (Bad Request)} if the flowBlock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flow-blocks")
    public ResponseEntity<FlowBlockDTO> createFlowBlock(@Valid @RequestBody FlowBlockDTO flowBlockDTO) throws URISyntaxException {
        log.debug("REST request to save FlowBlock : {}", flowBlockDTO);
        if (flowBlockDTO.getId() != null) {
            throw new BadRequestAlertException("A new flowBlock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlowBlockDTO result = flowBlockService.save(flowBlockDTO);
        return ResponseEntity
            .created(new URI("/api/flow-blocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flow-blocks/:id} : Updates an existing flowBlock.
     *
     * @param id the id of the flowBlockDTO to save.
     * @param flowBlockDTO the flowBlockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flowBlockDTO,
     * or with status {@code 400 (Bad Request)} if the flowBlockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flowBlockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flow-blocks/{id}")
    public ResponseEntity<FlowBlockDTO> updateFlowBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FlowBlockDTO flowBlockDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FlowBlock : {}, {}", id, flowBlockDTO);
        if (flowBlockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flowBlockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FlowBlockDTO result = flowBlockService.save(flowBlockDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flowBlockDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flow-blocks/:id} : Partial updates given fields of an existing flowBlock, field will ignore if it is null
     *
     * @param id the id of the flowBlockDTO to save.
     * @param flowBlockDTO the flowBlockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flowBlockDTO,
     * or with status {@code 400 (Bad Request)} if the flowBlockDTO is not valid,
     * or with status {@code 404 (Not Found)} if the flowBlockDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the flowBlockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flow-blocks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FlowBlockDTO> partialUpdateFlowBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FlowBlockDTO flowBlockDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FlowBlock partially : {}, {}", id, flowBlockDTO);
        if (flowBlockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flowBlockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlowBlockDTO> result = flowBlockService.partialUpdate(flowBlockDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flowBlockDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /flow-blocks} : get all the flowBlocks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flowBlocks in body.
     */
    @GetMapping("/flow-blocks")
    public ResponseEntity<List<FlowBlockDTO>> getAllFlowBlocks(FlowBlockCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FlowBlocks by criteria: {}", criteria);
        Page<FlowBlockDTO> page = flowBlockQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flow-blocks/count} : count all the flowBlocks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/flow-blocks/count")
    public ResponseEntity<Long> countFlowBlocks(FlowBlockCriteria criteria) {
        log.debug("REST request to count FlowBlocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(flowBlockQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /flow-blocks/:id} : get the "id" flowBlock.
     *
     * @param id the id of the flowBlockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flowBlockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flow-blocks/{id}")
    public ResponseEntity<FlowBlockDTO> getFlowBlock(@PathVariable Long id) {
        log.debug("REST request to get FlowBlock : {}", id);
        Optional<FlowBlockDTO> flowBlockDTO = flowBlockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flowBlockDTO);
    }

    /**
     * {@code DELETE  /flow-blocks/:id} : delete the "id" flowBlock.
     *
     * @param id the id of the flowBlockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flow-blocks/{id}")
    public ResponseEntity<Void> deleteFlowBlock(@PathVariable Long id) {
        log.debug("REST request to delete FlowBlock : {}", id);
        flowBlockService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
