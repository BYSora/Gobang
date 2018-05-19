package com.ytu.android.gobang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class SelectActivity extends AppCompatActivity {

    private Button mSimpleBlackButton;
    private Button mSimpleWhiteButton;
    private Button mHardBlackButton;
    private Button mHardWhiteButton;
    private Button mDoubleButton;
    private Button mStatisticButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        mSimpleBlackButton = (Button) findViewById(R.id.simple_black_button);
        mSimpleBlackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,MainActivity_SimpleBlack.class);
                startActivity(intent);
            }
        });
        mSimpleWhiteButton = (Button) findViewById(R.id.simple_white_button);
        mSimpleWhiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,MainActivity_SimpleWhite.class);
                startActivity(intent);
            }
        });
        mHardBlackButton = (Button) findViewById(R.id.hard_black_button);
        mHardBlackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,MainActivity_HardBlack.class);
                startActivity(intent);
            }
        });
        mHardWhiteButton = (Button) findViewById(R.id.hard_white_button);
        mHardWhiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,MainActivity_HardWhite.class);
                startActivity(intent);
            }
        });
        mDoubleButton = (Button) findViewById(R.id.double_button);
        mDoubleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this,MainActivity_Double.class);
                startActivity(intent);
            }
        });
        mStatisticButton = (Button) findViewById(R.id.statistic_button);
        mStatisticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        //去除ActionBar下方的阴影
        if(Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }
    }
}
