package awers.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import awers.modelo.tablas.AlertaTbl;

public class AlertaDAO {
	private DataSource dataSource;
	private static String TABLA = "alertas";

	static Logger log = Logger.getLogger(AlertaDAO.class.getName());
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String QRY_OBTENER = "SELECT * " + "FROM " + TABLA;

	private static String QRY_INSERT = "INSERT INTO alertas(client_number, " + " telefono, "
	        + " codigo_alerta, " + "	duracion, " + "	fecha_activacion) " + "VALUES(?,?,?,?,?) ";

	private static String QRY_DELETE = "DELETE FROM alertas WHERE client_number = ? and codigo_alerta = ?";

	Connection conn = null;

	public List<AlertaTbl> obtener() {
		List<AlertaTbl> alertas = new ArrayList<AlertaTbl>();
		try {
			this.conn = this.dataSource.getConnection();

			PreparedStatement ps = this.conn.prepareStatement(QRY_OBTENER);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				alertas.add(new AlertaTbl(rs.getString("client_number"), rs.getString("telefono"),
				        rs.getString("codigo_alerta"), rs.getInt("duracion"), this.sdf.parse(rs
				                .getObject("fecha_activacion").toString())));
			}
			ps.close();

		} catch (Exception e) {
			log.error(e.getMessage());

		} finally {
			if (this.conn != null) {
				try {
					this.conn.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}

		}
		return alertas;
	}

	public void guardar(AlertaTbl alerta) {
		try {
			this.conn = this.dataSource.getConnection();

			PreparedStatement ps = this.conn.prepareStatement(QRY_INSERT);
			ps.setString(1, alerta.getClientNumber());
			ps.setString(2, alerta.getTelefono());
			ps.setString(3, alerta.getCodigoAlerta());
			ps.setInt(4, alerta.getDuracion());
			ps.setObject(5, this.sdf.format(new Date()));

			ps.execute();

		} catch (SQLException e) {
			log.error(e.getMessage());

		} finally {
			if (this.conn != null) {
				try {
					this.conn.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}

		}
	}

	public void eliminarPorId(String id, String dir) {
		try {
			this.conn = this.dataSource.getConnection();
			PreparedStatement ps = this.conn.prepareStatement(QRY_DELETE);
			ps.setString(1, id);
			ps.setString(2, dir);
			ps.execute();

		} catch (SQLException e) {
			log.error(e.getMessage());

		} finally {
			if (this.conn != null) {
				try {
					this.conn.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}

		}
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
