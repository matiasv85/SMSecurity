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

public class AdministradorDeHilos implements ApplicationContextAware {
	public HashMap<String, Alerta> alertas;
	private ApplicationContext contexto;
	private ClienteDAO clienteDao;
	private AlertaDAO alertaDao;

	static Logger log = Logger.getLogger(AdministradorDeHilos.class.getName());

	private Cliente cliente;

	public AdministradorDeHilos() {
		this.alertas = new HashMap<String, Alerta>();

	}

	public void anularAlerta(String clientNumber, String codigoAlerta) {
		String idAlerta = clientNumber + "_" + codigoAlerta;
		this.alertas.get(idAlerta).anularAlerta();
		this.alertas.remove(idAlerta);
		this.alertaDao.eliminarPorId(clientNumber, codigoAlerta);
	}

	public void levantarAlerta(String clientNumber, String telefono, int tiempoDuracionAlerta,
	        String codigoAlerta) {
		String idAlerta = clientNumber + "_" + codigoAlerta;
		try {
			if (!this.alertas.containsKey(idAlerta)) {
				// llamar a una URL para indicar el alta del servicio
				((InterfazPHP) this.contexto.getBean("interfazPHP"))
				        .informarRegistroAlerta(this.cliente);

				// M�todo privado que crea el Thread -1 significa que tiene que
				// tomar el tiempo del cliente
				this.crearAlerta(clientNumber, tiempoDuracionAlerta, codigoAlerta, idAlerta);

				this.registrarAlerta(clientNumber, tiempoDuracionAlerta, telefono, codigoAlerta);
			} else {
				((InterfazPHP) this.contexto.getBean("interfazPHP"))
				        .informarAlertaExistente(this.cliente);
			}
		} catch (Exception exc) {
			log.error(exc.getMessage());
		}

	}

	private void registrarAlerta(String clientNumber, int tiempoDuracionAlerta, String telefono,
	        String codigoAlerta) {
		this.alertaDao.guardar(new AlertaTbl(clientNumber, telefono, codigoAlerta,
		        tiempoDuracionAlerta));

	}

	public void setClienteDao(ClienteDAO clienteDao) {
		this.clienteDao = clienteDao;
	}

	public void setAlertaDao(AlertaDAO alertaDao) {
		this.alertaDao = alertaDao;
	}

	public void inializarAlertasPendientes() {
		try {
			List<AlertaTbl> alertas = this.alertaDao.obtener();
			Iterator<AlertaTbl> i = alertas.iterator();
			DateTime ahora = new DateTime();

			while (i.hasNext()) {
				AlertaTbl alerta = i.next();
				DateTime fechaActivacion = new DateTime(alerta.getFechaActivacion());
				Duration duration = new Duration(fechaActivacion, ahora);
				Long restantes = duration.getStandardMinutes();
				if (restantes < alerta.getDuracion()) {
					int tiempoEspera = ((int) (alerta.getDuracion() - restantes));
					this.crearAlerta(alerta.getClientNumber(), tiempoEspera,
					        alerta.getCodigoAlerta(), null);
				}

			}
		} catch (Exception exc) {
			log.error(exc.getMessage());
		}

	}

	private void crearAlerta(String clientNumber, int tiempoPrimerAlerta, String codigoAlerta,
	        String idAlerta) {

		Alerta alerta = new Alerta(clientNumber,
		        (InterfazPHP) this.contexto.getBean("interfazPHP"), tiempoPrimerAlerta,
		        codigoAlerta);

		this.alertas.put(clientNumber + "_" + codigoAlerta, alerta);
		new Thread(alerta).start();
	}

	@Override
	public void setApplicationContext(ApplicationContext contexto) throws BeansException {
		this.contexto = contexto;

	}
}
