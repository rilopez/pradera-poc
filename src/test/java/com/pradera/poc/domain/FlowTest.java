package com.pradera.poc.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pradera.poc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Flow.class);
        Flow flow1 = new Flow();
        flow1.setId(1L);
        Flow flow2 = new Flow();
        flow2.setId(flow1.getId());
        assertThat(flow1).isEqualTo(flow2);
        flow2.setId(2L);
        assertThat(flow1).isNotEqualTo(flow2);
        flow1.setId(null);
        assertThat(flow1).isNotEqualTo(flow2);
    }
}
