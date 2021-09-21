/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.GameUI;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ET_Productions.minesweeper.GeneralGameFiles.BoardFiles.Board;
import com.ET_Productions.minesweeper.GeneralGameFiles.GameManager;
import com.ET_Productions.minesweeper.MainActivity;
import com.ET_Productions.minesweeper.Animations.MyHandler;
import com.ET_Productions.minesweeper.Animations.MyThread;
import com.ET_Productions.minesweeper.GeneralGameFiles.Player;
import com.ET_Productions.minesweeper.GeneralGameFiles.Statistics;
import com.example.minesweeper.R;

import java.io.File;
import java.util.Date;
import android.app.ActionBar;

public class GameUIActivity extends Activity implements GameUIActivityInterface
{

    private TextView playerName;
    private Bundle GB;//Global Bundle
    private MediaPlayer player;
    private Player user;
    private Chronometer timer;
    private GameManager gameManager;
    private Button[][] butnMatrix = new Button[9][9];
    private Drawable [] winAnimArr;
    private Drawable [] loseAnimArr;
    private MyHandler myHandler;
    private MyThread myThread;
    private ImageView myEGIVA;
    private BroadcastReceiver mBatInfoReceiver;
    private boolean mBatteryCheck = false; /** this is the switch whether the player wants to continue playing after reaching 20% or under on his/her battery**/


