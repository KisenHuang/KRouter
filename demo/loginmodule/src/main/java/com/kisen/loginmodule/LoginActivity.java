package com.kisen.loginmodule;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kisen.router.RouterApi;
import com.kisen.router.annotations.Router;

@Router("login")
public class LoginActivity extends AppCompatActivity {

    private String target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle extras = getIntent().getExtras();
        target = extras.getString("target");
    }

    public void onLoginClick(View view) {
        RouterApi.build(target)
                .setSkipInterceptors(true)
                .start(this);
        finish();
    }
}
