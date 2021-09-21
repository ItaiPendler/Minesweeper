/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.GeneralGameFiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minesweeper.R;


public class HighScores extends Activity {

    private TextView tv_lastscore, tv_bestscore, tv_2ndbestscore, tv_3rdbestscore;

    private CheckBox cbGeneral, cbBegginer, cbExpert, cbMaster;

    private String lastScore1ArrayString, best1ArrayString, best2ArrayString, best3ArrayString;

    private String expandedLastScorePlayerDisplayString;
    private String expandedBest1ScorePlayerDisplayString;
    private String expandedBest2ScorePlayerDisplayString;
    private String expandedBest3ScorePlayerDisplayString;

    private String best1DisplayScoreString;
    private String best2DisplayScoreString;
    private String best3DisplayScoreString;
    private String lastScoreDisplayScoreString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        getActionBar().hide();

        int intbest1 = Integer.MAX_VALUE, intbest2 = Integer.MAX_VALUE, intbest3 = Integer.MAX_VALUE, intlastScore = Integer.MAX_VALUE;
        Intent intentin = getIntent();
        String finalStringbest1 = "", finalStringbest2 = "", finalStringbest3 = "";

        //finds the TextViews in the XML file
        tv_lastscore = (TextView) findViewById(R.id.LastScoreTV);
        tv_lastscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(v);
            }
        });
        tv_bestscore = (TextView) findViewById(R.id.highestScore);
        tv_bestscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(v);
            }
        });
        tv_2ndbestscore = (TextView) findViewById(R.id.secondBestScore);
        tv_2ndbestscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(v);
            }
        });
        tv_3rdbestscore = (TextView) findViewById(R.id.thirdBestScore);
        tv_3rdbestscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(v);
            }
        });


        cbBegginer = (CheckBox) findViewById(R.id.cbBeginner);
        cbBegginer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbOnclick(v);
            }
        });
        cbExpert = (CheckBox) findViewById(R.id.cbExpert);
        cbExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbOnclick(v);
            }
        });
        cbMaster = (CheckBox) findViewById(R.id.cbMaster);
        cbMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbOnclick(v);
            }
        });
        cbGeneral = (CheckBox) findViewById(R.id.cbGeneral);
        cbGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbOnclick(v);
            }
        });
        cbGeneral.setChecked(true);

        SharedPreferences general_final_scores = getSharedPreferences("GENERAL_FINAL_SCORE", MODE_PRIVATE);
        getData(general_final_scores);
    }


    public void clearScores(View v) {

        //clears the values from the editor.
        SharedPreferences preferences = getSharedPreferences("GENERAL_FINAL_SCORE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("best1", "NA");
        editor.putString("best2", "NA");
        editor.putString("best3", "NA");
        editor.apply();

        SharedPreferences Beginnerpreferences = getSharedPreferences("BEGINNER_FINAL_SCORE", 0);
        SharedPreferences.Editor Beginnereditor = Beginnerpreferences.edit();
        Beginnereditor.putString("best1", "NA");
        Beginnereditor.putString("best2", "NA");
        Beginnereditor.putString("best3", "NA");
        Beginnereditor.apply();


        SharedPreferences expertpreferences = getSharedPreferences("EXPERT_FINAL_SCORE", 0);
        SharedPreferences.Editor Experteditor = expertpreferences.edit();
        Experteditor.putString("best1", "NA");
        Experteditor.putString("best2", "NA");
        Experteditor.putString("best3", "NA");
        Experteditor.apply();

        SharedPreferences masterPreferences = getSharedPreferences("MASTER_FINAL_SCORE", 0);
        SharedPreferences.Editor masterEditor = masterPreferences.edit();
        masterEditor.putString("best1", "NA");
        masterEditor.putString("best2", "NA");
        masterEditor.putString("best3", "NA");
        masterEditor.apply();

        //clears the text from the Textviews
        tv_bestscore.setText("Best Score Was: NA");
        tv_2ndbestscore.setText("#2 Best Score Was: NA");
        tv_3rdbestscore.setText("#3 Best Score Was: NA");

        //Toasts the confirmation.
        Toast.makeText(getApplicationContext(), "Scores Cleared", Toast.LENGTH_SHORT).show();
    }

    public void showMoreDialog(View v) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = this.getLayoutInflater().inflate(R.layout.score_dialog, null);

        Button closeDialogButn = (Button) mView.findViewById(R.id.closeDialog);
        TextView scoreTV = (TextView) mView.findViewById(R.id.scoreTV);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        closeDialogButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        switch (v.getId()) {
            case R.id.LastScoreTV:
                scoreTV.setText(expandedLastScorePlayerDisplayString);
                break;

            case R.id.highestScore:
                scoreTV.setText(expandedBest1ScorePlayerDisplayString);
                break;

            case R.id.secondBestScore:
                scoreTV.setText(expandedBest2ScorePlayerDisplayString);
                break;

            case R.id.thirdBestScore:
                scoreTV.setText(expandedBest3ScorePlayerDisplayString);
                break;
        }

        dialog.show();

    }



    private void cbOnclick(View v) {
        switch (v.getId()) {
            case R.id.cbBeginner:
                cbBegginer.setChecked(true);

                cbMaster.setChecked(false);
                cbExpert.setChecked(false);
                cbGeneral.setChecked(false);

                changeView(1);
                break;
            case R.id.cbExpert:
                cbBegginer.setChecked(false);
                cbMaster.setChecked(false);
                cbGeneral.setChecked(false);

                cbExpert.setChecked(true);

                changeView(2);
                break;
            case R.id.cbMaster:
                cbMaster.setChecked(true);

                cbBegginer.setChecked(false);
                cbGeneral.setChecked(false);
                cbExpert.setChecked(false);

                changeView(3);
                break;
            case R.id.cbGeneral:
                cbBegginer.setChecked(false);
                cbMaster.setChecked(false);
                cbExpert.setChecked(false);

                cbGeneral.setChecked(true);

                changeView(0);
                break;

        }
    }


    private void changeView(int mode) {
        switch (mode) {
            case 0:
                //General
                SharedPreferences FinalGeneralScorePrefrences = getSharedPreferences("GENERAL_FINAL_SCORE", Context.MODE_PRIVATE);
                getData(FinalGeneralScorePrefrences);

                System.gc();
            case 1:
                //Beginner
                SharedPreferences FinalBeginnerPreferences = getSharedPreferences("BEGINNER_FINAL_SCORE", Context.MODE_PRIVATE);
                getData(FinalBeginnerPreferences);

                System.gc();
                break;
            case 2:
                //Expert
                SharedPreferences FinalExpertPreferences = getSharedPreferences("EXPERT_FINAL_SCORE", Context.MODE_PRIVATE);
                getData(FinalExpertPreferences);

                System.gc();
                break;
            case 3:
                //Master
                SharedPreferences FinalMasterPreferences = getSharedPreferences("MASTER_FINAL_SCORE", Context.MODE_PRIVATE);
                getData(FinalMasterPreferences);

                System.gc();
                break;
        }
    }


    private void getData(SharedPreferences preferences) {
        Log.d("C:HS, F:getData", "Just Started");

        Player lastScore = null, best1 = null, best2 = null, best3 = null;

        best1DisplayScoreString = null;
        best2DisplayScoreString = null;
        best3DisplayScoreString = null;
        lastScoreDisplayScoreString = null;

        int ssLastScore = Integer.MAX_VALUE, ssBest1 = Integer.MAX_VALUE, ssBest2 = Integer.MAX_VALUE, ssBest3 = Integer.MAX_VALUE;

        //Getting last Score.
        lastScore1ArrayString = preferences.getString("testLastScore", "NA");
        Log.d("C:HS, F:getData", " just got lastScore1ArrayString, it is " + lastScore1ArrayString);
        if (!lastScore1ArrayString.equals("NA")) {
            String[] lastScorePlayer1Array = lastScore1ArrayString.split("#");
            String lastScore1Name = lastScorePlayer1Array[0];
            String lastScore1GameD = lastScorePlayer1Array[1];
            String lastScore1Time = lastScorePlayer1Array[2];

            String[] timeArray = lastScore1Time.split(":");
            if (timeArray.length == 3) {
                int HH = Integer.parseInt(timeArray[0]);
                int mm = Integer.parseInt(timeArray[1]) + (HH * 60);
                ssLastScore = Integer.parseInt(timeArray[2]) + (mm * 60);
            } else {
                int mm = Integer.parseInt(timeArray[0]);
                ssLastScore = Integer.parseInt(timeArray[1]) + (mm * 60);
            }
            String lastScore1Date = lastScorePlayer1Array[3];
            String lastScore1Moves = lastScorePlayer1Array[4];
            lastScore = new Player(lastScore1Name, Integer.valueOf(lastScore1Moves), lastScore1GameD, lastScore1Date, lastScore1Time);
            Log.d("C:HS, F:getData", "player toString is " + lastScore.toString());
            lastScoreDisplayScoreString = lastScore1Name + " who won in " + lastScore1Time;
            expandedLastScorePlayerDisplayString = lastScore.getName() + " won the game in " + lastScore1Time + " in the " + lastScore.getGameD() + "'s difficulty, he won on " + lastScore1Date + " in " + lastScore1Moves + " moves.";
            Log.d("C:HS, F:getData", "just set expandedLastScorePlayerDisplayString to " + expandedLastScorePlayerDisplayString);
        }

        best1ArrayString = preferences.getString("best1", "NA");
        Log.d("C:HS, F:getData", "just got best1ArrayString and it is " + best1ArrayString);
        if (!best1ArrayString.equals("NA")) {
            String[] best1Array = best1ArrayString.split("#");
            String best1Name = best1Array[0];
            String best1GameD = best1Array[1];
            String best1Time = best1Array[2];

            String[] timeArray = best1Time.split(":");
            if (timeArray.length == 3) {
                int HH = Integer.parseInt(timeArray[0]);
                int mm = Integer.parseInt(timeArray[1]) + (HH * 60);
                ssBest1 = Integer.parseInt(timeArray[2]) + (mm * 60);
            } else {
                int mm = Integer.parseInt(timeArray[0]);
                ssBest1 = Integer.parseInt(timeArray[1]) + (mm * 60);
            }
            String best1Date = best1Array[3];
            String best1Moves = best1Array[4];
            best1 = new Player(best1Name, Integer.valueOf(best1Moves), best1GameD, best1Date, best1Time);
            Log.d("C:HS, F:getData", "player toString is " + best1.toString());
            best1DisplayScoreString = best1Name + " who won in " + best1Time;
            expandedBest1ScorePlayerDisplayString = best1.getName() + " won the game in " + best1Time + " in the " + best1.getGameD() + "'s difficulty, he won on " + best1Date + " in " + best1Moves + " moves.";
            Log.d("C:HS, F:getData", "just set expandedBest1PlayerDisplayString to " + expandedBest1ScorePlayerDisplayString);
        }

        best2ArrayString = preferences.getString("best1", "NA");
        Log.d("C:HS, F:getData", "just got best2ArrayString and it is " + best2ArrayString);
        if (!best2ArrayString.equals("NA")) {
            String[] best2Array = best2ArrayString.split("#");
            String best2Name = best2Array[0];
            String best2GameD = best2Array[1];
            String best2Time = best2Array[2];
            String[] timeArray = best2Time.split(":");
            if (timeArray.length == 3) {
                int HH = Integer.parseInt(timeArray[0]);
                int mm = Integer.parseInt(timeArray[1]) + (HH * 60);
                ssBest2 = Integer.parseInt(timeArray[2]) + (mm * 60);
            } else {
                int mm = Integer.parseInt(timeArray[0]);
                ssBest2 = Integer.parseInt(timeArray[1]) + (mm * 60);
            }
            String best2Date = best2Array[3];
            String best2Moves = best2Array[4];
            best2 = new Player(best2Name, Integer.valueOf(best2Moves), best2GameD, best2Date, best2Time);
            Log.d("C:HS, F:getData", "player2 toString is " + best2.toString());
            best2DisplayScoreString = best2Name + " who won in " + best2Time;
            expandedBest2ScorePlayerDisplayString = best2.getName() + " won the game in " + best2Time + " in the " + best2.getGameD() + "'s difficulty, he won on " + best2Date + " in " + best2Moves + " moves.";
            Log.d("C:HS, F:getData", "just set expandedBest2PlayerDisplayString to " + expandedBest2ScorePlayerDisplayString);
        }


        best3ArrayString = preferences.getString("best3", "NA");
        Log.d("C:HS, F:getData", "just got best3ArrayString and it is " + best3ArrayString);
        if (!best3ArrayString.equals("NA")) {
            String[] best3Array = best3ArrayString.split("#");
            String best3Name = best3Array[0];
            String best3GameD = best3Array[1];
            String best3Time = best3Array[2];

            String[] timeArray = best3Time.split(":");
            if (timeArray.length == 3) {
                int HH = Integer.parseInt(timeArray[0]);
                int mm = Integer.parseInt(timeArray[1]) + (HH * 60);
                ssBest3 = Integer.parseInt(timeArray[2]) + (mm * 60);
            } else {
                int mm = Integer.parseInt(timeArray[0]);
                ssBest3 = Integer.parseInt(timeArray[1]) + (mm * 60);
            }

            String best3Date = best3Array[3];
            String best3Moves = best3Array[4];
            best3 = new Player(best3Name, Integer.valueOf(best3Moves), best3GameD, best3Date, best3Time);
            Log.d("C:HS, F:getData", "player toString is " + best3.toString());
            best3DisplayScoreString = best3Name + " who won in " + best3Time;
            expandedBest3ScorePlayerDisplayString = best3.getName() + " won the game in " + best3Time + " in the " + best3.getGameD() + "'s difficulty, he won on " + best3Date + " in " + best3Moves + " moves.";
            Log.d("C:HS, F:getData", "just set expandedBest3PlayerDisplayString to " + expandedBest3ScorePlayerDisplayString);
        }
        sortScores(ssBest1, ssBest2, ssBest3, ssLastScore, preferences);
        displayScores(best1DisplayScoreString, best2DisplayScoreString, best3DisplayScoreString, lastScoreDisplayScoreString);
    }

    private void displayScores(String best1DisplayScoreString, String best2DisplayScoreString, String best3DisplayScoreString, String lastScoreDisplayScoreString) {
        if (lastScoreDisplayScoreString == null||best1DisplayScoreString == null|| best2DisplayScoreString == null||best3DisplayScoreString == null) {
            Toast.makeText(getApplicationContext(), "There Aren't Any High Scores In This Category, Go Make Some!", Toast.LENGTH_LONG).show();
            cbOnclick(findViewById(R.id.cbGeneral));
        }
        tv_lastscore.setText("the last score was " + lastScoreDisplayScoreString);
        tv_bestscore.setText("the best Score was " + best1DisplayScoreString);
        tv_2ndbestscore.setText("the 2nd best Score was " + best2DisplayScoreString);
        tv_3rdbestscore.setText("the 3rd best Score was " + best3DisplayScoreString);

    }

    private void sortScores(int best1Time, int best2Time, int best3Time, int lastScoreTime, SharedPreferences preferences) {

        //sort the times.

        if (lastScoreTime < best3Time) {
            best3ArrayString = lastScore1ArrayString;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("best3", best3ArrayString);
            editor.apply();
        }

        if (lastScoreTime < best2Time) {
            String temp = best2ArrayString;

            best2ArrayString = lastScore1ArrayString;

            best3ArrayString = temp;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("best3", best3ArrayString);
            editor.putString("best2", best2ArrayString);
            editor.apply();
        }
        if (lastScoreTime < best1Time) {
            String temp = best1ArrayString;

            best1ArrayString = lastScore1ArrayString;

            best2ArrayString = temp;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("best1", best1ArrayString);
            editor.putString("best2", best2ArrayString);
            editor.apply();
        }
    }
}
