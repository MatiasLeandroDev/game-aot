package juego;


public class Proyectiles {
	
	Proyectil [] bala;
		
    
	public Proyectiles() {
		this.bala = new Proyectil [13];
		crearBalas();
	}
	
	
	public void crearBalas() {
		cargarBalas();
		
	}
	
	public void cargarBalas() {
		for (int i = 0; i < bala.length; i++ ) {
			Proyectil muni = new Proyectil();
			bala[i] = muni;
		}
		
	}
}

