package com.example.pianoapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pianoapp.R;
import com.example.pianoapp.model.Key;
import com.example.pianoapp.utils.SoundManager;

import java.util.ArrayList;

public class PianoView extends View {
    public static final int NUMBER_OF_KEYS = 14;

    private ArrayList<Key> whites;
    private ArrayList<Key> blacks;
    private int keyWidth, keyHeight;
    Paint whitePen ,blackPen, yellowPen;

    private SoundManager soundManager;


    public PianoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        whites = new ArrayList<Key>();
        blacks = new ArrayList<Key>();

        whitePen = new Paint();
        whitePen.setColor(Color.WHITE);
        whitePen.setStyle(Paint.Style.FILL);

        blackPen = new Paint();
        blackPen.setColor(Color.BLACK);
        blackPen.setStyle(Paint.Style.FILL);

        yellowPen = new Paint();
        yellowPen.setColor(Color.YELLOW);
        yellowPen.setStyle(Paint.Style.FILL);

        soundManager = SoundManager.getInstance();
        soundManager.init(context);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        keyWidth = w / NUMBER_OF_KEYS;
        keyHeight = h;

        int blackCount = 15;
        // tạo mang phím trắng
        for(int i = 0 ; i < NUMBER_OF_KEYS; i++) {
            int left = i = keyWidth;
            int right = left + keyWidth;

            RectF rect = new RectF(left,0,right,h);
            whites.add(new Key(i+1,rect,false));

            // tạo mảng phím đen
            if(i != 0 && i!=3 && i!=7 && i!=10) {
                rect = new RectF((float)(i-1)*keyWidth + 0.75f*keyWidth,
                            0,
                        (float) i*keyWidth + 0.25f*keyWidth,
                        0.67f*keyHeight
                        );
                blacks.add(new Key(blackCount,rect,false));
                blackCount++;
            }
        }


    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        for(Key k : whites) {
            canvas.drawRect(k.rect,k.down? yellowPen: whitePen);
        }

        // Vẽ đuonường thẳng đẻ phân biệt các phím trắng
        for(int i = 1; i < NUMBER_OF_KEYS; i++) {
            canvas.drawLine(i+keyWidth,0,i+keyWidth,keyHeight,blackPen);
        }

        for(Key k : blacks) {
            canvas.drawRect(k.rect,k.down? yellowPen: blackPen);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean isDownAction = action == MotionEvent.ACTION_DOWN ||
                                action == MotionEvent.ACTION_MOVE;

        for(int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++) {
            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            for(Key k: whites) {
                if(k.rect.contains(x,y)) {
                    k.down = isDownAction;
//                    soundManager.playsound(R.raw.c3);
                }
            }

            for(Key k: blacks) {
                if(k.rect.contains(x,y)) {

                }
            }


        }
        invalidate();
        return true;

//        return super.onTouchEvent(event);


    }
}
