package com.github.tuozq.code.generator.database.mybatis.basic.model;

import com.github.pagehelper.PageHelper;
import com.github.tuozq.code.generator.entity.filter.FilterInfo;
import com.github.tuozq.code.generator.entity.query.PageResult;
import com.github.tuozq.code.generator.entity.query.Pagenation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/10.
 */

public class BaseService <R extends BaseRepository<E, ID>, E extends BaseEntity<ID>, ID> {

    @Autowired
    protected R mapper;

    public BaseService() {
    }

    public int insert(E entity) {
        this.beforeInsert(entity);
        return this.mapper.insert(entity);
    }

    public int insertSelective(E entity) {
        this.beforeInsert(entity);
        return this.mapper.insertSelective(entity);
    }

    public int deleteByPrimaryKey(ID id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    public int deleteByPrimaryKeys(Collection<ID> ids) {
        return this.mapper.deleteByPrimaryKeys(new ArrayList(ids));
    }


    public int delete(FilterInfo filter) {
        return this.mapper.deleteByFilter(filter);
    }


    public int updateByPrimaryKey(E entity) {
        this.beforeUpdate(entity);
        return this.mapper.updateByPrimaryKey(entity);
    }

    public int updateByPrimaryKeySelective(E entity) {
        this.beforeUpdate(entity);
        return this.mapper.updateByPrimaryKeySelective(entity);
    }

    public int updateSelective(E entity, FilterInfo filter) {
        this.beforeUpdate(entity);
        return this.mapper.updateByFilterSelective(entity, filter);
    }

    public E selectByPrimaryKey(ID id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    public List<E> selectByPrimaryKeys(Collection<ID> ids) {
        return this.mapper.selectByPrimaryKeys(new ArrayList(ids));
    }

    public List<E> selectAll() {
        return this.mapper.selectAll();
    }

    public List<E> selectByEntity(E entity) {
        return this.mapper.selectByEntity(entity);
    }

    public E selectOneByEntity(E entity) {
        List<E> entities = this.mapper.selectByEntity(entity);
        return !Objects.isNull(entities) && !entities.isEmpty() ? (E)entities.get(0) : null;
    }

    public List<E> select(FilterInfo filter) {
        return this.mapper.selectByFilter(filter);
    }

    public E selectFirst(FilterInfo filter) {
        List<E> entities = this.mapper.selectByFilter(filter);
        return !Objects.isNull(entities) && !entities.isEmpty() ? (E)entities.get(0) : null;
    }

    public PageResult selectPage(Pagenation page) {
        PageHelper.startPage(page.getPage(), page.getPagesize());
        List<E> list = this.mapper.selectAll();
        return new PageResult(list);
    }

    public PageResult selectPage(Pagenation page, E entity) {
        PageHelper.startPage(page.getPage(), page.getPagesize());
        List<E> list = this.mapper.selectByEntity(entity);
        return new PageResult(list);
    }

    public PageResult selectPage(Pagenation page, FilterInfo filter) {
        PageHelper.startPage(page.getPage(), page.getPagesize());
        List<E> list = this.select(filter);
        return new PageResult(list);
    }

    public int insertBatch(List<E> entities) {
        return this.insertBatch(entities, 500);
    }

    public int insertBatch(List<E> entities, int batchSize) {
        this.beforeInsert(entities);
        int size = entities.size();
        int batchTime = size / batchSize;
        int remaining = size % batchSize;
        int updateCount = 0;

        for(int i = 0; i < batchTime; ++i) {
            updateCount += this.mapper.insertBatch(entities.subList(i * batchSize, (i + 1) * batchSize));
        }

        if (remaining > 0) {
            updateCount += this.mapper.insertBatch(entities.subList(size - remaining, size));
        }

        return updateCount;
    }

    public int updateBatch(List<E> entities) {
        return this.updateBatch(entities, 500);
    }

    public int updateBatch(List<E> entities, int batchSize) {
        this.beforeUpdate(entities);
        int size = entities.size();
        int batchTime = size / batchSize;
        int remaining = size % batchSize;
        int updateCount = 0;

        for(int i = 0; i < batchTime; ++i) {
            updateCount += this.mapper.updateBatch(entities.subList(i * batchSize, (i + 1) * batchSize));
        }

        if (remaining > 0) {
            updateCount += this.mapper.updateBatch(entities.subList(size - remaining, size));
        }

        return updateCount;
    }

    public Long count() {
        return this.mapper.count();
    }

    public Object selectMax(String column) {
        return this.selectByFunction(Func.MAX, column);
    }

    public Object selectMin(String column) {
        return this.selectByFunction(Func.MIN, column);
    }

    public Object selectAvg(String column) {
        return this.selectByFunction(Func.AVG, column);
    }

    public Object selectSum(String column) {
        return this.selectByFunction(Func.SUM, column);
    }

    private Object selectByFunction(Func func, String column) {
        Map<String, Object> result = this.mapper.selectByFunction(func.name(), column);
        return !Objects.isNull(result) && !result.isEmpty() ? result.values().iterator().next() : null;
    }

    private void beforeInsert(E entity) {
        Class<? extends BaseEntity> entityClass = entity.getClass();
        /*if (Dateable.class.isAssignableFrom(entityClass) && Objects.isNull(((Dateable)entity).getExtCreatedDate())) {
            ((Dateable)entity).setExtCreatedDate(new Date());
        }*/
    }

    private void beforeUpdate(E entity) {
        Class<? extends BaseEntity> entityClass = entity.getClass();
        /*if (Dateable.class.isAssignableFrom(entityClass) && Objects.isNull(((Dateable)entity).getExtLastModifiedDate())) {
            ((Dateable)entity).setExtLastModifiedDate(new Date());
        }*/
    }

    private void beforeInsert(List<E> entities) {
        entities.forEach(this::beforeInsert);
    }

    private void beforeUpdate(List<E> entities) {
        entities.forEach(this::beforeUpdate);
    }

}
