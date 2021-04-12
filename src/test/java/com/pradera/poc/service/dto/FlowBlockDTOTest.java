package com.pradera.poc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pradera.poc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlowBlockDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlowBlockDTO.class);
        FlowBlockDTO flowBlockDTO1 = new FlowBlockDTO();
        flowBlockDTO1.setId(1L);
        FlowBlockDTO flowBlockDTO2 = new FlowBlockDTO();
        assertThat(flowBlockDTO1).isNotEqualTo(flowBlockDTO2);
        flowBlockDTO2.setId(flowBlockDTO1.getId());
        assertThat(flowBlockDTO1).isEqualTo(flowBlockDTO2);
        flowBlockDTO2.setId(2L);
        assertThat(flowBlockDTO1).isNotEqualTo(flowBlockDTO2);
        flowBlockDTO1.setId(null);
        assertThat(flowBlockDTO1).isNotEqualTo(flowBlockDTO2);
    }
}
