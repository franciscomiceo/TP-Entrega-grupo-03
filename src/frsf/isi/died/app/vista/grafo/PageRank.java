package frsf.isi.died.app.vista.grafo;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import frsf.isi.died.app.vista.material.BusquedaTableModel;
import frsf.isi.died.app.vista.material.PageRanksTableModel;

public class PageRank extends JDialog {

	private JScrollPane scrollPane;
	private JTable tabla;
	private PageRanksTableModel tableModel;
	
	public PageRank(JFrame principal,boolean modal, List<VerticeView> vertices) {
		super(principal, modal);
        setTitle("PageRanks: Tema"+ vertices.get(0).getTema());
        this.tableModel = new PageRanksTableModel();
        initComponents(vertices);
        this.setLocationRelativeTo(null);
	}

	private void initComponents(List<VerticeView> vertices) {
		this.tableModel.setPageRanks(vertices);
		tabla = new JTable(this.tableModel);
		tabla.setFillsViewportHeight(true);
		scrollPane= new JScrollPane(tabla);
		this.add(scrollPane);
		this.pack();
	}
}
