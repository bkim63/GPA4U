package com.bumjinkim.GPA4U;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class KimMyAssessmentAdapter extends RecyclerView.Adapter<KimMyAssessmentAdapter.KimMyAssessmentViewHolder> {

    private String[] names;
    private String[] grades;

    public KimMyAssessmentAdapter(String[] names, String[] grades) {
        this.names = names;
        this.grades = grades;
    }

    @Override
    public KimMyAssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kim_my_assessment, viewGroup, false);
        return new KimMyAssessmentViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull KimMyAssessmentViewHolder kimMyAssessmentViewHolder, int i) {
        kimMyAssessmentViewHolder.nameView.setText(names[i]);
        kimMyAssessmentViewHolder.gradeView.setText(grades[i]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public static class KimMyAssessmentViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView gradeView;

        public KimMyAssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
