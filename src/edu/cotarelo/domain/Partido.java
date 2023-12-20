package edu.cotarelo.domain;

import java.util.Date;

public class Partido {
	public static final String PARAM_CLUB1 = "PARAM_CLUB1";
	public static final String PARAM_CLUB2 = "PARAM_CLUB2";
	public static final String PARAM_PARTIDO = "PARAM_PARTIDO";
	private Club IdClub1;
	private Club IdClub2;
	private Date fecha;

	public Partido() {
		super();
	}
	public Partido(Club IdClub1, Club IdClub2, Date fecha) {
		//el id es un autonumérico
		super();
		this.IdClub1 = IdClub1;
		this.IdClub2 = IdClub2;
		this.fecha = fecha;
	}
	public Partido(Club IdClub1, Club IdClub2) {
		this.IdClub1 = IdClub1;
		this.IdClub2 = IdClub2;
	}
	public Club getIdClub1() {
		return IdClub1;
	}
	public void setIdClub1(Club IdClub1) {
		this.IdClub1 = IdClub1;
	}
	public Club getIdClub2() {
		return IdClub2;
	}
	public void setIdClub2(Club IdClub2) {
		this.IdClub2 = IdClub2;
	}

	public Date getfecha() {
		return fecha;
	}
	public void setfecha(Date fecha) {
		this.fecha = fecha;
	}

}
