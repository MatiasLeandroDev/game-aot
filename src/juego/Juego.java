package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
import javax.sound.sampled.Clip;


public class Juego extends InterfaceJuego {

	///////////////////////VARIABLES////////////////////////////////
	private Entorno entorno;
	private Clip musicaFondo, sonidoMuerteEnemigo, sonidoTomaDePosion, musicaGanar, musicaInicio, musicaPerder;
	private boolean perdioJuego = false, iniciarJuego = false, ganoJuego = false;
	Heroe mikasa;
	Obstaculos obstaculos;
	Enemigo[] titanes;
	Enemigo titanColosal;
	Proyectiles proyectiles;
	Proyectil[] proyectilesEnJuego;
	Elixir pociones;
	Vida vidaExtra;
	Image fondoPantallaPrincipal, fondo, gameover, ganar, calavera, arma, kiojina, img, casa1, fuente, casa2, casa3;
	double x1, x2, x3, y1, y2, y3;
	int contadorTicks, contadorSeg, capturaSeg, capturaSeg2, capturaSeg3, capturaSegundo, cantidadMuertes, totalTitanes;
	int[] capturaSegundos;
	
	Juego() {

		/////////////////////////ENTORNO /////////////////////
		
		this.entorno = new Entorno(this, "AOT | by: Heredia, Diaz, Garcia", 800, 650);
		
		iniciarAtributosGlobales();		
		
		iniciarImagenes();
		
		iniciarMusica();
		
		// Inicia el juego!
		this.entorno.iniciar();

	}

