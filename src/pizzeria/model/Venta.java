package pizzeria.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Venta {
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private int id;
    private LocalDateTime fecha;
    private ArrayList<DetalleVenta> items;
    private ArrayList<DetalleCombo> combos;   // combos como elementos aparte
    private double total;
    private int idCajero;
    private MetodoPago metodoPago;
    private double cambio;
    private EstadoPedido estado;
    private LocalDateTime horaEntrega;
    private String nombreCliente;

    public Venta(int id, int idCajero) {
        this.id = id;
        this.idCajero = idCajero;
        this.fecha = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.combos = new ArrayList<>();
        this.total = 0.0;
        this.cambio = 0.0;
        this.estado = EstadoPedido.PENDIENTE;
        this.metodoPago = MetodoPago.EFECTIVO;
        this.nombreCliente = "";
    }

    public ArrayList<DetalleCombo> getCombos() {
        return combos;
    }

    public void setCombos(ArrayList<DetalleCombo> combos) {
        this.combos = combos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }


    public ArrayList<DetalleVenta> getItems() {
        return items;
    }

    public void setItems(ArrayList<DetalleVenta> items) {
        this.items = items;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


    public int getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }


    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }


    public double getCambio() {
        return cambio;
    }

    public void setCambio(double cambio) {
        this.cambio = cambio;
    }


    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }


    public LocalDateTime getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(LocalDateTime horaEntrega) {
        this.horaEntrega = horaEntrega;
    }


    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        if (nombreCliente == null) {
            this.nombreCliente = "";
        } else {
            this.nombreCliente = nombreCliente.trim();
        }
    }

    // Recalcula el total de la venta sumando subtotales de productos individuales y combos
    public void calcularTotal() {
        total = 0.0;
        for (DetalleVenta d : items) {
            total += d.getSubTotal();
        }
        for (DetalleCombo c : combos) {
            total += c.getSubTotal();
        }
    }

    /** Indica si el pedido no tiene ni productos ni combos. */
    public boolean estaVacio() {
        return items.isEmpty() && combos.isEmpty();
    }

    // Calcula el cambio del cliente en caso de pago en efectivo
    public double calcularCambio(double montoPagado) {
        calcularTotal();
        cambio = montoPagado - total;
        if (cambio < 0) {
            cambio = -1;
        }
        return cambio;
    }

    // Convierte la venta a texto para guardarla en archivo
    public String escribirTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("|")
          .append(fecha.format(FORMATO_FECHA)).append("|")
          .append(idCajero).append("|")
          .append(metodoPago.name()).append("|")
          .append(String.format("%.2f", total).replace(",", ".")).append("|")
          .append(String.format("%.2f", cambio).replace(",", ".")).append("|")
          .append(estado.name()).append("|")
          .append(nombreCliente.replace("|", "/")).append("|");

        boolean primero = true;

        for (DetalleVenta d : items) {
            if (!primero) sb.append(";");
            sb.append(d.escribirTexto());
            primero = false;
        }

        for (DetalleCombo c : combos) {
            if (!primero) sb.append(";");
            sb.append(c.escribirTexto());
            primero = false;
        }

        return sb.toString();
    }

    // Devuelve una representación visual completa de la venta
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" Venta #%-4d | %s | Cajero: %d | %s%n",
                id, fecha.format(FORMATO_FECHA), idCajero, metodoPago));

        if (nombreCliente != null && !nombreCliente.isBlank()) {
            sb.append(String.format(" Cliente: %s%n", nombreCliente));
        }

        sb.append(String.format(" Estado: %-20s%n", estado));
        sb.append(" " + "-".repeat(58) + "\n");

        for (DetalleVenta d : items) {
            sb.append(d.toString()).append("\n");
        }

        for (DetalleCombo c : combos) {
            sb.append(c.toStringDetallado()).append("\n");
        }

        sb.append(" " + "-".repeat(58) + "\n");
        sb.append(String.format(" %-38s TOTAL: Bs.%7.2f%n", "", total));
        if (cambio > 0) {
            sb.append(String.format(" %-38s CAMBIO: Bs.%6.2f%n", "", cambio));
        }
        return sb.toString();
    }
    
    public void descontarInsumos(Inventario inventario, Menu menu){
        // Descontar insumos de productos individuales
        for(DetalleVenta detalle : items){
            Producto productoCompleto = menu.buscarProducto(detalle.getProducto().getID());
            if(productoCompleto == null) continue;
            for(int idInsumo : productoCompleto.getIngredientes()){
                Insumo insumo = inventario.buscarId(idInsumo);
                if(insumo == null) continue;
                double aDescontar = insumo.getCantidadPorPizza() * detalle.getCantidad();
                inventario.descontarStock(idInsumo, aDescontar);
            }
        }

        // Descontar insumos de los productos dentro de cada combo
        for(DetalleCombo dc : combos){
            Combo combo = menu.buscarCombo(dc.getNroCombo());
            if(combo == null) continue;
            for(Producto p : combo.getCombo()){
                Producto productoCompleto = menu.buscarProducto(p.getID());
                if(productoCompleto == null) productoCompleto = p;
                for(int idInsumo : productoCompleto.getIngredientes()){
                    Insumo insumo = inventario.buscarId(idInsumo);
                    if(insumo == null) continue;
                    double aDescontar = insumo.getCantidadPorPizza() * dc.getCantidad();
                    inventario.descontarStock(idInsumo, aDescontar);
                }
            }
        }
    }
}
