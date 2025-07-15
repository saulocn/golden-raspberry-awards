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

import java.util.Objects;

@Entity
@NamedQuery(
        name = Movie.FIND_MIN_INTERVAL,
        query = """
                SELECT new br.com.saulocn.goldenraspberryawards.resource.vo.Interval(
                    m.producers, 
                    ABS(m.year - (SELECT MIN(m2.year) FROM Movie m2 WHERE m2.producers = m.producers AND m2.winner = true)),
                    (SELECT MIN(m3.year) FROM Movie m3 WHERE m3.producers = m.producers AND m3.winner = true), 
                    m.year
                )
                FROM Movie m 
                WHERE
                m.winner = true 
                AND ABS(m.year - (SELECT MIN(m4.year) FROM Movie m4 WHERE m4.producers = m.producers AND m4.winner = true)) > 0
                ORDER BY ABS(m.year - (SELECT MIN(m4.year) FROM Movie m4 WHERE m4.producers = m.producers AND m4.winner = true)) ASC
                """,
        resultClass = Interval.class
)
@NamedQuery(
        name = Movie.FIND_MAX_INTERVAL,
        query = """
                SELECT new br.com.saulocn.goldenraspberryawards.resource.vo.Interval(
                    m.producers,
                    ABS(m.year - (SELECT MIN(m2.year) FROM Movie m2 WHERE m2.producers = m.producers AND m2.winner = true)),
                    (SELECT MIN(m3.year) FROM Movie m3 WHERE m3.producers = m.producers AND m3.winner = true), 
                    m.year
                )
                FROM Movie m 
                WHERE
                m.winner = true 
                AND ABS(m.year - (SELECT MIN(m4.year) FROM Movie m4 WHERE m4.producers = m.producers AND m4.winner = true)) > 0
                ORDER BY ABS(m.year - (SELECT MIN(m4.year) FROM Movie m4 WHERE m4.producers = m.producers AND m4.winner = true)) DESC
                """,
        resultClass = Interval.class
)
@Table(name = "movie",
        indexes = {
                @Index(name = "idx_producers", columnList = "str_producers"),
                @Index(name = "idx_winner", columnList = "bool_winner")
        })
public class Movie {

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
    @Column(name = "str_producers", nullable = false)
    private String producers;
    @Column(name = "bool_winner", nullable = false)
    private boolean winner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getProducers() {
        return producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
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
        return year == movie.year && winner == movie.winner && Objects.equals(title, movie.title) && Objects.equals(studios, movie.studios) && Objects.equals(producers, movie.producers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, title, studios, producers, winner);
    }

    @Override
    public String toString() {
        return "Movie{" +
               "year=" + year +
               ", title='" + title + '\'' +
               ", studios='" + studios + '\'' +
               ", producers='" + producers + '\'' +
               ", winner=" + winner +
               '}';
    }

    public static Movie build(String[] strings) {
        Movie movie = new Movie();
        movie.setYear(Integer.parseInt(strings[0]));
        movie.setTitle(strings[1]);
        movie.setStudios(strings[2]);
        movie.setProducers(strings[3]);
        movie.setWinner("yes".equals(strings[4]));
        return movie;
    }
}
