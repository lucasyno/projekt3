package rest;

import domain.Actor;
import domain.Movie;
import domain.services.ActorService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukaszgodlewski on 11.05.2017.
 */

@Path("/actors")
public class ActorResources {

    private ActorService db = new ActorService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Actor> getAll(){
        return db.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Actor actor){
        db.add(actor);
        return Response.ok(actor.getId()).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id){
        Actor actor = db.get(id);

        if(actor == null){
            return Response.status(404).build();
        }
        return Response.ok(actor).build();
    }

    @Path("/{id}/movies")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getMovies(@PathParam("id") int id){
        Actor a = db.get(id);
        if(a == null){
            return null;
        }
        if(a.getMovies() == null){
            a.setMovies(new ArrayList<>());
        }
        return a.getMovies();
    }

}
