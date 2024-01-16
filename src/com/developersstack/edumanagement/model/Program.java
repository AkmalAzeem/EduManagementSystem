package com.developersstack.edumanagement.model;

import java.util.Arrays;

public class Program {
    private String code;
    private String name;
    private String[] technologies;
    private String teacher;
    private double cost;

    public Program() {
    }

    public Program(String code, String name, String[] technologies, String teacher, double cost) {
        this.code = code;
        this.name = name;
        this.technologies = technologies;
        this.teacher = teacher;
        this.cost = cost;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String[] technologies) {
        this.technologies = technologies;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Program{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", technologies=" + Arrays.toString(technologies) +
                ", teacher='" + teacher + '\'' +
                ", cost=" + cost +
                '}';
    }
}