    public GameUIActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_ui);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setTitle(" ");
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.gray)));
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_USE_LOGO);


        Intent in = getIntent();
        GB = in.getExtras();
        gameManager = new GameManager(this);

        winAnimArr = new Drawable[41];
        loseAnimArr = new Drawable[6];
        myHandler = new MyHandler(this);
        myEGIVA = (ImageView) findViewById(R.id.endGameImageViewAnimator);
        myEGIVA.setClickable(true);
        myEGIVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAnim(v);
            }
        });
        fillArray(winAnimArr, "WIN");
        fillArray(loseAnimArr,"LOSE");


        if (!GB.getString("player name").equals("LoaderIstheName")) {
            gameManager.setFlagMode(false);
            findViewById(R.id.butnflag).setBackgroundResource(R.drawable.unflagtile);

            playerName = (TextView) findViewById(R.id.difficulltyChooser);


            int b = GB.getInt("GameD");
            Log.d("b =", b + " ");
            gameManager.setBoard(new Board(b));

            String playernameString = GB.getString("player name");
            user = new Player(String.valueOf(playernameString));
            user.setMoves(0);
            user.setGameD(b);
            Date d = new Date();
            user.setDate(d.toString());

            Log.d("player name is ", " " + user.getName());

            timer = (Chronometer) findViewById(R.id.chronometer1);
            timer.start();
            user.setTime(String.valueOf(timer.getText()));
            playerName.setText(user.getName());

//            if (user.getName().substring(0, 2).toLowerCase().equals("noa")) ;
//            {
//                 Toast.makeText(getApplicationContext(), "Shoot'em down", Toast.LENGTH_SHORT).show();
//            }
            Button realButnReset = (Button) findViewById(R.id.realButnReset);
            realButnReset.setBackgroundResource(R.drawable.normalsmiley);

            newInitButtons();

            gameManager.getAi().drawBoard(gameManager.getBoard());
            drawBoard(butnMatrix);
            gameManager.getAi().drawNumbers(gameManager.getBoard().getCells());

            player = MediaPlayer.create(this.getApplicationContext(), R.raw.flyingsnails);
            player.setLooping(true);
            player.start();

        } else {
            playerName = (TextView) findViewById(R.id.difficulltyChooser);
            timer = (Chronometer) findViewById(R.id.chronometer1);
            player = MediaPlayer.create(this.getApplicationContext(), R.raw.flyingsnails);
            player.setLooping(true);
            player.start();

            File f = getApplicationContext().getFilesDir();
            try {
                long base = gameManager.loadGameDataFromFile(f, this);
                timer = (Chronometer) findViewById(R.id.chronometer1);
                timer.setBase(base);
                timer.start();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        int a = findNumOfMine();
        Log.d("OnCreate", "Num Of Mines Is" + a);
        user.setFlagNum(a);
        Log.d("OnCreate", "Num Of Mines Is" + user.getFlagNum());

        mBatInfoReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent intent) {
                if(!mBatteryCheck) {
                    int level = 70;
                    try {
                        level = intent.getIntExtra("level", 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (level <= 20) {
                        openDialog();
                    }
                    if (level == 100) {
                        Toast.makeText(GameUIActivity.this, "the battery is 100", Toast.LENGTH_SHORT).show();
                    }
                }
            }};
        viewInstructions();
        Log.d("C: GameUi F: OnCreate","Ready For Game!");
        this.registerReceiver(this.mBatInfoReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveTab:
                saveGame();
                break;
            case R.id.resetTab:
                reset();
                break;
            case R.id.closeTab:
                openDialog();
                break;
            case R.id.ScreenShot:
                String fileName = "Screenshot " + user.getDate();
                File completeFile = gameManager.store(takeScreenshot(), fileName);
                openFile(completeFile);
                break;
            case R.id.statisticsTab:
                Intent stats = new Intent(this, Statistics.class);
                user.setTime(String.valueOf(timer.getText()));
                stats.putExtra("user", user);
                startActivity(stats);
                break;
            case R.id.musicToggleBox:
                toggleMusic();
                break;
            case R.id.winGameTabButn:
                finishGame();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        Log.d("C:GUIA, F:OBP","onBackPressed, just started");
        if (doubleBackToExitPressedOnce) {
            backToMain();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (gameManager.isFlagMode()) {
                        gameManager.setFlagMode(false);
                        Button butnFlag = (Button) findViewById(R.id.butnflag);
                        butnFlag.setBackgroundResource(R.drawable.unflagtile);
                    } else {
                        gameManager.setFlagMode(true);
                        Button butnFlag = (Button) findViewById(R.id.butnflag);
                        butnFlag.setBackgroundResource(R.drawable.realflaggedtile);
                    }
                }

                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (gameManager.isFlagMode()) {
                        gameManager.setFlagMode(false);
                        Button butnFlag = (Button) findViewById(R.id.butnflag);
                        butnFlag.setBackgroundResource(R.drawable.unflagtile);
                    } else {
                        gameManager.setFlagMode(true);
                        Button butnFlag = (Button) findViewById(R.id.butnflag);
                        butnFlag.setBackgroundResource(R.drawable.realflaggedtile);

                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }


    public TextView getPlayerName() {
        return playerName;
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public Player getUser() {
        return user;
    }

    public void setUser(Player tempuser) {
        user = tempuser;

    }

    public Chronometer getTimer() {
        return timer;
    }

    public void setTimer(Chronometer chronometer) {
        timer = chronometer;
    }

    public Bundle getGB() {
        return GB;
    }

    public void setGB(Bundle bundle) {
        GB = bundle;
    }

    public GameUIActivity getThis() {
        return this;
    }

    public void press(View v) {
        user.setMoves(user.getMoves() + 1);
        Vibrator vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        if (!v.isClickable()) {
            Log.d("can Vibrate", " =" + vib.hasVibrator());
            vib.vibrate(300);
            return;
        }
        if (getUser().getName().toLowerCase().equals("wow")) {//just in case.
            MediaPlayer wow = MediaPlayer.create(this, R.raw.wow_owan_wilson);
            wow.setLooping(false);
            wow.seekTo(0);
            wow.start();

        }
        gameManager.getAi().press(v, this);
    }

    public void flagMode(View v) {
        if (gameManager.isFlagMode()) {
            gameManager.setFlagMode(false);
            Button butnFlag = (Button) v;
            butnFlag.setBackgroundResource(R.drawable.unflagtile);
        } else {
            gameManager.setFlagMode(true);
            Button butnFlag = (Button) v;
            butnFlag.setBackgroundResource(R.drawable.realflaggedtile);
        }
    }

    public void reset(View v) {
        reset();
    }


    public void redrawBoard() {
        //Log.d("C:GUIA, F: REDRAWBOARD", "");
        Log.d("C:GUIA, F: REDRAWBOARD", "starting");
        Log.d("redraw Board", " just before initating the buttons");
        newInitButtons();

        Log.d("redraw Board", "finished buttons now going to d player");
        player.pause();
        player.seekTo(0);
        player.start();

        for (int i = 1; i < gameManager.getBoard().getCells().length; i++) {
            for (int j = 1; j < gameManager.getBoard().getCells()[i].length; j++) {
                if (gameManager.getBoard().getCells()[i][j].isFinished()) {
                    Log.d("C:GUIA, F: REDRAWBOARD", "revealing " + i+ "," +j);
                    reveal(i,j);
                } else if (gameManager.getBoard().getCells()[i][j].isFlagged()) {
                    Log.d("C:GUIA, F: REDRAWBOARD", "flagging " + i+ "," +j);
                    flag(i,j);
                } else {
                    butnMatrix[i][j].setBackgroundResource(R.drawable.realunopenedtile);
                }
            }
        }
        Log.d("redraw Board", "finished player");
    }

    private Bitmap takeScreenshot() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        return gameManager.getScreenShot(rootView);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void publicCloseApp() {
        closeApp();
    }

    private void backToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        timer.stop();
        player.stop();
        finish();
    }

    public void reset() {
        int b = GB.getInt("GameD");

        timer = (Chronometer) findViewById(R.id.chronometer1);
        timer.stop();
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        gameManager.getAi().reset(b, gameManager.getBoard(), player);
        resetButnImages();
        Button realButnReset = (Button) findViewById(R.id.realButnReset);
        realButnReset.setBackgroundResource(R.drawable.normalsmiley);

        gameManager.setFlagMode(false);
        findViewById(R.id.butnflag).setBackgroundResource(R.drawable.unflagtile);


        int a = findNumOfMine();
        Log.d("OnCreate", "Num Of Mines Is" + a);
        user.setFlagNum(a);
        Log.d("OnCreate", "Num Of Mines Is" + user.getFlagNum());

        ImageView imgView = (ImageView) findViewById(R.id.endGameImageViewAnimator);
        imgView.setBackgroundResource(0);
        findViewById(R.id.endGameImageViewAnimator).setVisibility(View.GONE);
        this.getUser().setMoves(0);
    }

    private void resetButnImages() {
        for (int i = 1; i < gameManager.getBoard().getCells().length; i++) {
            for (int j = 1; j < gameManager.getBoard().getCells()[i].length; j++) {
                butnMatrix[i][j].setBackgroundResource(R.drawable.realunopenedtile);
                butnMatrix[i][j].setClickable(true);
            }
        }
    }

    public Button[][] getButnMatrix() {
        return butnMatrix;
    }

    public void newInitButtons()
    {
        //Log.d("C: gmui, F: newInitBtns","");
        Log.d("C: gmui, F: newInitBtns","just started");

        int o = 1;
        String b;
        Resources res = getResources();

        for (int i = 1; i <butnMatrix.length; i++)
        {
            Log.d("C: gmui, F: newInitBtns","in for loop, doing i number " + i );

            for (int j = 1; j <butnMatrix[1].length; j++)
            {
                Log.d("C: gmui, F: newInitBtns","in for loop, doing j number " + j);
                b = "butn" + o;
                butnMatrix[i][j] = (Button) findViewById(res.getIdentifier(b, "id", getPackageName()));
                butnMatrix[i][j].setBackgroundResource((R.drawable.realunopenedtile));
                butnMatrix[i][j].setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    CharSequence tempI = v.getContentDescription();
                    //Log.d("C:Board F:Press",);
                    Log.d("C:Board F:Press", "tempI IS " + tempI);
                    String stirngI = String.valueOf(tempI.charAt(2));
                    int i = Integer.valueOf(stirngI);
                    Log.d("C:Board F:Press", "tempI IS " + tempI + " and i = " + i);
                    String stringJ = String.valueOf(tempI.charAt(6));
                    int j = Integer.valueOf(stringJ);
                    Log.d("C:Board F:Press", "tempI IS " + tempI + " and j = " + j);


                    if (!gameManager.getBoard().getCells()[i][j].isFinished())
                    {
                        if (gameManager.getBoard().getCells()[i][j].isFlagged()) {
                            unflag(i,j);
                            user.setFlagNum(user.getFlagNum() + 1);

                        } else {
                            if (!gameManager.getBoard().getCells()[i][j].isFinished() && user.getFlagNum() > 0) {
                                flag(i,j);
                                user.setFlagNum(user.getFlagNum() - 1);

                            }
                        }
                    }

                    if (gameManager.getAi().win(gameManager.getBoard())) {
                        gameManager.getAi().finishGame(gameManager.getBoard(),getThis());
                        timer.stop();
                    }
                    return true;
                }
            });
                o++;
            }
        }
    }

    private void closeApp() {
        player.stop();
        finish();
        System.exit(1);
    }

    private void saveGame() {
        gameManager.saveGame(this);
    }


    private int findNumOfMine() {
        int mineCounter = 0;
        for (int i = 1; i < gameManager.getBoard().getCells().length; i++) {
            for (int j = 1; j < gameManager.getBoard().getCells()[i].length; j++) {
                if (gameManager.getBoard().getCells()[i][j].isMine()) {
                    mineCounter += 1;
                }
            }
        }

        return mineCounter;
    }

    private void finishGame() {
        timer.stop();
        gameManager.getAi().finishGame(gameManager.getBoard(), getThis());
    }

    private void toggleMusic() {
        gameManager.getAi().toggleMusic(player);
    }

    private void openFile(File completeFile) {
        Intent intent = new Intent();
        Uri uri = Uri.fromFile(completeFile);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }


    private void openDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameUIActivity.this);
        gameManager.getAi().openDialog(this, mBuilder);
    }

    private void viewInstructions()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameUIActivity.this);
        View mView = this.getLayoutInflater().inflate(R.layout.instructions, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    public void reveal(int i, int j){
        Button b = butnMatrix[i][j];
         switch (gameManager.getBoard().getCells()[i][j].getNumber()) {
                case 1:
                    b.setBackgroundResource(R.drawable.number1);
                    break;
                case 2:
                    b.setBackgroundResource(R.drawable.number2);
                    break;
                case 3:
                    b.setBackgroundResource(R.drawable.number3);
                    break;
                case 4:
                    b.setBackgroundResource(R.drawable.number4);
                    break;
                case 5:
                    b.setBackgroundResource(R.drawable.number5);
                    break;
                case 6:
                    b.setBackgroundResource(R.drawable.number6);
                    break;
                case 0:
                    b.setBackgroundResource(R.drawable.realemptytile);
                    break;
                case 7:
                    b.setBackgroundResource(R.drawable.number7);
                    break;
                case 8:
                    b.setBackgroundResource(R.drawable.number8);
                    break;
                case -1:
                    b.setBackgroundResource(R.drawable.ognormalmine);
            }
        gameManager.getBoard().getCells()[i][j].reveal();
    }

    public void flag(int i, int j)
    {
        butnMatrix[i][j].setBackgroundResource((R.drawable.realflaggedtile));
        gameManager.getBoard().getCells()[i][j].flag();

    }

    public void hide(int i,int j)
    {
        butnMatrix[i][j].setBackgroundResource(R.drawable.realunopenedtile);
        gameManager.getBoard().getCells()[i][j].hide();
    }

    public void unflag(int i, int j)
    {
        butnMatrix[i][j].setBackgroundResource(R.drawable.realunopenedtile);
        gameManager.getBoard().getCells()[i][j].unFlag();
    }
    public void drawBoard(Button [][] butnMatrix)
    {
        for (int i = 1; i < butnMatrix.length; i++)
        {
            for (int j = 1; j < butnMatrix[i].length; j++)
            {
                butnMatrix[i][j].setBackgroundResource(R.drawable.realunopenedtile);
                Log.d("resting the board", "now doing " + i + " loop");
            }
        }
    }

    /***
     *
     * @param res this is the resource you want to display on the view.
     * @param buttonIDStr this is the view you want to change.
     * this method will change the background of a view who's name is given.
     */
    public void setBackground(String res, String buttonIDStr){
        //Log.d("C: GUIA, F: setBakgrnd","");
        Log.d("C: GUIA, F: setBakgrnd","just started");
        Log.d("C: GUIA, F: setBakgrnd","res is " + res + " and thebuttonIDStr is " + buttonIDStr);

        View b = (View) findViewById( getResources().getIdentifier( buttonIDStr,"id",getPackageName() ) );
        Log.d("C: GUIA, F: setBakgrnd","b's id is " + b.getId());

        b.setBackgroundResource( getResources().getIdentifier( res,"drawable",getPackageName() ) );
        Log.d("C: GUIA, F: setBakgrnd","changed the background");

        return;
    }


    private void fillArray(Drawable[] AnimArr, String WINLOSE) {
        for(int i = 1 ; i < AnimArr.length; i++){
          String drawableName = null;
            if(WINLOSE.equals("WIN")) {
                drawableName = "final_firework_frame" + i;
            }
            else{
                drawableName = "explosion_frame_number" + i;
            }
            int mResId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
            AnimArr[i] = getResources().getDrawable(mResId);

        }
    }

    public void startAnim(String WINLOSE){
        //Log.d("C: GUIA, F:startAnim","");
        Log.d("C: GUIA, F:startAnim","just started");
        myEGIVA =(ImageView) findViewById(R.id.endGameImageViewAnimator);
        myEGIVA.setVisibility(View.VISIBLE);
        Log.d("C: GUIA, F:startAnim","setVisibility to visible");
        myEGIVA.bringToFront();
        Log.d("C: GUIA, F:startAnim","brought to front");
        if(myThread==null) {
            myThread = new MyThread(myHandler, WINLOSE);
            myThread.start();
        }


    }

    public void stopAnim(View v)
    {
        myEGIVA.setVisibility(View.GONE);
        myThread.setStop(true);
        myThread= null;
    }


    public void switchFrame(int count, String WINLOSE){
        //Log.d("C: GUIA, F: switchFrame","");
        Log.d("C: GUIA, F: switchFrame","just started");

        if(WINLOSE.equals("WIN")) {
            Log.d("C: GUIA, F: switchFrame","is win and tbe count is " + count);
            Log.d("C: GUIA, F: switchFrame","the background is " + winAnimArr[count]);
            myEGIVA.setImageDrawable(winAnimArr[count]);
        }
        else {
            Log.d("C: GUIA, F: switchFrame","is lose and the count is " + count);
            Log.d("C: GUIA, F: switchFrame","the background is " + winAnimArr[count]);
            myEGIVA.setImageDrawable(loseAnimArr[count]);
        }
        Log.d("C: GUIA, F: switchFrame","finished");

    }

    public void setmBatteryCheck(boolean mBatteryCheck) {
        this.mBatteryCheck = mBatteryCheck;
    }
}

