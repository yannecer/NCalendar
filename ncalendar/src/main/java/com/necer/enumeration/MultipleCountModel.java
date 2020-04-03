package com.necer.enumeration;

import java.io.Serializable;

/**
 * 多选数量的模式
 * @author necer
 */
public enum MultipleCountModel implements Serializable {

    /**
     * 超过数量清除所有
     */
    FULL_CLEAR,

    /**
     * 超过数量清除第一个
     */
    FULL_REMOVE_FIRST
}
