package pizzeria.controller;


import pizzeria.model.EstadoReserva;
import pizzeria.model.EstadoPedido;
import pizzeria.model.Menu;
import pizzeria.model.PedidoCocina;
import pizzeria.model.Inventario;
import pizzeria.model.Insumo;
import pizzeria.model.Reserva;
import pizzeria.model.Venta;
import pizzeria.model.Producto;
import pizzeria.model.DetalleVenta;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class GestorCocina{
    private Queue<PedidoCocina> colaPendientes;
    private List<PedidoCocina> enPreparacion;
    private List<PedidoCocina> listos;
    private int siguienteIdPedido;
    private Inventario inventario;
    private Menu menu;

    public GestorCocina(Inventario inventario, Menu menu){
        this.colaPendientes = new LinkedList<>();
        this.enPreparacion = new ArrayList<>();
        this.listos = new ArrayList<>();
        this.siguienteIdPedido = 1;
        this.inventario = inventario;
        this.menu = menu;
    }

    public Queue<PedidoCocina> getColaPendientes(){
        return colaPendientes;
    }

    public List<PedidoCocina> getEnPreparacion(){
        return enPreparacion;
    }

    public List<PedidoCocina> getListos(){
        return listos;
    }

    public boolean agregarVentaACocina(Venta venta){
        if(venta == null){
            return false;
        }

        if(venta.getEstado() == EstadoPedido.CANCELADO ||
            venta.getEstado() == EstadoPedido.ENTREGADO){
            return false;
        }

        if(yaExistePedido("VENTA", venta.getId())){
            return false;
        }

        PedidoCocina pedido = new PedidoCocina(siguienteIdPedido++, venta);
        colaPendientes.offer(pedido);
        venta.setEstado(EstadoPedido.PENDIENTE);
        return true;
    }

    public boolean agregarReservaACocina(Reserva reserva){
        if(reserva == null){
            return false;
        }

        if(reserva.getEstado() == EstadoReserva.CANCELADA ||
            reserva.getEstado() == EstadoReserva.ENTREGADA){
            return false;
        }

        if(yaExistePedido("RESERVA", reserva.getId())){
            return false;
        }

        PedidoCocina pedido = new PedidoCocina(siguienteIdPedido++, reserva);
        colaPendientes.offer(pedido);
        return true;
    }

    public PedidoCocina tomarSiguientePedido(){
        PedidoCocina pedido = colaPendientes.poll();

        if(pedido != null){
            pedido.setEstado(EstadoPedido.EN_PREPARACION);
            enPreparacion.add(pedido);

            if(pedido.getVentaOrigen() != null){
                pedido.getVentaOrigen().setEstado(EstadoPedido.EN_PREPARACION);
            }

            if(pedido.getReservaOrigen() != null){
                pedido.getReservaOrigen().setEstado(EstadoReserva.EN_COCINA);
            }
        }

        return pedido;
    }

    public PedidoCocina buscarEnPreparacion(int idPedidoCocina){
        for(PedidoCocina pedido : enPreparacion){
            if(pedido.getIdPedidoCocina() == idPedidoCocina){
                return pedido;
            }
        }
        return null;
    }

    public boolean marcarComoListo(int idPedidoCocina){
        PedidoCocina pedido = buscarEnPreparacion(idPedidoCocina);

        if(pedido == null){
            return false;
        }

        enPreparacion.remove(pedido);
        pedido.setEstado(EstadoPedido.LISTO);
        listos.add(pedido);

        if(pedido.getVentaOrigen() != null){
            pedido.getVentaOrigen().setEstado(EstadoPedido.LISTO);
        }

        if(pedido.getReservaOrigen() != null){
            pedido.getReservaOrigen().setEstado(EstadoReserva.LISTA);
        }

        return true;
    }

    public PedidoCocina buscarListo(int idPedidoCocina){
        for(PedidoCocina pedido : listos){
            if(pedido.getIdPedidoCocina() == idPedidoCocina){
                return pedido;
            }
        }
        return null;
    }

    public boolean marcarComoEntregado(int idPedidoCocina){
        PedidoCocina pedido = buscarListo(idPedidoCocina);

        if(pedido == null){
            return false;
        }

        listos.remove(pedido);
        pedido.setEstado(EstadoPedido.ENTREGADO);

        if(pedido.getVentaOrigen() != null){
            pedido.getVentaOrigen().setEstado(EstadoPedido.ENTREGADO);
            pedido.getVentaOrigen().setHoraEntrega(java.time.LocalDateTime.now());
        }

        if(pedido.getReservaOrigen() != null){
            pedido.getReservaOrigen().setEstado(EstadoReserva.ENTREGADA);
        }

        descontarStockPedido(pedido);
        return true;
    }

    public boolean cancelarPedidoPorVenta(int idVenta){
        PedidoCocina pedido = buscarPorOrigen("VENTA", idVenta);

        if(pedido == null){
            return false;
        }

        if(pedido.getEstado() == EstadoPedido.ENTREGADO){
            return false;
        }

        colaPendientes.remove(pedido);
        enPreparacion.remove(pedido);
        listos.remove(pedido);
        pedido.setEstado(EstadoPedido.CANCELADO);

        if(pedido.getVentaOrigen() != null){
            pedido.getVentaOrigen().setEstado(EstadoPedido.CANCELADO);
        }

        return true;
    }

    public boolean cancelarPedidoPorReserva(int idReserva){
        PedidoCocina pedido = null;

        for(PedidoCocina p : colaPendientes){
            if(p.getTipoOrigen().equals("RESERVA") && p.getIdOrigen() == idReserva){
                pedido = p;
                break;
            }
        }

        if(pedido == null){
            return false;
        }

        colaPendientes.remove(pedido);
        pedido.setEstado(EstadoPedido.CANCELADO);
        return true;
    }

    public void reconstruirDesdeEstados(List<Venta> ventas, List<Reserva> reservas){
        colaPendientes.clear();
        enPreparacion.clear();
        listos.clear();
        siguienteIdPedido = 1;

        if(ventas != null){
            for(Venta venta : ventas){
                if(venta.getEstado() == EstadoPedido.PENDIENTE){
                    PedidoCocina p = new PedidoCocina(siguienteIdPedido++, venta);
                    p.setEstado(EstadoPedido.PENDIENTE);
                    colaPendientes.offer(p);
                }else if (venta.getEstado() == EstadoPedido.EN_PREPARACION){
                    PedidoCocina p = new PedidoCocina(siguienteIdPedido++, venta);
                    p.setEstado(EstadoPedido.EN_PREPARACION);
                    enPreparacion.add(p);
                }else if (venta.getEstado() == EstadoPedido.LISTO){
                    PedidoCocina p = new PedidoCocina(siguienteIdPedido++, venta);
                    p.setEstado(EstadoPedido.LISTO);
                    listos.add(p);
                }
            }
        }

        if(reservas != null){
            for(Reserva reserva : reservas){
                if(reserva.getEstado() == EstadoReserva.EN_COCINA){
                    PedidoCocina p = new PedidoCocina(siguienteIdPedido++, reserva);
                    p.setEstado(EstadoPedido.PENDIENTE);
                    colaPendientes.offer(p);
                }else if (reserva.getEstado() == EstadoReserva.LISTA){
                    PedidoCocina p = new PedidoCocina(siguienteIdPedido++, reserva);
                    p.setEstado(EstadoPedido.LISTO);
                    listos.add(p);
                }
            }
        }
    }

    public void verPendientes(){
        if(colaPendientes.isEmpty()){
            System.out.println(" No hay pedidos pendientes en cocina.");
            return;
        }

        System.out.println("=== PEDIDOS PENDIENTES ===");
        for(PedidoCocina pedido : colaPendientes){
            System.out.println(pedido);
        }
    }

    public void verEnPreparacion(){
        if(enPreparacion.isEmpty()){
            System.out.println(" No hay pedidos en preparación.");
            return;
        }

        System.out.println("=== PEDIDOS EN PREPARACIÓN ===");
        for(PedidoCocina pedido : enPreparacion){
            System.out.println(pedido);
        }
    }

    public void verListos(){
        if(listos.isEmpty()){
            System.out.println(" No hay pedidos listos.");
            return;
        }

        System.out.println("=== PEDIDOS LISTOS ===");
        for(PedidoCocina pedido : listos){
            System.out.println(pedido);
        }
    }

    private boolean yaExistePedido(String tipoOrigen, int idOrigen){
        return buscarPorOrigen(tipoOrigen, idOrigen) != null;
    }

    private PedidoCocina buscarPorOrigen(String tipoOrigen, int idOrigen){
        for(PedidoCocina pedido : colaPendientes){
            if(pedido.getTipoOrigen().equals(tipoOrigen) && pedido.getIdOrigen() == idOrigen){
                return pedido;
            }
        }

        for(PedidoCocina pedido : enPreparacion){
            if(pedido.getTipoOrigen().equals(tipoOrigen) && pedido.getIdOrigen() == idOrigen){
                return pedido;
            }
        }

        for(PedidoCocina pedido : listos){
            if(pedido.getTipoOrigen().equals(tipoOrigen) && pedido.getIdOrigen() == idOrigen){
                return pedido;
            }
        }

        return null;
    }

    private void descontarStockPedido(PedidoCocina pedido){
        for(DetalleVenta detalle : pedido.getItems()){
            Producto productoCompleto = menu.buscarProducto(detalle.getProducto().getID());

            if(productoCompleto == null){
                productoCompleto = detalle.getProducto();
            }

            for(int idInsumo : productoCompleto.getIngredientes()){
                Insumo insumo = inventario.buscarId(idInsumo);
                if(insumo == null){
                    continue;
                }

                double cantidadADescontar = insumo.getCantidadPorPizza() * detalle.getCantidad();
                inventario.descontarStock(idInsumo, cantidadADescontar);
            }
        }

        inventario.guardarArchivo("resources/data/Insumos.txt");
    }
}