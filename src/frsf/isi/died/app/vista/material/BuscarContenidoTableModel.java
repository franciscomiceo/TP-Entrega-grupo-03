package frsf.isi.died.app.vista.material;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class BuscarContenidoTableModel extends AbstractTableModel {

	List<String> titulos;
	private String[] columnas = {"Titulo"};
	
	public BuscarContenidoTableModel()
	{
		this.titulos=new ArrayList<>();
	}
	
	@Override
	public String getColumnName(int indice) {
		return this.columnas[indice];
	}
	
	@Override
	public int getRowCount() {
		return titulos.size();
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
			valor = this.titulos.get(rowIndex);
			break;
		default:
			System.out.println("Indice fuera de rango");
			valor = "S/D";
			break;
		}
		return valor;
	}

	public List<String> getTitulos() {
		return titulos;
	}

	public void setTitulos(List<String> titulos) {
		this.titulos = titulos;
	}

	
}
