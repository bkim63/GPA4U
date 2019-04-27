package com.bumjinkim.GPA4U;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class KimMyAssessmentAdapter extends RecyclerView.Adapter<KimMyAssessmentAdapter.KimMyAssessmentViewHolder> {

    private Context context;
    private ArrayList<Object> assessments;

    public KimMyAssessmentAdapter(Context context, ArrayList<Object> assessments, RecyclerView recyclerView) {
        this.context = context;
        this.assessments = assessments;
        this.recyclerView = recyclerView;
    }

    private RecyclerView recyclerView;

    @Override
    public KimMyAssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kim_my_assessment, viewGroup, false);
        return new KimMyAssessmentViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull KimMyAssessmentViewHolder kimMyAssessmentViewHolder, int i) {
        kimMyAssessmentViewHolder.nameView.setText(((Assessment)this.assessments.get(i)).name);
        kimMyAssessmentViewHolder.gradeView.setText(String.valueOf(((Assessment)this.assessments.get(i)).grade));
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public static class KimMyAssessmentViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView gradeView;

        public KimMyAssessmentViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.kim_my_assessment_name_view);
            gradeView = itemView.findViewById(R.id.kim_my_assessment_grade_view);
        }
    }

    public void updateAdapter(List<Object> assessments){
        this.assessments.clear();
        this.assessments.addAll(assessments);
        notifyDataSetChanged();
    }
}
