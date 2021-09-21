/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ET_Productions.minesweeper.GameUI.GameUIActivity;
import com.ET_Productions.minesweeper.GeneralGameFiles.Difficulty;
import com.ET_Productions.minesweeper.GeneralGameFiles.HighScores;
import com.example.minesweeper.R;

import java.io.FileInputStream;


public class MainActivity extends Activity {

    private EditText edfirstname2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getActionBar().hide();

        edfirstname2 = (EditText) findViewById(R.id.editText2);

    }

    public void play(View v) {
        Log.d("C: Main, F: Play", "The First Log");

        Intent in = new Intent(this, Difficulty.class);
        Log.d("C: Main, F: Play", "just created the intent, it'll send me to Difficulty class ");

        String n1 = this.edfirstname2.getText().toString();
        if(n1 == null || n1.equals("")){
            n1 = "Steve";
        }

        in.putExtra("isNewGame", "it is a new Game");
        Log.d("C: Main, F: Play", "Player Name is " + n1);
        in.putExtra("player name", n1);
        startActivity(in);
        finish();

    }

    public void load(View v) {
        Intent loadIn = new Intent(this, GameUIActivity.class);
        String n1 = "LoaderIstheName";
        loadIn.putExtra("player name", n1);
        if(checkSave()) {
            startActivity(loadIn);
            finish();
        }
        else{
            return;
        }

    }

    public boolean checkSave() {
        try {
            FileInputStream fileStream = this.openFileInput("Saves");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "There Is No Last Save", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    public void highScore(View v) {
        Intent intent = new Intent(this, HighScores.class);
        startActivity(intent);
    }
}
