package pizzeria.view;

import pizzeria.model.MovimientoCaja;
import pizzeria.model.TipoMovimiento;
import pizzeria.model.Deuda;
import pizzeria.view.Consola;
import pizzeria.controller.GestorFinanzas;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
public class MenuFinanzas{
    
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final GestorFinanzas gestorFinanzas;

    public MenuFinanzas(GestorFinanzas gestorFinanzas){
        this.gestorFinanzas = gestorFinanzas;
    }
    
    public void mostrar(){
        boolean enMenu = true;
        while(enMenu){
            Consola.titulo("MÓDULO DE FINANZAS");
            System.out.printf(" Saldo actual en caja: S/. %.2f%n",
                    gestorFinanzas.calcularSaldo());
            Consola.separador();
            System.out.println(" ── Caja ─────────────────────────────────");
            System.out.println(" 1. Ver saldo actual y resumen");
            System.out.println(" 2. Ver historial de movimientos");
            System.out.println(" 3. Registrar egreso directo");
            System.out.println(" ── Deudas y compromisos ─────────────────");
            System.out.println(" 4. Registrar compromiso / deuda");
            System.out.println(" 5. Ver deudas pendientes");
            System.out.println(" 6. Pagar una deuda");
            System.out.println(" 7. Ver todas las deudas (incluye pagadas)");
            System.out.println(" ── Reportes ─────────────────────────────");
            System.out.println(" 8. Reporte diario");
            System.out.println(" 9. Reporte semanal");
            System.out.println(" 10. Reporte mensual");
            System.out.println(" 0. Volver al menú principal");
            Consola.separador();

            int opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 10);

            switch (opcion) {
                case 1  -> verSaldo();
                case 2  -> verHistorial();
                case 3  -> registrarEgresoDirecto();
                case 4  -> registrarDeuda();
                case 5  -> verDeudasPendientes();
                case 6  -> pagarDeuda();
                case 7  -> verTodasLasDeudas();
                case 8  -> reporteDiario();
                case 9  -> reporteSemanal();
                case 10 -> reporteMensual();
                case 0  -> enMenu = false;
            }
        }
    }
    
    private void verSaldo(){
        Consola.titulo("ESTADO DE CAJA");
        double saldo = gestorFinanzas.calcularSaldo();
        double deudaPendiente = gestorFinanzas.totalDeudaPendiente();

        System.out.println();
        System.out.println(" ┌──────────────────────────────────────────────┐");
        System.out.printf( " │  Saldo actual en caja  : S/. %10.2f      │%n", saldo);
        System.out.printf( " │  Total deuda pendiente : S/. %10.2f      │%n", deudaPendiente);
        System.out.printf( " │  Saldo disponible real : S/. %10.2f      │%n", saldo - deudaPendiente);
        System.out.println(" └──────────────────────────────────────────────┘");

        if(deudaPendiente > saldo){
            System.out.println();
            System.out.println(" ALERTA: El saldo actual no cubre todas las deudas pendientes.");
        }
        Consola.pausar();
    }
    
    public void verHistorial(){
        Consola.titulo("HISTORIAL DE MOVIMIENTOS");
        System.out.println(" Filtrar por:");
        System.out.println(" 1. Todos los movimientos");
        System.out.println(" 2. Solo ingresos");
        System.out.println(" 3. Solo egresos");
        System.out.println(" 4. Por rango de fechas");
        System.out.println(" 0. Cancelar");
        Consola.separador();

        int opcion = Consola.leerEnteroRango("Seleccione filtro: ", 0, 4);

        List<MovimientoCaja> lista;

        switch(opcion){
            case 0 -> { return; }
            case 1 -> lista = gestorFinanzas.getTodosLosMovimientos();
            case 2 -> lista = gestorFinanzas.getTodosLosMovimientos().stream()
                    .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
                    .collect(java.util.stream.Collectors.toList());
            case 3 -> lista = gestorFinanzas.getTodosLosMovimientos().stream()
                    .filter(m -> m.getTipo() == TipoMovimiento.EGRESO)
                    .collect(java.util.stream.Collectors.toList());
            case 4 -> {
                LocalDate desde = leerFecha("Fecha desde (yyyy-MM-dd): ");
                LocalDate hasta = leerFecha("Fecha hasta (yyyy-MM-dd): ");
                if(desde == null || hasta == null){
                    System.out.println("  [!] Fecha inválida. Operación cancelada.");
                    Consola.pausar();
                    return;
                }
                lista = gestorFinanzas.getMovimientosPeriodo(
                        desde.atStartOfDay(),
                        hasta.plusDays(1).atStartOfDay().minusNanos(1));
            }
            default -> lista = gestorFinanzas.getTodosLosMovimientos();
        }

        gestorFinanzas.imprimirMovimientos(lista);
        Consola.pausar();
    }
    
    private void registrarEgresoDirecto(){
        Consola.titulo("REGISTRAR EGRESO DIRECTO");
        System.out.println(" Use este formulario para gastos no planificados como deuda.");
        System.out.println(" (Para compras de insumos planificadas use 'Registrar deuda')");
        System.out.println();

        double monto = leerMonto("Monto del egreso (S/.): ");
        if(monto <= 0){ 
            Consola.pausar(); return; 
        }

        System.out.println();
        System.out.println(" Categoría del egreso:");
        System.out.println(" 1. Compra de insumo");
        System.out.println(" 2. Salario");
        System.out.println(" 3. Otro");
        int catOp = Consola.leerEnteroRango(" Seleccione: ", 1, 3);
        
        String categoria = switch (catOp){
            case 1 -> MovimientoCaja.CAT_COMPRA_INSUMO;
            case 2 -> MovimientoCaja.CAT_SALARIO;
            default -> MovimientoCaja.CAT_OTRO;
        };

        String descripcion = Consola.leerTexto("Descripción: ");

        System.out.println();
        System.out.printf(" Monto     : S/. %.2f%n", monto);
        System.out.printf(" Categoría : %s%n", categoria);
        System.out.printf(" Descripción: %s%n", descripcion);

        if(!Consola.confirmar("¿Confirmar egreso?")){
            System.out.println(" Operación cancelada.");
            Consola.pausar();
            return;
        }

        MovimientoCaja mov = gestorFinanzas.registrarEgreso(monto, categoria, descripcion);
        if(mov != null){
            System.out.printf("%n Egreso registrado. Nuevo saldo: S/. %.2f%n",
                    gestorFinanzas.calcularSaldo());
        }else{
            System.out.println(" Error al registrar el egreso.");
        }
        Consola.pausar();
    }
    
    private void registrarDeuda(){
        Consola.titulo("REGISTRAR COMPROMISO / DEUDA");
        System.out.println(" Registre aqui compras planificadas o salarios a pagar.");
        System.out.println(" El saldo NO se afecta hasta que efectivamente se pague.");
        System.out.println();

        System.out.println(" Tipo de deuda:");
        System.out.println(" 1. Compra de insumo");
        System.out.println(" 2. Salario de empleado");
        System.out.println(" 3. Otro");
        int tipoOp = Consola.leerEnteroRango(" Seleccione: ", 1, 3);
        String tipo = switch(tipoOp){
            case 1 -> Deuda.TIPO_COMPRA_INSUMO;
            case 2 -> Deuda.TIPO_SALARIO;
            default -> Deuda.TIPO_OTRO;
        };

        String descripcion = Consola.leerTexto("Descripcion (ej: 10 kg harina): ");
        double monto = leerMonto("Monto total (S/.): ");
        if(monto <= 0){ 
            Consola.pausar(); return; 
        }

        String proveedor = Consola.leerTexto(
                tipo.equals(Deuda.TIPO_SALARIO) ? "Nombre del empleado: " : "Proveedor: ");

        LocalDate fechaVence = leerFecha("Fecha de vencimiento (yyyy-MM-dd): ");
        if(fechaVence == null){
            System.out.println(" Fecha invalida. Operacion cancelada.");
            Consola.pausar();
            return;
        }

        System.out.println();
        System.out.printf(" Tipo        : %s%n", tipo);
        System.out.printf(" Descripción : %s%n", descripcion);
        System.out.printf(" Monto       : S/. %.2f%n", monto);
        System.out.printf(" Proveedor   : %s%n", proveedor);
        System.out.printf(" Vence       : %s%n", fechaVence);

        if(!Consola.confirmar("¿Confirmar registro de deuda?")){
            System.out.println(" Operacion cancelada.");
            Consola.pausar();
            return;
        }

        Deuda d = gestorFinanzas.registrarDeuda(tipo, descripcion, monto, proveedor,
                fechaVence.atStartOfDay());
        if(d != null){
            System.out.printf("%n Deuda registrada con ID %d. El saldo no fue afectado.%n",
                    d.getId());
        }else{
            System.out.println(" Error al registrar la deuda (monto invalido).");
        }
        Consola.pausar();
    }
    
    public void verDeudasPendientes(){
        Consola.titulo("DEUDAS PENDIENTES");
        gestorFinanzas.imprimirDeudasPendientes();
        Consola.pausar();
    }
    
    
    private void pagarDeuda(){
        Consola.titulo("PAGAR DEUDA");
        gestorFinanzas.imprimirDeudasPendientes();

        List<Deuda> pendientes = gestorFinanzas.getDeudasPendientes();
        if(pendientes.isEmpty()){
            Consola.pausar();
            return;
        }

        int idDeuda = Consola.leerEntero("ID de la deuda a pagar (0 = cancelar): ");
        if(idDeuda == 0){
            return;    
        }

        Deuda d = gestorFinanzas.buscarDeudaPorId(idDeuda);
        if(d == null){
            System.out.println(" No se encontrO una deuda con ese ID.");
            Consola.pausar();
            return;
        }
        if(!d.esPendiente()){
            System.out.println(" Esa deuda ya fue pagada.");
            Consola.pausar();
            return;
        }

        double saldo = gestorFinanzas.calcularSaldo();
        System.out.println();
        System.out.println(" Deuda a pagar: " + d);
        System.out.printf(" Saldo actual : S/. %.2f%n", saldo);

        if(saldo < d.getMontoTotal()){
            System.out.printf(" ADVERTENCIA: El saldo actual (S/. %.2f) es insuficiente%n", saldo);
            System.out.printf(" para cubrir esta deuda (S/. %.2f).%n", d.getMontoTotal());
            if(!Consola.confirmar("¿Desea pagar de todas formas (saldo quedará negativo)?")){
                System.out.println(" Operación cancelada.");
                Consola.pausar();
                return;
            }
        }

        if(!Consola.confirmar("¿Confirmar pago de la deuda?")){
            System.out.println(" Operacion cancelada.");
            Consola.pausar();
            return;
        }

        MovimientoCaja mov = gestorFinanzas.pagarDeuda(idDeuda);
        if(mov != null){
            System.out.printf("%n Deuda pagada. Egreso registrado (ID mov. %d).%n",
                    mov.getId());
            System.out.printf("  Nuevo saldo en caja: S/. %.2f%n",
                    gestorFinanzas.calcularSaldo());
        } else {
            System.out.println(" Error al procesar el pago.");
        }
        Consola.pausar();
    }
    
    private void verTodasLasDeudas(){
        Consola.titulo("TODAS LAS DEUDAS");
        List<Deuda> todas = gestorFinanzas.getTodasLasDeudas();
        if(todas.isEmpty()){
            System.out.println(" No hay deudas registradas.");
        }else{
            System.out.println();
            System.out.println(" " + "─".repeat(90));
            for(Deuda d : todas){
                System.out.println("  " + d);
            }
            System.out.println(" " + "─".repeat(90));
            System.out.printf(" Total registros: %d%n", todas.size());
        }
        Consola.pausar();
    }
    
    public void reporteDiario(){
        Consola.titulo("REPORTE DIARIO");
        System.out.println(" Deje en blanco para ver el reporte de HOY.");
        String input = Consola.leerTextoOpcional("Fecha (yyyy-MM-dd) [Enter = hoy]: ");

        LocalDate fecha;
        if(input.isEmpty()){
            fecha = LocalDate.now();
        }else{
            fecha = parseFecha(input);
            if(fecha == null){
                System.out.println("  [!] Formato de fecha invalido. Use yyyy-MM-dd.");
                Consola.pausar();
                return;
            }
        }

        gestorFinanzas.reporteDiario(fecha);
        Consola.pausar();
    }
    
    public void reporteSemanal(){
        Consola.titulo("REPORTE SEMANAL");
        System.out.println(" Deje en blanco para ver los ultimos 7 dias.");
        String input = Consola.leerTextoOpcional("Fecha de inicio (yyyy-MM-dd) [Enter = hace 7 días]: ");

        LocalDate inicio;
        if(input.isEmpty()){
            inicio = LocalDate.now().minusDays(6);
        }else{
            inicio = parseFecha(input);
            if(inicio == null){
                System.out.println(" Formato de fecha invalido. Use yyyy-MM-dd.");
                Consola.pausar();
                return;
            }
        }
        LocalDate fin = inicio.plusDays(6);
        gestorFinanzas.reportePeriodo(inicio, fin, "SEMANAL");
        Consola.pausar();
    }
    
    public void reporteMensual(){
        Consola.titulo("REPORTE MENSUAL");
        int mes = Consola.leerEnteroRango("Mes (1-12)         : ", 1, 12);
        int año = Consola.leerEntero("Año (ej: 2025)     : ");

        try{
            LocalDate inicio = LocalDate.of(año, mes, 1);
            LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
            gestorFinanzas.reportePeriodo(inicio, fin, "MENSUAL " + mes + "/" + año);
        }catch(Exception e){
            System.out.println(" Mes o año invalido.");
        }
        Consola.pausar();
    }
    
    private double leerMonto(String mensaje){
        while(true){
            System.out.print(" " + mensaje);
            String entrada = Consola.getScanner().nextLine().trim();
            if(entrada.isEmpty()){
                System.out.println("  [!] El monto no puede estar vacio.");
                continue;
            }
            try{
                double monto = Double.parseDouble(entrada.replace(",", "."));
                if(monto <= 0){
                    System.out.println(" El monto debe ser mayor que cero.");
                    continue;
                }
                return monto;
            }catch(NumberFormatException e){
                System.out.println(" Ingrese un número valido (ej: 25.50).");
            }
        }
    }
    
    private LocalDate leerFecha(String mensaje){
        System.out.print(" " + mensaje);
        String entrada = Consola.getScanner().nextLine().trim();
        return parseFecha(entrada);
    }

    private LocalDate parseFecha(String texto){
        try{
            return LocalDate.parse(texto, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch (DateTimeParseException e){
            return null;
        }
    }
}
