package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graphs;
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
		
		
		
	}
	
	public void creaGrafo(int year) {
		
		dao.listAllDirectors(idMapD);
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		for(Director d : dao.loadDirectors(idMapD,year))
			if(!grafo.vertexSet().contains(d))
				grafo.addVertex(d);
		
		for(Connessione c : dao.loadConnessioni(idMapD, grafo.vertexSet())) {
			
			if(!grafo.containsEdge(c.getD1(), c.getD2()))
				Graphs.addEdgeWithVertices(grafo, c.getD1(), c.getD2(), c.getWeight());
			
		}
		
		System.out.println("#Verici: "+grafo.vertexSet().size());
		System.out.println("#Archi: "+grafo.edgeSet().size());
		
	}

}
