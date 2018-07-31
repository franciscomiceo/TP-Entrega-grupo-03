package frsf.isi.died.tp.modelo;

import java.util.Collection;

import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;

public interface Biblioteca {


	/**
	 * Agrega un material de capacitacion
	 * @param material
	 */
	public void agregar(MaterialCapacitacion material);

	/**
	 * Retorna la cantidad de materiales de capacitacion que se encuentran en el sistema 
	 * @return
	 */
	public Integer cantidadMateriales();

	/**
	 * Retorna la cantidad de libros que se encuentran en el sistema 
	 * @return
	 */
	public Integer cantidadLibros();

	/**
	 * Retorna la cantidad de videos que se encuentran en el sistema 
	 * @return
	 */
	public Integer cantidadVideos();
	
	/**
	 * Retorna una lista de todos los materiales de capacitacion 
	 * @return
	 */
	public Collection<MaterialCapacitacion> materiales();
	
	/**
	 * Imprime por consola todos los materiales de capacitacion
	 */
	public void imprimir();
	
	/**
	 * Si el valor b es true entonces se ordena el arreglo por precio
	 * @param b
	 */
	public void ordenarPorPrecio(Boolean b);

	/** 
	 * 
	 */
	public MaterialCapacitacion buscar(Integer precio);
	
}
