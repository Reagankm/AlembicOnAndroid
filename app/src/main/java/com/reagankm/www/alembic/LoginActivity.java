package com.reagankm.www.alembic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button fbButton;
    private Button skipLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fbButton = (Button) findViewById(R.id.fb_login);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        skipLoginButton = (Button) findViewById(R.id.skip_login);
        skipLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){


           }
        });

    }
}
