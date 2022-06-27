package com.example.woowa.util;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.common.util.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class FileUtilTest {

    @Test
    @DisplayName("txt 파일을 읽어서 행정구역을 저장할 수 있다.")
    public void parseAreaCodeList() {
        List<AreaCode> areaCodeList = FileUtil.parseAreaCodeList();
        assertThat(areaCodeList).hasSizeGreaterThan(1);
    }
}