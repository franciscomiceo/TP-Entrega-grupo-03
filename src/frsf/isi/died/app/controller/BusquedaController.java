package frsf.isi.died.app.controller;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import frsf.isi.died.app.dao.MaterialConexion;
import frsf.isi.died.app.vista.material.BuscarContenidoPanel;
import frsf.isi.died.app.vista.material.BusquedaPanel;
import frsf.isi.died.app.vista.material.CargarSeccionesDelCapitulo;
import frsf.isi.died.tp.estructuras.ArbolNario;
import frsf.isi.died.tp.estructuras.Nodo;
import frsf.isi.died.tp.estructuras.TipoNodo;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;

public class BusquedaController {

	private BusquedaPanel panelBusqueda;
	private HashSet<MaterialCapacitacion> hs;
	private Queue<MaterialCapacitacion> monticulo; //PriorityQueue!!!
	
	private ArbolNario arbolNarioPrincipal; //en "cargarContenido" y "construirPestanas"
	private Integer capituloSeccion=2; //indexNodoCapitulo desde donde arranco a cargar Secciones c/parrafos, y Metadatos
	private Integer capituloMetadato=2;
	
	public BusquedaController(BusquedaPanel panelBusqueda) {
		this.panelBusqueda = panelBusqueda;
		this.panelBusqueda.setController(this);
		
		Comparator<MaterialCapacitacion> comparador=(MaterialCapacitacion mc1,MaterialCapacitacion mc2)->{
			int ret=0; //mc1.getRelevancia().compareTo(mc2.getRelevancia()); 
			if(ret==0)
				{
					ret=mc2.getCalificacion().compareTo(mc1.getCalificacion());
					if(ret==0)
					{
						ret=mc2.precio().compareTo(mc1.precio());
						if(ret==0)
							return mc2.getTitulo().compareTo(mc1.getTitulo());
					}
				}
			return ret;
		};
		
		monticulo=new PriorityQueue<>(comparador);
		hs=new HashSet<>();
	}

	public void crearPanel() {
	this.panelBusqueda.construir();
	}
	

	public BusquedaPanel getPanelBusqueda() {
		return panelBusqueda;
	}

	public void setPanelBusqueda(BusquedaPanel panelBusqueda) {
		this.panelBusqueda = panelBusqueda;
	}

	//tipo=0 (ambos) 1 (libros) 2 (videos)
	public void mostrarMateriales(Comparator<MaterialCapacitacion> comparador,boolean asc,int tipo) {
		this.panelBusqueda.setListaMateriales(new ArrayList<>(),true); //vacio la tabla
		TreeSet<MaterialCapacitacion> arbol=new TreeSet<>(comparador);
		
		try
		{
			if(tipo<=1)
			for(MaterialCapacitacion mc: MaterialConexion.getLibros())
				arbol.add(mc);
			if(tipo!=1)
			for(MaterialCapacitacion mc: MaterialConexion.getVideos())
				arbol.add(mc);
			if(arbol.size()>0)
				{
					Iterator<MaterialCapacitacion> it;
					if(asc) //ASCENDENTEMENTE 
						it=arbol.iterator();
					else //DESCENDENTEMENTE
						it=arbol.descendingIterator();
					while(it.hasNext())
						this.panelBusqueda.add(it.next(),true);
				}
		}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
	}

