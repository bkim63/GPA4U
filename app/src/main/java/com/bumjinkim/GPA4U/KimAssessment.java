package com.bumjinkim.GPA4U;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class KimAssessment extends RealmObject {

    @PrimaryKey
    public Long id;

    public KimCourse course;
    public String name;
    public boolean expected;

    public KimWeight weight;

    public Double grade;
    public Double assessmentWeight;
}
