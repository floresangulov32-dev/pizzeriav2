package pizzeria.model;

import pizzeria.model.DetalleVenta;
import pizzeria.model.Reserva;
import pizzeria.model.Venta;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoCocina{
    private int idPedidoCocina;
    private String tipoOrigen;
    private int idOrigen;
    private String nombreCliente;
    private List<DetalleVenta> items;
    private EstadoPedido estado;
    private LocalDateTime fechaIngreso;
    private Venta ventaOrigen;
    private Reserva reservaOrigen;

    public PedidoCocina(int idPedidoCocina, Venta venta){
        this.idPedidoCocina = idPedidoCocina;
        this.tipoOrigen = "VENTA";
        this.idOrigen = venta.getId();
        this.nombreCliente = "VENTA MOSTRADOR";
        this.items = new ArrayList<>(venta.getItems());
        this.estado = EstadoPedido.PENDIENTE;
        this.fechaIngreso = LocalDateTime.now();
        this.ventaOrigen = venta;
        this.reservaOrigen = null;
    }

    public PedidoCocina(int idPedidoCocina, Reserva reserva){
        this.idPedidoCocina = idPedidoCocina;
        this.tipoOrigen = "RESERVA";
        this.idOrigen = reserva.getId();
        this.nombreCliente = reserva.getNombreCliente();
        this.items = new ArrayList<>(reserva.getPedido());
        this.estado = EstadoPedido.PENDIENTE;
        this.fechaIngreso = LocalDateTime.now();
        this.ventaOrigen = null;
        this.reservaOrigen = reserva;
    }

    public int getIdPedidoCocina(){
        return idPedidoCocina;
    }

    public void setIdPedidoCocina(int idPedidoCocina){
        this.idPedidoCocina = idPedidoCocina;
    }

    public String getTipoOrigen(){
        return tipoOrigen;
    }

    public void setTipoOrigen(String tipoOrigen){
        this.tipoOrigen = tipoOrigen;
    }

    public int getIdOrigen(){
        return idOrigen;
    }

    public void setIdOrigen(int idOrigen){
        this.idOrigen = idOrigen;
    }

    public String getNombreCliente(){
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente){
        this.nombreCliente = nombreCliente;
    }

    public List<DetalleVenta> getItems(){
        return items;
    }

    public void setItems(List<DetalleVenta> items){
        this.items = items;
    }

    public EstadoPedido getEstado(){
        return estado;
    }

    public void setEstado(EstadoPedido estado){
        this.estado = estado;
    }

    public LocalDateTime getFechaIngreso(){
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso){
        this.fechaIngreso = fechaIngreso;
    }

    public Venta getVentaOrigen(){
        return ventaOrigen;
    }

    public void setVentaOrigen(Venta ventaOrigen){
        this.ventaOrigen = ventaOrigen;
    }

    public Reserva getReservaOrigen(){
        return reservaOrigen;
    }

    public void setReservaOrigen(Reserva reservaOrigen){
        this.reservaOrigen = reservaOrigen;
    }

    public double calcularTotal(){
        double total = 0;
        for(DetalleVenta d : items){
            total += d.getSubTotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format(
                "PedidoCocina #%d | Origen: %s #%d | Cliente: %s | Estado: %s | Total: Bs.%.2f",
                idPedidoCocina,
                tipoOrigen,
                idOrigen,
                nombreCliente,
                estado,
                calcularTotal()
        );
    }
}