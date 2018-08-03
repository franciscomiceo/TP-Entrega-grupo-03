package frsf.isi.died.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import frsf.isi.died.app.vista.grafo.GrafoPanel;
import frsf.isi.died.app.vista.grafo.VerticeView;
import frsf.isi.died.tp.estructuras.Arista;
import frsf.isi.died.tp.estructuras.Grafo;
import frsf.isi.died.tp.estructuras.Vertice;
//import frsf.isi.died.tp.modelo.Biblioteca;
//import frsf.isi.died.tp.modelo.BibliotecaABB;
import frsf.isi.died.tp.modelo.productos.Libro;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;
import frsf.isi.died.tp.modelo.productos.Relevancia;
import frsf.isi.died.tp.modelo.productos.Video;

public class MaterialConexion {
	
	private static Grafo<MaterialCapacitacion> GRAFO_MATERIAL = new Grafo<MaterialCapacitacion>();
//	private static Biblioteca biblioteca = new BibliotecaABB(); NO LO USO (VER) TODO
	
	public MaterialConexion() {
		 //System.out.println("grafo es vacio?: "+GRAFO_MATERIAL.esVacio());
		
		 if(this.GRAFO_MATERIAL==null||GRAFO_MATERIAL.esVacio()) {
			 this.GRAFO_MATERIAL = new Grafo<MaterialCapacitacion>();
			 //System.out.println("cargando grafo");
			try {
			cargarGrafo();
			}catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.getMessage());}
		}
	}

	private void cargarGrafo() throws Exception{
		try {
		List<Libro> libros = getLibros();
		for(Libro libro : libros) {
			GRAFO_MATERIAL.addNodo(libro);
		}
		List<Video> videos= getVideos();
		for(Video video: videos) {
			GRAFO_MATERIAL.addNodo(video);
		}
		
		List<Arista<MaterialCapacitacion>> aristas= getAristas();
		for(Arista<MaterialCapacitacion> arista: aristas) { 
			GRAFO_MATERIAL.conectar(arista.getInicio(),arista.getFin());
		}
		}catch(Exception ex) {throw new Exception("No se pudo cargar el grafo ");}
 	}


public List<Arista<MaterialCapacitacion>> getAristas() throws Exception{
	List<Arista<MaterialCapacitacion>> aristas=new ArrayList<>();
	
	Connection con;
    try {
        con= Conexion.estableceConexion();
        Statement sent = con.createStatement();
        ResultSet result = sent.executeQuery("select id_origen,id_destino "
        		+ "FROM died.arista");
      //System.out.println("ingreso en getAristas");
        while (result.next()) 
        {
        	Arista<MaterialCapacitacion> arista=new Arista<MaterialCapacitacion>();
        	arista.setInicio(new Vertice<MaterialCapacitacion>(this.findById(result.getInt(1))));
        	arista.setFin(new Vertice<MaterialCapacitacion>(this.findById(result.getInt(2))));
        	aristas.add(arista);
        }
        result.close();
        sent.close();
        con.close();
        }catch (SQLException ex) {
            throw new Exception("No se pudo recuperar la lista de aristas");}
		return aristas;
	}

public static void agregarLibro(MaterialCapacitacion unLibro) throws Exception {  
        
        try {
        	validarTitulo(unLibro);
    
        	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
          
        	PreparedStatement sql = Conexion.estableceConexion().prepareStatement(
  "INSERT INTO died.material (titulo,costo,calificacion,fecha_publicacion,precio_compra,tema,paginas)"
  + "VALUES ('" + unLibro.getTitulo()
  + "' ,  '" + unLibro.getCosto()
 + "' ,  '" + unLibro.getCalificacion()
+ "' ,  '" + fmt.format(unLibro.getFecha_publicacion())
+ "' ,  '" + ((Libro)unLibro).getPrecioCompra()
+ "' ,  '" + unLibro.getTema()
+ "' ,  '" + ((Libro)unLibro).getPaginas()+ "' )");
            sql.executeUpdate();
            
            int id_libro=0;
            sql = Conexion.estableceConexion().prepareStatement("SELECT id_material FROM died.material ORDER BY id_material DESC LIMIT 1");
            ResultSet result =sql.executeQuery();
            while (result.next()) {id_libro=result.getInt(1);}
            unLibro.setId(id_libro);
          
            sql.close();

            GRAFO_MATERIAL.addNodo(unLibro);	
        	
        } catch (SQLException ex) {
           throw new Exception("No se pudo agregar el Libro ");
		}catch(Exception ex) {
	           throw new Exception(ex.getMessage());}
  }   
