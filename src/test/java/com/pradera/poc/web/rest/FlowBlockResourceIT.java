package com.pradera.poc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pradera.poc.IntegrationTest;
import com.pradera.poc.domain.Block;
import com.pradera.poc.domain.Flow;
import com.pradera.poc.domain.FlowBlock;
import com.pradera.poc.repository.FlowBlockRepository;
import com.pradera.poc.service.criteria.FlowBlockCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FlowBlockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlowBlockResourceIT {

    private static final Long DEFAULT_BLOCK_ORDER = 1L;
    private static final Long UPDATED_BLOCK_ORDER = 2L;
    private static final Long SMALLER_BLOCK_ORDER = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/flow-blocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlowBlockRepository flowBlockRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlowBlockMockMvc;

    private FlowBlock flowBlock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlowBlock createEntity(EntityManager em) {
        FlowBlock flowBlock = new FlowBlock().blockOrder(DEFAULT_BLOCK_ORDER);
        return flowBlock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlowBlock createUpdatedEntity(EntityManager em) {
        FlowBlock flowBlock = new FlowBlock().blockOrder(UPDATED_BLOCK_ORDER);
        return flowBlock;
    }

    @BeforeEach
    public void initTest() {
        flowBlock = createEntity(em);
    }

    @Test
    @Transactional
    void createFlowBlock() throws Exception {
        int databaseSizeBeforeCreate = flowBlockRepository.findAll().size();
        // Create the FlowBlock
        restFlowBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowBlock)))
            .andExpect(status().isCreated());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeCreate + 1);
        FlowBlock testFlowBlock = flowBlockList.get(flowBlockList.size() - 1);
        assertThat(testFlowBlock.getBlockOrder()).isEqualTo(DEFAULT_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void createFlowBlockWithExistingId() throws Exception {
        // Create the FlowBlock with an existing ID
        flowBlock.setId(1L);

        int databaseSizeBeforeCreate = flowBlockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowBlock)))
            .andExpect(status().isBadRequest());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBlockOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowBlockRepository.findAll().size();
        // set the field null
        flowBlock.setBlockOrder(null);

        // Create the FlowBlock, which fails.

        restFlowBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowBlock)))
            .andExpect(status().isBadRequest());

        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFlowBlocks() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        // Get all the flowBlockList
        restFlowBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flowBlock.getId().intValue())))
            .andExpect(jsonPath("$.[*].blockOrder").value(hasItem(DEFAULT_BLOCK_ORDER.intValue())));
    }

    @Test
    @Transactional
    void getFlowBlock() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        // Get the flowBlock
        restFlowBlockMockMvc
            .perform(get(ENTITY_API_URL_ID, flowBlock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flowBlock.getId().intValue()))
            .andExpect(jsonPath("$.blockOrder").value(DEFAULT_BLOCK_ORDER.intValue()));
    }

    @Test
    @Transactional
    void getFlowBlocksByIdFiltering() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        Long id = flowBlock.getId();

        defaultFlowBlockShouldBeFound("id.equals=" + id);
        defaultFlowBlockShouldNotBeFound("id.notEquals=" + id);

        defaultFlowBlockShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFlowBlockShouldNotBeFound("id.greaterThan=" + id);

        defaultFlowBlockShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFlowBlockShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFlowBlocksByBlockOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        // Get all the flowBlockList where blockOrder equals to DEFAULT_BLOCK_ORDER
        defaultFlowBlockShouldBeFound("blockOrder.equals=" + DEFAULT_BLOCK_ORDER);

        // Get all the flowBlockList where blockOrder equals to UPDATED_BLOCK_ORDER
        defaultFlowBlockShouldNotBeFound("blockOrder.equals=" + UPDATED_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void getAllFlowBlocksByBlockOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        // Get all the flowBlockList where blockOrder not equals to DEFAULT_BLOCK_ORDER
        defaultFlowBlockShouldNotBeFound("blockOrder.notEquals=" + DEFAULT_BLOCK_ORDER);

        // Get all the flowBlockList where blockOrder not equals to UPDATED_BLOCK_ORDER
        defaultFlowBlockShouldBeFound("blockOrder.notEquals=" + UPDATED_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void getAllFlowBlocksByBlockOrderIsInShouldWork() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        // Get all the flowBlockList where blockOrder in DEFAULT_BLOCK_ORDER or UPDATED_BLOCK_ORDER
        defaultFlowBlockShouldBeFound("blockOrder.in=" + DEFAULT_BLOCK_ORDER + "," + UPDATED_BLOCK_ORDER);

        // Get all the flowBlockList where blockOrder equals to UPDATED_BLOCK_ORDER
        defaultFlowBlockShouldNotBeFound("blockOrder.in=" + UPDATED_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void getAllFlowBlocksByBlockOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        // Get all the flowBlockList where blockOrder is not null
        defaultFlowBlockShouldBeFound("blockOrder.specified=true");

        // Get all the flowBlockList where blockOrder is null
        defaultFlowBlockShouldNotBeFound("blockOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllFlowBlocksByBlockOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        // Get all the flowBlockList where blockOrder is greater than or equal to DEFAULT_BLOCK_ORDER
        defaultFlowBlockShouldBeFound("blockOrder.greaterThanOrEqual=" + DEFAULT_BLOCK_ORDER);

        // Get all the flowBlockList where blockOrder is greater than or equal to UPDATED_BLOCK_ORDER
        defaultFlowBlockShouldNotBeFound("blockOrder.greaterThanOrEqual=" + UPDATED_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void getAllFlowBlocksByBlockOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        // Get all the flowBlockList where blockOrder is less than or equal to DEFAULT_BLOCK_ORDER
        defaultFlowBlockShouldBeFound("blockOrder.lessThanOrEqual=" + DEFAULT_BLOCK_ORDER);

        // Get all the flowBlockList where blockOrder is less than or equal to SMALLER_BLOCK_ORDER
        defaultFlowBlockShouldNotBeFound("blockOrder.lessThanOrEqual=" + SMALLER_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void getAllFlowBlocksByBlockOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        // Get all the flowBlockList where blockOrder is less than DEFAULT_BLOCK_ORDER
        defaultFlowBlockShouldNotBeFound("blockOrder.lessThan=" + DEFAULT_BLOCK_ORDER);

        // Get all the flowBlockList where blockOrder is less than UPDATED_BLOCK_ORDER
        defaultFlowBlockShouldBeFound("blockOrder.lessThan=" + UPDATED_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void getAllFlowBlocksByBlockOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        // Get all the flowBlockList where blockOrder is greater than DEFAULT_BLOCK_ORDER
        defaultFlowBlockShouldNotBeFound("blockOrder.greaterThan=" + DEFAULT_BLOCK_ORDER);

        // Get all the flowBlockList where blockOrder is greater than SMALLER_BLOCK_ORDER
        defaultFlowBlockShouldBeFound("blockOrder.greaterThan=" + SMALLER_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void getAllFlowBlocksByFlowIsEqualToSomething() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);
        Flow flow = FlowResourceIT.createEntity(em);
        em.persist(flow);
        em.flush();
        flowBlock.setFlow(flow);
        flowBlockRepository.saveAndFlush(flowBlock);
        Long flowId = flow.getId();

        // Get all the flowBlockList where flow equals to flowId
        defaultFlowBlockShouldBeFound("flowId.equals=" + flowId);

        // Get all the flowBlockList where flow equals to (flowId + 1)
        defaultFlowBlockShouldNotBeFound("flowId.equals=" + (flowId + 1));
    }

    @Test
    @Transactional
    void getAllFlowBlocksByBlockIsEqualToSomething() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);
        Block block = BlockResourceIT.createEntity(em);
        em.persist(block);
        em.flush();
        flowBlock.setBlock(block);
        flowBlockRepository.saveAndFlush(flowBlock);
        Long blockId = block.getId();

        // Get all the flowBlockList where block equals to blockId
        defaultFlowBlockShouldBeFound("blockId.equals=" + blockId);

        // Get all the flowBlockList where block equals to (blockId + 1)
        defaultFlowBlockShouldNotBeFound("blockId.equals=" + (blockId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFlowBlockShouldBeFound(String filter) throws Exception {
        restFlowBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flowBlock.getId().intValue())))
            .andExpect(jsonPath("$.[*].blockOrder").value(hasItem(DEFAULT_BLOCK_ORDER.intValue())));

        // Check, that the count call also returns 1
        restFlowBlockMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFlowBlockShouldNotBeFound(String filter) throws Exception {
        restFlowBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFlowBlockMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFlowBlock() throws Exception {
        // Get the flowBlock
        restFlowBlockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFlowBlock() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        int databaseSizeBeforeUpdate = flowBlockRepository.findAll().size();

        // Update the flowBlock
        FlowBlock updatedFlowBlock = flowBlockRepository.findById(flowBlock.getId()).get();
        // Disconnect from session so that the updates on updatedFlowBlock are not directly saved in db
        em.detach(updatedFlowBlock);
        updatedFlowBlock.blockOrder(UPDATED_BLOCK_ORDER);

        restFlowBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFlowBlock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFlowBlock))
            )
            .andExpect(status().isOk());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeUpdate);
        FlowBlock testFlowBlock = flowBlockList.get(flowBlockList.size() - 1);
        assertThat(testFlowBlock.getBlockOrder()).isEqualTo(UPDATED_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingFlowBlock() throws Exception {
        int databaseSizeBeforeUpdate = flowBlockRepository.findAll().size();
        flowBlock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flowBlock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlowBlock() throws Exception {
        int databaseSizeBeforeUpdate = flowBlockRepository.findAll().size();
        flowBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlowBlock() throws Exception {
        int databaseSizeBeforeUpdate = flowBlockRepository.findAll().size();
        flowBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowBlockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowBlock)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlowBlockWithPatch() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        int databaseSizeBeforeUpdate = flowBlockRepository.findAll().size();

        // Update the flowBlock using partial update
        FlowBlock partialUpdatedFlowBlock = new FlowBlock();
        partialUpdatedFlowBlock.setId(flowBlock.getId());

        restFlowBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlowBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlowBlock))
            )
            .andExpect(status().isOk());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeUpdate);
        FlowBlock testFlowBlock = flowBlockList.get(flowBlockList.size() - 1);
        assertThat(testFlowBlock.getBlockOrder()).isEqualTo(DEFAULT_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateFlowBlockWithPatch() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        int databaseSizeBeforeUpdate = flowBlockRepository.findAll().size();

        // Update the flowBlock using partial update
        FlowBlock partialUpdatedFlowBlock = new FlowBlock();
        partialUpdatedFlowBlock.setId(flowBlock.getId());

        partialUpdatedFlowBlock.blockOrder(UPDATED_BLOCK_ORDER);

        restFlowBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlowBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlowBlock))
            )
            .andExpect(status().isOk());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeUpdate);
        FlowBlock testFlowBlock = flowBlockList.get(flowBlockList.size() - 1);
        assertThat(testFlowBlock.getBlockOrder()).isEqualTo(UPDATED_BLOCK_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingFlowBlock() throws Exception {
        int databaseSizeBeforeUpdate = flowBlockRepository.findAll().size();
        flowBlock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flowBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlowBlock() throws Exception {
        int databaseSizeBeforeUpdate = flowBlockRepository.findAll().size();
        flowBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowBlock))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlowBlock() throws Exception {
        int databaseSizeBeforeUpdate = flowBlockRepository.findAll().size();
        flowBlock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowBlockMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flowBlock))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlowBlock in the database
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlowBlock() throws Exception {
        // Initialize the database
        flowBlockRepository.saveAndFlush(flowBlock);

        int databaseSizeBeforeDelete = flowBlockRepository.findAll().size();

        // Delete the flowBlock
        restFlowBlockMockMvc
            .perform(delete(ENTITY_API_URL_ID, flowBlock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlowBlock> flowBlockList = flowBlockRepository.findAll();
        assertThat(flowBlockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
