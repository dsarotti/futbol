package edu.cotarelo.dao.file;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingException;
import edu.cotarelo.dao.objects.UsuarioDAO;
import edu.cotarelo.domain.Usuario;

public class FileUsuarioDAO implements UsuarioDAO {

	@Override
	public int insertar(Usuario usuario) throws NamingException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Usuario getUsuarioById(int id) throws NamingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioByNombreContraseña(String nombre, String pass) throws NamingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean estaUsuarioEnBBDD(Usuario user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int borrar(Usuario usuario) throws NamingException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modificar(Usuario usuario) throws NamingException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Usuario> getListaUsuarios(int tipo) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean esClaveCorrecta(Integer IdUser, String clave) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Hashtable<Integer, String> getListaUsuariosSelect(int tipo) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}



}