private static void validarTitulo(MaterialCapacitacion mc) throws Exception{
	Connection con;
    try {
        con= Conexion.estableceConexion();
        Statement sent = con.createStatement();
        ResultSet result;
        //System.out.println("id="+mc.getId());
        if(mc.getId()>0) 
        	result= sent.executeQuery("SELECT id_material FROM died.material WHERE titulo='"+mc.getTitulo()+"' AND id_material!='"+mc.getId()+"'");
        else result= sent.executeQuery("SELECT id_material FROM died.material WHERE titulo='"+mc.getTitulo()+"'");
        int id=0;
        while(result.next())
        	id=result.getInt(1);
        if(id>0) throw new Exception("Ya existe un material con el mismo titulo");
    }catch(SQLException ex) {throw new Exception("No se pudo validar el titulo");}
}

//--------------------------------------------------------------------------------------------------------------------
public static List<Libro> getLibros() throws Exception{
	List<Libro> libros = new ArrayList<>();
	
	Connection con;
    try {
        con= Conexion.estableceConexion();
        Statement sent = con.createStatement();
        ResultSet result = sent.executeQuery("select id_material,titulo,costo,calificacion,fecha_publicacion,precio_compra,paginas,tema "
        		+ "FROM died.material WHERE duracion IS NULL");
      //System.out.println("ingreso en getLibros");
        while (result.next()) {
        	Libro libro=new Libro();
        	libro.setId(result.getInt(1));
        	((MaterialCapacitacion)libro).setTitulo(result.getString(2));
        	libro.setCosto(result.getDouble(3));
        	((MaterialCapacitacion)libro).setCalificacion(result.getInt(4));
        	Timestamp fechaSql = result.getTimestamp(5);
            Date fecha = new Date(fechaSql.getTime());
        	((MaterialCapacitacion)libro).setFecha_publicacion(fecha);
        	libro.setPrecioCompra(result.getDouble(6));
        	libro.setPaginas(result.getInt(7));
        	libro.setTema(result.getString(8));
        	libros.add(libro);
        }
        result.close();
        sent.close();
        con.close();
    }catch(SQLException ex) {throw new Exception("No se pudo recuperar lista de libros");}
	
	return libros;
}
//--------------------------------------------------------------------------------------------------------------------
public static void agregarVideo(MaterialCapacitacion unVideo) throws Exception {  
    
    try {   
    	validarTitulo(unVideo);
    	
    	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        PreparedStatement sql = Conexion.estableceConexion().prepareStatement(
"INSERT INTO died.material (titulo,costo,calificacion,fecha_publicacion,tema,duracion)"
+ "VALUES ('" + unVideo.getTitulo()
+ "' ,  '" + unVideo.getCosto()
+ "' ,  '" + unVideo.getCalificacion()
+ "' ,  '" + fmt.format(unVideo.getFecha_publicacion())
+ "' ,  '" + unVideo.getTema()
+ "' ,  '" + ((Video)unVideo).getDuracionEnSegundos()+ "' )");
        sql.executeUpdate();
        
        int id_video=0;
        sql = Conexion.estableceConexion().prepareStatement("SELECT id_material FROM died.material ORDER BY id_material DESC LIMIT 1");
        ResultSet result =sql.executeQuery();
        while (result.next()) {
               id_video=result.getInt(1);
        }
        sql.close();
        
        unVideo.setId(id_video);
        GRAFO_MATERIAL.addNodo(unVideo);	
		
        sql.close();
    } catch (SQLException ex) {
       throw new Exception("No se pudo agregar el Video ");
    }catch(Exception ex) {
        throw new Exception(ex.getMessage());}
}   
//--------------------------------------------------------------------------------------------------------------------
public static List<Video> getVideos() throws Exception{
	List<Video> videos = new ArrayList<>();
	
	Connection con;
  try {
      con= Conexion.estableceConexion();
      Statement sent = con.createStatement();
      ResultSet result = sent.executeQuery("select id_material,titulo,costo,calificacion,fecha_publicacion,duracion,tema "
      		+ "FROM died.material WHERE paginas IS NULL");
    //System.out.println("ingreso en getVideos");
      while (result.next()) {
      	Video video=new Video();
      	video.setId(result.getInt(1));
      	((MaterialCapacitacion)video).setTitulo(result.getString(2));
      	video.setCosto(result.getDouble(3));
      	((MaterialCapacitacion)video).setCalificacion(result.getInt(4));
      	Timestamp fechaSql = result.getTimestamp(5);
          Date fecha = new Date(fechaSql.getTime());
      	((MaterialCapacitacion)video).setFecha_publicacion(fecha);
      	video.setDuracionEnSegundos(result.getInt(6));
      	video.setTema(result.getString(7));
      	videos.add(video);
      }
      result.close();
      sent.close();
      con.close();
  }catch(SQLException ex) {throw new Exception("No se pudo recuperar lista de videos");}
	
	return videos;
}

