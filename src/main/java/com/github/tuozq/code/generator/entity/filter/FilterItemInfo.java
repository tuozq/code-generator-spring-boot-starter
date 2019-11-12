package com.github.tuozq.code.generator.entity.filter;

import com.github.tuozq.code.generator.entity.query.CompareType;
import lombok.Data;

import java.util.Collection;

/**
 * @author tuozq
 * @description:
 * @date 2019/6/19.
 */
@Data
public class FilterItemInfo {

    private boolean noValue;

    private boolean singleValue;

    private boolean listValue;

    /**
     *  过滤字段
     */
    private String propertyName;

    /**
     * 比较类型
     */
    private CompareType compareType;

    /**
     * 比较值
     */
    private Object compareValue;

    FilterItemInfo(String propertyName, Object compareValue, CompareType compareType){
        this.propertyName = propertyName;
        this.compareType = compareType;
        this.compareValue = compareValue;
        if(compareValue instanceof Collection){
            this.listValue = true;
        }else{
            this.singleValue = true;
        }

    }

    FilterItemInfo(String propertyName, CompareType compareType){
        this.propertyName = propertyName;
        this.compareType = compareType;
        this.noValue = true;
    }



}
