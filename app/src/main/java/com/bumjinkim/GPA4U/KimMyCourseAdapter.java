package com.bumjinkim.GPA4U;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class KimMyCourseAdapter extends RecyclerView.Adapter<KimMyCourseAdapter.CourseViewHolder> {

    private Context context;
    private ArrayList<Object> courses = new ArrayList<>();

    public KimMyCourseAdapter(Context context, ArrayList<Object> courses) {
        this.context = context;
        this.courses = courses;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kim_my_course, viewGroup, false);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, KimMyAssessmentsActivity.class);
                myIntent.putExtra("course", ((Course)courses.get(i)).id);
                context.startActivity(myIntent);
            }
        });
        return new CourseViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder courseViewHolder, int i) {
        courseViewHolder.nameView.setText(((Course)courses.get(i)).name);
        courseViewHolder.gradeView.setText(((Course)courses.get(i)).grade);
        courseViewHolder.creditView.setText(String.valueOf(((Course)courses.get(i)).credit));
    }

    @Override
    public int getItemCount() {
        return courses.size();
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

    public void updateAdapter(List<Object> courses){
        this.courses.clear();
        this.courses.addAll(courses);
        notifyDataSetChanged();
    }
}
