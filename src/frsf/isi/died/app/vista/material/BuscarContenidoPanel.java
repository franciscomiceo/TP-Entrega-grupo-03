package frsf.isi.died.app.vista.material;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import datechooser.beans.DateChooserCombo;
import datechooser.events.SelectionChangedEvent;
import datechooser.events.SelectionChangedListener;
import frsf.isi.died.tp.estructuras.ArbolNario;
import frsf.isi.died.tp.estructuras.Nodo;
import frsf.isi.died.tp.estructuras.TipoNodo;

public class BuscarContenidoPanel extends JPanel {
	
	private JScrollPane scrollPane;
	private JTable tabla;
	private String[] detalles;

	private DateChooserCombo dccFechaInicio,dccFechaFin;
	
	private JButton btnBuscar,btnSalir;
	
	private BuscarContenidoTableModel tableModel;
	
	public BuscarContenidoPanel(JFrame principal, BusquedaPanel panelBusqueda, List<ArbolNario> arboles)
	{	
		tableModel = new BuscarContenidoTableModel();
		this.setLayout(new GridLayout(2, 1));
		Set<ArbolNario> aux=new HashSet<>(); //Para ir recorriendo los árbolesNarios
		
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(0,2));
		
		agregarLinea(new JCheckBox("Titulo: "),obtenerValoresRaices(new HashSet<>(arboles),TipoNodo.TITULO).toArray(),panel);
		
		aux=obtenerHijosTipo(TipoNodo.METADATO,new HashSet<>(arboles));//arbolesMETADATO (primer hijo del nodoTITULO)
		
		agregarLinea(new JCheckBox("Autor: "),obtenerValoresHijos(TipoNodo.AUTOR,aux,TipoNodo.AUTOR).toArray(),panel);
		agregarLinea(new JCheckBox("Editorial: "),obtenerValoresHijos(TipoNodo.EDITORIAL,aux,TipoNodo.EDITORIAL).toArray(),panel);
		
		panel.add(new JCheckBox("Desde: "));
		Calendar maxDate=Calendar.getInstance(); 
		maxDate.setTime(new Date()); //HASTA "hoy"
		
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yy");
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
		dccFechaInicio.addSelectionChangedListener(new SelectionChangedListener() {
			@Override
			public void onSelectionChange(SelectionChangedEvent sce) {
				FechaInicioActionPerformed(sce);	
			}
        });
		panel.add(dccFechaInicio);
		
		panel.add(new JCheckBox("Hasta: "));
		dccFechaFin = new DateChooserCombo();
		dccFechaFin.setDateFormat(fmt);
		dccFechaFin.setCalendarPreferredSize(dim);
		dccFechaFin.setMaxDate(maxDate);
		dccFechaFin.setSelectedDate(null);
		dccFechaFin.setEnabled(false);
		dccFechaFin.addSelectionChangedListener(new SelectionChangedListener() {
			@Override
			public void onSelectionChange(SelectionChangedEvent sce) {
				FechaFinActionPerformed(sce);	
			}
        });
		panel.add(dccFechaFin);
		
		agregarLinea(new JCheckBox("Palabra Clave en Titulo: "),obtenerPalabrasValoresHijos(TipoNodo.PALABRAS_CLAVES,aux,TipoNodo.TITULO).toArray(),panel);
		agregarLinea(new JCheckBox("Palabra Clave en Parrafos del Resumen: "),obtenerPalabrasValoresHijos(TipoNodo.PALABRAS_CLAVES,aux,TipoNodo.RESUMEN).toArray(),panel);
		agregarLinea(new JCheckBox("Capitulo: "),obtenerValoresHijos(TipoNodo.CAPITULO,new HashSet<>(arboles),TipoNodo.CAPITULO).toArray(),panel);
		
		aux=obtenerHijosTipo(TipoNodo.CAPITULO,new HashSet<>(arboles));//arbolesCAPITULO (segundo hijo del nodoTITULO)
		
		agregarLinea(new JCheckBox("Seccion: "),obtenerValoresHijos(TipoNodo.SECCION,aux,TipoNodo.SECCION).toArray(),panel);
	
