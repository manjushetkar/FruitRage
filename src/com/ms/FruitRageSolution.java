package com.ms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class FruitRageSolution {
    public static final String PATH_FILE = "input.txt";
    public static final java.lang.String OUTPUT_FILE = "output.txt";
    public static final java.lang.String UTF_8 = "UTF-8";
    public static int row = 0;
    public static int column = 0;
    public static int gscore = Integer.MIN_VALUE;
    public static int depthLimit = 4;

    public static void main(String[] args) throws IOException {
        String input = readInput(PATH_FILE);
        String result[] = input.split("\n", 4);
        Integer size = Integer.parseInt(result[0].trim());
        Integer fruitTypes = Integer.parseInt(result[1].trim());
        float timeRemaining = Float.parseFloat(result[2].trim());
        String cells[] = result[3].split("\n", size);
        int zoo[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            cells[i] = cells[i].trim();
            String s[] = cells[i].split("", size);
            for (int j = 0; j < size; j++) {
                if (s[j].contains("*")) {
                    zoo[i][j] = 10;
                } else {
                    zoo[i][j] = Integer.parseInt(s[j].trim());
                }
            }
        }
        if (timeRemaining < 1 && size > 18) {
            List<ScoreIndex> qwe = evalBoard(zoo);
            fill(zoo, qwe.get(0).row, qwe.get(0).col, zoo[qwe.get(0).row][qwe.get(0).col]);
            gravity(zoo);
            output_print(zoo, qwe.get(0).row, qwe.get(0).col);
        } else {
            if (size > 18)
                depthLimit = 3;
            minimax(zoo, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1, 0);
            fill(zoo, row, column, zoo[row][column]);
            gravity(zoo);
            output_print(zoo, row, column);
        }
    }

    public static List<ScoreIndex> evalBoard(int[][] zoo) {
        List<ScoreIndex> eval = new ArrayList<>();
        int[][] zootemp = new int[zoo.length][zoo.length];
        for (int i = 0; i < zoo.length; i++)
            System.arraycopy(zoo[i], 0, zootemp[i], 0, zoo.length);
        for (int i = 0; i < zootemp.length; i++)
            for (int j = 0; j < zootemp.length; j++) {
                if (zootemp[i][j] != 10) {
                    int cnt;
                    cnt = fill(zootemp, i, j, zootemp[i][j]);
                    int cntsqr = cnt * cnt;
                    eval.add(new ScoreIndex(cntsqr, i, j));

                }
            }
        Collections.sort(eval, new ScoreComparator());
        return eval;
    }

    private static String readInput(String path_file) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(path_file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static int minimax(int[][] zoo, int alpha, int beta, int depth, int player, int Score) throws IOException {

        int cur = 0;
        int tempvalue = 0;
        //boolean [][] checkpos=new boolean[zoo.length][zoo.length];
        int[][] temparr = new int[zoo.length][zoo.length];
        for (int i = 0; i < zoo.length; i++)
            System.arraycopy(zoo[i], 0, temparr[i], 0, zoo.length);
        tempvalue = Score;
        if (depth == depthLimit) {
            if (player == 1)
                return Score + evalMax(zoo);
            else
                return Score;//-evalMax(zoo);
        }
        if (isFull(zoo)) {
            return Score;
        }
        List<ScoreIndex> eval = evalBoard(zoo);
        for (int i = 0; i < eval.size(); i++) {
            int a = eval.get(i).row;
            int b = eval.get(i).col;
            int cnt = fill(zoo, a, b, zoo[a][b]);
            gravity(zoo);
            if (player == 1) {
                Score = Score + (cnt * cnt);
                cur = minimax(zoo, alpha, beta, depth + 1, 2, Score);
                //System.out.println(cur);
                alpha = Math.max(alpha, cur);
                if (depth == 0) {
                    if (gscore < alpha) {
                        row = a;
                        column = b;
                        gscore = alpha;
                        //System.out.println(gscore);
                    }
                }
                if (alpha >= beta) {


                    return alpha;
                }
            } else if (player == 2) {
                Score = Score - (cnt * cnt);
                cur = minimax(zoo, alpha, beta, depth + 1, 1, Score);
                //System.out.println(depth+"+"+cur+"2");
                beta = Math.min(cur, beta);
                if (alpha >= beta) {

                    return beta;
                }
            }
            for (int k = 0; k < zoo.length; k++)
                System.arraycopy(temparr[k], 0, zoo[k], 0, zoo.length);
            Score = tempvalue;
        }
        return player == 1 ? alpha : beta;
    }

    public static char ch(int i) {
        i = i + 65;
        return ((char) i);
    }

    public static int fill(int[][] zoo, int r, int c, int num) {
        int cnt = 1;
        zoo[r][c] = 10;

        if (r < (zoo.length - 1) && zoo[r + 1][c] == num) {
            cnt += fill(zoo, r + 1, c, num);
        }

        if (r > 0 && zoo[r - 1][c] == num) {
            cnt += fill(zoo, r - 1, c, num);
        }
        if (c < (zoo.length - 1) && zoo[r][c + 1] == num) {
            cnt += fill(zoo, r, c + 1, num);
        }
        if (c > 0 && zoo[r][c - 1] == num) {
            cnt += fill(zoo, r, c - 1, num);
        }
        return cnt;
    }

    public static void gravity(int[][] zoo) throws IOException {
        int[][] temp_zoo;
        temp_zoo = new int[zoo.length][zoo.length];
        int temp_i = 0;
        int[] pos = new int[zoo.length];
        for (int i = 0; i < zoo.length; i++) {
            int temp_j = 0;
            for (int[] zoo1 : zoo) {
                if (zoo1[i] == 10) {
                    temp_zoo[temp_j][temp_i] = 10;
                    //System.out.println(temp_zoo[temp_j][temp_i]);
                    temp_j++;
                    pos[i] = temp_j;
                }
            }
            temp_i++;
        }
        for (int i = 0; i < zoo.length; i++) {
            int index = pos[i];
            // System.out.println(index);
            for (int[] zoo1 : zoo) {
                if (zoo1[i] != 10) {
                    temp_zoo[index][i] = zoo1[i];
                    index++;
                }
            }
        }
        for (int i = 0; i < temp_zoo.length; i++) {
            System.arraycopy(temp_zoo[i], 0, zoo[i], 0, temp_zoo.length);
        }
    }

    public static void output_print(int[][] zoo, int row, int col) throws IOException {
        try (PrintWriter writer = new PrintWriter(OUTPUT_FILE, UTF_8)) {
            writer.print(ch(col));
            writer.println(row + 1);
            for (int i = 0; i < zoo[0].length; i++) {
                for (int j = 0; j < zoo[0].length; j++) {
                    if (zoo[i][j] == 10) {
                        writer.print("*");
                    } else {
                        writer.print(zoo[i][j]);
                    }

                }
                writer.println();
            }
        }
    }

    public static boolean isFull(int[][] zoo) {
        for (int[] zoo1 : zoo) {
            for (int j = 0; j < zoo.length; j++) {
                if (zoo1[j] != 10) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int evalMax(int[][] zoo) {
        int q = 0;
        int s = 0;
        int mx = 0;
        for (int i = 0; i < zoo.length; i++)
            for (int j = 0; j < zoo.length; j++)
                if (zoo[i][j] != 10) {
                    q = fill(zoo, i, j, zoo[i][j]);
                    s = q * q;
                    mx = Math.max(mx, s);
                }
        return mx;  //Returns score of max value from the cluster
    }
}