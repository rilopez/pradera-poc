package com.pradera.poc.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BlockMapperTest {

    private BlockMapper blockMapper;

    @BeforeEach
    public void setUp() {
        blockMapper = new BlockMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(blockMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(blockMapper.fromId(null)).isNull();
    }
}
