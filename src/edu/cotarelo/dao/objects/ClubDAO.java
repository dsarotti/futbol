package edu.cotarelo.dao.objects;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingException;

import edu.cotarelo.domain.Club;

public interface ClubDAO {
	public int insertar(Club club) throws NamingException;
	public Club getClubById(String idClub) throws NamingException;
	public boolean estaClubEnBBDD(Club user);
	public boolean tieneJugadores(Club club);
	public int borrar(Club club) throws NamingException;
	public int modificar(Club nuevoclub,Club elclub) throws NamingException ;
	public Hashtable<Integer, String> getListaSelect(int Todos);
	public ArrayList<Club> getLista(int Todos);
	;
}
