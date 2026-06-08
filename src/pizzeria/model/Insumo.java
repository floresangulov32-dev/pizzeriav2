package pizzeria.model;

public class Insumo{
    public static final String[] UNIDADES_VALIDAS ={
        "kg", "g", "litro", "ml", "unidad", "porcion", "docena", "caja"
    };

    private int id;
    private String nombre;
    private String unidad;
    private double stockActual;
    private double stockMinimo;
    private double precioCompra;
    private double cantidadPorPizza;

    public Insumo(int id, String nombre, String unidad, double stockActual, double stockMinimo, double precioCompra, double cantidadPorPizza){
        this.id = id;
        this.nombre = nombre;
        this.unidad = unidad;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.precioCompra = precioCompra;
        this.cantidadPorPizza = cantidadPorPizza;
    }

    public int getId(){
        return id; 
    }
    public String getNombre(){ 
        return nombre; 
    }
    public String getUnidad(){
        return unidad; 
    }
    public double getStockActual(){
        return stockActual; 
    }
    public double getStockMinimo(){
        return stockMinimo; 
    }
    public double getPrecioCompra(){
        return precioCompra; 
    }
    public double getCantidadPorPizza(){
        return cantidadPorPizza; 
    }

    public void setId(int id){
        this.id = id; 
    }
    public void setNombre(String nombre){
        this.nombre = nombre; 
    }
    public void setUnidad(String unidad){
        this.unidad = unidad; 
    }
    public void setStockActual(double stockActual){ 
        this.stockActual = stockActual; 
    }
    public void setStockMinimo(double stockMinimo){
        this.stockMinimo = stockMinimo; 
    }
    public void setPrecioCompra(double precioCompra){
        this.precioCompra = precioCompra; 
    }
    public void setCantidadPorPizza(double c){
        this.cantidadPorPizza = c; 
    }

    public boolean stockBajo(){
        return stockActual <= stockMinimo;
    }
    
    public String escribirTexto(){
        return id + "," + nombre + "," + unidad + "," + stockActual + "," + stockMinimo + "," + precioCompra + "," + cantidadPorPizza;
    }

    @Override
    public String toString(){
        return String.format(
            "ID: %-4d | %-18s | %-8s | Stock: %7.3f | Min: %7.3f | Bs.%6.2f | Uso/pizza: %.3f",
            id, nombre, unidad, stockActual, stockMinimo, precioCompra, cantidadPorPizza
        );
    }
}