	public void mostrarMaterialesEntreFechas(Comparator<MaterialCapacitacion> comparador, boolean asc, Date desde, Date hasta) {
		this.panelBusqueda.setListaMateriales(new ArrayList<>(),true); //vacio la tabla
		TreeSet<MaterialCapacitacion> arbol=new TreeSet<>(comparador);
		List<MaterialCapacitacion> materiales=new ArrayList<>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(desde);
		cal.add(Calendar.DAY_OF_YEAR,-1);
		Date desdeAnt= cal.getTime();
		
		//System.out.println(desdeAnt);
		
		try {
		for(MaterialCapacitacion mc: MaterialConexion.getLibros())
			materiales.add(mc);
		for(MaterialCapacitacion mc: MaterialConexion.getVideos())
			materiales.add(mc);
		if(materiales.size()>0)
		{
			materiales=materiales.stream()
					.filter(mc->mc.getFecha_publicacion().after(desdeAnt)
							&&mc.getFecha_publicacion().before(hasta))
					.collect(Collectors.toList());
			for(MaterialCapacitacion mc: materiales)
				arbol.add(mc);
			if(arbol.size()>0)
			{
				Iterator<MaterialCapacitacion> it;
				if(asc) //ASCENDENTEMENTE 
					it=arbol.iterator();
				else //DESCENDENTEMENTE
					it=arbol.descendingIterator();
				while(it.hasNext())
					this.panelBusqueda.add(it.next(),true);
			}
		}
		}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
	}

	public void agregarMonticulo(MaterialCapacitacion mc) {
		if(hs.contains(mc)) JOptionPane.showMessageDialog(null,"Ya habia sido agregado al WishList");
		else
			hs.add(mc); //lo voy agregando primero al HASHSET (sin duplicados)
		//System.out.println(hs);
	}
	
	public void quitar(int id_material)
	{
		hs.removeIf(mc->mc.getId()==id_material);
		this.panelBusqueda.quitar(id_material);
	}
	
	public boolean mostrarMonticulo()
	{
		if(hs.size()==0) {JOptionPane.showMessageDialog(null,"No hay ningun material agregado al WishList"); return false;}
		
			Iterator<MaterialCapacitacion> it=hs.iterator();
			while(it.hasNext())
			{
				monticulo.add(it.next()); //del HASHSET al MONTICULO
				it.remove();
			}
			//System.out.println("mostrarMonticulo="+monticulo);
			List<MaterialCapacitacion> ret=new ArrayList<>();
			while(!monticulo.isEmpty())
				ret.add(monticulo.poll()); //voy cargando en un ArrayList los elementos sacados del MONTICULO (de < a >, segun el comparador)
			//System.out.println("lista para setear tabla="+ret);
			this.panelBusqueda.setListaMateriales(ret,true);  //los muestro ordenados en la tabla de busqueda
			for(MaterialCapacitacion mc: ret)
				hs.add(mc);		//recupero nuevamente los materiales sacados del HASHSET
			//System.out.println("A la salida del mostrarMonticulo="+hs);
		return true;
	}

