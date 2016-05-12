package awers.sistema;

public class Configuracion {
	
	private int tiempoPrimerAlerta;
	private int tiempoSegundoAlerta;
	
	//?user=admin&passwd=232431&cat=1&to=%s&text=%s
	
	private String user;
	private String passwd;
	
	private String urlAviso;
	private String urlNueveOnce;
	
	private String mensajeAvisoAlta;
	private String mensajeAvisoPendiente;
	private String mensajeBajaAlerta;
	private String mensajeNueveOnce;
	private String mensajeNoServicio;
	private String mensajeAlertaExistente;
	
	
	
	public int getTiempoPrimerAlerta() {
		return tiempoPrimerAlerta;
	}
	public void setTiempoPrimerAlerta(int tiempoPrimerAlerta) {
		this.tiempoPrimerAlerta = tiempoPrimerAlerta;
	}
	
	public int getTiempoSegundoAlerta() {
		return tiempoSegundoAlerta;
	}
	public void setTiempoSegundoAlerta(int tiempoSegundoAlerta) {
		this.tiempoSegundoAlerta = tiempoSegundoAlerta;
	}
	
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getUrlAviso() {
		return urlAviso;
	}
	public void setUrlAviso(String urlAviso) {
		String definitiva = urlAviso+"?user="+user+"&passwd="+passwd+"&cat=1&to=%s&text=%s";
		this.urlAviso = definitiva;
	}
	public String getUrlNueveOnce() {
		return urlNueveOnce;
	}
	public void setUrlNueveOnce(String urlNueveOnce) {
		String definitiva = urlNueveOnce+"?CLIENT_NUMBER=%s&type=ALERT";
		this.urlNueveOnce = definitiva;
	}
	public String getMensajeAvisoAlta() {
		return mensajeAvisoAlta;
	}
	
	public void setMensajeAvisoAlta(String mensajeAvisoAlta) {
		this.mensajeAvisoAlta = formatoTexto(mensajeAvisoAlta);
	}
	public String getMensajeAvisoPendiente() {
		return mensajeAvisoPendiente;
	}
	public void setMensajeAvisoPendiente(String mensajeAvisoPendiente) {
		this.mensajeAvisoPendiente = formatoTexto(mensajeAvisoPendiente);
	}
	public String getMensajeNueveOnce() {
		return mensajeNueveOnce;
	}
	public void setMensajeNueveOnce(String mensajeNueveOnce) {
		this.mensajeNueveOnce = formatoTexto(mensajeNueveOnce);
	}
	
	public String getMensajeBajaAlerta() {
		return mensajeBajaAlerta;
	}
	
	public void setMensajeBajaAlerta(String mensajeBajaAlerta) {
		this.mensajeBajaAlerta = formatoTexto(mensajeBajaAlerta);
	}
	
	public String getMensajeNoServicio() {
		return mensajeNoServicio;
	}
	
	public void setMensajeNoServicio(String mensajeNoServicio) {
		this.mensajeNoServicio = formatoTexto(mensajeNoServicio);
	}
	
	public String getMensajeAlertaExistente() {
		return mensajeAlertaExistente;
	}
	public void setMensajeAlertaExistente(String mensajeAlertaExistente) {
		this.mensajeAlertaExistente = formatoTexto(mensajeAlertaExistente);
	}
	private String formatoTexto(String original){
		return original.replace(" ", "%20");
	}
	
	@SuppressWarnings("unused")
	private void inicializar(){
		System.out.println("Sistema inicializado!");
		
	}
	
	
	
}