		aux=obtenerHijosTipo(TipoNodo.METADATO,new HashSet<>(aux));//arbolesMETADATO descendiente de arbolesCAPITULO
		
		agregarLinea(new JCheckBox("Palabra Clave en Capitulos: "),obtenerPalabrasValoresHijos(TipoNodo.PALABRAS_CLAVES,aux,TipoNodo.CAPITULO).toArray(),panel);
		agregarLinea(new JCheckBox("Palabra Clave en Secciones: "),obtenerPalabrasValoresHijos(TipoNodo.PALABRAS_CLAVES,aux,TipoNodo.SECCION).toArray(),panel);
		agregarLinea(new JCheckBox("Palabra Clave en Parrafos de Secciones: "),obtenerPalabrasValoresHijos(TipoNodo.PALABRAS_CLAVES,aux,TipoNodo.PARRAFO).toArray(),panel);
		agregarLinea(new JCheckBox("Sitio web relacionado: "),obtenerValoresHijos(TipoNodo.SITIO_WEB,aux,TipoNodo.SITIO_WEB).toArray(),panel);
		agregarLinea(new JCheckBox("Sitio web ejercicios relacionado: "),obtenerValoresHijos(TipoNodo.SITIO_WEB_EJERCICIO,aux,TipoNodo.SITIO_WEB_EJERCICIO).toArray(),panel);
		
		btnBuscar=crearBoton(new JButton("Buscar"));
		btnBuscar.addActionListener( (e) ->{ 
			List<ValueMember> busquedas=new ArrayList<>();
			for(int i=0;i<panel.getComponentCount();i++)
			{
				Component c=panel.getComponent(i);
				if(c instanceof JCheckBox)
					if(panel.getComponent(i+1).isEnabled()) //si el sig componente (JComboBox) está activado-->el respectivo JCheckBox está marcado
						{
							if(panel.getComponent(i+1) instanceof JComboBox)
								busquedas.add((ValueMember)((JComboBox)panel.getComponent(i+1)).getSelectedItem()); //agrego el criterio de busqueda a la lista
							else // es la Fecha
							{
								SimpleDateFormat fmto = new SimpleDateFormat("yyyy-MM-dd");
								if(dccFechaInicio==panel.getComponent(i+1) && dccFechaInicio.getSelectedDate()!=null)
									busquedas.add(new ValueMember("1 "+fmto.format(dccFechaInicio.getSelectedDate().getTime()),TipoNodo.FECHA_PUBLICACION));
								if(dccFechaFin==panel.getComponent(i+1) && dccFechaFin.getSelectedDate()!=null)
									busquedas.add(new ValueMember("2 "+fmto.format(dccFechaFin.getSelectedDate().getTime()),TipoNodo.FECHA_PUBLICACION));
							} //Si aparecen 2 TipoNodo.FECHA_PUBLICACION-->examinar entre dichas fechas; sino, desde ("1 fecha") o hasta ("2 fecha") la fecha indicada
						}
			}
			
			//obtengo los árboles resultados según los criterios de búsqueda
			List<ArbolNario> rtdos=obtenerArboles(arboles,busquedas);
			
			//para guardar los detalles de los árboles resultados
			detalles=new String[rtdos.size()];
			
			List<String> valores=new ArrayList<>();
			int i=0;
			for(ArbolNario arbol: rtdos)
			 {
				valores.add(arbol.getRaiz().getValor()); //TITULOS ENCONTRADOS, que serán mostrados en la tabla
				detalles[i]="ARBOL: "+arbol.toString();  //guardo los detalles de los árboles encontrados
			 }
			
			this.tableModel.setTitulos(valores);
			this.tableModel.fireTableDataChanged();
		});
		panel.add(btnBuscar);
		
		btnSalir=crearBoton(new JButton("Salir"));
		btnSalir.addActionListener( (e) ->{
			    principal.setTitle("Busquedas");
				principal.setContentPane(panelBusqueda);
			    principal.pack();
		});
		panel.add(btnSalir);
		
