package frsf.isi.died.tp.estructuras;

public class ArbolNario{

	    private Nodo raiz;

	    public ArbolNario() {
	        raiz=null;
	    }

	    public ArbolNario(Nodo raiz) {
	        this.raiz = raiz;
	    }
	    
	    public boolean tieneNodo(Nodo buscado)
	    {
	    	if(raiz==null) return false;
	    	if(raiz.getValor()!=null&&raiz.toString().compareTo(buscado.toString())==0) return true;
	    	if(raiz.tieneHijos()) 
	    		for(ArbolNario hijo: raiz.getHijos()) 
	    			if(hijo.tieneNodo(buscado))
	    				return true;
	    	return false;
	    }
	    
	    public Nodo getRaiz() {
	        return this.raiz;
	    }

	    public int getCantidadNodos() {
	        int cant = 0;

	        if(raiz != null) {
	            cant = auxCantidadNodos(raiz) + 1; //1 for the root!
	        }

	        return cant;
	    }

	    private int auxCantidadNodos(Nodo raiz) {
	        int cant = raiz.getCantidadHijos();

	        for(ArbolNario hijo : raiz.getHijos()) {
	            cant += auxCantidadNodos(hijo.raiz);
	        }

	        return cant;
	    }
	    
	    public boolean existe(String dato) { //TODO
	        return (encontrar(dato) != null);
	    }

	    public Nodo encontrar(String valor) {
	        Nodo ret = null;

	        if(raiz != null) {
	            ret = auxEncontrar(raiz,valor);
	        }
	        return ret;
	    }

	    private Nodo auxEncontrar(Nodo actual, String valor) {
	        Nodo ret= null;
	        int i = 0;

	        if (actual.getValor().contains(valor)) {
	            ret = actual;
	        }

	        else if(actual.tieneHijos()) {
	            i = 0;
	            while(ret == null && i < actual.getCantidadHijos()) {
	                ret = auxEncontrar(actual.getHijoAt(i).raiz, valor);
	                i++;
	            }
	        }

	        return ret;
	    }

	public boolean esVacio() {
	     return (raiz == null);
	}

	public String toString()
	{
		if(raiz.tieneHijos())
			return ""+raiz+" - \n "+raiz.getHijos()+"\n";
		else return ""+raiz;
	}
	
/*	@Override
	public Integer profundidad() {
		// TODO Auto-generated method stub
		return null;
	}
*/
	
}
