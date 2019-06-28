package com.example.lenovo.puzzlegame.Beans;

public class HistoryItem {
    private String  Game_Level ;
    private String Game_Step;

    public String getGame_Level() {
        return Game_Level;
    }

    public void setGame_Level(String game_Level) {
        Game_Level = game_Level;
    }

    public String getGame_Step() {
        return Game_Step;
    }

    public void setGame_Step(String game_Step) {
        Game_Step = game_Step;
    }

    public String getOver_Time() {
        return Over_Time;
    }

    public void setOver_Time(String over_Time) {
        Over_Time = over_Time;
    }

    public String getGame_Time() {
        return Game_Time;
    }

    public void setGame_Time(String game_Time) {
        Game_Time = game_Time;
    }

    @Override
    public String toString() {
        return "HistoryItem{" +
                "Game_Level='" + Game_Level + '\'' +
                ", Game_Step='" + Game_Step + '\'' +
                ", Over_Time='" + Over_Time + '\'' +
                ", Game_Time='" + Game_Time + '\'' +
                '}';
    }

    private String Over_Time;
    private String Game_Time;
    public HistoryItem(String Game_Level,String Game_Step,String Game_Time,String Over_Time){
       this.Game_Level=Game_Level;
       this.Game_Step = Game_Step;
       this.Game_Time=Game_Time;
       this.Over_Time=Over_Time;
    }
}