	public void cargarContenido(int id_material, String titulo, String fecha) {
		
		JFrame principal=this.panelBusqueda.getPrincipal();
		principal.setTitle("Carga del Contenido del material: "+titulo);
		  // el panel que contiene todo se crea y se pone en el frame
		  JPanel contentPane = new JPanel();
		  
		  contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		  principal.setContentPane(contentPane);
		  // distribución nula para poder posicionar los elementos
		  // en las coordenadas que queramos
		  contentPane.setLayout(null);
		  
		  // se crea el panel de pestañas
		  JTabbedPane panelDePestanas = new JTabbedPane(JTabbedPane.SCROLL_TAB_LAYOUT);
		  // se posiciona en el panel
		  panelDePestanas.setBounds(10, 11, 450, 250);
		  contentPane.add(panelDePestanas);
		  

		// coordenadas de las esquinas del frame principal
		principal.setBounds(150, 150, 500, 400);
		principal.setLocationRelativeTo(null);
		  
		   //se cargan las pestañas del panel de pestañas
		  construirPestañas(panelDePestanas,titulo,fecha);
	}		  

	
    private void construirPestañas(JTabbedPane panelDePestanas, String titulo, String fecha) {
		
    	//comienzo a construir el arbolNario con el TITULO, METADATO y FECHA
    			Nodo nodoTitulo=new Nodo(titulo);
    			Nodo nodoMetadato=new Nodo(TipoNodo.METADATO,null);
    			Nodo nodoFecha=new Nodo(TipoNodo.FECHA_PUBLICACION,fecha);
    			
    			ArbolNario arbolFecha=new ArbolNario(nodoFecha);
    			nodoMetadato.addHijo(arbolFecha);
    			ArbolNario arbolMetadato=new ArbolNario(nodoMetadato);
    			nodoTitulo.addHijo(arbolMetadato);
    			arbolNarioPrincipal=new ArbolNario(nodoTitulo);
    			
    	
    	  // PANEL "METADATOS"
		  JPanel metadatos = new JPanel();
		  panelDePestanas.addTab("METADATOS", null, metadatos, null);
		  metadatos.setLayout(new GridLayout(0, 2)); //0-->cualquier cant de filas;;; 2-->sólo dos elementos por fila
		  
		  agregarLinea(new JLabel("Titulo: "),new JLabel(titulo),metadatos);
		  agregarLinea(new JLabel("Fecha Publicacion: "),new JLabel(fecha),metadatos);
		  agregarLinea(new JLabel("Autor (uno por linea): "),new JTextArea(),metadatos);
		  agregarLinea(new JLabel("Editorial: "),new JTextField(),metadatos);
		  agregarLinea(new JLabel("Palabras Claves: "),new JTextField(),metadatos);
		  
		  JButton btnCargarMetadatos=crearBoton(new JButton("Cargar Metadatos"));
		  btnCargarMetadatos.addActionListener( (e) ->{ 
			if(verificarCargaTotal(metadatos,btnCargarMetadatos)) //Se cargaron TODOS los campos de METADATOS-->sigo cargando los hijos del nodoMetadato
			{
				boolean primerJTF=true;
				for(Component c: metadatos.getComponents())
				  {
				  	if(c instanceof JTextField)
				  		if(primerJTF)	//EDITORIAL
				  		{
				  			primerJTF=false;
				  			Nodo nodoEditorial=new Nodo(TipoNodo.EDITORIAL,((JTextField)c).getText().trim());
				  			ArbolNario arbolEditorial=new ArbolNario(nodoEditorial); 
				  			nodoMetadato.addHijo(arbolEditorial);
				  		}
				  		else	//PALABRAS CLAVES
				  		{
				  			Nodo nodoPalabrasClaves=new Nodo(TipoNodo.PALABRAS_CLAVES,((JTextField)c).getText().trim());
				  			ArbolNario arbolPalabrasClaves=new ArbolNario(nodoPalabrasClaves); 
				  			nodoMetadato.addHijo(arbolPalabrasClaves);
				  		}
				  	if(c instanceof JScrollPane) //AUTOR*
				  	{
				  		List<String> autores=obtenerLista((JScrollPane)c);
				  		for(String autor: autores)
				  		{	
				  			Nodo nodoAutor=new Nodo(TipoNodo.AUTOR,autor);
				  			ArbolNario arbolAutor=new ArbolNario(nodoAutor); 
				  			nodoMetadato.addHijo(arbolAutor);
				  		}
				  	}
				  }
			}
			//System.out.println(this.arbolNarioPrincipal);
		  }); 
		  
		  agregarLinea(new JLabel("Ya cargados, NO podran modificarse"),btnCargarMetadatos,metadatos);
		  
		  // PANEL "RESUMEN"
		  JPanel resumen = new JPanel();
		  panelDePestanas.addTab("PARRAFOS DEL RESUMEN", null, resumen, null);
		  resumen.setLayout(new GridLayout(2, 1));
		  
		  JButton btnCargarResumen=crearBoton(new JButton("Cargar Párrafos del Resumen (finalizan en . seguido de un salto de línea)"));
		  btnCargarResumen.addActionListener( (e) ->{ // un "." seguido de un "\n"-->1 nuevo párrafo 
		if(!btnCargarMetadatos.isEnabled())
		{	  if(verificarCargaTotal(resumen,btnCargarResumen))
			  {
				  Nodo nodoResumen=new Nodo(TipoNodo.RESUMEN,null);
				  
				  List<String> parrafos=obtenerListaParrafos((JScrollPane)resumen.getComponent(0));
			  	  
				  for(String parrafo: parrafos)
			  	  {	
			  	      Nodo nodoParrafo=new Nodo(TipoNodo.PARRAFO,parrafo+".");
			  		  ArbolNario arbolParrafo=new ArbolNario(nodoParrafo); 
			  		  nodoResumen.addHijo(arbolParrafo);
			  	  }
			  		
			  	  ArbolNario arbolResumen=new ArbolNario(nodoResumen);
			  	  nodoTitulo.addHijo(arbolResumen);
			  }
		}else JOptionPane.showMessageDialog(null, "Debe cargar primero los METADATOS");
		  }); 
		  
		  agregarLinea(new JTextArea(),btnCargarResumen,resumen);
		  
		// PANEL "CAPITULOS"
		  JPanel capitulos = new JPanel();
		  panelDePestanas.addTab("CAPITULOS", null, capitulos, null);
		  capitulos.setLayout(new GridLayout(2, 1));
		  
		  JTextArea txtCapitulos= new JTextArea(); 
		  txtCapitulos.addKeyListener(new java.awt.event.KeyListener() {
	            @Override
	            public void keyTyped(java.awt.event.KeyEvent evt) {aMayuscula(evt);}
	            @Override
	            public void keyPressed(KeyEvent e) {}
	            @Override
	            public void keyReleased(KeyEvent e) {}});
			
		  JScrollPane scrollPaneCapitulos = new JScrollPane(txtCapitulos);
		  
		  agregarLinea(new JLabel("Capitulos (uno por línea): "),scrollPaneCapitulos,capitulos);
		  
		  JButton btnCargarCapitulos=crearBoton(new JButton("Cargar Capitulos (hasta 3 por carga; c/más de 2 caracteres)"));
		  		
		  JButton btnFinalizar=crearBoton(new JButton("Finalizar Carga Contenido"));
		  
		  btnFinalizar.addActionListener( (e) ->{volverBusqueda();});
		  
		  agregarLinea(btnCargarCapitulos,btnFinalizar,capitulos);

		// PANEL "METADATOS DE CAPITULOS"
		  JPanel metadatosCap = new JPanel();
		  panelDePestanas.addTab("METADATOS DE CAPITULOS", null, metadatosCap, null);
		  metadatosCap.setLayout(new GridLayout(0, 2));
		  		  
		  btnCargarCapitulos.addActionListener( (e) ->{
		if(!btnCargarResumen.isEnabled())
		{
			  List<String> caps=obtenerLista(scrollPaneCapitulos); //sólo capítulos con más de 2 caracteres 
			  
			  if(caps.size()>0&&caps.size()<=3 && verificarCargaTotal(capitulos, btnCargarCapitulos)) //se pueden cargar hasta 3 capítulos
			  {   
				  Queue<JDialog> cargaSecciones=new LinkedList<>();
				  
				  for(int i=0;i<caps.size();i++) //POR CAPITULO
				  { 
					  //Sigo cargando los hijos (nodos Capitulo) del nodoTitulo
					  Nodo nodoCapitulo=new Nodo(TipoNodo.CAPITULO,caps.get(i));
					  ArbolNario arbolCapitulo=new ArbolNario(nodoCapitulo);
					  nodoTitulo.addHijo(arbolCapitulo);
					  
					  //Para los metadatos de los capitulos
					  agregarLinea(new JLabel("Sitios web del Capitulo "+caps.get(i)+" (uno por línea): "),new JTextArea(),metadatosCap);
					  agregarLinea(new JLabel("Sitios web Ejercicios (uno por línea): "),new JTextArea(),metadatosCap);
					  agregarLinea(new JLabel("Palabras Claves: "),new JTextField(),metadatosCap);
					  
					  //Solicito carga de Secciones del Capitulo actual (JDialog)
					  CargarSeccionesDelCapitulo csc=new CargarSeccionesDelCapitulo(this.panelBusqueda.getPrincipal(),true,capituloSeccion,arbolNarioPrincipal);
					  cargaSecciones.offer(csc);
					  capituloSeccion++;
				  }
				  
				  //para cargar los metadatos de los capitulos
				  JButton btnCargarMetadatosCap=crearBoton(new JButton("Cargar Metadatos de Capitulos"));
	
				  btnCargarMetadatosCap.addActionListener( (ev) ->{ 
				  if(this.verificarCargaTotal(metadatosCap, btnCargarMetadatosCap))
				  {
					  //Cargo nodoMetadato con sus nodosHijos como hijo de cada nodoCapitulo (indexHijo=capituloMetadatos) del nodoTitulo
					  int lugar=0;
					  Nodo nodoMetadatoCap=null;
						
					  for(int i=1;i<metadatosCap.getComponentCount()-2;i+=2) //PARA CADA CAPITULO, voy cargando los Metadatos en el arbol
					  {
						  if(lugar==0) //SITIOS WEB RELACIONADOS
						  {
							  nodoMetadatoCap=new Nodo(TipoNodo.METADATO,null);
							  lugar++;
							  List<String> sitios=obtenerLista((JScrollPane)metadatosCap.getComponent(i));
							  for(String sitio: sitios)
							  {
								  Nodo nodoSitio=new Nodo(TipoNodo.SITIO_WEB,sitio);
								  ArbolNario arbolSitio=new ArbolNario(nodoSitio);
								  nodoMetadatoCap.addHijo(arbolSitio);
							  }
							  
						  }
						  else if(lugar==1) //SITIOS WEB EJERCICIOS RELACIONADOS
						  {
							  lugar++;
							  List<String> ejercicios=obtenerLista((JScrollPane)metadatosCap.getComponent(i));
							  for(String ejercicio: ejercicios)
							  {
								  Nodo nodoEjercicio=new Nodo(TipoNodo.SITIO_WEB_EJERCICIO,ejercicio);
								  ArbolNario arbolEjercicio=new ArbolNario(nodoEjercicio);
								  nodoMetadatoCap.addHijo(arbolEjercicio);
							  }
						  }
						  else //PALABRAS CLAVES
						  {//TODO
							  	lugar=0;
							    Nodo nodoPalabra=new Nodo(TipoNodo.PALABRAS_CLAVES,((JTextField)metadatosCap.getComponent(i)).getText().trim());
								ArbolNario arbolPalabra=new ArbolNario(nodoPalabra);
								nodoMetadatoCap.addHijo(arbolPalabra);
								

								ArbolNario arbolMetadatoCap=new ArbolNario(nodoMetadatoCap);
								nodoTitulo.getHijoAt(capituloMetadato).getRaiz().addHijo(arbolMetadatoCap);
								capituloMetadato++;
						  }
					  }
					  
					  //System.out.println("ARBOL:"); //TODO
					  //System.out.println(this.arbolNarioPrincipal.toString());
					  
					  //limpio las pestañas "METADATOS DE CAPITULOS"
					  metadatosCap.removeAll();
					  
					  //limpio JTextArea y lo activo, y activo btnCargarCapitulos; en la Pestaña "CAPITULOS"
					  txtCapitulos.setText("");
					  txtCapitulos.setEditable(true);
					  btnCargarCapitulos.setEnabled(true);
				  }//fin if(verificarCargaTotal)
				  }); 
				  
				  agregarLinea(new JLabel(),btnCargarMetadatosCap,metadatosCap);
				  
				  while(!cargaSecciones.isEmpty())
					  cargaSecciones.poll().setVisible(true);
			  }
			  else JOptionPane.showMessageDialog(null, "Debe cargar hasta 3 capitulos");
		}else JOptionPane.showMessageDialog(null, "Debe cargar primero los PARRAFOS DEL RESUMEN");
		  });		  
		 }

