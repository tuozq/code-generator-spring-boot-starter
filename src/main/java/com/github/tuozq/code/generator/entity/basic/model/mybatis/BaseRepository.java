package com.github.tuozq.code.generator.entity.basic.model.mybatis;


import com.github.tuozq.code.generator.entity.filter.FilterInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author tuozq
 * @description: 数据层基类
 * @date 2019/5/10.
 */
public interface BaseRepository<M extends BaseEntity<ID>, ID> {

    int insert(M model);

    int insertSelective(M model);

    int deleteByPrimaryKey(ID primaryKey);

    int deleteByPrimaryKeys(List<ID> primaryKeys);

    int deleteByFilter(@Param("filter") FilterInfo filter);

    int updateByPrimaryKey(M model);

    int updateByPrimaryKeySelective(M model);

    int updateByFilterSelective(@Param("model") M model, @Param("filter") FilterInfo filter);

    M selectByPrimaryKey(ID primaryKey);

    List<M> selectByPrimaryKeys(List<ID> var1);

    List<M> selectAll();

    List<M> selectByEntity(M model);

    List<M> selectByFilter(@Param("filter") FilterInfo filter);

    int insertBatch(List<M> list);

    int updateBatch(List<M> list);

    Long count();

    Map<String, Object> selectByFunction(@Param("function") String function, @Param("column") String column);

}
