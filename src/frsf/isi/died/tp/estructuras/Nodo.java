package frsf.isi.died.tp.estructuras;

import java.util.*;

public class Nodo {
    
	private String valor;
	private TipoNodo tipo;
    private List<ArbolNario> hijos;
    
    public Nodo(String valor) {
        this.tipo=TipoNodo.TITULO;
    	this.valor=valor;
    	hijos = new ArrayList<>();
    }
    
    
    public Nodo(TipoNodo tipo,String valor)
    {
    	this(valor);
    	this.tipo=tipo;
    }

    public List<ArbolNario> getHijos() {
        return this.hijos;
    }

    public int getCantidadHijos() {
        return getHijos().size();
    }

    public boolean tieneHijos() {
        return (getCantidadHijos() > 0);
    }

    public void setHijos(List<ArbolNario> hijos) {
            this.hijos = hijos;
    }

    public void addHijo(ArbolNario hijo) {
        hijos.add(hijo);
    }

    public void addHijoAt(int index, ArbolNario hijo) throws IndexOutOfBoundsException {
        hijos.add(index, hijo);
    }

    public void removeHijos() {
        this.hijos = new ArrayList<>();
    }

    public void removeHijoAt(int index) throws IndexOutOfBoundsException {
        hijos.remove(index);
    }

    public ArbolNario getHijoAt(int index) throws IndexOutOfBoundsException {
        return hijos.get(index);
    }

    public String getValor() {
        return this.valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

	public TipoNodo getTipo() {
		return tipo;
	}

	public void setTipo(TipoNodo tipo) {
		this.tipo = tipo;
	}
	
    @Override
    public String toString() {
    	if(getValor()==null) return "tipo="+this.getTipo()+" : null";
        return "tipo="+this.getTipo()+" : "+getValor(); 
    }
    
   // @Override
  //  public boolean equals(Object obj) {} //TODO
    
}
