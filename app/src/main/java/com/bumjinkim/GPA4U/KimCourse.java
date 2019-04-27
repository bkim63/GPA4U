package com.bumjinkim.GPA4U;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class KimCourse extends RealmObject {
    @PrimaryKey
    public Long id;
    public String name;
    public String grade;

    public int credit;
    public boolean su;
}
