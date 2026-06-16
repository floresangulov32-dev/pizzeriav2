package pizzeria.controller;

import pizzeria.model.EstadoPedido;
import pizzeria.model.Menu;
import pizzeria.model.MovimientoCaja;
import pizzeria.model.Inventario;
import pizzeria.model.Insumo;
import pizzeria.model.Reserva;
import pizzeria.model.Venta;
import pizzeria.model.Producto;
import pizzeria.model.Combo;
import pizzeria.model.TipoProducto;
import pizzeria.model.MetodoPago;
import pizzeria.model.DetalleVenta;
import pizzeria.model.DetalleCombo;
import pizzeria.view.Consola;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GestorVenta {

    private static final String ARCHIVO_VENTAS = "resources/data/ventas.txt";;

    private ArrayList<Venta> listaVenta;
    private Menu menu;
    private Venta ventaActual;
    private Inventario inventario;
    private GestorFinanzas gestorFinanzas;
    private GestorReserva gestorReserva;
    private GestorCocina gestorCocina;

    public GestorVenta(Menu menu, Inventario inventario, GestorFinanzas gestorFinanzas,
                       GestorReserva gestorReserva, GestorCocina gestorCocina) {
        listaVenta = new ArrayList<>();
        this.menu = menu;
        this.inventario = inventario;
        this.gestorFinanzas = gestorFinanzas;
        this.gestorReserva = gestorReserva;
        this.gestorCocina = gestorCocina;
    }

    public ArrayList<Venta> getListaVenta() {
        return listaVenta;
    }

    public Venta getVentaActual() {
        return ventaActual;
    }
    
/*
 * Valida todo el stock requerido por el pedido actual.
 *
 * Incluye:
 * - Productos individuales.
 * - Combos.
 * - Cantidades actuales.
 * - Ingredientes repetidos y acumulados.
 *
 * Retorna null cuando el pedido puede confirmarse.
 * Retorna un mensaje cuando el pedido no es válido
 * o no existe stock suficiente.
 */
public String validarStockPedidoActualGUI() {
    if (ventaActual == null) {
        return "No existe un pedido actual.";
    }

    if (ventaActual.estaVacio()) {
        return "El pedido está vacío. Agregue al menos un producto o combo.";
    }

    HashMap<Integer, Double> requeridoPorInsumo =
            new HashMap<>();

    String errorPedido =
            acumularConsumoPedidoActual(requeridoPorInsumo);

    if (errorPedido != null) {
        return errorPedido;
    }

    return validarDisponibilidadAcumulada(
            requeridoPorInsumo,
            "confirmar el pedido"
    );
}

    // Muestra el menú principal del módulo de ventas
    public void menuVentas(int idCajero) {
        int opcion;
        ArrayList<Producto> productos = menu.getProductos();

        do {
            Consola.titulo("GESTIÓN DE VENTAS");
            System.out.println(" 1. Nuevo pedido");
            System.out.println(" 2. Ver historial de ventas");
            System.out.println(" 3. Buscar venta por ID");
            System.out.println(" 4. Cancelar venta pagada");
            System.out.println(" 0. Volver");
            Consola.separador();

            opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 4);

            switch (opcion) {
                case 1 -> menuNuevoPedido(idCajero, productos);
                case 2 -> verHistorial();
                case 3 -> buscarVentaMenu();
                case 4 -> cancelarVentaPagadaMenu();
                case 0 -> System.out.println(" Volviendo al menú principal...");
            }
        } while (opcion != 0);
    }

    // Inicia el armado de un pedido nuevo
    private void menuNuevoPedido(int idCajero, ArrayList<Producto> productos) {
        crearVenta(idCajero);
        Consola.titulo("NUEVO PEDIDO #" + ventaActual.getId());

        int opcion;
        do {
            ventaActual.calcularTotal();

            System.out.printf("%n Total actual: Bs.%.2f%n", ventaActual.getTotal());
            Consola.separador();
            System.out.println(" 1. Agregar producto");
            System.out.println(" 2. Agregar combo");
            System.out.println(" 3. Quitar producto");
            System.out.println(" 4. Ver items actuales");
            System.out.println(" 5. Cobrar y definir tipo de pedido");
            System.out.println(" 0. Cancelar armado del pedido");
            Consola.separador();

            opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 5);

            switch (opcion) {
                case 1 -> menuAgregarItem(productos);
                case 2 -> menuAgregarCombo();
                case 3 -> menuQuitarItem();
                case 4 -> verItemsActuales();
                case 5 -> {
                    if (ventaActual.estaVacio()) {
                        System.out.println(" No hay productos en el pedido.");
                    } else {
                        boolean finalizado = menuCobrarYDefinirDestino();
                        if (finalizado) {
                            opcion = 0;
                        }
                    }
                }
                case 0 -> {
                    if (Consola.confirmar("¿Está seguro de cancelar el pedido actual?")) {
                        cancelarArmadoPedido();
                    }
                }
            }
        } while (opcion != 0);
    }

    // Permite agregar un producto individual al pedido actual
    private void menuAgregarItem(ArrayList<Producto> productos) {
        Consola.titulo("AGREGAR PRODUCTO");

        for (Producto p : productos) {
            System.out.printf("  [%2d] %-22s Bs.%.2f%n",
                    p.getID(), p.getNombre(), p.getPrecio());
        }

        Consola.separador();
        Integer idProd = Consola.leerEnteroCancelable("ID del producto");
        if (idProd == null) {
            System.out.println(" Registro de producto cancelado.");
            return;
        }

        if (idProd <= 0) {
            System.out.println(" ID inválido.");
            Consola.pausar();
            return;
        }

        Producto prod = menu.buscarProducto(idProd);

        if (prod == null) {
            System.out.println(" Producto no encontrado.");
            Consola.pausar();
            return;
        }

        Integer cant = Consola.leerEnteroCancelable("Cantidad");
        if (cant == null) {
            System.out.println(" Registro de producto cancelado.");
            return;
        }

        if (cant <= 0) {
            System.out.println(" La cantidad debe ser mayor a 0.");
            Consola.pausar();
            return;
        }
        
        // Verificar stock antes de agregar
        String mensajeStock = validarStockProducto(prod, cant);

        if (mensajeStock != null) {
            Consola.separador();
            System.out.println(mensajeStock);
            Consola.separador();
            Consola.pausar();
            return;
        }
        
        agregarItem(prod, cant);

        System.out.printf(" Agregado: %dx %s%n", cant, prod.getNombre());
        Consola.pausar();
    }

    // Permite agregar un combo al pedido actual como un elemento único con su precio total
    private void menuAgregarCombo() {
        ArrayList<Combo> combos = menu.getCombos();

        if (combos.isEmpty()) {
            System.out.println(" No hay combos disponibles.");
            Consola.pausar();
            return;
        }

        Consola.titulo("AGREGAR COMBO");

        for (Combo c : combos) {
            System.out.printf("  [%2d] Combo #%d  —  Bs.%.2f%n",
                    c.getNroCombo(), c.getNroCombo(), c.getPrecio());
            for (Producto p : c.getCombo()) {
                System.out.printf("        • %s%n", p.getNombre());
            }
        }

        Consola.separador();

        Integer nro = Consola.leerEnteroCancelable("Nro. de combo");
        if (nro == null) {
            System.out.println(" Registro de combo cancelado.");
            return;
        }

        if (nro <= 0) {
            System.out.println(" Número de combo inválido.");
            return;
        }

        Combo combo = menu.buscarCombo(nro);

        if (combo == null) {
            System.out.println(" Combo " + nro + " no encontrado.");
            Consola.pausar();
            return;
        }

        Integer cant = Consola.leerEnteroCancelable("Cantidad de combos");
        if (cant == null) {
            System.out.println(" Registro de combo cancelado.");
            return;
        }

        if (cant <= 0) {
            System.out.println(" La cantidad debe ser mayor a 0.");
            Consola.pausar();
            return;
        }

        // Verificar stock de los productos del combo antes de agregar
        String mensajeStock = validarStockCombo(combo, cant);

        if (mensajeStock != null) {
            Consola.separador();
            System.out.println(mensajeStock);
            Consola.separador();
            Consola.pausar();
            return;
        }

        // Agregar el combo como un elemento único — sin modificar precios de productos
        agregarCombo(combo, cant);

        System.out.printf(" Combo #%d agregado x%d — Bs.%.2f c/u%n", nro, cant, combo.getPrecio());
        Consola.pausar();
    }

    // Permite quitar un item o combo del pedido actual
    private void menuQuitarItem() {
        if (ventaActual.estaVacio()) {
            System.out.println(" No hay productos en el pedido.");
            return;
        }

        Consola.titulo("QUITAR PRODUCTO / COMBO");
        mostrarItemsSinPausa();

        ArrayList<DetalleVenta> items = ventaActual.getItems();
        ArrayList<DetalleCombo> combos = ventaActual.getCombos();
        int totalItems = items.size() + combos.size();

        Integer index = Consola.leerEnteroCancelable(
                "Índice del producto/combo a quitar (1 - " + totalItems + ")");

        if (index == null) {
            System.out.println(" Acción cancelada.");
            return;
        }

        if (index < 1 || index > totalItems) {
            System.out.println(" Índice inválido.");
            Consola.pausar();
            return;
        }

        String nombre;
        if (index <= items.size()) {
            nombre = items.get(index - 1).getProducto().getNombre();
        } else {
            int comboIdx = index - items.size() - 1;
            nombre = "COMBO #" + combos.get(comboIdx).getNroCombo();
        }

        if (Consola.confirmar("¿Está seguro de quitar este elemento?")) {
            if (index <= items.size()) {
                quitarItem(index - 1);
            } else {
                int comboIdx = index - items.size() - 1;
                combos.remove(comboIdx);
                ventaActual.calcularTotal();
            }
            System.out.println(" Eliminado: " + nombre);
        } else {
            System.out.println(" No se eliminó ningún producto.");
        }

        Consola.pausar();
    }

    // Muestra los items del pedido actual
    private void verItemsActuales() {
        Consola.titulo("ITEMS DEL PEDIDO ACTUAL");
        if (ventaActual.estaVacio()) {
            System.out.println(" (sin productos)");
        } else {
            int idx = 1;
            for (DetalleVenta d : ventaActual.getItems()) {
                System.out.printf(" [%d] %s%n", idx++, d);
            }
            for (DetalleCombo c : ventaActual.getCombos()) {
                System.out.printf(" [%d] %s%n", idx++, c.toStringDetallado());
            }

            Consola.separador();
            System.out.printf(" Total: Bs.%.2f%n", ventaActual.getTotal());
        }
        Consola.pausar();
    }

    // Muestra los items actuales sin pausar
    private void mostrarItemsSinPausa() {
        if (ventaActual.estaVacio()) {
            System.out.println(" (sin productos)");
            return;
        }

        int idx = 1;
        for (DetalleVenta d : ventaActual.getItems()) {
            System.out.printf(" [%d] %s%n", idx++, d);
        }
        for (DetalleCombo c : ventaActual.getCombos()) {
            System.out.printf(" [%d] %s%n", idx++, c.toStringDetallado());
        }

        Consola.separador();
        System.out.printf(" Total: Bs.%.2f%n", ventaActual.getTotal());
    }

    // Cobra el pedido actual y permite elegir si será venta inmediata o reserva
    private boolean menuCobrarYDefinirDestino() {
        Consola.titulo("COBRO DEL PEDIDO");

        ventaActual.calcularTotal();

        if (!hayStockSuficienteParaPedido(ventaActual.getItems())) {
            System.out.println(" No hay stock suficiente para confirmar este pedido.");
            System.out.println(" Revise el inventario o modifique el pedido.");
            Consola.pausar();
            return false;
        }

        System.out.println(ventaActual);

        String nombreCliente = Consola.leerTextoOpcionalCancelable("Nombre del cliente");
        if (nombreCliente == null) {
            System.out.println(" Cobro cancelado.");
            return false;
        }
        ventaActual.setNombreCliente(nombreCliente);

        System.out.println(" TIPO DE ATENCIÓN DEL PEDIDO:");
        System.out.println("  1. Atender ahora (venta inmediata)");
        System.out.println("  2. Registrar como reserva");
        System.out.println("  0. Cancelar cobro");
        int tipoPedido = Consola.leerEnteroRango("Seleccione una opción: ", 0, 2);

        if (tipoPedido == 0) {
            if (Consola.confirmar("¿Está seguro de cancelar el cobro?")) {
                System.out.println(" Cobro cancelado.");
                return false;
            }
        }

        MetodoPago[] metodos = MetodoPago.values();
        System.out.println(" MÉTODO DE PAGO:");
        for (int i = 0; i < metodos.length; i++) {
            System.out.printf("  [%d] %s%n", i + 1, metodos[i].getNombre());
        }
        System.out.println("  [0] Cancelar cobro");

        int opPago = Consola.leerEnteroRango("Seleccione método: ", 0, metodos.length);

        if (opPago == 0) {
            if (Consola.confirmar("¿Está seguro de cancelar el cobro?")) {
                System.out.println(" Cobro cancelado.");
                return false;
            } else {
                return menuCobrarYDefinirDestino();
            }
        }

        MetodoPago metodo = metodos[opPago - 1];

        double montoPagado = ventaActual.getTotal();
        if (metodo == MetodoPago.EFECTIVO) {
            while (true) {
                Double monto = Consola.leerDoubleCancelable(
                        String.format("Total: Bs.%.2f  —  Monto recibido: Bs.", ventaActual.getTotal()));

                if (monto == null) {
                    System.out.println(" Cobro cancelado.");
                    return false;
                }

                if (monto < ventaActual.getTotal()) {
                    System.out.println(" Monto insuficiente, intente de nuevo.");
                } else {
                    montoPagado = monto;
                    break;
                }
            }
        }

        if (tipoPedido == 1) {
            finalizarVentaInmediata(metodo, montoPagado);
        } else {
            registrarReservaPagada(metodo, montoPagado);
        }

        return true;
    }

    // Convierte el pedido actual en una venta inmediata pagada y la envía a cocina
    private void finalizarVentaInmediata(MetodoPago metodo, double montoPagado) {
        if (ventaActual == null) {
            return;
        }

        ventaActual.setMetodoPago(metodo);
        ventaActual.calcularTotal();
        ventaActual.calcularCambio(montoPagado);
        ventaActual.setEstado(EstadoPedido.PENDIENTE);

        Venta ventaFinalizada = ventaActual;
        listaVenta.add(ventaFinalizada);
        
        //descuenta el stock al confirmar la venta
        ventaFinalizada.descontarInsumos(inventario, menu);
        
        registrarCobro(ventaFinalizada.getTotal(), metodo, ventaFinalizada.getCambio(),
                "Venta inmediata #" + ventaFinalizada.getId());

        gestorCocina.agregarVentaACocina(ventaFinalizada);

        guardarArchivo();
        preguntarYGenerarFactura(ventaFinalizada);
        ventaActual = null;
    }
    
    
    public Venta finalizarVentaInmediataGUI(MetodoPago metodo, double montoPagado, String nombreCliente) {
        if (ventaActual == null || ventaActual.estaVacio()) {
            return null;
        }

        ventaActual.setNombreCliente(nombreCliente);
        ventaActual.setMetodoPago(metodo);
        ventaActual.calcularTotal();
        ventaActual.calcularCambio(montoPagado);
        ventaActual.setEstado(EstadoPedido.PENDIENTE);

        if (ventaActual.getCambio() < 0) {
            return null;
        }

        Venta ventaFinalizada = ventaActual;

        listaVenta.add(ventaFinalizada);

        ventaFinalizada.descontarInsumos(inventario, menu);

        registrarCobro(
                ventaFinalizada.getTotal(),
                metodo,
                ventaFinalizada.getCambio(),
                "Venta inmediata #" + ventaFinalizada.getId()
        );

        if (gestorCocina != null) {
            gestorCocina.agregarVentaACocina(ventaFinalizada);
        }

        guardarArchivo();

        ventaActual = null;

        return ventaFinalizada;
    }

    // Convierte el pedido actual en una reserva pagada
    private void registrarReservaPagada(MetodoPago metodo, double montoPagado) {
        if (ventaActual == null) {
            return;
        }

        String nombreReserva = ventaActual.getNombreCliente();

        if (nombreReserva == null || nombreReserva.isBlank()) {
            nombreReserva = Consola.leerTextoCancelable("Nombre del cliente para la reserva");
            if (nombreReserva == null) {
                System.out.println(" Registro de reserva cancelado.");
                return;
            }
        }

        String telefono = Consola.leerTextoCancelable("Teléfono");
        if (telefono == null) {
            System.out.println(" Registro de reserva cancelado.");
            return;
        }

        LocalDateTime fechaReserva = LocalDateTime.now();

        ventaActual.setMetodoPago(metodo);
        ventaActual.calcularTotal();
        ventaActual.calcularCambio(montoPagado);

        List<DetalleVenta> copiaPedido = copiarItems(ventaActual.getItems());
        ArrayList<DetalleCombo> copiaCombos = copiarCombos(ventaActual.getCombos());
        Reserva reserva = gestorReserva.nuevaReserva(nombreReserva, telefono, fechaReserva, copiaPedido);

        registrarCobro(reserva.calcularTotal(), metodo, ventaActual.getCambio(),
                "Reserva #" + reserva.getId());

        gestorReserva.guardarArchivo("reservas.txt");
        generarConfirmacionReserva(reserva);

        ventaActual = null;
    }
    
    //// Metodo para la GUI
    public Reserva registrarReservaPagadaGUI(MetodoPago metodo, double montoPagado,
                                         String nombreCliente, String telefono,
                                         LocalDateTime fechaReserva) {
        if (ventaActual == null || ventaActual.estaVacio()) {
            return null;
        }

        if (gestorReserva == null) {
            return null;
        }

        
        
        
        

        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            nombreCliente = "Sin nombre";
        }

        if (telefono == null || telefono.trim().isEmpty()) {
            return null;
        }

        ventaActual.setNombreCliente(nombreCliente);
        ventaActual.setMetodoPago(metodo);
        ventaActual.calcularTotal();
        ventaActual.calcularCambio(montoPagado);

        if (ventaActual.getCambio() < 0) {
            return null;
        }

        //List<DetalleVenta> copiaPedido = copiarItems(ventaActual.getItems());
        List<DetalleVenta> copiaPedido = copiarPedidoParaReservaConCombos(ventaActual);
        
        Reserva reserva = gestorReserva.nuevaReserva(
                nombreCliente,
                telefono.trim(),
                fechaReserva,
                copiaPedido
        );

        registrarCobro(
                reserva.calcularTotal(),
                metodo,
                ventaActual.getCambio(),
                "Reserva #" + reserva.getId()
        );

        gestorReserva.guardarArchivo("resources/data/reservas.txt");

        ventaActual = null;

        return reserva;
    }

    // Registra en caja el ingreso y, si corresponde, el egreso por cambio
    private void registrarCobro(double total, MetodoPago metodo, double cambio, String descripcion) {
        if (gestorFinanzas != null) {
            gestorFinanzas.registrarIngreso(
                    total,
                    MovimientoCaja.CAT_VENTA,
                    descripcion
            );

            if (metodo == MetodoPago.EFECTIVO && cambio > 0) {
                gestorFinanzas.registrarEgreso(
                        cambio,
                        MovimientoCaja.CAT_OTRO,
                        "Cambio entregado de " + descripcion
                );
            }
        }
    }

    // Muestra el historial de ventas registradas
    private void verHistorial() {
        Consola.titulo("HISTORIAL DE VENTAS");
        if (listaVenta.isEmpty()) {
            System.out.println(" No hay ventas registradas.");
        } else {
            for (Venta v : listaVenta) {
                String cliente = (v.getNombreCliente() == null || v.getNombreCliente().isBlank())
                        ? "Sin nombre"
                        : v.getNombreCliente();

                System.out.printf(" #%-4d | %s | Cliente: %-15s | Bs.%7.2f | %-20s | [%s]%n",
                        v.getId(),
                        v.getFecha().format(Venta.FORMATO_FECHA),
                        cliente,
                        v.getTotal(),
                        v.getMetodoPago(),
                        v.getEstado());
            }
        }
        Consola.pausar();
    }

    // Busca una venta por ID y la muestra completa
    private void buscarVentaMenu() {
        Consola.titulo("BUSCAR VENTA");

        Integer id = Consola.leerEnteroCancelable("ID de venta");
        if (id == null) {
            System.out.println(" Búsqueda cancelada.");
            return;
        }

        Venta v = buscarVentaPorId(id);

        if (v == null) {
            System.out.println(" Venta no encontrada.");
        } else {
            System.out.println(v);
        }

        Consola.pausar();
    }

    // Permite cancelar una venta pagada antes de ser entregada y registra reembolso
    private void cancelarVentaPagadaMenu() {
        Consola.titulo("CANCELAR VENTA PAGADA");

        Integer id = Consola.leerEnteroCancelable("ID de venta");
        if (id == null) {
            System.out.println(" Acción cancelada.");
            return;
        }

        Venta venta = buscarVentaPorId(id);
        if (venta == null) {
            System.out.println(" No existe una venta con ese ID.");
            Consola.pausar();
            return;
        }

        System.out.println(venta);

        if (!Consola.confirmar("¿Está seguro de cancelar esta venta pagada?")) {
            System.out.println(" No se realizó ninguna cancelación.");
            Consola.pausar();
            return;
        }

        boolean cancelada = cancelarVentaPagada(id);

        if (cancelada) {
            System.out.println(" Venta cancelada correctamente y reembolso registrado.");
        } else {
            System.out.println(" No se pudo cancelar la venta. Puede no existir o ya estar entregada/cancelada.");
        }

        Consola.pausar();
    }

    // Crea una nueva venta temporal para comenzar a armar un pedido
    public void crearVenta(int idCajero) {
        int nuevoId;
        if (listaVenta.isEmpty()) {
            nuevoId = 1;
        } else {
            nuevoId = listaVenta.get(listaVenta.size() - 1).getId() + 1;
        }

        ventaActual = new Venta(nuevoId, idCajero);
    }

    // Agrega un combo al pedido actual, o aumenta su cantidad si ya estaba
    public void agregarCombo(Combo combo, int cantidad) {
        if (ventaActual == null) return;

        for (DetalleCombo dc : ventaActual.getCombos()) {
            if (dc.getNroCombo() == combo.getNroCombo()) {
                dc.setCantidad(dc.getCantidad() + cantidad);
                ventaActual.calcularTotal();
                return;
            }
        }

        ventaActual.getCombos().add(new DetalleCombo(combo, cantidad));
        ventaActual.calcularTotal();
    }

    // Agrega un item al pedido actual o aumenta cantidad si ya existía
    public void agregarItem(Producto p, int cantidad) {
        if (ventaActual == null) {
            return;
        }

        for (DetalleVenta d : ventaActual.getItems()) {
            if (d.getProducto().getID() == p.getID()) {
                d.setCantidad(d.getCantidad() + cantidad);
                ventaActual.calcularTotal();
                return;
            }
        }

        ventaActual.getItems().add(new DetalleVenta(p, cantidad));
        ventaActual.calcularTotal();
    }
    
    ///Método para agregar producto validando stock
    public String agregarItemGUI(Producto producto, int cantidad) {
        if (ventaActual == null) {
            return "No existe un pedido actual.";
        }

        if (producto == null) {
            return "Producto no válido.";
        }

        if (cantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }

        String mensajeStock = validarStockProducto(producto, cantidad);

        if (mensajeStock != null) {
            return mensajeStock;
        }

        agregarItem(producto, cantidad);
        return null;
    }
    
    /////Método para agregar combo validando stock
    public String agregarComboGUI(Combo combo, int cantidad) {
        if (ventaActual == null) {
            return "No existe un pedido actual.";
        }

        if (combo == null) {
            return "Combo no válido.";
        }

        if (cantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }

        String mensajeStock = validarStockCombo(combo, cantidad);

        if (mensajeStock != null) {
            return mensajeStock;
        }

        agregarCombo(combo, cantidad);
        return null;
    }
    
    ////Validar stock de producto
    
    // ============================================================
