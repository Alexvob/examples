package com.astudio.progressmonitor.scheme;

public class SchemeElement {

    int id;
    int parentId;
    String name;
    String post;

    public SchemeElement(int id, int parentId, String name, String post) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.post = post;
    }

    @Override
    public String toString() {
        return "SchemeElement{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", post='" + post + '\'' +
                '}';
    }

}
