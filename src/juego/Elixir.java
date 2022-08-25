package juego;
import java.awt.Image;
import java.awt.Rectangle;


import entorno.Herramientas;


public class Elixir {


///////VARIABLES DE INSTANCIA    
    int width = 30;
    int height = 30;
    Rectangle cuerpo;
    Image mensaje; 
    Image img2;
    boolean coordenadaOk;
    
/////////CONSTRUCTOR//////////////////////////////////////////////////////////////////////////////////
    public Elixir() { 
    	this.cuerpo = new Rectangle((int)(Math.random()*770), (int)(Math.random()*570), 10, 10);
    	img2 = Herramientas.cargarImagen("pocionTitan.gif");
	}
    
    
	 public Image getImage() {
		 return this.img2;
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
//////////////////////METODOS//////////////////////

public void verificaCoordenada (Elixir elixir,Obstaculos obs) {
	while(elixir.seSuperpone(obs)) {
		this.cuerpo = new Rectangle((int)(Math.random()*770), (int)(Math.random()*570), 10, 10);
	} coordenadaOk = true;
}

public boolean seSuperpone (Obstaculos obs) {
	
	double anchoM= this.cuerpo.width/2; 
	double  altoM= this.cuerpo.height/2;

	for (int i = 0; i < obs.lista.length; i++) {
		if ( this.cuerpo.x < (obs.lista[i].getX() + obs.lista[i].getWidht() / 2 + anchoM)
			&& this.cuerpo.x > (obs.lista[i].getX() - obs.lista[i].getWidht() / 2 - anchoM)
			&& this.cuerpo.y < (obs.lista[i].getY() + obs.lista[i].getHeigth() / 2 + altoM)
			&& this.cuerpo.y > (obs.lista[i].getY() - obs.lista[i].getHeigth() / 2 - altoM)) {
			return true; 
		}
	}
	return false;
}	

}


