/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsf.isi.died.tp.modelo.usuarios;

import java.util.List;

import frsf.isi.died.tp.modelo.productos.MaterialCapacitacion;

/**
 *
 * @author mdominguez
 */
public final class Suscriptor {
    private String nombre; 
    private Double credito;
    private MaterialCapacitacion[] materialSuscripto;

    public Suscriptor(){
        credito=0.0;
    }    
    
    public Suscriptor(String nombre){
        this();
        this.nombre=nombre;
    }       
    
    public Double getCredito() {
        return credito;
    } 

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
   
    
    // metodos de negocio 

    public void addCredito(Double cantidad){
        this.credito+=cantidad;
    }
    
    public void suscribir(MaterialCapacitacion m) {
        if(this.credito-m.precio()>0){
            this.credito-=m.precio();
            // m.suscribir(this);
            // agregarlo en la coleccion
        }
    }

    public void suscribir(MaterialCapacitacion[] m) {
    }
    
    public MaterialCapacitacion[] listar() {
    	return null;
    }
        

}
