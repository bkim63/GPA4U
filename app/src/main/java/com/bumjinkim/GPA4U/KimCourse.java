package com.bumjinkim.GPA4U;

public class KimCourse {
    public String id;
    public String name;
    public String grade;
    public int credit;

    public KimCourse(String name, String grade, int credit) {
        this.id = KimRandomCourseID.randomString(20);
        this.name = name;
        this.grade = grade;
        this.credit = credit;
    }
}