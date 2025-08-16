package br.com.saulocn.goldenraspberryawards.model;

import br.com.saulocn.goldenraspberryawards.resource.vo.Interval;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQuery(
        name = Movie.FIND_MIN_INTERVAL,
        query = """
                SELECT new br.com.saulocn.goldenraspberryawards.resource.vo.Interval(
                    actualMovie.producer, 
                    (actualMovie.year - (SELECT MAX(lastWinnerMovieBeforeActual.year) FROM Movie lastWinnerMovieBeforeActual WHERE lastWinnerMovieBeforeActual.producer = actualMovie.producer AND lastWinnerMovieBeforeActual.winner = true AND lastWinnerMovieBeforeActual.year < actualMovie.year)),
                    (SELECT MAX(lastWinnerMovieBeforeActual.year) FROM Movie lastWinnerMovieBeforeActual WHERE lastWinnerMovieBeforeActual.producer = actualMovie.producer AND lastWinnerMovieBeforeActual.winner = true AND lastWinnerMovieBeforeActual.year < actualMovie.year), 
                    actualMovie.year
                )
                FROM Movie actualMovie 
                WHERE
                actualMovie.winner = true 
                AND (actualMovie.year - (SELECT MAX(lastWinnerMovieBeforeActual.year) FROM Movie lastWinnerMovieBeforeActual WHERE lastWinnerMovieBeforeActual.producer = actualMovie.producer AND lastWinnerMovieBeforeActual.winner = true AND lastWinnerMovieBeforeActual.year < actualMovie.year)) > 0
                ORDER BY (actualMovie.year - (SELECT MAX(lastWinnerMovieBeforeActual.year) FROM Movie lastWinnerMovieBeforeActual WHERE lastWinnerMovieBeforeActual.producer = actualMovie.producer AND lastWinnerMovieBeforeActual.winner = true AND lastWinnerMovieBeforeActual.year < actualMovie.year)) ASC
                """,
        resultClass = Interval.class
)
@NamedQuery(
        name = Movie.FIND_MAX_INTERVAL,
        query = """
                SELECT new br.com.saulocn.goldenraspberryawards.resource.vo.Interval(
                    actualMovie.producer, 
                    (actualMovie.year - (SELECT MAX(lastWinnerMovieBeforeActual.year) FROM Movie lastWinnerMovieBeforeActual WHERE lastWinnerMovieBeforeActual.producer = actualMovie.producer AND lastWinnerMovieBeforeActual.winner = true AND lastWinnerMovieBeforeActual.year < actualMovie.year)),
                    (SELECT MAX(lastWinnerMovieBeforeActual.year) FROM Movie lastWinnerMovieBeforeActual WHERE lastWinnerMovieBeforeActual.producer = actualMovie.producer AND lastWinnerMovieBeforeActual.winner = true AND lastWinnerMovieBeforeActual.year < actualMovie.year), 
                    actualMovie.year
                )
                FROM Movie actualMovie 
                WHERE
                actualMovie.winner = true 
                AND (actualMovie.year - (SELECT MAX(lastWinnerMovieBeforeActual.year) FROM Movie lastWinnerMovieBeforeActual WHERE lastWinnerMovieBeforeActual.producer = actualMovie.producer AND lastWinnerMovieBeforeActual.winner = true AND lastWinnerMovieBeforeActual.year < actualMovie.year)) > 0
                ORDER BY (actualMovie.year - (SELECT MAX(lastWinnerMovieBeforeActual.year) FROM Movie lastWinnerMovieBeforeActual WHERE lastWinnerMovieBeforeActual.producer = actualMovie.producer AND lastWinnerMovieBeforeActual.winner = true AND lastWinnerMovieBeforeActual.year < actualMovie.year)) DESC
                """,
        resultClass = Interval.class
)
@NamedQuery(
        name = Movie.FIND_ALL_WINNERS,
        query = """
                SELECT m
                FROM Movie m
                WHERE m.winner = true
                ORDER BY m.year DESC
                """,
        resultClass = Interval.class
)
@Table(name = "movie",
        indexes = {
                @Index(name = "idx_producer", columnList = "str_producer"),
                @Index(name = "idx_winner", columnList = "bool_winner")
        })
public class Movie {

    public static final String FIND_ALL_WINNERS = "Movie.findAllWinners";
    public static final String FIND_MIN_INTERVAL = "Movie.findMinInterval";
    public static final String FIND_MAX_INTERVAL = "Movie.findMaxInterval";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "num_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "num_year", nullable = false)
    private int year;
    @Column(name = "str_title", nullable = false)
    private String title;
    @Column(name = "str_studios", nullable = false)
    private String studios;
    @Column(name = "str_producer", nullable = false)
    private String producer;
    @Column(name = "bool_winner", nullable = false)
    private boolean winner;

    public Long getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudios() {
        return studios;
    }

    public void setStudios(String studios) {
        this.studios = studios;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return year == movie.year && winner == movie.winner && Objects.equals(title, movie.title) && Objects.equals(studios, movie.studios) && Objects.equals(producer, movie.producer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, title, studios, producer, winner);
    }

    @Override
    public String toString() {
        return "Movie{" +
               "year=" + year +
               ", title='" + title + '\'' +
               ", studios='" + studios + '\'' +
               ", producer='" + producer + '\'' +
               ", winner=" + winner +
               '}';
    }

    public static List<Movie> build(String[] strings) {
        String[] producers = strings[3].split(",|\\band\\b");
        return Arrays.stream(producers)
                .map(String::trim)
                .filter(producer -> !producer.isBlank())
                .map(producer -> {
                    Movie movie = new Movie();
                    movie.setYear(Integer.parseInt(strings[0]));
                    movie.setTitle(strings[1]);
                    movie.setProducer(producer);
                    movie.setStudios(strings[2]);
                    movie.setWinner("yes".equals(strings[4]));
                    return movie;
                }).toList();
    }
}
