package com.ytu.android.gobang;


import android.graphics.Point;
import android.util.Log;

import java.util.List;

public class SimpleBlackAI {
    final private int mBlack = 1,mWhite = 2;
    final private int[][] mChessBoard = new int[15][15];
    final private int INF = 0x3f3f3f3f;

    SimpleBlackAI(List<Point> player, List<Point> AI) {
        for(int i = 0, n = player.size(); i < n; i++) {
            Point black = player.get(i);
            mChessBoard[black.x][black.y] = mBlack;
        }
        for(int i = 0, n = AI.size(); i < n ; i++) {
            Point white = AI.get(i);
            mChessBoard[white.x][white.y] = mWhite;
        }
    }

    private void mcopy(int[][] des, int[][] src) {
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                des[i][j] = src[i][j];
            }
        }
    }

    //电脑进行下棋
    public Point play(){
        int ans = -INF, mi = 0 , mj = 0, f;
        int[][] tmp = new int[15][15];
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(mChessBoard[i][j] == 0) {
                    mcopy(tmp, mChessBoard);
                    tmp[i][j] = mWhite; //电脑下
                    if(check(mWhite,tmp)){
                        f=INF;
                    }else{
                        f=dfs(tmp, ans);
                    }
                    if(f > ans) {
                        ans = f;
                        mi = i;
                        mj = j;
                    }
                }
            }
        }
        //如果程序判断已输，就返回一个不存在的点
        if(mi==0&&mj==0&&ans == -INF){
            mi=-1;
            mj=-1;
            return new Point(mi, mj);
        }
        String msg = String.valueOf(ans);
        Log.i("ans",msg);
        return new Point(mi , mj);
    }

    private int dfs(int[][] A, int alpha) {
        int ans = INF, num;
        int[][] tmp = new int[15][15];
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(A[i][j] == 0) {
                    mcopy(tmp, A);
                    tmp[i][j] = mBlack; //模拟对手下
                    if(check(mBlack,tmp)) {
                        num = -INF;
                    }else {
                        num = h(tmp);
                    }
                    if(num <= alpha) //剪枝
                        return num;
                    ans=Math.min(ans, num);
                }
            }
        }
        return ans;
    }

    //检查是否有五子连珠
    private boolean check(int piece, int tmp[][]) {
        for(int i = 0; i < 15; i++) { //水平方向
            for(int j = 0; j < 11; j++) {
                if(tmp[i][j]==piece&&tmp[i][j+1]==piece&&tmp[i][j+2]==piece
                        &&tmp[i][j+3]==piece&&tmp[i][j+4]==piece) {
                    return true;
                }
            }
        }
        for(int i = 0; i < 11; i++) { //垂直方向
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j]==piece&&tmp[i+1][j]==piece&&tmp[i+2][j]==piece
                        &&tmp[i+3][j]==piece&&tmp[i+4][j]==piece) {
                    return true;
                }
            }
        }
        for(int i = 0; i < 11; i++) { //正斜方向
            for(int j = 0; j < 11; j++) {
                if(tmp[i][j]==piece&&tmp[i+1][j+1]==piece&&tmp[i+2][j+2]==piece
                        &&tmp[i+3][j+3]==piece&&tmp[i+4][j+4]==piece) {
                    return true;
                }
            }
        }
        for(int i = 0; i < 11; i++){
            for(int j = 14; j > 3; j--) { //反斜方向
                if(tmp[i][j]==piece&&tmp[i+1][j-1]==piece&&tmp[i+2][j-2]==piece
                        &&tmp[i+3][j-3]==piece&&tmp[i+4][j-4]==piece) {
                    return true;
                }
            }
        }
        return false;
    }

    private int h(int[][] A) {
        int value = 0;
        int[][] tmp = new int[15][15];
        mcopy(tmp, A);
        //棋谱1：四子连线，端点都在
        for(int i = 0; i < 15; i++) { //水平方向
            for(int j = 0; j < 10; j++) {
                if(tmp[i][j]+tmp[i][j+5]!=2&&tmp[i][j+1]==mWhite&&tmp[i][j+2]==mWhite
                        &&tmp[i][j+3]==mWhite&&tmp[i][j+4]==mWhite) {
                    value += 10000;
                }
                if(tmp[i][j]!=mWhite&&tmp[i][j+1]==mBlack&&tmp[i][j+2]==mBlack
                        &&tmp[i][j+3]==mBlack&&tmp[i][j+4]==mBlack&&tmp[i][j+5]!=mWhite) {
                    value += -10000;
                }
            }
        }
        for(int i = 0; i < 10; i++) { //垂直方向
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j]+tmp[i+5][j]!=2&&tmp[i+1][j]==mWhite&&tmp[i+2][j]==mWhite
                        &&tmp[i+3][j]==mWhite&&tmp[i+4][j]==mWhite) {
                    value += 10000;
                }
                if(tmp[i][j]!=mWhite&&tmp[i+1][j]==mBlack&&tmp[i+2][j]==mBlack
                        &&tmp[i+3][j]==mBlack&&tmp[i+4][j]==mBlack&&tmp[i+5][j]!=mWhite) {
                    value += -10000;
                }
            }
        }
        for(int i = 0; i < 10; i++) { //正斜方向
            for(int j = 0; j < 10; j++) {
                if(tmp[i][j]+tmp[i+5][j+5]!=2&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]==mWhite
                        &&tmp[i+3][j+3]==mWhite&&tmp[i+4][j+4]==mWhite) {
                    value += 10000;
                }
                if(tmp[i][j]!=mWhite&&tmp[i+1][j+1]==mBlack&&tmp[i+2][j+2]==mBlack
                        &&tmp[i+3][j+3]==mBlack&&tmp[i+4][j+4]==mBlack&&tmp[i+5][j+5]!=mWhite) {
                    value += -10000;
                }
            }
        }
        for(int i = 0; i < 10; i++){
            for(int j = 14; j > 4; j--) { //反斜方向
                if(tmp[i][j]+tmp[i+5][j-5]!=2&&tmp[i+1][j-1]==mWhite&&tmp[i+2][j-2]==mWhite
                        &&tmp[i+3][j-3]==mWhite&&tmp[i+4][j-4]==mWhite) {
                    value += 10000;
                }
                if(tmp[i][j]!=mWhite&&tmp[i+1][j-1]==mBlack&&tmp[i+2][j-2]==mBlack
                        &&tmp[i+3][j-3]==mBlack&&tmp[i+4][j-4]==mBlack&&tmp[i+5][j-5]!=mWhite) {
                    value += -10000;
                }
            }
        }
        //出现没有堵住的三子，对己方不利的评价要很高
        for(int i = 0; i < 15; i++) { //水平方向
            for(int j = 0; j < 11; j++) {
                if(tmp[i][j]!=mBlack&&tmp[i][j+1]==mWhite&&tmp[i][j+2]==mWhite
                        &&tmp[i][j+3]==mWhite&&tmp[i][j+4]!=mBlack) {
                    value += 1000;
                }
                if(tmp[i][j]!=mWhite&&tmp[i][j+1]==mBlack&&tmp[i][j+2]==mBlack
                        &&tmp[i][j+3]==mBlack&&tmp[i][j+4]!=mWhite) {
                    value += -1000;
                }
            }
        }
        for(int i = 0; i < 11; i++) { //垂直方向
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j]!=mBlack&&tmp[i+1][j]==mWhite&&tmp[i+2][j]==mWhite
                        &&tmp[i+3][j]==mWhite&&tmp[i+4][j]!=mBlack) {
                    value += 1000;
                }
                if(tmp[i][j]!=mWhite&&tmp[i+1][j]==mBlack&&tmp[i+2][j]==mBlack
                        &&tmp[i+3][j]==mBlack&&tmp[i+4][j]!=mWhite) {
                    value += -1000;
                }
            }
        }
        for(int i = 0; i < 11; i++) { //正斜方向
            for(int j = 0; j < 11; j++) {
                if(tmp[i][j]!=mBlack&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]==mWhite
                        &&tmp[i+3][j+3]==mWhite&&tmp[i+4][j+4]!=mBlack) {
                    value += 1000;
                }
                if(tmp[i][j]!=mWhite&&tmp[i+1][j+1]==mBlack&&tmp[i+2][j+2]==mBlack
                        &&tmp[i+3][j+3]==mBlack&&tmp[i+4][j+4]!=mWhite) {
                    value += -1000;
                }
            }
        }
        for(int i = 0; i < 11; i++){
            for(int j = 14; j > 3; j--) { //反斜方向
                if(tmp[i][j]!=mBlack&&tmp[i+1][j-1]==mWhite&&tmp[i+2][j-2]==mWhite
                        &&tmp[i+3][j-3]==mWhite&&tmp[i+4][j-4]!=mBlack) {
                    value += 1000;
                }
                if(tmp[i][j]!=mWhite&&tmp[i+1][j-1]==mBlack&&tmp[i+2][j-2]==mBlack
                        &&tmp[i+3][j-3]==mBlack&&tmp[i+4][j-4]!=mWhite) {
                    value += -1000;
                }
            }
        }
        //出现未堵住的两子，评价次之
        for(int i = 0; i < 15; i++) { //水平方向
            for(int j = 0; j < 12; j++) {
                if(tmp[i][j]!=mBlack&&tmp[i][j+1]==mWhite&&tmp[i][j+2]==mWhite
                        &&tmp[i][j+3]!=mBlack) {
                    value += 100;
                }
                if(tmp[i][j]!=mWhite&&tmp[i][j+1]==mBlack&&tmp[i][j+2]==mBlack
                        &&tmp[i][j+3]!=mWhite) {
                    value += -100;
                }
            }
        }
        for(int i = 0; i < 12; i++) { //垂直方向
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j]!=mBlack&&tmp[i+1][j]==mWhite&&tmp[i+2][j]==mWhite
                        &&tmp[i+3][j]!=mBlack) {
                    value += 100;
                }
                if(tmp[i][j]!=mWhite&&tmp[i+1][j]==mBlack&&tmp[i+2][j]==mBlack
                        &&tmp[i+3][j]!=mWhite) {
                    value += -100;
                }
            }
        }
        for(int i = 0; i < 12; i++) { //正斜方向
            for(int j = 0; j < 12; j++) {
                if(tmp[i][j]!=mBlack&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]==mWhite
                        &&tmp[i+3][j+3]!=mBlack) {
                    value += 100;
                }
                if(tmp[i][j]!=mWhite&&tmp[i+1][j+1]==mBlack&&tmp[i+2][j+2]==mBlack
                        &&tmp[i+3][j+3]!=mWhite) {
                    value += -100;
                }
            }
        }
        for(int i = 0; i < 12; i++){ //反斜方向
            for(int j = 14; j > 2; j--) {
                if(tmp[i][j]!=mBlack&&tmp[i+1][j-1]==mWhite&&tmp[i+2][j-2]==mWhite
                        &&tmp[i+3][j-3]!=mBlack) {
                    value += 100;
                }
                if(tmp[i][j]!=mWhite&&tmp[i+1][j-1]==mBlack&&tmp[i+2][j-2]==mBlack
                        &&tmp[i+3][j-3]!=mWhite) {
                    value += -100;
                }
            }
        }
        //如果上述情况不能推得有效值，则分别将棋盘填充，判断能成型多少五子连珠来算
        int sum1 = 0;
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j] == 0)
                    tmp[i][j] = mWhite;
            }
        }
        for(int i = 0; i < 15; i++) { //水平方向
            for(int j = 0; j < 11; j++) {
                if(tmp[i][j]==mWhite&&tmp[i][j+1]==mWhite&&tmp[i][j+2]==mWhite
                        &&tmp[i][j+3]==mWhite&&tmp[i][j+4]==mWhite) {
                    sum1++;
                }
            }
        }
        for(int i = 0; i < 11; i++) { //垂直方向
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j]==mWhite&&tmp[i+1][j]==mWhite&&tmp[i+2][j]==mWhite
                        &&tmp[i+3][j]==mWhite&&tmp[i+4][j]==mWhite) {
                    sum1 ++;
                }
            }
        }
        for(int i = 0; i < 11; i++) { //正斜方向
            for(int j = 0; j < 11; j++) {
                if(tmp[i][j]==mWhite&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]==mWhite
                        &&tmp[i+3][j+3]==mWhite&&tmp[i+4][j+4]==mWhite) {
                    sum1 ++;
                }
            }
        }
        for(int i = 0; i < 11; i++){ //反斜方向
            for(int j = 14; j > 3; j--) {
                if(tmp[i][j]==mWhite&&tmp[i+1][j-1]==mWhite&&tmp[i+2][j-2]==mWhite
                        &&tmp[i+3][j-3]==mWhite&&tmp[i+4][j-4]==mWhite) {
                    sum1 ++;
                }
            }
        }
        mcopy(tmp, A);
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j] == 0)
                    tmp[i][j] = mBlack;
            }
        }
        int sum0 = 0;
        for(int i = 0; i < 15; i++) {//水平方向
            for(int j = 0; j < 11; j++) {
                if(tmp[i][j]==mBlack&&tmp[i][j+1]==mBlack&&tmp[i][j+2]==mBlack
                        &&tmp[i][j+3]==mBlack&&tmp[i][j+4]==mBlack) {
                    sum0 ++;
                }
            }
        }
        for(int i = 0; i < 11; i++) { //垂直方向
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j]==mBlack&&tmp[i+1][j]==mBlack&&tmp[i+2][j]==mBlack
                        &&tmp[i+3][j]==mBlack&&tmp[i+4][j]==mBlack) {
                    sum0 ++;
                }
            }
        }
        for(int i = 0; i < 11; i++) { //正斜方向
            for(int j = 0; j < 11; j++) {
                if(tmp[i][j]==mBlack&&tmp[i+1][j+1]==mBlack&&tmp[i+2][j+2]==mBlack
                        &&tmp[i+3][j+3]==mBlack&&tmp[i+4][j+4]==mBlack) {
                    sum0 ++;
                }
            }
        }
        for(int i = 0; i < 11; i++){ //反斜方向
            for(int j = 14; j > 3; j--) {
                if(tmp[i][j]==mBlack&&tmp[i+1][j-1]==mBlack&&tmp[i+2][j-2]==mBlack
                        &&tmp[i+3][j-3]==mBlack&&tmp[i+4][j-4]==mBlack) {
                    sum0 ++;
                }
            }
        }
        return value + sum1 - sum0;
    }
}
