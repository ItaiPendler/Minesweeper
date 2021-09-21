/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.GeneralGameFiles.BoardFiles;

import android.util.Log;

import java.io.Serializable;

public class Board implements Serializable {

    private static final int ROW_NUM = 8;
    private static final int COLOUMN_NUM = 8;
    private Cell cells[][];


    public Board(int mp)
    {

        cells = new Cell[ROW_NUM + 1][COLOUMN_NUM + 1];
        Log.d("Board", "just made cells");


        for (int i = 1; i < cells.length; i++) {
            for (int j = 1; j < cells[i].length; j++) {
                Cell c = new Cell(mp);

                cells[i][j] = c;
                Log.d("Board", "loop #" + i);
                Log.d("Board", "loop #2 #" + j);


            }
        }

        Log.d("Board", "finished making cells");


    }

    public Board() {
        this.cells = new Cell[ROW_NUM + 1][COLOUMN_NUM + 1];
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    public int arraylength() {
        return this.cells.length;
    }

    public int ROW_NUM() {
        return ROW_NUM + 1;
    }

    public int COLOUMN_NUM() {
        return COLOUMN_NUM + 1;
    }

}
