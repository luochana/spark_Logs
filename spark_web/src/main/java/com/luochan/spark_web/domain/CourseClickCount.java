package com.luochan.spark_web.domain;

public class CourseClickCount {
    private String name;
    private Long value;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getValue() {
        return value;
    }
    public void setValue(Long value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "CourseClickCount [name=" + name + ", value=" + value + "]";
    }

}

