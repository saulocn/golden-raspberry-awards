package br.com.saulocn.goldenraspberryawards.resource.vo;

import br.com.saulocn.goldenraspberryawards.model.Movie;

public record MovieVO(int year, String title, String producers, boolean winner, String studios) {

    public static MovieVO fromMovie(Movie movie) {
        return new MovieVO(
                movie.getYear(),
                movie.getTitle(),
                movie.getProducers(),
                movie.isWinner(),
                movie.getStudios()
        );
    }

    public Movie toMovie() {
        Movie movie = new Movie();
        movie.setYear(year);
        movie.setTitle(title);
        movie.setProducers(producers);
        movie.setWinner(winner);
        movie.setStudios(studios);
        return movie;
    }
}
