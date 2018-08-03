/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsf.isi.died.app.vista.grafo;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import frsf.isi.died.app.controller.GrafoController;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;

/**
 *
 * @author mdominguez
 */
public class ControlPanel extends JPanel {
    
	public int id_usuario;
    private JComboBox<String> cmbVertice1,cmbVertice2,cmbTemas; 
    private JTextField txtLongitudCamino; 
    private JButton btnBuscarCamino, btnPageRank,btnReset;
    private GrafoController controller;
    private List<MaterialCapacitacion> listaVertices;
        
    public void armarPanel(List<MaterialCapacitacion> listaVertices){
    	this.listaVertices = listaVertices;
    	this.cmbVertice1 = new JComboBox(listaVertices.toArray());
        this.cmbVertice2 = new JComboBox(listaVertices.toArray());
        
        Set<String> temas=new HashSet<>();
        for(MaterialCapacitacion mc: listaVertices) 
        		temas.add(mc.getTema());
        List<String> tt=new ArrayList<>(temas);
        tt.sort((t1,t2)->t1.compareTo(t2)); //ordeno alf//
        this.cmbTemas=new JComboBox(tt.toArray());
        
        this.txtLongitudCamino = new JTextField(5); 
        
        this.btnBuscarCamino = crearBoton(new JButton("Buscar Camino"));
        this.btnBuscarCamino.addActionListener(
                e -> { 
                if(this.listaVertices.size()>1){
                	Integer n=0;
                    if(!txtLongitudCamino.getText().isEmpty())
                	{
                    	try{n =Integer.parseInt(txtLongitudCamino.getText());}catch(Exception ex) {JOptionPane.showMessageDialog(null,"Debe ingresar un numero de saltos entre 1 y "+(listaVertices.size()-1));}
                    	if(n<1||n>this.listaVertices.size()-1)
                    		if(listaVertices.size()==2)JOptionPane.showMessageDialog(null,"Solo puede ingresar 1 como numero de saltos");
                    		else JOptionPane.showMessageDialog(null,"Debe ingresar un numero de saltos entre 1 y "+(listaVertices.size()-1));
                	}
                    
                    Integer idOrigen = this.listaVertices.get(cmbVertice1.getSelectedIndex()).getId();
                    Integer idDestino= this.listaVertices.get(cmbVertice2.getSelectedIndex()).getId();
                    
                    if(idOrigen!=idDestino)
                    {
                    	//System.out.println("Entra, idOrigen="+idOrigen+", idDestino="+idDestino);
                    	if(n>0&&n<this.listaVertices.size())
                    	controller.buscarCamino(idOrigen,idDestino,n);
                    	else if(n==0)
                    		controller.buscarCamino(idOrigen, idDestino);
                    }
                    else JOptionPane.showMessageDialog(null,"Debe seleccionar 2 puntos distintos");
                }
                else JOptionPane.showMessageDialog(null,"Debe haber cargado por lo menos 2 materiales de capacitacion");
                }
        );
        
        this.btnPageRank = crearBoton(new JButton("Calcular PageRank"));
        this.btnPageRank.addActionListener(e -> {controller.calcularPageRank((String)cmbTemas.getSelectedItem());});
        
        this.btnReset=crearBoton(new JButton("Reset"));
        this.btnReset.addActionListener(e -> {
        	try{controller.resetGrafo();}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}});
        
        this.add(new JLabel("Vertice Origen"));this.add(cmbVertice1);this.add(new JLabel("Vertice Destino"));
    	this.add(cmbVertice2); this.add(new JLabel("Cantidad de saltos"));this.add(txtLongitudCamino);        
    	this.add(btnBuscarCamino); this.add(btnPageRank); this.add(cmbTemas); this.add(btnReset);
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

    public GrafoController getController() {
        return controller;
    }

    public void setController(GrafoController controller) {
        this.controller = controller;
    }
}
