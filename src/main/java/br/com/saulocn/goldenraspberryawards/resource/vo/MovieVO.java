package br.com.saulocn.goldenraspberryawards.resource.vo;

import br.com.saulocn.goldenraspberryawards.model.Movie;

public record MovieVO(int year, String title, String producers, boolean winner) {

    public static MovieVO fromMovie(Movie movie) {
        return new MovieVO(
                movie.getYear(),
                movie.getTitle(),
                movie.getProducers(),
                movie.isWinner()
        );
    }

    public Movie toMovie() {
        Movie movie = new Movie();
        movie.setYear(year);
        movie.setTitle(title);
        movie.setProducers(producers);
        movie.setWinner(winner);
        return movie;
    }

    @Override
    public String toString() {
        return "MovieVO{" +
               "year=" + year +
               ", title='" + title + '\'' +
               ", producers='" + producers + '\'' +
               ", winner=" + winner +
               '}';
    }
}
