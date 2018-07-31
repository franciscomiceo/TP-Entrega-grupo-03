package frsf.isi.died.app.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import frsf.isi.died.app.vista.grafo.ControlPanel;
import frsf.isi.died.app.vista.grafo.GrafoPanel;
import frsf.isi.died.app.vista.material.*;
import frsf.isi.died.tp.estructuras.ArbolNario;

public class MenuController {

	private JFrame framePrincipal;
	private List<ArbolNario> arboles; //para mantener los arboles de contenidos de materiales
	
	public MenuController(JFrame f) {
		this.framePrincipal = f;
		this.arboles=new ArrayList<>();
	}
	
	public void showView(TiposAcciones accion) {
		switch (accion) {
		case ABM_LIBROS:
			framePrincipal.setTitle("Gestionar Libro");
			LibroPanel panelLibros = new LibroPanel(this.framePrincipal);
			LibroController controller = new LibroController(panelLibros);
			controller.crearPanel();
			panelLibros.setPreferredSize(new Dimension(500,400));// hardCoded sizing
			panelLibros.setMaximumSize(new Dimension(500, 300));  // hardCoded sizing
			panelLibros.setMinimumSize(new Dimension(300, 300));  // hardCoded sizing
			
			framePrincipal.setContentPane(controller.getPanelLibro());
			break;
		case ABM_VIDEOS:
			framePrincipal.setTitle("Gestionar Video");
			VideoPanel panelVideos = new VideoPanel(this.framePrincipal);
			VideoController controllerV = new VideoController(panelVideos);
			controllerV.crearPanel();
			panelVideos.setPreferredSize(new Dimension(500,400));// hardCoded sizing
			panelVideos.setMaximumSize(new Dimension(500, 300));  // hardCoded sizing
			panelVideos.setMinimumSize(new Dimension(300, 300));  // hardCoded sizing
			
			framePrincipal.setContentPane(controllerV.getPanelVideo());
			break;
		case VER_GRAFO:
			framePrincipal.setTitle("Asignar Relaciones");
			JPanel panel = new JPanel(new BorderLayout());
			ControlPanel controlPanel = new ControlPanel();
			GrafoPanel grafoPanel = new GrafoPanel(framePrincipal);
			try {
			GrafoController grfController = new GrafoController(grafoPanel,controlPanel);
			}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
			panel.add(controlPanel , BorderLayout.PAGE_START);
			panel.add(grafoPanel , BorderLayout.CENTER);
			framePrincipal.setContentPane(panel);
			break;
		case BUSQUEDA:
			framePrincipal.setTitle("Busquedas");
			BusquedaPanel panelBusqueda = new BusquedaPanel(this.framePrincipal,arboles);
			BusquedaController controllerB = new BusquedaController(panelBusqueda);
			controllerB.crearPanel();
			panelBusqueda.setPreferredSize(new Dimension(500,400));// hardCoded sizing
			panelBusqueda.setMaximumSize(new Dimension(500, 300));  // hardCoded sizing
			panelBusqueda.setMinimumSize(new Dimension(300, 300));  // hardCoded sizing
			
			framePrincipal.setContentPane(controllerB.getPanelBusqueda());
			break;
		
		default:
			break;
		}
		framePrincipal.pack();

	}
	
	
}
