package com.ytu.android.gobang;

import android.graphics.Point;
import android.util.Log;

import java.util.List;

public class HardWhiteAI {
    final private int mBlack = 1,mWhite = 2;
    final private int[][] mChessBoard = new int[15][15];
    final private int INF = 0x3f3f3f3f;
    private int si = -1,sj = -1;

    HardWhiteAI(List<Point> player, List<Point> AI) {
        for(int i = 0, n = player.size(); i < n; i++) {
            Point black = player.get(i);
            mChessBoard[black.x][black.y] = mWhite;
        }
        for(int i = 0, n = AI.size(); i < n ; i++) {
            Point white = AI.get(i);
            mChessBoard[white.x][white.y] = mBlack;
        }
    }

    private void mcopy(int[][] des, int[][] src) {
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                des[i][j] = src[i][j];
            }
        }
    }

    private boolean around(int[][] tmp, int x, int y) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(x+i>=0&&x+i<=14&&y+j>=0&&y+j<=14) {
                    if(tmp[x+i][y+j]!=0)
                        return true;
                }
            }
        }
        return false;
    }

    //电脑进行下棋
    public Point play(){
        int ans = -INF, mi = 0 , mj = 0, f;
        int[][] tmp = new int[15][15];

        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(mChessBoard[i][j] == 0) {
                    mChessBoard[i][j] = mBlack;
                    if(check(mBlack,mChessBoard))
                        return new Point(i,j);
                    else
                        mChessBoard[i][j] = 0;
                }
            }
        }

        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(mChessBoard[i][j] == 0) {
                    if(around(mChessBoard,i,j)) {
                        mcopy(tmp, mChessBoard);
                        tmp[i][j] = mBlack; //电脑下
                        if(check(mBlack,tmp)){
                            f=INF;
                        }else{
                            f=dfs(tmp, ans);
                        }
                        if(f == INF && check(mBlack,tmp))
                            return new Point(i,j);
                        if(f == -INF && si != -1)
                            return new Point(si,sj);
                        if(f > ans) {
                            ans = f;
                            mi = i;
                            mj = j;
                            String log = "("+mi+","+mj+"):"+ans;
                            Log.i("ans",log);
                        }
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
        Log.i("ans","-------------");
        return new Point(mi , mj);
    }

    private int dfs(int[][] A, int alpha) {
        int ans = INF, num;
        int[][] tmp = new int[15][15];
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(A[i][j] == 0) {
                    if(around(A,i,j)) {
                        mcopy(tmp, A);
                        tmp[i][j] = mWhite; //模拟对手下
                        if(check(mWhite,tmp)) {
                            si = i;
                            sj = j;
                            return -INF;
                        }else {
                            num = play1(tmp, ans);
                        }
                        if(num <= alpha) //剪枝
                            return num;
                        ans=Math.min(ans, num);
                    }
                }
            }
        }
        return ans;
    }

    private int play1(int[][] A, int beta){
        int ans = -INF, f;
        int[][] tmp = new int[15][15];
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(A[i][j] == 0) {
                    if(around(A,i,j)) {
                        mcopy(tmp, A);
                        tmp[i][j] = mBlack; //电脑下
                        if(check(mBlack,tmp)){
                            return INF;
                        }else{
                            f=dfs1(tmp, ans);
                        }
                        if(f >= beta)
                            return f;
                        ans = Math.max(f,ans);
                    }
                }
            }
        }
        return ans;
    }

    private int dfs1(int[][] A, int alpha) {
        int ans = INF, num;
        int[][] tmp = new int[15][15];
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(A[i][j] == 0) {
                    if(around(A,i,j)) {
                        mcopy(tmp, A);
                        tmp[i][j] = mWhite; //模拟对手下
                        if(check(mWhite,tmp)) {
                            return -INF;
                        }else {
                            num = h1(tmp);
                        }
                        if(num <= alpha) //剪枝
                            return num;
                        ans=Math.min(ans, num);
                    }
                }
            }
        }
        return ans;
    }

    //检查是否有五子连珠
    private boolean check(int piece, int tmp[][]) {
        for(int i = 0; i < 15; i++)
            for(int j = 0; j < 15; j++){
                if(tmp[i][j] == piece) {
                    if (i <= 10) {
                        if (tmp[i + 4][j] == piece && tmp[i][j] == piece && tmp[i + 1][j] == piece
                                && tmp[i + 2][j] == piece && tmp[i + 3][j] == piece) {
                            return true;
                        }
                    }
                    if (j <= 10) {
                        if (tmp[i][j + 4] == piece && tmp[i][j] == piece && tmp[i][j + 1] == piece
                                && tmp[i][j + 2] == piece && tmp[i][j + 3] == piece) {
                            return true;
                        }
                    }
                    if (i <= 10 && j <= 10) {
                        if (tmp[i + 4][j + 4] == piece && tmp[i][j] == piece && tmp[i + 1][j + 1] == piece
                                && tmp[i + 2][j + 2] == piece && tmp[i + 3][j + 3] == piece) {
                            return true;
                        }
                    }
                    if (i >= 4 && j <= 10) {
                        if (tmp[i - 4][j + 4] == piece && tmp[i][j] == piece && tmp[i - 1][j + 1] == piece
                                && tmp[i - 2][j + 2] == piece && tmp[i - 3][j + 3] == piece) {
                            return true;
                        }
                    }
                }
            }
        return false;
    }

    private int h(int[][] A) {
        boolean three = false, four = false;
        int value = 0;
        int[][] tmp = new int[15][15];
        mcopy(tmp, A);
        //棋谱1：四子连线，端点都在
        for(int i = 0; i < 15; i++) { //水平方向
            for(int j = 0; j < 10; j++) {
                if(tmp[i][j]+tmp[i][j+5]!=4&&tmp[i][j+1]==mBlack&&tmp[i][j+2]==mBlack
                        &&tmp[i][j+3]==mBlack&&tmp[i][j+4]==mBlack) {
                    value += 20000;
                }
                if(tmp[i][j]!=mBlack&&tmp[i][j+1]==mWhite&&tmp[i][j+2]==mWhite
                        &&tmp[i][j+3]==mWhite&&tmp[i][j+4]==mWhite&&tmp[i][j+5]!=mBlack) {
                    value += -10000;
                }

                if(tmp[i][j]+tmp[i][j+5]!=2&&tmp[i][j+1]==mWhite&&tmp[i][j+2]==mWhite
                        &&tmp[i][j+3]==mWhite&&tmp[i][j+4]==mWhite) {
                    four = true;
                }

                if(tmp[i][j]+tmp[i][j+5]!=4&&tmp[i][j+1]==mBlack&&tmp[i][j+2]==0
                        &&tmp[i][j+3]==mBlack&&tmp[i][j+4]==mBlack) {
                    value += 100;
                }
                if(tmp[i][j]+tmp[i][j+5]!=4&&tmp[i][j+1]==mBlack&&tmp[i][j+2]==mBlack
                        &&tmp[i][j+3]==0&&tmp[i][j+4]==mBlack) {
                    value += 100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i][j+1]==mWhite&&tmp[i][j+2]==0
                        &&tmp[i][j+3]==mWhite&&tmp[i][j+4]==mWhite&&tmp[i][j+5]!=mBlack) {
                    value += -100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i][j+1]==mWhite&&tmp[i][j+2]==mWhite
                        &&tmp[i][j+3]==0&&tmp[i][j+4]==mWhite&&tmp[i][j+5]!=mBlack) {
                    value += -100;
                }
            }
        }
        for(int i = 0; i < 10; i++) { //垂直方向
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j]+tmp[i+5][j]!=4&&tmp[i+1][j]==mBlack&&tmp[i+2][j]==mBlack
                        &&tmp[i+3][j]==mBlack&&tmp[i+4][j]==mBlack) {
                    value += 20000;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j]==mWhite&&tmp[i+2][j]==mWhite
                        &&tmp[i+3][j]==mWhite&&tmp[i+4][j]==mWhite&&tmp[i+5][j]!=mBlack) {
                    value += -10000;
                }

                if(tmp[i][j]+tmp[i+5][j]!=2&&tmp[i+1][j]==mWhite&&tmp[i+2][j]==mWhite
                        &&tmp[i+3][j]==mWhite&&tmp[i+4][j]==mWhite) {
                    four = true;
                }

                if(tmp[i][j]+tmp[i+5][j]!=4&&tmp[i+1][j]==mBlack&&tmp[i+2][j]==0
                        &&tmp[i+3][j]==mBlack&&tmp[i+4][j]==mBlack) {
                    value += 100;
                }
                if(tmp[i][j]+tmp[i+5][j]!=4&&tmp[i+1][j]==mBlack&&tmp[i+2][j]==mBlack
                        &&tmp[i+3][j]==0&&tmp[i+4][j]==mBlack) {
                    value += 100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j]==mWhite&&tmp[i+2][j]==0
                        &&tmp[i+3][j]==mWhite&&tmp[i+4][j]==mWhite&&tmp[i+5][j]!=mBlack) {
                    value += -100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j]==mWhite&&tmp[i+2][j]==mWhite
                        &&tmp[i+3][j]==0&&tmp[i+4][j]==mWhite&&tmp[i+5][j]!=mBlack) {
                    value += -100;
                }
            }
        }
        for(int i = 0; i < 10; i++) { //正斜方向
            for(int j = 0; j < 10; j++) {
                if(tmp[i][j]+tmp[i+5][j+5]!=4&&tmp[i+1][j+1]==mBlack&&tmp[i+2][j+2]==mBlack
                        &&tmp[i+3][j+3]==mBlack&&tmp[i+4][j+4]==mBlack) {
                    value += 20000;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]==mWhite
                        &&tmp[i+3][j+3]==mWhite&&tmp[i+4][j+4]==mWhite&&tmp[i+5][j+5]!=mBlack) {
                    value += -10000;
                }

                if(tmp[i][j]+tmp[i+5][j+5]!=2&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]==mWhite
                        &&tmp[i+3][j+3]==mWhite&&tmp[i+4][j+4]==mWhite) {
                    four = true;
                }

                if(tmp[i][j]+tmp[i+5][j+5]!=4&&tmp[i+1][j+1]==mBlack&&tmp[i+2][j+2]==0
                        &&tmp[i+3][j+3]==mBlack&&tmp[i+4][j+4]==mBlack) {
                    value += 100;
                }
                if(tmp[i][j]+tmp[i+5][j+5]!=4&&tmp[i+1][j+1]==mBlack&&tmp[i+2][j+2]==mBlack
                        &&tmp[i+3][j+3]==0&&tmp[i+4][j+4]==mBlack) {
                    value += 100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]==0
                        &&tmp[i+3][j+3]==mWhite&&tmp[i+4][j+4]==mWhite&&tmp[i+5][j+5]!=mBlack) {
                    value += -100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]==mWhite
                        &&tmp[i+3][j+3]==0&&tmp[i+4][j+4]==mWhite&&tmp[i+5][j+5]!=mBlack) {
                    value += -100;
                }
            }
        }
        for(int i = 0; i < 10; i++){
            for(int j = 14; j > 4; j--) { //反斜方向
                if(tmp[i][j]+tmp[i+5][j-5]!=4&&tmp[i+1][j-1]==mBlack&&tmp[i+2][j-2]==mBlack
                        &&tmp[i+3][j-3]==mBlack&&tmp[i+4][j-4]==mBlack) {
                    value += 20000;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j-1]==mWhite&&tmp[i+2][j-2]==mWhite
                        &&tmp[i+3][j-3]==mWhite&&tmp[i+4][j-4]==mWhite&&tmp[i+5][j-5]!=mBlack) {
                    value += -10000;
                }

                if(tmp[i][j]+tmp[i+5][j-5]!=2&&tmp[i+1][j-1]==mWhite&&tmp[i+2][j-2]==mWhite
                        &&tmp[i+3][j-3]==mWhite&&tmp[i+4][j-4]==mWhite) {
                    four = true;
                }

                if(tmp[i][j]+tmp[i+5][j-5]!=4&&tmp[i+1][j-1]==mBlack&&tmp[i+2][j-2]==0
                        &&tmp[i+3][j-3]==mBlack&&tmp[i+4][j-4]==mBlack) {
                    value += 100;
                }
                if(tmp[i][j]+tmp[i+5][j-5]!=4&&tmp[i+1][j-1]==mBlack&&tmp[i+2][j-2]==mBlack
                        &&tmp[i+3][j-3]==0&&tmp[i+4][j-4]==mBlack) {
                    value += 100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j-1]==mWhite&&tmp[i+2][j-2]==0
                        &&tmp[i+3][j-3]==mWhite&&tmp[i+4][j-4]==mWhite&&tmp[i+5][j-5]!=mBlack) {
                    value += -100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j-1]==mWhite&&tmp[i+2][j-2]==mWhite
                        &&tmp[i+3][j-3]==0&&tmp[i+4][j-4]==mWhite&&tmp[i+5][j-5]!=mBlack) {
                    value += -100;
                }
            }
        }
        //出现没有堵住的三子，对己方不利的评价要很高
        for(int i = 0; i < 15; i++) { //水平方向
            for(int j = 0; j < 11; j++) {
                if(tmp[i][j]!=mWhite&&tmp[i][j+1]==mBlack&&tmp[i][j+2]==mBlack
                        &&tmp[i][j+3]==mBlack&&tmp[i][j+4]!=mWhite) {
                    value += 1000;
                }
                if(tmp[i][j]!=mBlack&&tmp[i][j+1]==mWhite&&tmp[i][j+2]==mWhite
                        &&tmp[i][j+3]==mWhite&&tmp[i][j+4]!=mBlack) {
                    value += -1000;
                    if(tmp[i][j]+tmp[i][j+4]==0)
                        three = true;
                }
            }
        }
        for(int i = 0; i < 11; i++) { //垂直方向
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j]!=mWhite&&tmp[i+1][j]==mBlack&&tmp[i+2][j]==mBlack
                        &&tmp[i+3][j]==mBlack&&tmp[i+4][j]!=mWhite) {
                    value += 1000;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j]==mWhite&&tmp[i+2][j]==mWhite
                        &&tmp[i+3][j]==mWhite&&tmp[i+4][j]!=mBlack) {
                    value += -1000;
                    if(tmp[i][j]+tmp[i+4][j]==0)
                        three = true;
                }
            }
        }
        for(int i = 0; i < 11; i++) { //正斜方向
            for(int j = 0; j < 11; j++) {
                if(tmp[i][j]!=mWhite&&tmp[i+1][j+1]==mBlack&&tmp[i+2][j+2]==mBlack
                        &&tmp[i+3][j+3]==mBlack&&tmp[i+4][j+4]!=mWhite) {
                    value += 1000;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]==mWhite
                        &&tmp[i+3][j+3]==mWhite&&tmp[i+4][j+4]!=mBlack) {
                    value += -1000;
                    if(tmp[i][j]+tmp[i+4][j+4]==0)
                        three = true;
                }
            }
        }
        for(int i = 0; i < 11; i++){
            for(int j = 14; j > 3; j--) { //反斜方向
                if(tmp[i][j]!=mWhite&&tmp[i+1][j-1]==mBlack&&tmp[i+2][j-2]==mBlack
                        &&tmp[i+3][j-3]==mBlack&&tmp[i+4][j-4]!=mWhite) {
                    value += 1000;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j-1]==mWhite&&tmp[i+2][j-2]==mWhite
                        &&tmp[i+3][j-3]==mWhite&&tmp[i+4][j-4]!=mBlack) {
                    value += -1000;
                    if(tmp[i][j]+tmp[i+4][j-4]==0)
                        three = true;
                }
            }
        }
        if(four&&three)
            return -INF;
        //出现未堵住的两子，评价次之
        for(int i = 0; i < 15; i++) { //水平方向
            for(int j = 0; j < 12; j++) {
                if(tmp[i][j]!=mWhite&&tmp[i][j+1]==mBlack&&tmp[i][j+2]==mBlack
                        &&tmp[i][j+3]!=mWhite) {
                    value += 100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i][j+1]==mWhite&&tmp[i][j+2]==mWhite
                        &&tmp[i][j+3]!=mBlack) {
                    value += -100;
                }
            }
        }
        for(int i = 0; i < 12; i++) { //垂直方向
            for(int j = 0; j < 15; j++) {
                if(tmp[i][j]!=mWhite&&tmp[i+1][j]==mBlack&&tmp[i+2][j]==mBlack
                        &&tmp[i+3][j]!=mWhite) {
                    value += 100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j]==mWhite&&tmp[i+2][j]==mWhite
                        &&tmp[i+3][j]!=mBlack) {
                    value += -100;
                }
            }
        }
        for(int i = 0; i < 12; i++) { //正斜方向
            for(int j = 0; j < 12; j++) {
                if(tmp[i][j]!=mWhite&&tmp[i+1][j+1]==mBlack&&tmp[i+2][j+2]==mBlack
                        &&tmp[i+3][j+3]!=mWhite) {
                    value += 100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]==mWhite
                        &&tmp[i+3][j+3]!=mBlack) {
                    value += -100;
                }
            }
        }
        for(int i = 0; i < 12; i++){ //反斜方向
            for(int j = 14; j > 2; j--) {
                if(tmp[i][j]!=mWhite&&tmp[i+1][j-1]==mBlack&&tmp[i+2][j-2]==mBlack
                        &&tmp[i+3][j-3]!=mWhite) {
                    value += 100;
                }
                if(tmp[i][j]!=mBlack&&tmp[i+1][j-1]==mWhite&&tmp[i+2][j-2]==mWhite
                        &&tmp[i+3][j-3]!=mBlack) {
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
        return value + sum0 - sum1;
    }

    private int h1(int[][] A) {
        int value = 0;
        int[][] tmp = new int[15][15];
        mcopy(tmp,A);
        for(int i = 1; i <= 13; i++){
            for(int j = 1; j <= 13; j++) {
                if(tmp[i][j] == mBlack) {
                    if (i <= 10) { //水平方向四子
                        if (tmp[i - 1][j] + tmp[i + 4][j] != 4 && tmp[i][j] == mBlack && tmp[i + 1][j] == mBlack
                                && tmp[i + 2][j] == mBlack && tmp[i + 3][j] == mBlack) {
                            value += 20000;
                        }

                        if (tmp[i - 1][j] + tmp[i + 4][j] != 4 && tmp[i][j] == mBlack && tmp[i + 1][j] == 0
                                && tmp[i + 2][j] == mBlack && tmp[i + 3][j] == mBlack) {
                            value += 100;
                        }
                        if (tmp[i - 1][j] + tmp[i + 4][j] != 4 && tmp[i][j] == mBlack && tmp[i + 1][j] == mBlack
                                && tmp[i + 2][j] == 0 && tmp[i + 3][j] == mBlack) {
                            value += 100;
                        }
                    }
                    if (j <= 10) { //垂直方向四子
                        if (tmp[i][j - 1] + tmp[i][j + 4] != 4 && tmp[i][j] == mBlack && tmp[i][j + 1] == mBlack
                                && tmp[i][j + 2] == mBlack && tmp[i][j + 3] == mBlack) {
                            value += 20000;
                        }

                        if (tmp[i][j - 1] + tmp[i][j + 4] != 4 && tmp[i][j] == mBlack && tmp[i][j + 1] == 0
                                && tmp[i][j + 2] == mBlack && tmp[i][j + 3] == mBlack) {
                            value += 100;
                        }
                        if (tmp[i][j - 1] + tmp[i][j + 4] != 4 && tmp[i][j] == mBlack && tmp[i][j + 1] == mBlack
                                && tmp[i][j + 2] == 0 && tmp[i][j + 3] == mBlack) {
                            value += 100;
                        }
                    }
                    if (i <= 10 && j <= 10) { //正斜方向四子
                        if (tmp[i - 1][j - 1] + tmp[i + 4][j + 4] != 4 && tmp[i][j] == mBlack && tmp[i + 1][j + 1] == mBlack
                                && tmp[i + 2][j + 2] == mBlack && tmp[i + 3][j + 3] == mBlack) {
                            value += 20000;
                        }

                        if (tmp[i - 1][j - 1] + tmp[i + 4][j + 4] != 4 && tmp[i][j] == mBlack && tmp[i + 1][j + 1] == 0
                                && tmp[i + 2][j + 2] == mBlack && tmp[i + 3][j + 3] == mBlack) {
                            value += 100;
                        }
                        if (tmp[i - 1][j - 1] + tmp[i + 4][j + 4] != 4 && tmp[i][j] == mBlack && tmp[i + 1][j + 1] == mBlack
                                && tmp[i + 2][j + 2] == 0 && tmp[i + 3][j + 3] == mBlack) {
                            value += 100;
                        }
                    }
                    if (i >= 4 && j <= 10) { //反斜方向四子
                        if (tmp[i + 1][j - 1] + tmp[i - 4][j + 4] != 4 && tmp[i][j] == mBlack && tmp[i - 1][j + 1] == mBlack
                                && tmp[i - 2][j + 2] == mBlack && tmp[i - 3][j + 3] == mBlack) {
                            value += 20000;
                        }

                        if (tmp[i + 1][j - 1] + tmp[i - 4][j + 4] != 4 && tmp[i][j] == mBlack && tmp[i - 1][j + 1] == 0
                                && tmp[i - 2][j + 2] == mBlack && tmp[i - 3][j + 3] == mBlack) {
                            value += 100;
                        }
                        if (tmp[i + 1][j - 1] + tmp[i - 4][j + 4] != 4 && tmp[i][j] == mBlack && tmp[i - 1][j + 1] == mBlack
                                && tmp[i - 2][j + 2] == mBlack && tmp[i - 3][j + 3] == mBlack) {
                            value += 100;
                        }
                    }

                    if (i <= 11) {
                        if (tmp[i - 1][j] != mWhite && tmp[i][j] == mBlack && tmp[i + 1][j] == mBlack
                                && tmp[i + 2][j] == mBlack && tmp[i + 3][j] != mWhite) {
                            value += 2000;
                        }
                    }
                    if (j <= 11) {
                        if (tmp[i][j - 1] != mWhite && tmp[i][j] == mBlack && tmp[i][j + 1] == mBlack
                                && tmp[i][j + 2] == mBlack && tmp[i][j + 3] != mWhite) {
                            value += 2000;
                        }
                    }
                    if (i <= 11 && j <= 11) {
                        if (tmp[i - 1][j - 1] != mWhite && tmp[i][j] == mBlack && tmp[i + 1][j + 1] == mBlack
                                && tmp[i + 2][j + 2] == mBlack && tmp[i + 3][j + 3] != mWhite) {
                            value += 2000;
                        }
                    }
                    if (i >= 3 && j <= 11) {
                        if (tmp[i + 1][j - 1] != mWhite && tmp[i][j] == mBlack && tmp[i - 1][j + 1] == mBlack
                                && tmp[i - 2][j + 2] == mBlack && tmp[i - 3][j + 3] != mWhite) {
                            value += 2000;
                        }
                    }

                    if (i <= 12) {
                        if (tmp[i - 1][j] != mWhite && tmp[i][j] == mBlack && tmp[i + 1][j] == mBlack && tmp[i + 2][j] != mWhite) {
                            value += 200;
                        }
                    }
                    if (j <= 12) {
                        if (tmp[i][j - 1] != mWhite && tmp[i][j] == mBlack && tmp[i][j + 1] == mBlack && tmp[i][j + 2] != mWhite) {
                            value += 200;
                        }
                    }
                    if (i <= 12 && j <= 12) {
                        if (tmp[i - 1][j - 1] != mWhite && tmp[i][j] == mBlack && tmp[i + 1][j + 1] == mBlack && tmp[i + 2][j + 2] != mWhite) {
                            value += 200;
                        }
                    }
                    if (i >= 2 && j <= 12) {
                        if (tmp[i + 1][j - 1] != mWhite && tmp[i][j] == mBlack && tmp[i - 1][j + 1] == mBlack && tmp[i - 2][j + 2] != mWhite) {
                            value += 200;
                        }
                    }
                }else if(tmp[i][j] == mWhite) {
                    if(i <= 10) { //水平方向四子
                        if(tmp[i-1][j]!=mBlack&&tmp[i][j]==mWhite&&tmp[i+1][j]==mWhite
                                &&tmp[i+2][j]==mWhite&&tmp[i+3][j]==mWhite&&tmp[i+4][j]!=mBlack) {
                            value += -10000;
                        }

                        if(tmp[i-1][j]!=mBlack&&tmp[i][j]==mWhite&&tmp[i+1][j]==0
                                &&tmp[i+2][j]==mWhite&&tmp[i+3][j]==mWhite&&tmp[i+4][j]!=mBlack) {
                            value += -100;
                        }
                        if(tmp[i-1][j]!=mBlack&&tmp[i][j]==mWhite&&tmp[i+1][j]==mWhite
                                &&tmp[i+2][j]==0&&tmp[i+3][j]==mWhite&&tmp[i+4][j]!=mBlack) {
                            value += -100;
                        }
                    }
                    if(j <= 10) { //垂直方向四子
                        if(tmp[i][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i][j+1]==mWhite
                                &&tmp[i][j+2]==mWhite&&tmp[i][j+3]==mWhite&&tmp[i][j+4]!=mBlack) {
                            value += -10000;
                        }

                        if(tmp[i][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i][j+1]==0
                                &&tmp[i][j+2]==mWhite&&tmp[i][j+3]==mWhite&&tmp[i][j+4]!=mBlack) {
                            value += -100;
                        }
                        if(tmp[i][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i][j+1]==mWhite
                                &&tmp[i][j+2]==0&&tmp[i][j+3]==mWhite&&tmp[i][j+4]!=mBlack) {
                            value += -100;
                        }
                    }
                    if(i <= 10 && j <= 10) { //正斜方向四子
                        if(tmp[i-1][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i+1][j+1]==mWhite
                                &&tmp[i+2][j+2]==mWhite&&tmp[i+3][j+3]==mWhite&&tmp[i+4][j+4]!=mBlack) {
                            value += -10000;
                        }

                        if(tmp[i-1][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i+1][j+1]==0
                                &&tmp[i+2][j+2]==mWhite&&tmp[i+3][j+3]==mWhite&&tmp[i+4][j+4]!=mBlack) {
                            value += -100;
                        }
                        if(tmp[i-1][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i+1][j+1]==mWhite
                                &&tmp[i+2][j+2]==0&&tmp[i+3][j+3]==mWhite&&tmp[i+4][j+4]!=mBlack) {
                            value += -100;
                        }
                    }
                    if(i >= 4 && j <= 10) { //反斜方向四子
                        if(tmp[i+1][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i-1][j+1]==mWhite
                                &&tmp[i-2][j+2]==mWhite&&tmp[i-3][j+3]==mWhite&&tmp[i-4][j+4]!=mBlack) {
                            value += -10000;
                        }

                        if(tmp[i+1][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i-1][j+1]==0
                                &&tmp[i-2][j+2]==mWhite&&tmp[i-3][j+3]==mWhite&&tmp[i-4][j+4]!=mBlack) {
                            value += -100;
                        }
                        if(tmp[i+1][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i-1][j+1]==mWhite
                                &&tmp[i-2][j+2]==0&&tmp[i-3][j+3]==mWhite&&tmp[i-4][j+4]!=mBlack) {
                            value += -100;
                        }
                    }
                    //四个方向上三子
                    if(i <= 11) {
                        if(tmp[i-1][j]!=mBlack&&tmp[i][j]==mWhite&&tmp[i+1][j]==mWhite
                                &&tmp[i+2][j]==mWhite&&tmp[i+3][j]!=mBlack) {
                            value += -1000;
                        }
                    }
                    if(j <= 11) {
                        if(tmp[i][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i][j+1]==mWhite
                                &&tmp[i][j+2]==mWhite&&tmp[i][j+3]!=mBlack) {
                            value += -1000;
                        }
                    }
                    if(i <= 11 && j <= 11) {
                        if(tmp[i-1][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i+1][j+1]==mWhite
                                &&tmp[i+2][j+2]==mWhite&&tmp[i+3][j+3]!=mBlack) {
                            value += -1000;
                        }
                    }
                    if(i >= 3 && j <= 11) {
                        if(tmp[i+1][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i-1][j+1]==mWhite
                                &&tmp[i-2][j+2]==mWhite&&tmp[i-3][j+3]!=mBlack) {
                            value += -1000;
                        }
                    }
                    //四个方向上两字
                    if(i <= 12) {
                        if(tmp[i-1][j]!=mBlack&&tmp[i][j]==mWhite&&tmp[i+1][j]==mWhite&&tmp[i+2][j]!=mBlack) {
                            value += -100;
                        }
                    }
                    if(j <= 12) {
                        if(tmp[i][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i][j+1]==mWhite&&tmp[i][j+2]!=mBlack) {
                            value += -100;
                        }
                    }
                    if(i <= 12 && j <= 12) {
                        if(tmp[i-1][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i+1][j+1]==mWhite&&tmp[i+2][j+2]!=mBlack) {
                            value += -100;
                        }
                    }
                    if(i >= 2&& j <= 12) {
                        if(tmp[i+1][j-1]!=mBlack&&tmp[i][j]==mWhite&&tmp[i-1][j+1]==mWhite&&tmp[i-2][j+2]!=mBlack) {
                            value += -100;
                        }
                    }
                }
            }
        }
        return value;
    }
}
