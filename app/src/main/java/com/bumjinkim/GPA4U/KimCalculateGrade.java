package com.bumjinkim.GPA4U;

import android.util.Log;

import java.util.ArrayList;

public class KimCalculateGrade {
    public static double calculateOverallGPA(ArrayList<Object> assessments, ArrayList<Object> weights, ArrayList<Object> courses) {
        double grade = 0.0;
        for (Object course : courses) {
            grade += calculateCourseGPA(course, assessments, weights, false);
        }

        return calculateGPAFromGrade(grade);
    }

    public static double calculateOverallExpectedGPA(ArrayList<Object> assessments, ArrayList<Object> weights, ArrayList<Object> courses) {
        double grade = 0.0;
        for (Object course : courses) {
            grade += calculateCourseGPA(course, assessments, weights, true);
        }
        return calculateGPAFromGrade(grade);
    }

    public static double calculateCourseGPA(Object course, ArrayList<Object> assessments, ArrayList<Object> weights, boolean expected) {
        double grade = 0.0;
        double weightTotal = 0.0;

        ArrayList<KimWeight> missingWeights = new ArrayList<>();

        boolean missingAssessment = false;

        for (int i = 0; i < weights.size(); i++) {
            KimWeight weight = (KimWeight) weights.get(i);

            Log.d("GRADE CALCULATION", weight.course);
            Log.d("GRADE CALCULATION", ((KimCourse) course).id);

            if (((KimCourse) course).id.equals(weight.course)) {

                int assessmentCount = 0;
                double percent = 0.0;

                for (Object a : assessments) {
                    KimAssessment assessment = (KimAssessment) a;
                    Log.d("GRADE CALCULATION", ((KimCourse) course).id);
                    Log.d("GRADE CALCULATION", assessment.course);
                    Log.d("GRADE CALCULATION", String.valueOf(((KimCourse) course).id.equals(assessment.course)));

                    if (((KimCourse) course).id.equals(assessment.course)) {

                        Log.d("GRADE CALCULATION", weight.id);
                        Log.d("GRADE CALCULATION", assessment.weight);
                        Log.d("GRADE CALCULATION", String.valueOf(weight.id.equals(assessment.weight)));

                        if (weight.id.equals(assessment.weight)) {
                            if (!expected) {
                                if (!assessment.expected) {
                                    percent += assessment.grade;
                                    assessmentCount++;
                                }
                            } else {
                                percent += assessment.grade;
                                assessmentCount++;
                            }
                            weightTotal += weight.percent / 100;
                        }
                    }
                }

                if (weightTotal < 100) {
                    missingWeights.add(weight);
                    missingAssessment = true;
                }

                Log.d("GRADE CALCULATION", String.valueOf(percent));
                Log.d("GRADE CALCULATION", String.valueOf(assessmentCount));
                Log.d("GRADE CALCULATION", String.valueOf(weight.percent));

                grade += (percent / (assessmentCount * 100)) * 100 * weight.percent / 100;
                Log.d("MISSING ASSESSMENTS", String.valueOf(grade));
            }
        }

        if (missingAssessment) {
            double percent = 0.0;

            Log.d("MISSING ASSESSMENTS", String.valueOf(percent));
            Log.d("MISSING ASSESSMENTS", String.valueOf(grade));

            grade = grade / (weightTotal);

            Log.d("MISSING ASSESSMENTS", String.valueOf(grade));

        }

        Log.d("GRADE CALCULATE", String.valueOf(grade));

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
