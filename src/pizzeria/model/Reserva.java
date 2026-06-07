package pizzeria.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private int id;
    private String nombreCliente;
    private String telefono;
    private LocalDateTime fechaReserva;
    private List<DetalleVenta> pedido;
    private EstadoReserva estado;

    public Reserva() {
        this.pedido = new ArrayList<>();
        this.estado = EstadoReserva.PENDIENTE;
    }

    public Reserva(int id, String nombreCliente, String telefono, LocalDateTime fechaReserva) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.fechaReserva = fechaReserva;
        this.pedido = new ArrayList<>();
        this.estado = EstadoReserva.PENDIENTE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }


    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }


    public List<DetalleVenta> getPedido() {
        return pedido;
    }

    public void setPedido(List<DetalleVenta> pedido) {
        this.pedido = pedido;
    }


    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    // Agrega un detalle al pedido de la reserva
    public void agregarDetalle(DetalleVenta detalle) {
        pedido.add(detalle);
    }

    // Elimina un detalle del pedido según la posición indicada
    public void eliminarDetalle(int indice) {
        if (indice >= 0 && indice < pedido.size()) {
            pedido.remove(indice);
        }
    }

    // Calcula el total de la reserva sumando los subtotales de cada detalle
    public double calcularTotal() {
        double total = 0;
        for (DetalleVenta detalle : pedido) {
            total += detalle.getSubTotal();
        }
        return total;
    }

    // Devuelve un resumen de la reserva para mostrar en consola
    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fechaReserva=" + fechaReserva +
                ", estado=" + estado +
                ", total=" + calcularTotal() +
                '}';
    }
}