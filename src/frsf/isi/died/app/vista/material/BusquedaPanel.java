package frsf.isi.died.app.vista.material;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import datechooser.beans.DateChooserCombo;
import datechooser.events.SelectionChangedEvent;
import frsf.isi.died.app.controller.BusquedaController;
import frsf.isi.died.tp.estructuras.ArbolNario;
import frsf.isi.died.tp.modelo.productos.Libro;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;
import frsf.isi.died.tp.modelo.productos.Video;

public class BusquedaPanel extends JPanel {

	private JFrame principal;
	
	private JScrollPane scrollPane;
	private JTable tabla;
	private BusquedaTableModel tableModel;
	
	private JComboBox<String> cbCriterio;
	private DateChooserCombo dccFechaInicio,dccFechaFin;
	
	private JButton btnASC,btnDESC,btnAgregar,btnQuitar,btnMostrar,btnCargarContenido,btnBuscarContenido;
	private boolean mostrado;
	
	List<ArbolNario> arboles; //donde se almacenan los arboles de contenidos cargados hasta el momento, para seguir cargando más 
	
	private BusquedaController controller;
	
	public BusquedaPanel(JFrame principal, List<ArbolNario> arboles) {
		this.arboles=arboles;
		this.setPrincipal(principal);
		this.setLayout(new GridBagLayout());
		tableModel = new BusquedaTableModel();
	}

