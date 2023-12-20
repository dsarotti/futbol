package edu.cotarelo.dao.objects;


public interface Factory {
	public AccionesDAO getAccionesDAO();
	public UsuarioDAO getUsuarioDAO();
	public JugadorDAO getJugadorDAO();
	public ClubDAO getClubDAO();
	public PartidoDAO getPartidoDAO();
}
