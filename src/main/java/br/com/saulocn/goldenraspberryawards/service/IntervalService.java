package br.com.saulocn.goldenraspberryawards.service;

import br.com.saulocn.goldenraspberryawards.model.Movie;
import br.com.saulocn.goldenraspberryawards.resource.vo.Interval;
import br.com.saulocn.goldenraspberryawards.resource.vo.ProducerIntervals;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class IntervalService {
    private static final Logger LOGGER = Logger.getLogger("IntervalService");

    @Inject
    private EntityManager entityManager;

    public ProducerIntervals getIntervals() {
        var start = LocalDateTime.now();
        var allMinIntervalList = getMinInterval();
        var minIntervalsList = filterIntervals(allMinIntervalList, allMinIntervalList.getFirst().interval());


        var allMaxIntervalList = getMaxInterval();
        var maxIntervalList = filterIntervals(allMaxIntervalList, allMaxIntervalList.getFirst().interval());


        var end = LocalDateTime.now();
        LOGGER.info("Tempo de execução: " + Duration.between(start, end).toMillis() + " ms");
        return new ProducerIntervals(
                minIntervalsList, maxIntervalList
        );
    }

    private static List<Interval> filterIntervals(List<Interval> allMinIntervalList, int intervalNeeded) {
        return allMinIntervalList
                .stream()
                .filter(intervalVO -> intervalVO.interval() == intervalNeeded)
                .toList();
    }

    private List<Interval> getMaxInterval() {
        return entityManager.createNamedQuery(Movie.FIND_MAX_INTERVAL, Interval.class)
                .getResultList();
    }

    private List<Interval> getMinInterval() {
        return entityManager.createNamedQuery(Movie.FIND_MIN_INTERVAL, Interval.class)
                .getResultList();
    }
}
