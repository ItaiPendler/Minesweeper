/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.Actions;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ET_Productions.minesweeper.GeneralGameFiles.BoardFiles.Board;
import com.ET_Productions.minesweeper.GameUI.GameUIActivity;
import com.ET_Productions.minesweeper.GameUI.GameUIActivityInterface;
import com.ET_Productions.minesweeper.GeneralGameFiles.BoardFiles.Cell;
import com.ET_Productions.minesweeper.GeneralGameFiles.HighScores;
import com.example.minesweeper.R;

import java.io.Serializable;


public class Actions implements Serializable, Actions_Interface {


    private GameUIActivityInterface gameUIActivityInterface;

    public Actions(GameUIActivity gameUIActivity) {
        gameUIActivityInterface = gameUIActivity;
    }

    public void press(View v, GameUIActivity dis)
    {
        Vibrator vib = (Vibrator) dis.getSystemService(Context.VIBRATOR_SERVICE);
        boolean lost = false;
        MediaPlayer explosion = MediaPlayer.create(dis.getApplicationContext(), R.raw.explosionsoundeffect);

        CharSequence tempI = v.getContentDescription();
        Log.d("C: Board, F: Press", "tempI IS " + tempI);
        String stirngI = String.valueOf(tempI.charAt(2));
        int i = Integer.valueOf(stirngI);
        Log.d("C: Board, F: Press", "tempI IS " + tempI + " and i = " + i);
        String stringJ = String.valueOf(tempI.charAt(6));
        int j = Integer.valueOf(stringJ);
        Log.d("C: Board, F: Press", "tempI IS " + tempI + " and j = " + j);

        Log.d("C: Board, F: Press","id is " +  dis.getResources().getResourceEntryName(v.getId()) );

        boolean finished = false;
        if (!dis.getGameManager().isFlagMode())
        {
            if (dis.getGameManager().getBoard().getCells()[i][j].isMine())
            {
                lose(dis.getGameManager().getBoard());

                gameUIActivityInterface.setBackground("ogexplodedmine", dis.getResources().getResourceEntryName(v.getId()));

                gameUIActivityInterface.setBackground("deadsmiley","realButnReset");

                lost = true;

                dis.getPlayer().pause();

                vib = (Vibrator) dis.getSystemService(Context.VIBRATOR_SERVICE);
                Log.d("can Vibrate", " =" + vib.hasVibrator());
                vib.vibrate(500);

                dis.getTimer().stop();

                explosion.setLooping(false);
                explosion.start();

                dis.startAnim("LOSE");

            } else {
                if (dis.getGameManager().getBoard().getCells()[i][j].isFlagged()) {
                    this.openBoard(dis.getGameManager().getBoard().getCells()[i][j], i, j, dis.getGameManager().getBoard());
                    dis.getUser().setFlagNum(dis.getUser().getFlagNum() + 1);
                }
                if (dis.getGameManager().getBoard().getCells()[i][j].isHidden()) {
                    this.openBoard(dis.getGameManager().getBoard().getCells()[i][j], i, j, dis.getGameManager().getBoard());

                }
            }

        } else
            {
                if (!finished)
                {
                    if (dis.getGameManager().getBoard().getCells()[i][j].isFlagged())
                    {
                        dis.unflag(i, j);
                        dis.getUser().setFlagNum(dis.getUser().getFlagNum() + 1);
                    }
                    else
                        {
                            if (dis.getUser().getFlagNum() != 0)
                            {
                                dis.flag(i, j);
                                dis.getUser().setFlagNum(dis.getUser().getFlagNum() - 1);
                            }

                        }

                }
            }
        System.gc();


        if (!lost) {
            if (this.win( dis.getGameManager().getBoard() ) ) {
                dis.getTimer().stop();
                this.finishGame(dis.getGameManager().getBoard(), dis);
            }
        }


    }

