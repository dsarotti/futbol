package edu.cotarelo.dao.mysql;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.NamingException;

import edu.cotarelo.dao.objects.AccionesDAO;

public class MySQLAccionesDAO implements AccionesDAO {
	
	private Hashtable<String,String> acciones=new Hashtable<String,String>();

	public Hashtable<String, String> getAcciones() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NamingException {
		MySQLConexionDAO connection =  new MySQLConexionDAO();	
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			if(connection.abreConexion(null)) {					
				ps = connection.pStatement("SELECT * from actions");
				rs = ps.executeQuery();	
				while (rs.next()){
					//instanciación por reflexión:  instanciamos en base al nombre de la clase
					//crea un objteto de la clase recogida de la tabla de acciones de la bd
					System.out.println("Cargando la acción: "+ rs.getString("class"));
					//guardamos las acciones posibles en la tabla Hash
					this.acciones.put(rs.getString("id"), rs.getString("class"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
		return this.acciones;
	}

	@Override
	public String getAccionById(String actionId) {
		if (this.acciones!=null) {
			return this.acciones.get(actionId);
		}else
			return null;
	}
}