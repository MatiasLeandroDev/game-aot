package juego;

import java.awt.Image;
import java.awt.Rectangle;
import javax.sound.sampled.Clip;
import entorno.Entorno;
import entorno.Herramientas;

public class Heroe{

/////////////////////VARIABLES DE INSTANCIA/////////////////
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private final double velocidad; 
	private double angulo;
	private boolean estado; 
	private boolean inmortal;
	private Proyectil[] municion;
	private int vidas; 
	double anchoM; 
	double altoM; 
	Image corazon;
	Image Imagen; 
	private Boolean isPowerUp;
    private Double timePowerUp;
	private Clip sonidoTomaDePosion;
    
/////////////////////CONSTRUCTOR////////////////////////
	   	public Heroe(double x, double y, double velocidad) {
	    	this.x = x;
	    	this.y = y;
	    	this.alto = 47; 
	    	this.ancho = 47; 
	    	this.angulo = (Math.PI*-1/2);
	    	this.velocidad = velocidad; 
	    	this.vidas=3;
	    	this.inmortal = false;
	    	this.anchoM= this.ancho/2 ; 
			this.altoM=this.alto/2;
			this.isPowerUp = false;
			this.timePowerUp = 70.0;
			this.Imagen=Herramientas.cargarImagen("boton_arriba1.png");
	    	this.corazon= Herramientas.cargarImagen("heart.gif");
	    	this.sonidoTomaDePosion = Herramientas.cargarSonido("potaD.wav");
	    }
////////////////////SETTERS & GETTERS////////////////////
	   	
	    public double getX() {
			return x;
		}
		public void setX(double x) {
			this.x = x;
		}
		public double getY() {
			return y;
		}
		public void setY(double y) {
			this.y = y;
		}
		public double getVelocidad() {
			return velocidad;
		}
		public boolean isEstado() {
			return estado;
		}
		public void setEstado(boolean estado) {
			this.estado = estado;
		}
		public boolean isInmortal() {
			return inmortal;
		}
		public void setInmortal(boolean inmortal) {
			this.inmortal = inmortal;
		}
		public double getAngulo() {
			return angulo;
		}
		public void setAngulo(double angulo) {
			this.angulo = angulo;
		}
		public int getVidas() {
			return vidas;
		}
		public void setVidas( int vidas) {
			this.vidas = vidas;
		}
		public double getAlto() { 
			return alto;
		}
		public void setAlto(double alto) { 
			this.alto = alto;
		}
		public double getAncho() { 
			return ancho;
		}
		public void setAncho(double ancho) { 
			this.ancho = ancho;
		}

		
//////////////////////METODOS///////////////////////////////

	public void dibujarHeroina(Entorno entorno) {	    	

    		entorno.dibujarImagen(this.Imagen, this.x, this.y, this.angulo,1);
	}
	
//MOVIMIENTOS   
    public void girar(Entorno entorno) {
    	if(entorno.estaPresionada(entorno.TECLA_DERECHA))
    		this.angulo += Herramientas.radianes(1);
    	if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA))
    		this.angulo += Herramientas.radianes(-1);
    	if(this.angulo < 0) {
			this.angulo +=2*Math.PI;
		}
        if(this.angulo > 2*Math.PI) {
        	this.angulo -=2*Math.PI;
        }
    }
    
    public void caminar() {
	    this.x += Math.cos(this.angulo)*this.velocidad;
		this.y += Math.sin(this.angulo)*this.velocidad;
    }	
    
// COLISIONES CON OBSTACULOS 

	public void colisionConObstaculos(Obstaculo casa  ) {
		double anchoM= this.ancho/2 ; 
		double altoM=this.alto/2; 
		
		//interseccion derecha //interseccion izq /// insterseccion inferior// interseccion superior
		if ( this.x-anchoM < (casa.getX()+ casa.getWidht()/2 ) && this.x+anchoM > (casa.getX()-casa.getWidht()/2)
		&& this.y-altoM < (casa.getY() + casa.getHeigth()/2 ) && this.y+altoM > ( casa.getY() - casa.getHeigth()/2 ) ) {
		
			//llama al metodo para ver en que lugar colisionó con el obstaculo y restringe los movimientos de mikasa 
			if (lugarDeColision(casa)== "izquierda") {
				this.x-=2;
			}
			if (lugarDeColision(casa)== "derecha") {
				this.x+=2;
			}
			if (lugarDeColision(casa)== "arriba") {
				this.y-=2;
			}
			if (lugarDeColision(casa)== "abajo") {
				this.y+=2;
			}
		}
	}
	
	public String lugarDeColision (Obstaculo casa) {
		double anchoM= this.ancho/2 ; 
		double altoM=this.alto/2; 
		
		if (this.x+anchoM > casa.getX()-casa.getWidht()/2 && this.x < casa.getX()-casa.getWidht()/2 +8) {
			return "izquierda";	}	
		if  (this.x-anchoM < casa.getX()+casa.getWidht()/2 && this.x > casa.getX()+casa.getWidht()/2 -8){
			return "derecha";}
		if (this.y+altoM > casa.getY()-casa.getHeigth()/2 && this.y+altoM< casa.getY()-casa.getHeigth()/2 +8 ) {
			return "arriba";}
		if (this.y-anchoM < casa.getY()+casa.getHeigth()/2 && this.y-anchoM > casa.getY()+casa.getHeigth()/2 -8 ){
			return "abajo";}
		return "";
	}	
	
