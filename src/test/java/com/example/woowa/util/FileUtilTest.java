package com.example.woowa.util;

import com.example.woowa.common.AreaCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class FileUtilTest {

    @Test
    public void _1() {
        List<AreaCode> areaCodeList = FileUtil.parseAreaCodeList();
        assertThat(areaCodeList).hasSizeGreaterThan(1);
    }
}