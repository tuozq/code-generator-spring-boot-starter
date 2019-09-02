package com.github.tuozq.code.generator.entity.filter;

import com.github.tuozq.code.generator.entity.query.CompareType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author tuozq
 * @description:
 * @date 2019/6/19.
 */
@Data
public class FilterInfo {

    private static final String ORDER_BY = "orderBy";

    private List<FilterItemInfo> filterItems = new ArrayList<>();

    private String ordreByClause = null;

    public void clear(){
        this.filterItems.clear();
        this.ordreByClause = null;
    }


    public FilterInfo orderBy(String ordreByClause){
        this.ordreByClause = ordreByClause;
        return this;
    }

    public FilterInfo filter(String field, Object value, CompareType type) {
        if(value == null){
            return this;
        }
        if(value.equals("%") || value.equals("%%")){
            return this;
        }
        this.filterItems.add(new FilterItemInfo(field, value, type));
        return this;
    }

    private FilterInfo filter(String field, CompareType type) {
        this.filterItems.add(new FilterItemInfo(field, type));
        return this;
    }

    public FilterInfo equal(String field, Object value) {
        return filter(field, value, CompareType.EQUALS);
    }

    public FilterInfo equalNumber( Object value) {
        return equal("FNumber", value);
    }

    public FilterInfo equalFParentId( Object value) {
        return equal("FParentId", value);
    }

    public FilterInfo notEqual(String field, Object value) {
        return filter(field, value, CompareType.NOTEQUALS);
    }

    public FilterInfo greater(String field, Object value) {
        return filter(field, value, CompareType.GREATER);
    }

    public FilterInfo greaterEqual(String field, Object value) {
        return filter(field, value, CompareType.GREATER_EQUALS);
    }

    public FilterInfo less(String field, Object value) {
        return filter(field, value, CompareType.LESS);
    }

    public FilterInfo lessEqual(String field, Object value) {
        return filter(field, value, CompareType.LESS_EQUALS);
    }

    public FilterInfo like(String field, Object value) {
        return filter(field, value, CompareType.LIKE);
    }

    public FilterInfo notLike(String field, Object value) {
        return filter(field, value, CompareType.NOTLIKE);
    }

    public FilterInfo inner(String field, Object value) {
        return filter(field, value, CompareType.INNER);
    }

    public FilterInfo notInner(String field, Object value) {
        return filter(field, value, CompareType.NOTINNER);
    }

    public FilterInfo exists(Object value) {
        return filter(null, value, CompareType.EXISTS);
    }

    public FilterInfo notExists(Object value) {
        return filter(null, value, CompareType.NOTEXISTS);
    }

    public FilterInfo empty(String field) {
        return filter(field, null, CompareType.IS);
    }

    public FilterInfo notEmpty(String field) {
        return filter(field, null, CompareType.ISNOT);
    }

    public FilterInfo is(String field, Object value) {
        return filter(field, value, CompareType.IS);
    }

    public FilterInfo isNot(String field, Object value) {
        return filter(field, value, CompareType.ISNOT);
    }

    public FilterInfo include(String field, Set set) {
        return filter(field, set, CompareType.INCLUDE);
    }

    public FilterInfo include(String field, Object[] values) {
        return filter(field, values, CompareType.INCLUDE);
    }

    public FilterInfo include(String field, List list) {
        return filter(field, list, CompareType.INCLUDE);
    }

    public FilterInfo notInclude(String field, Set set) {
        return filter(field, set, CompareType.NOTINCLUDE);
    }

    public FilterInfo notInclude(String field, Object[] values) {
        return filter(field, values, CompareType.NOTINCLUDE);
    }

    public FilterInfo notInclude(String field, List list) {
        return filter(field, list, CompareType.NOTINCLUDE);
    }


    public FilterInfo isNull(String field) {
        return filter(field, CompareType.ISNULL);
    }

    public FilterInfo isNotNull(String field) {
        return filter(field, CompareType.ISNOTNULL);
    }

    /**
     * 左Like条件
     * @param field
     * @param value
     * @return
     */
    public FilterInfo leftLike(String field, Object value){
        if(value != null && value.toString().length() == 0){
            return this;
        }
        return like(field, "%" + value);
    }

    /**
     * 右Like条件
     * @param field
     * @param value
     * @return
     */
    public FilterInfo rightLike(String field, Object value){
        if(value != null && value.toString().length() == 0){
            return this;
        }
        return like(field, value + "%");
    }

}
