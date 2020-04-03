package com.necer.enumeration;

import java.io.Serializable;

/**
 * @author necer
 */

public enum CheckModel implements Serializable {

    /**
     * 单选，默认每页单个选中，每页都会有选中
     */
    SINGLE_DEFAULT_CHECKED,

    /**
     * 单选，默认页面无选中，点击跳转才会有选中
     */
    SINGLE_DEFAULT_UNCHECKED,

    /**
     * 多选日期 点击跳转选中
     */
    MULTIPLE
}
