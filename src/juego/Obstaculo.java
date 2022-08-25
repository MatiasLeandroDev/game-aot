package juego;
import java.awt.Rectangle;

public class Obstaculo {
	
	Rectangle cuerpo;

 
    public Obstaculo(int x, int y, int width, int heigth) {
    	this.cuerpo = new Rectangle(x, y, width, heigth);
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

}
