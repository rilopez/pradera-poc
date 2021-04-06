package com.pradera.poc.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pradera.poc.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BlockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Block.class);
        Block block1 = new Block();
        block1.setId(1L);
        Block block2 = new Block();
        block2.setId(block1.getId());
        assertThat(block1).isEqualTo(block2);
        block2.setId(2L);
        assertThat(block1).isNotEqualTo(block2);
        block1.setId(null);
        assertThat(block1).isNotEqualTo(block2);
    }
}
