/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsf.isi.died.tp.modelo.productos;

import java.util.ArrayList;
import java.util.Date;

import frsf.isi.died.tp.util.Ordenable;

/**
 * Representa de manera abstracta los materiales de capacitación
 * 
 * @author mdominguez
 */
public abstract class MaterialCapacitacion implements Ordenable,Comparable<MaterialCapacitacion>{
	protected Integer id;
	/**
	 * Titulo del material
	 */
	protected String titulo;

	/**
	 * Costo básico que debe sumarse al precio por el mero hecho de publicarlo en el
	 * portal
	 */
	protected Double costo;
	
	/**
	 * Valor entre 1 y 100, rating con el que los usuarios calificaron al material en cuesti�n
	 */
	protected Integer calificacion; 
	
	/**
	 * Fecha de publicacion del material en cuestion
	 */
	protected Date fecha_publicacion;

	/**
	 * Relevancia del material en cuestion (BAJA/MEDIA/ALTA)
	 * 0-32->Revelancia.BAJA ; 33-65-> Revelancia.MEDIA ; 66-100-> Revelancia.ALTA
	 */
	
	protected Relevancia relevancia;
	
	/**
	 * Tema del material en cuestion (JAVA/C++/SMALLTALK)
	 */
	
	protected String tema;

	/**
	 * Constructor por defecto
	 */

	public MaterialCapacitacion() {
		this(0,"en desarrollo",0.0);
	}

	/**
	 * Constructor que recibe como argumento un ID y un Titulo
	 * 
	 * @param id
	 * @param titulo
	 */
	public MaterialCapacitacion(Integer id, String titulo) {
		this(id,titulo,0.0);
	}

	/**
	 * Constructor que recibe como argumento un ID y un costo
	 * 
	 * @param id
	 * @param titulo
	 */
	public MaterialCapacitacion(Integer id,String titulo, Double costo) {
		this.id =id;
		this.titulo = titulo;
		this.costo = costo;
	}
	
	public MaterialCapacitacion(Integer id,String titulo, Double costo,Date fp, Integer calificacion, String tema) 
	{
		this(id,titulo,costo);
		this.fecha_publicacion=fp;
		this.setCalificacion(calificacion);
		this.tema=tema;
	}

	public String getTitulo() {
		return titulo;
	}

	public Double getCosto() {
		return costo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}
	

	/**
	 * El precio de un material se define según el tipo del material y toma como
	 * base el costo del mismo
	 * 
	 * @return
	 */
	public abstract Double precio();
	
	/**
	 * Retorna verdadero si es una instancia de libro, falso en caso contrario
	 * @return
	 */
	public abstract Boolean esLibro();

	/**
	 * Retorna verdadero si es una instancia de video, falso en caso contrario
	 * @return
	 */
	public abstract Boolean esVideo();
	

	/**
	 * Retorna un valor numerico que será utilizado para ordenar los elementos
	 * @return
	 */
	@Override
	public final int valor() {
		return this.precio().intValue();
	}

	/**
	 * el método toString imprime el titulo, y el precio de un libro
	 * usando el formato : [Titulo: <titulo> ; Precio: <precio> ]
	 */
	@Override
	public String toString() {
		return "[Titulo " +this.titulo+"; PRECIO: "+this.precio()+"]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaterialCapacitacion other = (MaterialCapacitacion) obj;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		return true;
	}

	@Override
	public int compareTo(MaterialCapacitacion o) {
		int aux = this.titulo.compareTo(o.titulo);
		if(aux==0) {
			aux = this.precio().compareTo(o.precio());
		}
		return aux;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCalificacion() {
		return calificacion;
	}

	//0-32->Revelancia.BAJA ; 33-65-> Revelancia.MEDIA ; 66-100-> Revelancia.ALTA
	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
		if(this.calificacion<33.0)
			this.relevancia = Relevancia.BAJA;
		else if(this.calificacion>=66.0)
			this.relevancia=Relevancia.ALTA;
			else this.relevancia=Relevancia.MEDIA;
	}

	public Date getFecha_publicacion() {
		return fecha_publicacion;
	}

	public void setFecha_publicacion(Date fecha_publicacion) {
		this.fecha_publicacion = fecha_publicacion;
	}

	public Relevancia getRelevancia() {
		return relevancia;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public void setRelevancia(Relevancia r) {
		this.relevancia=r;
		
	}	
}
