/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzeria.model;

/**
 *
 * @author KATANA
 */
public class DetalleCombo {
    private int nroCombo;
    private String descripcion;   // nombres de los productos separados por " + "
    private int cantidad;
    private double precioUnitario;
    private double subTotal;

    public DetalleCombo(Combo combo, int cantidad) {
        this.nroCombo       = combo.getNroCombo();
        this.cantidad       = cantidad;
        this.precioUnitario = combo.getPrecio();
        this.subTotal       = precioUnitario * cantidad;

        // Armar descripción con los nombres de los productos del combo
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < combo.getCombo().size(); i++) {
            if (i > 0) sb.append(" + ");
            sb.append(combo.getCombo().get(i).getNombre());
        }
        this.descripcion = sb.toString();
    }

    // Constructor para reconstruir desde archivo
    public DetalleCombo(int nroCombo, String descripcion, double precioUnitario, int cantidad) {
        this.nroCombo       = nroCombo;
        this.descripcion    = descripcion;
        this.cantidad       = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotal       = precioUnitario * cantidad;
    }

    public int getNroCombo()         { return nroCombo; }
    public String getDescripcion()   { return descripcion; }
    public int getCantidad()         { return cantidad; }
    public double getPrecioUnitario(){ return precioUnitario; }
    public double getSubTotal()      { return subTotal; }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subTotal = precioUnitario * cantidad;
    }

    /**
     * Serializa el detalle de combo para guardar en archivo.
     * Formato: COMBO~nroCombo~descripcion~precioUnitario~cantidad
     */
    public String escribirTexto() {
        return "COMBO~" + nroCombo + "~"
             + descripcion.replace("~", "-") + "~"
             + String.format("%.2f", precioUnitario).replace(",", ".") + "~"
             + cantidad;
    }

    /** Vista para mostrar en pedido actual (lista de ítems). */
    @Override
    public String toString() {
        return String.format("  %-22s x%2d  @Bs.%6.2f  =>  Bs.%7.2f",
                "COMBO #" + nroCombo, cantidad, precioUnitario, subTotal);
    }

    /** Vista detallada para recibo/factura. */
    public String toStringDetallado() {
        return toString() + "\n"
             + String.format("    ( %s )", descripcion);
    }
    
}
