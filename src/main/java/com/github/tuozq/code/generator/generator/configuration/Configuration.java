package com.github.tuozq.code.generator.generator.configuration;



import com.github.tuozq.code.generator.database.Jdbc;
import com.github.tuozq.code.generator.entity.basic.model.mybatis.BaseEntity;
import com.github.tuozq.code.generator.entity.basic.model.mybatis.BaseRepository;
import com.github.tuozq.code.generator.entity.basic.model.mybatis.BaseService;
import lombok.Data;

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

    /**
     * 默认的代码，模板存放路径
     */
    public final String DEFATULT_TEMPLATE_DIRECTORY = "/generator/template/";

    private Jdbc jdbc;
    private String templateDirectory = DEFATULT_TEMPLATE_DIRECTORY;

    /**
     * 模块名
     * 如定义 oms,则代码的包名为 com.oms.dao，com.oms.service
     * 如定义 cloud-oms,则代码的包名为 com.cloud.oms.dao，com.cloud.oms.service
     */
    private String moduleName;
    private String outputDirectory;

    private List<TableConfig> tableConfigs;

    private GenerateJavaFile modelFile;
    private GenerateJavaFile repositoryFile;
    private GenerateJavaFile serviceFile;
    private GenerateXmlFile repositorySqlFile;
    private GenerateXmlFile repositoryCustomSqlFile;

    /**
     * 是否按目录拆分生成代码
     * 1.按目录拆分则会在文件输出路径生成
     *  ${outputDirectory}\${moduleName}-dao\src\main\java\com\${moduleName}\dao  --->存放model、mapper文件
     *  ${outputDirectory}\${moduleName}-service\src\main\java\com\${moduleName}\service --->存放service文件
     * 2.不拆分
     *  ${outputDirectory}\${moduleName}-${defaultCatalogue}\src\main\java\com\${moduleName}  --->存放model、mapper、service文件
     */
    private boolean splitCatalogue = true;

    /**
     * 如果 splitCatalogue = false, 则defaultCatalogue生效，dao层与service层生成到相同目录
     */
    private String defaultCatalogue = "code-auto";

    /**
     * model父类, 可继承BaseEntity扩展
     */
    private Class<? extends BaseEntity> modelExtendType = BaseEntity.class;

    /**
     * mapper父类, 可继承BaseRepository扩展
     */
    private Class<? extends BaseRepository> repositoryExtendType = BaseRepository.class;

    /**
     * service父类, 可继承BaseService扩展
     */
    private Class<? extends BaseService> serviceExtendType = BaseService.class;


    public Map<String, TableConfig> getTableConfigMap(){
        Map<String, TableConfig> map = new HashMap<>();
        if(!Objects.isNull(this.tableConfigs) && !this.tableConfigs.isEmpty()){
            this.tableConfigs.forEach(tableConfig -> map.put(tableConfig.getTableName().toUpperCase(), tableConfig));
        }
        return map;
    }
}