// DISPONIBILIDAD DE PRODUCTOS Y COMBOS PARA EL MENÚ DINÁMICO
// ============================================================

/*
 * Indica si se puede agregar la cantidad indicada de un producto.
 *
 * Se considera:
 * - El stock actual del inventario.
 * - Los productos que ya están en el pedido.
 * - Los combos que ya están en el pedido.
 * - La nueva cantidad solicitada.
 */
public boolean puedeAgregarProducto(Producto producto, int cantidad) {
    return validarStockProducto(producto, cantidad) == null;
}

//
 // Indica si se puede agregar la cantidad indicada de un combo.
 //
 // Se suman los ingredientes de todos los productos del combo,
 // incluyendo ingredientes repetidos entre diferentes productos.
 //
public boolean puedeAgregarCombo(Combo combo, int cantidad) {
    return validarStockCombo(combo, cantidad) == null;
}

//
 // Valida el stock acumulado del pedido actual más el producto
 // que se pretende agregar.
 //
 // Retorna null cuando sí existe stock suficiente.
 // Retorna un mensaje cuando no se puede agregar.
 //
private String validarStockProducto(Producto producto, int cantidad) {
    if (producto == null) {
        return "Producto no válido.";
    }

    if (cantidad <= 0) {
        return "La cantidad debe ser mayor a 0.";
    }

    HashMap<Integer, Double> requeridoPorInsumo = new HashMap<>();

    // Primero sumar todo lo que ya está en el pedido.
    String errorPedido =
            acumularConsumoPedidoActual(requeridoPorInsumo);

    if (errorPedido != null) {
        return errorPedido;
    }

    // Después sumar el nuevo producto solicitado.
    String errorProducto = acumularConsumoProducto(
            requeridoPorInsumo,
            producto,
            cantidad
    );

    if (errorProducto != null) {
        return errorProducto;
    }

    return validarDisponibilidadAcumulada(
            requeridoPorInsumo,
            "el producto " + producto.getNombre()
    );
}

