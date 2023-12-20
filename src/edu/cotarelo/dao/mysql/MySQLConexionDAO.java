package edu.cotarelo.dao.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.NamingException;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.sql.DataSource;

public class MySQLConexionDAO {

    /*private static final String PARAM_CONTEXTO_BBDD = "jdbc/Obligatoria";
	private Context initCtx ;	
	private static Context envCtx = null;
	private static DataSource dataSource;*/
    private Connection connection = null;

    public boolean abreConexion(Context context) throws NamingException, ClassNotFoundException {
        boolean salida = false;
        try {
            /*this.initCtx = new InitialContext();			
			MySQLConexionDAO.envCtx = (Context) initCtx.lookup("java:comp/env");
			MySQLConexionDAO.dataSource = (DataSource) MySQLConexionDAO.envCtx.lookup(PARAM_CONTEXTO_BBDD);			
			this.connection = MySQLConexionDAO.dataSource.getConnection();*/
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dad2", "root", "abc123.");
            salida = true;
            //} catch (NamingException | SQLException e) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salida;
    }

    public void cierraConexion(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            if (this.connection != null && !this.connection.isClosed()) {
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

    public PreparedStatement prepareStatement(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    public PreparedStatement pStatementGK(String string, int num) throws SQLException {
        return this.connection.prepareStatement(string, num);
    }

    public PreparedStatement pStatement(String string) throws SQLException {
        return this.connection.prepareStatement(string);
    }

}
