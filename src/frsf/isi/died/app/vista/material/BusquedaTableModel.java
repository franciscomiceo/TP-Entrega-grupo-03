package frsf.isi.died.app.vista.material;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import frsf.isi.died.tp.modelo.productos.Libro;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;
import frsf.isi.died.tp.modelo.productos.Video;


public class BusquedaTableModel extends AbstractTableModel {

	private List<MaterialCapacitacion> materiales;
	private String[] columnas = {"ID","Titulo","Precio Compra","Costo publicacion","Paginas","Duracion en segs","Fecha Publicacion","Calificacion","Tema","Relevancia","Precio Suscripcion"};
	
	public BusquedaTableModel()
	{
		this.materiales=new ArrayList<>();
	}
	
	public List<MaterialCapacitacion> getMateriales() {
		return materiales;
	}

	public void setMateriales(List<MaterialCapacitacion> materiales) {
		this.materiales = materiales;
	}
	
	public void add(MaterialCapacitacion mc)
	{
		this.materiales.add(mc);
	}
	
	@Override
	public String getColumnName(int indice) {
		return this.columnas[indice];
	}
	
	@Override
	public int getRowCount() {
		return materiales.size();
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
			valor = this.materiales.get(rowIndex).getId();
			break;
		case 1:
			valor = this.materiales.get(rowIndex).getTitulo();
			break;
		case 2:
			if(this.materiales.get(rowIndex).esLibro())
			valor = ((Libro)this.materiales.get(rowIndex)).getPrecioCompra();
			else valor="-";
			break;
		case 3:
			valor = this.materiales.get(rowIndex).getCosto();
			break;
		case 4:
			if(this.materiales.get(rowIndex).esLibro())
			valor = ((Libro)this.materiales.get(rowIndex)).getPaginas();
			else valor="-";
			break;
		case 5:
			if(this.materiales.get(rowIndex).esVideo())
			valor = ((Video)this.materiales.get(rowIndex)).getDuracionEnSegundos();
			else valor="-";
			break;
		case 6:
			SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
			valor = fmt.format(this.materiales.get(rowIndex).getFecha_publicacion());
			break;
		case 7:
			valor = this.materiales.get(rowIndex).getCalificacion();
			break;
		case 8:
			valor = this.materiales.get(rowIndex).getTema();
			break;
		case 9:
			valor = this.materiales.get(rowIndex).getRelevancia();
			break;
		case 10:
			valor = this.materiales.get(rowIndex).precio();
			break;
		default:
			System.out.println("Indice fuera de rango");
			valor = "S/D";
			break;
		}
		return valor;

	}

	public void quitar(int id_material) {
		this.materiales.removeIf(mc->mc.getId()==id_material);
	}

}