//
 // Valida el stock acumulado del pedido actual más el combo
 // que se pretende agregar.
 //
 // Retorna null cuando sí existe stock suficiente.
 // Retorna un mensaje cuando no se puede agregar.
 //
private String validarStockCombo(Combo combo, int cantidad) {
    if (combo == null) {
        return "Combo no válido.";
    }

    if (cantidad <= 0) {
        return "La cantidad debe ser mayor a 0.";
    }

    HashMap<Integer, Double> requeridoPorInsumo = new HashMap<>();

    // Sumar lo que ya consume el pedido.
    String errorPedido =
            acumularConsumoPedidoActual(requeridoPorInsumo);

    if (errorPedido != null) {
        return errorPedido;
    }

    // Sumar todos los productos incluidos en el nuevo combo.
    String errorCombo = acumularConsumoCombo(
            requeridoPorInsumo,
            combo,
            cantidad
    );

    if (errorCombo != null) {
        return errorCombo;
    }

    return validarDisponibilidadAcumulada(
            requeridoPorInsumo,
            "el combo #" + combo.getNroCombo()
    );
}

//
 // Suma al mapa todos los ingredientes que ya requiere
 // el pedido que se encuentra en construcción.
 //
private String acumularConsumoPedidoActual(
        HashMap<Integer, Double> requeridoPorInsumo
) {
    if (ventaActual == null) {
        return null;
    }

    // Productos individuales que ya están en el pedido.
    for (DetalleVenta detalle : ventaActual.getItems()) {
        Producto producto =
                menu.buscarProducto(detalle.getProducto().getID());

        if (producto == null) {
            producto = detalle.getProducto();
        }

        String error = acumularConsumoProducto(
                requeridoPorInsumo,
                producto,
                detalle.getCantidad()
        );

        if (error != null) {
            return error;
        }
    }

    // Combos que ya están en el pedido.
    for (DetalleCombo detalleCombo : ventaActual.getCombos()) {
        Combo combo =
                menu.buscarCombo(detalleCombo.getNroCombo());

        if (combo == null) {
            return "No se encontró la configuración del combo #"
                    + detalleCombo.getNroCombo() + ".";
        }

        String error = acumularConsumoCombo(
                requeridoPorInsumo,
                combo,
                detalleCombo.getCantidad()
        );

        if (error != null) {
            return error;
        }
    }

    return null;
}

