package pizzeria.model;

import java.util.ArrayList;

public class Producto{
    private static int contadorId = 1;
    
    private int ID;
    private String nombre;
    private String descripcion;
    private double precio;
    private ArrayList <Integer> ingredientes;
    private TipoProducto tipo;
    
    public Producto(String nombre, String descripcion, double precio, TipoProducto tipo){
        this.ID = contadorId++;
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.ingredientes = new ArrayList<>();
    }
    
    public Producto(int ID, TipoProducto tipo, String nombre, String descripcion, double precio){
        this.ID = ID;
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.ingredientes = new ArrayList<>();
        
        if(ID >= contadorId){
            contadorId = ID + 1;
        }
    }
    
    
    public static void ajustarContador(int maxIdCargado){
        if(maxIdCargado >= contadorId){
            contadorId = maxIdCargado + 1;
        }
    }
    
    public static int getContadorId(){
        return contadorId;
    }
    
    public static void resetearContador(){
        contadorId = 1;
    }
    
    public int getID(){
        return ID;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
    
    public double getPrecio(){
        return precio;
    }
    
    public ArrayList<Integer> getIngredientes(){
        return ingredientes;
    }
    
    public void setID(int id){
        this.ID = id;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public void setDescripcion(String descrip){
        descripcion = descrip;
    }
    
    public void setPrecio(double precio){
        this.precio = precio;
    }
    
    public TipoProducto getTipo(){
        return tipo;
    }
    
    public void setTipo(TipoProducto tipo){
        this.tipo = tipo;
    }
    
    public void agregarIngrediente(int i){
        ingredientes.add(i); 
    }
    
    public void eliminarIngrediente(int id){
        for(int i = 0; i < ingredientes.size(); i++){
            if(ingredientes.get(i) == id){
                ingredientes.remove(i);
                break;
            }
        }
    }
    
    public String verProducto(){
        return String.format("[%02d] %-20s ....... %6.2f Bs", ID, nombre, precio);
    }
}

