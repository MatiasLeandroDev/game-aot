package juego;

import java.awt.Image;
import javax.sound.sampled.Clip;
import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo {
	///////////////////////////////VARIABLES////////////////////////////
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private double angulo;
	private final double velocidad;
	private boolean estadoColision;
	private boolean contenido;		
	private boolean superposicion;
	private double anchoM ; 
	private double altoM ;
	private Clip sonidoMuerteEnemigo;
	private Image img;
	private int vida;
	private boolean esColosal;

	//////////////////////////////CONSTRUCTOR////////////////////////////
	public Enemigo(double x, double y, double velocidad, int angulo) {
		this.x = x;
		this.y = y;
		this.alto = 75;
		this.ancho = 60; 	
		this.angulo = angulo * (Math.PI);
		this.velocidad = velocidad;
		this.estadoColision = false;
		this.contenido = false;
		this.superposicion = false;
		this.altoM= this.alto/2;  
		this.anchoM= this.ancho/2;
		this.sonidoMuerteEnemigo = Herramientas.cargarSonido("enemy_hit.wav");
		this.img = Herramientas.cargarImagen("titan.gif");
	}

	////////////////////////////SETTERS & GETTERS/////////////////////

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
	public boolean isEstadoColision() {
		return estadoColision;
	}
	public void setEstadoColision(boolean estado) {
		this.estadoColision = estado;
	}
	public boolean isContenido() {
		return contenido;
	}
	public void setContenido(boolean contenido) {
		this.contenido = contenido;
	}
	public boolean isSuperposicion() {
		return superposicion;
	}
	public void setSuperposicion(boolean superposicion) {
		this.superposicion = superposicion;
	}
	public double getAngulo() {
		return angulo;
	}
	public void setAngulo(double angulo) {
		this.angulo = angulo;
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
	public boolean getEsColosal() {
		return this.esColosal;
	}
	public void descontarVida() {
		this.vida -= 1; 
	}
	public int getVida() {
		return this.vida;
	}

	/////////////////////////METODOS//////////////////////////////////

	public void dibujarTitan(Entorno entorno) {
		if(this.esColosal) {
			entorno.dibujarImagen(img, this.x, this.y, this.angulo-1.69, 0.30 );
			
		}else {
			entorno.dibujarImagen(img, this.x, this.y, this.angulo, 1.5 );
			
			
		}
			
	}

// CAMINA DEPENDIENDO DE QUE ANGULO TENGA
	public void caminar(){
		this.x += Math.cos(this.angulo)*this.velocidad;
		this.y += Math.sin(this.angulo)*this.velocidad;
		if(this.contenido) {	
			
			if(this.x < 30) {								
				this.x = 35;								
				this.angulo = Math.PI/2 + this.angulo;		
			}												
			if(this.x > 760) {								
				this.x = 755;								
				this.angulo = Math.PI/2 + this.angulo;		
			}												
			if(this.y < 30) {								
				this.y = 35;								
				this.angulo = Math.PI/2 + this.angulo;		
			}												
			if(this.y > 560) {								
				this.y = 555;								
				this.angulo = Math.PI/2 + this.angulo;			
			}
		}
	}
	
// GIRA AL TITAN ENTORNO A UNA X e Y
	public void girar(double x1, double x2, double y1, double y2) {
		this.angulo = Math.atan2(y1-y2, x1-x2);
	}

// DETECTA A LA HEROINA
	public boolean detectarHeroina(double x1, double x2, double y1, double y2) {
		return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)) < 300;
	}
	
//Instancia Titan Colosal
	public void iniciarTitanColosal() {
		this.vida = 2;
		this.esColosal = true;
		this.img = Herramientas.cargarImagen("jefe_final.gif");
	}

//OBSTACULO-TITAN

	public boolean colisionObstaculo(Obstaculos obs) {
		for (int i = 0; i < obs.lista.length; i++) {
			if (this.x < (obs.lista[i].getX() + obs.lista[i].getWidht() / 2 + anchoM)
					&& this.x > (obs.lista[i].getX() - obs.lista[i].getWidht() / 2 - anchoM)
					&& this.y < (obs.lista[i].getY() + obs.lista[i].getHeigth() / 2 + altoM)
					&& this.y > (obs.lista[i].getY() - obs.lista[i].getHeigth() / 2 - altoM)) {
				angulo += Herramientas.grados(180);
				return true;
			}
		}return false;
	}

//TITAN PROYECTIL
	public boolean colisionConProyectil (Proyectil []bala) {
		for (int i=0; i <bala.length; i++ ) {
			if (bala[i] != null && this.x < (bala[i].getX()+ bala[i].getWidht()/2 +anchoM) && this.x > (bala[i].getX()-bala[i].getWidht()/2-anchoM) &&
					this.y < (bala[i].getY() + bala[i].getHeigth()/2 +altoM) && this.y > ( bala[i].getY() - bala[i].getHeigth()/2 - altoM) ) {
				bala[i]= null; 
				sonidoMuerteEnemigo.start();
				return true; 
			}
		}return false;
	}
	
	public boolean colisionConPocion (Elixir pocion) {
			if (pocion != null && this.x < (pocion.getX()+ pocion.getWidht()/2 +anchoM) && this.x > (pocion.getX()-pocion.getWidht()/2-anchoM) &&
					this.y < (pocion.getY() + pocion.getHeigth()/2 +altoM) && this.y > ( pocion.getY() - pocion.getHeigth()/2 - altoM) ) {
				 pocion= null; 
				 return true; 
			}
			return false;
	}

//TITAN-TITAN 

public void colisionDeTitanes(Enemigo[] obs, int indice) {
	for (int i = 0; i < obs.length; i++) {
		if (obs[i]!=null && indice != i && this.x < (obs[i].getX() + obs[i].getAncho() / 2 + anchoM)
				&& this.x > (obs[i].getX() - obs[i].getAncho() / 2 - anchoM)
				&& this.y < (obs[i].getY() + obs[i].getAlto() / 2 + altoM)
				&& this.y > (obs[i].getY() - obs[i].getAlto() / 2 - altoM)) {
			angulo += Herramientas.grados(180);
		}
	}
}

	
}
	// FIN DE CLASE	






















