package frsf.isi.died.app.vista.material;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import frsf.isi.died.tp.modelo.productos.Libro;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;

public class LibroTableModel  extends AbstractTableModel {

	private List<Libro> libros;
	private String[] columnas = {"ID","Titulo","Precio Compra","Costo publicacion","Paginas","Fecha Publicacion","Calificacion","Tema","Relevancia","Precio Suscripcion"};
	
	public LibroTableModel()
	{
		libros=new ArrayList<>();
	}
	
	public Libro getFila(int i)
	{
		return libros.get(i);
	}
	
	@Override
	public String getColumnName(int indice) {
		return this.columnas[indice];
	}
	
	public List<Libro> getLibros() {
		return libros;
	}

	public void setLibros(List<Libro> libros) {
		libros.sort((Libro l1, Libro l2)->l1.getTitulo().compareTo(l2.getTitulo()));
		this.libros = libros;
	}

	@Override
	public int getRowCount() {
		return libros.size();
	}

	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object valor = null;
		switch (columnIndex) {
		case 0:
			valor = this.libros.get(rowIndex).getId();
			break;
		case 1:
			valor = this.libros.get(rowIndex).getTitulo();
			break;
		case 2:
			valor = this.libros.get(rowIndex).getPrecioCompra();
			break;
		case 3:
			valor = this.libros.get(rowIndex).getCosto();
			break;
		case 4:
			valor = this.libros.get(rowIndex).getPaginas();
			break;
		case 5:
			SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
			valor = fmt.format(this.libros.get(rowIndex).getFecha_publicacion());
			break;
		case 6:
			valor = this.libros.get(rowIndex).getCalificacion();
			break;
		case 7:
			valor = this.libros.get(rowIndex).getTema();
			break;
		case 8:
			valor = this.libros.get(rowIndex).getRelevancia();
			break;
		case 9:
			valor = this.libros.get(rowIndex).precio();
			break;
		default:
			System.out.println("Indice fuera de rango");
			valor = "S/D";
			break;
		}
		return valor;
	}

}
