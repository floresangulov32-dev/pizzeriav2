package pizzeria.view;

import pizzeria.model.Menu;
import pizzeria.controller.GestorReserva;
import pizzeria.controller.GestorCocina;
import pizzeria.controller.GestorFinanzas;
import pizzeria.model.Reserva;
import pizzeria.model.Producto;
import pizzeria.model.DetalleVenta;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsolaGestorReservas {

    private final GestorReserva gestorReserva;
    private final GestorCocina gestorCocina;
    private final GestorFinanzas gestorFinanzas;
    private final Menu menu;
    private final Scanner sc;
    private static final String ARCHIVO_RESERVAS = "resources/data/reservas.txt";
   
    public ConsolaGestorReservas(GestorReserva gestorReserva, GestorCocina gestorCocina, GestorFinanzas gestorFinanzas, Menu menu) {
        this.gestorReserva = gestorReserva;
        this.gestorCocina = gestorCocina;
        this.gestorFinanzas = gestorFinanzas;
        this.menu = menu;
        this.sc = new Scanner(System.in);
    }

    public void mostrar() {
        boolean continuar = true;

        while (continuar) {
            titulo("ADMINISTRAR RESERVAS");
            System.out.printf("  1. Ver reservas%n");
            System.out.printf("  2. Buscar reserva por ID%n");
            System.out.printf("  3. Cancelar reserva%n");
            System.out.printf("  4. Enviar reserva a cocina%n");
            System.out.printf("  5. Ver reservas del día%n");
            System.out.printf("  6. Guardar archivo%n");
            System.out.printf("  7. Recargar archivo%n");
            System.out.printf("  8. Ver productos disponibles%n");
            System.out.printf("  0. Volver%n");
            separador();

            int opcion = leerEnteroRango("Seleccione una opción: ", 0, 8);

            switch (opcion) {
                case 1 -> verReservas();
                case 2 -> buscarReservaPorId();
                case 3 -> cancelarReserva();
                case 4 -> enviarReservaACocina();
                case 5 -> verReservasDelDia();
                case 6 -> guardarArchivoManual();
                case 7 -> recargarArchivo();
                case 8 -> {
                    mostrarProductosDisponibles();
                    pausar();
                }
                case 0 -> {
                    gestorReserva.guardarArchivo(ARCHIVO_RESERVAS);
                    continuar = false;
                }
            }
        }
    }

    private void verReservas() {
        titulo("LISTA DE RESERVAS");

        List<Reserva> reservas = gestorReserva.verReservas();

        if (reservas.isEmpty()) {
            System.out.println(" No hay reservas registradas.");
            pausar();
            return;
        }

        separador();
        System.out.printf(" %-5s %-20s %-12s %-12s %-12s%n",
                "ID", "CLIENTE", "TELÉFONO", "ESTADO", "TOTAL");
        separador();

        for (Reserva reserva : reservas) {
            System.out.printf(" %-5d %-20s %-12s %-12s Bs %-8.2f%n",
                    reserva.getId(),
                    reserva.getNombreCliente(),
                    reserva.getTelefono(),
                    reserva.getEstado(),
                    reserva.calcularTotal());
        }

        pausar();
    }

    private void buscarReservaPorId() {
        titulo("BUSCAR RESERVA POR ID");

        int id = leerEntero("Ingrese el ID de la reserva: ");
        Reserva reserva = gestorReserva.buscarPorId(id);

        if (reserva == null) {
            System.out.println(" No se encontró la reserva.");
            pausar();
            return;
        }

        separador();
        System.out.printf("  ID            : %d%n", reserva.getId());
        System.out.printf("  Cliente       : %s%n", reserva.getNombreCliente());
        System.out.printf("  Teléfono      : %s%n", reserva.getTelefono());
        System.out.printf("  Fecha reserva : %s%n", reserva.getFechaReserva());
        System.out.printf("  Estado        : %s%n", reserva.getEstado());
        System.out.printf("  Total         : Bs %.2f%n", reserva.calcularTotal());
        separador();

        System.out.println(" DETALLES DEL PEDIDO");
        separador();
        System.out.printf(" %-5s %-20s %-10s %-12s%n",
                "NRO", "PRODUCTO", "CANTIDAD", "SUBTOTAL");
        separador();

        List<DetalleVenta> detalles = reserva.getPedido();

        for (int i = 0; i < detalles.size(); i++) {
            DetalleVenta detalle = detalles.get(i);
            System.out.printf(" %-5d %-20s %-10d Bs %-8.2f%n",
                    i + 1,
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad(),
                    detalle.getSubTotal());
        }

        pausar();
    }

    private void cancelarReserva() {
        titulo("CANCELAR RESERVA");

        int id = leerEntero("Ingrese el ID de la reserva: ");
        boolean cancelada = gestorReserva.cancelarReserva(id, gestorFinanzas, gestorCocina);

        if (!cancelada) {
            System.out.println(" No se pudo cancelar la reserva. Puede no existir, ya estar en preparación, lista o entregada.");
            pausar();
            return;
        }

        gestorReserva.guardarArchivo(ARCHIVO_RESERVAS);

        System.out.printf("%n");
        System.out.println(" Reserva cancelada correctamente y reembolso registrado.");
        pausar();
    }

    private void enviarReservaACocina() {
        titulo("ENVIAR RESERVA A COCINA");

        int id = leerEntero("Ingrese el ID de la reserva: ");
        boolean enviada = gestorReserva.enviarACocina(id, gestorCocina);

        if (!enviada) {
            System.out.println(" No se pudo enviar la reserva a cocina.");
            System.out.println(" Verifique que exista y que esté en estado PENDIENTE.");
            pausar();
            return;
        }

        gestorReserva.guardarArchivo(ARCHIVO_RESERVAS);

        System.out.println(" Reserva enviada a cocina correctamente.");
        pausar();
    }

    private void verReservasDelDia() {
        titulo("RESERVAS DEL DÍA");

        List<Reserva> reservasDelDia = gestorReserva.verDelDia();

        if (reservasDelDia.isEmpty()) {
            System.out.println(" No hay reservas registradas para hoy.");
            pausar();
            return;
        }

        separador();
        System.out.printf(" %-5s %-20s %-12s %-12s %-12s%n",
                "ID", "CLIENTE", "TELÉFONO", "ESTADO", "TOTAL");
        separador();

        for (Reserva reserva : reservasDelDia) {
            System.out.printf(" %-5d %-20s %-12s %-12s Bs %-8.2f%n",
                    reserva.getId(),
                    reserva.getNombreCliente(),
                    reserva.getTelefono(),
                    reserva.getEstado(),
                    reserva.calcularTotal());
        }

        pausar();
    }

    private void guardarArchivoManual() {
        titulo("GUARDAR ARCHIVO");
        gestorReserva.guardarArchivo(ARCHIVO_RESERVAS);
        System.out.println(" Archivo guardado correctamente.");
        pausar();
    }

    private void recargarArchivo() {
        titulo("RECARGAR ARCHIVO");
        gestorReserva.cargarArchivo(ARCHIVO_RESERVAS);
        System.out.println(" Archivo cargado correctamente.");
        pausar();
    }

    private void mostrarProductosDisponibles() {
        titulo("PRODUCTOS DISPONIBLES");
        separador();
        System.out.printf(" %-5s %-25s %-12s%n", "ID", "PRODUCTO", "PRECIO");
        separador();

        ArrayList<Producto> productos = menu.getProductos();

        if (productos.isEmpty()) {
            System.out.println(" No hay productos registrados en el menú.");
            separador();
            return;
        }

        for (Producto producto : productos) {
            System.out.printf(" %-5d %-25s Bs %-8.2f%n",
                    producto.getID(),
                    producto.getNombre(),
                    producto.getPrecio());
        }

        separador();
    }
  
    private String leerTexto(String mensaje) {
        String entrada;
        do {
            System.out.print(" " + mensaje);
            entrada = sc.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println(" El campo no puede estar vacío. Intente de nuevo.");
            }
        } while (entrada.isEmpty());

        return entrada;
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(" " + mensaje);
            String entrada = sc.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(" Debe ingresar un número entero válido.");
            }
        }
    }

    private int leerEnteroRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje);

            if (valor >= min && valor <= max) {
                return valor;
            }

            System.out.printf(" Ingrese un número entero entre %d y %d.%n", min, max);
        }
    }

    private void pausar() {
        System.out.printf("%n");
        System.out.print(" Presione Enter para continuar...");
        sc.nextLine();
    }

    private void separador() {
        System.out.println(" " + "-".repeat(70));
    }

    private void titulo(String texto) {
        int total = 70;
        int padding = (total - texto.length() - 2) / 2;
        String pad = "=".repeat(Math.max(padding, 1));

        System.out.printf("%n");
        System.out.println(" " + pad + " " + texto + " " + pad);
    }
}