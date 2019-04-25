package com.bumjinkim.GPA4U;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class KimMyCourseAdapter extends RecyclerView.Adapter<KimMyCourseAdapter.CourseViewHolder> {

    private String[] names;
    private String[] grades;
    private int[] credits;

    public KimMyCourseAdapter(String[] names, String[] grades, int[] credits) {
        this.names = names;
        this.grades = grades;
        this.credits = credits;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kim_my_course, viewGroup, false);
        return new CourseViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder courseViewHolder, int i) {
        courseViewHolder.nameView.setText(names[i]);
        courseViewHolder.gradeView.setText(grades[i]);
        courseViewHolder.creditView.setText(credits[i]);
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

        }
    }

}
