package br.com.saulocn.goldenraspberryawards.resource;

import br.com.saulocn.goldenraspberryawards.resource.vo.MovieVO;
import br.com.saulocn.goldenraspberryawards.resource.vo.ProducerIntervals;
import br.com.saulocn.goldenraspberryawards.service.MovieService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import java.util.List;

@Path("movies")
@Produces("application/json")
@Consumes("application/json")
public class MovieResource {

    @Inject
    private MovieService movieService;

    @GET
    public List<MovieVO> listMovies() {
        return movieService.listMovies();
    }

    @GET
    @Path("winners")
    public List<MovieVO> listWinnerMovies() {
        return movieService.listWinners();
    }

    @GET
    @Path("by-producer")
    public List<MovieVO> listMoviesByProducers(@QueryParam("producer") String producer) {
        return movieService.listMoviesByProducers(producer);
    }

    @GET
    @Path("intervals")
    public ProducerIntervals listIntervalsByProducer() {
        return movieService.getIntervalsByProducer();
    }
}
