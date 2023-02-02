package com.perfume.allpouse.model.enums;

public enum BulletinType {

    FREE("자유게시판"),
    PERFUMER("조향사게시판"),
    BANNER("배너게시판");

    private String boardType;

    BulletinType(String type) {
        this.boardType = type;}

    public String getValue() {
        return this.boardType;}
}
