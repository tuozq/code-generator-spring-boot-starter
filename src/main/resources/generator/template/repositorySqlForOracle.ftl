<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${repositoryPackage}.${repositoryName}">
    <resultMap id="BaseResultMap" type="${modelPackage}.${modelName}">
        <id column="${primaryKey.name}" jdbcType="${primaryKey.jdbcType}" property="${primaryKey.property}"/>
        <#list columnsWithoutPrimaryKey as column>
        <result column="${column.name}" jdbcType="${column.jdbcType}" property="${column.property}"/>
        </#list>
    </resultMap>

    <sql id="Filter_Where_Clause">
        <where>
            <trim prefix="(" prefixOverrides="and" suffix=")">
                <foreach collection="filter.filterItems" item="filterItem">
                    <choose>
                        <when test="filterItem.noValue">
                            and ${'$\{'}filterItem.propertyName} ${'$\{'}filterItem.compareType.value}
                        </when>
                        <when test="filterItem.singleValue">
                            and ${'$\{'}filterItem.propertyName} ${'$\{'}filterItem.compareType.value} ${r'#{filterItem.compareValue}'}
                        </when>
                        <when test="filterItem.listValue">
                            and ${'$\{'}filterItem.propertyName} ${'$\{'}filterItem.compareType.value}
                            <foreach close=")" collection="filterItem.compareValue" item="listItem" open="(" separator=",">
                                ${r'#{listItem}'}
                            </foreach>
                        </when>
                    </choose>
                </foreach>
            </trim>
        </where>
    </sql>


    <sql id="Base_Column_List">
        ${columnNames}
    </sql>

    <insert id="insert">
        insert into ${tableName} (${columnNames})
        values (<#list columns as column>${'#\{'}${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if></#list>)
    </insert>

    <insert id="insertSelective">
        insert into ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#list columns as column>
            <if test="${column.property} != null">
                ${column.name}<#if column_has_next>,</#if>
            </if>
            </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <#list columns as column>
            <if test="${column.property} != null">
                ${'#\{'}${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if>
            </if>
            </#list>
        </trim>
    </insert>

    <delete id="deleteByPrimaryKey">
        delete from ${tableName}
        where ${primaryKey.name} = ${'#\{'}${primaryKey.property}}
    </delete>

    <delete id="deleteByPrimaryKeys">
        delete from ${tableName} where ${primaryKey.name} in
        <foreach collection="list" item="item" open="(" close=")" separator=",">
        ${'#\{'}item}
        </foreach>
    </delete>

    <delete id="deleteByFilter">
        delete from ${tableName}
        <include refid="Filter_Where_Clause" />
    </delete>

    <update id="updateByPrimaryKey">
        update ${tableName}
        set <#list columnsWithoutPrimaryKey as column>${column.name}=${'#\{'}${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if></#list>
        where ${primaryKey.name} = ${'#\{'}${primaryKey.property},jdbcType=${primaryKey.jdbcType}}
    </update>

    <update id="updateByPrimaryKeySelective">
        update ${tableName}
        <set>
        <#list columnsWithoutPrimaryKey as column>
            <if test="${column.property} != null">
                ${column.name}=${'#\{'}${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if>
            </if>
        </#list>
        </set>
        where ${primaryKey.name} = ${'#\{'}${primaryKey.property},jdbcType=${primaryKey.jdbcType}}
    </update>

    <delete id="updateByFilterSelective">
        update ${tableName}
        <set>
        <#list columnsWithoutPrimaryKey as column>
            <if test="model.${column.property} != null">
                ${column.name}=${'#\{'}model.${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if>
            </if>
        </#list>
        </set>
        <include refid="Filter_Where_Clause" />
    </delete>

    <select id="selectByPrimaryKey" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        where ${primaryKey.name} = ${'#\{'}${primaryKey.property},jdbcType=${primaryKey.jdbcType}}
    </select>

    <select id="selectByPrimaryKeys" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName} where ${primaryKey.name} in
        <foreach collection="list" item="item" open="(" close=")" separator=",">
        ${'#\{'}item}
        </foreach>
    </select>

    <select id="selectAll" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
    </select>

    <select id="selectByEntity" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        <where>
        <#list columns as column>
            <if test="${column.property} != null">
                and ${column.name}=${'#\{'}${column.property},jdbcType=${column.jdbcType}}
            </if>
        </#list>
        </where>
    </select>

    <select id="selectByFilter" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        <include refid="Filter_Where_Clause" />
        <if test="filter.ordreByClause != null">
            order by ${'$\{'}filter.ordreByClause}
        </if>
    </select>



    <insert id="insertBatch">
        <foreach collection="list" item="item" index="index" open="begin" close=";end;" separator=";">
            insert into ${tableName}
            <trim prefix="(" suffix=")" suffixOverrides=",">
                <#list columns as column>
                    <if test="item.${column.property} != null">
                        ${column.name}<#if column_has_next>,</#if>
                    </if>
                </#list>
            </trim>

            <trim prefix="values (" suffix=")" suffixOverrides=",">
                <#list columns as column>
                    <if test="item.${column.property} != null">
                        ${'#\{'}item.${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if>
                    </if>
                </#list>
            </trim>
        </foreach>
    </insert>

    <update id="updateBatch">
        <foreach collection="list" item="item" index="index" open="begin" close=";end;" separator=";">
            update ${tableName}
            <set>
            <#list columnsWithoutPrimaryKey as column>
            <if test="item.${column.property} != null">
                ${column.name} = ${'#\{'}item.${column.property},jdbcType=${column.jdbcType}}<#if column_has_next>,</#if>
            </if>
            </#list>
            </set>
            where ${primaryKey.name} = ${'#\{'}item.${primaryKey.property},jdbcType=${primaryKey.jdbcType}}
        </foreach>
    </update>

    <select id="count" resultType="java.lang.Long">
        select count(*) from ${tableName}
    </select>

    <select id="selectByFunction" resultType="java.lang.Long">
        select ${r'${'}function}(${r'${'}column}) from ${tableName}
    </select>


</mapper>

