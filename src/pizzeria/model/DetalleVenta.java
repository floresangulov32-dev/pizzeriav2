package pizzeria.model;


import pizzeria.model.Producto;


public class DetalleVenta{
    private Producto producto;
    private int cantidad;
    private double subTotal;
 
    public DetalleVenta(Producto producto, int cantidad){
        this.producto = producto;
        this.cantidad = cantidad;
        calcularSubTotal();
    }
 
    public void calcularSubTotal(){
        this.subTotal = producto.getPrecio() * cantidad;
    }
 
    public Producto getProducto(){
        return producto;
    }
 
    public void setProducto(Producto producto){
        this.producto = producto;
        calcularSubTotal();
    }
 
    public int getCantidad(){
        return cantidad;
    }
 
    public void setCantidad(int cantidad){
        this.cantidad = cantidad;
        calcularSubTotal();
    }
 
    public double getSubTotal(){
        return subTotal;
    }
 
    public String escribirTexto(){
        return producto.getID() + "~"
             + producto.getNombre() + "~"
             + String.format("%.2f", producto.getPrecio()).replace(",", ".") + "~"
             + cantidad;
    }
    @Override
    public String toString(){
        return String.format("  %-22s x%2d  @Bs.%6.2f  =>  Bs.%7.2f",
                producto.getNombre(), cantidad, producto.getPrecio(), subTotal);
    }
}