// COLISIONES CON TITANES
	
	public boolean colisionConTitan(Enemigo titan){ 
		double anchoM= this.ancho/2; 
		double altoM=this.alto/2; 
		
		if ( this.x< (titan.getX()+ titan.getAncho()/2 +anchoM) && this.x > (titan.getX()-titan.getAncho()/2-anchoM) && 
			this.y < (titan.getY() + titan.getAlto()/2 +altoM) && this.y > ( titan.getY() - titan.getAlto()/2 - altoM) ) {
			if (!isPowerUp) {
				this.setVidas(this.getVidas()-1);
				this.setX(400); 
				this.setY(575);
				this.setAngulo((Math.PI*-1/2));
			}
			
			return true ;
		}
		else {
			return false; 
		}	
	}
	
// POCIONES-MIKASA
	public boolean colisionConPocion(Elixir pocion){ 
		double anchoM= this.ancho/2; 
		double altoM=this.alto/2; 
		
		if ( this.x<= (pocion.cuerpo.x+ pocion.cuerpo.width/2 +anchoM) && this.x >= (pocion.cuerpo.x-pocion.cuerpo.width/2-anchoM) && 
			this.y <= (pocion.cuerpo.y + pocion.cuerpo.height/2 +altoM) && this.y >= ( pocion.cuerpo.y- pocion.cuerpo.height- altoM) ) {
			sonidoTomaDePosion.start();
			return true ;
		}
		return false; 		
	}
	
	public void decrementarTimePowerUp() {
		if(this.isPowerUp)
			this.timePowerUp -= 0.25;
	}
	
	public void resetearTimePowerUp() {
		this.timePowerUp = 70.0;
	}
	
	public void setIsPowerUp() {
		this.isPowerUp = !isPowerUp;
	}
	
	public boolean getIsPowerUp() {
		return this.isPowerUp;
	}
	
	public Double getTimePowerUp() {
		return this.timePowerUp;
	}
	 
	
//VIDAS
    public void cantidadVidas(Entorno entorno, int vidas) {
    	switch (vidas) {
    	case 1:
    		entorno.dibujarImagen(corazon, 680, 30, 0, 0.4);
    		break;
    	case 2:
    		entorno.dibujarImagen(corazon, 680, 30, 0, 0.4);
    		entorno.dibujarImagen(corazon, 720,30, 0, 0.4);
    		break;
    	case 3:
    		entorno.dibujarImagen(corazon, 680, 30, 0, 0.4);
    		entorno.dibujarImagen(corazon, 720, 30, 0, 0.4);
    		entorno.dibujarImagen(corazon, 760, 30, 0, 0.4);
    		break;
    	}
    }	    
	
//DISPAROS Y MUNICIONES	       
	public Proyectil disparo() {
		Proyectil muni = null;
		Integer indice = -1;
		for (int i = municion.length - 1; i >= 0 ; i--) {
			if (municion[i] != null && muni == null) {
				muni = municion[i];
				muni.cuerpo = new Rectangle(doubleToInt(this.x), doubleToInt(this.y), 20, 10);
				muni.angulo = this.angulo;
				indice = i;
			}
		}
		if (muni != null) {
			municion[indice] = null;
			return muni;
		}
		return muni;
	}

	public void setMuniciones(Proyectiles listaMunicion) {
		this.municion = listaMunicion.bala;
	}

	private int doubleToInt(double num) {
		String numString = num + "";
		Double numDouble = Double.parseDouble(numString);
		return numDouble.intValue();
	}
	
	public int getCantidadDeMuniciones() {
		int cant = 0;
		for(int i = 0; i < municion.length; i++) {
			if(municion[i] != null) {
				cant++;
			}
		}
		return cant;
	}

   
}//FINAL CLASE