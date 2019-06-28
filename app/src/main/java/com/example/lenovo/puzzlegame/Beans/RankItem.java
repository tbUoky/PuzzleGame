package com.example.lenovo.puzzlegame.Beans;

public class RankItem {
    private String username ;
    private int score;

    public RankItem(String username, int score, int rank_level) {
        this.username = username;
        this.score = score;
        this.rank_level = rank_level;
    }
    public RankItem( ) {
        this.username = "";
        this.score = 0;
        this.rank_level = 0;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank_level() {
        return rank_level;
    }

    @Override
    public String toString() {
        return "RankItem{" +
                "username='" + username + '\'' +
                ", score=" + score +
                ", rank_level=" + rank_level +
                '}';
    }

    public void setRank_level(int rank_level) {
        this.rank_level = rank_level;
    }

    private int rank_level;
}
