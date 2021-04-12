package com.pradera.poc.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlowBlockMapperTest {

    private FlowBlockMapper flowBlockMapper;

    @BeforeEach
    public void setUp() {
        flowBlockMapper = new FlowBlockMapperImpl();
    }
}
