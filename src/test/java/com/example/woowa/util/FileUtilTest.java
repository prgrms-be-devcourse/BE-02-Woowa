package com.example.woowa.util;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.common.util.FileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class FileUtilTest {

    @Test
    @DisplayName("txt ������ ������ ���� AreaCode List�� �Ľ��� �� �ִ�.")
    public void parseAreaCodeList() {
        List<AreaCode> areaCodeList = FileUtil.parseAreaCodeList();
        assertThat(areaCodeList).hasSizeGreaterThan(1);
    }
}