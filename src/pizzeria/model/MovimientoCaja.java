package pizzeria.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovimientoCaja{
    
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                
    public static final String CAT_VENTA = "Venta";
    public static final String CAT_COMPRA_INSUMO = "CompraInsumo";
    public static final String CAT_SALARIO = "Salario";
    public static final String CAT_OTRO = "Otro";
    
    private int id;
    private TipoMovimiento tipo;
    private double monto;
    private LocalDateTime fecha;
    private String categoria;
    private String descripcion;
    
    public MovimientoCaja(int id, TipoMovimiento tipo, double monto, LocalDateTime fecha, String categoria, String descripcion){
        this.id = id;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }
    
    public int getId(){
        return id;
    }
    
    public TipoMovimiento getTipo(){
        return tipo;
    }
    
    public double getMonto(){
        return monto;
    }
    
    public LocalDateTime getFecha(){
        return fecha;
    }
    
    public String getCategoria(){
        return categoria;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setTipo(TipoMovimiento tipo){
        this.tipo = tipo;
    }
    
    public void setMonto(double monto){
        this.monto = monto;
    }
    
    public void setFecha(LocalDateTime fecha){
        this.fecha = fecha;
    }
    
    public void setCategoria(String categoria){
        this.categoria = categoria;
    }
    
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
    
    public String escribirTexto(){
        return id + "|" + tipo.name() + "|" + String.format("%.2f", monto) + "|"
            + fecha.format(FORMATO_FECHA) + "|" + categoria + "|" + descripcion;
    }
    
    
    public static MovimientoCaja leerTexto(String linea){
        try{
            String[] p = linea.split("\\|");
            if(p.length != 6){
                return null;
            }
            
            int id = Integer.parseInt(p[0].trim());
            TipoMovimiento tipo = TipoMovimiento.valueOf(p[1].trim());
            
            String montoStr = p[2].trim().replace(",",".");
            double monto = Double.parseDouble(montoStr);
            LocalDateTime  fecha = LocalDateTime.parse(p[3].trim(), FORMATO_FECHA);
            String categoria = p[4].trim();
            String descripcion = p[5].trim();
            
            return new MovimientoCaja(id, tipo,monto, fecha, categoria, descripcion);
        }catch(Exception e){
            return null;
        }
    }
    
    @Override
    public String toString(){
        return String.format("ID:%-4d | %-7s | S/.%8.2f | %s | %-15s | %s",
            id, tipo.name(), monto, fecha.format(FORMATO_FECHA), categoria, descripcion);
    }
    
}
