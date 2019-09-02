package com.github.tuozq.code.generator.generator.configuration;



import com.github.tuozq.code.generator.database.Jdbc;
import lombok.Data;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
@Data
public class Configuration {

    private Jdbc jdbc;
    private File templateDirectory;

    private String moduleName;
    private String outputDirectory;

    private List<TableConfig> tableConfigs;

    private GenerateJavaFile modelFile;
    private GenerateJavaFile repositoryFile;
    private GenerateJavaFile serviceFile;
    private GenerateXmlFile repositorySqlFile;
    private GenerateXmlFile repositoryCustomSqlFile;

    public Map<String, TableConfig> getTableConfigMap(){
        Map<String, TableConfig> map = new HashMap<>();
        if(!Objects.isNull(this.tableConfigs) && !this.tableConfigs.isEmpty()){
            this.tableConfigs.forEach(tableConfig -> map.put(tableConfig.getTableName().toUpperCase(), tableConfig));
        }
        return map;
    }
}
