package com.bumjinkim.GPA4U;

public class KimWeight {
    public String id;
    public String name;
    public Double percent;
    public String course;

    public KimWeight(String name, Double percent, String course) {
        this.id = KimRandomCourseID.randomString(22);
        this.name = name;
        this.percent = percent;
        this.course = course;
    }
}