public List<Libro> listaLibros() {
	List<Libro> libros = new ArrayList<>();
	for(MaterialCapacitacion mat : GRAFO_MATERIAL.listaVertices()) {
		if(mat.esLibro()) libros.add((Libro)mat); 
	}
	return libros;
}

public List<Video> listaVideos() {
	List<Video> vids = new ArrayList<>();
	for(MaterialCapacitacion mat : GRAFO_MATERIAL.listaVertices()) {
		if(mat.esVideo()) vids.add((Video)mat); 
	}
	return vids;
}

public MaterialCapacitacion findById(Integer id) {
	for(MaterialCapacitacion mat : GRAFO_MATERIAL.listaVertices()) {
		if(mat.getId().equals(id)) return mat;
	}
	return null;
}

public void buscarCamino(Integer idOrigen, Integer idDestino, Integer saltos,GrafoPanel cp) {
	MaterialCapacitacion n1 = this.findById(idOrigen);
	MaterialCapacitacion n2 = this.findById(idDestino);
	GRAFO_MATERIAL.buscarCaminoNSaltos(n1, n2, saltos,cp);
}

public void buscarCamino(Integer idOrigen, Integer idDestino, GrafoPanel cp) {
	MaterialCapacitacion n1 = this.findById(idOrigen);
	MaterialCapacitacion n2 = this.findById(idDestino);
	GRAFO_MATERIAL.buscarCaminos(n1, n2, cp);
}


public List<MaterialCapacitacion> listaMateriales() {
	return GRAFO_MATERIAL.listaVertices();
}

public void crearCamino(Integer idOrigen, Integer idDestino) throws Exception{
	MaterialCapacitacion n1 = this.findById(idOrigen);
	MaterialCapacitacion n2 = this.findById(idDestino);
	GRAFO_MATERIAL.conectar(n1, n2);
	try {
		  PreparedStatement sql = Conexion.estableceConexion().prepareStatement(
				  "INSERT INTO died.arista (id_origen,id_destino)"
				  + "VALUES ('" + idOrigen
				  + "' ,  '" + idDestino+ "' )");
		  sql.executeUpdate();
		  sql.close();
	} catch (SQLException ex) {throw new Exception("No se pudo agregar la arista ("+idOrigen+","+idDestino+")"+ex.getMessage());}
}
//---------------------------------------------------------------------------------------------------------------------

public static void modificarLibro(Libro l) throws Exception{
	Connection con;
		try {
			    validarTitulo(l);
	    		
			    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	            con=Conexion.estableceConexion();
	            PreparedStatement sql = con.prepareStatement(
	                "UPDATE died.material "
	                + "SET titulo='"+l.getTitulo()+"', costo='"+l.getCosto()+"', fecha_publicacion='"+fmt.format(l.getFecha_publicacion())
	                +"', precio_compra='"+l.getPrecioCompra()+"', paginas='"+l.getPaginas()+"', calificacion='"+l.getCalificacion()
	                +"', tema='"+l.getTema()
	                +"' WHERE id_material='"+l.getId()+"'");
	            sql.executeUpdate();
	            
	            //para modificar titulo también en el grafo
	            sql = con.prepareStatement(
		                "UPDATE died.vertice "
		                		+ "SET nombre='"+l.getTitulo()+"' WHERE id_material='"+l.getId()+"'");
	            sql.executeUpdate();
	            sql.close();
	            con.close();
	        } catch (SQLException ex) {throw new Exception("No se pudo modificar el Libro");}
	    }

