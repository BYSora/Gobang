package com.ytu.android.gobang;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


public class ChessBoardView_SimpleWhite extends View {
    // 棋盘的宽度，也是长度
    private int mViewWidth;
    // 棋盘每格的长度
    private float maxLineHeight;
    private Paint paint = new Paint();
    private Paint dot = new Paint();
    // 定义黑白棋子的Bitmap
    private Bitmap mWhitePiece, mBlackPiece, mCurrentPiece;
    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

    // 判断当前落下的棋子是否是白色的
    private boolean mIsWhite = true;
    // 记录黑白棋子位置的列表
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();
    private Point mCurrentPoint = new Point();
    // 游戏是否结束
    private boolean mIsGameOver;
    // 游戏结束，是否是白色方胜利
    private boolean mIsWhiteWinner;

    public ChessBoardView_SimpleWhite(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //初始化
    private void init() {
        paint.setColor(0x88000000);
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置防抖动
        paint.setDither(true);
        //画笔样式为描边
        paint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_b1);
        mCurrentPiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_b1_selected);

        dot.setColor(0x88000000);
        dot.setStrokeWidth(10);
        //由于算法的评价，电脑第一次下棋都会在左上角，所以要手动调整电脑第一次的下法
        Point point = new Point(7,7);
        mBlackArray.add(point);
    }

    //设置控件（当前View）的大小，依父控件大小而定
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //得到父控件的长度与宽度，及父控件对子控件的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);

        //由于棋盘为正方形，因此需要取父控件长宽中的最小值作为该View的边长
        int width = Math.min(widthSize, heightSize);
        //如果父控件没有对子控件的长或宽进行指定，可以直接设置View的边长
        if (widthModel == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightModel == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制棋盘的网格
        drawBoard(canvas);
        // 绘制棋盘的黑白棋子
        drawPieces(canvas);
        // 检查游戏是否结束
        checkGameOver();
    }

    // 检查游戏是否结束
    private void checkGameOver() {
        SharedPreferences read = getContext().getSharedPreferences("chess",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = getContext().getSharedPreferences("chess",Context.MODE_PRIVATE).edit();
        if(mIsGameOver) {
            Toast.makeText(getContext(), "白棋胜利", Toast.LENGTH_SHORT).show();
            int count = read.getInt(Constants.SIMPLEWHITECOUNT, 0);
            count ++;
            editor.putInt(Constants.SIMPLEWHITECOUNT, count);
            editor.commit();
            return;
        }
        CheckWinner checkWinner = new CheckWinner();
        boolean whiteWin = checkWinner.checkFiveInLineWinner(mWhiteArray);
        boolean blackWin = checkWinner.checkFiveInLineWinner(mBlackArray);

        if (whiteWin || blackWin) {
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;

            int count = read.getInt(Constants.SIMPLEWHITECOUNT, 0);
            count ++;
            editor.putInt(Constants.SIMPLEWHITECOUNT, count);
            if(!mIsWhiteWinner) {
                int i = read.getInt(Constants.SIMPLEWHITEAIWIN, 0);
                i ++;
                editor.putInt(Constants.SIMPLEWHITEAIWIN, i);
            }
            editor.commit();

            String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

        }
    }

    // 根据黑白棋子的数组绘制棋子
    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            float left = (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight;
            float top = (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight;
            canvas.drawBitmap(mWhitePiece, left, top, null);
        }

        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackPoint = mBlackArray.get(i);
            float left = (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight;
            float top = (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight;
            canvas.drawBitmap(mBlackPiece, left, top, null);
            if(blackPoint == mCurrentPoint)
                canvas.drawBitmap(mCurrentPiece, left, top, null);
        }
    }

    // 绘制棋盘的网线
    private void drawBoard(Canvas canvas) {
        int w = mViewWidth;
        float lineHeight = maxLineHeight;

        for (int i = 0; i < Constants.MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);

            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, paint);
            canvas.drawLine(y, startX, y, endX, paint);

            canvas.drawPoint((float) 7.5 * lineHeight, (float) 7.5 * lineHeight, dot);
            canvas.drawPoint((float) 3.5 * lineHeight, (float) 3.5 * lineHeight, dot);
            canvas.drawPoint((float) 3.5 * lineHeight, (float) 11.5 * lineHeight, dot);
            canvas.drawPoint((float) 11.5 * lineHeight, (float) 3.5 * lineHeight, dot);
            canvas.drawPoint((float) 11.5 * lineHeight, (float) 11.5 * lineHeight, dot);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        maxLineHeight = mViewWidth * 1.0f / Constants.MAX_LINE;

        //确定棋子的缩放级别，为每格边长的3/4
        int pieceWidth = (int) (maxLineHeight * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
        mCurrentPiece = Bitmap.createScaledBitmap(mCurrentPiece, pieceWidth, pieceWidth, false);
    }

    //重载触摸事件，确定落子位置
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point point = getValidPoint(x, y);
            if (mWhiteArray.contains(point) || mBlackArray.contains(point)) {
                return false;
            }
            if (mIsWhite) {
                mWhiteArray.add(point);
            }
            invalidate();
            mIsWhite = !mIsWhite;
        }else if(action == MotionEvent.ACTION_UP) {

            if (!mIsWhite) {
                SimpleWhiteAI ai = new SimpleWhiteAI(mWhiteArray, mBlackArray);
                Point point1 = ai.play();
                //程序判断已输，直接判负
                if(point1.x==-1) {
                    mIsGameOver = true;
                    mIsWhiteWinner = true;
                    invalidate();
                    return true;
                }
                mBlackArray.add(point1);
                mCurrentPoint = point1;
                mIsWhite = !mIsWhite;
                invalidate();
            }
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        int validX = (int) (x / maxLineHeight);
        int validY = (int) (y / maxLineHeight);

        return new Point(validX, validY);
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";
    private static final String INSTANCE_IS_WHITE = "instance_is_white";

    //保存棋盘状态，保证旋转设备或从后台唤醒时不会丢失数据
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
        bundle.putBoolean(INSTANCE_IS_WHITE, mIsWhite);

        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mIsWhite = bundle.getBoolean(INSTANCE_IS_WHITE);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    // 再来一局
    public void start() {
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver = false;
        mIsWhiteWinner = false;
        mIsWhite = true;
        Point point = new Point(7,7);
        mBlackArray.add(point);
        invalidate();
    }
}
