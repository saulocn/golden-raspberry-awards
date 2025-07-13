package br.com.saulocn.goldenraspberryawards.resource;

import br.com.saulocn.goldenraspberryawards.resource.vo.MovieVO;
import br.com.saulocn.goldenraspberryawards.service.MovieService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.List;

@Path("movies")
@Produces("application/json")
@Consumes("application/json")
public class MovieResource {

    @Inject
    private MovieService movieService;

    @POST
    @Operation(summary = "Save a new movie")
    @APIResponses(
            @APIResponse(
                    responseCode = "201",
                    description = "Movie created successfully"
            ))
    public Response saveMovie(MovieVO movieVO) {
        movieService.saveMovie(movieVO.toMovie());
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Operation(summary = "Update an existing movie")
    @APIResponses(
            @APIResponse(
                    responseCode = "200",
                    description = "Movie updated successfully"
            ))
    public Response updateMovie(@PathParam("id") Long id, MovieVO movieVO) {
        movieService.updateMovie(id, movieVO.toMovie());
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{id}")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Movie found",
                            content = @org.eclipse.microprofile.openapi.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @org.eclipse.microprofile.openapi.annotations.media.Schema(implementation = MovieVO.class)
                            )
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Movie not found"
                    )})
    @Operation(summary = "Get a movie by ID")
    public Response getById(@PathParam("id") Long id) {
        var movieOptional = movieService.getById(id);
        if (movieOptional.isPresent()) {
            return Response.ok(MovieVO.fromMovie(movieOptional.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete a movie by ID")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "204",
                            description = "Movie deleted successfully"
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Movie not found"
                    )})
    public Response delete(@PathParam("id") Long id) {
        var movieOptional = movieService.getById(id);
        if (movieOptional.isPresent()) {
            movieService.delete(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Operation(summary = "List all movies")
    @APIResponses(
            @APIResponse(
                    responseCode = "200",
                    description = "List of movies retrieved successfully",
                    content = @org.eclipse.microprofile.openapi.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @org.eclipse.microprofile.openapi.annotations.media.Schema(implementation = MovieVO.class)
                    )
            ))
    public List<MovieVO> listMovies() {
        return movieService.listMovies();
    }

    @GET
    @Path("winners")
    @Operation(summary = "List all winning movies")
    @APIResponses(
            @APIResponse(
                    responseCode = "200",
                    description = "List of winning movies retrieved successfully",
                    content = @org.eclipse.microprofile.openapi.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @org.eclipse.microprofile.openapi.annotations.media.Schema(implementation = MovieVO.class)
                    )
            ))
    public List<MovieVO> listWinnersMovies() {
        return movieService.listWinnersMovies();
    }

    @GET
    @Path("by-producer")
    @Operation(summary = "List movies by producer")
    @APIResponses(
            @APIResponse(
                    responseCode = "200",
                    description = "List of movies by producer retrieved successfully",
                    content = @org.eclipse.microprofile.openapi.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @org.eclipse.microprofile.openapi.annotations.media.Schema(implementation = MovieVO.class)
                    )
            ))
    public List<MovieVO> listMoviesByProducers(@QueryParam("producer") String producer) {
        return movieService.listMoviesByProducers(producer);
    }

}
