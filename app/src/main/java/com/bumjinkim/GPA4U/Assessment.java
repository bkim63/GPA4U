package com.bumjinkim.GPA4U;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Assessment {
    public String course;
    public String name;
    public boolean expected;
    public Double weight;
    public Double grade;

    public Assessment(String course, String name, boolean expected, Double weight, Double grade) {
        this.course = course;
        this.name = name;
        this.expected = expected;
        this.weight = weight;
        this.grade = grade;
    }
}
