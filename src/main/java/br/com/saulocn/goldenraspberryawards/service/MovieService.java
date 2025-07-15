package br.com.saulocn.goldenraspberryawards.service;

import br.com.saulocn.goldenraspberryawards.model.Movie;
import br.com.saulocn.goldenraspberryawards.resource.vo.MovieVO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
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
    public Movie saveMovie(Movie movie) {
        entityManager.persist(movie);
        return movie;
    }

    public List<MovieVO> listMoviesByProducers(String producer) {
        return entityManager.createQuery(
                        "SELECT m FROM Movie m WHERE m.producers LIKE :producer", Movie.class)
                .setParameter("producer", "%" + producer + "%")
                .getResultList().stream().map(MovieVO::fromMovie).toList();
    }

    public List<MovieVO> listWinnersMovies() {
        return entityManager.createQuery(
                        "SELECT m FROM Movie m WHERE m.winner=true", Movie.class)
                .getResultList().stream().map(MovieVO::fromMovie).toList();
    }

    @Transactional
    public void updateMovie(Long id, Movie movie) {
        Optional.of(entityManager.find(Movie.class, id))
                .ifPresent(existingMovie -> {
                    existingMovie.setTitle(movie.getTitle());
                    existingMovie.setProducers(movie.getProducers());
                    existingMovie.setWinner(movie.isWinner());
                    entityManager.merge(existingMovie);
                });
    }

    public Optional<Movie> getById(Long id) {
        return Optional.ofNullable(entityManager.find(Movie.class, id));
    }

    @Transactional
    public void delete(Long id) {
        Optional.of(entityManager.find(Movie.class, id))
                .ifPresent(movie -> {
                    entityManager.remove(movie);
                });
    }
}
