package com.cy.pj.entity;

/**
 * 主从数据源标志
 */
public enum DataSourceType {
    MASTER(1,"master"),
    SALVE(2,"salve");

    int type;
    String name;

    DataSourceType(Integer type, String name){
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
