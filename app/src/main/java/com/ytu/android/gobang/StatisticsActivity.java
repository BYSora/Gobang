package com.ytu.android.gobang;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity{
    private TextView mSimpleBlackCount;
    private TextView mSimpleBlackPlayerWin;
    private TextView mSimpleWhiteCount;
    private TextView mSimpleWhitePlayerWin;
    private TextView mHardBlackCount;
    private TextView mHardBlackPlayerWin;
    private TextView mHardWhiteCount;
    private TextView mHardWhitePlayerWin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        SharedPreferences read = getSharedPreferences("chess", MODE_PRIVATE);
        mSimpleBlackCount = (TextView) findViewById(R.id.simple_black_count);
        mSimpleBlackPlayerWin = (TextView) findViewById(R.id.simple_black_player_win);
        mSimpleWhiteCount = (TextView) findViewById(R.id.simple_white_count);
        mSimpleWhitePlayerWin = (TextView) findViewById(R.id.simple_white_player_win);
        mHardBlackCount = (TextView) findViewById(R.id.hard_black_count);
        mHardBlackPlayerWin = (TextView) findViewById(R.id.hard_black_player_win);
        mHardWhiteCount = (TextView) findViewById(R.id.hard_white_count);
        mHardWhitePlayerWin = (TextView) findViewById(R.id.hard_white_player_win);

        int sbc = read.getInt(Constants.SIMPLEBLACKCOUNT, 0);
        double d = 0.0;
        String tmp = "玩家执黑总局数：" + String.valueOf(sbc);
        mSimpleBlackCount.setText(tmp);
        if(sbc == 0) {
            tmp = "玩家胜率：0%";
        }else{
            int sbw = read.getInt(Constants.SIMPLEBLACKAIWIN, 0);
            d = (double)(sbc-sbw)/sbc * 100;
            tmp = "玩家胜率：" + String.format("%.1f",d) + "%";
        }
        mSimpleBlackPlayerWin.setText(tmp);

        int swc = read.getInt(Constants.SIMPLEWHITECOUNT, 0);
        tmp = "玩家执白总局数：" + String.valueOf(swc);
        mSimpleWhiteCount.setText(tmp);
        if(swc == 0) {
            tmp = "玩家胜率：0%";
        }else{
            int sww = read.getInt(Constants.SIMPLEWHITEAIWIN, 0);
            d = (double)(swc-sww)/swc * 100;
            tmp = "玩家胜率：" + String.format("%.1f",d) + "%";
        }
        mSimpleWhitePlayerWin.setText(tmp);

        int hbc = read.getInt(Constants.HARDBLACKCOUNT, 0);
        tmp = "玩家执黑总局数：" + String.valueOf(hbc);
        mHardBlackCount.setText(tmp);
        if(hbc == 0) {
            tmp = "玩家胜率：0%";
        }else{
            int hbw = read.getInt(Constants.HARDBLACKAIWIN, 0);
            d = (double)(hbc - hbw)/hbc * 100;
            tmp = "玩家胜率：" + String.format("%.1f",d) + "%";
        }
        mHardBlackPlayerWin.setText(tmp);

        int hwc = read.getInt(Constants.HARDWHITECOUNT, 0);
        tmp = "玩家执白总局数：" + String.valueOf(hwc);
        mHardWhiteCount.setText(tmp);
        if(hwc == 0) {
            tmp = "玩家胜率：0%";
        }else {
            int hww = read.getInt(Constants.HARDWHITEAIWIN, 0);
            d = (double)(hwc - hww)/hwc * 100;
            tmp = "玩家胜率：" + String.format("%.1f",d) + "%";
        }
        mHardWhitePlayerWin.setText(tmp);

        //去除ActionBar下方的阴影
        if(Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }
    }
}
