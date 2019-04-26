package com.bumjinkim.GPA4U;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class KimMyCourseAdapter extends RecyclerView.Adapter<KimMyCourseAdapter.CourseViewHolder> {

    private Context context;
    private String[] names;
    private String[] grades;
    private int[] credits;

    public KimMyCourseAdapter(Context context, String[] names, String[] grades, int[] credits) {
        this.context = context;
        this.names = names;
        this.grades = grades;
        this.credits = credits;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kim_my_course, viewGroup, false);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, KimMyAssessmentsActivity.class);
                context.startActivity(myIntent);
            }
        });
        return new CourseViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder courseViewHolder, int i) {
        courseViewHolder.nameView.setText(names[i]);
        courseViewHolder.gradeView.setText(grades[i]);
        courseViewHolder.creditView.setText(String.valueOf(credits[i]));
    }

    @Override
    public int getItemCount() {
        return names.length;
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
