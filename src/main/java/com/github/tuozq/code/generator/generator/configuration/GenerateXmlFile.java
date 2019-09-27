package com.github.tuozq.code.generator.generator.configuration;

import lombok.Data;

/**
 * @author tuozq
 * @description: xml配置
 * @date 2019/5/9.
 */
@Data
public class GenerateXmlFile extends GenerateFile {

    @Override
    public String getExtension() {
        return ".xml";
    }
}
