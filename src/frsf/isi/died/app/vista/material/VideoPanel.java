package frsf.isi.died.app.vista.material;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import datechooser.beans.DateChooserCombo;
import excepcion.Validar;
import frsf.isi.died.app.controller.VideoController;
import frsf.isi.died.app.dao.MaterialConexion;
import frsf.isi.died.tp.modelo.productos.Video;

public class VideoPanel extends JPanel {

	private JScrollPane scrollPane;
	private JTable tabla;
	
	private JTextField txtTitulo,txtCosto,txtDuracionEnSegs,txtCalificacion,txtTema;
	
	private JButton btnAgregar,btnModificar,btnEliminar,btnBuscar,btnReset;
	
	private DateChooserCombo dccFechaPublicacion;

	private VideoTableModel tableModel;
	private VideoController controller;
	private JFrame principal;
	
	public VideoPanel(JFrame principal) {
		this.setPrincipal(principal);
		tableModel = new VideoTableModel();
	}
	
	public void construir() {
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yy");
		this.setLayout(new GridLayout(2, 1));
		
		//creo los campos a llenar
		txtTitulo = new JTextField(); 
		txtCosto = new JTextField();
		txtDuracionEnSegs = new JTextField();
		txtCalificacion= new JTextField();
		txtTema= new JTextField();
		
		
		//creo los botones con su funcion
		btnAgregar = crearBoton(new JButton("Agregar"));
		btnAgregar.addActionListener( e ->{
				try {
					validarDatosyNulidad(txtTitulo.getText().trim(),txtCosto.getText().trim(),txtDuracionEnSegs.getText().trim(),dccFechaPublicacion.getSelectedDate(),txtCalificacion.getText().trim(),txtTema.getText().trim());
					
					String titulo=txtTitulo.getText().trim();
					Double costo = Double.valueOf(txtCosto.getText().trim());
					Integer duracion = Integer.valueOf(txtDuracionEnSegs.getText().trim());
					Date fecha=this.dccFechaPublicacion.getSelectedDate().getTime();
					Integer calificacion=Integer.parseInt(txtCalificacion.getText().trim());
					String tema=txtTema.getText().trim();
					
					controller.agregarVideo(titulo, costo, duracion,fecha,calificacion,tema);
					limpiar();
				}catch(Exception ex) {
				    JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
				}
		});
		
		btnModificar=crearBoton(new JButton("Modificar"));
		btnModificar.addActionListener(e -> {
			if(tabla.getSelectedRowCount()==1)
		    {
				VideoModificar vm=new VideoModificar(controller,principal,true);
				vm.id_video=Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 0).toString());
				vm.txtTitulo.setText(tabla.getValueAt(tabla.getSelectedRow(), 1).toString());
				vm.txtCosto.setText(tabla.getValueAt(tabla.getSelectedRow(), 2).toString());
				vm.txtDuracion.setText(tabla.getValueAt(tabla.getSelectedRow(), 3).toString());
				Calendar cal=Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				try {
				cal.setTime(format.parse(tabla.getValueAt(tabla.getSelectedRow(), 4).toString()));
				}catch(Exception ex) {JOptionPane.showMessageDialog(null, "No se pudo obtener la fecha de publicacion");}
				vm.dccFechaPublicacion.setSelectedDate(cal);
				vm.txtCalificacion.setText(tabla.getValueAt(tabla.getSelectedRow(), 5).toString());
				vm.txtTema.setText(tabla.getValueAt(tabla.getSelectedRow(), 6).toString());
				vm.pack();
				vm.setLocationRelativeTo(null);
				vm.setPreferredSize(new Dimension(500,400));// hardCoded sizing
				vm.setMaximumSize(new Dimension(500, 300));  // hardCoded sizing
				vm.setMinimumSize(new Dimension(300, 300));  // hardCoded sizing
				vm.setVisible(true);
		    }
			else JOptionPane.showMessageDialog(null, "Debe Seleccionar solo 1 video");
		});
		
		btnEliminar= crearBoton(new JButton("Eliminar"));
		btnEliminar.addActionListener(e->{
			if(tabla.getSelectedRowCount()==1)
		    {
				VideoEliminar ve=new VideoEliminar(controller,principal,true,tabla.getValueAt(tabla.getSelectedRow(), 1).toString());
				ve.id_video=Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 0).toString());
				ve.pack();
				ve.setVisible(true);
		    }
			else JOptionPane.showMessageDialog(null, "Debe Seleccionar solo 1 video");
			
		});
		
		//TODO probar buscar!!!
		btnBuscar = crearBoton(new JButton("Buscar"));
		btnBuscar.addActionListener( e ->{
			try {
				setListaVideos(MaterialConexion.getVideos(),true);
				validarDatosyNulidadBuscar(txtTitulo.getText().trim(),txtCosto.getText().trim(),txtDuracionEnSegs.getText().trim(),dccFechaPublicacion.getSelectedDate(),txtCalificacion.getText().trim(),txtTema.getText().trim());
				
				String titulo=txtTitulo.getText().trim();
				Double costo = txtCosto.getText().trim().length()>0?Double.valueOf(txtCosto.getText().trim()):null;
				Integer duracion = txtDuracionEnSegs.getText().trim().length()>0?Integer.valueOf(txtDuracionEnSegs.getText().trim()):null;
				Date fecha=this.dccFechaPublicacion.getSelectedDate()!=null?this.dccFechaPublicacion.getSelectedDate().getTime():null;
				Integer calificacion = txtCalificacion.getText().trim().length()>0?Integer.valueOf(txtCalificacion.getText().trim()):null;
				String tema=txtTema.getText().trim();
				
				List<Integer> filas=new ArrayList<>();
				filas=buscarVideos(titulo, costo, duracion,fecha,calificacion,tema);
				
				//System.out.println("filas que quedan de la tabla: "+filas);
				
				if(filas.size()>0)
					{
						if(filas.size()!=this.tableModel.getRowCount())
						{
							List<Video> videos=this.tableModel.getVideos();
							for(int i=this.tableModel.getRowCount()-1;i>=0;i--)
								if(!filas.contains(i))
									videos.remove(i);
							this.tableModel.setVideos(videos);
							this.tableModel.fireTableDataChanged();
						}
					}
				else 
					{
						JOptionPane.showMessageDialog(null, "No se han encontrado resultados para la busqueda realizada");
						this.tableModel.setVideos(new ArrayList<>()); 
						this.tableModel.fireTableDataChanged();
					}

				limpiar();
				
			}catch(Exception ex) {
			    JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
			}
		});

		
		btnReset = crearBoton(new JButton("Reset"));
		btnReset.addActionListener( e ->{
			try {
				setListaVideos(MaterialConexion.getVideos(),true);
				limpiar();
			}catch(Exception ex) {JOptionPane.showMessageDialog(null,"No se pudieron recuperar todos los videos");}
		});

		
		//crear campo fecha
		Dimension dim=new Dimension(350,200);
		dccFechaPublicacion = new DateChooserCombo();
		dccFechaPublicacion.setDateFormat(fmt);
		dccFechaPublicacion.setCalendarPreferredSize(dim);
		Calendar minInicio = Calendar.getInstance();
		minInicio.set(Calendar.YEAR, 1800);
		minInicio.set(Calendar.MONTH, 0);
		minInicio.set(Calendar.DAY_OF_MONTH, 0);
		dccFechaPublicacion.setMinDate(minInicio);
		dccFechaPublicacion.setSelectedDate(null); 
		
		tabla = new JTable(this.tableModel);
		tabla.setFillsViewportHeight(true);
		tabla.getColumnModel().getColumn(0).setMaxWidth(0); //Para ocultar la columna "ID"
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
		scrollPane= new JScrollPane(tabla);
		
		//agrego JLabel, campos a llenar a un panel, que será agregado primero al panelVideos
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(6,3));
		agregarLinea(new JLabel("Titulo: "),txtTitulo,btnAgregar,panel);
		agregarLinea(new JLabel("Costo: "),txtCosto,btnModificar,panel);
		agregarLinea(new JLabel("Duracion (segs): "),txtDuracionEnSegs,btnEliminar,panel);
		agregarLinea(new JLabel("Fecha Publicacion: "),dccFechaPublicacion,btnBuscar,panel);
		agregarLinea(new JLabel("Calificacion: "),txtCalificacion,btnReset,panel);
		agregarLinea(new JLabel("Tema: "),txtTema,new JLabel(),panel);
		
		this.add(panel);

		//agrego la tabla por último al panelVideos
		this.add(scrollPane);
	}
