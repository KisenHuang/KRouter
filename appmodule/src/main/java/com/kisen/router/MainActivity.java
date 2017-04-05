package com.kisen.router;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kisen.router.annotation.Router;
import com.kisen.router.router.RouterApi;

@Router({"main", "root"})
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void caseClick(View view) {
        RouterApi.build("case").start(this);
    }

    public void designerClick(View view) {
        RouterApi.build("designer").start(this);
    }

}
