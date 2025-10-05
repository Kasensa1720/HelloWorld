package com.example.assignment2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    private ListView lvUsers;
    private ArrayList<User> userList;
    private SharedPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);  // Rename layout to activity_student_list.xml

        userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        lvUsers = findViewById(R.id.lv_users);

        loadUsers();
        setupListView();
    }

    private void loadUsers() {
        userList = new ArrayList<>();
        int nextId = userPrefs.getInt("next_user_id", 1);

        for (int i = 1; i < nextId; i++) {
            String name = userPrefs.getString("user_" + i + "_name", "");
            if (!name.isEmpty()) {
                String email = userPrefs.getString("user_" + i + "_email", "");
                String phone = userPrefs.getString("user_" + i + "_phone", "");
                String gender = userPrefs.getString("user_" + i + "_gender", "");
                String password = userPrefs.getString("user_" + i + "_password", "");
                boolean newsletter = userPrefs.getBoolean("user_" + i + "_newsletter", false);
                userList.add(new User("USER" + String.format("%03d", i), name, email, phone, gender, password, newsletter));
            }
        }
    }

    private void setupListView() {
        ArrayList<String> displayList = new ArrayList<>();
        for (User user : userList) {
            displayList.add(user.getUserId() + " - " + user.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        lvUsers.setAdapter(adapter);

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

        if (userList.isEmpty()) {
            Toast.makeText(this, "No users yet. Sign up first!", Toast.LENGTH_SHORT).show();
        }
    }
}

//commit made for Student 1