		//Desactivo todos los JComboBox
		for(int j=0;j<panel.getComponentCount();j++)
		{	
			Component c=panel.getComponent(j); 
			if(c instanceof JComboBox)
				c.setEnabled(false);
			if(c instanceof JCheckBox )
			{
				JCheckBox jcb=((JCheckBox)c);
				Component comp=panel.getComponent(j+1);
				jcb.addItemListener(e-> {
					if(jcb.isSelected())
						comp.setEnabled(true); //los activo cuando su respectivo JCheckBox esté marcado
					else {
							comp.setEnabled(false); if(comp instanceof DateChooserCombo)
													((DateChooserCombo)comp).setSelectedDate(null);
					}});
			}
		}
		this.add(panel);
		
		tabla = new JTable(this.tableModel);
		tabla.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tablaMouseMoved(evt);
            }
        });
		tabla.setFillsViewportHeight(true);
		//Centro en la tabla los titulos (única columna) mostrados como resultados
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		tabla.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		scrollPane= new JScrollPane(tabla);
		this.add(scrollPane);
	}
	
//-----------------------------------------------------------------------------------------------------------------------------
	
	private void agregarLinea(JCheckBox jcb, Object[] valores, JPanel panel) 
	{	
		if(valores.length>0)
		{
			panel.add(jcb);
			JComboBox cb=new JComboBox(valores);
			panel.add(cb);
		}
	}