	public void construir() {
		mostrado=false; //TODO ver
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yy");
		this.setLayout(new GridLayout(2, 1));

		//creo los botones con sus funciones
		btnASC=crearBoton(new JButton("Buscar ASC"));
		btnASC.addActionListener( (e) ->{mostrado=false; mostrarMaterialesOrdenados(cbCriterio.getSelectedItem().toString(),true);});//ASCENDENTEMENTE
		
		btnDESC=crearBoton(new JButton("Buscar DESC"));
		btnDESC.addActionListener( (e) ->{mostrado=false; mostrarMaterialesOrdenados(cbCriterio.getSelectedItem().toString(),false);});//DESCENDENTEMENTE
		
		btnAgregar=crearBoton(new JButton("Agregar a WISHLIST"));
		btnAgregar.addActionListener( (e) ->{
			if(mostrado) JOptionPane.showMessageDialog(null, "Estos materiales ya fueron agregado");
			else if(tabla.getSelectedRowCount()==1)
		    {
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				MaterialCapacitacion mc=null;
				if(tabla.getValueAt(tabla.getSelectedRow(), 5).toString()=="-") //duracion="-" --> es un Libro
				{
					//(Integer id, String titulo, Double costo, Double precioCompra, Integer paginas,Date fp)
					try {
					mc=new Libro(
							Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 0).toString()),
							tabla.getValueAt(tabla.getSelectedRow(), 1).toString(),
							Double.parseDouble(tabla.getValueAt(tabla.getSelectedRow(), 3).toString()),
							Double.parseDouble(tabla.getValueAt(tabla.getSelectedRow(), 2).toString()),
							Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 4).toString()),
							format.parse(tabla.getValueAt(tabla.getSelectedRow(), 6).toString()),
							Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 7).toString()),
							tabla.getValueAt(tabla.getSelectedRow(), 8).toString());
					}catch(Exception ex) {JOptionPane.showMessageDialog(null,"No se pudo obtener la fecha de publicacion del libro "+tabla.getValueAt(tabla.getSelectedRow(), 1).toString());}
				}
				else //sino es un Video
				{
					//(Integer id,String titulo, Double costo, Integer duracion,Date fecha)
					try { 
						mc=new Video(
								Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 0).toString()),
								tabla.getValueAt(tabla.getSelectedRow(), 1).toString(),
								Double.parseDouble(tabla.getValueAt(tabla.getSelectedRow(), 3).toString()),
								Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 5).toString()),
								format.parse(tabla.getValueAt(tabla.getSelectedRow(), 6).toString()),
								Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 7).toString()),
								tabla.getValueAt(tabla.getSelectedRow(), 8).toString());
						}catch(Exception ex) {JOptionPane.showMessageDialog(null,"No se pudo obtener la fecha de publicacion del video "+tabla.getValueAt(tabla.getSelectedRow(), 1).toString());}
				}
				
				if(mc!=null)
					controller.agregarMonticulo(mc);
		    }
			else JOptionPane.showMessageDialog(null, "Debe Seleccionar solo 1 material");});
				
		btnQuitar=crearBoton(new JButton("Quitar del WishList"));
		btnQuitar.addActionListener( (e) ->{
			if(mostrado) 
				if(tabla.getSelectedRowCount()==1)
					controller.quitar(Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 0).toString()));
				else JOptionPane.showMessageDialog(null, "Debe Seleccionar solo 1 material");
			else JOptionPane.showMessageDialog(null, "Debe clickear el boton Mostrar WishList primero");
		});
		
		btnMostrar=crearBoton(new JButton("Mostrar WISHLIST"));
		btnMostrar.addActionListener( (e) ->{if(controller.mostrarMonticulo()) mostrado=true;});
				
		btnCargarContenido=crearBoton(new JButton("Cargar Contenido"));
		btnCargarContenido.addActionListener( (e) ->{     
			if(tabla.getSelectedRowCount()==1)
			{
				//Verifico primero que no se haya cargado aún el contenido del material seleccionado (analizando el TITULO)
				if(!arboles.stream().anyMatch(arbol->arbol.getRaiz().getValor().equals(tabla.getValueAt(tabla.getSelectedRow(), 1).toString())))
				{
					try  //cargo el arbol del material elegido
					{controller.cargarContenido(Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 0).toString()), //ID
						tabla.getValueAt(tabla.getSelectedRow(), 1).toString(), //TITULO
						tabla.getValueAt(tabla.getSelectedRow(), 6).toString());  //FECHA
					}catch(Exception ex) {JOptionPane.showMessageDialog(null,"No se pudo obtener la fecha de publicacion del material "+tabla.getValueAt(tabla.getSelectedRow(), 1).toString());}
				}else JOptionPane.showMessageDialog(null, "Ya se cargó el contenido de este material");
			}
			else JOptionPane.showMessageDialog(null, "Debe Seleccionar solo 1 material");
			}); 
		
		btnBuscarContenido=crearBoton(new JButton("Buscar Contenido"));
		btnBuscarContenido.addActionListener( (e) ->{     
			if(arboles.size()>0)
			{
				controller.buscarContenido(this.arboles);
			}
			else JOptionPane.showMessageDialog(null, "Debe cargar al menos 1 contenido");
			}); 
		
		//comboBox para el criterio de búsqueda
		cbCriterio=new JComboBox<String>(new String[] {"Titulo", "Calificacion", "Tema","Fecha Publicacion","Precio Compra","Relevancia"});
		cbCriterio.addItemListener((ItemEvent e) -> {		//activa/desactiva la seleccion de rango de fechas
			if(e.getStateChange()==ItemEvent.SELECTED&&e.getItem().toString().equals("Fecha Publicacion"))
				dccFechaInicio.setEnabled(true);
			else 
			{
				if(dccFechaFin.getSelectedDate()!=null)dccFechaFin.setSelectedDate(null);
				if(dccFechaInicio.getSelectedDate()!=null)dccFechaInicio.setSelectedDate(null);
				dccFechaInicio.setEnabled(false);
				dccFechaFin.setEnabled(false);
			}});
		
		//creo los componentes para las FECHAS	
		Calendar maxDate=Calendar.getInstance(); 
		maxDate.setTime(new Date()); //HASTA "hoy"
		
		Dimension dim=new Dimension(350,200);
		dccFechaInicio = new DateChooserCombo();
		dccFechaInicio.setDateFormat(fmt);
		dccFechaInicio.setCalendarPreferredSize(dim);
		Calendar minInicio = Calendar.getInstance();
		minInicio.set(Calendar.YEAR, 1800);
		minInicio.set(Calendar.MONTH, 0);
		minInicio.set(Calendar.DAY_OF_MONTH, 0);
		dccFechaInicio.setMinDate(minInicio);
		dccFechaInicio.setMaxDate(maxDate);
		dccFechaInicio.setSelectedDate(null);
		dccFechaInicio.setEnabled(false);
		dccFechaInicio.addSelectionChangedListener(sce-> {FechaInicioActionPerformed(sce);});
		
		dccFechaFin = new DateChooserCombo();
		dccFechaFin.setDateFormat(fmt);
		dccFechaFin.setCalendarPreferredSize(dim);
		dccFechaFin.setMaxDate(maxDate);
		dccFechaFin.setSelectedDate(null);
		dccFechaFin.setEnabled(false);
		dccFechaFin.addSelectionChangedListener(sce-> {FechaFinActionPerformed(sce);});
		
		//agrego todo a un panel, que luego será agregado al panelBusqueda
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(0,3));
		agregarLinea(new JLabel("Criterio: "),cbCriterio,btnASC,panel);
		agregarLinea(new JLabel("Desde: "),dccFechaInicio,btnDESC,panel);
		agregarLinea(new JLabel("Hasta: "),dccFechaFin,btnAgregar,panel);
		agregarLinea(new JLabel(),new JLabel(),btnQuitar,panel);
		agregarLinea(new JLabel(),new JLabel(),btnMostrar,panel);
		agregarLinea(new JLabel(),new JLabel(),btnCargarContenido,panel);
		agregarLinea(new JLabel(),new JLabel(),btnBuscarContenido,panel);
		this.add(panel);
		
		//por último, creo la tabla y la agrego al panelBusqueda
		tabla = new JTable(this.tableModel);
		tabla.setFillsViewportHeight(true);
		tabla.getColumnModel().getColumn(0).setMaxWidth(0);  //Para ocultar la columna "ID"
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
		scrollPane= new JScrollPane(tabla);
		this.add(scrollPane);
	}
