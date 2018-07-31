package frsf.isi.died.tp.util;

public abstract class ListasService {
	
	protected Ordenable[] arregloOrdenable; 

	public void setArreglo(Ordenable[] arreglo) {
		this.arregloOrdenable = arreglo;
	}
	
	public void imprimir() {
		int i = 0;
		for(Ordenable obj : arregloOrdenable) {
			if(obj!=null) System.out.println("Elemento en posicion "+(++i)+": "+obj);
		}
	}
	
	public abstract void ordenar() ;
	
}
