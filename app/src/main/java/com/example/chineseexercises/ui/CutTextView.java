package com.example.chineseexercises.ui;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by J on 2017/9/6.
 */

public class CutTextView extends RelativeLayout {
    private Context mContext;
    //物件寬
    private int  mContextWidth= 0;
    //物件高
    private int mContextHigh = 0;
    //隨機產生
    private Random random = new Random();
    //不斷產生文字
    private BarrageHandler mHandler;
    //最快速度,最慢速度
    private int fastestSpeed = 6000, slowestSpeed = 7000;
    //滑動的X,Y
    private int tx = 0, ty = 0;
    //顯示的文字
    String structureCode;
    private String[] cutTextArray, cutTextPartArray, structure;
    private int cutTextArraySize = 0;
    private ArrayList<CutTextItem> cutTextItensArray;
    //文字大小
    private int textSizeContext = 50;

    private int[] structureCodeIntArray=new int[13];
    int mode,numberOfFailures,numberOfFailuresMeter;
    String limit;
    //滑過的痕跡
    public SurfaceViewDraw mySurfaceView;
    boolean gameState=false,textShoew=false;
    //每个弹幕产生的间隔时间随机
    int duration = 3000;
    //建構值
    public CutTextView(Context context) {
        this(context, null);
    }

