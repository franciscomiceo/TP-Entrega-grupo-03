package frsf.isi.died.app.vista.material;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import datechooser.beans.DateChooserCombo;
import excepcion.Validar;
import frsf.isi.died.app.controller.VideoController;

public class VideoModificar extends JDialog {

	public JTextField txtTitulo,txtCosto,txtDuracion,txtCalificacion,txtTema;
	private JButton btnAceptar,btnCancelar;
	public DateChooserCombo dccFechaPublicacion;

	private VideoController controller;
	public int id_video;
	
	public VideoModificar(VideoController controller,java.awt.Frame principal, boolean modal)
    {
        super(principal, modal);
        this.controller=controller;
        construir();
    }
	
	public void construir() {
		this.setTitle("Modificar Video");
		this.setLayout(new GridLayout(0, 2));
		
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yy");
			
		txtTitulo = new JTextField();
		txtCosto = new JTextField();
		txtDuracion = new JTextField();
		txtCalificacion=new JTextField();
		txtTema=new JTextField();
		
		btnAceptar =crearBoton(new JButton("Aceptar"));
		btnAceptar.addActionListener( e ->{
			try {
				validarDatosyNulidad(txtTitulo.getText().trim(),txtCosto.getText().trim(),txtDuracion.getText().trim(),dccFechaPublicacion.getSelectedDate(),txtCalificacion.getText().trim(),txtTema.getText().trim());
				
				String titulo=txtTitulo.getText().trim();
				Double costo = Double.valueOf(txtCosto.getText().trim());
				Integer duracion = Integer.valueOf(txtDuracion.getText().trim());
				Date fecha=this.dccFechaPublicacion.getSelectedDate().getTime();
				Integer calificacion=Integer.parseInt(txtCalificacion.getText().trim());
				String tema=txtTema.getText().trim();
						
				controller.ModificarVideo(id_video,titulo, costo, duracion,fecha,calificacion,tema);
				dispose();
			}catch(Exception ex) {JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos incorrectos", JOptionPane.ERROR_MESSAGE);}});
		
		btnCancelar=crearBoton(new JButton("Cancelar"));
		btnCancelar.addActionListener(e->dispose());
		
		Dimension dim=new Dimension(350,200);
		dccFechaPublicacion = new DateChooserCombo();
		dccFechaPublicacion.setDateFormat(fmt);
		dccFechaPublicacion.setCalendarPreferredSize(dim);
		Calendar minInicio = Calendar.getInstance();
		minInicio.set(Calendar.YEAR, 2018);
		minInicio.set(Calendar.MONTH, 6);
		minInicio.set(Calendar.DAY_OF_MONTH, 4);
		dccFechaPublicacion.setMinDate(minInicio);
		dccFechaPublicacion.setSelectedDate(null);
	
		agregarLinea(new JLabel("Titulo: "),txtTitulo);
		agregarLinea(new JLabel("Costo: "),txtCosto);
		agregarLinea(new JLabel("Duracion (segs): "),txtDuracion);
		agregarLinea(new JLabel("Fecha Publicacion: "),dccFechaPublicacion);
		agregarLinea(new JLabel("Calificacion: "),txtCalificacion);
		agregarLinea(new JLabel("Tema: "),txtTema);
		agregarLinea(btnAceptar,btnCancelar);
	}

	private void validarDatosyNulidad(String titulo, String costo, String DuracionEnSegs,Calendar fecha, String calificacion, String tema) throws Exception{
		String campo="Titulo: ";
		Validar.isLetrasNumeros(campo,titulo, 2, 20, true);
		campo="Costo: ";
		Validar.isNumeros(campo,costo, 1, 7,false); //debe ser DOUBLE!
		campo="Duracion en segs: ";
		Validar.isNumeros(campo,DuracionEnSegs, 1, 7,true); //Integer
		if(fecha==null) throw new Exception(campo+"Debe seleccionar una fecha de publicacion");
		campo="Calificacion: ";
		Validar.isNumeros(campo,calificacion, 1, 3,true); //Integer
		campo="Tema: ";
		Validar.isLetrasNumeros(campo,tema, 2, 20, true);
	}
	
//---------------------------------------------------------------------------------------------------------------------
	private void agregarLinea(Component c1, Component c2) {
		if(c2 instanceof JTextField)
		c2.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {aMayuscula(evt);}});
		this.add(c1); this.add(c2);	
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

    private void aMayuscula(java.awt.event.KeyEvent evt){
        char c=evt.getKeyChar();
        if(Character.isLowerCase(c)){
            String cad=(""+c).toUpperCase();
            c=cad.charAt((0));
            evt.setKeyChar(c);
        }
    }
  //---------------------------------------------------------------------------------------------------------------------
    public VideoController getController() {
		return controller;
	}

	public void setController(VideoController controller) {
		this.controller = controller;
	}

}
