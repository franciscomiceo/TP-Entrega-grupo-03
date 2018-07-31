package frsf.isi.died.tp.estructuras;

import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArbolBinarioBusqueda extends Arbol {

	private MaterialCapacitacion valor;
	private Arbol izquierdo;
	private Arbol derecho;
	private Comparator<MaterialCapacitacion> comparador;

	public ArbolBinarioBusqueda(Comparator<MaterialCapacitacion> comparador) {
		this(null,comparador);
	}
	

	public ArbolBinarioBusqueda(MaterialCapacitacion e,Comparator<MaterialCapacitacion> comparador) {
		this.izquierdo = new ArbolVacio();
		this.derecho = new ArbolVacio();
		this.valor = e;
		this.comparador = comparador;
	}

	@Override
	public boolean esVacio() {
		return valor==null;
	}

	@Override
	public Arbol izquierdo() {
		return this.izquierdo;
	}

	@Override
	public Arbol derecho() {
		return this.derecho;
	}

	@Override
	public boolean contiene(MaterialCapacitacion unValor) {
		if (this.valor.equals(unValor))
			return true;
		return this.izquierdo.contiene(unValor) || this.derecho.contiene(unValor);
	}

	@Override
	public boolean equals(Arbol unArbol) {
		return unArbol instanceof ArbolBinarioBusqueda && this.valor.equals(((ArbolBinarioBusqueda) unArbol).valor)
				&& this.izquierdo.equals(unArbol.izquierdo()) && this.derecho.equals(unArbol.derecho());
	}

	@Override
	public Integer profundidad() {		
		if(this.valor==null) return 0;
		return 1 + Math.max(this.izquierdo.profundidad(), this.derecho.profundidad());
	}

	@Override
	public void add(MaterialCapacitacion mat) {
		if (this.valor == null) {
			this.valor = mat;
			return;
		} else {
			if (this.comparador.compare(mat, this.valor) > 0) {
				if (this.derecho.esVacio())
					this.derecho = new ArbolBinarioBusqueda(mat,this.comparador);
				else
					this.derecho.add(mat);
			} else {
				if (this.izquierdo.esVacio())
					this.izquierdo = new ArbolBinarioBusqueda(mat,this.comparador);
				else
					this.izquierdo.add(mat);
			}
		}
	}
	
	@Override
	public Integer tamanio() {  
		if(!esVacio()) return 1 + this.derecho.tamanio() + this.izquierdo.tamanio();
		return 0;
	}

	@Override
	public Integer tamanioLibros() {
		int aux = this.valor.esLibro() ? 1 : 0;
		return aux + this.derecho.tamanioLibros() + this.izquierdo.tamanioLibros();
	}

	@Override
	public Integer tamanioVideos() {
		int aux = this.valor.esVideo() ? 1 : 0;
		return aux + this.derecho.tamanioVideos() + this.izquierdo.tamanioVideos();
	}

	

	@Override
	public void imprimir() {
		this.inOrden().stream().forEach(System.out::println);
	}

	@Override
	public List<MaterialCapacitacion> preOrden() {
		List<MaterialCapacitacion> lista = new ArrayList<MaterialCapacitacion>();
		lista.add(this.valor);
		lista.addAll(this.izquierdo.preOrden());
		lista.addAll(this.derecho.preOrden());
		return lista;
	}

	@Override
	public List<MaterialCapacitacion> inOrden() {
		List<MaterialCapacitacion> lista = new ArrayList<MaterialCapacitacion>();
		lista.addAll(this.izquierdo.inOrden());
		lista.add(this.valor);
		lista.addAll(this.derecho.inOrden());
		return lista;
	}

	@Override
	public List<MaterialCapacitacion> postOrden() {
		List<MaterialCapacitacion> lista = new ArrayList<MaterialCapacitacion>();
		lista.addAll(this.izquierdo.postOrden());
		lista.addAll(this.derecho.postOrden());
		lista.add(this.valor);
		return lista;
	}
	
	@Override
	public MaterialCapacitacion buscar(Integer precioBuscado) {
		int comparacion =this.valor.precio().intValue()- precioBuscado.intValue(); 
		if(comparacion==0) return this.valor;

		if(comparacion<0) return this.derecho.buscar(precioBuscado);
		else return this.izquierdo.buscar(precioBuscado);
	}

	@Override
	public List<MaterialCapacitacion> rango(Double precioMin, Double precioMax) {
		List<MaterialCapacitacion> lista = new ArrayList<MaterialCapacitacion>();
		int comparacionPrecioMin =this.valor.precio().compareTo(precioMin);
		int comparacionPrecioMax =this.valor.precio().compareTo(precioMax);

		if(comparacionPrecioMin>=0 && comparacionPrecioMax<=0) {
			//si raiz mayor que minimo agrego raiz y derecho e izquierdo
			lista.add(this.valor);
			lista.addAll(this.izquierdo.rango(precioMin, precioMax));
			lista.addAll(this.derecho.rango(precioMin, precioMax));
		}
		if(comparacionPrecioMin<0) {
			//si raiz es menor que el minimo
			lista.addAll(this.derecho.rango(precioMin, precioMax));
		}

		if(comparacionPrecioMax>0) {
			//si raiz es menor que el maximo
			lista.addAll(this.izquierdo.rango(precioMin, precioMax));
		}		
		
		return lista;
	}


}
