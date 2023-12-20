package edu.cotarelo.dao.objects;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingException;

import edu.cotarelo.domain.Jugador;

public interface JugadorDAO {
	public int insertar(Jugador jugador) throws NamingException;
	public Jugador getJugadorById(int id) throws NamingException;
	public boolean estaJugadorEnBBDD(Jugador user);
	public int borrar(Jugador jugador) throws NamingException;
	public int modificar(Jugador jugador) throws NamingException ;
	public Hashtable<Integer,String> getListaPosiciones();
	public ArrayList<Jugador> getLista(int tipo);
	public Hashtable<Integer,String> getListaSelect(int tipo);
}
