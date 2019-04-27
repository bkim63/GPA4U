package com.bumjinkim.GPA4U;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KimAssessment {
    public String id;
    public String course;
    public String name;
    public boolean expected;
    public String weight;
    public Double grade;
    public Double assessmentWeight;

    public KimAssessment(String course, String name, boolean expected, String weight, Double grade, Double assessmentWeight) {
        this.course = course;
        this.name = name;
        this.expected = expected;
        this.weight = weight;
        this.grade = grade;
        this.assessmentWeight = assessmentWeight;
    }
}
