package frsf.isi.died.app.vista.material;

import java.util.Objects;

import frsf.isi.died.tp.estructuras.TipoNodo;

public class ValueMember {
  public TipoNodo valueMember;
  public String displayMember;

  public ValueMember(String displayMember,TipoNodo valueMember) {
    this.displayMember=displayMember;
    this.valueMember=valueMember;
  }

  public TipoNodo getTipo() {
    return valueMember;
  }

  public String getString() {
    return displayMember;
  }

  public void setNombre(String nombre) {
    this.displayMember=nombre;
  }

  //Esto es lo que hace que el JComboBox se visualize correctamente
  @Override
  public String toString() {
    return displayMember;
  } 
  
  @Override
  public int hashCode()
  {
	  int hash=7;
	  hash=19*hash+Objects.hashCode(this.displayMember);
	  hash=19*hash+Objects.hashCode(this.valueMember);
	  return hash;
  }
  
  @Override
  public boolean equals(Object obj)
  {
	  if(this==obj) return true;
	  if(obj==null) return false;
	  if(getClass()!=obj.getClass()) return false;
	  final ValueMember otro=(ValueMember) obj;
	  if(!Objects.equals(this.displayMember, otro.displayMember)) return false;
	  if(!Objects.equals(this.valueMember, otro.valueMember)) return false;
	  return true;
  }

}