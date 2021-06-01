package com.pradera.poc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.pradera.poc.domain.Block;
import com.pradera.poc.domain.Flow;
import com.pradera.poc.domain.FlowBlock;
import com.pradera.poc.domain.User;
import com.pradera.poc.domain.enumeration.BlockType;
import com.pradera.poc.repository.BlockRepository;
import com.pradera.poc.repository.FlowBlockRepository;
import com.pradera.poc.repository.FlowRepository;
import com.pradera.poc.repository.UserRepository;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Flow}.
 */
@Service
@Transactional
public class FlowService {

    private final Logger log = LoggerFactory.getLogger(FlowService.class);

    private final FlowRepository flowRepository;
    private final BlockRepository blockRepository;
    private final FlowBlockRepository flowBlockRepository;
    private final UserRepository userRepository;

    public FlowService(
        FlowRepository flowRepository,
        BlockRepository blockRepository,
        FlowBlockRepository flowBlockRepository,
        UserRepository userRepository
    ) {
        this.flowRepository = flowRepository;
        this.blockRepository = blockRepository;
        this.flowBlockRepository = flowBlockRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a flow.
     *
     * @param flow the entity to save.
     * @return the persisted entity.
     */
    public Flow save(Flow flow) {
        log.debug("Request to save Flow : {}", flow);
        return flowRepository.save(flow);
    }

    /**
     * Partially update a flow.
     *
     * @param flow the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Flow> partialUpdate(Flow flow) {
        log.debug("Request to partially update Flow : {}", flow);

        return flowRepository
            .findById(flow.getId())
            .map(
                existingFlow -> {
                    if (flow.getName() != null) {
                        existingFlow.setName(flow.getName());
                    }

                    return existingFlow;
                }
            )
            .map(flowRepository::save);
    }

    /**
     * Get all the flows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Flow> findAll(Pageable pageable) {
        log.debug("Request to get all Flows");
        return flowRepository.findAll(pageable);
    }

    /**
     * Get one flow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Flow> findOne(Long id) {
        log.debug("Request to get Flow : {}", id);
        return flowRepository.findById(id);
    }

    @Transactional
    public Optional<Flow> updateDocState(String currentUser, Long id, JsonNode docStateJson) throws JsonProcessingException {
        log.debug("updateDocState: {}, {}", id, docStateJson);

        User user = userRepository.findOneByLogin(currentUser).orElseThrow();

        //TODO load all nodes in flow
        Map<Long, Block> blocksInFlow = blockRepository.findByFlowId(id).stream().collect(Collectors.toMap(Block::getId, block -> block));
        //delete existing flow
        flowBlockRepository.deleteByFlowId(id);

        Flow flow = findOne(id).orElseThrow();
        flow.blocks(new LinkedHashSet<>());
        if (!flow.getBlocks().isEmpty()) {
            throw new RuntimeException("flow must not have any blocks at this point");
        }
        JsonNode content = docStateJson.get("content");
        long order = 1;
        for (JsonNode contentItem : content) {
            log.debug("item: {}", contentItem);
            JsonNode attrs = contentItem.get("attrs");
            JsonNode blockIdNode = attrs.get("blockId");
            String nodeType = contentItem.get("type").asText();

            JsonNode itemContent = contentItem.get("content");
            String nodeText = itemContent.get(0).get("text").asText();
            Block currentBlock;
            if (blockIdNode != null) {
                long blockId = blockIdNode.asLong();
                if (!blocksInFlow.containsKey(blockId)) {
                    throw new RuntimeException("Block with ID " + blockId + " not found in flow with ID " + id);
                }
                Block existingBlock = blocksInFlow.get(blockId);

                currentBlock = existingBlock;
                //did  the node changed? if so create a new one based in the last one
                if (
                    !StringUtils.equals(existingBlock.getContent(), nodeText) ||
                    !StringUtils.equals(nodeType, existingBlock.getType().toEditorType())
                ) {
                    Block newBlock = Block.Builder
                        .aBlock()
                        .type(BlockType.fromString(nodeType))
                        .content(nodeText)
                        .parent(existingBlock)
                        .user(user)
                        .hash("seudohash")
                        .createdDate(ZonedDateTime.now())
                        .build();
                    //  if block created attach to flow
                    blockRepository.save(newBlock);
                    currentBlock = newBlock;
                }
            } else {
                Block newBlock = Block.Builder
                    .aBlock()
                    .type(BlockType.fromString(nodeType))
                    .content(nodeText)
                    .user(user)
                    .createdDate(ZonedDateTime.now())
                    .hash("seudohash")
                    .build();
                blockRepository.save(newBlock);
                currentBlock = newBlock;
            }

            FlowBlock flowBlock = FlowBlock.Builder.aFlowBlock().block(currentBlock).flow(flow).blockOrder(order).build();
            flowBlockRepository.save(flowBlock);

            order++;
        }
        return findOne(id);
    }

    /**
     * Delete the flow by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Flow : {}", id);
        flowRepository.deleteById(id);
    }
}
