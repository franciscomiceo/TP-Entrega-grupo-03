package frsf.isi.died.tp.estructuras;

import java.util.List;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;
import java.util.Comparator;

public abstract class Arbol {

	public abstract boolean esVacio();

	public abstract void add(MaterialCapacitacion mat);

	public abstract Arbol izquierdo();

	public abstract Arbol derecho();
	
	public abstract boolean contiene(MaterialCapacitacion unValor);

	public abstract boolean equals(Arbol unArbol);

	public abstract Integer profundidad();
	
	public abstract void imprimir();

	public abstract List<MaterialCapacitacion> inOrden();
	public abstract List<MaterialCapacitacion> preOrden();
	public abstract List<MaterialCapacitacion> postOrden();

	public abstract Integer tamanio();
	public abstract Integer tamanioLibros();
	public abstract Integer tamanioVideos();


	public abstract MaterialCapacitacion buscar(Integer precio);
	public abstract List<MaterialCapacitacion> rango(Double precio1, Double precio2);
}
