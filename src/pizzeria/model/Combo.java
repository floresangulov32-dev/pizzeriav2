package pizzeria.model;

import pizzeria.model.Producto;
import java.util.ArrayList;

public class Combo{
    private static int contadorId = 1;

    public static void ajustarContador(int maxIdCargado){
        if(maxIdCargado >= contadorId){
            contadorId = maxIdCargado + 1;
        }
    }

    public static int getContadorId(){
        return contadorId;
    }

    private final int nroCombo;
    private double precio;
    private ArrayList<Producto> combo;
    
    public Combo(double precio, ArrayList<Producto> combo){
        this.nroCombo = contadorId++;
        this.precio   = precio;
        this.combo    = combo != null ? combo : new ArrayList<>();
    }

    public Combo(int nroFijo, double precio, ArrayList<Producto> combo){
        this.nroCombo = nroFijo;
        this.precio   = precio;
        this.combo    = combo != null ? combo : new ArrayList<>();
    }

    public boolean agregarProducto(Producto prod){
        for(Producto p : combo){
            if(p.getID() == prod.getID()){
                return false;
            }
        }
        combo.add(prod);
        return true;
    }

    public boolean eliminarProducto(int id){
        for(int i = 0; i < combo.size(); i++){
            if(combo.get(i).getID() == id){
                combo.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean estaVacio(){
        return combo.isEmpty();
    }

    public String verCombo(){
        StringBuilder sb = new StringBuilder();
        sb.append("Combo #").append(nroCombo).append("\n");
        sb.append("Productos:\n");
        if(combo.isEmpty()){
            sb.append("  (sin productos)\n");
        }else{
            for(Producto p : combo){
                sb.append(String.format("   - %-20s\n", p.getNombre()));
            }
        }
        sb.append(String.format("Precio total: %.2f Bs.%n", precio));
        return sb.toString();
    }

    public int getNroCombo(){
        return nroCombo; 
    }
    public double getPrecio(){ 
        return precio; 
    }
    public ArrayList<Producto> getCombo(){ 
        return combo; 
    }

    public void setPrecio(double precio){ 
        this.precio = precio; 
    }
    public void setCombo(ArrayList<Producto> combo){ 
        this.combo = combo; 
    }
}