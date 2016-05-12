package awers.proceso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import awers.beans.Alerta;
import awers.modelo.dao.AlertaDAO;
import awers.modelo.dao.ClienteDAO;
import awers.modelo.tablas.AlertaTbl;
import awers.modelo.tablas.Cliente;

public class AdministradorDeHilos implements ApplicationContextAware  {
	public HashMap<String,Alerta> alertas;
	private ApplicationContext contexto;
	private ClienteDAO clienteDao;
	private AlertaDAO alertaDao;
	
	static Logger log = Logger.getLogger(AdministradorDeHilos.class.getName());

	private Cliente cliente;
	
	
	public AdministradorDeHilos(){
		this.alertas = new HashMap<String, Alerta>();
		
		
	}
	
	public void anularAlerta(String id, String dir){
		this.alertas.get(id+"_"+dir).anularAlerta();
		this.alertas.remove(id+"_"+dir);
		alertaDao.eliminarPorId(id, dir);
	}

	public void levantarAlerta(String id, String dir) {
			try{
				//Verificar primero que es cliente en la BD
				//Si es cliente, obtener los datos del tiempo que tiene que esperar
				cliente = clienteDao.obtener(id, dir);
				if(cliente != null){
					if(!this.alertas.containsKey(cliente.getIdAlerta())){				
						// llamar a una URL para indicar el alta del servicio
						((InterfazPHP) this.contexto.getBean("interfazPHP")).informarRegistroAlerta(cliente);
						
						//Método privado que crea el Thread -1 significa que tiene que tomar el tiempo del cliente
						this.crearAlerta(id, cliente.getTiempoEspera(), dir, cliente);
						
						this.registrarAlerta(cliente);
					}
					else{
						((InterfazPHP) this.contexto.getBean("interfazPHP")).informarAlertaExistente(cliente);
					}
				}
				else{
					((InterfazPHP) this.contexto.getBean("interfazPHP")).informarNoServicio(id);
				}
			}
			catch(Exception exc){
				log.error(exc.getMessage());
			}
			
	}
		
	public void setClienteDao(ClienteDAO clienteDao) {
		this.clienteDao = clienteDao;
	}
	
	public void setAlertaDao(AlertaDAO alertaDao) {
		this.alertaDao = alertaDao;
	}
	
	private void registrarAlerta(Cliente cliente){
		alertaDao.guardar(new AlertaTbl(cliente.getTelefono(),cliente.getTiempoEspera(), cliente.getDir()));
	}
	
	public void inializarAlertasPendientes(){
		try{
			List<AlertaTbl> alertas =  alertaDao.obtener();
			Iterator<AlertaTbl> i = alertas.iterator();
			DateTime ahora = new DateTime();
			
			while(i.hasNext()){
				AlertaTbl alerta = i.next();
				DateTime fechaActivacion = new DateTime(alerta.getFechaActivacion());
				Duration duration = new Duration(fechaActivacion, ahora);
				Long restantes = duration.getStandardMinutes();
				if(restantes < alerta.getDuracion()){
					int tiempoEspera = ((int) (alerta.getDuracion() - restantes));
					this.crearAlerta(alerta.getTelefono(), tiempoEspera, alerta.getDir(), null);
				}
				
		
			}
		}
		catch(Exception exc){
			log.error(exc.getMessage());
		}
		
	}
	
	
	private void crearAlerta(String id, int tiempoPrimerAlerta, String dir, Cliente c){
		cliente = (c != null) ? c : clienteDao.obtener(id, dir);
		
		Alerta alerta = new Alerta(id, (InterfazPHP) this.contexto.getBean("interfazPHP"), cliente, tiempoPrimerAlerta);
		
		alertas.put(id+"_"+dir,alerta);
		new Thread(alerta).start();
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext contexto) throws BeansException {
		this.contexto = contexto;
		
	}
}
