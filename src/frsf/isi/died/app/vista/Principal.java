package frsf.isi.died.app.vista;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import frsf.isi.died.app.controller.TiposAcciones;
import frsf.isi.died.app.controller.MenuController;

public class Principal extends JFrame {

	public static void main(String[] args) {
		    javax.swing.SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
		          createAndShowGUI();
		        }
		    });
		}

		 private static void createAndShowGUI() {
			 JFrame f = new JFrame("Principal");
		f.setTitle("Principal"); 
		MenuController controller = new MenuController(f);
		 
	        JMenuBar menuBar;
	        JMenu menu;
	        JMenuItem menuItem;

	        menuBar = new JMenuBar();

	        menu = new JMenu("Sistema");
	        menuBar.add(menu);
	        
	        menuItem = new JMenuItem("Nuevo/Modificar/Eliminar Libro");
	        menuItem.addActionListener(e -> controller.showView(TiposAcciones.ABM_LIBROS));
	        menu.add(menuItem);

	        menuItem = new JMenuItem("Nuevo/Modificar/Eliminar Video");
	        menuItem.addActionListener(e -> controller.showView(TiposAcciones.ABM_VIDEOS));
	        menu.add(menuItem);
	        
	        menu.addSeparator();
	        menuItem = new JMenuItem("Salir");
	        menuItem.addActionListener(e->System.exit(99));
	        menu.add(menuItem);

	        menuBar.add(menu);
	        
	        menu = new JMenu("Opciones");
	        menuBar.add(menu);
	        
	        menuItem = new JMenuItem("Buscar Camino");
	        menuItem.addActionListener(e -> controller.showView(TiposAcciones.VER_GRAFO));
	        menu.add(menuItem);
	        
	        menuItem = new JMenuItem("Busquedas / WishList / Contenido");
	        menuItem.addActionListener(e -> controller.showView(TiposAcciones.BUSQUEDA));
	        menu.add(menuItem);
	        menuBar.add(menu);
	        
	        
	        f.setJMenuBar(menuBar);
	        
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        //f.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
	        f.pack();
	        f.setSize(850, 600);
	        f.setLocationRelativeTo(null);
	        f.setVisible(true);
	    }

}
