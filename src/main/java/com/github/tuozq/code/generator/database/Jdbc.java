package com.github.tuozq.code.generator.database;

import lombok.Data;

/**
 * @author tuozq
 * @description: 数据库连接属性
 * @date 2019/5/9.
 */
@Data
public class Jdbc {

    private String user;

    private String password;

    private String url;

    private String driver;

    Jdbc(String user, String password, String url, String driver){
        this.user = user;
        this.password = password;
        this.url = url;
        this.driver = driver;
    }


}