private void agregarLinea(JLabel lbl, Component c, JButton boton,JPanel panel) {
		panel.add(lbl); panel.add(c); panel.add(boton);
	}

//--------------------------------------------------------------------------------------------------------------------------------
	private void mostrarMaterialesOrdenados(String criterio,boolean asc) {
		switch(criterio)
		{
		case "Titulo":
			controller.mostrarMateriales(
			(MaterialCapacitacion mc1, MaterialCapacitacion mc2)->mc1.getTitulo().compareTo(mc2.getTitulo()),
			asc, 0); //ult arg=0 (ambos) 1(libros) 2(videos)
			break;
		case "Calificacion": 
			controller.mostrarMateriales(
			(MaterialCapacitacion mc1, MaterialCapacitacion mc2)->
			{int ret=mc1.getCalificacion().compareTo(mc2.getCalificacion());
			if(ret!=0) return ret;
			return mc1.getTitulo().compareTo(mc2.getTitulo());},
			asc,0); //si tienen =calificacion-->compara titulos alf//
			break;
		case "Tema":	
			controller.mostrarMateriales(
			(MaterialCapacitacion mc1, MaterialCapacitacion mc2)->
			{int ret=mc1.getTema().compareTo(mc2.getTema());
			if(ret!=0) return ret;
			return mc1.getTitulo().compareTo(mc2.getTitulo());},
			asc,0); //si tienen =tema-->compara titulos alf//
			break;
		case "Fecha Publicacion":
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); //primero por el año, desp por el mes, y x ult por el día
			Comparator<MaterialCapacitacion> comparador=(MaterialCapacitacion mc1, MaterialCapacitacion mc2)->
			{int ret=fmt.format(mc1.getFecha_publicacion()).compareTo(fmt.format(mc2.getFecha_publicacion()));
			if(ret!=0) return ret;
			return mc1.getTitulo().compareTo(mc2.getTitulo());}; //si tienen =fechaPublicacion-->compara titulos alf//
			
			if(this.dccFechaInicio.getSelectedDate()!=null&&this.dccFechaFin.getSelectedDate()==null)
				JOptionPane.showMessageDialog(null, "Debe seleccionar una Fecha final");
			else if(this.dccFechaInicio.getSelectedDate()==null&&this.dccFechaFin.getSelectedDate()==null)
				controller.mostrarMateriales(comparador,asc,0); 
			else
			{
				Date desde=dccFechaInicio.getSelectedDate().getTime();
				Date hasta=dccFechaFin.getSelectedDate().getTime();
				controller.mostrarMaterialesEntreFechas(comparador,asc,desde,hasta);
			}
			break;
		case "Precio Compra":
			controller.mostrarMateriales(
					(MaterialCapacitacion mc1, MaterialCapacitacion mc2)->
					{int ret=((Libro)mc1).getPrecioCompra().compareTo(((Libro)mc2).getPrecioCompra());
					if(ret!=0) return ret;
					return mc1.getTitulo().compareTo(mc2.getTitulo());},
					asc,1); //si tienen =precioCompra-->compara titulos alf//
					break;
		case "Relevancia": //si comparan segun el orden de aparacion en la declaracion del Enum 
			controller.mostrarMateriales(
			(MaterialCapacitacion mc1, MaterialCapacitacion mc2)->
			{int ret=mc1.getRelevancia().compareTo(mc2.getRelevancia());
			if(ret!=0) return ret;
			ret=mc1.getCalificacion()-mc2.getCalificacion();//si tienen =relevancia-->compara calificaciones
			if(ret!=0) return ret;
			return mc1.getTitulo().compareTo(mc2.getTitulo());}, //si tienen =calificacion-->compara titulos alf//
			asc,0); 
			break;
		default:
			break;
		}
	}

	public boolean hayMateriales() {return this.tableModel.getRowCount()>0;}
	
	public List<MaterialCapacitacion> getListaMateriales(){return this.tableModel.getMateriales();}
	
	public void setListaMateriales(List<MaterialCapacitacion> materiales,boolean actualizar) {
		this.tableModel.setMateriales(materiales);
		if(actualizar) this.tableModel.fireTableDataChanged();
	}

	public void add(MaterialCapacitacion mc,boolean actualizar) {
		this.tableModel.add(mc);
		if(actualizar) this.tableModel.fireTableDataChanged();
	}
	
	public void quitar(int id_material) {
		this.tableModel.quitar(id_material);
		this.tableModel.fireTableDataChanged();
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
			
//---------------------------------------------------------------------------------------------------------------------
	private void FechaInicioActionPerformed(SelectionChangedEvent sce) {
		if(dccFechaInicio.getSelectedDate()==null)
			dccFechaFin.setEnabled(false);
		else dccFechaFin.setEnabled(true);
		
		if(dccFechaFin.getSelectedDate()!=null && 
				dccFechaInicio.getSelectedDate().compareTo(dccFechaFin.getSelectedDate())>0)
		{
			JOptionPane.showMessageDialog(null, "Debe seleccionar una Fecha de Inicio menor o igual a Fecha de Fin");
			dccFechaInicio.setSelectedDate(dccFechaFin.getSelectedDate());
		}
	}

	private void FechaFinActionPerformed(SelectionChangedEvent sce) {	
		if(dccFechaInicio.getSelectedDate()!=null&&dccFechaFin.getSelectedDate()!=null)
		if(dccFechaInicio.getSelectedDate().compareTo(dccFechaFin.getSelectedDate())>0)
			{
				JOptionPane.showMessageDialog(null, "Debe seleccionar una Fecha de Fin mayor o igual a Fecha de Inicio");
				dccFechaFin.setSelectedDate(dccFechaInicio.getSelectedDate());
			}
	}

	public BusquedaController getController() {
		return controller;
	}

	public void setController(BusquedaController controller) {
		this.controller = controller;
	}
	
	public JFrame getPrincipal() {
		return principal;
	}

	public void setPrincipal(JFrame principal) {
		this.principal = principal;
	}

	
	public List<ArbolNario> getArboles() {
		return arboles;
	}

	public void setArboles(List<ArbolNario> arboles) {
		this.arboles = arboles;
	}

}
