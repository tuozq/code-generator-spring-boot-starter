package com.github.tuozq.code.generator.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author tuozq
 * @description: 将构建好的数据生成代码文件
 * @date 2019/5/9.
 */
public class CodeFileGenerator {

    private Configuration templateConfiguration;

    public static CodeFileGenerator create(Configuration configuration){
        return new CodeFileGenerator(configuration);
    }

    public CodeFileGenerator(Configuration configuration){
        this.templateConfiguration = configuration;
    }

    /**
     *
     * @param dataModel
     * @param template
     * @param output
     * @param isCover 是否覆盖
     */
    public void generator(Object dataModel, String template, File output, boolean isCover){
        Template ftl = this.getTemplate(template);
        if(!isCover && output.exists()){
            return;
        }
        if(output.exists()){
            output.delete();
        }else{
            if(!output.getParentFile().exists()){
                output.getParentFile().mkdirs();
            }
        }
        this.process(ftl, dataModel, output);

    }

    private void process(Template ftl, Object dataModel, File output){
        try {
            FileWriter out = new FileWriter(output);
            ftl.process(dataModel, out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private Template getTemplate(String name) {
        try {
            return this.templateConfiguration.getTemplate(name);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

}
