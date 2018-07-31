package frsf.isi.died.app.controller;

import java.awt.Container;
import java.util.Date;

import javax.swing.JOptionPane;

//import frsf.isi.died.app.dao.MaterialCapacitacionDao;
//import frsf.isi.died.app.dao.MaterialCapacitacionDaoDefault;
import frsf.isi.died.app.dao.MaterialConexion;
import frsf.isi.died.app.vista.material.LibroModificar;
import frsf.isi.died.app.vista.material.LibroPanel;
import frsf.isi.died.tp.modelo.productos.Libro;

public class LibroController {

	private LibroPanel panelLibro;
	
	public LibroController(LibroPanel panel) {
		this.panelLibro = panel;
		this.panelLibro.setController(this);
	}

	public void crearPanel() {		
		try {
		this.panelLibro.setListaLibros(MaterialConexion.getLibros(),true);
		}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
		this.panelLibro.construir();
	}

	
	public void agregarLibro(String titulo,Double costo,Double precio,Integer paginas, Date fecha, Integer calificacion, String tema) {	
		Libro l = new Libro(0,titulo, costo, precio, paginas,fecha,calificacion,tema) ;
		try {
		MaterialConexion.agregarLibro(l);
		this.panelLibro.setListaLibros(MaterialConexion.getLibros(),true);
		}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
	}
	

	public void ModificarLibro(int id_libro,String titulo, Double costo, Double precio, Integer paginas, Date fecha,Integer calificacion,String tema) {
		Libro l = new Libro(id_libro,titulo, costo, precio, paginas,fecha,calificacion,tema) ;
		try {
			MaterialConexion.modificarLibro(l);
			this.panelLibro.setListaLibros(MaterialConexion.getLibros(),true);
		}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
	}
	
	public void EliminarLibro(int id_libro) {
		try {
			MaterialConexion.eliminarLibro(id_libro);
			this.panelLibro.setListaLibros(MaterialConexion.getLibros(),true);
		}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
	}
	
	public LibroPanel getPanelLibro() {
		return panelLibro;
	}

	public void setPanelLibro(LibroPanel panelLibro) {
		this.panelLibro = panelLibro;
	}


}
