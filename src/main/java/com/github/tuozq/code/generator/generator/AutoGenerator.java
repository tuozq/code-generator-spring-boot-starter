package com.github.tuozq.code.generator.generator;

import com.github.tuozq.code.generator.generator.configuration.Configuration;
import com.github.tuozq.code.generator.generator.configuration.TableConfig;
import com.github.tuozq.code.generator.properties.CodeGeneratorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author tuozq
 * @description: 代码自动生成器
 * @date 2019/5/9.
 */

public class AutoGenerator {

    private CodeGeneratorProperties properties;

    public AutoGenerator(CodeGeneratorProperties properties){
        this.properties = properties;
    }

    /**
     * 自动生成代码
     * @param moduleName 模块名称
     * @param tables table配置信息
     */
    public void generator(Configuration configuration, String moduleName,  List<TableConfig> tables){
        if(moduleName == null || moduleName.isEmpty()){
            throw new IllegalArgumentException("moduleName 不能为空。");
        }
        if(tables == null || tables.isEmpty()){
            throw new IllegalArgumentException("tables 不能为空。");
        }
        // 针对每个table配置，可定义model、service、repository
        configuration.setTableConfigs(tables);
        configuration.setOutputDirectory(properties.getOutputDirectory());
        configuration.setModuleName(moduleName);
        configuration.setSplitCatalogue(properties.isSplitCatalogue());
        configuration.setDefaultCatalogue(properties.getDefaultCatalogue());
        configuration.setJdbc(properties.getDatasource());
        MybatisGenerator.bulid(configuration).defaultInit().generator();
    }

    /**
     * 自动生成代码
     * @param moduleName 模块名称
     * @param tables table配置信息
     */
    public void generator(String moduleName,  List<TableConfig> tables){
        Configuration configuration = new Configuration();
        generator(configuration, moduleName, tables);
    }



    public List<TableConfig> mapToTables(Map<String, String> map){
        List<TableConfig> tables = new ArrayList<>();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String tableName = iterator.next();
            TableConfig tableConfig = new TableConfig();
            tableConfig.setModelName(map.get(tableName));
            tableConfig.setTableName(tableName);
            tables.add(tableConfig);
        }
        return tables;
    }






}
