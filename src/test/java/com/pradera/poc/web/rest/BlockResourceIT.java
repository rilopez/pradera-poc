package com.pradera.poc.web.rest;

import com.pradera.poc.PraderaApp;
import com.pradera.poc.domain.Block;
import com.pradera.poc.domain.Block;
import com.pradera.poc.domain.User;
import com.pradera.poc.repository.BlockRepository;
import com.pradera.poc.repository.search.BlockSearchRepository;
import com.pradera.poc.service.BlockService;
import com.pradera.poc.service.dto.BlockDTO;
import com.pradera.poc.service.mapper.BlockMapper;
import com.pradera.poc.service.dto.BlockCriteria;
import com.pradera.poc.service.BlockQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.pradera.poc.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pradera.poc.domain.enumeration.BlockType;
/**
 * Integration tests for the {@link BlockResource} REST controller.
 */
@SpringBootTest(classes = PraderaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class BlockResourceIT {

    private static final BlockType DEFAULT_TYPE = BlockType.TITLE;
    private static final BlockType UPDATED_TYPE = BlockType.PARAGRAPH;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private BlockService blockService;

    /**
     * This repository is mocked in the com.pradera.poc.repository.search test package.
     *
     * @see com.pradera.poc.repository.search.BlockSearchRepositoryMockConfiguration
     */
    @Autowired
    private BlockSearchRepository mockBlockSearchRepository;

    @Autowired
    private BlockQueryService blockQueryService;

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
        Block block = new Block()
            .type(DEFAULT_TYPE)
            .content(DEFAULT_CONTENT)
            .createdDate(DEFAULT_CREATED_DATE)
            .hash(DEFAULT_HASH);
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
        Block block = new Block()
            .type(UPDATED_TYPE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .hash(UPDATED_HASH);
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
    public void createBlock() throws Exception {
        int databaseSizeBeforeCreate = blockRepository.findAll().size();
        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);
        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isCreated());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate + 1);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBlock.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBlock.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBlock.getHash()).isEqualTo(DEFAULT_HASH);

        // Validate the Block in Elasticsearch
        verify(mockBlockSearchRepository, times(1)).save(testBlock);
    }

    @Test
    @Transactional
    public void createBlockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blockRepository.findAll().size();

        // Create the Block with an existing ID
        block.setId(1L);
        BlockDTO blockDTO = blockMapper.toDto(block);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate);

        // Validate the Block in Elasticsearch
        verify(mockBlockSearchRepository, times(0)).save(block);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = blockRepository.findAll().size();
        // set the field null
        block.setType(null);

        // Create the Block, which fails.
        BlockDTO blockDTO = blockMapper.toDto(block);


        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = blockRepository.findAll().size();
        // set the field null
        block.setContent(null);

        // Create the Block, which fails.
        BlockDTO blockDTO = blockMapper.toDto(block);


        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = blockRepository.findAll().size();
        // set the field null
        block.setCreatedDate(null);

        // Create the Block, which fails.
        BlockDTO blockDTO = blockMapper.toDto(block);


        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = blockRepository.findAll().size();
        // set the field null
        block.setHash(null);

        // Create the Block, which fails.
        BlockDTO blockDTO = blockMapper.toDto(block);


        restBlockMockMvc.perform(post("/api/blocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBlocks() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList
        restBlockMockMvc.perform(get("/api/blocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)));
    }
    
    @Test
    @Transactional
    public void getBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get the block
        restBlockMockMvc.perform(get("/api/blocks/{id}", block.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(block.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH));
    }


    @Test
    @Transactional
    public void getBlocksByIdFiltering() throws Exception {
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
    public void getAllBlocksByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where type equals to DEFAULT_TYPE
        defaultBlockShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the blockList where type equals to UPDATED_TYPE
        defaultBlockShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllBlocksByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where type not equals to DEFAULT_TYPE
        defaultBlockShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the blockList where type not equals to UPDATED_TYPE
        defaultBlockShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllBlocksByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultBlockShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the blockList where type equals to UPDATED_TYPE
        defaultBlockShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllBlocksByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where type is not null
        defaultBlockShouldBeFound("type.specified=true");

        // Get all the blockList where type is null
        defaultBlockShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllBlocksByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where content equals to DEFAULT_CONTENT
        defaultBlockShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the blockList where content equals to UPDATED_CONTENT
        defaultBlockShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllBlocksByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where content not equals to DEFAULT_CONTENT
        defaultBlockShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the blockList where content not equals to UPDATED_CONTENT
        defaultBlockShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllBlocksByContentIsInShouldWork() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultBlockShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the blockList where content equals to UPDATED_CONTENT
        defaultBlockShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllBlocksByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where content is not null
        defaultBlockShouldBeFound("content.specified=true");

        // Get all the blockList where content is null
        defaultBlockShouldNotBeFound("content.specified=false");
    }
                @Test
    @Transactional
    public void getAllBlocksByContentContainsSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where content contains DEFAULT_CONTENT
        defaultBlockShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the blockList where content contains UPDATED_CONTENT
        defaultBlockShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllBlocksByContentNotContainsSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where content does not contain DEFAULT_CONTENT
        defaultBlockShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the blockList where content does not contain UPDATED_CONTENT
        defaultBlockShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }


    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate equals to DEFAULT_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate equals to UPDATED_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate not equals to UPDATED_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the blockList where createdDate equals to UPDATED_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is not null
        defaultBlockShouldBeFound("createdDate.specified=true");

        // Get all the blockList where createdDate is null
        defaultBlockShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is less than DEFAULT_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate is less than UPDATED_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBlocksByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultBlockShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the blockList where createdDate is greater than SMALLER_CREATED_DATE
        defaultBlockShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }


    @Test
    @Transactional
    public void getAllBlocksByHashIsEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash equals to DEFAULT_HASH
        defaultBlockShouldBeFound("hash.equals=" + DEFAULT_HASH);

        // Get all the blockList where hash equals to UPDATED_HASH
        defaultBlockShouldNotBeFound("hash.equals=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    public void getAllBlocksByHashIsNotEqualToSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash not equals to DEFAULT_HASH
        defaultBlockShouldNotBeFound("hash.notEquals=" + DEFAULT_HASH);

        // Get all the blockList where hash not equals to UPDATED_HASH
        defaultBlockShouldBeFound("hash.notEquals=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    public void getAllBlocksByHashIsInShouldWork() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash in DEFAULT_HASH or UPDATED_HASH
        defaultBlockShouldBeFound("hash.in=" + DEFAULT_HASH + "," + UPDATED_HASH);

        // Get all the blockList where hash equals to UPDATED_HASH
        defaultBlockShouldNotBeFound("hash.in=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    public void getAllBlocksByHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash is not null
        defaultBlockShouldBeFound("hash.specified=true");

        // Get all the blockList where hash is null
        defaultBlockShouldNotBeFound("hash.specified=false");
    }
                @Test
    @Transactional
    public void getAllBlocksByHashContainsSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash contains DEFAULT_HASH
        defaultBlockShouldBeFound("hash.contains=" + DEFAULT_HASH);

        // Get all the blockList where hash contains UPDATED_HASH
        defaultBlockShouldNotBeFound("hash.contains=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    public void getAllBlocksByHashNotContainsSomething() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList where hash does not contain DEFAULT_HASH
        defaultBlockShouldNotBeFound("hash.doesNotContain=" + DEFAULT_HASH);

        // Get all the blockList where hash does not contain UPDATED_HASH
        defaultBlockShouldBeFound("hash.doesNotContain=" + UPDATED_HASH);
    }


    @Test
    @Transactional
    public void getAllBlocksByParentIsEqualToSomething() throws Exception {
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

        // Get all the blockList where parent equals to parentId + 1
        defaultBlockShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }


    @Test
    @Transactional
    public void getAllBlocksByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = block.getUser();
        blockRepository.saveAndFlush(block);
        Long userId = user.getId();

        // Get all the blockList where user equals to userId
        defaultBlockShouldBeFound("userId.equals=" + userId);

        // Get all the blockList where user equals to userId + 1
        defaultBlockShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBlockShouldBeFound(String filter) throws Exception {
        restBlockMockMvc.perform(get("/api/blocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)));

        // Check, that the count call also returns 1
        restBlockMockMvc.perform(get("/api/blocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBlockShouldNotBeFound(String filter) throws Exception {
        restBlockMockMvc.perform(get("/api/blocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBlockMockMvc.perform(get("/api/blocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBlock() throws Exception {
        // Get the block
        restBlockMockMvc.perform(get("/api/blocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block
        Block updatedBlock = blockRepository.findById(block.getId()).get();
        // Disconnect from session so that the updates on updatedBlock are not directly saved in db
        em.detach(updatedBlock);
        updatedBlock
            .type(UPDATED_TYPE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .hash(UPDATED_HASH);
        BlockDTO blockDTO = blockMapper.toDto(updatedBlock);

        restBlockMockMvc.perform(put("/api/blocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBlock.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBlock.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBlock.getHash()).isEqualTo(UPDATED_HASH);

        // Validate the Block in Elasticsearch
        verify(mockBlockSearchRepository, times(1)).save(testBlock);
    }

    @Test
    @Transactional
    public void updateNonExistingBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Create the Block
        BlockDTO blockDTO = blockMapper.toDto(block);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockMockMvc.perform(put("/api/blocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(blockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Block in Elasticsearch
        verify(mockBlockSearchRepository, times(0)).save(block);
    }

    @Test
    @Transactional
    public void deleteBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeDelete = blockRepository.findAll().size();

        // Delete the block
        restBlockMockMvc.perform(delete("/api/blocks/{id}", block.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Block in Elasticsearch
        verify(mockBlockSearchRepository, times(1)).deleteById(block.getId());
    }

    @Test
    @Transactional
    public void searchBlock() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        blockRepository.saveAndFlush(block);
        when(mockBlockSearchRepository.search(queryStringQuery("id:" + block.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(block), PageRequest.of(0, 1), 1));

        // Search the block
        restBlockMockMvc.perform(get("/api/_search/blocks?query=id:" + block.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)));
    }
}
