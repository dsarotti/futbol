package edu.cotarelo.dao.objects;

import java.util.Hashtable;

import javax.naming.NamingException;

public interface AccionesDAO {
	public Hashtable<String, String> getAcciones() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NamingException;
	public String getAccionById(String actionId);

}