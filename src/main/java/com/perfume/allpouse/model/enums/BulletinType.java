package com.perfume.allpouse.model.enums;

public enum BulletinType {

    FREE("자유게시판"),
    PERFUMER("조향사게시판"),
    BANNER("배너게시판");


    private String type;

    BulletinType(String type) {this.type = type;}

    public String getValue() {return this.type;}
}
