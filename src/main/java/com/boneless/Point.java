package com.boneless;

public class Point {
    private int score;
    private String question;
    private String answer;

    public Point(int score, String question, String answer) {
        this.score = score;
        this.question = question;
        this.answer = answer;
    }

    public int getScore() {
        return score;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}