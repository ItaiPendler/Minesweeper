/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.GeneralGameFiles;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ET_Productions.minesweeper.GameUI.GameUIActivity;
import com.ET_Productions.minesweeper.MainActivity;
import com.example.minesweeper.R;

public class Difficulty extends Activity {

    private EditText gdtf;
    private int gameD;
    private Intent in;
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diffecullty);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent in1 = getIntent();

        gdtf = (EditText) findViewById(R.id.duffuculltySettterTF);
        Bundle b = in1.getExtras();

        playerName = b.getString("player name");


        getActionBar().hide();

    }

    public void onClick(View v) {
        String a = gdtf.getText().toString();
        Log.d(" a ", " is " + a);
        if (a != null) {
            gameD = Integer.parseInt(a);
        }

        in = new Intent(this, GameUIActivity.class);
        in.putExtra("player name", playerName);
        in.putExtra("isNewGame", "it is a new Game");
        switch (v.getId()) {
            case R.id.butnBegginer1:

                in.putExtra("GameD", 21);
                startActivity(in);
                finish();
                break;

            case R.id.butnExpert:
                in.putExtra("GameD", 30);
                startActivity(in);
                finish();
                break;

            case R.id.butnMaster:
                in.putExtra("GameD", 50);
                startActivity(in);
                finish();
                break;

            case R.id.butnCustom:
                in.putExtra("GameD", gameD);
                startActivity(in);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
