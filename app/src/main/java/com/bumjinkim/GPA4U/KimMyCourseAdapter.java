package com.bumjinkim.GPA4U;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;

public class KimMyCourseAdapter extends RecyclerView.Adapter<KimMyCourseAdapter.CourseViewHolder> {

    private Context context;
    private ArrayList<Object> courses = new ArrayList<>();
    private ArrayList<Object> assessments = new ArrayList<>();
    private ArrayList<Object> weights = new ArrayList<>();
    private int kim_add_course_request_code = 1;

    public KimMyCourseAdapter(Context context, ArrayList<Object> courses, ArrayList<Object> assessments, ArrayList<Object> weights, RecyclerView recyclerView) {
        this.context = context;
        this.courses = courses;
        this.weights = weights;
        this.assessments = assessments;
        this.recyclerView = recyclerView;
    }

    private RecyclerView recyclerView;

    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kim_my_course, viewGroup, false);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, KimMyAssessmentsActivity.class);
                myIntent.putExtra("course", ((KimCourse)courses.get(recyclerView.getChildAdapterPosition(v))).id);
                context.startActivity(myIntent);
            }
        });
        mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final CharSequence[] items = {"Edit", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Select");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent editIntent = new Intent(context, KimAddCourseActivity.class);
                                editIntent.putExtra("method", "edit");
                                editIntent.putExtra("course", ((KimCourse)courses.get(recyclerView.getChildAdapterPosition(v))).id);
                                ((FragmentActivity)context).startActivityForResult(editIntent, kim_add_course_request_code);
                                break;
                            case 1:
                                TinyDB tinyDB = new TinyDB(context);
                                ArrayList<Object> cs = tinyDB.getListObject("courses", KimCourse.class);
                                cs.remove(((KimCourse)cs.get(recyclerView.getChildAdapterPosition(v))));

                                tinyDB.putListObject("courses", cs);

                                courses = cs;

                                notifyDataSetChanged();

                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
        return new CourseViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder courseViewHolder, int i) {
        courseViewHolder.nameView.setText(((KimCourse)courses.get(i)).name);
        if (((KimCourse) courses.get(i)).su) {
            courseViewHolder.gradeView.setText(KimCalculateGrade.calculateSUGrade(KimCalculateGrade.calculateCourseGPA(courses.get(i), assessments, weights, false)));
        } else {
            courseViewHolder.gradeView.setText(KimCalculateGrade.calculateCourseLetterGrade(KimCalculateGrade.calculateCourseGPA(courses.get(i), assessments, weights, false)));
        }
        courseViewHolder.creditView.setText(String.valueOf(((KimCourse)courses.get(i)).credit));
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void updateAdapter(ArrayList<Object> courses, ArrayList<Object> weights, ArrayList<Object> assessments) {
        this.courses = courses;
        this.weights = weights;
        this.assessments = assessments;
        notifyDataSetChanged();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView gradeView;
        public TextView creditView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.kim_my_course_name_view);
            gradeView = (TextView) itemView.findViewById(R.id.kim_my_course_grade_view);
            creditView = (TextView) itemView.findViewById(R.id.kim_my_course_credit_view);
        }

    }

}