//---------------------------------------------------------------------------------------------------------------------------------
	private List<ArbolNario> obtenerArboles(List<ArbolNario> arboles, List<ValueMember> busquedas) {
		
		List<ArbolNario> arbolesEncontrados=new ArrayList<>(arboles); //inicio con TODOS los árboles
		if(busquedas.size()>0) //voy filtrando árboles según criterios de búsqueda 
		{
			Set<ArbolNario> subArbolesEncontrados=new HashSet<>();
			//System.out.println("INICIO arbolesEncontrados="+arbolesEncontrados);
			
			for(int i=0;i<busquedas.size();i++)
			{
				ValueMember busqueda=busquedas.get(i);
				TipoNodo tipoBuscar=busqueda.getTipo();
				
				switch(tipoBuscar){
				
				case TITULO: 
					subArbolesEncontrados=buscarPalabraEn(new HashSet<>(arboles),busqueda.displayMember);
					//System.out.println("TITULO SUBarbolesEncontrados="+subArbolesEncontrados);
					break;
				case AUTOR: 
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.METADATO,new HashSet<>(arboles));//arbolesMETADATO (primer hijo del nodoTITULO)
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.AUTOR, subArbolesEncontrados);
					subArbolesEncontrados=buscarPalabraEn(subArbolesEncontrados,busqueda.displayMember);
					//System.out.println("AUTOR SUBarbolesEncontrados="+subArbolesEncontrados);
					break;
				case EDITORIAL: 
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.METADATO,new HashSet<>(arboles));//arbolesMETADATO (primer hijo del nodoTITULO)
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.EDITORIAL,subArbolesEncontrados);
					subArbolesEncontrados=buscarPalabraEn(subArbolesEncontrados,busqueda.displayMember);
					//System.out.println("EDITORIAL SUBarbolesEncontrados="+subArbolesEncontrados);
					break;
				case FECHA_PUBLICACION: //TODO
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.METADATO,new HashSet<>(arboles));//arbolesMETADATO (primer hijo del nodoTITULO)
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.FECHA_PUBLICACION,subArbolesEncontrados);
					if(i+1<busquedas.size() && busquedas.get(i+1).getTipo().equals(TipoNodo.FECHA_PUBLICACION)) //hay dos fechas consecutivas
						{
							subArbolesEncontrados=buscarEntreFechas(subArbolesEncontrados,
									busqueda.displayMember.substring(2), //desde (le saco el "1 ")
									busquedas.get(i+1).displayMember.substring(2)); //hasta (le saco el "2 ")
							i++;
						} 
					else if(busqueda.displayMember.contains("1 ")) //desde
						subArbolesEncontrados=buscarEntreFechas(subArbolesEncontrados,
								busqueda.displayMember.substring(2), //desde
								"9999-99-99"); //hasta +infinito
					else //hasta 
						subArbolesEncontrados=buscarEntreFechas(subArbolesEncontrados,
							"1111-11-11", //desde -infinito
							busqueda.displayMember.substring(2)); //hasta
					//System.out.println("FECHA SUBarbolesEncontrados="+subArbolesEncontrados);
					break;
				case RESUMEN:
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.RESUMEN,new HashSet<>(arboles));//arbolesRESUMEN (primer hijo del nodoTITULO)
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.PARRAFO, subArbolesEncontrados);
					subArbolesEncontrados=buscarPalabraEn(subArbolesEncontrados,busqueda.displayMember);
					//System.out.println("RESUMEN SUBarbolesEncontrados="+subArbolesEncontrados);
					break;
				case CAPITULO:
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.CAPITULO,new HashSet<>(arboles)); 
					subArbolesEncontrados=buscarPalabraEn(subArbolesEncontrados,busqueda.displayMember);
					//System.out.println("CAPITULO SUBarbolesEncontrados="+subArbolesEncontrados);
					break;
				case SECCION:
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.CAPITULO,new HashSet<>(arboles));
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.SECCION,subArbolesEncontrados);
					subArbolesEncontrados=buscarPalabraEn(subArbolesEncontrados,busqueda.displayMember);
					//System.out.println("SECCION SUBarbolesEncontrados="+subArbolesEncontrados);
					break;
				case PARRAFO:
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.CAPITULO,new HashSet<>(arboles));
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.SECCION,subArbolesEncontrados);
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.PARRAFO,subArbolesEncontrados);
					subArbolesEncontrados=buscarPalabraEn(subArbolesEncontrados,busqueda.displayMember);
					break;
				case SITIO_WEB:
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.CAPITULO,new HashSet<>(arboles));
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.METADATO,subArbolesEncontrados);
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.SITIO_WEB,subArbolesEncontrados);
					subArbolesEncontrados=buscarPalabraEn(subArbolesEncontrados,busqueda.displayMember);
					break;
				case SITIO_WEB_EJERCICIO:
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.CAPITULO,new HashSet<>(arboles));
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.METADATO,subArbolesEncontrados);
					subArbolesEncontrados=obtenerHijosTipo(TipoNodo.SITIO_WEB_EJERCICIO,subArbolesEncontrados);
					subArbolesEncontrados=buscarPalabraEn(subArbolesEncontrados,busqueda.displayMember);
				break;
				default:
					break;
				}
				
				if(arbolesEncontrados.size()>0)
				{
					Nodo buscado=null;
					Iterator<ArbolNario> it=subArbolesEncontrados.iterator();
					if(it.hasNext()) buscado=it.next().getRaiz(); //me quedo con el nodo raiz (y sus hijos, si existen) del subArbol
				
					//System.out.println("BUSCADO="+buscado);
				
					if(buscado!=null)
					{	
						Iterator<ArbolNario> itArbol=arbolesEncontrados.iterator();
						while(itArbol.hasNext())
						{
							ArbolNario actual=itArbol.next();
							//System.out.println("analizo el arbol: "+actual);
							if(!actual.tieneNodo(buscado))
								itArbol.remove(); //remuevo el arbol que no tiene dicho nodo raiz
						}
						//System.out.println("resultantes con buscado="+buscado+": "+arbolesEncontrados);
					}else arbolesEncontrados=new ArrayList<>();
				}
			}//fin FOR(busquedas)
			
			//System.out.println("FINAL arbolesEncontrados: "+arbolesEncontrados);
		}
		return arbolesEncontrados;
	}