//------------------------------------------------------------------------------------------------------------------------
	private List<Integer> buscarVideos(String titulo, Double costo, Integer duracion, Date fecha,Integer calificacion,String tema) {
		List<Integer> filas=new ArrayList<>();
		List<Integer> remover=new ArrayList<>();
		
		for(int i=0;i<tabla.getRowCount();i++)
		{
			if(titulo.length()>0)
				if(titulo.compareTo(tabla.getValueAt(i,1).toString())==0)
					filas.add(i);
				else if(!remover.contains(i))remover.add(i);
			if(costo!=null)
				if(costo.equals(Double.parseDouble(tabla.getValueAt(i,2).toString())))
					{if(!filas.contains(i))
						filas.add(i);}
				else if(!remover.contains(i))remover.add(i);
			if(duracion!=null)
				if(duracion.equals(Integer.parseInt(tabla.getValueAt(i,3).toString())))
					{if(!filas.contains(i))
						filas.add(i);}
				else if(!remover.contains(i))remover.add(i);	
			if(fecha!=null)
			{
				SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
				//System.out.println("fechas iguales: "+fmt.format(fecha).equals(tabla.getValueAt(i,4).toString()));
				if(fmt.format(fecha).equals(tabla.getValueAt(i,4).toString()))
					{if(!filas.contains(i))
						filas.add(i);}
				else if(!remover.contains(i))remover.add(i);
			}
			if(calificacion!=null)
				if(calificacion.equals(Integer.parseInt(tabla.getValueAt(i,5).toString())))
					{if(!filas.contains(i))
						filas.add(i);}
				else if(!remover.contains(i))remover.add(i);
			if(tema.length()>0)
				if(tema.equals(tabla.getValueAt(i,6).toString()))
					{if(!filas.contains(i))
						filas.add(i);}
				else if(!remover.contains(i))remover.add(i);
		}		
		
		filas.removeAll(remover);
		
		//System.out.println("filas resultados: "+filas);
		return filas;
	}

	private void validarDatosyNulidad(String titulo, String costo, String DuracionEnSegs,Calendar fecha, String calificacion, String tema) throws Exception{
		String campo="Titulo: ";
		Validar.isLetrasNumeros(campo,titulo, 2, 20, true);
		campo="Costo: ";
		Validar.isNumeros(campo,costo, 1, 7,false); //DOUBLE
		campo="Duracion en segs: ";
		Validar.isNumeros(campo,DuracionEnSegs, 1, 7,true); //INTEGER
		if(fecha==null) throw new Exception(campo+"Debe seleccionar una fecha de publicacion");
		campo="Calificacion: ";
		Validar.isNumeros(campo,calificacion, 1, 3,true); //Integer
		if(Integer.parseInt(calificacion)<0||Integer.parseInt(calificacion)>100)
			throw new Exception(campo+"Debe ingresar un número entero entre 0 y 100");
		campo="Tema: ";
		Validar.isLetrasNumeros(campo,tema, 2, 20, true);
	}
	
	private void validarDatosyNulidadBuscar(String titulo, String costo, String duracion, Calendar fecha,String calificacion,String tema) throws Exception{
		 if(!(titulo.length()>0||costo.length()>0||duracion.length()>0||fecha!=null||calificacion.length()>0||tema.length()>0))
           throw new Exception("Debe llenar al menos 1 criterio de busqueda");
        
		try{
           if(titulo.length()>0)
           	Validar.isLetrasNumeros("Titulo: ",titulo, 2, 20, true);
       }catch(Exception ex){throw ex;
       }finally
       {
       	try{
       		if(costo.length()>0)
       			Validar.isNumeros("Costo: ",costo, 1, 7,false);
       	}catch(Exception ex){throw ex;
       	}finally
       	{
       		try{
           		if(duracion.length()>0)
           			Validar.isNumeros("Precio: ",duracion, 1, 7,false);
           	}catch(Exception ex){throw ex;
           	}finally
            {
            	try{
            		if(calificacion.length()>0)
            			{Validar.isNumeros("Calificacion: ",calificacion, 1, 3,true);
            			if(Integer.parseInt(calificacion)<0||Integer.parseInt(calificacion)>100)
            				throw new Exception("Calificacion: Debe ingresar un número entero entre 0 y 100");
            			}
            	}catch(Exception ex){throw ex;
            	}finally
                {
                	try{
                		if(tema.length()>0)
                			Validar.isLetrasNumeros("Tema: ",tema, 2, 20,false);
                	}catch(Exception ex){throw ex;}}}}
            	}
	}
	//---------------------------------------------------------------------------------------------------------------------
    
		protected void agregarLinea(JLabel lbl,Component c1,Component c2,JPanel panel)
		{
			if(c2 instanceof JButton)
			c2.addKeyListener(new java.awt.event.KeyListener() {

	            @Override
	            public void keyTyped(KeyEvent e) {
	            }

	            @Override
	            public void keyPressed(KeyEvent e) {
	     if(e.getKeyCode() == 10)
	            ((JButton)c2).doClick();
	       }

	            @Override
	            public void keyReleased(KeyEvent e) {
	            }
	        });
			
			if(c1 instanceof JTextField)
				c1.addKeyListener(new java.awt.event.KeyAdapter() {
		            @Override
		            public void keyTyped(java.awt.event.KeyEvent evt) {
		                aMayuscula(evt);
		            }
		        });
			
			panel.add(lbl);
			panel.add(c1);
			panel.add(c2);
		}
		
	//agregar el KeyListener al boton
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

    private void limpiar() {
		txtTitulo.setText("");
		txtCosto.setText("");
		txtDuracionEnSegs.setText("");
		dccFechaPublicacion.setSelectedDate(null);
		txtCalificacion.setText("");
		txtTema.setText("");
	}

	public void setListaVideos(List<Video> videosLista,boolean actualizar) {
		this.tableModel.setVideos(videosLista);
		if(actualizar) this.tableModel.fireTableDataChanged();
	}

	private void aMayuscula(java.awt.event.KeyEvent evt){
        char c=evt.getKeyChar();
        if(Character.isLowerCase(c)){
            String cad=(""+c).toUpperCase();
            c=cad.charAt((0));
            evt.setKeyChar(c);
        }
    }
    
	public VideoController getController() {
		return controller;
	}

	public void setController(VideoController controller) {
		this.controller = controller;
	}
	
	public JFrame getPrincipal() {
		return principal;
	}

	public void setPrincipal(JFrame principal) {
		this.principal = principal;
	}
}
