package com.kisen.router.casemodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kisen.router.annotation.Router;
import com.kisen.router.router.RouterApi;

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
