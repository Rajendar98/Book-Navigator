package com.firstapp.myfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class User_Admin extends AppCompatActivity {
    private Button user_btn,admin_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);
        user_btn=(Button) findViewById(R.id.user_btn);
        admin_btn=(Button) findViewById(R.id.admin_btn);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(User_Admin.this,All_search_activity.class);
                startActivity(intent);
            }
        });
        admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(User_Admin.this,Admin_floor.class);
                startActivity(intent);
            }
        });
    }
}