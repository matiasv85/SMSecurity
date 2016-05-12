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
	
	
	private static String QRY_OBTENER = "SELECT * "
			+"FROM "+TABLA;
	
	private static String QRY_INSERT = "INSERT INTO alertas(telefono, "
			+ " direccion, "
			+ "	duracion, "
			+ "	fecha_activacion) "
			+ "VALUES(?,?,?,?) ";
	
	private static String QRY_DELETE = "DELETE FROM alertas WHERE telefono = ? and direccion = ?";
	
	Connection conn = null;
	
	public List<AlertaTbl> obtener(){
		List<AlertaTbl> alertas = new ArrayList<AlertaTbl>();
		try {
			conn = dataSource.getConnection();
			
			PreparedStatement ps = conn.prepareStatement(QRY_OBTENER);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				alertas.add(new AlertaTbl(rs.getString("telefono"),
						rs.getInt("duracion"),
						sdf.parse(rs.getObject("fecha_activacion").toString()),
						rs.getString("direccion")));
			}
			ps.close();
 
		} catch (Exception e) {
			log.error(e.getMessage());
 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
			
		}
		return alertas;
	}
	
	public void guardar(AlertaTbl alerta){
		try {
			conn = dataSource.getConnection();
			
			PreparedStatement ps = conn.prepareStatement(QRY_INSERT);
			ps.setString(1, alerta.getTelefono());
			ps.setString(2, alerta.getDir());
			ps.setInt(3, alerta.getDuracion());
			ps.setObject(4, sdf.format(new Date()));
			
			ps.execute();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
			
		}
	}
	
	public void eliminarPorId(String id, String dir){
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(QRY_DELETE);
			ps.setString(1, id);
			ps.setString(2, dir);
			ps.execute();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
			
		}
	}
	
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
