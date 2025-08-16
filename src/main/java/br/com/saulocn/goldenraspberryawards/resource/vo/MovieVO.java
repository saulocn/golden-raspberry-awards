package br.com.saulocn.goldenraspberryawards.resource.vo;

import br.com.saulocn.goldenraspberryawards.model.Movie;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record MovieVO(
        Long id,
        @Min(value = 1900, message = "Invalid year, min 1900")
        int year,
        @NotEmpty
        String title,
        @NotEmpty
        String producer,
        boolean winner,
        @NotEmpty
        String studios
) {
    public static MovieVO of(int year, String title, String producer, boolean winner, String studios) {
        return new MovieVO(null, year, title, producer, winner, studios);
    }

    public static MovieVO fromMovie(Movie movie) {
        return new MovieVO(
                movie.getId(),
                movie.getYear(),
                movie.getTitle(),
                movie.getProducer(),
                movie.isWinner(),
                movie.getStudios()
        );
    }

    public Movie toMovie() {
        Movie movie = new Movie();
        movie.setYear(year);
        movie.setTitle(title);
        movie.setProducer(producer);
        movie.setWinner(winner);
        movie.setStudios(studios);
        return movie;
    }
}
