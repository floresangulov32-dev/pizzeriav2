package pizzeria.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deuda{
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_PAGADA = "PAGADA";
    
    public static final String TIPO_COMPRA_INSUMO = "CompraInsumo";
    public static final String TIPO_SALARIO = "Salario";
    public static final String TIPO_OTRO = "Otro";
    
    private int id;
    private String tipo;
    private String descripcion;
    private double montoTotal;
    private String proveedor;
    private LocalDateTime fechaCompromiso;
    private String estado;
    
    
    public Deuda(int id, String tipo, String descripcion, double montoTotal, String proveedor, LocalDateTime fechaCompromiso){
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.montoTotal = montoTotal;
        this.proveedor = proveedor;
        this.fechaCompromiso = fechaCompromiso;
        this.estado = ESTADO_PENDIENTE; 
    }
    
    public int getId(){
        return id;
    }
    
    public String getTipo(){
        return tipo;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
    
    public double getMontoTotal(){
        return montoTotal;
    }
    
    public String getProveedor(){
        return proveedor;
    }
    
    public LocalDateTime getFechaCompromiso(){
        return fechaCompromiso;
    }   
    
    public String getEstado(){
        return estado;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
    
    public void setMontoTotal(double montoTotal){
        this.montoTotal = montoTotal;
    }
    
    public void setProveedor(String proveedor){
        this.proveedor = proveedor;
    }
    
    public void setFecha(LocalDateTime fechaCompromiso){
        this.fechaCompromiso = fechaCompromiso;
    }   
    
    public void setEstado(String estado){
        this.estado = estado;
    }
    
    public boolean esPendiente(){
        return ESTADO_PENDIENTE.equals(estado);
    }
    
    public String escribirTexto(){
        return id + "|" + tipo + "|" + descripcion + "|" + String.format("%.2f", montoTotal).replace(",",".") + "|"
             + proveedor + "|" + fechaCompromiso.format(MovimientoCaja.FORMATO_FECHA) + "|"
             + estado;
    }
    
    public static Deuda leerTexto(String linea){
        try{
            String[] p = linea.split("\\|");
            if(p.length != 7){
                return null;    
            }

            int id = Integer.parseInt(p[0].trim());
            String tipo = p[1].trim();
            String descripcion = p[2].trim();

            String montoStr = p[3].trim().replace(",",".");
            double montoTotal = Double.parseDouble(montoStr);
            String proveedor = p[4].trim();
            LocalDateTime fechaCompromiso = LocalDateTime.parse(p[5].trim(), MovimientoCaja.FORMATO_FECHA);
            String estado = p[6].trim();

            Deuda d = new Deuda(id, tipo, descripcion, montoTotal, proveedor, fechaCompromiso);
            d.setEstado(estado);
            return d;
        }catch(Exception e){
            return null;
        }
    }
    
    @Override
    public String toString(){
        return String.format("ID:%-4d | %-13s | S/.%8.2f | Vence: %s | %-20s | [%s]",
                id, tipo, montoTotal,
                fechaCompromiso.format(MovimientoCaja.FORMATO_FECHA),
                proveedor, estado);
    }
    
}
