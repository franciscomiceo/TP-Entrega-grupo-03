package frsf.isi.died.tp.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;
import frsf.isi.died.tp.modelo.usuarios.Suscriptor;

public class BibliotecaList implements Biblioteca {

	private ArrayList<MaterialCapacitacion> materiales;
	private Predicate<MaterialCapacitacion> esLibro = m -> m.esLibro();
	private Predicate<MaterialCapacitacion> esVideo = m -> m.esVideo();

	public BibliotecaList() {
		this.materiales = new ArrayList<>();
	}

	@Override
	public void agregar(MaterialCapacitacion material) {
		this.materiales.add(material);
	}

	@Override
	public Integer cantidadMateriales() {
		return this.materiales.size();
	}

	@Override
	public Integer cantidadLibros() {
		int cantidadLibros = 0;
		for (MaterialCapacitacion mat : this.materiales) {
			if (this.esLibro.test(mat))
				cantidadLibros++;
		}
		return cantidadLibros;
	}

	@Override
	public Integer cantidadVideos() {
		int cantidadVideos = 0;
		for (MaterialCapacitacion mat : this.materiales) {
			if (this.esVideo.test(mat))
				cantidadVideos++;
		}
		return cantidadVideos;
	}

	@Override
	public Collection<MaterialCapacitacion> materiales() {
		return this.materiales;
	}

	@Override
	public void imprimir() {
		for (MaterialCapacitacion mat : this.materiales) {
			System.out.println(mat.toString());
		}
	}

	@Override
	public void ordenarPorPrecio(Boolean b) {
		Collections.sort(this.materiales, (m1, m2) -> m1.precio().intValue() - m2.precio().intValue());
	}

	@Override
	public MaterialCapacitacion buscar(Integer precio) {
		Collections.sort(this.materiales, (m1, m2) -> m1.getCosto().intValue() - m2.getCosto().intValue());
		return buscadorBinario(0, this.materiales.size()-1, precio);
	}

	//[12,15] --> 0 0
	
	private MaterialCapacitacion buscadorBinario(Integer inicio, Integer fin, Integer costo) {
		System.out.println(inicio + "_ " + fin);
		if (fin >= 0 || inicio>fin) {
			int medio = (fin + inicio+1) / 2;
//			System.out.println("medio " + medio);
//			System.out.println(this.materiales.get(medio).getCosto());
			if (costo.intValue() == this.materiales.get(medio).getCosto().intValue()) {
				return this.materiales.get(medio);
			}
			if (this.materiales.get(medio).getCosto() < costo) {
				return buscadorBinario(medio+1, fin, costo);
			} else {
				return buscadorBinario(inicio, medio-1, costo);
			}
		}
		throw new RuntimeException("Material de precio " + costo + " no encontrado");

	}
}
