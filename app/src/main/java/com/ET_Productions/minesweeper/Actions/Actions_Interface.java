/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.Actions;

import android.app.AlertDialog.Builder;
import android.media.MediaPlayer;
import android.view.View;

import com.ET_Productions.minesweeper.GeneralGameFiles.BoardFiles.Board;
import com.ET_Productions.minesweeper.GameUI.GameUIActivity;
import com.ET_Productions.minesweeper.GeneralGameFiles.BoardFiles.Cell;

public interface Actions_Interface
{

    void press(View v, GameUIActivity dis);

    void drawNumbers(Cell[][] cells);

    void reset(int b, Board board, MediaPlayer player);

    void drawBoard(Board board);

    boolean win(Board board);

    void finishGame(Board board, GameUIActivity dis);

    void openBoard(Cell currentCell, int i, int j, Board board);

    void lose(Board board);

    void openDialog(final GameUIActivity dis, Builder mBuilder);

    void toggleMusic(MediaPlayer player);

    void moveToHighScores(GameUIActivity dis);
}
