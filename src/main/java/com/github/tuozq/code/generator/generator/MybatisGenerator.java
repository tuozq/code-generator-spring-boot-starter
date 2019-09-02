package com.github.tuozq.code.generator.generator;


import com.alibaba.fastjson.JSON;
import com.github.tuozq.code.generator.database.Jdbc;
import com.github.tuozq.code.generator.database.JdbcConnectionBuilder;
import com.github.tuozq.code.generator.database.JdbcDriverEnum;
import com.github.tuozq.code.generator.database.helper.DatabaseMetaDataHelper;
import com.github.tuozq.code.generator.database.metadata.Column;
import com.github.tuozq.code.generator.database.metadata.PrimaryKey;
import com.github.tuozq.code.generator.database.metadata.Table;
import com.github.tuozq.code.generator.generator.configuration.*;
import com.github.tuozq.code.generator.generator.model.DataModel;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tuozq
 * @description: model、service、repository 代码生成工具
 * @date 2019/5/10.
 */
public class MybatisGenerator {

    private static final Logger log = LoggerFactory.getLogger(MybatisGenerator.class);

    /**
     * freemarker configuration
     */
    private freemarker.template.Configuration templateConfiguration;

    /**
     * mybatisGenerator configuration
     */
    private Configuration configuration;

    public MybatisGenerator(Configuration configuration){
        this.configuration = configuration;
    }

    /**
     *  构建Mybatis代码生成器对象
     * @param configuration 配置信息
     * @return
     */
    public static MybatisGenerator bulid(Configuration configuration){
        return new MybatisGenerator(configuration);
    }

    public MybatisGenerator templateConfiguration(freemarker.template.Configuration templateConfiguration){
        this.templateConfiguration = templateConfiguration;
        return this;
    }

