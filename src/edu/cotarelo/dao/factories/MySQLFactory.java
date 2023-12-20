package edu.cotarelo.dao.factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.cotarelo.dao.objects.AccionesDAO;
import edu.cotarelo.dao.objects.Factory;
import edu.cotarelo.dao.objects.UsuarioDAO;
import edu.cotarelo.dao.objects.JugadorDAO;
import edu.cotarelo.dao.objects.PartidoDAO;
import edu.cotarelo.dao.objects.ClubDAO;
import edu.cotarelo.dao.mysql.MySQLAccionesDAO;
import edu.cotarelo.dao.mysql.MySQLUsuarioDAO;
import edu.cotarelo.dao.mysql.MySQLJugadorDAO;
import edu.cotarelo.dao.mysql.MySQLClubDAO;
import edu.cotarelo.dao.mysql.MySQLPartidoDAO;

public class MySQLFactory implements Factory{
	private static final String PARAM_CONTEXTO_BBDD = "jdbc/dad2";
	private Context initCtx ;	
        public static Context envCtx = null;
        public static DataSource dataSource;
	private Connection connection = null;
	
	@Override
	public UsuarioDAO getUsuarioDAO() {		
		return new MySQLUsuarioDAO();
	}
	@Override
	public AccionesDAO getAccionesDAO() {
		return new MySQLAccionesDAO();
	}
	@Override
	public JugadorDAO getJugadorDAO() {
		return new MySQLJugadorDAO();
	}
	@Override
	public ClubDAO getClubDAO() {
		return new MySQLClubDAO();
	}	
	@Override
	public PartidoDAO getPartidoDAO() {
		return new MySQLPartidoDAO();
	}
	
	public boolean abreConexion() throws NamingException {
		boolean salida = false;
		try {
                        //String myBD = "jdbc:syssql://localhost:3306/dad2?serverTimezone=UTC";
			this.initCtx = new InitialContext();			
			MySQLFactory.envCtx = (Context) initCtx.lookup("java:comp/env");
			MySQLFactory.dataSource = (DataSource) MySQLFactory.envCtx.lookup(PARAM_CONTEXTO_BBDD);			
			this.connection = MySQLFactory.dataSource.getConnection();
			/*Class.forName("com.mysql.cj.jdbc.Driver");
                        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dad2", "root", "");*/
			salida = true;
		//} catch (NamingException | SQLException e) {
		} catch ( SQLException e) {
			e.printStackTrace();
		}
		return salida;
	}

	
	public void cierraConexion(PreparedStatement ps) {
		if(ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			if(this.connection != null && !this.connection.isClosed()) {
				try {
					this.connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public PreparedStatement prepareStatement(String string) throws SQLException {
		return this.pStatement(string) ;
	}


	public PreparedStatement pStatement(String string) throws SQLException {
		return this.connection.prepareStatement(string);
	}


}
