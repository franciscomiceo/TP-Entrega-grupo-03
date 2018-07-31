package frsf.isi.died.tp.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import frsf.isi.died.tp.modelo.productos.Libro;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;
import frsf.isi.died.tp.modelo.productos.Video;
import frsf.isi.died.tp.modelo.usuarios.Suscriptor;
import frsf.isi.died.tp.util.ListaServiceRadix;
import frsf.isi.died.tp.util.ListasService;

public class BibliotecaArray implements Biblioteca{

	private Suscriptor[] suscriptores;
	private MaterialCapacitacion[] materialCapacitacion;
	private Integer cantidadSuscriptores;
	private Integer cantidadMaterial;
	private ListasService service = new ListaServiceRadix();
	private Boolean ordenarPorPrecio;
	
	public BibliotecaArray() {
		cantidadMaterial=0;
		cantidadSuscriptores=0;
		this.ordenarPorPrecio = false;
		this.suscriptores = new Suscriptor[5];
		this.materialCapacitacion= new MaterialCapacitacion[5];
	}
	
	@Override
	public void agregar(MaterialCapacitacion material) {
		if(this.cantidadMaterial<this.materialCapacitacion.length) {
			this.materialCapacitacion[cantidadMaterial]=material;
			cantidadMaterial++;
		}
	}


	@Override
	public Integer cantidadMateriales() {
		return cantidadMaterial;
	}
	@Override
	public Collection<MaterialCapacitacion> materiales() {		
		return Arrays.asList(this.materialCapacitacion);
	}
	
	@Override
	public Integer cantidadLibros() {
		Integer libros=0;
		for(MaterialCapacitacion unMat : this.materialCapacitacion) {
			if(unMat!= null && unMat.esLibro())libros++;
		}
		return libros;
	}

	
	@Override
	public Integer cantidadVideos() {
		Integer videos = 0;
		for(MaterialCapacitacion unMat : this.materialCapacitacion) {
			if(unMat!=null && unMat.esVideo())videos++;
		}
		return videos;
	}

	@Override
	public void imprimir() {		
		this.service.setArreglo(materialCapacitacion);
		this.service.imprimir();
	}
	
	

	@Override
	public void ordenarPorPrecio(Boolean b) {
		this.service.setArreglo(materialCapacitacion);
		this.service.ordenar();
	}

	@Override
	public MaterialCapacitacion buscar(Integer precio) {
		// TODO Auto-generated method stub
		return null;
	}



}