    public void drawNumbers(final Cell[][] cells) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                {

                    for (int i = 1; i < cells.length; i++) {
                        for (int j = 1; j < cells[i].length; j++) {
                            int count = 0;

                            if (i > 1 && j > 1 && cells[i - 1][j - 1].isMine())
                                count++;

                            Log.d("first 'if' count ", " is " + count);

                            if (j > 1 && cells[i][j - 1].isMine())
                                count++;

                            if (i < cells.length - 1 && j > 1 && cells[i + 1][j - 1].isMine())
                                count++;

                            if (i > 1 && cells[i - 1][j].isMine())
                                count++;

                            if (i < cells.length - 1 && cells[i + 1][j].isMine())
                                count++;

                            if (i > 1 && j < cells[i].length - 1 && cells[i - 1][j + 1].isMine())
                                count++;
                            if (j < cells[i].length - 1 && cells[i][j + 1].isMine())
                                count++;
                            if (i < cells.length - 1 && j < cells[i].length - 1 && cells[i + 1][j + 1].isMine())
                                count++;

                            Log.d("finished adding count ", " is " + count);
                            cells[i][j].setNumber(count);

                            if (cells[i][j].isMine()) {
                                cells[i][j].setNumber(-1);
                            }
                        }
                    }


                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }


    public void reset(int b, Board board, MediaPlayer player) {
        for (int i = 1; i < board.getCells().length; i++) {
            for (int j = 1; j < board.getCells()[i].length; j++) {
                board.getCells()[i][j] = new Cell(b);
            }
        }

        drawBoard(board);
        drawNumbers(board.getCells());
    }

    public void drawBoard(Board board) {
        for (int i = 1; i < board.getCells().length; i++) {

            for (int j = 1; j < board.getCells()[i].length; j++) {
                Log.d("resting the board", "now doing " + i + " loop");
                gameUIActivityInterface.hide(i, j);
                Log.d("hidding it", "number " + i);
            }
        }
    }

    public boolean win(Board board) {

        for (int i = 1; i < board.getCells().length; i++) {

            for (int j = 1; j < board.getCells()[i].length; j++) {

                //checks if there's a number that's covered
                if (!board.getCells()[i][j].isMine() && !board.getCells()[i][j].isFinished())
                    return false;

                //checks if there's a mine that's not flagged
                if (board.getCells()[i][j].isMine() && !board.getCells()[i][j].isFlagged())
                    return false;

                gameUIActivityInterface.getButnMatrix()[i][j].setClickable(false);
            }
        }
        return true;
    }

