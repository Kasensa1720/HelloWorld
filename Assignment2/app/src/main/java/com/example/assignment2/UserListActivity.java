package com.example.assignment2;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import database.DatabaseHelper;

public class UserListActivity extends AppCompatActivity {

    private ListView lvUsers;
    private ArrayList<User> userList;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        databaseHelper = new DatabaseHelper(this);
        lvUsers = findViewById(R.id.lv_users);

        loadUsersFromDatabase();
        setupListView();
    }

    private void loadUsersFromDatabase() {
        userList = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllUsers();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String userId = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_USER_ID);
                String username = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_USERNAME);
                String email = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_EMAIL);
                String phone = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_PHONE);
                String gender = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_GENDER);
                String password = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_PASSWORD);

                userList.add(new User(userId, username, email, phone, gender, password, false));

            } while (cursor.moveToNext());
            cursor.close();
        }

        if (userList.isEmpty()) {
            Toast.makeText(this, "No users found in database", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListView() {
        displayList = new ArrayList<>();
        for (User user : userList) {
            displayList.add(user.getUserId() + " - " + user.getName());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        lvUsers.setAdapter(adapter);

        // Long click for delete
        lvUsers.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmationDialog(position);
            return true;
        });

        // Normal click for view details
        lvUsers.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = userList.get(position);
            Intent intent = new Intent(this, UserDetailsActivity.class);
            intent.putExtra("userId", selectedUser.getUserId());
            intent.putExtra("fullName", selectedUser.getName());
            intent.putExtra("email", selectedUser.getEmail());
            intent.putExtra("phone", selectedUser.getPhone());
            intent.putExtra("gender", selectedUser.getGender());
            intent.putExtra("newsletter", selectedUser.isNewsletter());
            intent.putExtra("password", selectedUser.getPassword());
            intent.putExtra("fromList", true);
            startActivity(intent);
        });
    }

    private void showDeleteConfirmationDialog(int position) {
        User userToDelete = userList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + userToDelete.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> deleteUser(position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteUser(int position) {
        User userToDelete = userList.get(position);
        boolean isDeleted = databaseHelper.deleteUser(userToDelete.getUserId());

        if (isDeleted) {
            userList.remove(position);
            displayList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "User deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete user!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSafeColumnValue(Cursor cursor, String columnName) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (columnIndex != -1) {
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}