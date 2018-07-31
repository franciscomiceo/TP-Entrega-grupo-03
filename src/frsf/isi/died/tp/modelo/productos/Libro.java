/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsf.isi.died.tp.modelo.productos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author mdominguez
 */
public class Libro extends MaterialCapacitacion {
	private Double precioCompra;
	private Integer paginas;

	public Libro() {
	}

	public Libro(Integer id, String titulo) {
		super(id, titulo);
		this.precioCompra = 0.0;
		this.paginas = 0;
	}

	public Libro(Integer id, String titulo, Double costo, Double precioCompra, Integer paginas) {
		super(id, titulo, costo);
		this.precioCompra = precioCompra;
		this.paginas = paginas;
	}
	
	public Libro(Integer id, String titulo, Double costo, Double precioCompra, Integer paginas,Date fp,Integer calificacion,String tema) {
		super(id,titulo,costo,fp,calificacion,tema);
		this.precioCompra = precioCompra;
		this.paginas = paginas;
	}

	public Double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(Double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public Integer getPaginas() {
		return paginas;
	}

	public void setPaginas(Integer paginas) {
		this.paginas = paginas;
	}

	private Double factorPagina() {
		return 1.0 + (0.03 * (this.paginas / 150));
	}

	@Override
	public Double precio() {
		return this.costo + (this.precioCompra * this.factorPagina());
	}

	@Override
	public Boolean esLibro() {
		return true;
	}

	@Override
	public Boolean esVideo() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Libro && super.equals(obj) ;
	}
	
}