package pizzeria.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovimientoInventario {
    
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    private int id;
    private int idInsumo;
    private String nombreInsumo;
    private String tipoMovimiento; // "ENTRADA" o "SALIDA"
    private double cantidad;
    private double stockResultante;
    private String usuario;
    private String observacion;
    private LocalDateTime fecha;
    
    public MovimientoInventario(int id, int idInsumo, String nombreInsumo, String tipoMovimiento, 
                                double cantidad, double stockResultante, String usuario, 
                                String observacion, LocalDateTime fecha) {
        this.id = id;
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.stockResultante = stockResultante;
        this.usuario = usuario;
        this.observacion = observacion;
        this.fecha = fecha;
    }
    
    // Getters
    public int getId() { return id; }
    public int getIdInsumo() { return idInsumo; }
    public String getNombreInsumo() { return nombreInsumo; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public double getCantidad() { return cantidad; }
    public double getStockResultante() { return stockResultante; }
    public String getUsuario() { return usuario; }
    public String getObservacion() { return observacion; }
    public LocalDateTime getFecha() { return fecha; }
    
    public String getFechaFormateada() {
        return fecha.format(FORMATO_FECHA);
    }
    
    public String escribirTexto() {
        return id + "|" + idInsumo + "|" + nombreInsumo + "|" + tipoMovimiento + "|" + 
               cantidad + "|" + stockResultante + "|" + usuario + "|" + observacion + "|" + 
               fecha.format(FORMATO_FECHA);
    }
    
    public static MovimientoInventario leerTexto(String linea) {
        try {
            String[] partes = linea.split("\\|");
            if (partes.length < 9) return null;
            
            int id = Integer.parseInt(partes[0].trim());
            int idInsumo = Integer.parseInt(partes[1].trim());
            String nombreInsumo = partes[2].trim();
            String tipoMovimiento = partes[3].trim();
            double cantidad = Double.parseDouble(partes[4].trim().replace(",", "."));
            double stockResultante = Double.parseDouble(partes[5].trim().replace(",", "."));
            String usuario = partes[6].trim();
            String observacion = partes[7].trim();
            LocalDateTime fecha = LocalDateTime.parse(partes[8].trim(), FORMATO_FECHA);
            
            return new MovimientoInventario(id, idInsumo, nombreInsumo, tipoMovimiento, 
                                           cantidad, stockResultante, usuario, observacion, fecha);
        } catch (Exception e) {
            return null;
        }
    }
}