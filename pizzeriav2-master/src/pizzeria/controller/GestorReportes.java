package pizzeria.controller;

//import pizzeria.controller.GestorFinanzas;
//import pizzeria.controller.GestorVenta;
import pizzeria.model.EstadoReserva;
import pizzeria.model.EstadoPedido;
import pizzeria.model.Reserva;
import pizzeria.model.Venta;
import pizzeria.model.Producto;
import pizzeria.model.DetalleVenta;
import pizzeria.model.MetodoPago;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestorReportes{
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private final GestorVenta gestorVenta;
    private final GestorReserva gestorReserva;
    private final GestorFinanzas gestorFinanzas;
    
    public GestorReportes(GestorVenta gestorVenta, GestorReserva gestorReserva, GestorFinanzas gestorFinanzas){
        this.gestorVenta = gestorVenta;
        this.gestorReserva = gestorReserva;
        this.gestorFinanzas = gestorFinanzas;
    }
    
    public List<Venta> getVentasPorPeriodo(LocalDate inicio, LocalDate fin){
        LocalDateTime desde = inicio.atStartOfDay();
        LocalDateTime hasta = fin.plusDays(1).atStartOfDay().minusNanos(1);
        
        return gestorVenta.getListaVenta().stream()
                .filter(v -> !v.getFecha().isBefore(desde))
                .filter(v -> !v.getFecha().isAfter(hasta))
                .collect(Collectors.toList());
    }
    
    public List<Venta> getVentasPorEstado(EstadoPedido estado){
        return gestorVenta.getListaVenta().stream()
                .filter(v -> v.getEstado() == estado)
                .collect(Collectors.toList());
    }
    
    public double getTotalVentasPeriodo(LocalDate inicio, LocalDate fin){
        return getVentasPorPeriodo(inicio, fin).stream()
                .mapToDouble(Venta::getTotal)
                .sum();
    }
    
    public long getCantidadVentasPeriodo(LocalDate inicio, LocalDate fin){
        return getVentasPorPeriodo(inicio, fin).size();
    }
    
    public Producto getProductoMasVendido(LocalDate inicio, LocalDate fin){
        java.util.Map<Integer, Integer> contadorProductos = new java.util.HashMap<>();
        
        for(Venta venta : getVentasPorPeriodo(inicio, fin)){
            for(DetalleVenta detalle : venta.getItems()){
                int id = detalle.getProducto().getID();
                contadorProductos.put(id, contadorProductos.getOrDefault(id, 0) + detalle.getCantidad());
            }
        }
        
        if(contadorProductos.isEmpty()){
            return null;
        }
        
        int idMasVendido = contadorProductos.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .get()
                .getKey();
                
        return null;
    }
    
    public List<Reserva> getReservasPorPeriodo(LocalDate inicio, LocalDate fin){
        LocalDateTime desde = inicio.atStartOfDay();
        LocalDateTime hasta = fin.plusDays(1).atStartOfDay().minusNanos(1);
        
        return gestorReserva.getListaReservas().stream()
                .filter(r -> !r.getFechaReserva().isBefore(desde))
                .filter(r -> !r.getFechaReserva().isAfter(hasta))
                .collect(Collectors.toList());
    }
    
    public List<Reserva> getReservasPorEstado(EstadoReserva estado){
        return gestorReserva.getListaReservas().stream()
                .filter(r -> r.getEstado() == estado)
                .collect(Collectors.toList());
    }
    
    public double getTotalReservasPeriodo(LocalDate inicio, LocalDate fin){
        return getReservasPorPeriodo(inicio, fin).stream()
                .mapToDouble(Reserva::calcularTotal)
                .sum();
    }
    
    public long getCantidadReservasPeriodo(LocalDate inicio, LocalDate fin){
        return getReservasPorPeriodo(inicio, fin).size();
    }
    
    public double getIngresoTotalPeriodo(LocalDate inicio, LocalDate fin){
        return getTotalVentasPeriodo(inicio, fin) + getTotalReservasPeriodo(inicio, fin);
    }
    
    public long getTotalPedidosPeriodo(LocalDate inicio, LocalDate fin){
        return getCantidadVentasPeriodo(inicio, fin) + getCantidadReservasPeriodo(inicio, fin);
    }
    
    public void imprimirReporteVentasDiario(LocalDate fecha){
        List<Venta> ventas = getVentasPorPeriodo(fecha, fecha);
        double total = ventas.stream().mapToDouble(Venta::getTotal).sum();
        
        System.out.println();
        System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
        System.out.printf( " │  REPORTE DE VENTAS DIARIO  —  %s%28s│%n", fecha, "");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        System.out.printf( " │  Total ventas         : %d%41s│%n", ventas.size(), "");
        System.out.printf( " │  Monto total          : Bs. %8.2f%28s│%n", total, "");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        
        if(!ventas.isEmpty()){
            System.out.println(" │  Detalle por método de pago:                                  │");
            java.util.Map<MetodoPago, Double> porMetodo = ventas.stream()
                    .collect(Collectors.groupingBy(Venta::getMetodoPago, 
                             Collectors.summingDouble(Venta::getTotal)));
            
            for(var entry : porMetodo.entrySet()){
                System.out.printf(" │    %-12s : Bs. %8.2f%31s│%n", 
                        entry.getKey().getNombre(), entry.getValue(), "");
            }
        }
        System.out.println(" └─────────────────────────────────────────────────────────────┘");
    }
    
    public void imprimirReporteReservasDiario(LocalDate fecha){
        List<Reserva> reservas = getReservasPorPeriodo(fecha, fecha);
        double total = reservas.stream().mapToDouble(Reserva::calcularTotal).sum();
        
        System.out.println();
        System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
        System.out.printf( " │  REPORTE DE RESERVAS DIARIO  —  %s%27s│%n", fecha, "");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        System.out.printf( " │  Total reservas       : %d%41s│%n", reservas.size(), "");
        System.out.printf( " │  Monto total          : Bs. %8.2f%28s│%n", total, "");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        
        if(!reservas.isEmpty()){
            System.out.println(" │  Detalle por estado:                                           │");
            java.util.Map<EstadoReserva, Long> porEstado = reservas.stream()
                    .collect(Collectors.groupingBy(Reserva::getEstado, Collectors.counting()));
            
            for(var entry : porEstado.entrySet()){
                System.out.printf(" │    %-12s : %d%41s│%n", 
                        entry.getKey().name(), entry.getValue(), "");
            }
        }
        System.out.println(" └─────────────────────────────────────────────────────────────┘");
    }
    
    public void imprimirReporteGeneralPeriodo(LocalDate inicio, LocalDate fin, String titulo){
        List<Venta> ventas = getVentasPorPeriodo(inicio, fin);
        List<Reserva> reservas = getReservasPorPeriodo(inicio, fin);
        
        double totalVentas = ventas.stream().mapToDouble(Venta::getTotal).sum();
        double totalReservas = reservas.stream().mapToDouble(Reserva::calcularTotal).sum();
        double totalGeneral = totalVentas + totalReservas;
        
        double egresos = gestorFinanzas.sumaEgresos(inicio.atStartOfDay(), 
                fin.plusDays(1).atStartOfDay().minusNanos(1));
        double gananciaNeta = totalGeneral - egresos;
        
        System.out.println();
        System.out.println(" ╔═══════════════════════════════════════════════════════════════╗");
        System.out.printf( " ║  REPORTE GENERAL %s  —  %s  a  %s%n", titulo, inicio, fin);
        System.out.println(" ╠═══════════════════════════════════════════════════════════════╣");
        System.out.println(" ║  ── VENTAS ──────────────────────────────────────────────────║");
        System.out.printf( " ║     Ventas realizadas     : %d%43s║%n", ventas.size(), "");
        System.out.printf( " ║     Monto ventas          : Bs. %8.2f%35s║%n", totalVentas, "");
        System.out.println(" ║  ── RESERVAS ────────────────────────────────────────────────║");
        System.out.printf( " ║     Reservas registradas  : %d%43s║%n", reservas.size(), "");
        System.out.printf( " ║     Monto reservas        : Bs. %8.2f%35s║%n", totalReservas, "");
        System.out.println(" ║  ── TOTALES ─────────────────────────────────────────────────║");
        System.out.printf( " ║     Total pedidos         : %d%43s║%n", 
                ventas.size() + reservas.size(), "");
        System.out.printf( " ║     Ingreso bruto         : Bs. %8.2f%35s║%n", totalGeneral, "");
        System.out.println(" ║  ── FINANZAS ─────────────────────────────────────────────────║");
        System.out.printf( " ║     Egresos del período   : Bs. %8.2f%35s║%n", egresos, "");
        System.out.printf( " ║     Ganancia neta         : Bs. %8.2f%35s║%n", gananciaNeta, "");
        System.out.println(" ╚═══════════════════════════════════════════════════════════════╝");
    }
    
    public void imprimirTopProductos(LocalDate inicio, LocalDate fin){
        java.util.Map<Integer, Integer> contadorProductos = new java.util.HashMap<>();
        
        for(Venta venta : getVentasPorPeriodo(inicio, fin)){
            for(DetalleVenta detalle : venta.getItems()){
                int id = detalle.getProducto().getID();
                contadorProductos.put(id, contadorProductos.getOrDefault(id, 0) + detalle.getCantidad());
            }
        }
        
        if(contadorProductos.isEmpty()){
            System.out.println(" No hay ventas en el período seleccionado.");
            return;
        }
        
        List<java.util.Map.Entry<Integer, Integer>> sorted = new ArrayList<>(contadorProductos.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        System.out.println();
        System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
        System.out.println(" │  TOP PRODUCTOS MÁS VENDIDOS                                 │");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        
        int limit = Math.min(5, sorted.size());
        for(int i = 0; i < limit; i++){
            var entry = sorted.get(i);
            System.out.printf(" │  %d. Producto ID: %-4d  -  Cantidad: %d%37s│%n", 
                    i + 1, entry.getKey(), entry.getValue(), "");
        }
        System.out.println(" └─────────────────────────────────────────────────────────────┘");
    }
}