/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.Animations;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ET_Productions.minesweeper.GameUI.GameUIActivity;

public class MyHandler extends Handler {

    public GameUIActivity guia;

    public MyHandler (GameUIActivity gameUIActivity){
        super();
        this.guia = gameUIActivity;
    }

    @Override
    public void handleMessage(Message msg)
    {
//        Log.d("C: MH, F: handleMessage","");
        Log.d("C: MH, F: handleMessage","just started");
        super.handleMessage(msg);
        Log.d("C: MH, F: handleMessage","");
        Bundle data = msg.getData();
        int num = data.getInt("count");
        Log.d("C: MH, F: handleMessage","got int i, it is " + num);
        String WINLOSE = data.getString("WINLOSE");
        Log.d("C: MH, F: handleMessage","got the winlsoe " + WINLOSE);
        guia.switchFrame(num,WINLOSE);
        Log.d("C: MH, F: handleMessage","finished");
    }
}
