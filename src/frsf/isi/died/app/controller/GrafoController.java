package frsf.isi.died.app.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import frsf.isi.died.app.dao.MaterialConexion;
import frsf.isi.died.app.vista.grafo.AristaView;
import frsf.isi.died.app.vista.grafo.ControlPanel;
import frsf.isi.died.app.vista.grafo.GrafoPanel;
import frsf.isi.died.app.vista.grafo.VerticeView;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;

public class GrafoController {

	private GrafoPanel vistaGrafo;
	private ControlPanel vistaControl;
	private MaterialConexion mc;

	public GrafoController(GrafoPanel panelGrf, ControlPanel panelCtrl) throws Exception{
		this.vistaGrafo = panelGrf;
		this.vistaGrafo.setController(this);
		
		this.vistaControl = panelCtrl;
		this.vistaControl.setController(this);
		mc=new MaterialConexion();
		
		try {
			if(mc.getVertices().size()>0)
		this.vistaGrafo.cargarGrafo(mc.getVertices(),mc.getAristas()); //recupera el grafo PINTÁNDOLO!!!!
		}catch(Exception ex) {throw ex;}
		
		this.vistaControl.armarPanel(mc.listaMateriales());
	}

	public void crearVertice(Integer coordenadaX, Integer coordenadaY, Color color, MaterialCapacitacion mc) throws Exception{
		VerticeView v = new VerticeView(coordenadaX, coordenadaY, color);
		v.setId(mc.getId());
		v.setNombre(mc.getTitulo());
		v.setTema(mc.getTema());
		if(this.vistaGrafo.agregar(v))
		{
			this.vistaGrafo.repaint();
			try {
				this.mc.agregarVertice(v);
			}catch(Exception ex) {throw ex;}
		}
	}
	

	public VerticeView crearVertice(VerticeView vv, Color color) {
		VerticeView v = new VerticeView(vv.getCoordenadaX(),vv.getCoordenadaY(),color);
		v.setId(vv.getId());
		v.setNombre(vv.getNombre());
		v.setTema(vv.getTema());
		this.vistaGrafo.agregar(v);
		this.vistaGrafo.repaint();
		
		return v;
	}

	public void crearArista(AristaView arista) throws Exception{
		if(this.vistaGrafo.agregar(arista))
			{
				try {
					mc.crearCamino(arista.getOrigen().getId(), arista.getDestino().getId());
				}catch(Exception ex) {throw new Exception("No se pudo crear el camino desde "+arista.getOrigen().getNombre()+" hasta "+arista.getDestino().getNombre()+ex.getMessage());}
				this.vistaGrafo.repaint();
			}
		}

	public void buscarCamino(Integer nodo1, Integer nodo2, Integer saltos) {
		//List<MaterialCapacitacion> camino=new ArrayList<>();
		if(this.vistaGrafo.getVertices().stream().anyMatch(vv->vv.getId()==nodo1)&&
				this.vistaGrafo.getVertices().stream().anyMatch(vv->vv.getId()==nodo2))
		 mc.buscarCamino(nodo1, nodo2, saltos,this.vistaGrafo);
		else JOptionPane.showMessageDialog(null,"Hay material seleccionado aun no ingresado en el grafo");
		//this.vistaGrafo.caminoPintar(camino);
		//this.vistaGrafo.repaint();
	}
	
	public void buscarCamino(Integer nodo1, Integer nodo2) {
		mc.buscarCamino(nodo1, nodo2,this.vistaGrafo);
	}

	public List<MaterialCapacitacion> listaVertices() {
		return mc.listaMateriales();
			}

	public void calcularPageRank(String tema) {
			vistaGrafo.calcularPageRank(tema);
		}

	public void resetGrafo() throws Exception{
		try{vistaGrafo.resetGrafo();
		this.vistaGrafo.repaint();}catch(Exception ex) {throw ex;}
		
	}

}
