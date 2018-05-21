package com.ytu.android.gobang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity_HardWhite extends AppCompatActivity {
    private ChessBoardView_HardWhite mChessBoardViewHardWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_white);
        mChessBoardViewHardWhite = (ChessBoardView_HardWhite) findViewById(R.id.boardView_hw);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 再来一局
        if (id == R.id.action_setting) {
            mChessBoardViewHardWhite.start();
            return true;
        }
        if(id == R.id.action_cancel) {
            mChessBoardViewHardWhite.fun();
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
