package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Connessione;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map<Integer,Director> idMapD){
		
		String sql = "SELECT * FROM directors";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				idMapD.put(director.getId(), director);
				
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Director> loadDirectors(Map<Integer,Director> idMapD, int year){
		
		String sql = "SELECT DISTINCT md.director_id AS id "
				+ "FROM movies_directors md, movies m "
				+ "WHERE m.id = md.movie_id AND m.year = ?";
		
		List<Director> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();
		
		try {
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			
			ResultSet res = st.executeQuery();
		
			while(res.next())
				result.add(idMapD.get(res.getInt("id")));
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Connessione> loadConnessioni(Map<Integer,Director> idMapD, Set<Director> vertexSet){
		
		String sql = "SELECT md1.director_id AS id1, md2.director_id AS id2, COUNT(r.actor_id) AS cnt "
				+ "FROM movies_directors md1, movies_directors md2, roles r "
				+ "WHERE (md1.movie_id = md2.movie_id AND md1.director_id != md2.director_id AND md1.movie_id = r.movie_id) OR (md1.director_id != md2.director_id AND md1.movie_id != md2.movie_id AND (md1.movie_id = r.movie_id AND md2.movie_id = r.movie_id)) "
				+ "GROUP BY md1.director_id, md2.director_id ";
		
		List<Connessione> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();
		
		try {
			
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				
				Connessione c = new Connessione(idMapD.get(rs.getInt("id1")),idMapD.get(rs.getInt("id2")),rs.getInt("cnt"));
				
				if(vertexSet.contains(c.getD1()) && vertexSet.contains(c.getD2()))
					result.add(c);
				
			}
			
			conn.close();
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
