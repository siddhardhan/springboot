package com.springboot.elasticsearch.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Department {

    private String name;
    private String type;

    public Department(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Department() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
