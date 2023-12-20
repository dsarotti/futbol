package edu.cotarelo.domain;


public class Jugador {
	public static final String PARAM_NOMBRE = "PARAM_NOMBRE";
	public static final String PARAM_APELLIDOS = "PARAM_APELLIDOS";
	public static final String PARAM_POSICION = "PARAM_POSICION";
	public static final String PARAM_JUGADOR_ID = "PARAM_JUGADOR_ID";
	private String nombre;
	private String apellidos;
	private String posicion;
	private Integer idJugador;

	public Jugador() {
		super();
	}
	public Jugador(String nombre, String apellidos, String posicion) {
		//el id es un autonumérico
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.posicion = posicion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getPosicion() {
		return posicion;
	}
	public void setPosicion(String clave) {
		this.posicion = clave;
	}
	
	public Integer getIdJugador() {
		return idJugador;
	}
	public void setIdJugador(Integer idJugador) {
		this.idJugador = idJugador;
	}

	

}
