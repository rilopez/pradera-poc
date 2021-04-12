package com.pradera.poc.web.rest;

import com.pradera.poc.domain.Flow;
import com.pradera.poc.repository.FlowRepository;
import com.pradera.poc.service.FlowQueryService;
import com.pradera.poc.service.FlowService;
import com.pradera.poc.service.criteria.FlowCriteria;
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
 * REST controller for managing {@link com.pradera.poc.domain.Flow}.
 */
@RestController
@RequestMapping("/api")
public class FlowResource {

    private final Logger log = LoggerFactory.getLogger(FlowResource.class);

    private static final String ENTITY_NAME = "flow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlowService flowService;

    private final FlowRepository flowRepository;

    private final FlowQueryService flowQueryService;

    public FlowResource(FlowService flowService, FlowRepository flowRepository, FlowQueryService flowQueryService) {
        this.flowService = flowService;
        this.flowRepository = flowRepository;
        this.flowQueryService = flowQueryService;
    }

    /**
     * {@code POST  /flows} : Create a new flow.
     *
     * @param flow the flow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flow, or with status {@code 400 (Bad Request)} if the flow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flows")
    public ResponseEntity<Flow> createFlow(@Valid @RequestBody Flow flow) throws URISyntaxException {
        log.debug("REST request to save Flow : {}", flow);
        if (flow.getId() != null) {
            throw new BadRequestAlertException("A new flow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Flow result = flowService.save(flow);
        return ResponseEntity
            .created(new URI("/api/flows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flows/:id} : Updates an existing flow.
     *
     * @param id the id of the flow to save.
     * @param flow the flow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flow,
     * or with status {@code 400 (Bad Request)} if the flow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flows/{id}")
    public ResponseEntity<Flow> updateFlow(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Flow flow)
        throws URISyntaxException {
        log.debug("REST request to update Flow : {}, {}", id, flow);
        if (flow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Flow result = flowService.save(flow);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flow.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flows/:id} : Partial updates given fields of an existing flow, field will ignore if it is null
     *
     * @param id the id of the flow to save.
     * @param flow the flow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flow,
     * or with status {@code 400 (Bad Request)} if the flow is not valid,
     * or with status {@code 404 (Not Found)} if the flow is not found,
     * or with status {@code 500 (Internal Server Error)} if the flow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flows/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Flow> partialUpdateFlow(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Flow flow
    ) throws URISyntaxException {
        log.debug("REST request to partial update Flow partially : {}, {}", id, flow);
        if (flow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Flow> result = flowService.partialUpdate(flow);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flow.getId().toString())
        );
    }

    /**
     * {@code GET  /flows} : get all the flows.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flows in body.
     */
    @GetMapping("/flows")
    public ResponseEntity<List<Flow>> getAllFlows(FlowCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Flows by criteria: {}", criteria);
        Page<Flow> page = flowQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flows/count} : count all the flows.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/flows/count")
    public ResponseEntity<Long> countFlows(FlowCriteria criteria) {
        log.debug("REST request to count Flows by criteria: {}", criteria);
        return ResponseEntity.ok().body(flowQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /flows/:id} : get the "id" flow.
     *
     * @param id the id of the flow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flows/{id}")
    public ResponseEntity<Flow> getFlow(@PathVariable Long id) {
        log.debug("REST request to get Flow : {}", id);
        Optional<Flow> flow = flowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flow);
    }

    /**
     * {@code DELETE  /flows/:id} : delete the "id" flow.
     *
     * @param id the id of the flow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flows/{id}")
    public ResponseEntity<Void> deleteFlow(@PathVariable Long id) {
        log.debug("REST request to delete Flow : {}", id);
        flowService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
