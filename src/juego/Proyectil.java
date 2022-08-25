package juego;

import java.awt.Image;
import java.awt.Rectangle;
import entorno.Herramientas;

public class Proyectil {
	
///////VARIABLES DE INSTANCIA
 
	double angulo;
	double velocidad;
	Rectangle cuerpo;
	Image spray;
	
	
	public Proyectil() {
		this.spray = Herramientas.cargarImagen("proyectil.png");
		this.velocidad = 2;
	}
	    
	    public Double convert (int numero) {
	    	String cadena = numero + "";
	    	return Double.parseDouble(cadena);
	    }
	    
	    public double getX() {
	    	return convert(this.cuerpo.x);
	    }
	    
	    public double getY() {
	    	return convert(this.cuerpo.y);
	    }
	    
	    public double getWidht(){
	    	return convert(this.cuerpo.width);
	    }
	    
	    public double getHeigth(){
	    	return convert(this.cuerpo.height);
	    }
	    
	    public Image getImage() {
			 return this.spray;
		 }
///////////////////////////METODOS///////////////////
	
	public boolean colisionaConObstaculo(Obstaculos obs) {
	
		double anchoM= this.cuerpo.width/2; 
		double  altoM= this.cuerpo.height/2;
	
		for (int i = 0; i < obs.lista.length; i++) {
			if ( this.cuerpo.x <= (obs.lista[i].getX() + obs.lista[i].getWidht() / 2 + anchoM)
				&& this.cuerpo.x >= (obs.lista[i].getX() - obs.lista[i].getWidht() / 2 - anchoM)
				&& this.cuerpo.y <= (obs.lista[i].getY() + obs.lista[i].getHeigth() / 2 + altoM)
				&& this.cuerpo.y >= (obs.lista[i].getY() - obs.lista[i].getHeigth() / 2 - altoM)) {
				return true; 
			}
		}
		return false;
}	    

}