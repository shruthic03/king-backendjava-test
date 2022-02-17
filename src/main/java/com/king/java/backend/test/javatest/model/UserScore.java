package com.king.java.backend.test.javatest.model;


public class UserScore implements Comparable<UserScore> {

    private final User user;
    private double score;


    public UserScore(User user, double score) {
        this.user = user;
        this.score = score;
    }


    public UserScore(User user) {
        this.user = user;
    }


    public User getUser() {
        return user;
    }


    public double getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }


    @Override
    public int compareTo(UserScore o) {
        if (this.score > o.score) return -1;
        else if (this.score < o.score) return 1;
        else return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserScore that = (UserScore) o;
        return Double.compare(that.score, score) == 0 && user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }

    @Override
    public String toString() {
        return "UserScore{" +
                "user=" + user +
                ", score=" + score +
                '}';
    }
}
