package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private SimpleWeightedGraph<Director, DefaultWeightedEdge> grafo;
	
	private ImdbDAO dao;
	
	private Map<Integer,Director> idMapD;
	
	public Model() {
		
		dao = new ImdbDAO();
		
		idMapD = new HashMap<>();
		
		for(Director d : dao.listAllDirectors()) {
			idMapD.put(d.getId(), d);
		}
			
		
	}
	
	public void creaGrafo(int year) {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		for(Director d : dao.loadDirectors(idMapD,year))
			if(!grafo.vertexSet().contains(d))
				grafo.addVertex(d);
		
	}

}