    public void moveToHighScores(GameUIActivity dis) {
        dis.getUser().setTime(String.valueOf(dis.getTimer().getText()));
        //Sorting the game based on difficulty:
        //this is the general one. just sending the game without concideration of gameD
        SharedPreferences specialScorePrefrence = dis.getSharedPreferences("GENERAL_FINAL_SCORE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSpecial = specialScorePrefrence.edit();
        String ssss = dis.getUser().playerToString();
        System.out.println(ssss + " is ssss");
        editorSpecial.putString("testLastScore", ssss);
        editorSpecial.apply();

        switch (dis.getUser().getIntGameD()) {

            case 21:
                //Beginner
                SharedPreferences FinalBeginnerPreferences = dis.getSharedPreferences("BEGINNER_FINAL_SCORE", Context.MODE_PRIVATE);
                SharedPreferences.Editor finalBegEditor = FinalBeginnerPreferences.edit();
                finalBegEditor.putString("testLastScore", dis.getUser().playerToString());
                finalBegEditor.apply();


                break;

            case 30:
                SharedPreferences FinalExpertPreferences = dis.getSharedPreferences("EXPERT_FINAL_SCORE", Context.MODE_PRIVATE);
                SharedPreferences.Editor finalExpEditor = FinalExpertPreferences.edit();
                finalExpEditor.putString("testLastScore", dis.getUser().playerToString());
                finalExpEditor.apply();

                break;

            case 50:

                SharedPreferences FinalMasterPreferences = dis.getSharedPreferences("MASTER_FINAL_SCORE", Context.MODE_PRIVATE);
                SharedPreferences.Editor finalMasEditor = FinalMasterPreferences.edit();
                finalMasEditor.putString("testLastScore", dis.getUser().playerToString());
                finalMasEditor.apply();
                break;

            case -1:
                SharedPreferences customPreferences = dis.getSharedPreferences("CUSTOM", 0);
                SharedPreferences.Editor editorCustom = customPreferences.edit();
                editorCustom.putString("lastScore", dis.getUser().toString());

                SharedPreferences FinalCustomPreferences = dis.getSharedPreferences("CUSTOM_FINAL_SCORE", Context.MODE_PRIVATE);
                SharedPreferences.Editor finalCusEditor = FinalCustomPreferences.edit();
                finalCusEditor.putString("testLastScore", dis.getUser().playerToString());
                finalCusEditor.apply();


                editorCustom.apply();
                break;

        }


        Intent intent = new Intent(dis.getApplicationContext(), HighScores.class);
        dis.startActivity(intent);
    }

    public void openBoard(Cell currentCell, int i, int j, Board board) {

        if (i < 9 && j < 9 && i > 0 && j > 0) {

            if (currentCell.isFinished()) {
                return;
            }

            if (currentCell.getNumber() != 0 && currentCell.getNumber() != -1) {//it is a number that's not 0
                gameUIActivityInterface.reveal(i, j);
                return;
            }

            if (currentCell.isMine()) {
                return;
            } else {// it isn't a bomb.
                if (currentCell.getNumber() == 0) {
                    gameUIActivityInterface.reveal(i, j);

                    if (i - 1 != 0)
                        openBoard(board.getCells()[i - 1][j], i - 1, j, board);

                    if (i + 1 != 9)
                        openBoard(board.getCells()[i + 1][j], i + 1, j, board);

                    if (j - 1 != 0)
                        openBoard(board.getCells()[i][j - 1], i, j - 1, board);

                    if (j + 1 != 9)
                        openBoard(board.getCells()[i][j + 1], i, j + 1, board);

                    if (i - 1 != 0 && j - 1 != 0)
                        openBoard(board.getCells()[i - 1][j - 1], i - 1, j - 1, board);

                    if (i + 1 != 9 && j + 1 != 9)
                        openBoard(board.getCells()[i + 1][j + 1], i + 1, j + 1, board);

                    if (i + 1 != 9 && j - 1 != 0)
                        openBoard(board.getCells()[i + 1][j - 1], i + 1, j - 1, board);

                    if (i - 1 != 0 && j + 1 != 9)
                        openBoard(board.getCells()[i - 1][j + 1], i - 1, j + 1, board);

                    return;
                }
            }
        } else {
            return;
        }

    }


    public void lose(Board board) {
        int counter =1;
        for (int i = 1; i < board.getCells().length; i++) {

            for (int j = 1; j < board.getCells()[i].length; j++) {

                if (board.getCells()[i][j].isMine() && board.getCells()[i][j].isFlagged()) {
                    gameUIActivityInterface.reveal(i, j);

                    gameUIActivityInterface.setBackground("checkednoramlbomb","butn"+counter);

                } else if (board.getCells()[i][j].isMine()) {
                    gameUIActivityInterface.reveal(i, j);
                }

                board.getCells()[i][j].setFinished();
                gameUIActivityInterface.getButnMatrix()[i][j].setClickable(false);
                counter++;
            }
        }
    }

    public void openDialog(final GameUIActivity dis, Builder mBuilder) {
        View mView = dis.getLayoutInflater().inflate(R.layout.alert_dialog, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        Button btnNo = (Button) mView.findViewById(R.id.yesAndSaveButn);
        Button buttn = (Button) mView.findViewById(R.id.button1);
        buttn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                dis.setmBatteryCheck(true);
            }
        });

        btnNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //saves and closes the App;
                dis.getGameManager().saveGame(dis);
                dis.publicCloseApp();
            }
        });

        Button btncloseApp = (Button) mView.findViewById(R.id.closeAppButn);
        btncloseApp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                dis.publicCloseApp();
            }
        });


        dialog.show();

    }

    public void finishGame(Board board, GameUIActivity dis) {
        for (int i = 1; i < board.getCells().length; i++) {
            for (int j = 1; j < board.getCells()[i].length; j++) {
                dis.reveal(i, j);
                dis.getButnMatrix()[i][j].setClickable(false);
                board.getCells()[i][j].setFinished();
                gameUIActivityInterface.setBackground("coolsmiley","realButnReset" );
            }
        }
        if (dis.getPlayer().isPlaying())
            dis.getPlayer().stop();

        Log.d("C:Actions, F: FN","just before starting animation");
        dis.startAnim("WIN");
        moveToHighScores(dis);
    }


    public void toggleMusic(MediaPlayer player) {
        if (player.isPlaying())
            player.pause();
        else {
            player.seekTo(0);
            player.start();
        }

    }

}
