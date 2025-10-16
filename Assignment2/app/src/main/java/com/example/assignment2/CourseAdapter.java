package com.example.assignment2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private CourseClickListener listener;

    public interface CourseClickListener {
        void onViewEnrollmentsClick(Course course);
        void onViewFacultyClick(Course course);
        void onEditClick(Course course);
        void onDeleteClick(Course course);
    }

    public CourseAdapter(List<Course> courseList, CourseClickListener listener) {
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);

        holder.textCourseName.setText(course.getCourseName());
        holder.textCourseCode.setText("Code: " + course.getCourseCode());
        holder.textFaculty.setText("Faculty: " + course.getFacultyName());
        holder.textCredits.setText("Credits: " + course.getCredits());
        holder.textCreator.setText("Created by: " + course.getCreatorName());

        holder.btnViewEnrollments.setOnClickListener(v -> listener.onViewEnrollmentsClick(course));
        holder.btnViewFaculty.setOnClickListener(v -> listener.onViewFacultyClick(course));
        holder.btnEditCourse.setOnClickListener(v -> listener.onEditClick(course));
        holder.btnDeleteCourse.setOnClickListener(v -> listener.onDeleteClick(course));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView textCourseName, textCourseCode, textFaculty, textCredits, textCreator;
        Button btnViewEnrollments, btnViewFaculty, btnEditCourse, btnDeleteCourse;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textCourseName = itemView.findViewById(R.id.textCourseName);
            textCourseCode = itemView.findViewById(R.id.textCourseCode);
            textFaculty = itemView.findViewById(R.id.textFaculty);
            textCredits = itemView.findViewById(R.id.textCredits);
            textCreator = itemView.findViewById(R.id.textCreator);
            btnViewEnrollments = itemView.findViewById(R.id.btnViewEnrollments);
            btnViewFaculty = itemView.findViewById(R.id.btnViewFaculty);
            btnEditCourse = itemView.findViewById(R.id.btnEditCourse);
            btnDeleteCourse = itemView.findViewById(R.id.btnDeleteCourse);
        }
    }
}