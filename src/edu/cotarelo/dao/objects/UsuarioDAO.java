package edu.cotarelo.dao.objects;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingException;

import edu.cotarelo.domain.Usuario;

public interface UsuarioDAO {
	public int insertar(Usuario usuario) throws NamingException;
	public Usuario getUsuarioById(int id) throws NamingException;
	public Usuario getUsuarioByNombreContraseña(String nombre, String pass) throws NamingException;
	public boolean estaUsuarioEnBBDD(Usuario user);
	public int borrar(Usuario usuario) throws NamingException;
	public int modificar(Usuario usuario) throws NamingException ;
	public ArrayList<Usuario> getListaUsuarios(int tipo) throws ClassNotFoundException ;
	public Hashtable<Integer,String> getListaUsuariosSelect(int tipo) throws ClassNotFoundException;
	public boolean esClaveCorrecta(Integer IdUser,String clave);
}
