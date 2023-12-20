package edu.cotarelo.dao.mysql;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingException;

import edu.cotarelo.dao.objects.PartidoDAO;
import edu.cotarelo.domain.Club;
import edu.cotarelo.domain.Partido;

public class MySQLPartidoDAO implements PartidoDAO {

	@Override
	public int insertar(Partido partido) throws NamingException {		
		Integer salida = -1;
		PreparedStatement ps = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();
		Club club1 = partido.getIdClub1();
		Club club2 = partido.getIdClub2();
		try {
			if(connection.abreConexion(null)&&club1!=null&&club2!=null) {					
				String sql = "INSERT into partidos (IdClub1,IdClub2) VALUES (?,?)";
				ps = connection.pStatementGK(sql,Statement.NO_GENERATED_KEYS);				
				if (ps!=null) {
					ps.setString(1, club1.getNombre());
					ps.setString(2, club2.getNombre());
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
	public int borrar(Partido partido) throws NamingException {		
		Integer salida = -1;
		PreparedStatement ps = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {					
				String sql = "Delete from partidos where IdClub1='"+partido.getIdClub1().getNombre()+"' and IdClub2='"+partido.getIdClub2().getNombre()+"'";
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
	public int modificar(Partido partido,Partido nuevopartido) throws NamingException {		
		Integer salida = -1;
		PreparedStatement ps = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();
		if(estaPartidoEnBBDD(partido)&&!estaPartidoEnBBDD(nuevopartido)) {
			try {
				if(connection.abreConexion(null)) {					
					String sql = "UPDATE partidos SET IdClub1 = '"+nuevopartido.getIdClub1().getNombre()+"', IdClub2='"+ nuevopartido.getIdClub2().getNombre()+"' WHERE partidos.IdClub1 ='"+partido.getIdClub1().getNombre()+"' AND partidos.IdClub2 ='"+partido.getIdClub2().getNombre()+"'";
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
		}
		return salida;
	}
	@SuppressWarnings({ "unused" })
	@Override
	public Partido getPartidoById(String IdClub1, String IdClub2) throws NamingException {
		Partido partido = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();				
		try {
			if(connection.abreConexion(null)) {					
				String sql = "SELECT * from partidos WHERE IdClub1='"+IdClub1+"' and IdClub2='"+IdClub2+"'";
				ps = connection.pStatementGK(sql,Statement.NO_GENERATED_KEYS);
				if (ps!=null) {
					rs = ps.executeQuery();
					Club club1 =null;
					Club club2 = null;
					java.util.Date fecha = null;
					while (rs.next()) {
						club1 = new Club();club2 = new Club();
						club1.setIdClub(rs.getString("IdClub1"));
						club1.setNombre(rs.getString("IdClub1"));
						club2.setIdClub(rs.getString("IdClub2"));
						club2.setNombre(rs.getString("IdClub2"));
						fecha = (java.util.Date) rs.getTimestamp("fecha");
					}
					if (club1!=null&&club2!=null) {
						partido = new Partido(club1,club2,fecha);
					}
				}				
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {//cerramos la conexión
			connection.cierraConexion(ps);
		}
		return partido;
	}
	
	@Override
	public boolean estaPartidoEnBBDD(Partido partido) {
		/* Verifica si el partido está en la BBDD;utiliza el identificador y la contraseña (está en MD5 en el objeto) del partido para la comparación
		 * Si está devuelve true , en caso contrario false
		 */
		boolean salida = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();	

		if(partido!=null) {//Si existe el Partido
			try {//Verificamos que no está asociado a ningún club
				if(connection.abreConexion(null)) {	
					ps = connection.pStatement("SELECT * from partidos WHERE IdClub1='"+partido.getIdClub1().getNombre()+"' and IdClub2='"+partido.getIdClub2().getNombre()+"'");
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
	
	@Override
	public ArrayList<Partido> getlistaPartidos() {
		/*	recupera la lista de jugadores
		 * 	Todos | jugadores sinclub
		 */
				ArrayList<Partido>lista = new ArrayList<Partido>();
				PreparedStatement ps = null;
				ResultSet rs = null;
				MySQLConexionDAO connection =  new MySQLConexionDAO();	
				try {
					if(connection.abreConexion(null)) {	
						String Sql = "Select * from partidos";
						ps = connection.pStatement(Sql);
						rs = ps.executeQuery();	
						while (rs.next()){
							Partido partido = new Partido();
							Club club1 = new Club();
							club1.setIdClub(rs.getString("IdClub1"));
							club1.setNombre(rs.getString("IdClub1"));
							Club club2 = new Club();
							club2.setIdClub(rs.getString("IdClub2"));
							club2.setNombre(rs.getString("IdClub2"));
							partido.setIdClub1(club1);
							partido.setIdClub2(club2);
							lista.add(partido);	
						}
					}
				}catch (SQLException | ClassNotFoundException | NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {//cerramos la conexión
					connection.cierraConexion(ps);
				}

		return lista;
	}

	@Override
	public Hashtable<Integer,String> getlistaPartidosSelect() {
		/*	recupera la lista de jugadores
		 * 	Todos | jugadores sinclub
		 */
				Hashtable<Integer,String>lista = new Hashtable<Integer,String>();
				PreparedStatement ps = null;
				ResultSet rs = null;
				MySQLConexionDAO connection =  new MySQLConexionDAO();	
				try {
					if(connection.abreConexion(null)) {	
						String Sql = "Select * from partidos";
						ps = connection.pStatement(Sql);
						rs = ps.executeQuery();	
						while (rs.next()){
							//guardamos las listaPosiciones posibles en la tabla Hash
							lista.put(rs.getRow(),rs.getString("IdClub1")+" | "+rs.getString("IdClub2"));
	
						}
					}
				}catch (SQLException | ClassNotFoundException | NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {//cerramos la conexión
					connection.cierraConexion(ps);
				}

		return lista;
	}	
	
	@Override
	public boolean estaClubEnPartido(Club club) {
		/* Verifica si el partido está en la BBDD;utiliza el identificador y la contraseña (está en MD5 en el objeto) del partido para la comparación
		 * Si está devuelve true , en caso contrario false
		 */
		boolean salida = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MySQLConexionDAO connection =  new MySQLConexionDAO();
		if(club!=null) {//Si existe el Partido
			try {//Verificamos que no está asociado a ningún club
				if(connection.abreConexion(null)) {	
					ps = connection.pStatement("SELECT * from partidos WHERE IdClub1='"+club.getIdClub()+"' or IdClub2='"+club.getIdClub()+"'");
					rs = ps.executeQuery();
					if(rs.next()) {
						salida=true;
					}				
				}
			} catch (SQLException | ClassNotFoundException | NamingException e)  {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {//cerramos la conexión
				connection.cierraConexion(ps);
			}
		}
		return salida;
	}

}
