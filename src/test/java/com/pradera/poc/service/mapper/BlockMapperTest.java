package com.pradera.poc.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockMapperTest {

    private BlockMapper blockMapper;

    @BeforeEach
    public void setUp() {
        blockMapper = new BlockMapperImpl();
    }
}
