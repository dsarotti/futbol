package edu.cotarelo.domain;


public class Club {
	public static final String PARAM_NOMBRE = "PARAM_NOMBRE";
	public static final String PARAM_DESCRIPCION = "PARAM_DESCRIPCION";
	public static final String PARAM_CAMPO = "PARAM_CAMPO";
	public static final String PARAM_CLUB_ID = "PARAM_CLUB_ID";	
	private String nombre;
	private String descripcion;
	private String campo;
	private String IdClub;
	public String getDescripcion() {
		return descripcion;
	}
	public Club(String paramNombre, String paramDescripcion, String paramCampo) {
		super();
		this.nombre = paramNombre;
		this.descripcion = paramDescripcion;
		this.campo = paramCampo;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	
	public Club() {
		super();
	}
	public Club(String nombre) {
		//el id es un autonumérico
		super();
		this.nombre = nombre;
	}
	public String getNombre() {
		return this.nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getIdClub() {
		return this.IdClub;
	}
	public void setIdClub(String idClub) {
		this.IdClub = idClub;
	}

	

}
