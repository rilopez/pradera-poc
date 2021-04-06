package com.pradera.poc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pradera.poc.IntegrationTest;
import com.pradera.poc.domain.Block;
import com.pradera.poc.domain.Book;
import com.pradera.poc.domain.Flow;
import com.pradera.poc.domain.User;
import com.pradera.poc.repository.FlowRepository;
import com.pradera.poc.service.FlowService;
import com.pradera.poc.service.criteria.FlowCriteria;
import com.pradera.poc.service.dto.FlowDTO;
import com.pradera.poc.service.mapper.FlowMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FlowResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FlowResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/flows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlowRepository flowRepository;

    @Mock
    private FlowRepository flowRepositoryMock;

    @Autowired
    private FlowMapper flowMapper;

    @Mock
    private FlowService flowServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlowMockMvc;

    private Flow flow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flow createEntity(EntityManager em) {
        Flow flow = new Flow().name(DEFAULT_NAME);
        return flow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flow createUpdatedEntity(EntityManager em) {
        Flow flow = new Flow().name(UPDATED_NAME);
        return flow;
    }

    @BeforeEach
    public void initTest() {
        flow = createEntity(em);
    }

    @Test
    @Transactional
    void createFlow() throws Exception {
        int databaseSizeBeforeCreate = flowRepository.findAll().size();
        // Create the Flow
        FlowDTO flowDTO = flowMapper.toDto(flow);
        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowDTO)))
            .andExpect(status().isCreated());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeCreate + 1);
        Flow testFlow = flowList.get(flowList.size() - 1);
        assertThat(testFlow.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createFlowWithExistingId() throws Exception {
        // Create the Flow with an existing ID
        flow.setId(1L);
        FlowDTO flowDTO = flowMapper.toDto(flow);

        int databaseSizeBeforeCreate = flowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = flowRepository.findAll().size();
        // set the field null
        flow.setName(null);

        // Create the Flow, which fails.
        FlowDTO flowDTO = flowMapper.toDto(flow);

        restFlowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowDTO)))
            .andExpect(status().isBadRequest());

        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFlows() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        // Get all the flowList
        restFlowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flow.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFlowsWithEagerRelationshipsIsEnabled() throws Exception {
        when(flowServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFlowMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(flowServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFlowsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(flowServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFlowMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(flowServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFlow() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        // Get the flow
        restFlowMockMvc
            .perform(get(ENTITY_API_URL_ID, flow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flow.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getFlowsByIdFiltering() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        Long id = flow.getId();

        defaultFlowShouldBeFound("id.equals=" + id);
        defaultFlowShouldNotBeFound("id.notEquals=" + id);

        defaultFlowShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFlowShouldNotBeFound("id.greaterThan=" + id);

        defaultFlowShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFlowShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFlowsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        // Get all the flowList where name equals to DEFAULT_NAME
        defaultFlowShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the flowList where name equals to UPDATED_NAME
        defaultFlowShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFlowsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        // Get all the flowList where name not equals to DEFAULT_NAME
        defaultFlowShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the flowList where name not equals to UPDATED_NAME
        defaultFlowShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFlowsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        // Get all the flowList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFlowShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the flowList where name equals to UPDATED_NAME
        defaultFlowShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFlowsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        // Get all the flowList where name is not null
        defaultFlowShouldBeFound("name.specified=true");

        // Get all the flowList where name is null
        defaultFlowShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFlowsByNameContainsSomething() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        // Get all the flowList where name contains DEFAULT_NAME
        defaultFlowShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the flowList where name contains UPDATED_NAME
        defaultFlowShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFlowsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        // Get all the flowList where name does not contain DEFAULT_NAME
        defaultFlowShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the flowList where name does not contain UPDATED_NAME
        defaultFlowShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFlowsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        flow.setUser(user);
        flowRepository.saveAndFlush(flow);
        Long userId = user.getId();

        // Get all the flowList where user equals to userId
        defaultFlowShouldBeFound("userId.equals=" + userId);

        // Get all the flowList where user equals to (userId + 1)
        defaultFlowShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllFlowsByBookIsEqualToSomething() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);
        Book book = BookResourceIT.createEntity(em);
        em.persist(book);
        em.flush();
        flow.setBook(book);
        flowRepository.saveAndFlush(flow);
        Long bookId = book.getId();

        // Get all the flowList where book equals to bookId
        defaultFlowShouldBeFound("bookId.equals=" + bookId);

        // Get all the flowList where book equals to (bookId + 1)
        defaultFlowShouldNotBeFound("bookId.equals=" + (bookId + 1));
    }

    @Test
    @Transactional
    void getAllFlowsByBlocksIsEqualToSomething() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);
        Block blocks = BlockResourceIT.createEntity(em);
        em.persist(blocks);
        em.flush();
        flow.addBlocks(blocks);
        flowRepository.saveAndFlush(flow);
        Long blocksId = blocks.getId();

        // Get all the flowList where blocks equals to blocksId
        defaultFlowShouldBeFound("blocksId.equals=" + blocksId);

        // Get all the flowList where blocks equals to (blocksId + 1)
        defaultFlowShouldNotBeFound("blocksId.equals=" + (blocksId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFlowShouldBeFound(String filter) throws Exception {
        restFlowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flow.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restFlowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFlowShouldNotBeFound(String filter) throws Exception {
        restFlowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFlowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFlow() throws Exception {
        // Get the flow
        restFlowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFlow() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        int databaseSizeBeforeUpdate = flowRepository.findAll().size();

        // Update the flow
        Flow updatedFlow = flowRepository.findById(flow.getId()).get();
        // Disconnect from session so that the updates on updatedFlow are not directly saved in db
        em.detach(updatedFlow);
        updatedFlow.name(UPDATED_NAME);
        FlowDTO flowDTO = flowMapper.toDto(updatedFlow);

        restFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flowDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowDTO))
            )
            .andExpect(status().isOk());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
        Flow testFlow = flowList.get(flowList.size() - 1);
        assertThat(testFlow.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // Create the Flow
        FlowDTO flowDTO = flowMapper.toDto(flow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flowDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // Create the Flow
        FlowDTO flowDTO = flowMapper.toDto(flow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // Create the Flow
        FlowDTO flowDTO = flowMapper.toDto(flow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flowDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlowWithPatch() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        int databaseSizeBeforeUpdate = flowRepository.findAll().size();

        // Update the flow using partial update
        Flow partialUpdatedFlow = new Flow();
        partialUpdatedFlow.setId(flow.getId());

        partialUpdatedFlow.name(UPDATED_NAME);

        restFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlow))
            )
            .andExpect(status().isOk());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
        Flow testFlow = flowList.get(flowList.size() - 1);
        assertThat(testFlow.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFlowWithPatch() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        int databaseSizeBeforeUpdate = flowRepository.findAll().size();

        // Update the flow using partial update
        Flow partialUpdatedFlow = new Flow();
        partialUpdatedFlow.setId(flow.getId());

        partialUpdatedFlow.name(UPDATED_NAME);

        restFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlow))
            )
            .andExpect(status().isOk());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
        Flow testFlow = flowList.get(flowList.size() - 1);
        assertThat(testFlow.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // Create the Flow
        FlowDTO flowDTO = flowMapper.toDto(flow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flowDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // Create the Flow
        FlowDTO flowDTO = flowMapper.toDto(flow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlow() throws Exception {
        int databaseSizeBeforeUpdate = flowRepository.findAll().size();
        flow.setId(count.incrementAndGet());

        // Create the Flow
        FlowDTO flowDTO = flowMapper.toDto(flow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlowMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flowDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flow in the database
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlow() throws Exception {
        // Initialize the database
        flowRepository.saveAndFlush(flow);

        int databaseSizeBeforeDelete = flowRepository.findAll().size();

        // Delete the flow
        restFlowMockMvc
            .perform(delete(ENTITY_API_URL_ID, flow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Flow> flowList = flowRepository.findAll();
        assertThat(flowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
