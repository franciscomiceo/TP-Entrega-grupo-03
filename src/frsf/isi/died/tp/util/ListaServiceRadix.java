package frsf.isi.died.tp.util;

import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;

public class ListaServiceRadix extends ListasService{

	private Ordenable[][] residuos = new Ordenable[10][10];
	private int[] cantidadPorFila = new int[10];
	
	
	@Override
	public void ordenar() {
//		Ordenable[] arregloOrdenado=new MaterialCapacitacion[this.arregloOrdenable.length];
//		System.arraycopy(this.arregloOrdenable, 0, arregloOrdenado, 0, this.arregloOrdenable.length);
		
		for(int i=0;i<3;i++) {
			int peso = (int) Math.pow(10.0,i);
			for(Ordenable mat : this.arregloOrdenable) {
				if(mat!=null) {
					int digito = (int) ((mat.valor()/peso)%10); 
					residuos[digito][cantidadPorFila[digito]]=mat;
					cantidadPorFila[digito]++;
				}
			}
			int indiceArregloOrdenado=0;
			for(int j=0;j<10;j++) {
				for(int k=1;k<=cantidadPorFila[j];k++) {
					this.arregloOrdenable[indiceArregloOrdenado++]=residuos[j][k-1];	
				}		
				//reiniciar el contador
				cantidadPorFila[j]=0;
			}			
		}
		//this.arregloOrdenable= arregloOrdenado;				
	}

}
