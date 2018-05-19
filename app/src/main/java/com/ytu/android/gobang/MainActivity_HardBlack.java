package com.ytu.android.gobang;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity_HardBlack extends AppCompatActivity{
    private ChessBoardView_HardBlack mChessBoardViewHardBlack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_black);
        mChessBoardViewHardBlack = (ChessBoardView_HardBlack) findViewById(R.id.boardView_hb);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 再来一局
        if (id == R.id.action_setting) {
            mChessBoardViewHardBlack.start();
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
