package juego;

public class Obstaculos {
	Obstaculo [] lista;
	
	int width ;
	int height ;
	
	

	public Obstaculos() {
		this.lista = new Obstaculo [6];
		crearYAgregarObjetos();
		
	}
	
	private void crearYAgregarObjetos() {
		agregarNuevoObstaculo(150, 150, 84, 70, 0);
		agregarNuevoObstaculo(650, 150, 80,75, 1);
		agregarNuevoObstaculo(400, 350, 118, 50, 2);
		agregarNuevoObstaculo(150, 450, 83,75, 3);
		agregarNuevoObstaculo(650, 450, 83,75, 4);
		agregarNuevoObstaculo(400, 323, 40,116, 5);
		
	}
	private void agregarNuevoObstaculo(int x, int y, int width, int height, int i) {
		Obstaculo obs1 = new Obstaculo(x, y, width, height );
		lista[i] = obs1;
	}
	
	
}