    /**
     * 默认设置 dao、servier、xml 等文件信息
     * @param jdbc
     * @return
     */
    public MybatisGenerator defaultInit(Jdbc jdbc){
        // freemarker 模板配置
        templateConfiguration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_22);
        templateConfiguration.setClassForTemplateLoading(AutoGenerator.class, "/generator/template/");
        // 代码生成配置信息
        configuration.setJdbc(jdbc);
        // 数据model层
        GenerateJavaFile modelFile = new GenerateJavaFile();
        modelFile.setOutputDirectory(buildOutputDirectory("dao","/dao/model"));
        modelFile.setPackageName(buildPackageName(".dao.model"));
        modelFile.setTemplate("model.ftl");
        modelFile.setOverride(true);
        configuration.setModelFile(modelFile);
        // 数据mapper层
        GenerateJavaFile repositoryFile = new GenerateJavaFile();
        repositoryFile.setOutputDirectory(buildOutputDirectory("dao","/dao/mapper"));
        repositoryFile.setPackageName(buildPackageName(".dao.mapper"));
        repositoryFile.setTemplate("repository.ftl");
        repositoryFile.setSuffix("Mapper");
        repositoryFile.setPrefix("");
        repositoryFile.setOverride(false);
        configuration.setRepositoryFile(repositoryFile);
        // 业务层
        GenerateJavaFile serviceFile = new GenerateJavaFile();
        serviceFile.setOutputDirectory(buildOutputDirectory("service","/service"));
        serviceFile.setPackageName(buildPackageName(".service"));
        serviceFile.setTemplate("service.ftl");
        serviceFile.setSuffix("Service");
        serviceFile.setPrefix("");
        serviceFile.setOverride(false);
        configuration.setServiceFile(serviceFile);
        // 数据层sql xml
        GenerateXmlFile repositorySqlFile = new GenerateXmlFile();
        repositorySqlFile.setOutputDirectory(buildMapperXmlOutputDirectory("/dao/mapper/auto"));
        if(configuration.getJdbc().getDriver().equals(JdbcDriverEnum.ORACLE_DRIVER.value())){
            repositorySqlFile.setTemplate("repositorySqlForOracle.ftl");
        }else if(configuration.getJdbc().getDriver().equals(JdbcDriverEnum.MYSQL_DRIVER.value())){
            repositorySqlFile.setTemplate("repositorySqlForMysql.ftl");
        }
        repositorySqlFile.setSuffix("Mapper");
        repositorySqlFile.setPrefix("_");
        repositorySqlFile.setOverride(true);
        configuration.setRepositorySqlFile(repositorySqlFile);
        // 数据层sql xml 自定义修改
        GenerateXmlFile repositoryCustomSqlFile = new GenerateXmlFile();
        repositoryCustomSqlFile.setOutputDirectory(buildMapperXmlOutputDirectory("/dao/mapper"));
        repositoryCustomSqlFile.setTemplate("repositorySqlForCustom.ftl");
        repositoryCustomSqlFile.setSuffix("Mapper");
        repositoryCustomSqlFile.setPrefix("");
        repositoryCustomSqlFile.setOverride(false);
        configuration.setRepositoryCustomSqlFile(repositoryCustomSqlFile);
        return this;
    }

    private String buildPackageName(String suffixPackage){
        return "com." + configuration.getModuleName().replace("-", ".") + suffixPackage;
    }

    private File buildOutputDirectory(String layer, String suffixPackage){
        String moduleFolder = configuration.getBaseFolder().concat(File.separator).concat(configuration.getModuleName());
        return new File(moduleFolder.concat(File.separator).concat(configuration.getModuleName().concat("-").concat(layer)) + "/src/main/java/com/" + configuration.getModuleName().replace("-", "/") + suffixPackage);
    }

    private File buildMapperXmlOutputDirectory(String suffixPackage){
        String moduleFolder = configuration.getBaseFolder().concat(File.separator).concat(configuration.getModuleName());
        return new File(moduleFolder.concat(File.separator).concat(configuration.getModuleName().concat("-dao")) + "/src/main/resources/com/" + configuration.getModuleName().replace("-", "/") + suffixPackage);
    }

    public void generator(){
        configuration.getTableConfigs().forEach(tableConfig -> {
            this.doGenerator(tableConfig.getTableName().toUpperCase());
        });
    }

    private void doGenerator(String tableNamePattern){
        Connection connection = new JdbcConnectionBuilder(configuration.getJdbc()).getConnection();
        try {
            List<Table> dbTables = (List<Table>)DatabaseMetaDataHelper.getTablesExtractor().setSchemaPattern(configuration.getJdbc().getUser()).setTableNamePattern(tableNamePattern).extract(connection.getMetaData());
            log.info("Table元数据 -> {}", JSON.toJSONString(dbTables));
            Map<String, TableConfig> tableConfigMap = configuration.getTableConfigMap();
            List<Table> filterTables = (List)dbTables.stream().filter(dbTable -> {
                return tableConfigMap.containsKey(dbTable.getTableName());
            }).collect(Collectors.toList());
            log.info("Filter后Table元数据 -> {}", JSON.toJSONString(filterTables));
            List<DataModel> dataModels = filterTables.stream().map(dbTable -> {
                TableConfig tableConfig = tableConfigMap.get(dbTable.getTableName());
                DataModel dataModel = new DataModel();
                if(Objects.isNull(tableConfig.getModelName())){
                    String tableName = tableConfig.getTableName().toLowerCase();
                    if(tableName.contains("_")){
                        tableConfig.setModelName(camelCaseName(tableName));
                    }
                    tableConfig.setModelName(tableConfig.getTableName().substring(0, 1).toUpperCase() + tableConfig.getTableName().substring(1));
                }
                dataModel.setModelName(tableConfig.getModelName());
                dataModel.setTableName(tableConfig.getTableName());

                if(Objects.nonNull(configuration.getRepositoryFile())){
                    String repositoryName = tableConfig.getRepositoryName();
                    if(Objects.isNull(repositoryName)){
                        repositoryName = configuration.getRepositoryFile().getPrefix() + dataModel.getModelName() + configuration.getRepositoryFile().getSuffix();
                    }
                    dataModel.setRepositoryName(repositoryName);
                    dataModel.setRepositoryPackage(configuration.getRepositoryFile().getPackageName());
                }

                if(Objects.nonNull(configuration.getServiceFile())){
                    String serviceName = tableConfig.getServiceName();
                    if(Objects.isNull(serviceName)){
                        serviceName = configuration.getServiceFile().getPrefix() + dataModel.getModelName() + configuration.getServiceFile().getSuffix();
                    }
                    dataModel.setServiceName(serviceName);
                    dataModel.setServicePackage(configuration.getServiceFile().getPackageName());
                }

                if(Objects.nonNull(configuration.getRepositorySqlFile())){
                    String repositoryName = tableConfig.getRepositorySqlName();
                    if(Objects.isNull(repositoryName)){
                        repositoryName = configuration.getRepositorySqlFile().getPrefix() + dataModel.getModelName() + configuration.getRepositorySqlFile().getSuffix();
                    }
                    dataModel.setRepositorySqlName(repositoryName);
                }

                if(Objects.nonNull(configuration.getRepositoryCustomSqlFile())){
                    String customRepositoryName = tableConfig.getCustomRepositorySqlName();
                    if(Objects.isNull(customRepositoryName)){
                        customRepositoryName = configuration.getRepositoryCustomSqlFile().getPrefix() + dataModel.getModelName() + configuration.getRepositoryCustomSqlFile().getSuffix();
                    }
                    dataModel.setCustomRepositorySqlName(customRepositoryName);
                }

                dataModel.setColumns(dbTable.getColumns().stream().map(dbColumn -> {
                    DataModel.Column column = new DataModel.Column();
                    column.setName(dbColumn.getColumnName());
                    column.setProperty(camelCaseName(column.getName().toLowerCase()));
                    column.setRemarks("");
                    setJdbcTypeAndJavaType(column, dbColumn);
                    return column;
                }).collect(Collectors.toList()));

                List<PrimaryKey> dbPrimaryKeys = dbTable.getPrimaryKeys();
                if(Objects.nonNull(dbPrimaryKeys) && !dbPrimaryKeys.isEmpty()){
                    dataModel.getColumns().forEach(column -> {
                        if(column.getName().equals(dbPrimaryKeys.get(0).getColumnName())){
                            dataModel.setPrimaryKey(column);
                            return;
                        }
                    });
                }
                dataModel.setModelPackage(configuration.getModelFile().getPackageName());
                if(dataModel.getPrimaryKey() == null){
                    DataModel.Column pk = new DataModel.Column();
                    pk.setName("id");
                    pk.setProperty("id");
                    pk.setJdbcType("VARCHAR");
                    pk.setJavaType(String.class);
                    dataModel.setPrimaryKey(pk);
                }
                log.info("TABLE [ {} ]", dataModel.getTableName());
                log.info("主键 -> {}", dataModel.getPrimaryKey().getName());
                generatorAll(dataModel);
                return dataModel;
            }).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generatorAll(DataModel dataModel){
        generatorModel(dataModel);
        generatorRepository(dataModel);
        generatorService(dataModel);
        generatorRepositorySql(dataModel);
        generatorRepositoryCustomSql(dataModel);
    }

    public void generatorModel(DataModel dataModel){
        generatorFile(dataModel, this.configuration.getModelFile(), dataModel.getModelName());
    }

    public void generatorRepositorySql(DataModel dataModel){
        generatorFile(dataModel, this.configuration.getRepositorySqlFile(), dataModel.getRepositorySqlName());
    }

    public void generatorRepositoryCustomSql(DataModel dataModel){
        generatorFile(dataModel, this.configuration.getRepositoryCustomSqlFile(), dataModel.getCustomRepositorySqlName());
    }

    public void generatorRepository(DataModel dataModel){
        generatorFile(dataModel, this.configuration.getRepositoryFile(), dataModel.getRepositoryName());
    }

    public void generatorService(DataModel dataModel){
        generatorFile(dataModel, this.configuration.getServiceFile(), dataModel.getServiceName());
    }

    /**
     *
     * @param dataModel
     * @param generateFile
     * @param fileName
     */
    public void generatorFile(DataModel dataModel, GenerateFile generateFile, String fileName){
        String fullFileName = fileName + generateFile.getExtension();
        log.info("AutoGenerator File -> {} start ", fullFileName);
        File output = new File(generateFile.getOutputDirectory(), fullFileName);
        CodeFileGenerator.create(this.templateConfiguration).generator(dataModel, generateFile.getTemplate(), output, generateFile.isOverride());
    }



    /**
     * 转换为驼峰
     *
     * @param underscoreName
     * @return
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }


    public static void setJdbcTypeAndJavaType(DataModel.Column column, Column dbColumn) {
        String typeName = dbColumn.getTypeName();
        if (Objects.equals(typeName, "BIGINT")) {
            column.setJdbcType(JdbcType.BIGINT.name());
            column.setJavaType(Long.class);
        } else if (Arrays.asList("INT", "INTEGER").contains(typeName)) {
            column.setJdbcType(JdbcType.INTEGER.name());
            column.setJavaType(Integer.class);
        } else if (Arrays.asList("DOUBLE", "FLOAT", "DECIMAL").contains(typeName)) {
            column.setJdbcType(JdbcType.DECIMAL.name());
            column.setJavaType(BigDecimal.class);
        } else if (Objects.equals("BIT", typeName)) {
            column.setJdbcType(JdbcType.BIT.name());
            column.setJavaType(Boolean.class);
        } else if (Objects.equals("BLOB", typeName)) {
            column.setJdbcType(JdbcType.BLOB.name());
            column.setJavaType(byte[].class);
        } else if (Objects.equals(typeName, "NUMBER")) {
            column.setJdbcType(JdbcType.DECIMAL.name());
            Integer digits = dbColumn.getDecimalDigits();
            Integer size = dbColumn.getColumnSize();
            if (digits <= 0 && size <= 18) {
                if (size >= 10) {
                    column.setJavaType(Long.class);
                } else {
                    column.setJavaType(Integer.class);
                }
            } else {
                column.setJavaType(BigDecimal.class);
            }
        } else if (!Objects.equals("DATE", typeName) && !Objects.equals("DATETIME", typeName) && !typeName.startsWith("TIMESTAMP")) {
            column.setJdbcType(JdbcType.VARCHAR.name());
            column.setJavaType(String.class);
        } else {
            column.setJdbcType(JdbcType.TIMESTAMP.name());
            column.setJavaType(Date.class);
        }
    }



}
