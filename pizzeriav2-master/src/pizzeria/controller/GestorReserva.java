package pizzeria.controller;

import pizzeria.model.EstadoReserva;
import pizzeria.model.MovimientoCaja;
import pizzeria.model.DetalleVenta;
import pizzeria.model.TipoProducto;

//import pizzeria.controller.GestorCocina;
//import pizzeria.controller.GestorFinanzas;
import pizzeria.model.Reserva;
import pizzeria.model.Producto;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestorReserva{
    private List<Reserva> listaReservas;
    private int siguienteId;

    public GestorReserva() {
        listaReservas = new ArrayList<>();
        siguienteId = 1;
    }

    public List<Reserva> getListaReservas() {
        return listaReservas;
    }

    public void setListaReservas(List<Reserva> listaReservas) {
        this.listaReservas = listaReservas;
    }

    // Genera un ID automático para cada nueva reserva
    public int generarId() {
        return siguienteId++;
    }

    // Crea una nueva reserva, la agrega a la lista y la devuelve
    public Reserva nuevaReserva(String nombreCliente, String telefono, LocalDateTime fechaReserva, List<DetalleVenta> pedido) {
        Reserva reserva = new Reserva(generarId(), nombreCliente, telefono, fechaReserva);
        reserva.setPedido(pedido);
        listaReservas.add(reserva);
        return reserva;
    }

    // Busca una reserva por su ID
    public Reserva buscarPorId(int id) {
        for (Reserva reserva : listaReservas) {
            if (reserva.getId() == id) {
                return reserva;
            }
        }
        return null;
    }

    // Cancela una reserva pagada y registra el reembolso si todavía no fue preparada ni entregada
    public boolean cancelarReserva(int id, GestorFinanzas gestorFinanzas, GestorCocina gestorCocina) {
        Reserva reserva = buscarPorId(id);

        if (reserva == null) {
            return false;
        }

        if (reserva.getEstado() == EstadoReserva.CANCELADA ||
            reserva.getEstado() == EstadoReserva.ENTREGADA ||
            reserva.getEstado() == EstadoReserva.LISTA) {
            return false;
        }

        if (reserva.getEstado() == EstadoReserva.EN_COCINA) {
            boolean retirada = false;
            if (gestorCocina != null) {
                retirada = gestorCocina.cancelarPedidoPorReserva(id);
            }

            if (!retirada) {
                return false;
            }
        }

        reserva.setEstado(EstadoReserva.CANCELADA);

        if (gestorFinanzas != null) {
            gestorFinanzas.registrarEgreso(
                    reserva.calcularTotal(),
                    MovimientoCaja.CAT_OTRO,
                    "Reembolso reserva #" + reserva.getId()
            );
        }

        return true;
    }

    // Versión simple sin finanzas ni cocina, por compatibilidad
    public boolean cancelarReserva(int id) {
        return cancelarReserva(id, null, null);
    }

    // Envía una reserva a cocina si está pendiente
    public boolean enviarACocina(int idReserva, GestorCocina gestorCocina) {
        Reserva reserva = buscarPorId(idReserva);

        if (reserva == null) {
            return false;
        }

        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            return false;
        }

        boolean agregada = gestorCocina.agregarReservaACocina(reserva);

        if (agregada) {
            reserva.setEstado(EstadoReserva.EN_COCINA);
            return true;
        }

        return false;
    }

    // Devuelve la lista completa de reservas
    public List<Reserva> verReservas() {
        return listaReservas;
    }

    // Devuelve únicamente las reservas del día actual
    public List<Reserva> verDelDia() {
        List<Reserva> reservasDelDia = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        for (Reserva reserva : listaReservas) {
            if (reserva.getFechaReserva().toLocalDate().equals(hoy)) {
                reservasDelDia.add(reserva);
            }
        }

        return reservasDelDia;
    }

    // Guarda las reservas en archivo, incluyendo el detalle del pedido
    public void guardarArchivo(String nombreArchivo) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo));

            for (Reserva reserva : listaReservas) {
                String linea = reserva.getId() + ";" +
                               reserva.getNombreCliente() + ";" +
                               reserva.getTelefono() + ";" +
                               reserva.getFechaReserva() + ";" +
                               reserva.getEstado() + ";" +
                               serializarPedido(reserva.getPedido());

                bw.write(linea);
                bw.newLine();
            }

            bw.close();
            System.out.println(" Archivo de reservas guardado correctamente.");
        } catch (IOException e) {
            System.out.println(" Error al guardar el archivo de reservas.");
        }
    }

    // Carga las reservas desde archivo y reconstruye también el pedido
    public void cargarArchivo(String nombreArchivo) {
        File archivo = new File(nombreArchivo);

        if (!archivo.exists()) {
            System.out.println(" No existe archivo previo de reservas. Se iniciará con lista vacía.");
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            listaReservas.clear();
            siguienteId = 1;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";", 6);

                int id = Integer.parseInt(partes[0]);
                String nombreCliente = partes[1];
                String telefono = partes[2];
                LocalDateTime fechaReserva = LocalDateTime.parse(partes[3]);
                EstadoReserva estado = EstadoReserva.valueOf(partes[4]);

                Reserva reserva = new Reserva(id, nombreCliente, telefono, fechaReserva);
                reserva.setEstado(estado);

                if (partes.length == 6 && !partes[5].isEmpty()) {
                    reserva.setPedido(deserializarPedido(partes[5]));
                }

                listaReservas.add(reserva);

                if (id >= siguienteId) {
                    siguienteId = id + 1;
                }
            }

            br.close();
            System.out.println(" Archivo de reservas cargado correctamente.");
        } catch (IOException e) {
            System.out.println(" Error al leer el archivo de reservas.");
        } catch (Exception e) {
            System.out.println(" El archivo de reservas tiene un formato incorrecto.");
        }
    }

    // Convierte el pedido en texto para poder guardarlo en el archivo
    private String serializarPedido(List<DetalleVenta> pedido) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < pedido.size(); i++) {
            DetalleVenta d = pedido.get(i);
            Producto p = d.getProducto();

            sb.append(p.getID()).append("~")
              .append(p.getNombre()).append("~")
              .append(p.getDescripcion()).append("~")
              .append(p.getPrecio()).append("~")
              .append(d.getCantidad());

            if (i < pedido.size() - 1) {
                sb.append("|");
            }
        }

        return sb.toString();
    }

    // Reconstruye el pedido desde el texto guardado en el archivo
    private List<DetalleVenta> deserializarPedido(String textoPedido) {
        List<DetalleVenta> pedido = new ArrayList<>();

        String[] detalles = textoPedido.split("\\|");

        for (String detalleTxt : detalles) {
            String[] campos = detalleTxt.split("~");

            int idProducto = Integer.parseInt(campos[0]);
            String nombre = campos[1];
            String descripcion = campos[2];
            double precio = Double.parseDouble(campos[3]);
            int cantidad = Integer.parseInt(campos[4]);

            Producto producto = new Producto(idProducto, TipoProducto.PRODUCTO, nombre, descripcion, precio);
            DetalleVenta detalle = new DetalleVenta(producto, cantidad);
            pedido.add(detalle);
        }

        return pedido;
    }
}
