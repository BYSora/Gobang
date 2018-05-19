package com.ytu.android.gobang;

import android.graphics.Point;
import java.util.List;

public class CheckWinner {
    private Point point1, point2;

    public boolean checkFiveInLineWinner(List<Point> points) {
        for (Point point : points) {
            int x = point.x;
            int y = point.y;
            if (check(x, y, points, Constants.HORIZONTAL)) {
                return true;
            }else if(check(x, y, points, Constants.VERTICAL)) {
                return true;
            }else if(check(x, y, points, Constants.LEFT_DIAGONAL)) {
                return true;
            }else if(check(x, y, points, Constants.RIGHT_DIAGONAL)) {
                return true;
            }
        }
        return false;
    }

    private boolean check(int x, int y, List<Point> points, int checkOri) {
        int count = 1;
        for (int i = 1; i < Constants.MAX_COUNT_IN_LINE; i++) {
            switch (checkOri) {
                case Constants.HORIZONTAL:
                    point1 = new Point(x - i, y);
                    break;
                case Constants.VERTICAL:
                    point1 = new Point(x, y - i);
                    break;
                case Constants.LEFT_DIAGONAL:
                    point1 = new Point(x - i, y + i);
                    break;
                case Constants.RIGHT_DIAGONAL:
                    point1 = new Point(x + i, y + i);
                    break;
            }
            if (points.contains(point1)) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < Constants.MAX_COUNT_IN_LINE; i++) {
            switch (checkOri) {
                case Constants.HORIZONTAL:
                    point2 = new Point(x + i, y);
                    break;
                case Constants.VERTICAL:
                    point2 = new Point(x, y + i);
                    break;
                case Constants.LEFT_DIAGONAL:
                    point2 = new Point(x + i, y - i);
                    break;
                case Constants.RIGHT_DIAGONAL:
                    point2 = new Point(x - i, y - i);
                    break;
            }
            if (points.contains(point2)) {
                count++;
            } else {
                break;
            }
        }

        if (count == Constants.MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }
}
