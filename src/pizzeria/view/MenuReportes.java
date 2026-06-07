package pizzeria.view;

import pizzeria.view.MenuFinanzas;
import pizzeria.view.Consola;
import pizzeria.controller.GestorReportes;
import pizzeria.controller.GestorFinanzas;
import pizzeria.model.EstadoReserva;
import pizzeria.model.Reserva;
import pizzeria.model.Venta;
import pizzeria.model.MetodoPago;
import pizzeria.model.DetalleVenta;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MenuReportes {
    
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private final GestorReportes gestorReportes;
    private final GestorFinanzas gestorFinanzas;
    private final MenuFinanzas menuFinanzas;
    
    public MenuReportes(GestorReportes gestorReportes, GestorFinanzas gestorFinanzas, MenuFinanzas menuFinanzas) {
        this.gestorReportes = gestorReportes;
        this.gestorFinanzas = gestorFinanzas;
        this.menuFinanzas = menuFinanzas;
    }
    
    public void mostrar() {
        boolean enMenu = true;
        while (enMenu) {
            Consola.titulo("MÓDULO DE REPORTES");
            System.out.println(" ── REPORTES DE VENTAS ────────────────────────────────────");
            System.out.println(" 1. Reporte de ventas diario");
            System.out.println(" 2. Reporte de ventas por período");
            System.out.println(" 3. Top productos más vendidos");
            System.out.println(" ── REPORTES DE RESERVAS ──────────────────────────────────");
            System.out.println(" 4. Reporte de reservas diario");
            System.out.println(" 5. Reporte de reservas por período");
            System.out.println(" ── REPORTES GENERALES ────────────────────────────────────");
            System.out.println(" 6. Reporte general diario (ventas + reservas + finanzas)");
            System.out.println(" 7. Reporte general semanal");
            System.out.println(" 8. Reporte general mensual");
            System.out.println(" 9. Reporte personalizado por período");
            System.out.println(" ── ACCESO RÁPIDO A FINANZAS ──────────────────────────────");
            System.out.println(" 10. Ir a reportes financieros");
            System.out.println(" 11. Ver saldo actual y deudas pendientes");
            System.out.println(" 0. Volver al menú principal");
            Consola.separador();
            
            int opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 11);
            
            switch (opcion) {
                case 1 -> reporteVentasDiario();
                case 2 -> reporteVentasPeriodo();
                case 3 -> reporteTopProductos();
                case 4 -> reporteReservasDiario();
                case 5 -> reporteReservasPeriodo();
                case 6 -> reporteGeneralDiario();
                case 7 -> reporteGeneralSemanal();
                case 8 -> reporteGeneralMensual();
                case 9 -> reporteGeneralPersonalizado();
                case 10 -> accesoRapidoFinanzas();
                case 11 -> verResumenFinanciero();
                case 0 -> enMenu = false;
            }
        }
    }
    
    private void reporteVentasDiario() {
        Consola.titulo("REPORTE DE VENTAS DIARIO");
        LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd) [Enter = hoy]: ");
        if (fecha == null) fecha = LocalDate.now();
        
        List<Venta> ventas = gestorReportes.getVentasPorPeriodo(fecha, fecha);
        double total = ventas.stream().mapToDouble(Venta::getTotal).sum();
        
        System.out.println();
        System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
        System.out.printf( " │  REPORTE DE VENTAS DIARIO  —  %s%28s│%n", fecha, "");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        System.out.printf( " │  Total ventas         : %d%41s│%n", ventas.size(), "");
        System.out.printf( " │  Monto total          : Bs. %8.2f%28s│%n", total, "");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        
        if (!ventas.isEmpty()) {
            System.out.println(" │  Detalle por método de pago:                                  │");
            java.util.Map<MetodoPago, Double> porMetodo = ventas.stream()
                    .collect(java.util.stream.Collectors.groupingBy(Venta::getMetodoPago, 
                             java.util.stream.Collectors.summingDouble(Venta::getTotal)));
            
            for (var entry : porMetodo.entrySet()) {
                System.out.printf(" │    %-12s : Bs. %8.2f%31s│%n", 
                        entry.getKey().getNombre(), entry.getValue(), "");
            }
        }
        System.out.println(" └─────────────────────────────────────────────────────────────┘");
        
        // Preguntar si quiere ver el detalle completo
        if (!ventas.isEmpty() && Consola.confirmar("¿Ver detalle completo de las ventas?")) {
            System.out.println();
            for (Venta v : ventas) {
                System.out.println(v);
                System.out.println(" " + "-".repeat(70));
                System.out.println();
            }
        }
        
        Consola.pausar();
    }
    
    private void reporteVentasPeriodo() {
        Consola.titulo("REPORTE DE VENTAS POR PERÍODO");
        
        LocalDate inicio = leerFecha("Fecha inicio (yyyy-MM-dd): ");
        if (inicio == null) {
            System.out.println(" Fecha inválida.");
            Consola.pausar();
            return;
        }
        
        LocalDate fin = leerFecha("Fecha fin (yyyy-MM-dd): ");
        if (fin == null) {
            System.out.println(" Fecha inválida.");
            Consola.pausar();
            return;
        }
        
        List<Venta> ventas = gestorReportes.getVentasPorPeriodo(inicio, fin);
        double total = ventas.stream().mapToDouble(Venta::getTotal).sum();
        
        System.out.println();
        System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
        System.out.printf( " │  REPORTE DE VENTAS  —  %s  a  %s%n", inicio, fin);
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        System.out.printf( " │  Total ventas         : %d%41s│%n", ventas.size(), "");
        System.out.printf( " │  Monto total          : Bs. %8.2f%28s│%n", total, "");
        System.out.println(" └─────────────────────────────────────────────────────────────┘");
        
        if (!ventas.isEmpty() && Consola.confirmar("¿Ver detalle de ventas?")) {
            System.out.println();
            for (Venta v : ventas) {
                System.out.println(v);
                System.out.println(" " + "-".repeat(70));
                System.out.println();
            }
        }
        
        Consola.pausar();
    }
    
    private void reporteTopProductos() {
        Consola.titulo("TOP PRODUCTOS MÁS VENDIDOS");
        
        LocalDate inicio = leerFecha("Fecha inicio (yyyy-MM-dd) [Enter = último mes]: ");
        if (inicio == null) {
            inicio = LocalDate.now().minusMonths(1);
        }
        
        LocalDate fin = leerFecha("Fecha fin (yyyy-MM-dd) [Enter = hoy]: ");
        if (fin == null) {
            fin = LocalDate.now();
        }
        
        gestorReportes.imprimirTopProductos(inicio, fin);
        Consola.pausar();
    }
    
    private void reporteReservasDiario() {
        Consola.titulo("REPORTE DE RESERVAS DIARIO");
        LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd) [Enter = hoy]: ");
        if (fecha == null) fecha = LocalDate.now();
        
        List<Reserva> reservas = gestorReportes.getReservasPorPeriodo(fecha, fecha);
        double total = reservas.stream().mapToDouble(Reserva::calcularTotal).sum();
        
        System.out.println();
        System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
        System.out.printf( " │  REPORTE DE RESERVAS DIARIO  —  %s%27s│%n", fecha, "");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        System.out.printf( " │  Total reservas       : %d%41s│%n", reservas.size(), "");
        System.out.printf( " │  Monto total          : Bs. %8.2f%28s│%n", total, "");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        
        if (!reservas.isEmpty()) {
            System.out.println(" │  Detalle por estado:                                           │");
            java.util.Map<EstadoReserva, Long> porEstado = reservas.stream()
                    .collect(java.util.stream.Collectors.groupingBy(Reserva::getEstado, 
                             java.util.stream.Collectors.counting()));
            
            for (var entry : porEstado.entrySet()) {
                System.out.printf(" │    %-12s : %d%41s│%n", 
                        entry.getKey().name(), entry.getValue(), "");
            }
        }
        System.out.println(" └─────────────────────────────────────────────────────────────┘");
        
        if (!reservas.isEmpty() && Consola.confirmar("¿Ver detalle completo de las reservas?")) {
            System.out.println();
            for (Reserva r : reservas) {
                System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
                System.out.printf(" │  RESERVA #%-4d  |  Cliente: %-20s%21s│%n", 
                        r.getId(), r.getNombreCliente(), "");
                System.out.printf(" │  Teléfono: %-12s  |  Estado: %-12s%28s│%n", 
                        r.getTelefono(), r.getEstado().name(), "");
                System.out.println(" ├─────────────────────────────────────────────────────────────┤");
                System.out.println(" │  DETALLES DEL PEDIDO:                                        │");
                for (DetalleVenta detalle : r.getPedido()) {
                    System.out.printf(" │    • %-22s x%2d  = Bs. %7.2f%23s│%n",
                            detalle.getProducto().getNombre(),
                            detalle.getCantidad(),
                            detalle.getSubTotal(), "");
                }
                System.out.println(" ├─────────────────────────────────────────────────────────────┤");
                System.out.printf(" │  TOTAL: Bs. %8.2f%48s│%n", r.calcularTotal(), "");
                System.out.println(" └─────────────────────────────────────────────────────────────┘");
                System.out.println();
            }
        }
        
        Consola.pausar();
    }
    
    private void reporteReservasPeriodo() {
        Consola.titulo("REPORTE DE RESERVAS POR PERÍODO");
        
        LocalDate inicio = leerFecha("Fecha inicio (yyyy-MM-dd): ");
        if (inicio == null) {
            System.out.println(" Fecha inválida.");
            Consola.pausar();
            return;
        }
        
        LocalDate fin = leerFecha("Fecha fin (yyyy-MM-dd): ");
        if (fin == null) {
            System.out.println(" Fecha inválida.");
            Consola.pausar();
            return;
        }
        
        List<Reserva> reservas = gestorReportes.getReservasPorPeriodo(inicio, fin);
        double total = reservas.stream().mapToDouble(Reserva::calcularTotal).sum();
        
        System.out.println();
        System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
        System.out.printf( " │  REPORTE DE RESERVAS  —  %s  a  %s%n", inicio, fin);
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        System.out.printf( " │  Total reservas       : %d%41s│%n", reservas.size(), "");
        System.out.printf( " │  Monto total          : Bs. %8.2f%28s│%n", total, "");
        System.out.println(" └─────────────────────────────────────────────────────────────┘");
        
        if (!reservas.isEmpty() && Consola.confirmar("¿Ver detalle de reservas?")) {
            System.out.println();
            for (Reserva r : reservas) {
                System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
                System.out.printf(" │  RESERVA #%-4d  |  Cliente: %-20s%21s│%n", 
                        r.getId(), r.getNombreCliente(), "");
                System.out.printf(" │  Teléfono: %-12s  |  Estado: %-12s%28s│%n", 
                        r.getTelefono(), r.getEstado().name(), "");
                System.out.println(" ├─────────────────────────────────────────────────────────────┤");
                System.out.println(" │  DETALLES DEL PEDIDO:                                        │");
                for (DetalleVenta detalle : r.getPedido()) {
                    System.out.printf(" │    • %-22s x%2d  = Bs. %7.2f%23s│%n",
                            detalle.getProducto().getNombre(),
                            detalle.getCantidad(),
                            detalle.getSubTotal(), "");
                }
                System.out.println(" ├─────────────────────────────────────────────────────────────┤");
                System.out.printf(" │  TOTAL: Bs. %8.2f%48s│%n", r.calcularTotal(), "");
                System.out.println(" └─────────────────────────────────────────────────────────────┘");
                System.out.println();
            }
        }
        
        Consola.pausar();
    }
    
    private void reporteGeneralDiario() {
        Consola.titulo("REPORTE GENERAL DIARIO");
        LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd) [Enter = hoy]: ");
        if (fecha == null) fecha = LocalDate.now();
        
        gestorReportes.imprimirReporteGeneralPeriodo(fecha, fecha, "DIARIO");
        Consola.pausar();
    }
    
    private void reporteGeneralSemanal() {
        Consola.titulo("REPORTE GENERAL SEMANAL");
        
        LocalDate inicio = leerFecha("Fecha de inicio (yyyy-MM-dd) [Enter = hace 7 días]: ");
        if (inicio == null) {
            inicio = LocalDate.now().minusDays(6);
        }
        
        LocalDate fin = inicio.plusDays(6);
        gestorReportes.imprimirReporteGeneralPeriodo(inicio, fin, "SEMANAL");
        Consola.pausar();
    }
    
    private void reporteGeneralMensual() {
        Consola.titulo("REPORTE GENERAL MENSUAL");
        
        int mes = Consola.leerEnteroRango("Mes (1-12): ", 1, 12);
        int año = Consola.leerEntero("Año (ej: 2025): ");
        
        try {
            LocalDate inicio = LocalDate.of(año, mes, 1);
            LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
            gestorReportes.imprimirReporteGeneralPeriodo(inicio, fin, "MENSUAL " + mes + "/" + año);
        } catch (Exception e) {
            System.out.println(" Fecha inválida.");
        }
        
        Consola.pausar();
    }
    
    private void reporteGeneralPersonalizado() {
        Consola.titulo("REPORTE GENERAL PERSONALIZADO");
        
        LocalDate inicio = leerFecha("Fecha inicio (yyyy-MM-dd): ");
        if (inicio == null) {
            System.out.println(" Fecha inválida.");
            Consola.pausar();
            return;
        }
        
        LocalDate fin = leerFecha("Fecha fin (yyyy-MM-dd): ");
        if (fin == null) {
            System.out.println(" Fecha inválida.");
            Consola.pausar();
            return;
        }
        
        gestorReportes.imprimirReporteGeneralPeriodo(inicio, fin, "PERSONALIZADO");
        Consola.pausar();
    }
    
    private void accesoRapidoFinanzas() {
        Consola.titulo("ACCESO RÁPIDO A FINANZAS");
        System.out.println(" Abriendo módulo de finanzas...");
        System.out.println();
        
        // Mostrar submenú de opciones financieras rápidas
        boolean enSubMenu = true;
        while (enSubMenu) {
            System.out.println(" ── OPCIONES FINANCIERAS ────────────────────────────────────");
            System.out.println(" 1. Ver saldo actual");
            System.out.println(" 2. Ver historial de movimientos");
            System.out.println(" 3. Ver deudas pendientes");
            System.out.println(" 4. Reporte financiero diario");
            System.out.println(" 5. Reporte financiero semanal");
            System.out.println(" 6. Reporte financiero mensual");
            System.out.println(" 0. Volver a reportes");
            Consola.separador();
            
            int opcion = Consola.leerEnteroRango("Seleccione: ", 0, 6);
            
            switch (opcion) {
                case 1 -> verResumenFinanciero();
                case 2 -> menuFinanzas.verHistorial();
                case 3 -> menuFinanzas.verDeudasPendientes();
                case 4 -> menuFinanzas.reporteDiario();
                case 5 -> menuFinanzas.reporteSemanal();
                case 6 -> menuFinanzas.reporteMensual();
                case 0 -> enSubMenu = false;
            }
        }
    }
    
    private void verResumenFinanciero() {
        Consola.titulo("RESUMEN FINANCIERO RÁPIDO");
        
        double saldo = gestorFinanzas.calcularSaldo();
        double deudaPendiente = gestorFinanzas.totalDeudaPendiente();
        
        System.out.println();
        System.out.println(" ┌──────────────────────────────────────────────────────────────┐");
        System.out.printf( " │  Saldo actual en caja  : Bs. %10.2f      │%n", saldo);
        System.out.printf( " │  Total deuda pendiente : Bs. %10.2f      │%n", deudaPendiente);
        System.out.printf( " │  Saldo disponible real : Bs. %10.2f      │%n", saldo - deudaPendiente);
        System.out.println(" └──────────────────────────────────────────────────────────────┘");
        
        if (deudaPendiente > saldo) {
            System.out.println();
            System.out.println(" ⚠ ALERTA: El saldo actual no cubre todas las deudas pendientes.");
            System.out.printf("   Déficit: Bs. %.2f%n", deudaPendiente - saldo);
        }
        
        Consola.pausar();
    }
    
    private LocalDate leerFecha(String mensaje) {
        System.out.print(" " + mensaje);
        String entrada = Consola.getScanner().nextLine().trim();
        if (entrada.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(entrada, FMT_FECHA);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}