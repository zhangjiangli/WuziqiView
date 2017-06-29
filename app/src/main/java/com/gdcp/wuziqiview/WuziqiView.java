package com.gdcp.wuziqiview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by asus- on 2017/6/8.
 */

public class WuziqiView extends View {
    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;
    private Paint mPaint = new Paint();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float mBiliLineHeight = 3 * 1.0f / 4;
    private boolean mIsWhite = true;
    private ArrayList<Point> mBlackPoints = new ArrayList<>();
    private ArrayList<Point> mWhitePoints = new ArrayList<>();
    private boolean mIsGameOver;
    private boolean mIsWhiteWinner;
    private int MAX_COUNT_IN_LINE = 5;
    private static final String INSTANCE="instance";
    private static final String INSTANCE_GAME_OVER="instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY="instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY="instance_black_array";
    private ArrayList<Point> lastPoint=new ArrayList<>();
    private Match match=new Match();
    private String objectId;




    public WuziqiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;
        int width = (int) (mLineHeight * mBiliLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, width, width, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, width, width, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        checkGameOver();


    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhitePoints);
        boolean blackWin = checkFiveInLine(mBlackPoints);
        if (whiteWin || blackWin) {
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;
            String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point point : points) {
            int x = point.x;
            int y = point.y;
            boolean win = checkHorizontal(x, y, points);
            if (win)return true;
            win = checkVertical(x, y, points);
            if (win)return true;
            win = checkLeftDiagonal(x, y, points);
            if (win)return true;
            win = checkRightDiagonal(x, y, points);
            if (win)return true;
        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        //左
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
//右
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x , y-i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x , y+i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }
    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y+i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y-i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count = 1;

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y-i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y+i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }





    private void drawPiece(Canvas canvas) {
        for (int i = 0; i < mWhitePoints.size(); i++) {
            Point point = mWhitePoints.get(i);
            canvas.drawBitmap(mWhitePiece, (point.x + (1 - mBiliLineHeight) / 2) * mLineHeight,
                    (point.y + (1 - mBiliLineHeight) / 2) * mLineHeight, null);
        }

        for (int i = 0; i < mBlackPoints.size(); i++) {
            Point point = mBlackPoints.get(i);
            canvas.drawBitmap(mBlackPiece, (point.x + (1 - mBiliLineHeight) / 2) * mLineHeight,
                    (point.y + (1 - mBiliLineHeight) / 2) * mLineHeight, null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;
        //横向划线
        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);
        }
        //纵向划线
        for (int i = 0; i < MAX_LINE; i++) {
            int startY = (int) (lineHeight / 2);
            int endY = (int) (w - lineHeight / 2);
            int x = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(x, startY, x, endY, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point point = getVailedPoint(x, y);
            if (mBlackPoints.contains(point) || mWhitePoints.contains(point)) {
                return false;
            }
            if (mIsWhite) {
                mWhitePoints.add(point);
                match.setWhiteArray(mWhitePoints);
                match.setBlackArray(mBlackPoints);
                match.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e!=null){
                            Log.e("AAA",e.toString());
                        }else{
                            Log.e("AAA","update success");
                        }
                    }
                });

            } else {
                mBlackPoints.add(point);
                match.setBlackArray(mBlackPoints);
                match.setWhiteArray(mWhitePoints);
                match.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {

                    }
                });
            }
            lastPoint.add(point);
            invalidate();
           /* mIsWhite = !mIsWhite;*/
            return true;
        }
        return true;
    }


  public void setIsWhite (boolean isWhite){
      mIsWhite=isWhite;
  }


    public void setObjectId(String objectId) {
      /*  BmobQuery<Match> query=new BmobQuery<>();
        query.addQueryKeys("objectId");
        query.findObjects(new FindListener<Match>() {
            @Override
            public void done(List<Match> list, BmobException e) {
                objectId=list.get(0).getObjectId();
            }
        });*/
      this.objectId=objectId;
    }


    private Point getVailedPoint(int x, int y) {
        //?????
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhitePoints);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackPoints);
        return bundle;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            Bundle bundle= (Bundle) state;
            mIsGameOver=bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhitePoints=bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackPoints=bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
        }
        super.onRestoreInstanceState(state);
    }


   public void  updateData(){
       BmobQuery<Match> query=new BmobQuery<>();
       query.addWhereEqualTo("objectId",objectId);
       query.findObjects(new FindListener<Match>() {
           @Override
           public void done(List<Match> list, BmobException e) {
               if (e==null){
                   mBlackPoints.clear();
                   mWhitePoints.clear();
                   mWhitePoints.addAll(list.get(0).getWhiteArray());
                   mBlackPoints.addAll(list.get(0).getBlackArray());
                   invalidate();

               }else{

               }
           }
       });

   }

    public void start(){
        mWhitePoints.clear();;
        mBlackPoints.clear();
        mIsGameOver=false;
        mIsWhiteWinner=false;
        invalidate();
    }

    public void undo() {
        if (!mIsGameOver){
            if (lastPoint!=null&&lastPoint.size()>0){
                if (mWhitePoints.contains(lastPoint.get(lastPoint.size()-1))){
                    mWhitePoints.remove(mWhitePoints.size()-1);
                    lastPoint.remove(lastPoint.size()-1);
                    invalidate();
                }else if(mBlackPoints.contains(lastPoint.get(lastPoint.size()-1))){
                    mBlackPoints.remove(mBlackPoints.size()-1);
                    lastPoint.remove(lastPoint.size()-1);
                    invalidate();
                }

                match.setWhiteArray(mWhitePoints);
                match.setBlackArray(mBlackPoints);
                match.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e!=null){
                            Log.e("AAA",e.toString());
                        }else{
                            Log.e("AAA","update success");
                        }
                    }
                });


            }

        }else {
            Toast.makeText(getContext(),"已经结束了，不可悔棋", Toast.LENGTH_SHORT).show();
        }
    }
}
