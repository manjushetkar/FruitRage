package com.ms;

import java.util.Comparator;

public class ScoreComparator implements Comparator<ScoreIndex> {
    public int compare(ScoreIndex s1, ScoreIndex s2) {
        int diff = (s1.getScore() - s2.getScore());
        if (diff == 0)
            return 0;
        if (diff < 0)
            return 1;
        else
            return -1;
    }
}