//-----------------------------------------------------------------------------------------------------------------------------
	private Set<ArbolNario> buscarPalabraEn(Set<ArbolNario> arboles, String palabraBuscada) {
		Set<ArbolNario> ret=new LinkedHashSet<>();
		for(ArbolNario arbol: arboles)
			if(arbol.getRaiz().getValor().contains(palabraBuscada))
				ret.add(arbol);
		return ret;
	}

	private Set<ArbolNario> buscarEntreFechas(Set<ArbolNario> arboles, String desde,String hasta) {
		Set<ArbolNario> ret=new LinkedHashSet<>();
		for(ArbolNario arbol: arboles)
		{
			Stack<String> pila=new Stack<>();
			for(String s: arbol.getRaiz().getValor().split("-"))
				pila.push(s);
			String fecha=pila.pop()+"-"+pila.pop()+"-"+pila.pop(); //yyyy-MM-dd !!!
			/*System.out.println("fechaInvertida="+fecha+" desde="+desde+" hasta="+hasta); 
			boolean b1=fecha.compareTo(desde)>=0;
			boolean b2=fecha.compareTo(hasta)<=0;
			System.out.println("fecha.compareTo(desde)>=0:"+b1+" && fecha.compareTo(hasta)<=0:"+b2);*/
			if(fecha.compareTo(desde)>=0 && fecha.compareTo(hasta)<=0)
				ret.add(arbol);
		}
		return ret;
	}

	private Set<ValueMember> obtenerPalabrasValoresHijos(TipoNodo tipo, Set<ArbolNario> arboles,TipoNodo tipoBuscar) {
		Set<ValueMember> palabras=new LinkedHashSet<>();
		for(ArbolNario arbol: arboles)
			if(arbol.getRaiz().tieneHijos())
				for(ArbolNario hijo: arbol.getRaiz().getHijos())
					if(hijo.getRaiz().getTipo().equals(tipo))
						for(String palabra: hijo.getRaiz().getValor().split("\\s+"))
							//if(!palabras.stream().anyMatch(valor->valor.displayMember.compareTo(palabra)==0))
								palabras.add(new ValueMember(palabra,tipoBuscar));
		return palabras;
	}

	private Set<ArbolNario> obtenerHijosTipo(TipoNodo tipo, Set<ArbolNario> arboles) {
		Set<ArbolNario> hijos=new LinkedHashSet<>();
		for(ArbolNario arbol: arboles)
			if(arbol.getRaiz().tieneHijos())
				for(ArbolNario hijo: arbol.getRaiz().getHijos())
					if(hijo.getRaiz().getTipo().equals(tipo))
						hijos.add(hijo);
		return hijos;
	}

	private Set<ValueMember> obtenerValoresHijos(TipoNodo tipo, Set<ArbolNario> arboles,TipoNodo tipoBuscar) {
		Set<ValueMember> valores=new LinkedHashSet<>();
		for(ArbolNario arbol: arboles)
			if(arbol.getRaiz().tieneHijos())
				for(ArbolNario hijo: arbol.getRaiz().getHijos())
					if(hijo.getRaiz().getTipo().equals(tipo))
						valores.add(new ValueMember(hijo.getRaiz().getValor(),tipoBuscar));
		return valores;
	}

	protected Set<ValueMember> obtenerValoresRaices(Set<ArbolNario> arboles, TipoNodo tipoBuscar)
	{
		Set<ValueMember> valores=new LinkedHashSet<>();
		for(ArbolNario arbol: arboles)
				valores.add(new ValueMember(arbol.getRaiz().getValor(),tipoBuscar));
		return valores;
	}
	
	//---------------------------------------------------------------------------------------------------------------------
		private void FechaInicioActionPerformed(SelectionChangedEvent sce) {
			if(dccFechaFin.getSelectedDate()!=null && dccFechaFin.isEnabled() && 
					dccFechaInicio.getSelectedDate().compareTo(dccFechaFin.getSelectedDate())>0)
			{
				JOptionPane.showMessageDialog(null, "Debe seleccionar una Fecha de Inicio menor o igual a Fecha de Fin");
				dccFechaInicio.setSelectedDate(dccFechaFin.getSelectedDate());
			}
		}

		private void FechaFinActionPerformed(SelectionChangedEvent sce) {
			
			if(dccFechaInicio.getSelectedDate()!=null && dccFechaFin.getSelectedDate()!=null
					&& dccFechaInicio.isEnabled())
			if(dccFechaInicio.getSelectedDate().compareTo(dccFechaFin.getSelectedDate())>0)
				{
					JOptionPane.showMessageDialog(null, "Debe seleccionar una Fecha de Fin mayor o igual a Fecha de Inicio");
					dccFechaFin.setSelectedDate(dccFechaInicio.getSelectedDate());
				}
		}
		
	    private void tablaMouseMoved(java.awt.event.MouseEvent evt) {                                 
	    	try{
	    	    int i = tabla.rowAtPoint(evt.getPoint());
	    	tabla.setToolTipText(detalles[i]); 
	    	}catch(Exception ex){}
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
