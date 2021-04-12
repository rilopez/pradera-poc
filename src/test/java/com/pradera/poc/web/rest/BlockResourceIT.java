package com.pradera.poc.web.rest;

import static com.pradera.poc.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pradera.poc.IntegrationTest;
import com.pradera.poc.domain.Block;
import com.pradera.poc.domain.Block;
import com.pradera.poc.domain.FlowBlock;
import com.pradera.poc.domain.User;
import com.pradera.poc.domain.enumeration.BlockType;
import com.pradera.poc.repository.BlockRepository;
import com.pradera.poc.service.criteria.BlockCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link BlockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlockResourceIT {

    private static final BlockType DEFAULT_TYPE = BlockType.TITLE;
    private static final BlockType UPDATED_TYPE = BlockType.PARAGRAPH;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/blocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlockMockMvc;

    private Block block;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Block createEntity(EntityManager em) {
        Block block = new Block().type(DEFAULT_TYPE).content(DEFAULT_CONTENT).createdDate(DEFAULT_CREATED_DATE).hash(DEFAULT_HASH);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        block.setUser(user);
        return block;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Block createUpdatedEntity(EntityManager em) {
        Block block = new Block().type(UPDATED_TYPE).content(UPDATED_CONTENT).createdDate(UPDATED_CREATED_DATE).hash(UPDATED_HASH);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        block.setUser(user);
        return block;
    }

    @BeforeEach
    public void initTest() {
        block = createEntity(em);
    }

    @Test
    @Transactional
    void createBlock() throws Exception {
        int databaseSizeBeforeCreate = blockRepository.findAll().size();
        // Create the Block
        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isCreated());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate + 1);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBlock.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBlock.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBlock.getHash()).isEqualTo(DEFAULT_HASH);
    }

    @Test
    @Transactional
    void createBlockWithExistingId() throws Exception {
        // Create the Block with an existing ID
        block.setId(1L);

        int databaseSizeBeforeCreate = blockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = blockRepository.findAll().size();
        // set the field null
        block.setType(null);

        // Create the Block, which fails.

        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isBadRequest());

        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = blockRepository.findAll().size();
        // set the field null
        block.setCreatedDate(null);

        // Create the Block, which fails.

        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isBadRequest());

        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = blockRepository.findAll().size();
        // set the field null
        block.setHash(null);

        // Create the Block, which fails.

        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isBadRequest());

        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBlocks() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)));
    }

    @Test
    @Transactional
    void getBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get the block
        restBlockMockMvc
            .perform(get(ENTITY_API_URL_ID, block.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(block.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH));
    }

    @Test
    @Transactional
    void getBlocksByIdFiltering() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        Long id = block.getId();

        defaultBlockShouldBeFound("id.equals=" + id);
        defaultBlockShouldNotBeFound("id.notEquals=" + id);

        defaultBlockShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBlockShouldNotBeFound("id.greaterThan=" + id);

        defaultBlockShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBlockShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBlocksByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where type equals to DEFAULT_TYPE
        defaultBlockShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the blockList where type equals to UPDATED_TYPE
        defaultBlockShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBlocksByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where type not equals to DEFAULT_TYPE
        defaultBlockShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the blockList where type not equals to UPDATED_TYPE
        defaultBlockShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBlocksByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultBlockShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the blockList where type equals to UPDATED_TYPE
        defaultBlockShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllBlocksByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where type is not null
        defaultBlockShouldBeFound("type.specified=true");

        // Get all the blockList where type is null
        defaultBlockShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllBlocksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate equals to DEFAULT_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate equals to UPDATED_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBlocksByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate not equals to UPDATED_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBlocksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the blockList where createdDate equals to UPDATED_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBlocksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is not null
        defaultBlockShouldBeFound("createdDate.specified=true");

        // Get all the blockList where createdDate is null
        defaultBlockShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBlocksByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBlocksByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBlocksByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is less than DEFAULT_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate is less than UPDATED_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBlocksByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate is greater than SMALLER_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBlocksByHashIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash equals to DEFAULT_HASH
        defaultBlockShouldBeFound("hash.equals=" + DEFAULT_HASH);

        // Get all the blockList where hash equals to UPDATED_HASH
        defaultBlockShouldNotBeFound("hash.equals=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllBlocksByHashIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash not equals to DEFAULT_HASH
        defaultBlockShouldNotBeFound("hash.notEquals=" + DEFAULT_HASH);

        // Get all the blockList where hash not equals to UPDATED_HASH
        defaultBlockShouldBeFound("hash.notEquals=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllBlocksByHashIsInShouldWork() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash in DEFAULT_HASH or UPDATED_HASH
        defaultBlockShouldBeFound("hash.in=" + DEFAULT_HASH + "," + UPDATED_HASH);

        // Get all the blockList where hash equals to UPDATED_HASH
        defaultBlockShouldNotBeFound("hash.in=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllBlocksByHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash is not null
        defaultBlockShouldBeFound("hash.specified=true");

        // Get all the blockList where hash is null
        defaultBlockShouldNotBeFound("hash.specified=false");
    }

    @Test
    @Transactional
    void getAllBlocksByHashContainsSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash contains DEFAULT_HASH
        defaultBlockShouldBeFound("hash.contains=" + DEFAULT_HASH);

        // Get all the blockList where hash contains UPDATED_HASH
        defaultBlockShouldNotBeFound("hash.contains=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllBlocksByHashNotContainsSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash does not contain DEFAULT_HASH
        defaultBlockShouldNotBeFound("hash.doesNotContain=" + DEFAULT_HASH);

        // Get all the blockList where hash does not contain UPDATED_HASH
        defaultBlockShouldBeFound("hash.doesNotContain=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    void getAllBlocksByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);
        Block parent = BlockResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        block.setParent(parent);
        blockRepository.saveAndFlush(block);
        Long parentId = parent.getId();

        // Get all the blockList where parent equals to parentId
        defaultBlockShouldBeFound("parentId.equals=" + parentId);

        // Get all the blockList where parent equals to (parentId + 1)
        defaultBlockShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllBlocksByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        block.setUser(user);
        blockRepository.saveAndFlush(block);
        Long userId = user.getId();

        // Get all the blockList where user equals to userId
        defaultBlockShouldBeFound("userId.equals=" + userId);

        // Get all the blockList where user equals to (userId + 1)
        defaultBlockShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllBlocksByParentBlocksIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);
        Block parentBlocks = BlockResourceIT.createEntity(em);
        em.persist(parentBlocks);
        em.flush();
        block.addParentBlocks(parentBlocks);
        blockRepository.saveAndFlush(block);
        Long parentBlocksId = parentBlocks.getId();

        // Get all the blockList where parentBlocks equals to parentBlocksId
        defaultBlockShouldBeFound("parentBlocksId.equals=" + parentBlocksId);

        // Get all the blockList where parentBlocks equals to (parentBlocksId + 1)
        defaultBlockShouldNotBeFound("parentBlocksId.equals=" + (parentBlocksId + 1));
    }

    @Test
    @Transactional
    void getAllBlocksByFlowsIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);
        FlowBlock flows = FlowBlockResourceIT.createEntity(em);
        em.persist(flows);
        em.flush();
        block.addFlows(flows);
        blockRepository.saveAndFlush(block);
        Long flowsId = flows.getId();

        // Get all the blockList where flows equals to flowsId
        defaultBlockShouldBeFound("flowsId.equals=" + flowsId);

        // Get all the blockList where flows equals to (flowsId + 1)
        defaultBlockShouldNotBeFound("flowsId.equals=" + (flowsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBlockShouldBeFound(String filter) throws Exception {
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)));

        // Check, that the count call also returns 1
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBlockShouldNotBeFound(String filter) throws Exception {
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBlock() throws Exception {
        // Get the block
        restBlockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block
        Block updatedBlock = blockRepository.findById(block.getId()).get();
        // Disconnect from session so that the updates on updatedBlock are not directly saved in db
        em.detach(updatedBlock);
        updatedBlock.type(UPDATED_TYPE).content(UPDATED_CONTENT).createdDate(UPDATED_CREATED_DATE).hash(UPDATED_HASH);

        restBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBlock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBlock))
            )
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBlock.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBlock.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBlock.getHash()).isEqualTo(UPDATED_HASH);
    }

    @Test
    @Transactional
    void putNonExistingBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, block.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(block))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(block))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBlockWithPatch() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block using partial update
        Block partialUpdatedBlock = new Block();
        partialUpdatedBlock.setId(block.getId());

        partialUpdatedBlock.createdDate(UPDATED_CREATED_DATE);

        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlock))
            )
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBlock.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBlock.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBlock.getHash()).isEqualTo(DEFAULT_HASH);
    }

    @Test
    @Transactional
    void fullUpdateBlockWithPatch() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block using partial update
        Block partialUpdatedBlock = new Block();
        partialUpdatedBlock.setId(block.getId());

        partialUpdatedBlock.type(UPDATED_TYPE).content(UPDATED_CONTENT).createdDate(UPDATED_CREATED_DATE).hash(UPDATED_HASH);

        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlock))
            )
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBlock.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBlock.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBlock.getHash()).isEqualTo(UPDATED_HASH);
    }

    @Test
    @Transactional
    void patchNonExistingBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, block.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(block))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(block))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeDelete = blockRepository.findAll().size();

        // Delete the block
        restBlockMockMvc
            .perform(delete(ENTITY_API_URL_ID, block.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