//
 //Suma al mapa todos los ingredientes requeridos por un producto.
 //
private String acumularConsumoProducto(
        HashMap<Integer, Double> requeridoPorInsumo,
        Producto producto,
        int cantidad
) {
    if (producto == null) {
        return "Producto no válido.";
    }

    for (int idInsumo : producto.getIngredientes()) {
        Insumo insumo = inventario.buscarId(idInsumo);

        if (insumo == null) {
            return "Falta configurar el insumo con ID "
                    + idInsumo
                    + " del producto "
                    + producto.getNombre()
                    + ".";
        }

        double cantidadRequerida =
                insumo.getCantidadPorPizza() * cantidad;

        /*
         * Si el insumo ya estaba en el mapa, suma la cantidad.
         * Esto es importante porque varias pizzas o productos
         * de un combo pueden utilizar el mismo insumo.
         */
        requeridoPorInsumo.merge(
                idInsumo,
                cantidadRequerida,
                Double::sum
        );
    }

    return null;
}

//
 // Suma todos los ingredientes de todos los productos de un combo.
 //
private String acumularConsumoCombo(
        HashMap<Integer, Double> requeridoPorInsumo,
        Combo combo,
        int cantidad
) {
    if (combo == null
            || combo.getCombo() == null
            || combo.getCombo().isEmpty()) {

        return "El combo no tiene productos configurados.";
    }

    for (Producto productoCombo : combo.getCombo()) {
        Producto producto =
                menu.buscarProducto(productoCombo.getID());

        if (producto == null) {
            producto = productoCombo;
        }

        String error = acumularConsumoProducto(
                requeridoPorInsumo,
                producto,
                cantidad
        );

        if (error != null) {
            return error;
        }
    }

    return null;
}