public static void modificarVideo(Video v) throws Exception{
	Connection con;
	try {  
			validarTitulo(v);
    		
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            con=Conexion.estableceConexion();
            PreparedStatement sql = con.prepareStatement(
                "UPDATE died.material "
                + "SET titulo='"+v.getTitulo()+"', costo='"+v.getCosto()+"', fecha_publicacion='"+fmt.format(v.getFecha_publicacion())
                +"', duracion='"+v.getDuracionEnSegundos()+"', calificacion='"+v.getCalificacion()
                +"', tema='"+v.getTema()
                +"' WHERE id_material='"+v.getId()+"'");
            sql.executeUpdate();
            
          //para modificar titulo también en el grafo
            sql = con.prepareStatement(
	                "UPDATE died.vertice "
	                		+ "SET nombre='"+v.getTitulo()+"' WHERE id_material='"+v.getId()+"'");
            sql.executeUpdate();
            sql.close();
            con.close();
        } catch (SQLException ex) {throw new Exception("No se pudo modificar el Video");}
	
}

public static void eliminarLibro(int id_libro) throws Exception{
	Connection con;
	try {  
            con=Conexion.estableceConexion();
            PreparedStatement sql = con.prepareStatement(
                "DELETE FROM died.material WHERE id_material='"+id_libro+"'");
            sql.executeUpdate();
            sql.close();
            con.close();
        } catch (SQLException ex) {throw new Exception("No se pudo eliminar el Libro");}
}

public static void eliminarVideo(int id_video) throws Exception{
	Connection con;
	try {  
            con=Conexion.estableceConexion();
            PreparedStatement sql = con.prepareStatement(
                "DELETE FROM died.material WHERE id_material='"+id_video+"'");
            sql.executeUpdate();
            sql.close();
            con.close();
        } catch (SQLException ex) {throw new Exception("No se pudo eliminar el Video");}
	
}

public void agregarVertice(VerticeView v) throws Exception{
	try {
		System.out.println(v.getTema());
		  PreparedStatement sql = Conexion.estableceConexion().prepareStatement(
				  "INSERT INTO died.vertice (id_material,nombre,x,y,tema)"
				  + "VALUES ('" + v.getId()
				  + "' ,  '" + v.getNombre()
				  + "' ,  '" + v.getCoordenadaX()
				  + "' ,  '" + v.getCoordenadaY()
				  + "' ,  '" + v.getTema()+ "' )");
		  sql.executeUpdate();
		  sql.close();
	} catch (SQLException ex) {throw new Exception("No se pudo agregar el vertice "+v.getNombre()+"");}	
}

public List<VerticeView> getVertices() throws Exception{
List<VerticeView> vertices = new ArrayList<>();
	
	Connection con;
  try {
      con= Conexion.estableceConexion();
      Statement sent = con.createStatement();
      ResultSet result = sent.executeQuery("SELECT id_material,nombre,x,y,tema FROM died.vertice");
    //System.out.println("ingreso en verticesV");
      while (result.next()) {
    	  VerticeView v=new VerticeView();
    	  v.setId(result.getInt(1));
    	  v.setNombre(result.getString(2));
    	  v.setCoordenadaX(result.getInt(3));
    	  v.setCoordenadaY(result.getInt(4));
    	  v.setTema(result.getString(5));
    	  vertices.add(v);
      }
  	} catch (SQLException ex) {throw new Exception("No se pudieron recuperar los vertices para mostrarlos");}
    
  return vertices;
}

public static void resetGrafo() throws Exception{
	Connection con;
	try {  
            con=Conexion.estableceConexion();
            PreparedStatement sql = con.prepareStatement(
                "DELETE FROM died.arista");
            sql.executeUpdate();
            sql = con.prepareStatement(
                    "DELETE FROM died.vertice");
            sql.executeUpdate();
            sql.close();
            con.close();
            
            GRAFO_MATERIAL.resetear();
            
        } catch (SQLException ex) {throw new Exception("No se pudo resetear el Grafo");}
}

}
