/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzeria.model;


public enum TipoProducto {
    PRODUCTO("Producto"),
    REFRESCO("Refresco");

    private final String nombre;

    TipoProducto(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    // Convierte el String del archivo al enum (tolerante a mayúsculas)
    public static TipoProducto fromString(String texto){
        try{
            return TipoProducto.valueOf(texto.trim().toUpperCase());
        } catch(IllegalArgumentException e){
            return PRODUCTO;
        }
    }
    
}
