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

    private String outputDirectory;

    private Jdbc datasource;

}
