package pizzeria.model;


public enum MetodoPago{
    EFECTIVO("Efectivo"),
    TARJETA("Tarjeta"),
    QR("QR / Transferencia");
 
    private final String nombre;
 
    MetodoPago(String nombre) {
        this.nombre = nombre;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    @Override
    public String toString() {
        return nombre;
    }
}
