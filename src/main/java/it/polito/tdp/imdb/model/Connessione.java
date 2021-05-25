package it.polito.tdp.imdb.model;

public class Connessione {

	int da;
	int db;
	
	public Connessione(int da, int db) {
		super();
		this.da = da;
		this.db = db;
	}
	
	public int getDa() {
		return da;
	}
	
	public void setDa(int da) {
		this.da = da;
	}
	
	public int getDb() {
		return db;
	}
	
	public void setDb(int db) {
		this.db = db;
	}
	
}
