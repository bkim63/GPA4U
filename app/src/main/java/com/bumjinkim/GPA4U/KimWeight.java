package com.bumjinkim.GPA4U;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class KimWeight extends RealmObject {
    @PrimaryKey
    public Long id;
    public String name;
    public Double percent;

    public KimCourse course;
}
