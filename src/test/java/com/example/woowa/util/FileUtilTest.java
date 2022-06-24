package com.example.woowa.util;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.common.util.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class FileUtilTest {

    @Test
    @DisplayName("txt 법정동 파일을 통해 AreaCode List를 파싱할 수 있다.")
    public void parseAreaCodeList() {
        List<AreaCode> areaCodeList = FileUtil.parseAreaCodeList();
        assertThat(areaCodeList).hasSizeGreaterThan(1);
    }
}