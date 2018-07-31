package frsf.isi.died.app.vista.material;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import frsf.isi.died.tp.estructuras.ArbolNario;
import frsf.isi.died.tp.estructuras.Nodo;
import frsf.isi.died.tp.estructuras.TipoNodo;


public class CargarParrafosDeSeccion extends JDialog {

	private JScrollPane scrollPane;
	
	public CargarParrafosDeSeccion(int seccionActual, Integer capituloActual, JFrame principal, ArbolNario arbol) {
        super(principal,true);
        this.setLocationRelativeTo(principal);
        this.setTitle("Carga de Parrafos de Seccion "+arbol.getRaiz().getHijoAt(capituloActual).getRaiz().getHijoAt(seccionActual).getRaiz().getValor());
        setSize(450, 244);
		initComponents(seccionActual,capituloActual, principal, arbol);
		//setVisible(true);
    }
//---------------------------------------------------------------------------------------------------------------------                     
    private void initComponents(int seccionActual, Integer capituloActual, JFrame principal, ArbolNario arbol) {
		
		this.setLayout(new GridLayout(2,1));
	  
	  JButton btnCargarParrafos=crearBoton(new JButton("Cargar Parrafos"));
	  btnCargarParrafos.addActionListener( (ev) ->{ 
		  List<String> parrafos=this.obtenerListaParrafos(scrollPane);
		  if(parrafos.size()>0)
		  {
			  for(String parrafo: parrafos)
			  {
				  Nodo nodoParrafo=new Nodo(TipoNodo.PARRAFO,parrafo);
				  ArbolNario arbolParrafo=new ArbolNario(nodoParrafo);
				  arbol.getRaiz().getHijoAt(capituloActual).getRaiz().getHijoAt(seccionActual).getRaiz().addHijo(arbolParrafo);
			  }
			  dispose(); 
		  }
		  else JOptionPane.showMessageDialog(null, "No se ha cargado ningún párrafo");
	  }); 
	  
	  cargarComponentes(new JTextArea(),btnCargarParrafos);
	}

//------------------------------------------------------------------------------------------------------------------------------    
private void cargarComponentes(JTextArea jta, JButton boton) {
	jta.addKeyListener(new java.awt.event.KeyListener() {
        @Override
        public void keyTyped(java.awt.event.KeyEvent evt) {aMayuscula(evt);}
        @Override
        public void keyPressed(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}});
	
	scrollPane = new JScrollPane(jta);
	scrollPane.setSize(200,200);
	this.add(scrollPane);

  this.add(boton);  
}
//-----------------------------------------------------------------------------------------------------------
		protected void aMayuscula(KeyEvent evt) {
	  	  char c=evt.getKeyChar();
	        if(Character.isLowerCase(c)){
	            String cad=(""+c).toUpperCase();
	            c=cad.charAt((0));
	            evt.setKeyChar(c);
	        }
	  }
		
 //-----------------------------------------------------------------------------------------------------------    
	    private List<String> obtenerListaParrafos(JScrollPane scrollPane) {
			List<String> lista=new ArrayList<>();
	    	JTextArea jta=(JTextArea)scrollPane.getViewport().getView();
	    	
	    	for(String s: jta.getText().split("\\n"))
	    		if(s.trim().length()>2)
	    			if(s.trim().charAt(s.trim().length()-1)!='.')
	    				lista.add(s.trim()+".");
	    			else lista.add(s.trim());
	    	
	    	return lista;
		}
//-----------------------------------------------------------------------------------------------------------    	    
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

}
