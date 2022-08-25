package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Vida {

	////////////////////////VARIABLE////////////////////////////

	double x; 
	double y; 
	double ancho; 
	double alto;
	double zona;
	Image corazon; 
	boolean coordenadaOk;
	////////////////////////////CONSTRUCTOR////////////////////////

	public Vida() {
		this.x=Math.random()*750; 
		this.y=Math.random()*550;
		this.ancho = 30;
		this.alto = 30;
		this.corazon = Herramientas.cargarImagen("heart1.gif");
	}
	
	//////////////////////METODOS VERIFICADORES DE POSICION//////////////////////
	
	public void dibujarVida(Entorno entorno) { 
		entorno.dibujarImagen(this.corazon, this.x, this.y, 0 ,0.06);

	}
	
	public boolean colisionConMikasa(Heroe mikasa){ 
		double anchoM= this.ancho/2; 
		double altoM=this.alto/2; 

		if ( this.x<= (mikasa.getX()+ mikasa.getAncho()/2 +anchoM) && this.x >= (mikasa.getX()-mikasa.getAncho()/2-anchoM) && 
				this.y <= (mikasa.getY()+ mikasa.getAlto()/2 +altoM) && this.y >= ( mikasa.getY()- mikasa.getAlto()- altoM) ) {
			return true ;
		}
		return false; 	
	}
	
	public void verificaCoordenada (Vida vida,Obstaculos obs) {
		while(vida.seSuperpone(obs)) {
			this.x= (int)(Math.random()*750);
			this.y=(int)(Math.random()*550);	
		} coordenadaOk = true;
	}
 
	public boolean seSuperpone (Obstaculos obs) {

		double anchoM= this.ancho/2; 
		double  altoM= this.alto/2;

		for (int i = 0; i < obs.lista.length; i++) {
			if ( this.x < (obs.lista[i].getX() + obs.lista[i].getWidht() / 2 + anchoM)
					&& this.x > (obs.lista[i].getX() - obs.lista[i].getWidht() / 2 - anchoM)
					&& this.y < (obs.lista[i].getY() + obs.lista[i].getHeigth() / 2 + altoM)
					&& this.y > (obs.lista[i].getY() - obs.lista[i].getHeigth() / 2 - altoM)) {
				return true; 
			}
		}
		return false;
	}	
	
}