//
 // Compara el consumo total acumulado con el stock real.
 //
 
private String validarDisponibilidadAcumulada(
        HashMap<Integer, Double> requeridoPorInsumo,
        String elementoSolicitado
) {
    /*
     * Ordenamos los IDs para que los ingredientes siempre
     * aparezcan en un orden estable dentro del mensaje.
     */
    java.util.ArrayList<Integer> idsInsumos =
            new java.util.ArrayList<>(requeridoPorInsumo.keySet());

    java.util.Collections.sort(idsInsumos);

    StringBuilder mensaje = new StringBuilder();
    int cantidadFaltantes = 0;

    for (Integer idInsumo : idsInsumos) {
        Insumo insumo = inventario.buscarId(idInsumo);

        if (insumo == null) {
            return "No se encontró en inventario el insumo con ID "
                    + idInsumo
                    + ". Revise la configuración del producto o combo.";
        }

        double cantidadRequerida =
                requeridoPorInsumo.getOrDefault(idInsumo, 0.0);

        double cantidadDisponible =
                insumo.getStockActual();

        /*
         * Se utiliza una pequeña tolerancia para evitar errores
         * de comparación producidos por números decimales.
         */
        boolean insuficiente =
                cantidadRequerida > cantidadDisponible + 0.000001;

        if (!insuficiente) {
            continue;
        }

        double cantidadFaltante =
                cantidadRequerida - cantidadDisponible;

        if (cantidadFaltante < 0) {
            cantidadFaltante = 0;
        }

        if (cantidadFaltantes == 0) {
            mensaje.append("No hay stock suficiente para ")
                    .append(elementoSolicitado)
                    .append(".\n\n");

            mensaje.append("INGREDIENTES INSUFICIENTES:\n\n");
        }

        cantidadFaltantes++;

        String unidad = insumo.getUnidad();

        if (unidad == null || unidad.trim().isEmpty()) {
            unidad = "unidades";
        }

        mensaje.append("• ")
                .append(insumo.getNombre())
                .append("\n");

        mensaje.append("  Requerido por el pedido: ")
                .append(formatearCantidadInsumo(cantidadRequerida))
                .append(" ")
                .append(unidad)
                .append("\n");

        mensaje.append("  Disponible: ")
                .append(formatearCantidadInsumo(cantidadDisponible))
                .append(" ")
                .append(unidad)
                .append("\n");

        mensaje.append("  Faltante: ")
                .append(formatearCantidadInsumo(cantidadFaltante))
                .append(" ")
                .append(unidad)
                .append("\n\n");
    }

    if (cantidadFaltantes == 0) {
        return null;
    }

    mensaje.append("Reduzca la cantidad solicitada o reponga los ingredientes indicados.");

    return mensaje.toString();
}
    
    
    
private String formatearCantidadInsumo(double cantidad) {
    /*
     * Si el valor es prácticamente entero, se muestra sin
     * decimales. Ejemplo: 2 unidades.
     */
    if (Math.abs(cantidad - Math.rint(cantidad)) < 0.000001) {
        return String.format("%.0f", cantidad);
    }

    /*
     * Para kg, litros, gramos y otras cantidades decimales,
     * mostramos hasta tres decimales.
     */
    return String.format("%.3f", cantidad);
}
    
    
// ============================================================
// ACTUALIZAR CANTIDAD DE PRODUCTOS Y COMBOS DEL PEDIDO ACTUAL
// ============================================================

/**
 * Actualiza la cantidad de un producto individual.
 *
 * Retorna null cuando la actualización fue correcta.
 * Retorna un mensaje cuando la cantidad o el stock no son válidos.
 */
public String actualizarCantidadItemGUI(
        int indiceProducto,
        int nuevaCantidad
) {
    if (ventaActual == null) {
        return "No existe un pedido actual.";
    }

    if (nuevaCantidad <= 0) {
        return "La cantidad debe ser mayor a 0.";
    }

    ArrayList<DetalleVenta> items = ventaActual.getItems();

    if (indiceProducto < 0 || indiceProducto >= items.size()) {
        return "No se pudo identificar el producto seleccionado.";
    }

    DetalleVenta detalleSeleccionado =
            items.get(indiceProducto);

    Producto producto =
            detalleSeleccionado.getProducto();

    HashMap<Integer, Double> requeridoPorInsumo =
            new HashMap<>();

    /*
     * Se acumula todo el pedido excepto el producto
     * que se está editando.
     */
    String errorPedido = acumularConsumoPedidoActualExcepto(
            requeridoPorInsumo,
            indiceProducto,
            -1
    );

    if (errorPedido != null) {
        return errorPedido;
    }

    /*
     * Se vuelve a agregar el producto, pero utilizando
     * la nueva cantidad.
     */
    String errorProducto = acumularConsumoProducto(
            requeridoPorInsumo,
            producto,
            nuevaCantidad
    );

    if (errorProducto != null) {
        return errorProducto;
    }

    String errorStock = validarDisponibilidadAcumulada(
            requeridoPorInsumo,
            "actualizar la cantidad de " + producto.getNombre()
    );

    if (errorStock != null) {
        return errorStock;
    }

    detalleSeleccionado.setCantidad(nuevaCantidad);
    ventaActual.calcularTotal();

    return null;
}


/**
 * Actualiza la cantidad de un combo del pedido actual.
 */
