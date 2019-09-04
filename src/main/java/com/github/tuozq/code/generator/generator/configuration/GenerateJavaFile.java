package com.github.tuozq.code.generator.generator.configuration;

import lombok.Data;

/**
 * @author tuozq
 * @description: Java文件配置
 * @date 2019/5/9.
 */
@Data
public class GenerateJavaFile extends GenerateFile {

    @Override
    public String getExtension() {
        return ".java";
    }

    /**
     * 包名
     */
    private String packageName;



}
