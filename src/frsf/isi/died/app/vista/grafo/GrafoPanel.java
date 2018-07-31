/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsf.isi.died.app.vista.grafo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import frsf.isi.died.app.controller.GrafoController;
import frsf.isi.died.tp.estructuras.Arista;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;

/**
 *
 * @author martdominguez
 */
public class GrafoPanel extends JPanel {

    private JFrame framePadre;
    private JFrame principal;
    private Queue<Color> colaColores;
    private GrafoController controller;

    private List<VerticeView> vertices;
    private List<AristaView> aristas;

    private AristaView auxiliar;

    boolean drag=false;
    
    public GrafoPanel(JFrame principal) {
        this.framePadre = (JFrame) this.getParent();
        this.principal=principal;
        this.vertices = new ArrayList<>();
        this.aristas = new ArrayList<>();

        
        this.colaColores = new LinkedList<Color>();
        this.colaColores.add(Color.RED);
        this.colaColores.add(Color.BLUE);
        this.colaColores.add(Color.ORANGE);
        this.colaColores.add(Color.CYAN);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2 && !event.isConsumed()) {
                	//System.out.println("mouse2Clicks");
                    event.consume();
                    if(controller.listaVertices().size()>0)
                    {
                    Object[] mats = controller.listaVertices().toArray();
                    //String text = JOptionPane.showInputDialog(, "ID del nodo");
                    Object verticeMatSeleccionado= (MaterialCapacitacion) JOptionPane.showInputDialog(framePadre, 
                            "Que material corresponde con el vertice?",
                            "Agregar Vertice",
                            JOptionPane.QUESTION_MESSAGE, 
                            null, 
                            mats, 
                            mats[0]);

                    if (verticeMatSeleccionado != null) {
                        // quito un color de la cola
                        Color aux = colaColores.remove();
                        try {
                        controller.crearVertice(event.getX(), event.getY(), aux,(MaterialCapacitacion) verticeMatSeleccionado);
                        }catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
                        // pongo el color al final de la cola
                        colaColores.add(aux);
                    }
                    }
                    else JOptionPane.showMessageDialog(null,"Debe cargar al menos 2 materiales");
                }
            }

            public void mouseReleased(MouseEvent event) {
            	if(drag)
            	{
                try{
                	//System.out.println("mouseReleased");
                		VerticeView vDestino = clicEnUnNodo(event.getPoint());
                
                		if(auxiliar.getOrigen()==vDestino)
                			JOptionPane.showMessageDialog(null,"Debe seleccionar 2 puntos distintos");
                
                		if (auxiliar!=null && vDestino != null) 
                		{
                			auxiliar.setDestino(vDestino);
                			System.out.println("Vertice destino: "+vDestino.toString());
                			controller.crearArista(auxiliar);
                		}
                		auxiliar=null;
                	}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
            }
            	drag=false;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent event) {
            	drag=true;
            	try{
            		//System.out.println("mouseDragged");
                VerticeView vOrigen = clicEnUnNodo(event.getPoint());
                
                if (auxiliar==null && vOrigen != null) {
                    auxiliar = new AristaView();                    
                    auxiliar.setOrigen(vOrigen);
                    System.out.println("Vertice origen: "+vOrigen.toString());
                }
            	}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
            }
        });
    }

    public boolean agregar(AristaView arista){
    	boolean valido=true;
    	for(AristaView av: aristas)
    		if(arista.getOrigen().equals(av.getOrigen())&&arista.getDestino().equals(av.getDestino()))
    		{
    			valido=false; 
    			JOptionPane.showMessageDialog(null,"La arista que desea crear ya existe");
    		}
    	
    	if(valido)
    		this.aristas.add(arista);
    	return valido;
    }    
    
    public boolean agregar(VerticeView vert){
    	boolean valido=true;
    	
    	for(VerticeView vv: vertices)
    		{
    			if(vert.getNombre().equals(vv.getNombre())) 
    			{
    				valido=false; JOptionPane.showMessageDialog(null,"Ya existe un vertice con el mismo nombre");
    			}
    			if(vert.getCoordenadaX()==vv.getCoordenadaX()&&vert.getCoordenadaY()==vv.getCoordenadaY())
    			{
    				valido=false; JOptionPane.showMessageDialog(null,"Ya existe un vertice en esta posicion");
    			}
    		}
    	
    	if(valido)
    		this.vertices.add(vert);
    	return valido;
    }

    public void caminoPintar(List<MaterialCapacitacion> camino){
        //this.vertices.add(vert);
    	Integer idOrigen =-1;
    	Integer idDestino =-1;
    	for(MaterialCapacitacion mat : camino) {
    		if(idOrigen<0) {
    			idOrigen=mat.getId();
    		}else {
    			idDestino = mat.getId();
    			for(AristaView av : this.aristas) {
    				if(av.getOrigen().getId().equals(idOrigen) && av.getDestino().getId().equals(idDestino) ) {
    	    			av.setColor(Color.RED);
    	    			av.getOrigen().setColor(Color.BLUE);
    	    			av.getDestino().setColor(Color.BLUE);
    				}
    			}
    			idOrigen = idDestino;
    		}
    	}
    }
    
    //Se llaman repetidamente (?????)//////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        dibujarVertices(g2d);
        dibujarAristas(g2d);
    }
    
    private void dibujarVertices(Graphics2D g2d) {
    	/*System.out.println("dibujarVertices de GrafoPanel ");
    	for(VerticeView vv: this.vertices)
    		System.out.println(vv);
    	System.out.println();*/
    	
    	for (VerticeView v : this.vertices) {
        	g2d.setPaint(v.getColor());
            g2d.drawString(v.etiqueta(),v.getCoordenadaX()-5,v.getCoordenadaY()-5);
            g2d.fill(v.getNodo());
        }
    }

    private void dibujarAristas(Graphics2D g2d) {
        /*System.out.println("dibujarAristas de GrafoPanel ");
    	for(AristaView av: this.aristas)
    		System.out.println(av);
    	System.out.println();*/
    	
        for (AristaView a : this.aristas) {
            g2d.setPaint(a.getColor());
            g2d.setStroke ( a.getFormatoLinea());
            g2d.draw(a.getLinea());
            //dibujo una flecha al final
            // con el color del origen para que se note
            g2d.setPaint(Color.BLACK);
            Polygon flecha = new Polygon();  
            flecha.addPoint(a.getDestino().getCoordenadaX(), a.getDestino().getCoordenadaY()+7);
            flecha.addPoint(a.getDestino().getCoordenadaX()+20, a.getDestino().getCoordenadaY()+10);
            flecha.addPoint(a.getDestino().getCoordenadaX(), a.getDestino().getCoordenadaY()+18);
            g2d.fillPolygon(flecha);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private VerticeView clicEnUnNodo(Point p) {
        for (VerticeView v : this.vertices) {
            if (v.getNodo().contains(p)) {
                return v;
            }
        }
        return null;
    }

    public Dimension getPreferredSize() {
        return new Dimension(450, 400);
    }

    public GrafoController getController() {
        return controller;
    }

    public void setController(GrafoController controller) {
        this.controller = controller;
    }

    //recupera el grafo PINTÁNDOLO!!!!!!!!
	public void cargarGrafo(List<VerticeView> vertices, List<Arista<MaterialCapacitacion>> aristas) {
		this.colaColores = new LinkedList<Color>();
        this.colaColores.add(Color.RED);
        this.colaColores.add(Color.BLUE);
        this.colaColores.add(Color.ORANGE);
        this.colaColores.add(Color.CYAN);
        
        List<VerticeView> verticesV=new ArrayList<>();
        
        for(VerticeView vv: vertices)
		{
			// quito un color de la cola
			Color aux=colaColores.remove();
			try {
				verticesV.add(controller.crearVertice(vv,aux));
			}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
			// pongo el color al final de la cola
			colaColores.add(aux);
		}
        
        this.vertices=verticesV;
        
        //System.out.println("vertices PINTADOS!!!");
        //System.out.println(this.vertices);
        
        for(Arista<MaterialCapacitacion> a: aristas)
        {
        	auxiliar = new AristaView();
        	
        	VerticeView origen=new VerticeView();
        	VerticeView destino=new VerticeView();
        	
        	for(VerticeView vv: verticesV)
        	{
        		if(vv.getId()==a.getInicio().getValor().getId()) 
        			origen=vv;
        		if(vv.getId()==a.getFin().getValor().getId())
        			destino=vv;
        	}
        	
        	auxiliar.setOrigen(origen);
        	auxiliar.setDestino(destino);
        	
        	this.aristas.add(auxiliar);
        }
        
        //System.out.println("aristas PINTADAS!!!");
        //System.out.println(this.aristas);
	}

	public List<VerticeView> getVertices() {
		return vertices;
	}

	public void setVertices(List<VerticeView> vertices) {
		this.vertices = vertices;
	}

	
//PARA CALCULAR Y MOSTRAR LOS PAGERANKS de un tema determinado------------------------------------------------------------------------------
	public void calcularPageRank(String tema) {
		//System.out.println(tema);
		
		List<VerticeView> verticesTema=new ArrayList<>(); //considero sólo los vértices con dicho "TEMA"
		for(VerticeView vv: vertices)
		{
			System.out.println(vv.getTema());
			if(vv.getTema().compareTo(tema)==0)
				verticesTema.add(vv);
		}
		
		if(verticesTema.size()>0)
			if(aristas.size()>0)
		{
			
			double[] pageRanks=new double[verticesTema.size()];
			int i=0;
			for(;i<verticesTema.size();i++)
				pageRanks[i]=1.0;

		final double d=0.5;
		final double e=Math.pow(10,-8);
		int menor=0; //cuenta la cantidad de diferencias entre cada iteración menores a "e"
		
		do {
			/* DecimalFormat df = new DecimalFormat("0.00000000");
		     System.out.print("pageRanks: "); 
			for(int k=0;k<pageRanks.length;k++) {System.out.printf(df.format(pageRanks[k])); if(k!=pageRanks.length-1)System.out.print(",");}
			System.out.println();*/
			
			double[] aux=new double[verticesTema.size()];
			for(i=0;i<verticesTema.size();i++)
				aux[i]=0.0;
			
			for(i=0;i<verticesTema.size();i++) //ANALIZADO
			{
				for(int j=0;j<verticesTema.size();j++) //LOS DEMAS
					if(i!=j)
					for(AristaView arista: aristas)
						if(arista.getOrigen().equals(verticesTema.get(j))&&arista.getDestino().equals(verticesTema.get(i)))
						{
							int cont=0; //cantidad de enlaces salientes
							for(AristaView a: aristas)
								if(a.getOrigen().equals(arista.getOrigen()))
									cont++;
							aux[i]=(double)(aux[i]+pageRanks[verticesTema.indexOf(arista.getOrigen())]/(double)cont);
						}
					aux[i]=(double)(((double)1-d)+d*aux[i]);
			}
			
			menor=0;
			for(int j=0;j<aux.length;j++)
				if(Math.abs((double)(aux[j]-pageRanks[j]))<e)
					menor++;
			
			pageRanks=aux;
			
		}while(menor!=verticesTema.size());
		
		for(i=0;i<verticesTema.size();i++)
			verticesTema.get(i).setPageRank(pageRanks[i]);
		
		PageRank pr=new PageRank(principal,true,verticesTema);
		pr.setVisible(true);
		
		}
			else JOptionPane.showMessageDialog(null,"No hay caminos agregados aun");
		else JOptionPane.showMessageDialog(null,"No hay vertices y/o caminos agregados aun para dicho tema");
	}
	
}
