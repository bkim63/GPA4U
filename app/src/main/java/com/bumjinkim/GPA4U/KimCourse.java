package com.bumjinkim.GPA4U;

public class KimCourse {
    public String id;
    public String name;
    public String grade;
    public int credit;
    public boolean su;

    public KimCourse(String name, String grade, int credit, boolean su) {
        this.id = KimRandomCourseID.randomString(20);
        this.name = name;
        this.grade = grade;
        this.credit = credit;
        this.su = su;
    }
}
