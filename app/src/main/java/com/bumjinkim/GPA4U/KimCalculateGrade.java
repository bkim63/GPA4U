package com.bumjinkim.GPA4U;

import android.util.Log;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class KimCalculateGrade {
    public static double calculateOverallGPA(ArrayList<KimCourse> courses) {
        double grade = 0.0;
        double totalCredit = 0.0;
        for (KimCourse course : courses) {
            if (!course.su) {
                totalCredit += ((KimCourse) course).credit;
            }
        }

        Realm realm = Realm.getDefaultInstance();
        for (KimCourse course : courses) {
            if (!course.su) {
                RealmResults<KimWeight> weights = realm.where(KimWeight.class).equalTo("course.id", course.id).findAll();
                RealmResults<KimAssessment> assessments = realm.where(KimAssessment.class).equalTo("course.id", course.id).findAll();

                grade += calculateGPAFromGrade(calculateCourseGPA(course, new ArrayList<KimWeight>(weights), new ArrayList<KimAssessment>(assessments), false)) * ((KimCourse) course).credit / totalCredit;
                Log.d("GPA", String.valueOf(grade));
            }
        }

        return grade;
    }

    public static String calculateSUGrade(double grade) {
        String su = "U";
        if (grade >= 69.5) {
            su = "S";
        }
        return su;
    }

    public static double calculateOverallExpectedGPA(ArrayList<KimCourse> courses) {
        double grade = 0.0;
        double totalCredit = 0.0;
        for (KimCourse course : courses) {
            if (!course.su) {
                totalCredit += ((KimCourse) course).credit;
            }
        }

        Realm realm = Realm.getDefaultInstance();
        for (KimCourse course : courses) {
            if (!course.su) {
                RealmResults<KimWeight> weights = realm.where(KimWeight.class).equalTo("course.id", course.id).findAll();
                RealmResults<KimAssessment> assessments = realm.where(KimAssessment.class).equalTo("course.id", course.id).findAll();

                grade += calculateGPAFromGrade(calculateCourseGPA(course, new ArrayList<KimWeight>(weights), new ArrayList<KimAssessment>(assessments), true)) * ((KimCourse) course).credit / totalCredit;
                Log.d("GPA", String.valueOf(grade));
            }
        }

        return grade;
    }

    public static double calculateCourseGPA(KimCourse course, ArrayList<KimWeight> weights, ArrayList<KimAssessment> assessments, boolean expected) {
        double grade = 0.0;

        for (int i = 0; i < weights.size(); i++) {
            KimWeight weight = (KimWeight) weights.get(i);

            int assessmentCount = 0;
            double percent = 0.0;

            for (Object a : assessments) {
                KimAssessment assessment = (KimAssessment) a;


                if (!expected) {
                    if (!assessment.expected) {
                        percent += assessment.grade;
                        assessmentCount++;
                    }
                } else {
                    percent += assessment.grade;
                    assessmentCount++;
                }
            }

            grade += (percent / (assessmentCount * 100)) * 100 * weight.percent / 100;
        }

        double outOf = 0.0;
        for (KimWeight weight : weights) {
            outOf += weight.percent;
        }

        Log.d("GRADE CALCULATION", String.valueOf(outOf));

        grade = grade / (outOf / 100);

        return grade;
    }

    public static Double calculateGPAFromGrade(double grade) {
        double gpa;

        if (grade >= 97.5) {
            gpa = 4.0;
        } else if (grade <= 97.5 && grade >= 92.5) {
            gpa = 4.0;
        } else if (grade <= 92.5 && grade >= 89.5) {
            gpa = 3.7;
        } else if (grade <= 89.5 && grade >= 87.5) {
            gpa = 3.3;
        } else if (grade <= 87.5 && grade >= 82.5) {
            gpa = 3.0;
        } else if (grade <= 82.5 && grade >= 79.5) {
            gpa = 2.7;
        } else if (grade <= 79.5 && grade >= 77.5) {
            gpa = 2.3;
        } else if (grade <= 77.5 && grade >= 72.5) {
            gpa = 2.0;
        } else if (grade <= 72.5 && grade >= 69.5) {
            gpa = 1.7;
        } else if (grade <= 69.5 && grade >= 59.5) {
            gpa = 1.3;
        } else if (grade <= 59.5 && grade >= 0) {
            gpa = 1.0;
        } else {
            gpa = 0.0;
        }

        return gpa;
    }

    public static String calculateCourseLetterGrade(double grade) {
        String letterGrade;

        if (grade >= 97.5) {
            letterGrade = "A+";
        } else if (grade <= 97.5 && grade >= 92.5) {
            letterGrade = "A";
        } else if (grade <= 92.5 && grade >= 89.5) {
            letterGrade = "A-";
        } else if (grade <= 89.5 && grade >= 87.5) {
            letterGrade = "B+";
        } else if (grade <= 87.5 && grade >= 82.5) {
            letterGrade = "B";
        } else if (grade <= 82.5 && grade >= 79.5) {
            letterGrade = "B-";
        } else if (grade <= 79.5 && grade >= 77.5) {
            letterGrade = "C+";
        } else if (grade <= 77.5 && grade >= 72.5) {
            letterGrade = "C";
        } else if (grade <= 72.5 && grade >= 69.5) {
            letterGrade = "C-";
        } else if (grade <= 69.5 && grade >= 59.5) {
            letterGrade = "D";
        } else if (grade <= 59.5 && grade >= 0) {
            letterGrade = "F";
        } else {
            letterGrade = "No Grade";
        }

        return letterGrade;
    }

}
