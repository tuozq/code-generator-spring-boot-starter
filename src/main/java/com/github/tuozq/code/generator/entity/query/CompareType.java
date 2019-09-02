package com.github.tuozq.code.generator.entity.query;

/**
 * @author tuozq
 * @description:
 * @date 2019/6/19.
 */
public enum  CompareType {

    EQUALS(" = "),
    GREATER(" > "),
    LESS(" < "),
    GREATER_EQUALS(" >= "),
    LESS_EQUALS(" <= "),
    NOTEQUALS(" != "),
    LIKE(" like "),
    NOTLIKE(" not like "),
    INCLUDE(" in "),
    NOTINCLUDE(" not in "),
    INNER(" inner "),
    NOTINNER(" not inner "),
    IS(" is "),
    ISNOT("is not "),
    EXISTS(" exists "),
    NOTEXISTS(" not exists "),
    ISNULL(" is null "),
    ISNOTNULL(" is not null "),;

    private String value;
    CompareType( String value){
        this.value = value;
    }


    public String getValue(){
        return value;
    }




}
