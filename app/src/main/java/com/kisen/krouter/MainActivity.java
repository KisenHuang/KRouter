package com.kisen.krouter;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kisen.router.RouterApi;
import com.kisen.router.annotations.Router;

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

    @Override
    protected void onDestroy() {
        RouterApi.printMonitor();
        super.onDestroy();
    }
}
