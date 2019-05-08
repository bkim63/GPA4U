package com.bumjinkim.GPA4U;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class KimMyAssessmentAdapter extends RecyclerView.Adapter<KimMyAssessmentAdapter.KimMyAssessmentViewHolder> {

    private final long courseId;
    private Context context;
    private ArrayList<KimAssessment> assessments;
    private int kim_add_assessment_request_code = 2;

    public KimMyAssessmentAdapter(Context context, long courseId, ArrayList<KimAssessment> assessments, RecyclerView recyclerView) {
        this.context = context;
        this.assessments = assessments;
        this.courseId = courseId;
        this.recyclerView = recyclerView;
    }

    private RecyclerView recyclerView;

    @Override
    public KimMyAssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kim_my_assessment, viewGroup, false);
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
                                Intent editIntent = new Intent(context, KimAddAssessmentActivity.class);
                                editIntent.putExtra("method", "edit");
                                editIntent.putExtra("assessment", assessments.get(recyclerView.getChildAdapterPosition(v)).id);
                                editIntent.putExtra("course", courseId);
                                ((AppCompatActivity)context).startActivityForResult(editIntent, kim_add_assessment_request_code);
                                break;
                            case 1:
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();

                                final RealmResults<KimAssessment> results = realm.where(KimAssessment.class).equalTo("id", assessments.get(recyclerView.getChildAdapterPosition(v)).id).findAll();
                                results.deleteAllFromRealm();

                                realm.commitTransaction();
                                assessments.remove(recyclerView.getChildAdapterPosition(v));
                                notifyDataSetChanged();

                                KimPushNotification.sendPush(context);

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
        return new KimMyAssessmentViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull KimMyAssessmentViewHolder kimMyAssessmentViewHolder, int i) {
        kimMyAssessmentViewHolder.nameView.setText(this.assessments.get(i).name);
        kimMyAssessmentViewHolder.gradeView.setText(KimCalculateGrade.calculateCourseLetterGrade(this.assessments.get(i).grade));
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

    public void updateAdapter(ArrayList<KimAssessment> assessments){
        this.assessments = assessments;
        notifyDataSetChanged();
    }
}
