package rest;

import domain.Actor;
import domain.Comment;
import domain.Grade;
import domain.Movie;
import domain.services.ActorService;
import domain.services.MovieService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukaszgodlewski on 12.05.2017.
 */


@Path("/movies")
public class MovieResources {

    private MovieService db_movies = new MovieService();
    private ActorService db_actors = new ActorService();
    private static int commentId = 0;
    private static int gradeId = 0;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getAll(){
        return db_movies.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Movie m){
        db_movies.add(m);
        return Response.ok(m.getId()).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id){
        Movie movie = db_movies.get(id);

        if(movie == null){
            return Response.status(404).build();
        }
        return Response.ok(movie).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Movie m){
        Movie movie = db_movies.get(id);
        if(movie == null){
            return Response.status(404).build();
        }
        m.setId(id);
        db_movies.update(m);
        return Response.ok().build();

    }

    @Path("/{id}/comments")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getComments(@PathParam("id") int id){
        Movie m = db_movies.get(id);
        if(m == null){
            return null;
        }
        if(m.getComments() == null){
            m.setComments(new ArrayList<>());
        }
        return m.getComments();
    }

    @Path("/{id}/comments")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("id") int id, Comment comment){
        Movie m = db_movies.get(id);
        if(m == null){
            return Response.status(404).build();
        }
        if(m.getComments() == null){
            m.setComments(new ArrayList<>());
        }

        comment.setId(++commentId);
        m.getComments().add(comment);
        return Response.ok().build();
    }

    @Path("/{movieId}/comments/{commentId}")
    @DELETE
    public Response deleteComment(@PathParam("movieId") int movieId, @PathParam("commentId") int commentId){
        Movie m = db_movies.get(movieId);
        Comment c = null;
        for(Comment comment: m.getComments()){
            if(comment.getId()==commentId){
                c = comment;
                break;
            }
        }
        if(c == null){
            return Response.status(404).build();
        }
        m.getComments().remove(c);
        return Response.ok().build();
    }

    @Path("/{id}/grade")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGrade(@PathParam("id") int id, Grade grade){
        Movie m = db_movies.get(id);
        if(m == null){
            return Response.status(404).build();
        }
        if(m.getGrades() == null){
            m.setGrades(new ArrayList<>());
        }
        grade.setId(++gradeId);
        m.getGrades().add(grade);
        int counter = 0;
        double score = 0;
        for(Grade g: m.getGrades()){
            score += g.getScore();
            counter++;
        }
        m.setScore(score / counter);
        return Response.ok().build();

    }

    @Path("/{id}/actors")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Actor> getActors(@PathParam("id")int id){
        Movie m = db_movies.get(id);
        if(m == null){
            return null;
        }
        if(m.getActors() == null){
            m.setActors(new ArrayList<>());
        }
        return m.getActors();
    }

    @Path("/{movieId}/actors/{actorId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addActor(@PathParam("movieId") int movieId, @PathParam("actorId") int actorId){
        Movie m = db_movies.get(movieId);
        Actor a = db_actors.get(actorId);
        if(m == null || a == null){
            return Response.status(404).build();
        }
        if(m.getActors() == null){
            m.setActors(new ArrayList<>());
        }
        if(a.getMovies() == null){
            a.setMovies(new ArrayList<>());
        }
        m.getActors().add(a);

        Actor actor = new Actor();
        actor.setId(a.getId());
        actor.setName(a.getName());
        actor.setSurname(a.getSurname());
        actor.setMovies(a.getMovies());

        Movie movie = new Movie();
        movie.setId(m.getId());
        movie.setTitle(m.getTitle());
        movie.setDescription(m.getDescription());

        actor.getMovies().add(movie);
        return Response.ok().build();
    }

}
