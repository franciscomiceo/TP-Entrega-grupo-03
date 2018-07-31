package frsf.isi.died.app.vista.material;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

import frsf.isi.died.app.controller.LibroController;
import frsf.isi.died.app.controller.VideoController;

public class VideoEliminar extends JDialog {
	private VideoController controller;
	public int id_video;
	
	public VideoEliminar(VideoController controller,java.awt.Frame principal, boolean modal,String titulo)
    {
        super(principal, modal);
        setTitle("Eliminar Video");
        this.controller=controller;
        initComponents(titulo);
        this.setLocationRelativeTo(null);
    }
	
    private void initComponents(String titulo) {
        jLabel = new JLabel("¿Realmente desea eliminar el Video?: "+titulo);
        btnEliminar =crearBoton(new JButton("Eliminar"));
        btnCancelar =crearBoton(new JButton("Cancelar"));
       
        btnEliminar.addActionListener(e-> {
        	controller.EliminarVideo(id_video); 
        	JOptionPane.showMessageDialog(null, "El Video "+titulo+" se ha eliminado satisfactoriamente");
        	dispose();}); 
        btnCancelar.addActionListener(e-> dispose());
        
        this.setLayout(new GridLayout(2,1));
        this.add(jLabel);
        JPanel panel=new JPanel(); panel.setLayout(new GridLayout(1,2));
        panel.add(btnEliminar); panel.add(btnCancelar);
        this.add(panel);
    }
    
	//agrega el KeyListener al boton
	private JButton crearBoton(JButton boton) {
		boton.addKeyListener(new java.awt.event.KeyListener() {
	         @Override
	         public void keyTyped(KeyEvent e) {}
	         @Override
	         public void keyPressed(KeyEvent e) {if(e.getKeyCode() == 10) boton.doClick();}
	         @Override
		     public void keyReleased(KeyEvent e) {}});  
		return boton;
	}
	
    // Variables declaration - do not modify                     
    private JButton btnEliminar,btnCancelar;
    private JLabel jLabel;
    // End of variables declaration                   


}
