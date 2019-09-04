package com.github.tuozq.code.generator.generator.configuration;

import lombok.Data;

import java.io.File;

/**
 * @author tuozq
 * @description:
 * @date 2019/5/9.
 */
@Data
public abstract class GenerateFile {

    /**
     * 输出文件
     */
    private File outputDirectory;
    /**
     * 模板
     */
    private String template;
    /**
     * 是否覆盖
     */
    private boolean override;
    /**
     * 前缀
     */
    private String prefix;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 文件扩展名
     */
    private String extension;

}
