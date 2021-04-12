package com.pradera.poc.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pradera.poc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlowBlockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlowBlock.class);
        FlowBlock flowBlock1 = new FlowBlock();
        flowBlock1.setId(1L);
        FlowBlock flowBlock2 = new FlowBlock();
        flowBlock2.setId(flowBlock1.getId());
        assertThat(flowBlock1).isEqualTo(flowBlock2);
        flowBlock2.setId(2L);
        assertThat(flowBlock1).isNotEqualTo(flowBlock2);
        flowBlock1.setId(null);
        assertThat(flowBlock1).isNotEqualTo(flowBlock2);
    }
}
