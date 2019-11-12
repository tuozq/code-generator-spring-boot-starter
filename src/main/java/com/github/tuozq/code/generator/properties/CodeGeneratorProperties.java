package com.github.tuozq.code.generator.properties;

import com.github.tuozq.code.generator.database.Jdbc;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tuozq
 * @description:
 * @date 2019/9/2.
 */
@ConfigurationProperties(prefix = "spring.code.generator")
@Data
public class CodeGeneratorProperties {

    /**
     * 模板默认路径
     */
    private String templateDirectory;

    /**
     * 文件输出路径
     */
    private String outputDirectory;

    /**
     * 数据源
     */
    private Jdbc datasource;

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

}
