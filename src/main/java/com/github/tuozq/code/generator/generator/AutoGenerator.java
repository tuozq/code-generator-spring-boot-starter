package com.github.tuozq.code.generator.generator;

import com.alibaba.fastjson.JSON;
import com.github.tuozq.code.generator.generator.configuration.Configuration;
import com.github.tuozq.code.generator.generator.configuration.TableConfig;
import com.github.tuozq.code.generator.properties.CodeGeneratorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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

    private static final Logger log = LoggerFactory.getLogger(AutoGenerator.class);

    private CodeGeneratorProperties properties;

    public AutoGenerator(CodeGeneratorProperties properties){
        this.properties = properties;
    }

    /**
     * 自动生成代码
     * @param moduleParentFolder 父模块路径
     * @param moduleName 模块名称
     * @param tables table配置信息
     */
    public void generator(String moduleParentFolder, String moduleName,  List<TableConfig> tables){
        if(tables == null || tables.isEmpty()){
            log.info("Table元数据为空，未生成代码");
        }
        Configuration configuration = new Configuration();
        String baseFolder;
        if(null == moduleParentFolder || moduleParentFolder.length() == 0){
            baseFolder = properties.getProjectFolder();
        }else{
            baseFolder = properties.getProjectFolder() + File.separator + moduleParentFolder;
        }
        // 针对每个table配置，可定义model、service、repository
        configuration.setTableConfigs(tables);
        configuration.setBaseFolder(baseFolder);
        configuration.setModuleName(moduleName);
        MybatisGenerator.bulid(configuration).defaultInit(properties.getDatasource()).generator();
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
