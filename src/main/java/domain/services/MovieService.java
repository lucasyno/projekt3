package domain.services;

import domain.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukaszgodlewski on 11.05.2017.
 */

public class MovieService {

    private static List<Movie> db = new ArrayList<Movie>();
    private static int currentId = 0;

    public List<Movie> getAll(){
        return db;
    }

    public Movie get(int id){
        for(Movie m: db){
            if(m.getId() == id)
                return m;
        }
        return null;
    }

    public void add(Movie m){
        m.setId(++currentId);
        db.add(m);
    }

    public void delete(Movie m){
        db.remove(m);
    }

    public void update(Movie movie){
        for(Movie m: db){
            if(m.getId()==movie.getId()){
                m.setTitle(movie.getTitle());
                m.setDescription(movie.getDescription());
            }
        }
    }
}