public String actualizarCantidadComboGUI(
        int indiceCombo,
        int nuevaCantidad
) {
    if (ventaActual == null) {
        return "No existe un pedido actual.";
    }

    if (nuevaCantidad <= 0) {
        return "La cantidad debe ser mayor a 0.";
    }

    ArrayList<DetalleCombo> combos =
            ventaActual.getCombos();

    if (indiceCombo < 0 || indiceCombo >= combos.size()) {
        return "No se pudo identificar el combo seleccionado.";
    }

    DetalleCombo detalleSeleccionado =
            combos.get(indiceCombo);

    Combo combo = menu.buscarCombo(
            detalleSeleccionado.getNroCombo()
    );

    if (combo == null) {
        return "No se encontró la configuración del combo #"
                + detalleSeleccionado.getNroCombo()
                + ".";
    }

    HashMap<Integer, Double> requeridoPorInsumo =
            new HashMap<>();

    /*
     * Se acumula todo el pedido excepto el combo
     * que se encuentra en edición.
     */
    String errorPedido = acumularConsumoPedidoActualExcepto(
            requeridoPorInsumo,
            -1,
            indiceCombo
    );

    if (errorPedido != null) {
        return errorPedido;
    }

    /*
     * Se agrega nuevamente el combo usando la cantidad nueva.
     */
    String errorCombo = acumularConsumoCombo(
            requeridoPorInsumo,
            combo,
            nuevaCantidad
    );

    if (errorCombo != null) {
        return errorCombo;
    }

    String errorStock = validarDisponibilidadAcumulada(
            requeridoPorInsumo,
            "actualizar la cantidad del combo #"
                    + combo.getNroCombo()
    );

    if (errorStock != null) {
        return errorStock;
    }

    detalleSeleccionado.setCantidad(nuevaCantidad);
    ventaActual.calcularTotal();

    return null;
}


/**
 * Acumula el consumo completo del pedido, excepto el producto
 * o combo que se encuentra en edición.
 *
 * Utilizar -1 significa que no se excluirá ningún elemento
 * de ese tipo.
 */
