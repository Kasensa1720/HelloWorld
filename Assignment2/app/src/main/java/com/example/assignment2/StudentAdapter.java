package com.example.assignment2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private StudentClickListener listener;

    public interface StudentClickListener {
        void onViewEnrollmentsClick(Student student);
        void onViewFacultyClick(Student student);
        void onEditClick(Student student);
        void onDeleteClick(Student student);
    }

    public StudentAdapter(List<Student> studentList, StudentClickListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.textStudentName.setText(student.getStudentName());
        holder.textStudentId.setText("ID: " + student.getStudentId());
        holder.textFaculty.setText("Faculty: " + student.getFacultyName());
        holder.textEmail.setText("Email: " + student.getEmail());
        holder.textCreator.setText("Created by: " + student.getCreatorName());

        holder.btnViewEnrollments.setOnClickListener(v -> listener.onViewEnrollmentsClick(student));
        holder.btnViewFaculty.setOnClickListener(v -> listener.onViewFacultyClick(student));
        holder.btnEditStudent.setOnClickListener(v -> listener.onEditClick(student));
        holder.btnDeleteStudent.setOnClickListener(v -> listener.onDeleteClick(student));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textStudentName, textStudentId, textFaculty, textEmail, textCreator;
        Button btnViewEnrollments, btnViewFaculty, btnEditStudent, btnDeleteStudent;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textStudentName = itemView.findViewById(R.id.textStudentName);
            textStudentId = itemView.findViewById(R.id.textStudentId);
            textFaculty = itemView.findViewById(R.id.textFaculty);
            textEmail = itemView.findViewById(R.id.textEmail);
            textCreator = itemView.findViewById(R.id.textCreator);
            btnViewEnrollments = itemView.findViewById(R.id.btnViewEnrollments);
            btnViewFaculty = itemView.findViewById(R.id.btnViewFaculty);
            btnEditStudent = itemView.findViewById(R.id.btnEditStudent);
            btnDeleteStudent = itemView.findViewById(R.id.btnDeleteStudent);
        }
    }
}