/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.GeneralGameFiles.BoardFiles;

import android.util.Log;

import java.io.Serializable;
import java.util.Random;

public class Cell implements Serializable {

    private boolean isMine, isFlagged, isHidden, isPressed, isFinished;
    private int number;

    public Cell(Cell c)
    {
        isMine = c.isMine();
        isFlagged = c.isFlagged();
        isHidden = c.isHidden();
        isPressed = c.isPressed();
        isFinished = c.isFinished();
        number = c.getNumber();
    }

    public Cell(int d)
    {
        isMine = false;
        isFlagged = false;
        isHidden = true;
        isPressed = false;
        isFinished = false;
        number = 0;
        makeMine(this, d);
    }

    public Cell() {
        isMine = false;
        isFlagged = false;
        isHidden = true;
        isPressed = false;
        isFinished = false;
        number = 0;
    }

    public Cell(boolean isMine, boolean isFlagged, boolean isHidden, boolean isFinished, boolean isPressed, int number) {
        this.isMine = isMine;
        this.isFlagged = isFlagged;
        this.isHidden = isHidden;
        this.isFinished = isFinished;
        this.isPressed = isPressed;
        this.number = number;
        Log.d("number is " + number, "just started to make the cell");
    }

    private static void makeMine(Cell c, int mp) {
        Random r = new Random();
        int p = r.nextInt(100) + 1;
        if (p < mp) {
            c.setMine();
            c.setNumber(-1);
        }
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine() {
        this.isMine = true;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean b) {
        this.isFlagged = b;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean a) {
        isHidden = a;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void unpress() {
        this.isPressed = false;
        this.unFlag();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean b) {
        this.isFinished = b;
    }

    public void setFinished() {
        this.isFinished = true;
    }

    @Override
    public String toString() {
        return "Cell [isMine=" + isMine + ", isFlagged=" + isFlagged
                + ", isHidden=" + isHidden + ", isPressed=" + isPressed
                + ", isFinished=" + isFinished + ", number=" + number + "]";
    }

    public void hide() {
        isHidden = true;
    }

    public void flag() {
        isFlagged = true;
    }

    public void reveal() {
        isHidden = false;
        isFinished = true;
    }

    public void unFlag() {
        this.isFlagged = false;
    }

}
