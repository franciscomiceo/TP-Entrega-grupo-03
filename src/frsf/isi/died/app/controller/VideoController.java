package frsf.isi.died.app.controller;

import java.util.Date;

import javax.swing.JOptionPane;

import frsf.isi.died.app.dao.MaterialConexion;
import frsf.isi.died.app.vista.material.VideoPanel;
import frsf.isi.died.tp.modelo.productos.Libro;
import frsf.isi.died.tp.modelo.productos.Video;

public class VideoController {

	private VideoPanel panelVideo;
	
	public VideoController(VideoPanel panel) {
		this.panelVideo = panel;
		this.panelVideo.setController(this);
	}

	
	public void agregarVideo(String titulo,Double costo,Integer duracion,Date fecha,Integer calificacion,String tema) {	
		Video v = new Video(0,titulo, costo, duracion,fecha,calificacion,tema) ;
		
		try {
			MaterialConexion.agregarVideo(v);
			this.panelVideo.setListaVideos(MaterialConexion.getVideos(),true);
			}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
	}
	
	public void EliminarVideo(int id_libro) {
		try {
			MaterialConexion.eliminarLibro(id_libro);
			this.panelVideo.setListaVideos(MaterialConexion.getVideos(),true);
		}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
	}

	
	public void crearPanel() {		
		try {
		this.panelVideo.setListaVideos(MaterialConexion.getVideos(),false);
		}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
		this.panelVideo.construir();
	}

	public VideoPanel getPanelVideo() {
		return panelVideo;
	}

	public void setPanelVideo(VideoPanel panelVideo) {
		this.panelVideo = panelVideo;
	}


	public void ModificarVideo(int id_video, String titulo, Double costo, Integer duracion, Date fecha,Integer calificacion,String tema) {
		Video v = new Video(id_video,titulo, costo, duracion,fecha,calificacion,tema) ;
		try {
			MaterialConexion.modificarVideo(v);
			this.panelVideo.setListaVideos(MaterialConexion.getVideos(),true);
		}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
	
	}
	
	
}
