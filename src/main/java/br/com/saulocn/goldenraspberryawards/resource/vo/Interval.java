package br.com.saulocn.goldenraspberryawards.resource.vo;

import java.util.Objects;

public record Interval(String producer, int interval, int previousWin, int followingWin) {


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Interval interval1 = (Interval) o;
        return interval == interval1.interval && previousWin == interval1.previousWin && followingWin == interval1.followingWin && Objects.equals(producer, interval1.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producer, interval, previousWin, followingWin);
    }
}
