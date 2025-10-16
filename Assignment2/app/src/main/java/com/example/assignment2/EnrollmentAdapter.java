package com.example.assignment2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EnrollmentAdapter extends RecyclerView.Adapter<EnrollmentAdapter.EnrollmentViewHolder> {

    private List<Enrollment> enrollmentList;
    private EnrollmentClickListener listener;

    public interface EnrollmentClickListener {
        void onDeleteClick(Enrollment enrollment);
    }

    public EnrollmentAdapter(List<Enrollment> enrollmentList, EnrollmentClickListener listener) {
        this.enrollmentList = enrollmentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EnrollmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.enrollment_item, parent, false);
        return new EnrollmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnrollmentViewHolder holder, int position) {
        Enrollment enrollment = enrollmentList.get(position);

        holder.textStudentName.setText(enrollment.getStudentName());
        holder.textCourseName.setText("Course: " + enrollment.getCourseName());
        holder.textEnrollmentDate.setText("Enrolled: " + enrollment.getEnrollmentDate());
        holder.textCreator.setText("Created by: " + enrollment.getCreatorName());

        holder.btnDeleteEnrollment.setOnClickListener(v -> listener.onDeleteClick(enrollment));
    }

    @Override
    public int getItemCount() {
        return enrollmentList.size();
    }

    static class EnrollmentViewHolder extends RecyclerView.ViewHolder {
        TextView textStudentName, textCourseName, textEnrollmentDate, textCreator;
        Button btnDeleteEnrollment;

        public EnrollmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textStudentName = itemView.findViewById(R.id.textStudentName);
            textCourseName = itemView.findViewById(R.id.textCourseName);
            textEnrollmentDate = itemView.findViewById(R.id.textEnrollmentDate);
            textCreator = itemView.findViewById(R.id.textCreator);
            btnDeleteEnrollment = itemView.findViewById(R.id.btnDeleteEnrollment);
        }
    }
}