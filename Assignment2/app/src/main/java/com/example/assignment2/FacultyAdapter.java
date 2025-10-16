package com.example.assignment2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder> {

    private List<Faculty> facultyList;
    private FacultyClickListener listener;

    public interface FacultyClickListener {
        void onViewStudentsClick(Faculty faculty);
        void onViewCoursesClick(Faculty faculty);
        void onEditClick(Faculty faculty);
        void onDeleteClick(Faculty faculty);
    }

    public FacultyAdapter(List<Faculty> facultyList, FacultyClickListener listener) {
        this.facultyList = facultyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FacultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_item, parent, false);
        return new FacultyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyViewHolder holder, int position) {
        Faculty faculty = facultyList.get(position);

        holder.textFacultyName.setText(faculty.getFacultyName());
        holder.textDeanName.setText("Dean: " + faculty.getDeanName());
        holder.textCreator.setText("Created by: " + faculty.getCreatorName());

        holder.btnViewStudents.setOnClickListener(v -> listener.onViewStudentsClick(faculty));
        holder.btnViewCourses.setOnClickListener(v -> listener.onViewCoursesClick(faculty));
        holder.btnEditFaculty.setOnClickListener(v -> listener.onEditClick(faculty));
        holder.btnDeleteFaculty.setOnClickListener(v -> listener.onDeleteClick(faculty));
    }

    @Override
    public int getItemCount() {
        return facultyList.size();
    }

    static class FacultyViewHolder extends RecyclerView.ViewHolder {
        TextView textFacultyName, textDeanName, textCreator;
        Button btnViewStudents, btnViewCourses, btnEditFaculty, btnDeleteFaculty;

        public FacultyViewHolder(@NonNull View itemView) {
            super(itemView);
            textFacultyName = itemView.findViewById(R.id.textFacultyName);
            textDeanName = itemView.findViewById(R.id.textDeanName);
            textCreator = itemView.findViewById(R.id.textCreator);
            btnViewStudents = itemView.findViewById(R.id.btnViewStudents);
            btnViewCourses = itemView.findViewById(R.id.btnViewCourses);
            btnEditFaculty = itemView.findViewById(R.id.btnEditFaculty);
            btnDeleteFaculty = itemView.findViewById(R.id.btnDeleteFaculty);
        }
    }
}