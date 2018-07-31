package frsf.isi.died.app.vista.material;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import frsf.isi.died.app.vista.grafo.VerticeView;
import frsf.isi.died.tp.modelo.productos.Video;

public class PageRanksTableModel extends AbstractTableModel {

	List<VerticeView> vertices;
	private String[] columnas = {"Titulo","PageRanks"};
	
	@Override
	public String getColumnName(int indice) {
		return this.columnas[indice];
	}
	
	public void setPageRanks(List<VerticeView> vertices) {
		vertices.sort((VerticeView v1, VerticeView v2)->new Double(v2.getPageRank()).compareTo(new Double(v1.getPageRank())));
		this.vertices = vertices;
	}
	
	@Override
	public int getRowCount() {
		return vertices.size();
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
			valor = this.vertices.get(rowIndex).getNombre();
			break;
		case 1:
			DecimalFormat df = new DecimalFormat("0.00000000");
			valor = df.format(this.vertices.get(rowIndex).getPageRank());
			break;
		default:
			System.out.println("Indice fuera de rango");
			valor = "S/D";
			break;
		}
		return valor;
	}

}
