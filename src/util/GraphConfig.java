package util;

import bezier.BezierCurve;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GraphConfig {
    private static final String point = "62,122;84,60;117,54;183,75;216,105;210,168;_210,168;228,189;240,201;258,204;282,189;276,174;_276,174;312,174;312,150;297,129;306,108;312,96;_312,96;294,105;276,96;270,57;282,54;296,49;_296,49;294,34;302,28;314,19;331,17;342,36;_342,36;361,24;380,31;380,43;380,58;375,78;_375,78;384,93;393,105;372,99;363,105;375,111;_375,111;380,127;387,123;390,141;387,147;378,150;_378,150;375,168;384,180;396,189;408,192;393,198;_393,198;408,234;384,285;324,318;303,327;297,330;_297,330;309,345;327,354;351,354;366,372;363,381;_363,381;357,372;351,366;342,363;351,378;345,384;_345,384;336,372;330,363;318,357;312,357;306,366;_306,366;303,345;294,336;288,351;282,360;273,351;_273,351;267,363;267,375;270,360;370,407;312,396;_312,396;329,400;328,437;336,424;303,400;282,396;_282,396;300,437;317,443;265,393;253,391;232,393;_232,393;267,384;261,381;264,372;261,366;252,357;_252,357;264,363;255,345;240,336;222,321;216,303;_216,303;210,312;198,312;198,297;186,297;183,306;_183,306;177,309;168,303;162,297;156,288;150,276;_150,276;147,288;159,297;144,300;129,282;123,270;_123,270;120,276;129,285;105,276;102,255;108,240;_108,240;93,249;93,273;78,243;102,207;132,192;_132,192;34,220;81,228;56,216;75,172;135,156;_135,156;117,141;23,151;55,210;26,126;129,117;_129,117;117,108;102,111;84,114;75,117;62,122;";
    private static final int POLINOM_LEVEL = 5;

    public static Map<Integer, List<Point>> getPointsBySix() {
        Map<Integer, List<Point>> pointsByFive = new TreeMap<>();
        int lineId = 0;
        for (String bearingPoints : point.split("_")) {
            List<Point> exactPoints = new ArrayList<>(POLINOM_LEVEL + 1);
            for (String bearingPoint : bearingPoints.split(";")) {
                String[] bearingPointCoordinates = bearingPoint.split(",");
                Point point = new Point(
                        Integer.valueOf(bearingPointCoordinates[0]) + 20,
                        Integer.valueOf(bearingPointCoordinates[1]) + 20);
                exactPoints.add(point);
            }
            pointsByFive.put(lineId, exactPoints);
            lineId++;
        }
        return pointsByFive;
    }
}
