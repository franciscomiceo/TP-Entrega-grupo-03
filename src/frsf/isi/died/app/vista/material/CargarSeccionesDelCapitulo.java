package frsf.isi.died.app.vista.material;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.*;

import frsf.isi.died.tp.estructuras.ArbolNario;
import frsf.isi.died.tp.estructuras.Nodo;
import frsf.isi.died.tp.estructuras.TipoNodo;

public class CargarSeccionesDelCapitulo extends JDialog {

	private JScrollPane scrollPane;
	
	public CargarSeccionesDelCapitulo(JFrame principal, boolean modal,Integer capituloActual,ArbolNario arbol) 
    {
		super(principal,modal);
		setTitle("Cargar Secciones del Capitulo "+arbol.getRaiz().getHijoAt(capituloActual).getRaiz().getValor());
        this.setLocationRelativeTo(principal);
        setSize(450, 244);
        initComponents(capituloActual,arbol,principal);
        //setVisible(true);
    }
//---------------------------------------------------------------------------------------------------------------------
	private void initComponents(Integer capituloActual,ArbolNario arbol,JFrame principal){
		this.setLayout(new GridLayout(2,1));
		
	  JButton btnCargarSecciones=crearBoton(new JButton("Cargar Secciones (una por linea)"));
	  btnCargarSecciones.addActionListener( (ev) ->{ 
		  List<String> secciones=this.obtenerLista(scrollPane);
		  if(secciones.size()>0)
		  {
			  int indexSeccion=0;
			  Queue<JDialog> cargasParrafos=new LinkedList<>();
			  for(String seccion: secciones)
			  {
				  Nodo nodoSeccion=new Nodo(TipoNodo.SECCION,seccion);
				  ArbolNario arbolSeccion=new ArbolNario(nodoSeccion);
				  arbol.getRaiz().getHijoAt(capituloActual).getRaiz().addHijo(arbolSeccion);
				  
				  CargarParrafosDeSeccion cps=new CargarParrafosDeSeccion(indexSeccion, capituloActual,principal,arbol);
				  cargasParrafos.offer(cps);
				  indexSeccion++;
			  }
			  dispose();
	
			  while(!cargasParrafos.isEmpty())
				  cargasParrafos.poll().setVisible(true);
		  }
		  else JOptionPane.showMessageDialog(null, "No se ha cargado ninguna seccion");
	  }); 

	  cargarComponentes(new JTextArea(), btnCargarSecciones);
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

 //---------------------------------------------------------------------------------------------------------------------------------   
    protected void aMayuscula(KeyEvent evt) {
    	  char c=evt.getKeyChar();
          if(Character.isLowerCase(c)){
              String cad=(""+c).toUpperCase();
              c=cad.charAt((0));
              evt.setKeyChar(c);
          }
    }	
    
    private List<String> obtenerLista(JScrollPane scrollPane) {
    	List<String> lista=new ArrayList<>();
    	JTextArea jta=(JTextArea)scrollPane.getViewport().getView();
    	
    	for(String s: jta.getText().split("\\n"))
    		if(s.trim().length()>2)
    			lista.add(s.trim());
    	
    	return lista;
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

}