    public CutTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CutTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.cutTextItensArray = new ArrayList<CutTextItem>();
        this.mHandler = new BarrageHandler();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        addSurView();
    }

    public void addCutTextArray(String degree, String[] cutTextArray, String[] cutTextPartArray, String[] structure) {
        cutTextArraySize = cutTextArray.length;
        this.cutTextArray = cutTextArray;
        this.cutTextPartArray = cutTextPartArray;
        this.structure = structure;
        userDegree(degree);
    }

    public void setModeAndLimit(int mode,String limit){
        this.mode=mode;
        this.limit=limit;
    }
    public void start(){
        gameState=true;
    }
    public void stop(){
        gameState=false;
    }

    private void userDegree(String usDegree){
        switch ("1"){
            case "1":
                numberOfFailures=20;
                fastestSpeed=6000;
                slowestSpeed=7000;
                duration=3000;
                break;
            case "2":
                numberOfFailures=15;
                fastestSpeed=5000;
                slowestSpeed=6000;
                duration=2500;
                break;
            case "3":
                numberOfFailures=10;
                fastestSpeed=4000;
                slowestSpeed=5000;
                duration=2000;
                break;
            case "4":
                numberOfFailures=3;
                fastestSpeed=3000;
                slowestSpeed=4000;
                duration=1500;
                break;
            default:
                numberOfFailures=25;
                fastestSpeed=7000;
                slowestSpeed=6000;
                duration=4000;
                break;
        }
    }

    private void produceTextView() {
        if (cutTextArraySize > 0&&gameState) {
            Log.v("mContext", String.format("%s,%s", mContextWidth, mContextHigh));
            //存放TextView的物件
            int random_number = random.nextInt(cutTextArraySize);
            CutTextItem cti = new CutTextItem(mContext, cutTextArray[random_number], cutTextPartArray[random_number], structure[random_number], textSizeContext);
            //彈跳速度隨機範圍
            cti.setSpeed(fastestSpeed + random.nextInt(slowestSpeed - fastestSpeed));
            //設定移動路線
            cti.setPosition(throwOnTheMove());
            //顯示Text動畫
            cti.showTextView();
            //將產生的物件放入Array陣列
            cutTextItensArray.add(cti);
        }
    }
  /*  public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }*/

    /**
     * 当view显示在窗口的时候，回调的visibility等于View.VISIBLE。。当view不显示在窗口时，回调的visibility等于View.GONE
     * <p>
     * 窗口隐藏了，把内容全部清空，防止onPause时候内容停滞
     **/
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
            if (visibility == View.GONE) {
                mHandler.removeMessages(0);
            } else if (visibility == View.VISIBLE) {
                mHandler.sendEmptyMessage(0);
            }
    }

    //判斷手指切割物件
    public String judgmentText(int x, int y) {
        for (int i = 0; i < cutTextItensArray.size(); i++) {
            //是否有在螢幕上
            if (cutTextItensArray.get(i).getAnimationState()) {
                //是否有接觸到
                if (cutTextItensArray.get(i).collisionCheck(x, y)) {
                    //判斷是否被切割過
                    if (!cutTextItensArray.get(i).getCutStatus()) {
                        //代表TextView被切割
                        cutTextItensArray.get(i).setCutStatus();
                        //清除顯示的TextView
                        cutTextItensArray.get(i).removeTextA();
                        //部件代號
                        String structureCode = cutTextItensArray.get(i).getStructureCode();
                        //部件
                        String[] pjArray = cutTextItensArray.get(i).getCtiNamePojArray().split("#");
                        //模式限制
                        //modeLimit(structureCode,pjArray);
                        //顯示部件
                        showPJ(structureCode, pjArray, x, y);
                        String cutName=cutTextItensArray.get(i).ctiName;
                        Log.v("cutTextItensArraySize",cutTextItensArray.get(i).getAnimationState()+","+cutTextItensArray.get(i).collisionCheck(x, y)+","+cutTextItensArray.get(i).getCutStatus()+","+cutName);
                        return cutName;
                    }
                }
            } else {
                cutTextItensArray.remove(i);
            }
        }
        return "false";
    }

    //顯示部件散落的方式
    private void showPJ(String structureCode, String[] pjArray, int startTextX, int startTextY) {
        //彈跳的方式
        /*1.左右、2.上下、5.左上至右下、6.左下至右上、9.包圍字
        3.左中右、4.上中下、7.上一下二、8.左一右二
        10.特殊 11.上二下一*/
        if (pjArray != null) {
            //佈件散落的路線
            int[][] partPath = new int[pjArray.length][6];
            switch (structureCode) {
                case "0":
                    partPath[0] = lowerLeftCurve(startTextX, startTextY);
                    break;
                //左右結構
                case "1":
                    partPath[0] = lowerLeftCurve(startTextX, startTextY);
                    partPath[1] = lowerRightCurve(startTextX, startTextY);
                    break;
                //上下結構
                case "2":
                    partPath[0] = lowerLeftCurve(startTextX, startTextY);
                    partPath[1] = lowerRightCurve(startTextX, startTextY + (textSizeContext / 2));
                    break;
                //左中右結構
                case "3":
                    partPath[0] = lowerLeftCurve(startTextX, startTextY);
                    partPath[1] = middleCurve(startTextX + (textSizeContext / 3), startTextY);
                    partPath[2] = lowerRightCurve(startTextX + (textSizeContext / 3 * 2), startTextY);
                    break;
                //上中下結構
                case "4":
                    partPath[0] = lowerLeftCurve(startTextX, startTextY);
                    partPath[1] = middleCurve(startTextX, startTextY + (textSizeContext / 3));
                    partPath[2] = lowerRightCurve(startTextX, startTextY + (textSizeContext / 3 * 2));
                    break;
                //左上至右下結構
                case "5":
                    partPath[0] = lowerLeftCurve(startTextX, startTextY);
                    partPath[1] = lowerRightCurve(startTextX, startTextY);
                    break;
                //左下至右上結構
                case "6":
                    partPath[0] = lowerLeftCurve(startTextX, startTextY);
                    partPath[1] = lowerRightCurve(startTextX, startTextY);
                    break;
                //上一下二結構
                case "7":
                    partPath[0] = middleCurve(startTextX, startTextY);
                    partPath[1] = lowerLeftCurve(startTextX, startTextY + (textSizeContext));
                    partPath[2] = lowerRightCurve(startTextX + (textSizeContext), startTextY + (textSizeContext));
                    break;
                //左一右二結構
                case "8":
                    partPath[0] = lowerLeftCurve(startTextX, startTextY);
                    partPath[1] = lowerRightCurve(startTextX + (textSizeContext), startTextY);
                    partPath[2] = lowerRightCurve(startTextX + (textSizeContext), startTextY + (textSizeContext));
                    break;
                //包圍字結構
                case "9":
                    partPath[0] = lowerLeftCurve(startTextX, startTextY);
                    partPath[1] = lowerRightCurve(startTextX, startTextY);
                    break;
                //特殊結構
                case "10":
                    int[][] randomPath = new int[partPath.length][6];
                    int randomPathStarX = startTextX;
                    for (int i = 0; i < randomPath.length; i++) {
                        switch (random.nextInt(3)) {
                            case 0:
                                randomPath[i] = lowerLeftCurve(randomPathStarX, startTextY);
                                break;
                            case 1:
                                randomPath[i] = middleCurve(randomPathStarX, startTextY);
                                break;
                            case 2:
                                randomPath[i] = lowerRightCurve(randomPathStarX, startTextY);
                                break;
                        }
                        randomPathStarX += textSizeContext;
                    }
                    for (int i = 0; i < partPath.length; i++) {
                        partPath[i] = randomPath[i];
                    }
                    break;
                //上二下一
                case "11":
                    partPath[0] = lowerLeftCurve(startTextX, startTextY);
                    partPath[1] = lowerRightCurve(startTextX + (textSizeContext), startTextY);
                    partPath[2] = middleCurve(startTextX, startTextY + (textSizeContext));
                    break;
                default:

                    break;
            }
            //顯示部件
            for (int i = 0; i < pjArray.length; i++) {
                //存放TextView的物件
                CutTextItem cti = new CutTextItem(mContext, pjArray[i], null, null, 25);
                //彈跳速度
                cti.setSpeed(3000);
                //設定移動路線
                cti.setPosition(partPath[i]);
                //顯示Text動畫
                cti.showTextView();
            }
        }
    }

    //上拋曲線
    private int[] throwOnTheMove() {
        int[] throwOnTheMove = new int[6];
        //1起點值
        throwOnTheMove[0] = mContextWidth-textSizeContext;
        throwOnTheMove[1] = mContextHigh;
        //1終點值
        throwOnTheMove[4] = 0;
        throwOnTheMove[5] = mContextHigh;
        //1中間值
        throwOnTheMove[2] = mContextWidth / 2;
        throwOnTheMove[3] = -(mContextHigh / 2);
        return throwOnTheMove;
    }

    //左下曲線
    private int[] lowerLeftCurve(int startTextX, int startTextY) {
        int[] lowerLeftCurve = new int[6];
        //1起點值
        lowerLeftCurve[0] = startTextX;
        lowerLeftCurve[1] = startTextY;
        //1終點值
        lowerLeftCurve[4] = startTextX;
        lowerLeftCurve[5] = mContextHigh;
        //1中間值
        lowerLeftCurve[2] = startTextX / 2;
        lowerLeftCurve[3] = startTextY + ((mContextHigh - startTextY) / 2);
        return lowerLeftCurve;
    }

    //中下曲線
    private int[] middleCurve(int startTextX, int startTextY) {
        int[] middleCurve = new int[6];
        //1起點值
        middleCurve[0] = startTextX;
        middleCurve[1] = startTextY;
        //1終點值
        middleCurve[4] = startTextX;
        middleCurve[5] = mContextHigh;
        //1中間值
        middleCurve[2] = startTextX;
        middleCurve[3] = startTextY + ((mContextHigh - startTextY) / 2);
        return middleCurve;
    }

    //右下曲線
    private int[] lowerRightCurve(int startTextX, int startTextY) {
        int[] lowerRightCurve = new int[6];
        //起點值x,y
        lowerRightCurve[0] = startTextX;
        lowerRightCurve[1] = startTextY;
        //終點值x,y
        lowerRightCurve[4] = lowerRightCurve[0];
        lowerRightCurve[5] = mContextHigh;
        //中間值x,y
        lowerRightCurve[2] = lowerRightCurve[0] + ((mContextWidth - lowerRightCurve[0]) / 2);
        lowerRightCurve[3] = startTextY + ((mContextHigh - startTextY) / 2);
        return lowerRightCurve;
    }
    private void modeLimit(String structureCode,String[] pjArray){
        switch (mode){
            //學習模式
            case 0:
                break;
            //結構限制判斷
            case 1:
                Log.v("學習模式",String.format("%s,%s",Integer.valueOf(structureCode),Integer.valueOf(limit)));
                structureCodeIntArray[Integer.valueOf(structureCode)]++;
                if(structureCodeIntArray[Integer.valueOf(limit)]==numberOfFailures){
                    gameState=false;
                    mHandler.removeMessages(0);
                }
                break;
            //部件限制判斷
            case 2:
                for(String pj:pjArray){
                    if (pj.equals(limit)){
                        numberOfFailuresMeter++;
                        break;
                    }
                }
                if (numberOfFailuresMeter==numberOfFailures){
                    gameState=false;
                    mHandler.removeMessages(0);
                }
                break;
        }
    }
    public boolean getGameState(){
        return gameState;
    }

    class CutTextItem {
        private Context cutTextContext;
        private String ctiName, ctiNamePoj, structureCode;
        private int speed, width, length, textX, textY, textSize;
        private Point startPosition, centerPosition, endPosition;
        private TextView ctiTextView;
        private boolean animationState, cutStatus = false;
        private ValueAnimator anim;
        private LayoutParams params;
        CutTextItem(Context mCutTextContext, String textName, String PojString, String structureCode, int textSize) {
            this.cutTextContext = mCutTextContext;
            this.params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            setCtiName(textName);
            setCtiNamePojArray(PojString);
            setTextSize(textSize);
            setStructureCode(structureCode);
        }
        //名字
        void setCtiName(String name) {
            this.ctiName = name;
        }
        //部件
        void setCtiNamePojArray(String CtiNamePojArrayString) {
            this.ctiNamePoj = CtiNamePojArrayString;
        }
        void setStructureCode(String structureCode) {
            this.structureCode = structureCode;
        }
        String getStructureCode() {
            return structureCode;
        }
        //大小
        void setTextSize(int size) {
            this.textSize = size;
        }

        String getCtiNamePojArray() {
            return ctiNamePoj;
        }

        void setSpeed(int speedt) {
            this.speed = speedt;
        }

        //設定起點值
        void setStartPosition(int startPositionX, int startPositionY) {
            this.startPosition = new Point(startPositionX, startPositionY);
        }
        //設定中間值
        void setCenterPosition(int centerPositionX, int centerPositionY) {
            this.centerPosition = new Point(centerPositionX, centerPositionY);
        }
        //設定終點值
        void setEndPosition(int endPositionX, int endPositionY) {
            this.endPosition = new Point(endPositionX, endPositionY);
        }
        void setPosition(int[] positionArray) {
            setStartPosition(positionArray[0], positionArray[1]);
            setCenterPosition(positionArray[2], positionArray[3]);
            setEndPosition(positionArray[4], positionArray[5]);
        }
        void showTextView() {
            //產生一個TextView
            this.ctiTextView = new TextView(cutTextContext);
            this.ctiTextView.setText(ctiName);
            this.ctiTextView.setTextSize(textSize);
            //顯示TextView
            //params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            addView(ctiTextView, params);
            bringChildToFront(this.ctiTextView);
            //requestLayout();
            //執行動畫
            startBeizerAnimation();
        }

        private void startBeizerAnimation() {
            if (startPosition == null || endPosition == null || centerPosition == null)
                return;
            BezierEvaluator bezierEvaluator = new BezierEvaluator(centerPosition);
            anim = ValueAnimator.ofObject(bezierEvaluator, startPosition, endPosition);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Point point = (Point) valueAnimator.getAnimatedValue();
                    textX = point.x;
                    textY = point.y;
                    Log.v("Point_textX",String.valueOf(textX));
                    Log.v("Point_textY",String.valueOf(textY));
                    ctiTextView.setX(textX);
                    ctiTextView.setY(textY);
                    //invalidate();
                }
            });
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    //当动画开始时
                    animationState = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //动画结束时
                    // 动画执行完毕，清除动画，删除view，
                    anim.removeAllUpdateListeners();
                    anim.removeAllListeners();
                    anim.cancel();
                    removeView(ctiTextView);
                    animationState = false;
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                    //当动画取消时
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                    //当动画重复时
                }
            });
            anim.setDuration(speed);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.start();
        }
        //碰撞檢測
        int dt, roundHeartX, roundHeartY;
        double touchDistance;
        boolean collisionCheck(int fingerX, int fingerY) {
            dt = textSize / 2;
            //Text中心
            roundHeartX = textX + dt;
            roundHeartY = textY + dt;
            //觸碰距離
            touchDistance = Math.sqrt(Math.pow(fingerX - roundHeartX, 2) + Math.pow(fingerY - roundHeartY, 2));
            if (touchDistance < textSize) {
                return true;
            }
            return false;
        }
        //碰撞狀態
        void setCutStatus() {
            cutStatus = true;
        }
        boolean getCutStatus() {
            return cutStatus;
        }
        void removeTextA() {
            removeView(ctiTextView);
        }
        boolean getAnimationState() {
            return animationState;
        }
        private class BezierEvaluator implements TypeEvaluator<Point> {
            private Point controllPoint;
            public BezierEvaluator(Point controllPoint) {
                this.controllPoint = controllPoint;
            }

            @Override
            public Point evaluate(float t, Point startValue, Point endValue) {
                int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controllPoint.x + t * t * endValue.x);
                int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controllPoint.y + t * t * endValue.y);
                return new Point(x, y);
            }
        }
    }
    private class BarrageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mySurfaceView!=null){
                if(mySurfaceView.surfaceChangedbool){
                    produceTextView();
                }
            }
            this.sendEmptyMessageDelayed(0, duration);
        }
    }
    public void addSurView(){
        if (mContextWidth == 0 && mContextHigh == 0) {
            this.mContextWidth = getMeasuredWidth();
            this.mContextHigh = getMeasuredHeight();
            mySurfaceView = new SurfaceViewDraw(mContext, mContextWidth, mContextHigh);
            this.addView(mySurfaceView);
            //mySurfaceView.setZOrderOnTop(true);
            mySurfaceView.setZOrderMediaOverlay(true);
            this.setOnTouchListener(cutTouch);
        }
    }
    View.OnTouchListener cutTouch=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tx = (int) motionEvent.getX();
                    ty = (int) motionEvent.getY();
                    mySurfaceView.setActionDown(tx, ty);
                    break;
                case MotionEvent.ACTION_MOVE: // 绘制线段
                    tx = (int) motionEvent.getX();
                    ty = (int) motionEvent.getY();
                    mySurfaceView.setActionMove(tx, ty);
                    String a=judgmentText(tx, ty);
                    Log.v("User_answer", a);
                    break;
                case MotionEvent.ACTION_UP:
                    mySurfaceView.setActionUp();
                    break;
            }
            return true;
        }
    };
}