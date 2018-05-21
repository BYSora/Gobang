package com.ytu.android.gobang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity_SimpleBlack extends AppCompatActivity {
    private ChessBoardView_SimpleBlack mChessBoardViewSimpleBlack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_black);
        mChessBoardViewSimpleBlack = (ChessBoardView_SimpleBlack) findViewById(R.id.boardView_sb);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 再来一局
        if (id == R.id.action_setting) {
            mChessBoardViewSimpleBlack.start();
            return true;
        }
        if(id == R.id.action_cancel) {
            mChessBoardViewSimpleBlack.fun();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}