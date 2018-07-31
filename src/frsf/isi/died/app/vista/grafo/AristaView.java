/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsf.isi.died.app.vista.grafo;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import frsf.isi.died.tp.estructuras.Arista;
import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;

/**
 * 
 *
 * @author martdominguez
 */
public class AristaView {

    private VerticeView origen;
    private VerticeView destino;
    private Shape linea;
    private Stroke formatoLinea;
    private Paint color;

    public AristaView() {
    }

    /**
     * Define que el color de la linea es un degradado (color) 
 desde la coordenada origen de la linea, tomando como base el color origen de la linea
 hacia la coordenada destino de la linea, tomando como base el color destino de la linea
     * @return 
     */
    public Paint getColor() {
        if(this.color==null) this.color = new GradientPaint(origen.getCoordenadaX() + 10,origen.getCoordenadaY() + 10,destino.getColorBase(),destino.getCoordenadaX() + 10, destino.getCoordenadaY() + 10,origen.getColorBase());
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }

    
    public boolean pertenece(Point2D p) {
        return this.linea.contains(p);
    }

    /**
     * Dibuja una linea 2D desde el las coordenadas del nodo origen 
     * hacia las coordenadas del nodo destino
     * @return 
     */
    public Shape getLinea() {
        if(this.linea==null)  this.linea = new Line2D.Double(origen.getCoordenadaX() + 10, origen.getCoordenadaY() + 10, destino.getCoordenadaX() + 10, destino.getCoordenadaY() + 10);
        return linea;
    }

    public void setLinea(Shape linea) {
        this.linea = linea;
    }

    /**
     * Determina el grosor de la linea.
     * @return 
     */
    public Stroke getFormatoLinea() {
        if(this.formatoLinea==null)  this.formatoLinea = new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);       
        return formatoLinea;
    }

    public void setFormatoLinea(Stroke formatoLinea) {
        this.formatoLinea = formatoLinea;
    }

    public VerticeView getOrigen() {
        return origen;
    }

    public void setOrigen(VerticeView origen) {
        this.origen = origen;
    }

    public VerticeView getDestino() {
        return destino;
    }

    public void setDestino(VerticeView destino) {
        this.destino = destino;
    }

    @Override
    public String toString() {
        return "Arista{" + "origen=" + origen + ", destino=" + destino + ", linea=" + linea + ", formatoLinea=" + formatoLinea + ", gradiente=" + color + '}';
    }



}
