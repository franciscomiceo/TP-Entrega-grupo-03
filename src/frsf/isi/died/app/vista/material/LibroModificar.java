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
import frsf.isi.died.app.controller.LibroController;

public class LibroModificar extends JDialog {
	
	public JTextField txtTitulo,txtCosto,txtPrecioCompra,txtPaginas,txtCalificacion,txtTema;
	private JButton btnAceptar,btnCancelar;
	public DateChooserCombo dccFechaPublicacion;

	private LibroController controller;
	public int id_libro;
	
	public LibroModificar(LibroController controller,java.awt.Frame principal, boolean modal)
    {
        super(principal, modal);
        this.controller=controller;
        construir();
    }
	
	public void construir() {
		this.setTitle("Modificar Libro");
		this.setLayout(new GridLayout(0, 2));
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yy");
				
		txtTitulo = new JTextField();
		txtCosto = new JTextField();
		txtPrecioCompra = new JTextField();
		txtPaginas = new JTextField();
		txtCalificacion= new JTextField();
		txtTema= new JTextField();
		
		btnAceptar =crearBoton(new JButton("Aceptar"));
		btnAceptar.addActionListener( e ->{
			try {
				validarDatosyNulidad(txtTitulo.getText().trim(),txtCosto.getText().trim(),txtPrecioCompra.getText().trim(),txtPaginas.getText().trim(),dccFechaPublicacion.getSelectedDate(),txtCalificacion.getText().trim(),txtTema.getText().trim());
				
				String titulo=txtTitulo.getText().trim();
				Double costo = Double.valueOf(txtCosto.getText().trim());
				Double precio = Double.valueOf(txtPrecioCompra.getText().trim());
				Integer paginas = Integer.valueOf(txtPaginas.getText().trim());
				Date fecha=this.dccFechaPublicacion.getSelectedDate().getTime();
				Integer calificacion=Integer.valueOf(txtCalificacion.getText().trim());
				String tema=txtTema.getText().trim();
				
				controller.ModificarLibro(id_libro,titulo, costo, precio, paginas,fecha,calificacion,tema);
			}catch(Exception ex) {JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos incorrectos", JOptionPane.ERROR_MESSAGE);}
			dispose();
		});		
		
		btnCancelar=crearBoton(new JButton("Cancelar"));
		btnCancelar.addActionListener(e->dispose());
				
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
		
		agregarLinea(new JLabel("Titulo: "),txtTitulo);
		agregarLinea(new JLabel("Costo: "),txtCosto);
		agregarLinea(new JLabel("Precio Compra: "),txtPrecioCompra);
		agregarLinea(new JLabel("Paginas: "),txtPaginas);
		agregarLinea(new JLabel("Fecha Publicacion: "),dccFechaPublicacion);
		agregarLinea(new JLabel("Calificacion: "),txtCalificacion);
		agregarLinea(new JLabel("Tema: "),txtTema);
		agregarLinea(btnAceptar,btnCancelar);
	}

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

	private void validarDatosyNulidad(String titulo, String costo, String precio, String paginas,Calendar fecha,String calificacion,String tema) throws Exception{
		String campo="Titulo: ";
		Validar.isLetrasNumeros(campo,titulo, 2, 20, true);
		campo="Costo: ";
		Validar.isNumeros(campo,costo, 1, 7,false); //debe ser DOUBLE!
		campo="Precio: ";
		Validar.isNumeros(campo,precio, 1, 7,false); //debe ser DOUBLE!
		campo="Paginas: ";
		Validar.isNumeros(campo,paginas, 1, 7,true);//está bien (Integer)
		campo="Fecha Publicacion: ";
		if(fecha==null) throw new Exception(campo+"Debe seleccionar una fecha de publicacion");
		campo="Calificacion: ";
		Validar.isNumeros(campo,calificacion, 1, 3,true); //Integer
		campo="Tema: ";
		Validar.isLetrasNumeros(campo,titulo, 2, 20, true);
	}
	
	//---------------------------------------------------------------------------------------------------------------------
	public LibroController getController() {
		return controller;
	}

	public void setController(LibroController controller) {
		this.controller = controller;
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
    

}
