package awers.modelo.tablas;

import java.util.Date;

public class AlertaTbl {
	private String telefono;
	private String dir;
	private int duracion;
	private Date fechaActivacion;
	
	public AlertaTbl(String telefono, int duracion, Date fechaActivacion, String dir){
		this.telefono = telefono;
		this.duracion = duracion;
		this.fechaActivacion = fechaActivacion;
		this.dir = dir;
	}
	
	public AlertaTbl(String telefono, int duracion, String dir){
		this.telefono = telefono;
		this.duracion = duracion;
		this.dir = dir;
	}
	
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public int getDuracion() {
		return duracion;
	}
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}
	public Date getFechaActivacion() {
		return fechaActivacion;
	}
	public void setFechaActivacion(Date fechaActivacion) {
		this.fechaActivacion = fechaActivacion;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}
	
	public String getIdAlerta(){
		return this.getTelefono()+"_"+this.getDir();
	}
	
	
}
