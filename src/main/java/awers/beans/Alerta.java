package awers.beans;
import org.apache.log4j.Logger;

import awers.modelo.tablas.Cliente;
import awers.proceso.InterfazPHP;

/**
 * 
 * @author Pollo
 * Clase que representa un alerta activada por sms
 */
public class Alerta implements Runnable {

	private int tiempoHastaPrimerAlerta,tiempoHastaSegundoAlerta;
	private boolean activado;
	private InterfazPHP interfazPHP;
	private Cliente cliente;
	
	static Logger log = Logger.getLogger(InterfazPHP.class.getName());
	
	public Alerta(String id, InterfazPHP interfazPHP, Cliente cliente, int tiempoEspera){
		this.cliente = cliente;
		this.tiempoHastaPrimerAlerta = (tiempoEspera * 60000);
		this.tiempoHastaSegundoAlerta = 60000;
		this.interfazPHP = interfazPHP;
		activado = true;
	}
	@Override
	public void run() {
		
		try {
			int tiempoRestante = this.tiempoHastaPrimerAlerta;
			this.esperar(tiempoRestante);
			
			//No fue desactivado. Sino se hubiese atrapado la exception
			this.interfazPHP.informarAlertaAbierta(this.cliente);
			tiempoRestante = this.tiempoHastaSegundoAlerta;
			this.esperar(tiempoRestante);
			
			this.interfazPHP.informarNueveOnce(this.cliente);
			
			
		} catch (InterruptedException e) {
			this.interfazPHP.informarBajaAlerta(cliente);
		}
		
	}
	
	public void anularAlerta(){
		this.activado = false;
	}
	
	//Consulta cada 1 segundo si fue o no desactivado o si se acabó el tiempo de espera para el primer alerta
	public void esperar(int tiempoRestante) throws InterruptedException{
		while(this.activado && tiempoRestante > 0){
			Thread.sleep(1000);
			
			tiempoRestante-= 1000;
		}
		if(!this.activado){
			throw new InterruptedException("Fue cancelada el alerta");
		}
	}
}