private String acumularConsumoPedidoActualExcepto(
        HashMap<Integer, Double> requeridoPorInsumo,
        int indiceProductoExcluir,
        int indiceComboExcluir
) {
    if (ventaActual == null) {
        return null;
    }

    for (int i = 0; i < ventaActual.getItems().size(); i++) {
        if (i == indiceProductoExcluir) {
            continue;
        }

        DetalleVenta detalle =
                ventaActual.getItems().get(i);

        Producto producto =
                menu.buscarProducto(
                        detalle.getProducto().getID()
                );

        if (producto == null) {
            producto = detalle.getProducto();
        }

        String error = acumularConsumoProducto(
                requeridoPorInsumo,
                producto,
                detalle.getCantidad()
        );

        if (error != null) {
            return error;
        }
    }

    for (int i = 0; i < ventaActual.getCombos().size(); i++) {
        if (i == indiceComboExcluir) {
            continue;
        }

        DetalleCombo detalleCombo =
                ventaActual.getCombos().get(i);

        Combo combo =
                menu.buscarCombo(
                        detalleCombo.getNroCombo()
                );

        if (combo == null) {
            return "No se encontró la configuración del combo #"
                    + detalleCombo.getNroCombo()
                    + ".";
        }

        String error = acumularConsumoCombo(
                requeridoPorInsumo,
                combo,
                detalleCombo.getCantidad()
        );

        if (error != null) {
            return error;
        }
    }

    return null;
}
    
    
    
    // Quita un item del pedido actual según su índice
    public void quitarItem(int index) {
        if (ventaActual == null) {
            return;
        }

        ArrayList<DetalleVenta> items = ventaActual.getItems();

        if (index >= 0 && index < items.size()) {
            items.remove(index);
            ventaActual.calcularTotal();
        }
    }
    
    //NUEVO METODO QUE QUITA COMBOS, SI YA SE TENIA IMPLEMENTADO SE PUEDE BORRAR
    //////////////////////////////////////////////////////////////////////////
    public void quitarCombo(int index) {
        if (ventaActual == null) {
            return;
        }

        java.util.ArrayList<pizzeria.model.DetalleCombo> combos = ventaActual.getCombos();

        if (index >= 0 && index < combos.size()) {
            combos.remove(index);
            ventaActual.calcularTotal();
        }
    }
    
    ///////////////////////////////////////////////////////////////////////

    // Cancela el armado del pedido actual antes de cobrarlo
    public void cancelarArmadoPedido() {
        if (ventaActual != null) {
            System.out.println(" Pedido en armado cancelado.");
            ventaActual = null;
        }
    }

    // Cancela una venta ya pagada si todavía no fue entregada y registra su reembolso
    public boolean cancelarVentaPagada(int idVenta) {
        Venta venta = buscarVentaPorId(idVenta);

        if (venta == null) {
            return false;
        }

        if (venta.getEstado() == EstadoPedido.ENTREGADO ||
            venta.getEstado() == EstadoPedido.CANCELADO) {
            return false;
        }

        if (gestorCocina != null) {
            gestorCocina.cancelarPedidoPorVenta(idVenta);
        }
        venta.setEstado(EstadoPedido.CANCELADO);

        if (gestorFinanzas != null) {
            gestorFinanzas.registrarEgreso(
                    venta.getTotal(),
                    MovimientoCaja.CAT_OTRO,
                    "Reembolso venta #" + venta.getId()
            );
        }

        guardarArchivo();
        return true;
    }

    // Pregunta al cajero si el cliente desea factura y actúa en consecuencia
    private void preguntarYGenerarFactura(Venta v) {
        Consola.titulo("COMPROBANTE DE VENTA");
        System.out.println(v);
        System.out.println(" Gracias por su preferencia — La casa del Sabor");
        Consola.separador();

        if (!Consola.confirmar("¿El cliente desea factura?")) {
            return;
        }

        // Pedir datos de facturación
        String nit = Consola.leerTexto("NIT del cliente: ");
        String razonSocial = Consola.leerTexto("Nombre / Razón Social: ");

        generarFacturaOficial(v, nit, razonSocial);
    }

    // Genera e imprime la factura oficial con los datos de la pizzería y del cliente
    public void generarFacturaOficial(Venta v, String nit, String razonSocial) {
        String linea  = " " + "=".repeat(58);
        String lineaD = " " + "-".repeat(58);

        System.out.println();
        System.out.println(linea);
        System.out.println(centrar("LA CASA DEL SABOR", 60));
        System.out.println(centrar("Pje. Suiza 1645, Cochabamba", 60));
        System.out.println(centrar("Tel. 62620262  —  Cochabamba-Bolivia", 60));
        System.out.println(linea);
        System.out.println(centrar("F A C T U R A", 60));
        System.out.println(linea);
        System.out.printf(" NIT:          %s%n", nit);
        System.out.printf(" Razón Social: %s%n", razonSocial);
        System.out.printf(" Fecha:        %s%n", v.getFecha().format(Venta.FORMATO_FECHA));
        System.out.printf(" N° Venta:     %d%n", v.getId());
        System.out.println(lineaD);
        System.out.printf(" %-22s  %4s  %10s  %10s%n",
                "Descripción", "Cant", "P.Unit (Bs)", "Total (Bs)");
        System.out.println(lineaD);

        // Productos individuales
        for (DetalleVenta d : v.getItems()) {
            System.out.printf(" %-22s  %4d  %10.2f  %10.2f%n",
                    d.getProducto().getNombre(),
                    d.getCantidad(),
                    d.getProducto().getPrecio(),
                    d.getSubTotal());
        }

        // Combos como elemento único con su precio total sin distribuir
        for (DetalleCombo c : v.getCombos()) {
            System.out.printf(" %-22s  %4d  %10.2f  %10.2f%n",
                    "COMBO #" + c.getNroCombo(),
                    c.getCantidad(),
                    c.getPrecioUnitario(),
                    c.getSubTotal());
            System.out.printf("   ( %s )%n", c.getDescripcion());
        }

        System.out.println(lineaD);
        System.out.printf(" %-39s %10.2f Bs.%n", "TOTAL:", v.getTotal());

        if (v.getCambio() > 0) {
            System.out.printf(" %-39s %10.2f Bs.%n", "CAMBIO:", v.getCambio());
        }

        System.out.printf(" Método de pago: %s%n", v.getMetodoPago().getNombre());
        System.out.println(linea);
        System.out.println(centrar("¡Gracias por su preferencia!", 60));
        System.out.println(linea);
        System.out.println();
    }

    // Centra un texto en un ancho dado con espacios
    private String centrar(String texto, int ancho) {
        if (texto.length() >= ancho) return texto;
        int espacios = (ancho - texto.length()) / 2;
        return " ".repeat(espacios) + texto;
    }

    // Genera la factura de una venta inmediata (comprobante simple sin datos de facturación)
    public void generarFactura(Venta v) {
        Consola.titulo("COMPROBANTE DE VENTA");
        System.out.println(v);
        System.out.println(" Gracias por su preferencia — La casa del Sabor");
        Consola.separador();
    }

    // Genera una confirmación simple de reserva pagada
    public void generarConfirmacionReserva(Reserva r) {
        Consola.titulo("CONFIRMACIÓN DE RESERVA");
        System.out.println(r);
        System.out.println(" Reserva registrada y pagada correctamente.");
        Consola.separador();
    }

    // Busca una venta por su ID dentro del historial
    private Venta buscarVentaPorId(int id) {
        for (Venta v : listaVenta) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    // Valida si el stock actual alcanza para cubrir todo el pedido antes de aceptarlo
    private boolean hayStockSuficienteParaPedido(List<DetalleVenta> items) {
        HashMap<Integer, Double> requeridoPorInsumo = new HashMap<>();

        // Verificar insumos de productos individuales
        for (DetalleVenta detalle : items) {
            Producto productoCompleto = menu.buscarProducto(detalle.getProducto().getID());

            if (productoCompleto == null) {
                productoCompleto = detalle.getProducto();
            }

            for (int idInsumo : productoCompleto.getIngredientes()) {
                Insumo insumo = inventario.buscarId(idInsumo);

                if (insumo == null) {
                    System.out.println(" Falta configurar un insumo del producto: " + productoCompleto.getNombre());
                    return false;
                }

                double requerido = insumo.getCantidadPorPizza() * detalle.getCantidad();

                if (requeridoPorInsumo.containsKey(idInsumo)) {
                    requerido += requeridoPorInsumo.get(idInsumo);
                }

                requeridoPorInsumo.put(idInsumo, requerido);
            }
        }

        // Verificar insumos de los combos
        for (DetalleCombo dc : ventaActual.getCombos()) {
            Combo combo = menu.buscarCombo(dc.getNroCombo());
            if (combo == null) continue;

            for (Producto p : combo.getCombo()) {
                Producto productoCompleto = menu.buscarProducto(p.getID());
                if (productoCompleto == null) productoCompleto = p;

                for (int idInsumo : productoCompleto.getIngredientes()) {
                    Insumo insumo = inventario.buscarId(idInsumo);
                    if (insumo == null) continue;

                    double requerido = insumo.getCantidadPorPizza() * dc.getCantidad();

                    if (requeridoPorInsumo.containsKey(idInsumo)) {
                        requerido += requeridoPorInsumo.get(idInsumo);
                    }

                    requeridoPorInsumo.put(idInsumo, requerido);
                }
            }
        }

        boolean suficiente = true;

        for (Integer idInsumo : requeridoPorInsumo.keySet()) {
            Insumo insumo = inventario.buscarId(idInsumo);
            double requerido = requeridoPorInsumo.get(idInsumo);

            if (insumo.getStockActual() < requerido) {
                suficiente = false;
                System.out.printf(" Stock insuficiente para %s. Disponible: %.3f | Requerido: %.3f%n",
                        insumo.getNombre(), insumo.getStockActual(), requerido);
            }
        }

        return suficiente;
    }

    // Guarda las ventas en archivo
    public void guardarArchivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_VENTAS))) {
            bw.write("# Sistema Pizzería — Ventas");
            bw.newLine();
            bw.write("# Formato: id|fecha|idCajero|metodoPago|total|cambio|estado|nombreCliente|items...");
            bw.newLine();

            for (Venta v : listaVenta) {
                bw.write(v.escribirTexto());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(" Error al guardar ventas: " + e.getMessage());
        }
    }

    // Carga las ventas desde archivo y reconstruye los productos usando el menú real cuando es posible
    public void cargarArchivo() {
        listaVenta = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_VENTAS))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                Venta v = parsearVenta(linea);

                if (v != null) {
                    listaVenta.add(v);
                }
            }
        } catch (IOException e) {
            // Si no existe el archivo todavía, no se considera error crítico
        }
    }

    /*// Reconstruye una venta desde una línea de texto del archivo
    private Venta parsearVenta(String linea) {
        try {
            String[] partes = linea.split("\\|", 9);

            if (partes.length < 8) {
                return null;
            }

            int id = Integer.parseInt(partes[0].trim());
            LocalDateTime fecha = LocalDateTime.parse(partes[1].trim(), Venta.FORMATO_FECHA);
            int idCajero = Integer.parseInt(partes[2].trim());
            MetodoPago metodo = MetodoPago.valueOf(partes[3].trim());
            double total = Double.parseDouble(partes[4].trim().replace(",", "."));
            double cambio = Double.parseDouble(partes[5].trim().replace(",", "."));
            EstadoPedido estado = EstadoPedido.valueOf(partes[6].trim());

            Venta v = new Venta(id, idCajero);
            v.setFecha(fecha);
            v.setMetodoPago(metodo);
            v.setTotal(total);
            v.setCambio(cambio);
            v.setEstado(estado);

            String itemsRaw = "";

            if (partes.length == 8) {
                // Formato antiguo sin nombre de cliente
                itemsRaw = partes[7].trim();
                v.setNombreCliente("");
            } else {
                // Formato nuevo con nombre de cliente
                v.setNombreCliente(partes[7].trim());
                itemsRaw = partes[8].trim();
            }

            if (!itemsRaw.isEmpty()) {
                String[] itemsPartes = itemsRaw.split(";");

                for (String itemTxt : itemsPartes) {
                    String[] d = itemTxt.split("~");
                    if (d.length < 4) {
                        continue;
                    }

                    // Detectar si es un combo serializado (empieza con "COMBO")
                    if ("COMBO".equals(d[0].trim())) {
                        // Formato: COMBO~nroCombo~descripcion~precioUnitario~cantidad
                        if (d.length < 5) continue;
                        int nroCombo = Integer.parseInt(d[1].trim());
                        String descripcion = d[2].trim();
                        double precioUnit = Double.parseDouble(d[3].trim().replace(",", "."));
                        int cant = Integer.parseInt(d[4].trim());
                        v.getCombos().add(new DetalleCombo(nroCombo, descripcion, precioUnit, cant));
                    } else {
                        // Producto individual
                        int idProd = Integer.parseInt(d[0].trim());
                        String nombre = d[1].trim();
                        double precio = Double.parseDouble(d[2].trim().replace(",", "."));
                        int cant = Integer.parseInt(d[3].trim());

                        Producto prodMenu = menu.buscarProducto(idProd);
                        Producto prod;

                        if (prodMenu != null) {
                            prod = prodMenu;
                        } else {
                            prod = new Producto(idProd, TipoProducto.PRODUCTO, nombre, "", precio);
                        }

                        v.getItems().add(new DetalleVenta(prod, cant));
                    }
                }
            }

            return v;
        } catch (Exception e) {
            System.out.println(" [ERROR] parsearVenta: " + e.getMessage() + " → " + linea);
            return null;
        }
    }*/
    
    private Venta parsearVenta(String linea) {
        try {
            String[] partes = linea.split("\\|", 9);

            if (partes.length < 8) {
                return null;
            }

            int id = Integer.parseInt(partes[0].trim());
            LocalDateTime fecha = LocalDateTime.parse(partes[1].trim(), Venta.FORMATO_FECHA);
            int idCajero = Integer.parseInt(partes[2].trim());
            MetodoPago metodo = MetodoPago.valueOf(partes[3].trim());
            double total = Double.parseDouble(partes[4].trim().replace(",", "."));
            double cambio = Double.parseDouble(partes[5].trim().replace(",", "."));
            EstadoPedido estado = EstadoPedido.valueOf(partes[6].trim());

            Venta v = new Venta(id, idCajero);
            v.setFecha(fecha);
            v.setMetodoPago(metodo);
            v.setTotal(total);
            v.setCambio(cambio);
            v.setEstado(estado);

            String itemsRaw = "";

            if (partes.length == 8) {
                itemsRaw = partes[7].trim();
                v.setNombreCliente("");
            } else {
                v.setNombreCliente(partes[7].trim());
                itemsRaw = partes[8].trim();
            }

            if (!itemsRaw.isEmpty()) {
                String[] itemsPartes = itemsRaw.split(";");

                for (String itemTxt : itemsPartes) {
                    String[] d = itemTxt.split("~");
                    if (d.length < 4) {
                        continue;
                    }

                    // Detectar si es un combo
                    if ("COMBO".equals(d[0].trim())) {
                        if (d.length < 5) continue;
                        int nroCombo = Integer.parseInt(d[1].trim());
                        String descripcion = d[2].trim();
                        double precioUnit = Double.parseDouble(d[3].trim().replace(",", "."));
                        int cant = Integer.parseInt(d[4].trim());
                        v.getCombos().add(new DetalleCombo(nroCombo, descripcion, precioUnit, cant));
                    } else {
                        int idProd = Integer.parseInt(d[0].trim());
                        String nombre = d[1].trim();
                        double precio = Double.parseDouble(d[2].trim().replace(",", "."));
                        int cant = Integer.parseInt(d[3].trim());

                        // IMPORTANTE: Crear producto directamente sin usar menu
                        // Esto evita el NullPointerException cuando menu es null
                        Producto prod = new Producto(idProd, TipoProducto.PRODUCTO, nombre, "", precio);
                        v.getItems().add(new DetalleVenta(prod, cant));
                    }
                }
            }

            return v;
        } catch (Exception e) {
            System.out.println(" [ERROR] parsearVenta: " + e.getMessage() + " → " + linea);
            return null;
        }
    }

    // Crea una copia de los items y combos para reutilizarlos al generar una reserva
    private List<DetalleVenta> copiarItems(ArrayList<DetalleVenta> origen) {
        List<DetalleVenta> copia = new ArrayList<>();

        for (DetalleVenta d : origen) {
            Producto p = d.getProducto();
            Producto copiaProducto = new Producto(p.getID(), p.getTipo(), p.getNombre(), p.getDescripcion(), p.getPrecio());
            copiaProducto.getIngredientes().addAll(p.getIngredientes());

            copia.add(new DetalleVenta(copiaProducto, d.getCantidad()));
        }

        return copia;
    }

    // Crea una copia de los combos para reutilizarlos al generar una reserva
    private ArrayList<DetalleCombo> copiarCombos(ArrayList<DetalleCombo> origen) {
        ArrayList<DetalleCombo> copia = new ArrayList<>();
        for (DetalleCombo dc : origen) {
            copia.add(new DetalleCombo(dc.getNroCombo(), dc.getDescripcion(),
                                       dc.getPrecioUnitario(), dc.getCantidad()));
        }
        return copia;
    }
    
    ///////////METODO AUXILIAR PARA COMBOS
    private List<DetalleVenta> copiarPedidoParaReservaConCombos(Venta venta) {
        List<DetalleVenta> pedidoReserva = new ArrayList<>();

        for (DetalleVenta detalle : venta.getItems()) {
            Producto p = detalle.getProducto();

            Producto copiaProducto = new Producto(
                    p.getID(),
                    p.getTipo(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getPrecio()
            );

            copiaProducto.getIngredientes().addAll(p.getIngredientes());

            pedidoReserva.add(new DetalleVenta(copiaProducto, detalle.getCantidad()));
        }

        for (DetalleCombo detalleCombo : venta.getCombos()) {
            int idComboComoProducto = 9000 + detalleCombo.getNroCombo();

            Producto comboComoProducto = new Producto(
                    idComboComoProducto,
                    TipoProducto.PRODUCTO,
                    "Combo #" + detalleCombo.getNroCombo(),
                    detalleCombo.getDescripcion(),
                    detalleCombo.getPrecioUnitario()
            );

            pedidoReserva.add(new DetalleVenta(comboComoProducto, detalleCombo.getCantidad()));
        }

        return pedidoReserva;
    }
    
    
    public boolean cancelarReservaGUI(int idReserva) {
        if (gestorReserva == null) {
            return false;
        }

        boolean cancelada = gestorReserva.cancelarReserva(
                idReserva,
                gestorFinanzas,
                gestorCocina
        );

        if (cancelada) {
            gestorReserva.guardarArchivo("resources/data/reservas.txt");
        }

        return cancelada;
    }

    public boolean enviarReservaACocinaGUI(int idReserva) {
        if (gestorReserva == null || gestorCocina == null) {
            return false;
        }

        boolean enviada = gestorReserva.enviarACocina(idReserva, gestorCocina);

        if (enviada) {
            gestorReserva.guardarArchivo("resources/data/reservas.txt");
        }

        return enviada;
    }
}
