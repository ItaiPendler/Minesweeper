/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.GameUI;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.ET_Productions.minesweeper.GeneralGameFiles.GameManager;
import com.ET_Productions.minesweeper.GeneralGameFiles.Player;

public interface GameUIActivityInterface {

    TextView getPlayerName();

    MediaPlayer getPlayer();

    Player getUser();

    Chronometer getTimer();

    GameUIActivity getThis();

    void press(View v);

    void flagMode(View v);

    void reset();

    void redrawBoard();

    GameManager getGameManager();

    void newInitButtons();

    void reveal(int i, int j);

    void flag(int i, int j);

    void hide(int i, int j);

    void unflag(int i, int j);

    void drawBoard(Button[][] butnMatrix);

    void setBackground(String drawableResStr, String buttonIDStr);

    Button[][] getButnMatrix();
}
