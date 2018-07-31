package excepcion;

import java.util.regex.Pattern;

public class Validar {
        
    public static void isNumeros(String campo,String cod,int min, int max,boolean b) throws Exception {
        if(b) //debe ser Integer
        	{if ((!cod.matches("^[0-9]{"+min+","+max+"}$")))
        		throw new Exception(campo+"Debe Ingresar solo numeros enteros; entre "+min+" y "+max+" digitos");
        	}
        else //debe ser Double
        	{
        		final Pattern DOUBLE_PATTERN = Pattern.compile(
    		    "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
    		    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
    		    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
    		    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");
    	
        		if(!DOUBLE_PATTERN.matcher(cod).matches()) //if ((!cod.matches("^[0-9]{"+min+","+max+"}$"))){
        			throw new Exception(campo+"Debe Ingresar solo numeros decimales; entre "+min+" y "+max+" digitos");   
        	}
        if(Double.parseDouble(cod)<=0)
        	throw new Exception(campo+"Debe Ingresar solo numeros mayores a cero (0)");
    }
        
        public static void estaEntre(String campo,String nombre, int min, int max) throws Exception{
        if (nombre.length() >= min || nombre.length() <= max) {
            throw new Exception(campo+"Debe ingresar entre "+min+" y "+max+" caracteres");
        }
        }
        
        //tipo==true <-> nombrePuesto, nombreEmpresa, nombreConsultor, contraseniaConsultor
        //tipo==false <-> descripcion
        public static void isLetrasNumeros(String campo,String nom,int min, int max,boolean tipo) throws Exception {
       if(tipo)
       {
       char[] nombre=nom.toCharArray();
       String caracter="!\"#$%&'()*+,-./:;?@_";
       char[] car=caracter.toCharArray();
       for(char input: nombre)
           for (char crt : car)
            if (crt == input)
                throw new Exception(campo+"Debe ingresar solo letras y numeros entre "+min+" y "+max+" caracteres. \n"
                        + "caracteres no admitidos: !\"#$%&'()*+,-./:;?@_");
       }
       
       if (!nom.matches("^[a-zA-Z0-9 ]{"+min+","+max+"}$")) {
                throw new Exception(campo+"Debe ingresar solo letras y numeros entre "+min+" y "+max+" caracteres");
            }
       
      }
    }
