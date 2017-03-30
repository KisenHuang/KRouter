package com.kisen.route.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kisen.router.annotation.Router;
import com.kisen.router.router.RouterApi;

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
