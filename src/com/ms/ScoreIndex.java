package com.ms;

public class ScoreIndex {

    int score;
    int row;
    int col;

    ScoreIndex(int score, int row, int col) {
        this.score = score;
        this.row = row;
        this.col = col;
    }

    public int getScore() {
        return score;
    }
}
