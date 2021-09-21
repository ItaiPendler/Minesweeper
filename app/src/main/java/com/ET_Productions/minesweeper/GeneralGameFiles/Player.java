/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.GeneralGameFiles;

import java.io.Serializable;

public class Player implements Serializable {

    private String name;
    private int moves;
    private int timeInMilisecondsToEnd;
    private int flagNum;
    private String gameD;
    private String time;
    private String date;

    public Player(String name) {
        this.name = name;
    }


    public Player(String name, int moves, String gameD, String date, String time) {
        this.name = name;
        this.moves = moves;
        this.gameD = gameD;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", moves=" + moves +
                ", gameD='" + gameD + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getTimeInMilisecondsToEnd() {
        return timeInMilisecondsToEnd;
    }

    public void setTimeInMilisecondsToEnd(int timeInMilisecondsToEnd) {
        this.timeInMilisecondsToEnd = timeInMilisecondsToEnd;
    }

    public int getFlagNum() {
        return flagNum;
    }

    public void setFlagNum(int flagNum) {
        this.flagNum = flagNum;
    }

    public String getGameD() {
        return gameD;
    }

    public void setGameD(int b) {
        switch (b) {
            case 21:
                gameD = "Beginner";
                break;
            case 30:
                gameD = "Expert";
                break;
            case 50:
                gameD = "Master";
                break;
            default:
                gameD = "Custom";
                break;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String d) {
        this.date = d;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String a) {
        this.time = a;
    }

    public int getIntGameD() {
        if (gameD.equals("Beginner"))
            return 21;
        else {
            if (gameD.equals("Expert"))
                return 30;
            if (gameD.equals("Master"))
                return 50;
        }
        return -1;

    }

    public String playerString() {
        return name + " won the game in " + getTime() + " while playing in " + getGameD() + " difficulty." + "\n";
    }

    public String playerToString() {
        String string = name + "#" + getGameD() + "#" + getTime() + "#" + getDate() + "#" + getMoves();
        System.out.println(string);
        return string;
    }

}
