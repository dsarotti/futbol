package edu.cotarelo.dao.mysql;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingException;

import edu.cotarelo.dao.objects.ClubDAO;
import edu.cotarelo.domain.Club;

public class MySQLClubDAO implements ClubDAO {

	@Override
	public int insertar(Club club) throws NamingException {		
		Integer salida = -1;
		PreparedStatement ps = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {					
				String sql = "INSERT into clubs (nombre,descripcion,campo) VALUES (?,?,?)";
				ps = connection.pStatementGK(sql,Statement.NO_GENERATED_KEYS);
				
				if (ps!=null) {
					ps.setString(1, club.getNombre());
					ps.setString(2, club.getDescripcion());
					ps.setString(3,club.getCampo());
					int rowAffected = ps.executeUpdate();
					if(rowAffected == 1){
		                salida = 1;
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
	public int borrar(Club club) throws NamingException {		
		Integer salida = -1;
		PreparedStatement ps = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {					
				String sql = "Delete from clubs where nombre='"+club.getIdClub()+"'";
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
	public int modificar(Club nuevoclub,Club elclub) throws NamingException {		
		Integer salida = -1;
		PreparedStatement ps = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {					
				String sql = "UPDATE clubs SET nombre = '"+nuevoclub.getNombre()+"', descripcion='"+nuevoclub.getDescripcion()+"', campo='"+nuevoclub.getCampo()+"'  WHERE nombre ='"+elclub.getIdClub()+"'";
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
	public Club getClubById(String id) throws NamingException {
		Club club = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {					
				ps = connection.pStatement("SELECT * from clubs WHERE nombre='"+id+"'");
				rs = ps.executeQuery();
				while (rs.next()) {
					club = new Club();
					club.setIdClub(rs.getString("nombre"));
					club.setNombre(rs.getString("nombre"));
					club.setDescripcion(rs.getString("descripcion"));
					club.setCampo(rs.getString("campo"));
				}				
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
		return club;
	}
	@Override
	public boolean estaClubEnBBDD(Club club) {
		/* Verifica si el club está en la BBDD;utiliza el identificador y la contraseña (está en MD5 en el objeto) del club para la comparación
		 * Si está devuelve true , en caso contrario false
		 */
		boolean salida = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();	

		if(club!=null&&!club.getIdClub().equals("")) {//Si existe el Club
			try {//Verificamos que no está asociado a ningún club					
				ps = connection.pStatement("SELECT * from clubs WHERE nombre='"+club.getIdClub()+"'");
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
	public Hashtable<Integer,String> getListaSelect(int Todos) {
		/*recupera de BBDD la lista de clubs*/
		//Todos | clubs sin jugadores
		String sql = "";
		if (Todos==1)//todos los clubs
			sql = "Select clubs.* from clubs";
		else if (Todos==2)//sin jugadores
			sql="SELECT clubs.* FROM clubs WHERE clubs.nombre NOT IN (Select DISTINCT partidos.IdClub1 from partidos) AND clubs.nombre NOT IN (Select DISTINCT partidos.IdClub2 from partidos)";
		else//sin jugadores ni partidos -Todos=3
			sql= "SELECT clubs.* FROM clubs WHERE clubs.nombre NOT IN (Select DISTINCT partidos.IdClub1 from partidos) AND clubs.nombre NOT IN (Select DISTINCT partidos.IdClub2 from partidos) AND clubs.nombre NOT IN (SELECT DISTINCT jugadorclub.IdClub from jugadorclub)";
		Hashtable<Integer,String>lista = new Hashtable<Integer,String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();
		try {
			if(connection.abreConexion(null)) {					
				ps = connection.pStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					lista.put(rs.getRow(),rs.getString("nombre"));
				}				
			}	
		} catch (SQLException | ClassNotFoundException | NamingException e) {
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
			
		
		return lista;
	}
	@Override
	public ArrayList<Club> getLista(int Todos) {
		/*recupera de BBDD la lista de clubs*/
		//Todos | clubs sin jugadores
		String sql = "";
		if (Todos==1)//todos los clubs
			sql = "Select clubs.* from clubs";
		else if (Todos==2)//sin jugadores
			sql="SELECT clubs.*clubs WHERE clubs.nombre NOT IN (Select DISTINCT partidos.IdClub1 from partidos) AND clubs.nombre NOT IN (Select DISTINCT partidos.IdClub2 from partidos)";
		else//sin jugadores ni partidos -Todos=3
			sql= "SELECT clubs.* FROM clubs WHERE clubs.nombre NOT IN (Select DISTINCT partidos.IdClub1 from partidos) AND clubs.nombre NOT IN (Select DISTINCT partidos.IdClub2 from partidos) AND clubs.nombre NOT IN (SELECT DISTINCT jugadorclub.IdClub from jugadorclub)";
		ArrayList<Club>lista = new ArrayList<Club>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();
		try {
			if(connection.abreConexion(null)) {					
				ps = connection.pStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					Club club = new Club();
					//guardamos las lista de clubes en ArrayList
					club.setIdClub(rs.getString("nombre"));
					club.setNombre(rs.getString("nombre"));
					if (rs.getString("descripcion")!=null && rs.getString("descripcion")!="") {
						
						club.setDescripcion(rs.getString("descripcion"));
						
					}else {
						club.setDescripcion("");
					}
					if (rs.getString("campo")!=null && rs.getString("campo")!="") {
						
						club.setCampo(rs.getString("campo"));
						
					}else {
						club.setCampo("");
					}
					lista.add(club);
				}
			}	
		} catch (SQLException | ClassNotFoundException | NamingException e) {
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
			
		
		return lista;
	}
	@Override
	public boolean tieneJugadores(Club club) {
		/*Verifica si tiene jugadores asociados
		 * 
		 */
		boolean salida = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();	

		if(club!=null&&!club.getIdClub().equals("")) {//Si existe el Club
			try {//Verificamos que no está asociado a ningún club		
				if(connection.abreConexion(null)) {	
					ps = connection.pStatement("SELECT * from jugadorclub WHERE nombre='"+club.getIdClub()+"'");
					rs = ps.executeQuery();
					if(rs.next()) {
						salida=true;
					}
				}
			} catch (SQLException | ClassNotFoundException | NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {//cerramos la conexión
				connection.cierraConexion(ps);
			}
		}
		return salida;
	}
	

}