 private void volverBusqueda() {
	//System.out.println(this.arbolNarioPrincipal); 
	  
	  	this.getPanelBusqueda().getArboles().add(arbolNarioPrincipal); //SE AÑADE EL ARBOL DEL CONTENIDO DEL MATERIAL CARGADO
	  	JFrame framePrincipal=this.panelBusqueda.getPrincipal();
	    framePrincipal.setTitle("Busquedas");
		framePrincipal.setContentPane(this.getPanelBusqueda());
	    framePrincipal.pack();		
	}

//le agrega listener cuando pulso ENTER en el boton
 private JButton crearBoton(JButton boton) {
	 boton.addKeyListener(new java.awt.event.KeyListener() {

         @Override
         public void keyTyped(KeyEvent e) {
         }

         @Override
         public void keyPressed(KeyEvent e) {
  if(e.getKeyCode() == 10)
 	 boton.doClick();
    }

         @Override
         public void keyReleased(KeyEvent e) {
         }
     });  
	 return boton;
	}

//Carga los componentes c1 y c2 en una linea de la pestaña pasada como argumento
private void agregarLinea(Component c1, Component c2, JPanel pestana) {
	
	if(c1 instanceof JLabel && c2 instanceof JScrollPane)
	{
		pestana.add(c1); pestana.add(c2);
	}
	else if(c1 instanceof JLabel) //c1=JLabel y c2=txtArea o txtField o JButton
	{
	pestana.add(c1);
	
	if(!(c2 instanceof JButton))
		c2.addKeyListener(new java.awt.event.KeyListener() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {aMayuscula(evt);}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}});
		
