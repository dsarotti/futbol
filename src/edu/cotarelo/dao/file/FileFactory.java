package edu.cotarelo.dao.file;


import edu.cotarelo.dao.objects.AccionesDAO;
import edu.cotarelo.dao.objects.ClubDAO;
import edu.cotarelo.dao.objects.Factory;
import edu.cotarelo.dao.objects.JugadorDAO;
import edu.cotarelo.dao.objects.PartidoDAO;
import edu.cotarelo.dao.objects.UsuarioDAO;

public class FileFactory implements Factory {

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new FileUsuarioDAO();
	}

	@Override
	public AccionesDAO getAccionesDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JugadorDAO getJugadorDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClubDAO getClubDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartidoDAO getPartidoDAO() {
		// TODO Auto-generated method stub
		return null;
	}

}
