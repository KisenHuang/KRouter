package com.kisen.router.designermodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kisen.router.annotation.Router;

@Router(value = "designer", permissions = Router.Permission.LOGIN)
public class DesignerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer);
    }
}
