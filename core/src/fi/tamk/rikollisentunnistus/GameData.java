package fi.tamk.rikollisentunnistus;

/**
 * Created by Samu Koivulahti on 25.3.2018.
 */

public class GameData {
    private int points;
    private boolean win;
    private int level;

    public GameData() {
    }

    public int getPoints() {
        System.out.println(points + "getter");
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
        System.out.println(points + "setter");
    }

    public boolean getWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
