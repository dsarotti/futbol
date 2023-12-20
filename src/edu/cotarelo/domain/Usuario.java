package edu.cotarelo.domain;


public class Usuario {
	public static final String PARAM_USUARIO_NOMBRE = "PARAM_USUARIO_NOMBRE";
	public static final String PARAM_USUARIO_APELLIDOS = "PARAM_USUARIO_APELLIDOS";
	public static final String PARAM_USUARIO_CLAVE = "PARAM_USUARIO_CLAVE";
	public static final String PARAM_USUARIO_ROL = "PARAM_USUARIO_ROL";
	public static final String PARAM_USUARIO_ID = "PARAM_USUARIO_ID";
	private String nombre;
	private String apellidos;
	private String clave;
	private String rol;
	private Integer IdUsuario;

	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public Usuario() {
	
	}
	public Usuario(String nombre, String apellidos, String clave, String rol) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.clave = clave;
		this.rol = rol.equals("")?"normal":rol.toLowerCase();
	}
	public Usuario(String nombre, String apellidos, String clave) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.clave = clave;
		this.rol = "normal";
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
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public Integer getIdUsuario() {
		return IdUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		IdUsuario = idUsuario;
	}
	
	public boolean EsAdministrador() {
		if (this.getRol()==null)
				return false;
		else
			return this.getRol().equals("admin");
	}
	public boolean EsClaveCorrecta(Usuario user,String clave) {
		return user.getClave()==clave?true:false;
	}

	
}

