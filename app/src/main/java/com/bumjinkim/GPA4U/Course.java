package com.bumjinkim.GPA4U;

public class Course {
    public String id;
    public String name;
    public String grade;
    public int credit;

    public Course(String name, String grade, int credit) {
        this.id = RandomString.randomString(20);
        this.name = name;
        this.grade = grade;
        this.credit = credit;
    }
}
