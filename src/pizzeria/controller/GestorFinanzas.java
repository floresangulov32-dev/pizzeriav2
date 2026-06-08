package pizzeria.controller;

import pizzeria.model.MovimientoCaja;
import pizzeria.model.TipoMovimiento;
import pizzeria.model.Deuda;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GestorFinanzas{
    
    private static final String ARCHIVO_CAJA = "resources/data/caja.txt";
    private static final String ARCHIVO_DEUDAS = "resources/data/deudas.txt";
    
    
    private final List<MovimientoCaja> movimientos = new ArrayList<>();
    private final List<Deuda> deudas = new ArrayList<>();
    private int proximoIdMov = 1;
    private int proximoIdDeuda = 1;
    
    public double calcularSaldo(){
        double saldo = 0;
        for(MovimientoCaja m : movimientos){
            if(m.getTipo() == TipoMovimiento.INGRESO){
                saldo += m.getMonto();
            }else{
                saldo -= m.getMonto();
            }
        }
        return saldo;
    }
    
    public double sumaIngresos(LocalDateTime desde, LocalDateTime hasta){
        return movimientos.stream()
            .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
            .filter(m -> !m.getFecha().isBefore(desde))
            .filter(m -> hasta == null || !m.getFecha().isAfter(hasta))
            .mapToDouble(MovimientoCaja::getMonto)
            .sum();
    }
    
    public double sumaEgresos(LocalDateTime desde, LocalDateTime hasta){
        return movimientos.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.EGRESO)
                .filter(m -> !m.getFecha().isBefore(desde))
                .filter(m -> hasta == null || !m.getFecha().isAfter(hasta))
                .mapToDouble(MovimientoCaja::getMonto)
                .sum();
    }
    
    public List<MovimientoCaja> getMovimientosPeriodo(LocalDateTime desde, LocalDateTime hasta){
        return movimientos.stream()
                .filter(m -> !m.getFecha().isBefore(desde))
                .filter(m -> hasta == null || !m.getFecha().isAfter(hasta))
                .sorted((a,b) -> b.getFecha().compareTo(a.getFecha()))
                .collect(Collectors.toList());
    }
    
    public List<MovimientoCaja> getTodosLosMovimientos(){
        return movimientos.stream()
                .sorted((a,b) -> b.getFecha().compareTo(a.getFecha()))
                .collect(Collectors.toList());
    }
    
    
    public MovimientoCaja registrarIngreso(double monto, String categoria,String descripcion){
        if(monto <= 0){
            return null;
        }
        MovimientoCaja m = new MovimientoCaja(
            proximoIdMov++, TipoMovimiento.INGRESO,
            monto, LocalDateTime.now(), categoria, descripcion);
        movimientos.add(m);
        
        guardarMovimientos();
        
        return m;
    }
    
    public MovimientoCaja registrarEgreso(double monto, String categoria, String descripcion){
        if(monto <= 0){
            return null;
        }
        MovimientoCaja m = new MovimientoCaja(
                proximoIdMov ++, TipoMovimiento.EGRESO,monto, LocalDateTime.now(),
                categoria, descripcion);
        movimientos.add(m);
        
        guardarMovimientos();
        
        return m;
    }
    
    public Deuda registrarDeuda(String tipo, String descripcion, double montoTotal,
                                String proveedor, LocalDateTime fechaCompromiso){
        
        if(montoTotal <= 0){
            return null;
        }
        Deuda d = new Deuda(proximoIdDeuda++, tipo, descripcion, montoTotal, proveedor, fechaCompromiso);
        deudas.add(d);
        
        guardarDeudas();
        
        return d;
    }
    
    public MovimientoCaja pagarDeuda(int idDeuda){
        Deuda d = buscarDeudaPorId(idDeuda);
        if(d == null || !d.esPendiente()){
            return null;
        }
        MovimientoCaja mov = registrarEgreso(
                d.getMontoTotal(),
                d.getTipo(),
                "Pago deuda #" + d.getId() + " - " + d.getDescripcion());
        d.setEstado(Deuda.ESTADO_PAGADA);
        return mov;
    }
    
    public List<Deuda> getDeudasPendientes(){
        return deudas.stream()
                .filter(Deuda::esPendiente)
                .collect(Collectors.toList());
    }
    
    
    public List<Deuda> getTodasLasDeudas(){
        return new ArrayList<>(deudas);
    }
    
    public Deuda buscarDeudaPorId(int id){
        for(Deuda d : deudas){
            if(d.getId() == id){
                return d;    
            }
        }
        return null;
    }
    
    public double totalDeudaPendiente(){
        return deudas.stream()
                .filter(Deuda::esPendiente)
                .mapToDouble(Deuda::getMontoTotal)
                .sum();
    }
    
    public void reporteDiario(LocalDate dia){
        LocalDateTime inicio = dia.atStartOfDay();
        LocalDateTime fin = dia.plusDays(1).atStartOfDay().minusNanos(1);
        
        double ingresos = sumaIngresos(inicio, fin);
        double egresos = sumaEgresos(inicio, fin);
        List<MovimientoCaja> movsDia = getMovimientosPeriodo(inicio, fin);
        
        System.out.println();
        System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
        System.out.printf( " │  REPORTE DIARIO  —  %s%26s│%n", dia, "");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        System.out.printf( " │  Ingresos del día   : S/. %8.2f%27s│%n", ingresos, "");
        System.out.printf( " │  Egresos del día    : S/. %8.2f%27s│%n", egresos, "");
        System.out.printf( " │  Balance del día    : S/. %8.2f%27s│%n", ingresos - egresos, "");
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        System.out.printf( " │  Saldo total en caja: S/. %8.2f%27s│%n", calcularSaldo(), "");
        System.out.println(" └─────────────────────────────────────────────────────────────┘");
        
        if(movsDia.isEmpty()){
            System.out.println("  No hay movimientos registrados en este día.");            
        }else{
            System.out.println();
            System.out.println(" Detalle de movimientos:");
            System.out.println(" " + "-".repeat(80));
            for(MovimientoCaja m : movsDia){
                System.out.println(" " + "-".repeat(80));
            }
        }
    }
    
    public void reportePeriodo(LocalDate inicio, LocalDate fin, String etiqueta){
        LocalDateTime desde = inicio.atStartOfDay();
        LocalDateTime hasta = fin.plusDays(1).atStartOfDay().minusNanos(1);
        
        double ingresos = sumaIngresos(desde, hasta);
        double egresos = sumaEgresos(desde, hasta);
        List<MovimientoCaja> movs = getMovimientosPeriodo(desde, hasta);
        
        System.out.println();
        System.out.println(" ┌─────────────────────────────────────────────────────────────┐");
        System.out.printf( " │  REPORTE %s  —  %s  a  %s%n", etiqueta, inicio, fin);
        System.out.println(" ├─────────────────────────────────────────────────────────────┤");
        System.out.printf( " │  Total ingresos  : S/. %8.2f%28s│%n", ingresos, "");
        System.out.printf( " │  Total egresos   : S/. %8.2f%28s│%n", egresos, "");
        System.out.printf( " │  Ganancia neta   : S/. %8.2f%28s│%n", ingresos - egresos, "");
        System.out.printf( " │  Movimientos     : %d%41s│%n", movs.size(), "");
        System.out.println(" └─────────────────────────────────────────────────────────────┘");
    }
    
    public void imprimirMovimientos(List<MovimientoCaja> lista){
        if(lista.isEmpty()){
            System.out.println(" No hay movimientos en este periodo.");
            return;
        }
        System.out.println();
        System.out.println(" " + "─".repeat(90));
        System.out.printf(" %-6s %-8s %10s  %-16s %-15s %s%n",
                "ID", "TIPO", "MONTO", "FECHA", "CATEGORIA", "DESCRIPCION");
        System.out.println(" " + "─".repeat(90));
        for(MovimientoCaja m : lista){
            System.out.printf(" %-6d %-8s S/.%8.2f  %s  %-15s %s%n",
                    m.getId(), m.getTipo().name(), m.getMonto(),
                    m.getFecha().format(MovimientoCaja.FORMATO_FECHA),
                    m.getCategoria(), m.getDescripcion());
        }
        System.out.println(" " + "─".repeat(90));
        System.out.printf(" Total registros: %d%n%n", lista.size());
    }
    
    public void imprimirDeudasPendientes(){
        List<Deuda> pendientes = getDeudasPendientes();
        if(pendientes.isEmpty()){
            System.out.println(" No hay deudas pendientes.");
            return;
        }
        double saldo = calcularSaldo();
        System.out.println();
        System.out.println(" " + "-".repeat(70));
        System.out.printf("  %-6s %-13s %10s  %-16s %-20s %s%n",
                "ID", "TIPO", "MONTO", "VENCE", "PROVEEDOR/EMPL.", "DESCRIPCION");
        System.out.println("  " + "─".repeat(90));
        for(Deuda d : pendientes){
            String alerta = (saldo >= d.getMontoTotal()) ? "" : " [SALDO INSUF.]";
            System.out.printf(" %-6d %-13s S/.%8.2f  %s  %-20s %s%s%n",
                    d.getId(), d.getTipo(), d.getMontoTotal(),
                    d.getFechaCompromiso().format(MovimientoCaja.FORMATO_FECHA),
                    d.getProveedor(), d.getDescripcion(), alerta);
        }
        System.out.println(" " + "─".repeat(90));
        System.out.printf(" Total deuda pendiente: S/. %.2f   |   Saldo actual: S/. %.2f%n%n",
                totalDeudaPendiente(), saldo);
    }
    
    public void cargarArchivos(){
        cargarMovimientos();
        cargarDeudas();
    }
    
    public void guardarArchivos(){
        guardarMovimientos();
        guardarDeudas();
    }
    
    private void cargarMovimientos(){
        File archivo = new File(ARCHIVO_CAJA);
        if(!archivo.exists()){
            System.out.println(" No se encontro caja.txt. Iniciando sin historial.");
            return;
        }
        movimientos.clear();
        int maxId = 0;
        
        try(BufferedReader br = new BufferedReader(new FileReader(archivo))){
            String linea;
            int numLinea = 0;
            while((linea = br.readLine()) != null){
                numLinea++;
                linea = linea.trim();
                
                if(linea.isEmpty() || linea.startsWith("#")){
                    continue;    
                }
                
                MovimientoCaja m = MovimientoCaja.leerTexto(linea);
                if(m != null){
                    movimientos.add(m);
                    if(m.getId() > maxId){
                        maxId = m.getId();    
                    }
                }else{
                    System.out.printf(" Línea %d ignorada en caja.txt%n", numLinea);
                }
            }
            proximoIdMov = maxId + 1;
            System.out.printf(" %d movimiento(s) cargado(s) desde caja.txt%n",
                    movimientos.size());
        }catch(IOException e){
            System.out.println(" No se pudo leer caja.txt: "+ ARCHIVO_CAJA + e.getMessage());
        }
    }
    
    private void guardarMovimientos(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_CAJA))) {
            bw.write("# Sistema Pizzeria — Movimientos de caja");
            bw.newLine();
            bw.write("# Formato: id|TIPO|monto|yyyy-MM-dd HH:mm|categoria|descripcion");
            bw.newLine();
            for(MovimientoCaja m : movimientos){
                bw.write(m.escribirTexto());
                bw.newLine();
            }
            System.out.printf(" %d movimiento(s) guardado(s) en caja.txt%n",
                    movimientos.size(), ARCHIVO_CAJA);
        }catch(IOException e){
            System.out.println(" No se pudo guardar caja.txt: " + e.getMessage());
        }
    }
    
    
    private void cargarDeudas(){
        File archivo = new File(ARCHIVO_DEUDAS);
        if(!archivo.exists()){
            System.out.println(" No se encontro deudas.txt. Iniciando sin deudas.");
            return;
        }
        
        deudas.clear();
        int maxId = 0;
        
        try(BufferedReader br = new BufferedReader(new FileReader(archivo))){
            
            String linea;
            int numLinea = 0;
            
            while((linea = br.readLine()) != null){
                numLinea++;
                linea = linea.trim();
                
                if(linea.isEmpty() || linea.startsWith("#")){
                    continue;    
                }
                
                Deuda d = Deuda.leerTexto(linea);
                if(d != null){
                    deudas.add(d);
                    if(d.getId() > maxId){
                        maxId = d.getId();    
                    }
                }else{
                    System.out.printf(" Línea %d ignorada en deudas.txt%n", numLinea, linea);
                }
            }
            
            proximoIdDeuda = maxId + 1;
            System.out.printf(" %d deuda(s) cargada(s) desde deudas.txt%n",
                    deudas.size());
                    
        }catch (IOException e){
            System.out.println(" No se pudo leer deudas.txt: "+  ARCHIVO_DEUDAS +": "  + e.getMessage());
        }
    }
    
    private void guardarDeudas(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_DEUDAS))){
            bw.write("# Sistema Pizzeria — Deudas y compromisos");
            bw.newLine();
            bw.write("# Formato: id|tipo|descripcion|monto|proveedor|yyyy-MM-dd HH:mm|estado");
            bw.newLine();
            for(Deuda d : deudas){
                bw.write(d.escribirTexto());
                bw.newLine();
            }
            System.out.printf(" %d deuda(s) guardada(s) en deudas.txt%n",
                    deudas.size());
        }catch(IOException e){
            System.out.println(" No se pudo guardar deudas.txt: " + ARCHIVO_DEUDAS + ": " + e.getMessage());
        }
    }
}
