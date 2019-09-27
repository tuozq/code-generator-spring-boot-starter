package com.github.tuozq.code.generator.generator.configuration;

import lombok.Data;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
@Data
public class TableConfig {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 实体类名
     */
    private String modelName;

    /**
     * service类名
     * 默认值 = modelName + Service 后缀
     */
    private String serviceName;

    /**
     * mapper.xml 对应的java mapper类名
     * 默认值 = modelName + Repository 后缀
     */
    private String repositoryName;

    /**
     * 自动生成的mapper.xml名
     * 默认值 = _前缀 + modelName + Repository 后缀
     */
    private String repositorySqlName;

    /**
     * 自定义的mapper.xml名
     * 默认值 = modelName + Repository 后缀
     */
    private String customRepositorySqlName;


}
