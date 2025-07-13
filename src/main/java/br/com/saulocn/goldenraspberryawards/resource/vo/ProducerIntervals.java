package br.com.saulocn.goldenraspberryawards.resource.vo;

import java.util.List;

public record ProducerIntervals(List<Interval> min, List<Interval> max) {
}
