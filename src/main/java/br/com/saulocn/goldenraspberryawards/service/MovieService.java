package br.com.saulocn.goldenraspberryawards.service;

import br.com.saulocn.goldenraspberryawards.model.Movie;
import br.com.saulocn.goldenraspberryawards.resource.vo.Interval;
import br.com.saulocn.goldenraspberryawards.resource.vo.MovieVO;
import br.com.saulocn.goldenraspberryawards.resource.vo.ProducerIntervals;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class MovieService {
    private static final Logger LOGGER = Logger.getLogger("MovieService");

    @Inject
    private EntityManager entityManager;

    public List<MovieVO> listMovies() {
        return entityManager.createQuery("SELECT m FROM Movie m", Movie.class)
                .getResultList().stream().map(MovieVO::fromMovie).toList();
    }

    @Transactional
    public void saveMovie(Movie movie) {
        entityManager.persist(movie);
    }

    public List<MovieVO> listMoviesByProducers(String producer) {
        return entityManager.createQuery(
                        "SELECT m FROM Movie m WHERE m.producers LIKE :producer", Movie.class)
                .setParameter("producer", "%" + producer + "%")
                .getResultList().stream().map(MovieVO::fromMovie).toList();
    }

    public List<MovieVO> listWinners() {
        return entityManager.createQuery(
                        "SELECT m FROM Movie m WHERE m.winner=true", Movie.class)
                .getResultList().stream().map(MovieVO::fromMovie).toList();
    }

    public ProducerIntervals getIntervalsByProducer() {
        LocalDateTime start = LocalDateTime.now();
        List<Interval> minInterval = getMinInterval();
        List<Interval> maxInterval = getMaxInterval();
        LocalDateTime end = LocalDateTime.now();
        LOGGER.info("Tempo de execução: " + Duration.between(start, end).toMillis() + " ms");
        return new ProducerIntervals(
                minInterval.isEmpty() ? null : minInterval.getFirst(),
                maxInterval.isEmpty() ? null : maxInterval.getFirst()
        );
    }

    private List<Interval> getMaxInterval() {
        return entityManager.createNamedQuery(Movie.FIND_MAX_INTERVAL, Interval.class)
                .setMaxResults(1)
                .getResultList();
    }

    private List<Interval> getMinInterval() {
        return entityManager.createNamedQuery(Movie.FIND_MIN_INTERVAL, Interval.class)
                .setMaxResults(1)
                .getResultList();
    }
}
