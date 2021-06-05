package com.pradera.poc.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pradera.poc.IntegrationTest;
import com.pradera.poc.domain.Block;
import com.pradera.poc.domain.Flow;
import com.pradera.poc.domain.FlowBlock;
import com.pradera.poc.domain.enumeration.BlockType;
import com.pradera.poc.repository.FlowBlockRepository;
import com.pradera.poc.repository.FlowRepository;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@Transactional
class FlowServiceTestIT {

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowRepository flowRepository;

    @Autowired
    private FlowRepository blockRepository;

    @Autowired
    private FlowBlockRepository flowBlockRepository;

    @BeforeEach
    void setupData() {
        Flow flow = Flow.Builder.aFlow().name("official").build();

        flow.addBlocks(Block.Builder.aBlock().content("This is a Title").type(BlockType.TITLE).build());
        flow.addBlocks(Block.Builder.aBlock().content("Hello world").type(BlockType.PARAGRAPH).build());
        flow.addBlocks(Block.Builder.aBlock().content("bye").type(BlockType.PARAGRAPH).build());

        flowRepository.saveAndFlush(flow);
    }

    @Test
    @Disabled
    void assertThatUploadDocumentReplacesOldFlow() throws JsonProcessingException {
        List<Flow> all = flowRepository.findAll();
        assertThat(all).hasSize(1);
        Flow official = all.get(0);

        Set<FlowBlock> blocks = official.getBlocks();
        assertThat(blocks).hasSize(3);

        InputStream jsonStream = Objects.requireNonNull(this.getClass().getResourceAsStream("/docstate.json"));
        String json = new BufferedReader(new InputStreamReader(jsonStream, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode docStateJson = objectMapper.readTree(json);

        ObjectNode titleNode = (ObjectNode) docStateJson.get("content").get(0);
        ObjectNode titleNodeFirstItem = (ObjectNode) titleNode.get("content").get(0);
        titleNodeFirstItem.put("text", "New title");
        Optional<Flow> updatedFlow = flowService.updateDocState("user", official.getId(), docStateJson);

        assertThat(updatedFlow).isPresent();

        assertThat(official.toString()).isEqualToIgnoringWhitespace("# New title\n" + "Hello world\n" + "bye.");
    }
}
