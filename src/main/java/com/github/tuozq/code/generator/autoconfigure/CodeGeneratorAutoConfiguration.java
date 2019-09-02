package com.github.tuozq.code.generator.autoconfigure;

import com.github.tuozq.code.generator.generator.AutoGenerator;
import com.github.tuozq.code.generator.properties.CodeGeneratorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tuozq
 * @description: 自动配置注入
 * @date 2019/9/2.
 */
@Configuration
@EnableConfigurationProperties(CodeGeneratorProperties.class)
public class CodeGeneratorAutoConfiguration {

    @Autowired
    private CodeGeneratorProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public AutoGenerator generator(){
        return new AutoGenerator(this.properties);
    }

}
