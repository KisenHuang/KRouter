package com.kisen.router.designermodule;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kisen.router.annotations.Router;


@Router(value = "designer", intercept = "login")
public class DesignerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer);
    }
}