	/////////////////////////TICK////////////////////////////////
	public void tick() {
		
		entorno.dibujarImagen(fondoPantallaPrincipal, 400, 320, 0 ,0.65);
		musicaInicio.start();
		if(entorno.sePresiono(entorno.TECLA_ENTER)) {
			this.iniciarJuego = true;	
		}
		if(Boolean.TRUE.equals(iniciarJuego)) {
			musicaInicio.close();
			this.musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);


			// EJECUTA TICK SI MIKASA TIENE VIDAS
			if( mikasa.getVidas()<= 0 || this.perdioJuego)  {
				entorno.dibujarImagen(gameover, 400, 350, 0,0.27);
				sonidoMuerteEnemigo.stop();
				musicaFondo.stop();
				musicaFondo.close();
				musicaPerder.start();
			}
			else if(esGanador()) {
				musicaFondo.close();
				entorno.dibujarImagen(ganar, 400, 312, 0, 0.25);
				musicaGanar.start();
			}
			else {	

				agregarTitanesExtras();

				//CONTADOR DE MUERTES Y DE MUNICION 
				
				this.entorno.dibujarImagen(fondo, 400, 325, 0, 1.05);
				this.entorno.cambiarFont(entorno.getFont().getName(), 30, Color.BLACK);
				this.entorno.dibujarImagen(calavera, 40, 540, 0, 0.25);
				this.entorno.escribirTexto("" + cantidadMuertes, 90, 540);
				this.entorno.dibujarImagen(arma, 45, 590, 0, 0.2);
				this.entorno.escribirTexto("" + mikasa.getCantidadDeMuniciones(),90, 590 );

				//////////////////// CONTADOR DE TICKS PSEUDO-TIMER/////////////////
				contadorTicks++;
				contadorDeSegundos();
				
				////////////////////////// PERSONAJES////////////////////////////
				// MIKASA

				mikasa.dibujarHeroina(entorno);

				if (entorno.estaPresionada(entorno.TECLA_ARRIBA))
					mikasa.caminar();
				mikasa.girar(entorno);

				if (limiteIzq(mikasa.getX()))
					mikasa.setX(22.9);
				if (limiteDer(mikasa.getX()))
					mikasa.setX(772.9);
				if (limiteArr(mikasa.getY()))
					mikasa.setY(22.9);
				if (limiteAbj(mikasa.getY()))
					mikasa.setY(572.9);

				mikasa.cantidadVidas(entorno, mikasa.getVidas());

				// TITAN
				movientosTitanes();
				
				//PROYECTILES
				dispararProyectil();
				dibujarProyectiles();
				disparoMultiple();
				
				//POCION
				this.dibujarPota(this.entorno, this.pociones);
				crearPocion();
				duracionDeEfectoDePocion();
				desactivarPocion();
				
				////////////////////// COLISIONES////////////////////////////////

				// MIKASA-OBSTACULOS
				colisionMikasaObtaculos();
				
				// MIKASA-TITANES
				colisionMikasaTitanes();

				//MIKASA -TITAN COLOSAL
				colisionMikasaConTitanColosal();

				// MIKASA CON POCION
				colisionMikasaPocion();
				mikasa.decrementarTimePowerUp();

				//TITAN CON PROYECTIL 
				colisionTitanConProyectil();
				
				//TITAN COLOSAL - PROYECTIL (mata con dos disparos)
				colicionTtitanColosalConProyectil();
				
				// TITAN CON TITAN 
				//OPCION CON CONDICION 
				colisionTitanConTitan();

				// TITAN -OBSTACULOS

				colisionTitanConObstaculo();

				//TITAN COLOSAL - OBSTACULO
				colisionTitanColosalConObstaculo();

				//PROYECTIL CON OBSTACULO
				colisionProyectilConObstaculo();

				////////////////////////// OBSTACULOS Y EXTRAS/////////////////////

				// OBSTACULOS
				this.dibujarObstaculos(this.entorno, this.obstaculos);
				
				//TITAN COLOSAL
				iniciarTitanColosal();
				movientosTitanColosal();
				
				// VIDAS EXTRAS
				crearVidaExtra();
				dibujarVidaExtra();
				desapareceVidaExtra(); 	
				agregaVidaExtra();

				

			}
		}// FIN IF DE VIDAS
	}// FINAL TICK

	////////////////////////MÉTODOS/////////////////////////////		
	public void dibujarObstaculos(Entorno entorno, Obstaculos obstaculos) {
		for (int i = 0; i < obstaculos.lista.length; i++) {
			
			switch (i) {
	    	case 0:
	    		entorno.dibujarImagen(casa3, obstaculos.lista[i].getX()+5, obstaculos.lista[i].getY()+5, 0, 1);
	    		break;	
	    	case 1:
	    		entorno.dibujarImagen(casa1,obstaculos.lista[i].getX()+1.5, obstaculos.lista[i].getY(), 0, 1);
	    		break;
	    	case 2:
	    		entorno.dibujarImagen(fuente,obstaculos.lista[i].getX(), obstaculos.lista[i].getY()-27, 0, 1);
	    		break;
	    	case 3:
	    		entorno.dibujarImagen(casa2, obstaculos.lista[i].getX()+5, obstaculos.lista[i].getY()+7, 0, 1);
	    		break;
	    	case 4:
	    		entorno.dibujarImagen(casa3,obstaculos.lista[i].getX()+5, obstaculos.lista[i].getY()+7, 0, 1);

	    		break;
	    	default:
	    		break;
	    		
	    	}
	    }	    
	
		}

	
	private void colisionMikasaObtaculos() {
		for(int i = 0; i < obstaculos.lista.length; i ++) {
			mikasa.colisionConObstaculos(obstaculos.lista[i]);
		}
	}
	
	private void colisionProyectilConObstaculo() {
		for(int i = 0; i < proyectilesEnJuego.length; i++) {
			if (proyectilesEnJuego[i]!=null && proyectilesEnJuego[i].colisionaConObstaculo(obstaculos)) {
				proyectilesEnJuego[i]= null;
			}	
		}
	}
	
	private void colisionMikasaTitanes() {
		for(int i = 0; i < titanes.length; i++) {
			if (titanes[i]!=null && mikasa.isInmortal() == false) {
				if(mikasa.colisionConTitan(titanes[i]))
					if(mikasa.getIsPowerUp()) {
						titanes[i] = null;
						cantidadMuertes++;
						mikasa.setIsPowerUp();
						mikasa.resetearTimePowerUp();
						mikasa.Imagen = img;
						sonidoMuerteEnemigo.start();
					}else {
						mikasa.setInmortal(true);
						capturaSeg3 = contadorSeg;
					}

			}
		}
	}
	

	private void colisionMikasaPocion() {
		if (pociones!=null && mikasa.colisionConPocion(pociones)) {
			this.sonidoTomaDePosion = Herramientas.cargarSonido("potaD.wav");
			sonidoTomaDePosion.start();
			pociones=null; 
			mikasa.Imagen=kiojina;
			mikasa.setIsPowerUp();

		}
	}
	
	private void colisionTitanConProyectil() {
		for(int i = 0; i < titanes.length; i++) {
			if (titanes[i]!=null && titanes[i].colisionConProyectil(proyectilesEnJuego)) {
				titanes[i]=null;
				cantidadMuertes ++;
			}	
		}
	}
	
	private void colisionTitanConObstaculo() {
		for (int i = 0; i < titanes.length; i++) {
			if (titanes[i] != null) {				// EJECUTA SI EL TITAN NO ESTA EN NULL
				if (titanes[i].colisionObstaculo(obstaculos)// SE CUMPLE LA CONDICION SI COLISIONA Y SI EL ESTADO DE COLISION ESTA EN FALSO
						&& titanes[i].isEstadoColision() == false) {

					titanes[i].setEstadoColision(true);// CAMBIA EL ESTADO A true PARA SABER QUE COLISIONO			
					capturaSeg = contadorSeg;// CAPTURA EL SEGUNDO EN EL QUE EL TITAN COLISIONO
				}

			}
		}
	}
	

	private void colisionTitanConTitan() {
		for(int i = 0; i < titanes.length; i++) {
			if (titanes[i]!=null && titanes[i].isSuperposicion() == false) {
				titanes[i].colisionDeTitanes ( titanes, i);
				titanes[i].setSuperposicion(true);			
				capturaSeg2 = contadorSeg;
			}
		}
	}
	
	private void colisionTitanColosalConObstaculo() {
		if(this.titanColosal != null && this.titanColosal.getEsColosal()) {
			if (this.titanColosal.colisionObstaculo(obstaculos)// SE CUMPLE LA CONDICION SI COLISIONA Y SI EL ESTADO DE COLISION ESTA EN FALSO
					&& this.titanColosal.isEstadoColision() == false) {

				this.titanColosal.setEstadoColision(true);// CAMBIA EL ESTADO A true PARA SABER QUE COLISIONO			
				capturaSeg = contadorSeg;// CAPTURA EL SEGUNDO EN EL QUE EL TITAN COLISIONO
			}
		}
	}
	
	private void colicionTtitanColosalConProyectil() {
		if (this.titanColosal!=null && this.titanColosal.colisionConProyectil(proyectilesEnJuego)) {
			if(this.titanColosal.getVida() > 0) {
				this.titanColosal.descontarVida();
				this.titanColosal.girar(400, this.titanColosal.getX(), 300, this.titanColosal.getY());
			}else {
				cantidadMuertes ++;
				this.titanColosal = null;
				this.ganoJuego = true;
			}

		}
	}
	
	public void dibujarProyectiles(Entorno entorno, Proyectil proyectil, Heroe mikasa) {
		Proyectil muni = proyectil;
		if (muni != null)
			entorno.dibujarImagen(muni.getImage(), muni.getX(), muni.getY(), muni.angulo, 0.05);
	}
	
	private void disparoMultiple() {
		for (int i = 0; i < proyectilesEnJuego.length; i++) {
			if (proyectilesEnJuego[i] != null) {
				Proyectil municion = proyectilesEnJuego[i];
				municion.cuerpo.x += Math.cos(municion.angulo) * municion.velocidad;
				municion.cuerpo.y += Math.sin(municion.angulo) * municion.velocidad;
				if (limiteIzq(municion.getX()))
					proyectilesEnJuego[i] = null;
				else if (limiteDer(municion.getX()))
					proyectilesEnJuego[i] = null;
				else if (limiteArr(municion.getY()))
					proyectilesEnJuego[i] = null;
				else if (limiteAbj(municion.getY()))
					proyectilesEnJuego[i] = null;
			}
		}
	}
	

	private void dispararProyectil() {
		if (entorno.sePresiono(entorno.TECLA_ESPACIO)) {
			for (int i = 0; i < proyectilesEnJuego.length; i++) {
				if (proyectilesEnJuego[i] == null) {
					proyectilesEnJuego[i] = mikasa.disparo();
					i = proyectilesEnJuego.length;
				}
			}
		}
	}
	
	public void dibujarPota(Entorno entorno, Elixir posiones) {
		if (posiones != null) {
			entorno.dibujarImagen(pociones.getImage(), pociones.getX(), pociones.getY(), 0, 0.05);
		}
	}
	
	private void duracionDeEfectoDePocion() {
		if(mikasa.getIsPowerUp() && mikasa.getTimePowerUp() <= 0) {
			mikasa.setIsPowerUp();
			mikasa.resetearTimePowerUp();
			mikasa.Imagen = img;
		}
	}
	
	private void agregaVidaExtra() {
		if (vidaExtra!=null && mikasa.getVidas()<3 && vidaExtra.colisionConMikasa(mikasa)) { // si colisiona con la vida le suma una vida
			vidaExtra=null; 
			mikasa.setVidas(mikasa.getVidas()+1);
		}
	}
	

	private void desapareceVidaExtra() {
		if ( vidaExtra != null && contadorSeg==capturaSegundo+8) { //desaparece la vida si no la colisiona mikasa
			vidaExtra = null;
		}
	}
	

	private void dibujarVidaExtra() {
		if (vidaExtra != null && vidaExtra.coordenadaOk) {
			vidaExtra.dibujarVida(entorno);
		}
	}
	

	private void dibujarProyectiles() {
		for (int i = 0; i < proyectilesEnJuego.length; i++) {
			if (proyectilesEnJuego[i] != null) {
				Proyectil municion = proyectilesEnJuego[i];
				this.dibujarProyectiles(this.entorno, municion, this.mikasa);
			}
		}
	}
	

	private void crearVidaExtra() {
		if (vidaExtra == null && mikasa.getVidas() <3 && contadorSeg%20 == 0) { //si tiene menos de 3 vidas aparece una extra 
			vidaExtra = new Vida();
			capturaSegundo=contadorSeg;
			vidaExtra.verificaCoordenada(vidaExtra, obstaculos);
		}
	}
	

	private void desactivarPocion() {
		if (contadorSeg % 30 == 0) {
			pociones = null;
		}
	}
	

	private void crearPocion() {
		if (pociones == null && contadorSeg % 20 == 0 && !mikasa.getIsPowerUp()) {
			pociones = new Elixir();
			pociones.verificaCoordenada(pociones, obstaculos);
		}
	}
	
	public boolean esGanador() {
		return this.ganoJuego;
	}

	private void contadorDeSegundos() {
		if (contadorTicks % 65 == 0) {
			contadorSeg++;
		}
	}
	
	private void iniciarTitanes() {
		this.x1 = (double) Math.random() * (-150 - (-20) + 1) - 20; 
		this.x2 = (double) Math.random() * (810 - 950 + 1) + 950; 
		this.x3 = (double) Math.random() * (300 - (500) + 1) + 500;
		this.y1 = -50; 
		this.y2 = 650; 
		this.y3 = 300;
		titanes = new Enemigo[4];
		for (int i = 0; i < titanes.length; i++) {
			if (i == 0) {
				titanes[i] = new Enemigo(this.x3, this.y1, 0.65, 2);
			} else if (i == 1) {
				titanes[i] = new Enemigo(this.x3, this.y2, 0.65, 2);
			} else if (i == 2) {
				titanes[i] = new Enemigo(this.x1, this.y3, 0.65, -1);
			} else {
				titanes[i] = new Enemigo(this.x2, this.y3, 0.65, -1);
			}
		}
	}
	
	private void movientosTitanes() {
		for (int i = 0; i < titanes.length; i++) {

			// EJECUTA SI EL TITAN NO ESTÁ EN NULL
			if (titanes[i] != null) {
				this.perdioJuego = this.perdioJuego || titanes[i].colisionConPocion(pociones);
				titanes[i].dibujarTitan(entorno);

				//DA TRUE SI EL TITAN ENTRA EN LA VENTANA
				if(titanes[i].getX() >= 30 && titanes[i].getX() <= 760 && titanes[i].getY() >= 30 && titanes[i].getY() <= 560) {

					//CAMBIA EL BOOLEAN PARA INDICAR QUE EL TITAN YA ENTRÓ EN LA VENTANA
					titanes[i].setContenido(true);
				}

				if(mikasa.isInmortal() && contadorSeg == capturaSeg3 + 2) {
					mikasa.setInmortal(false);
				}

				if (titanes[i].isSuperposicion() && contadorSeg == capturaSeg2 + 1) {
					titanes[i].setSuperposicion(false);
				}
				// SI EL TITAN COLISIONO CON UN OBSTACULO CAMBIA EL ESTADO A FALSE DESPUES DE 2 SEGUNDOS
				if (titanes[i].isEstadoColision() && contadorSeg == capturaSeg + 2) {
					titanes[i].setEstadoColision(false);
				}
				// EN LOS PRIMEROS TICKS GIRA A LOS TITANES PARA QUE APUNTEN AL CENTRO DE LA
				// PANTALLA
				if (contadorTicks <= 1) {
					titanes[i].girar(400, titanes[i].getX(), 300, titanes[i].getY());
				} else {
					if (titanes[i].detectarHeroina(mikasa.getX(), titanes[i].getX(), mikasa.getY(), titanes[i].getY()))
						if(titanes[i].isEstadoColision() == false && titanes[i].isSuperposicion() == false)
							titanes[i].girar(mikasa.getX(), titanes[i].getX(), mikasa.getY(), titanes[i].getY());
				}
				titanes[i].caminar();
			}
		}
	}
	
	private void iniciarTitanColosal() {
		if(cantidadMuertes == totalTitanes && this.titanColosal == null) {
			this.titanColosal = new Enemigo(400, -100 , 0.9, 2);
			this.titanColosal.girar(400, this.titanColosal.getX(), 300, this.titanColosal.getY());
			this.titanColosal.iniciarTitanColosal();
		}
	}
	
	private void movientosTitanColosal() {
		if(this.titanColosal != null && this.titanColosal.getEsColosal()) {
			this.titanColosal.dibujarTitan(entorno);

			if(this.titanColosal.getX() >= 30 && this.titanColosal.getX() <= 760 && this.titanColosal.getY() >= 30 && this.titanColosal.getY() <= 560) {

				//CAMBIA EL BOOLEAN PARA INDICAR QUE EL TITAN YA ENTRÓ EN LA VENTANA
				this.titanColosal.setContenido(true);
			}

			// SI EL TITAN COLISIONO CON UN OBSTACULO CAMBIA EL ESTADO A FALSE DESPUES DE 2 SEGUNDOS
			if (this.titanColosal.isEstadoColision() && contadorSeg == capturaSeg + 2) {
				this.titanColosal.setEstadoColision(false);
			}
			// EN LOS PRIMEROS TICKS GIRA A LOS TITANES PARA QUE APUNTEN AL CENTRO DE LA
			// PANTALLA
			if (contadorTicks <= 1) {
				this.titanColosal.girar(400, this.titanColosal.getX(), 300, this.titanColosal.getY());
			} else {
				if (this.titanColosal.detectarHeroina(mikasa.getX(), this.titanColosal.getX(), mikasa.getY(),
						this.titanColosal.getY()) && this.titanColosal.isEstadoColision() == false)
					this.titanColosal.girar(mikasa.getX(), this.titanColosal.getX(), mikasa.getY(), this.titanColosal.getY());
			}
			this.titanColosal.caminar();
		}
	}
	
	private void agregarTitanesExtras() {
		for(int i = 0; i < titanes.length; i++) {
			//SI HAY UN TITAN EN NULL Y TODAVIA NO LLEGARON A SALIR LOS 10 TITANES, CREA OTRO
			if(titanes[i] == null && totalTitanes < 10) {
				if (i == 0) {
					titanes[i] = new Enemigo(this.x3, (this.y1 - 50), 0.5, 2);

					//GIRA A LOS TITANES APENAS SE CREAN PARA QUE CAMINEN HACIA EL CENTRO DE LA PANTALLA
					//EN EL PRIMER TICK
					titanes[i].girar(400, titanes[i].getX(), 300, titanes[i].getY());

				} else if (i == 1) {
					titanes[i] = new Enemigo(this.x3, (this.y2 + 50), 0.55, 2);
					titanes[i].girar(400, titanes[i].getX(), 300, titanes[i].getY());
				} else if (i == 2) {
					titanes[i] = new Enemigo(this.x1, this.y3, 0.65, -1);
					titanes[i].girar(400, titanes[i].getX(), 300, titanes[i].getY());
				} else {
					titanes[i] = new Enemigo(this.x2, this.y3, 0.6, -1);
					titanes[i].girar(400, titanes[i].getX(), 300, titanes[i].getY());
				}
				totalTitanes++; //AUMENTA PARA SABER CUANTOS TITANES VAN EN TOTAL EN TO DO EL JUEGO
			}

		}
	}
	
	private void colisionMikasaConTitanColosal() {
		if (this.titanColosal != null && 
				this.titanColosal.getEsColosal() &&
				mikasa.colisionConTitan(this.titanColosal)) {
			if(mikasa.getIsPowerUp()) { //si mikasa tiene la posicion mata al titan colosal 
				cantidadMuertes++;
				mikasa.setIsPowerUp();
				mikasa.resetearTimePowerUp();
				mikasa.Imagen = img;
				sonidoMuerteEnemigo.start();
				this.titanColosal = null;
				this.ganoJuego = true;
			}

		}
	}
	
	public boolean limiteIzq(double x) {
		return x < 23;
	}

	public boolean limiteDer(double x) {
		return x > 773;
	}

	public boolean limiteArr(double y) {
		return y < 23;
	}

	public boolean limiteAbj(double y) {
		return y > 573;
	}
	
	private void iniciarAtributosGlobales() {
		this.contadorTicks = 0;
		this.contadorSeg = 0;
		this.cantidadMuertes = 0;
		this.totalTitanes = 4;
		this.obstaculos = new Obstaculos();
		this.proyectiles = new Proyectiles();
		this.mikasa = new Heroe(400, 500, 2);
		this.mikasa.setMuniciones(proyectiles);
		this.proyectilesEnJuego = new Proyectil[proyectiles.bala.length];
		iniciarTitanes();
	}
	public boolean limiteIzqTitan(double x) {
		return x < 30;
	}
	
	private void iniciarImagenes() {
		this.fondoPantallaPrincipal = Herramientas.cargarImagen("Fondo_Principal.png");
		this.fondo = Herramientas.cargarImagen("Fondo.png");
		this.gameover = Herramientas.cargarImagen("Fondo_Perder.png");
		this.ganar = Herramientas.cargarImagen("Fondo_Ganar.png");
		this.kiojina= Herramientas.cargarImagen("kiojina.gif");
		this.img = Herramientas.cargarImagen("boton_arriba1.png");
		this.calavera = Herramientas.cargarImagen("calavera.gif");
		this.arma = Herramientas.cargarImagen("arma.gif");
		this.casa1= Herramientas.cargarImagen("casa 1.png");
		this.fuente= Herramientas.cargarImagen("fuente.gif");
		this.casa2= Herramientas.cargarImagen("casa3.png");
		this.casa3= Herramientas.cargarImagen("casa4.png");
	}
	public boolean limiteDerTitan(double x) {
		return x > 760;
	}
	
	private void iniciarMusica() {
		this.musicaFondo = Herramientas.cargarSonido("musica_durante_el_juego.wav");
		this.sonidoMuerteEnemigo = Herramientas.cargarSonido("enemy_hit.wav");
		this.musicaInicio = Herramientas.cargarSonido("musica_de_inicio_de_juego.wav");
		this.musicaGanar = Herramientas.cargarSonido("musica_de_ganar_juego.wav");
		this.musicaPerder = Herramientas.cargarSonido("musica_de_inicio_de_juego.wav");
		this.sonidoTomaDePosion = Herramientas.cargarSonido("potaD.wav");
	}

	public boolean limiteArrTitan(double y) {
		return y < 30;
	}



	/////////////////////////////MAIN////////////////////////////////////////		
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();

	}
}