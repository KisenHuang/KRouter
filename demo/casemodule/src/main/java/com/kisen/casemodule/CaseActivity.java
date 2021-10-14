package com.kisen.casemodule;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kisen.router.RouterApi;
import com.kisen.router.annotations.Router;

@Router("case")
public class CaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
    }

    public void designerClick(View view) {
        RouterApi.build("designer").start(this);
    }

}
