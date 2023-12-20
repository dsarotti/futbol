package edu.cotarelo.dao.mysql;

import edu.cotarelo.dao.factories.MySQLFactory;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingException;

import edu.cotarelo.dao.objects.UsuarioDAO;
import edu.cotarelo.domain.Usuario;

public class MySQLUsuarioDAO implements UsuarioDAO {

    @Override
    public int insertar(Usuario usuario) throws NamingException {
        Integer salida = -1;
        PreparedStatement ps = null;
        MySQLConexionDAO connection = new MySQLConexionDAO();
        try {
            if (connection.abreConexion(null)) {
                String sql = "INSERT into users (nombre,apellidos,pass,rol) VALUES (?,?,?,?)";
                ps = connection.pStatementGK(sql, Statement.RETURN_GENERATED_KEYS);
                if (ps != null) {
                    ps.setString(1, usuario.getNombre());
                    ps.setString(2, usuario.getApellidos());
                    //guardamos la pass con md5 en MySql
                    ps.setString(3, md5(usuario.getClave()));
                    ps.setString(4, usuario.getRol().equals("") ? "normal" : usuario.getRol());
                    int rowAffected = ps.executeUpdate();
                    if (rowAffected == 1) {
                        ResultSet rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            salida = rs.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {//cerramos la conexión
            connection.cierraConexion(ps);
        }
        return salida;
    }

    @Override
    public int borrar(Usuario usuario) throws NamingException {
        Integer salida = -1;
        PreparedStatement ps = null;
        MySQLConexionDAO connection = new MySQLConexionDAO();
        try {
            if (connection.abreConexion(null)) {
                String sql = "Delete from users where idUser=?";
                ps = connection.pStatementGK(sql, Statement.NO_GENERATED_KEYS);
                if (ps != null) {
                    ps.setInt(1, usuario.getIdUsuario());
                    int rowAffected = ps.executeUpdate();
                    if (rowAffected == 1) {
                        salida = 1;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {//cerramos la conexión
            connection.cierraConexion(ps);
        }
        return salida;
    }

    @Override
    public int modificar(Usuario usuario) throws NamingException {
        Integer salida = -1;
        PreparedStatement ps = null;
        MySQLConexionDAO connection = new MySQLConexionDAO();
        try {
            if (connection.abreConexion(null)) {
                String sql = "";
                if (usuario.getClave().equals("")) {
                    sql = "Update users SET nombre='" + usuario.getNombre() + "', apellidos='" + usuario.getApellidos() + "', rol='" + usuario.getRol() + "' where idUser=" + usuario.getIdUsuario();
                } else {
                    sql = "Update users SET nombre='" + usuario.getNombre() + "', apellidos='" + usuario.getApellidos() + "', rol='" + usuario.getRol() + "', pass='" + MySQLUsuarioDAO.md5(usuario.getClave()) + "' where idUser=" + usuario.getIdUsuario();
                }
                ps = connection.pStatementGK(sql, Statement.NO_GENERATED_KEYS);
                if (ps != null) {
                    int rowAffected = ps.executeUpdate();
                    if (rowAffected == 1) {
                        salida = 1;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {//cerramos la conexión
            connection.cierraConexion(ps);
        }
        return salida;
    }

    @Override
    public Usuario getUsuarioById(int id) throws NamingException {
        Usuario usuario = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MySQLConexionDAO connection = new MySQLConexionDAO();
        try {
            if (connection.abreConexion(null)) {
                String sql = "SELECT * from users WHERE idUser=" + id;
                ps = connection.pStatement(sql);
                ps.executeQuery(sql);
                rs = ps.getResultSet();
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("idUser"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellidos(rs.getString("apellidos"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setClave(rs.getString("pass"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {//cerramos la conexión
            connection.cierraConexion(ps);
        }
        return usuario;
    }

    @Override
    public Usuario getUsuarioByNombreContraseña(String nombre, String pass) throws NamingException {
        Usuario usuario = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MySQLConexionDAO connection = new MySQLConexionDAO();
        try {
            if (connection.abreConexion(null)) {
                String sql = "SELECT * from users WHERE nombre='" + nombre + "' and pass='" + md5(pass) + "'";
                ps = connection.pStatement(sql);
                ps.execute();
                rs = ps.getResultSet();
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellidos(rs.getString("apellidos"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setClave(rs.getString("pass"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {//cerramos la conexión
            connection.cierraConexion(ps);
        }
        return usuario;
    }

    @Override
    public boolean estaUsuarioEnBBDD(Usuario user) {
        /* Verifica si el usuario está en la BBDD;utiliza el identificador y la contraseña (está en MD5 en el objeto) del usuario para la comparación
		 * Si está devuelve true , en caso contrario false
         */
        boolean salida = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MySQLConexionDAO connection = new MySQLConexionDAO();

        if (user != null && user.getIdUsuario() > 0) {//Si existe el Jugador
            try {//Verificamos que no está asociado a ningún club					
                String sql = "SELECT * FROM users WHERE IdUser ='" + user.getIdUsuario() + "' AND nombre='" + user.getNombre() + "' AND pass='" + user.getClave() + "'";
                ps = connection.pStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    salida = true;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {//cerramos la conexión
                connection.cierraConexion(ps);
            }
        }
        return salida;
    }

    public ArrayList<Usuario> getListaUsuarios(int tipo) throws ClassNotFoundException {
        /*	recupera la lista de jugadores
		 * 	Todos | jugadores sinclub
         */

        String sql;
        if (tipo == 1) {//Todos los usuarios
            sql = "Select * from users";
        } else if (tipo == 0) {//Usuarios Administradores
            sql = "Select users.* from users where users.rol='admin'";
        } else {//Usuarios no administradores
            sql = "Select users.* from users where users.rol<>'admin'";
        }

        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        MySQLConexionDAO connection = new MySQLConexionDAO();
        try {
            if (connection.abreConexion(null)) {
                ps = connection.pStatement(sql);
                ps.executeQuery(sql);
                rs = ps.getResultSet();
                while (rs.next()) {
                    //guardamos en la tabla Hash
                    Usuario user = new Usuario();
                    user.setIdUsuario(rs.getInt("IdUser"));
                    user.setNombre(rs.getString("nombre"));
                    user.setRol(rs.getString("rol"));
                    if (rs.getString("apellidos") != null && rs.getString("apellidos") != "") {
                        user.setApellidos(rs.getString("apellidos"));
                    }
                    lista.add(user);
                }
            }

        } catch (SQLException | ClassNotFoundException | NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {//cerramos la conexión
            connection.cierraConexion(ps);
        }
        return lista;
    }

    public Hashtable<Integer, String> getListaUsuariosSelect(int tipo) throws ClassNotFoundException {
        /*	recupera la lista de jugadores
		 * 	Todos | jugadores sinclub
         */

        String sql;
        if (tipo == 1) {//Todos los usuarios
            sql = "Select * from users";
        } else if (tipo == 0) {//Usuarios Administradores
            sql = "Select users.* from users where users.rol='admin'";
        } else {//Usuarios no administradores
            sql = "Select users.* from users where users.rol<>'admin'";
        }

        Hashtable<Integer, String> lista = new Hashtable<Integer, String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        MySQLConexionDAO connection = new MySQLConexionDAO();
        try {
            if (connection.abreConexion(null)) {
                ps = connection.pStatement(sql);
                ps.executeQuery(sql);
                rs = ps.getResultSet();
                while (rs.next()) {
                    //guardamos en la tabla Hash
                    if (rs.getString("apellidos") != null && rs.getString("apellidos") != "") {
                        lista.put(rs.getInt("IdUser"), rs.getString("nombre") + " " + rs.getString("apellidos"));
                    } else {
                        lista.put(rs.getInt("IdUser"), rs.getString("nombre"));
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException | NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {//cerramos la conexión
            connection.cierraConexion(ps);
        }
        return lista;
    }

    public boolean esClaveCorrecta(Integer IdUser, String clave) {
        boolean salida = false;
        Usuario elUser;
        try {
            //elUser = AccesoDatos.getFactory().getUsuarioDAO().getUsuarioById(IdUser);
            MySQLFactory fac = new MySQLFactory();
            elUser = fac.getUsuarioDAO().getUsuarioById(IdUser);
            if (elUser != null) {
                salida = elUser.getClave() == clave ? true : false;
            }
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return salida;
    }

    /*MÉTODOS AUXILIARES*/
    private static String md5(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes("UTF-8"));
            return getStringMD5(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getStringMD5(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            String hex = Integer.toHexString((int) 0x00FF & b);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
