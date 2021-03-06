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
                totalCredit += course.credit;
            }
        }

        for (KimCourse course : courses) {
            if (!course.su) {
                grade += calculateGPAFromGrade(calculateCourseGPA(course, false)) * course.credit / totalCredit;
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
                totalCredit += course.credit;
            }
        }

        for (KimCourse course : courses) {
            if (!course.su) {
                grade += calculateGPAFromGrade(calculateCourseGPA(course, true)) * course.credit / totalCredit;
                Log.d("GPA", String.valueOf(grade));
            }
        }

        return grade;
    }

    public static double calculateCourseGPA(KimCourse course, boolean expected) {
        double grade = 0.0;

        Realm realm = Realm.getDefaultInstance();

        RealmResults<KimWeight> weights = realm.where(KimWeight.class).equalTo("course.id", course.id).findAll();
        RealmResults<KimAssessment> assessments = realm.where(KimAssessment.class).equalTo("course.id", course.id).findAll();

        for (int i = 0; i < weights.size(); i++) {
            double tempGrade = 0.0;

            KimWeight weight = weights.get(i);

            int assessmentWeightTotal = 0;

            for (KimAssessment a : assessments) {
                if (!expected) {
                    if (!a.expected) {
                        assessmentWeightTotal += a.assessmentWeight;
                    }
                } else {
                    assessmentWeightTotal += a.assessmentWeight;
                }
            }

            for (KimAssessment a : assessments) {
                if (!expected) {
                    if (!a.expected) {
                        tempGrade += a.grade * a.assessmentWeight / assessmentWeightTotal;
                    }
                } else {
                    tempGrade += a.grade * a.assessmentWeight / assessmentWeightTotal;
                }
            }

            Log.d("GRADE CALCULATION", String.valueOf(tempGrade));
            grade += tempGrade * weight.percent / 100;
        }

        double outOf = 0.0;

        for (KimWeight weight : weights) {
            outOf += weight.percent;
        }

        Log.d("GRADE CALCULATION", String.valueOf(grade));

        grade = grade / outOf * 100;

        Log.d("GRADE CALCULATION", String.valueOf(grade));

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
            letterGrade = "F";
        }

        return letterGrade;
    }

}
