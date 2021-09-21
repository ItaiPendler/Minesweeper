/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.GeneralGameFiles;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import com.ET_Productions.minesweeper.Actions.Actions;
import com.ET_Productions.minesweeper.Actions.Actions_Interface;
import com.ET_Productions.minesweeper.GameUI.GameUIActivity;
import com.ET_Productions.minesweeper.GeneralGameFiles.BoardFiles.Board;
import com.ET_Productions.minesweeper.GeneralGameFiles.BoardFiles.Cell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class GameManager {

    private boolean flagMode;
    private Board board;
    private Actions_Interface ai;


    public GameManager(GameUIActivity GUIA) {
        ai = new Actions(GUIA);
    }

    public GameManager(boolean tempF, Board tempB) {
        board = tempB;
        flagMode = tempF;
    }


    public boolean isFlagMode() {
        return flagMode;
    }


    public void setFlagMode(boolean flagMode) {
        this.flagMode = flagMode;
    }


    public Board getBoard() {
        return board;
    }


    public void setBoard(Board board) {
        this.board = board;
    }


    public Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }


    public File store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dirPath, fileName + ".png");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            Log.d("Store Function", "Finished Compressing");
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void saveGame(GameUIActivity guia) {
        File f = new File(guia.getApplicationContext().getFilesDir(), "Saves");
        Log.d("the path ", "is " + f.getAbsolutePath());
        saveGameDataToFile(f, guia);
    }


    public long loadGameDataFromFile(File file, GameUIActivity guia) throws ClassNotFoundException {
        //Log.d("GameManager Class, loadGame Function","");
        String b = null;
        long base = 0;
        try {
            FileInputStream fileStream = guia.openFileInput("Saves");
            Log.d("C: GameManager, F:Load", "fileStream is " + fileStream);
            Log.d("C: GameManager, F:Load", "file is " + file);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            Log.d("C: GameManager, F:Load", "objectStream is " + objectStream);

            //			 	Log.d("read file"," String is "+(String) objectStream.readObject());
            String a = (String) objectStream.readObject();
            Log.d("read file", " a is " + a);
            guia.getPlayerName().setText(a);
            Log.d("C: GameManager, F: Load", "user is " + guia.getPlayerName().getText().toString());


            //			 	Log.d("read file","user is " + (Player) objectStream.readObject());
            guia.setUser((Player) objectStream.readObject());
            Log.d("C: GameManager, F: Load", "user is " + guia.getUser().getName());


            //			 	Log.d("read file","");
            Chronometer temp = new Chronometer(guia.getApplicationContext());
            guia.setTimer(temp);
            b = (String) objectStream.readObject();
            int tempMIN = Integer.valueOf(b.substring(0, b.indexOf(':')));
            int tempSEC = Integer.valueOf(b.substring(b.indexOf(':') + 1));
            Log.d("C: GameManager, F: Load", "tempMIN is " + tempMIN);
            Log.d("C: GameManager, F: Load", "tempSEC is " + tempSEC);

            base = SystemClock.elapsedRealtime() - (tempMIN * 60000 + tempSEC * 1000);
            Log.d("C: GameManager, F: Load", "base is " + base);

            Cell[][] savedCells;
            savedCells = (Cell[][]) objectStream.readObject();

            Board tempB = new Board();
            setBoard(tempB);

            remakeBoard(guia, savedCells);

            guia.redrawBoard();

            Integer GameD1gameD = (Integer) objectStream.readObject();
            int GameD = Integer.parseInt(GameD1gameD.toString());
            Log.d("C: GameManager, F: Load", " reading int, gameD is " + GameD);
            //Log.d("GameManager Class, loadGame Function","");


            guia.setGB(new Bundle());
            guia.getGB().putInt("GameD", GameD);
            guia.getGB().putString("player name", guia.getUser().getName());
            Log.d("C: GameManager, F: Load", "Just made the bundle");


            objectStream.close();
        } catch (FileNotFoundException e) {
            Log.d("file NOT Found", " yeah nothing more to say i guess");
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            Log.d("Stream corrupted", " again, not much else to say");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("IO Exception", "Is the reason we didn't save");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.d("NullPointerException", "has occurred");
            e.printStackTrace();
        }
        return base;
    }

    private void saveGameDataToFile(File file, GameUIActivity guia) {

        Log.d("save Game", "before starting try catch block there was this");

        try {
            Log.d("Starting Save", "idk if this will even work, of course it will");

            FileOutputStream fileStream = new FileOutputStream(file);
            Log.d("save Game", "just made fileStream");

            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            Log.d("save Game", "just made ObjectStream");

            objectStream.writeObject(guia.getPlayerName().getText().toString());
            Log.d("i wonder i wonder", "holy moly gwak it worked");


            objectStream.writeObject(guia.getUser());
            Log.d("saved ", "user");
            objectStream.flush();

            guia.getTimer().stop();

            objectStream.writeObject(guia.getTimer().getText());
            guia.getTimer().start();
            guia.getTimer().stop();
            objectStream.flush();


            Cell[][] cells = new Cell[board.arraylength()][board.arraylength()];

            for (int i = 1; i < board.getCells().length; i++) {
                for (int j = 1; j < board.getCells()[i].length; j++) {
                    if (board.getCells()[i][j].isMine() || board.getCells()[i][j].isFinished() || !board.getCells()[i][j].isHidden() || board.getCells()[i][j].isFlagged() || board.getCells()[i][j].getNumber() != 0)
                        cells[i][j] = new Cell(board.getCells()[i][j]);
                    else {
                        cells[i][j] = null;
                    }
                }
            }
            objectStream.writeObject(cells);
            Log.d("saved ", "saved cells");
            objectStream.flush();

            Integer gamed = guia.getGB().getInt("GameD");
            objectStream.writeObject(gamed);
            Log.d("saved ", "gameD");

            objectStream.flush();
            objectStream.close();
            Log.d("save Game", "closed objectStream");

            fileStream.close();
            Log.d("save Game", "just closed FileStream");


            Toast.makeText(guia.getApplicationContext(), "Game Saved!", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            //Toast.makeText(getApplicationContext(), "didn't saved the game because it didn't find the file",  Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            //Toast.makeText(getApplicationContext(), "didn't saved the game because of an io exception",  Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "didn't saved the game",  Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        guia.getTimer().start();
    }

   public Actions_Interface getAi()
    {
        return ai;
    }

    public void setAi(Actions_Interface ai) {
        this.ai = ai;
    }

    private void remakeBoard(GameUIActivity dis, Cell[][] cells) {
        //Log.d("C: GM, F: remakeBoard","");
        Log.d("C: GM, F: remakeBoard", "just started");
        for (int i = 1; i < cells.length; i++) {
            for (int j = 1; j < cells[i].length; j++) {

                Log.d("C: GM, F: remakeBoard", "i = " + i + " and j = " + j);
                if (cells[i][j] != null) {
                    Log.d("C: GM, F: remakeBoard", "Remaking Cell " + i + "," + j);
                    board.getCells()[i][j] = new Cell(cells[i][j]);
                } else {
                    Log.d("C: GM, F: remakeBoard", "Creating Cell " + i + "," + j);
                    board.getCells()[i][j] = new Cell();
                }
            }
        }
    }


}