	  if(c2 instanceof JTextArea)
	  {
		  //habilito sólo el scroll Vertical
		  ((JTextArea)c2).setLineWrap(true);
		  ((JTextArea)c2).setWrapStyleWord(true);
		  JScrollPane scrollPane = new JScrollPane(c2);
		  pestana.add(scrollPane);
	  }
	  else //c2 txtField o JButton
		  pestana.add(c2);
	}
	else if(c1 instanceof JTextArea)//c1=JTxtArea y c2=JButton
	{
		c1.addKeyListener(new java.awt.event.KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {aMayuscula(evt);}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}});
		//habilito sólo el scroll Vertical
		((JTextArea)c1).setLineWrap(true);
		((JTextArea)c1).setWrapStyleWord(true);
		JScrollPane scrollPaneParrafo = new JScrollPane(c1);
		pestana.add(scrollPaneParrafo);
		pestana.add(c2);
	}
	else //c1 y c2=JButton
	{
		pestana.add(c1); pestana.add(c2);
	}
	
}

//-----------------------------------------------------------------------------------------------------------    
    private List<String> obtenerLista(JScrollPane scrollPane) {
    	List<String> lista=new ArrayList<>();
    	JTextArea jta=(JTextArea)scrollPane.getViewport().getView();
    	
    	for(String s: jta.getText().split("\\n"))
    		if(s.trim().length()>2)
    			lista.add(s.trim());
    	
    	//System.out.println(lista);
    	return lista;
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
    
    private boolean verificarCargaTotal(JPanel pestana, JButton btnCargar)
    {
    	  boolean cargados=true;
		  for(Component c: pestana.getComponents())
			  {
			  	if(c instanceof JTextField)
			  		if(((JTextField)c).getText().trim().length()>0)
			  			{if(((JTextField)c).isEditable())
			  			((JTextField)c).setEditable(false);}
			  		else cargados=false;
			  	else if(c instanceof JScrollPane)
			  	{
			  		JTextArea txt=(JTextArea)(((JScrollPane)c).getViewport().getView());
			  			if(txt.getText().trim().length()>0)
			  				{if(txt.isEditable())
			  					txt.setEditable(false);}
			  			else cargados=false;
			  	}
			  }
		  if(cargados) 
		  {
			  //System.out.println("Metadados TODOS cargados");
			  btnCargar.setEnabled(false); //TODOS los Metadatos cargados-->desactivo la carga de metadatos
		  } 
		  else JOptionPane.showMessageDialog(null, "No se han cargado todos los datos de esta pestaña");
		  
		  return cargados;
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
	public void buscarContenido(List<ArbolNario> arboles) {
		JFrame principal=this.panelBusqueda.getPrincipal();
		principal.setTitle("Buscar Contenido");
		  // el panel que contiene todo se crea y se pone en el frame
		  JPanel contentPane = new BuscarContenidoPanel(principal,panelBusqueda,arboles);
		  principal.setContentPane(contentPane);
		  principal.pack();
	}
}
