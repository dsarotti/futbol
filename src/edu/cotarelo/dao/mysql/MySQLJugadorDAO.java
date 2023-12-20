package edu.cotarelo.dao.mysql;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingException;

import edu.cotarelo.dao.objects.JugadorDAO;
import edu.cotarelo.domain.Jugador;

public class MySQLJugadorDAO implements JugadorDAO {

	@Override
	public int insertar(Jugador jugador) throws NamingException {		
		Integer salida = -1;
		PreparedStatement ps = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {					
				String sql = "INSERT into jugadores (nombre,apellidos,posicion) VALUES (?,?,?)";
				ps = connection.pStatementGK(sql,Statement.RETURN_GENERATED_KEYS);
				
				if (ps!=null) {
					ps.setString(1, jugador.getNombre());
					ps.setString(2, jugador.getApellidos());
					ps.setString(3,jugador.getPosicion());
					int rowAffected = ps.executeUpdate();
					if(rowAffected == 1){
						ResultSet rs = ps.getGeneratedKeys();
			            if(rs.next())
			            {
			                salida = rs.getInt(1);
			            }
					}
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
		return salida;
	}
	@Override
	public int borrar(Jugador jugador) throws NamingException {		
		Integer salida = -1;
		PreparedStatement ps = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {					
				String sql = "Delete from jugadores where idJugador=?";
				ps = connection.pStatementGK(sql,Statement.NO_GENERATED_KEYS);
				
				if (ps!=null) {
					ps.setInt(1, jugador.getIdJugador());
					int rowAffected = ps.executeUpdate();
					if(rowAffected == 1){
						salida=1;
					}
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
		return salida;
	}
	@Override
	public int modificar(Jugador jugador) throws NamingException {		
		Integer salida = -1;
		PreparedStatement ps = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {					
				String sql = "UPDATE jugadores SET apellidos = '"+jugador.getApellidos()+"', nombre='"+jugador.getNombre()+"', posicion='"+jugador.getPosicion()+"'  WHERE jugadores.IdJugador ="+jugador.getIdJugador();
				ps = connection.pStatementGK(sql,Statement.NO_GENERATED_KEYS);
				if (ps!=null) {
					int rowAffected = ps.executeUpdate();
					if(rowAffected == 1){
						salida=1;
					}
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
		return salida;
	}
	@Override
	public Jugador getJugadorById(int id) throws NamingException {
		Jugador jugador = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {
				String sql = "SELECT * from jugadores WHERE idJugador="+id;
				ps = connection.pStatement(sql);
		        ps.executeQuery(sql);
		        rs = ps.getResultSet();
				if (rs.next()) {
					jugador = new Jugador();
					String nombre = rs.getString("nombre");
					jugador.setNombre(nombre);
					jugador.setApellidos(rs.getString("apellidos"));
					jugador.setPosicion(rs.getString("posicion"));
					jugador.setIdJugador(rs.getInt("IdJugador"));					
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
		return jugador;
	}
	

	@Override
	public boolean estaJugadorEnBBDD(Jugador jugador) {
		/* Verifica si el jugador está en la BBDD;utiliza el identificador y la contraseña (está en MD5 en el objeto) del jugador para la comparación
		 * Si está devuelve true , en caso contrario false
		 */
		boolean salida = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();	

		if(jugador!=null&&jugador.getIdJugador()>0) {//Si existe el Jugador
			try {//Verificamos que no está asociado a ningún club					
				ps = connection.pStatement("SELECT * from jugador WHERE idJugador="+jugador.getIdJugador());
				rs = ps.executeQuery();
				if(rs.next()) {
					salida=true;
				}				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {//cerramos la conexión
				connection.cierraConexion(ps);
			}
		}
		return salida;
	}
	@Override
	public Hashtable<Integer, String> getListaPosiciones() {
		Hashtable<Integer, String> lista = new Hashtable<Integer, String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {					
				ps = connection.pStatement("Select * from posicionjugador");
				rs = ps.executeQuery();
				while (rs.next()) {
					lista.put(rs.getRow(),rs.getString("IdPosicionJugador"));
				}				
			}
		} catch (SQLException | ClassNotFoundException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
		return lista;
	}
	@Override
	public ArrayList<Jugador> getLista(int tipo) {
		/*	recupera la lista de jugadores
		 * 	Todos | jugadores sinclub
		 */
		ArrayList<Jugador> lista = new ArrayList<Jugador>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();	
		String sql;
		if (tipo==1) {//Todos los jugadores
			sql = "Select * from jugadores";
		}else if (tipo==0) {//Todos los jugadores sin club
			sql = "Select jugadores.* from jugadores Where jugadores.IdJugador NOT IN (Select jugadorclub.idJugador from jugadorclub)";
		}else {//Todos los jugadores que tienen club
			sql = "Select jugadores.* from jugadores,jugadorclub Where jugadores.IdJugador=jugadorclub.IdJugador";
		}	
		try {
			if(connection.abreConexion(null)) {					
				ps = connection.pStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					Jugador jugador = new Jugador();
					//guardamos las lista de jugadores en la tabla Hash
					if (rs.getString("apellidos")!=null && rs.getString("apellidos")!="") {
						jugador.setIdJugador(rs.getInt("IdJugador"));
						jugador.setNombre(rs.getString("nombre"));
						jugador.setApellidos(rs.getString("apellidos"));
						
					}else {
						jugador.setIdJugador(rs.getInt("IdJugador"));
						jugador.setNombre(rs.getString("nombre"));
						jugador.setApellidos("");
					}
					lista.add(jugador);
				}
			}			
		} catch (SQLException | ClassNotFoundException | NamingException e) {
					// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
		return lista;
	}	
	public Hashtable<Integer, String> getListaSelect(int tipo) {
		/*	recupera la lista de jugadores
		 * 	Todos | jugadores sinclub
		 */
		Hashtable<Integer, String> lista = new Hashtable<Integer, String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();	
		String sql;
		if (tipo==1) {//Todos los jugadores
			sql = "Select * from jugadores";
		}else if (tipo==0) {//Todos los jugadores sin club
			sql = "Select jugadores.* from jugadores Where jugadores.IdJugador NOT IN (Select jugadorclub.idJugador from jugadorclub)";
		}else {//Todos los jugadores que tienen club
			sql = "Select jugadorclub.*,jugadores.apellidos,jugadores.nombre from jugadores,jugadorclub Where jugadores.IdJugador=jugadorclub.IdJugador";
		}	
		try {
			if(connection.abreConexion(null)) {					
				ps = connection.pStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
				//guardamos las lista de jugadores en la tabla Hash
					if (tipo==2) {//guardamos el club y el jugador
						if (rs.getString("apellidos")!=null && rs.getString("apellidos")!="") {
							lista.put(rs.getRow(),rs.getString("IdClub")+"|"+rs.getInt("IdJugador")+"|"+rs.getString("nombre")+" "+rs.getString("apellidos"));
						}else {
							lista.put(rs.getRow(),rs.getString("IdClub")+"|"+rs.getInt("IdJugador")+"|"+rs.getString("nombre"));
						}							
					}else {
						if (rs.getString("apellidos")!=null && rs.getString("apellidos")!="") {
							
							lista.put(rs.getInt("IdJugador"),rs.getString("nombre")+" "+rs.getString("apellidos"));
						}else {
							lista.put(rs.getInt("IdJugador"),rs.getString("nombre"));
						}
					}
				}
			}
			
		} catch (SQLException | ClassNotFoundException | NamingException e) {
					// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
		return lista;
	}		
	

}
