package com.github.tuozq.code.generator;

import com.github.tuozq.code.generator.database.Jdbc;
import com.github.tuozq.code.generator.generator.AutoGenerator;
import com.github.tuozq.code.generator.generator.configuration.Configuration;
import com.github.tuozq.code.generator.properties.CodeGeneratorProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tuozq
 * @description: 试运行代码生成器
 * @date 2019/9/4.
 */
public class TestGenerator {


    public static void main(String args[]){
        // 需要生成代码的table, key = 数据库表名 -> value = java类名
        Map<String, String> map = new HashMap<>();
        map.put("CT_ENQ_InquiryBill", "InquiryBill");
        map.put("CT_ENQ_InquiryBillEntry", "InquiryBillEntry");
        // 配置信息
        CodeGeneratorProperties properties = new CodeGeneratorProperties();
        // 代码输出路径
        properties.setOutputDirectory("G:\\test\\project\\project-oms");
        // 数据库联系信息
        properties.setDatasource(new Jdbc("user", "****", "jdbc:oracle:thin:@******:1521/orcl", "oracle.jdbc.driver.OracleDriver"));
        AutoGenerator autoGenerator = new AutoGenerator(properties);
        Configuration configuration = new Configuration();
        // 设置为不拆分目录生成
        //configuration.setSplitCatalogue(false);
        // 设置自带基类的扩展
        //configuration.setModelExtendType(BaseModelExt.class);
        // 生成代码， moduleName 模块名称
        autoGenerator.generator(configuration,"oms", autoGenerator.mapToTables(map));
    }

}
