/*package pizzeria.IU;

import java.util.ArrayList;
import pizzeria.controller.GestorCocina;
import pizzeria.controller.GestorFinanzas;
import pizzeria.controller.GestorReserva;
import pizzeria.controller.GestorVenta;
import pizzeria.model.Inventario;
import pizzeria.model.Menu;
import pizzeria.model.Producto;
import pizzeria.util.ArchivoMenu;

public class ContextoVentasGUI {

    private static ContextoVentasGUI instancia;

    private final Menu menu;
    private final GestorVenta gestorVenta;
    
    private String clienteCobro = "Sin nombre";
    private String metodoPagoCobro = "Efectivo";
    private String tipoPedidoCobro = "Venta inmediata";
    private double montoRecibidoCobro = 0.0;
    private double cambioCobro = 0.0;
    private pizzeria.model.Venta ultimaVentaRegistrada;
    private pizzeria.model.Venta ventaSeleccionadaConsulta;
    private pizzeria.model.Reserva ultimaReservaRegistrada;
    private final pizzeria.controller.GestorReserva gestorReserva;

    private ContextoVentasGUI() {
        ArchivoMenu archivoMenu = new ArchivoMenu();

        // Cargar productos y combos
        ArrayList<Producto> productos = archivoMenu.cargarProductos("resources/data/productos.txt");
        ArrayList<pizzeria.model.Combo> combos = archivoMenu.cargarCombos("resources/data/combos.txt", productos);
        
        // Crear MENU correctamente
        menu = new Menu(productos, combos);
        
        // Verificar que el menú tenga productos
        System.out.println("=== CONTEXTO VENTAS GUI ===");
        System.out.println("Productos cargados en menú: " + menu.getProductos().size());
        System.out.println("Combos cargados: " + menu.getCombos().size());

        Inventario inventario = new Inventario();
        inventario.cargarArchivo("resources/data/Insumos.txt");

        GestorFinanzas gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();

        gestorReserva = new GestorReserva();
        gestorReserva.cargarArchivo("resources/data/reservas.txt");

        GestorCocina gestorCocina = new GestorCocina(inventario, menu);

        // Crear GestorVenta con el menú ya cargado
        gestorVenta = new GestorVenta(
                menu,
                inventario,
                gestorFinanzas,
                gestorReserva,
                gestorCocina
        );

        // Cargar ventas desde archivo
        gestorVenta.cargarArchivo();
        
        System.out.println("Ventas cargadas: " + gestorVenta.getListaVenta().size());
    }

    public static ContextoVentasGUI getInstancia() {
        if (instancia == null) {
            instancia = new ContextoVentasGUI();
        }
        return instancia;
    }

    public Menu getMenu() {
        return menu;
    }

    public GestorVenta getGestorVenta() {
        return gestorVenta;
    }
    
    public void guardarDatosCobro(String cliente, String metodoPago, String tipoPedido,
                              double montoRecibido, double cambio) {
        if (cliente == null || cliente.trim().isEmpty()) {
            this.clienteCobro = "Sin nombre";
        } else {
            this.clienteCobro = cliente.trim();
        }

        this.metodoPagoCobro = metodoPago;
        this.tipoPedidoCobro = tipoPedido;
        this.montoRecibidoCobro = montoRecibido;
        this.cambioCobro = cambio;
    }

    public String getClienteCobro() {
        return clienteCobro;
    }

    public String getMetodoPagoCobro() {
        return metodoPagoCobro;
    }

    public String getTipoPedidoCobro() {
        return tipoPedidoCobro;
    }

    public double getMontoRecibidoCobro() {
        return montoRecibidoCobro;
    }

    public double getCambioCobro() {
        return cambioCobro;
    }
    
    public void setUltimaVentaRegistrada(pizzeria.model.Venta venta) {
        this.ultimaVentaRegistrada = venta;
    }

    public pizzeria.model.Venta getUltimaVentaRegistrada() {
        return ultimaVentaRegistrada;
    }
    
    public void setVentaSeleccionadaConsulta(pizzeria.model.Venta venta) {
        this.ventaSeleccionadaConsulta = venta;
    }

    public pizzeria.model.Venta getVentaSeleccionadaConsulta() {
        return ventaSeleccionadaConsulta;
    }
    
    public pizzeria.controller.GestorReserva getGestorReserva() {
        return gestorReserva;
    }
    
    public void setUltimaReservaRegistrada(pizzeria.model.Reserva reserva) {
        this.ultimaReservaRegistrada = reserva;
    }

    public pizzeria.model.Reserva getUltimaReservaRegistrada() {
        return ultimaReservaRegistrada;
    }
}*/

package pizzeria.IU;

import java.util.ArrayList;
import pizzeria.controller.GestorCocina;
import pizzeria.controller.GestorFinanzas;
import pizzeria.controller.GestorReserva;
import pizzeria.controller.GestorVenta;
import pizzeria.model.Inventario;
import pizzeria.model.Menu;
import pizzeria.model.Producto;
import pizzeria.util.ArchivoMenu;

public class ContextoVentasGUI {

    private static ContextoVentasGUI instancia;

    private final Menu menu;
    private final GestorVenta gestorVenta;
    private final GestorCocina gestorCocina;  // AGREGAR ESTA VARIABLE
    
    private String clienteCobro = "Sin nombre";
    private String metodoPagoCobro = "Efectivo";
    private String tipoPedidoCobro = "Venta inmediata";
    private double montoRecibidoCobro = 0.0;
    private double cambioCobro = 0.0;
    private pizzeria.model.Venta ultimaVentaRegistrada;
    private pizzeria.model.Venta ventaSeleccionadaConsulta;
    private pizzeria.model.Reserva ultimaReservaRegistrada;
    private final pizzeria.controller.GestorReserva gestorReserva;

    private ContextoVentasGUI() {
        ArchivoMenu archivoMenu = new ArchivoMenu();

        ArrayList<Producto> productos = archivoMenu.cargarProductos("resources/data/productos.txt");
        ArrayList<pizzeria.model.Combo> combos = archivoMenu.cargarCombos("resources/data/combos.txt", productos);
        
        menu = new Menu(productos, combos);
        
        System.out.println("=== CONTEXTO VENTAS GUI ===");
        System.out.println("Productos cargados en menú: " + menu.getProductos().size());
        System.out.println("Combos cargados: " + menu.getCombos().size());

        Inventario inventario = new Inventario();
        inventario.cargarArchivo("resources/data/Insumos.txt");

        GestorFinanzas gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();

        gestorReserva = new GestorReserva();
        gestorReserva.cargarArchivo("resources/data/reservas.txt");

        // CREAR GESTOR COCINA
        gestorCocina = new GestorCocina(inventario, menu);

        gestorVenta = new GestorVenta(
                menu,
                inventario,
                gestorFinanzas,
                gestorReserva,
                gestorCocina
        );

        gestorVenta.cargarArchivo();
        
        // Reconstruir pedidos de cocina desde ventas y reservas existentes
        gestorCocina.reconstruirDesdeEstados(gestorVenta.getListaVenta(), gestorReserva.getListaReservas());
        
        System.out.println("Ventas cargadas: " + gestorVenta.getListaVenta().size());
    }

    public static ContextoVentasGUI getInstancia() {
        if (instancia == null) {
            instancia = new ContextoVentasGUI();
        }
        return instancia;
    }

    public Menu getMenu() {
        return menu;
    }

    public GestorVenta getGestorVenta() {
        return gestorVenta;
    }
    
    // AGREGAR ESTE MÉTODO
    public GestorCocina getGestorCocina() {
        return gestorCocina;
    }
    
    public void guardarDatosCobro(String cliente, String metodoPago, String tipoPedido,
                              double montoRecibido, double cambio) {
        if (cliente == null || cliente.trim().isEmpty()) {
            this.clienteCobro = "Sin nombre";
        } else {
            this.clienteCobro = cliente.trim();
        }

        this.metodoPagoCobro = metodoPago;
        this.tipoPedidoCobro = tipoPedido;
        this.montoRecibidoCobro = montoRecibido;
        this.cambioCobro = cambio;
    }

    public String getClienteCobro() {
        return clienteCobro;
    }

    public String getMetodoPagoCobro() {
        return metodoPagoCobro;
    }

    public String getTipoPedidoCobro() {
        return tipoPedidoCobro;
    }

    public double getMontoRecibidoCobro() {
        return montoRecibidoCobro;
    }

    public double getCambioCobro() {
        return cambioCobro;
    }
    
    public void setUltimaVentaRegistrada(pizzeria.model.Venta venta) {
        this.ultimaVentaRegistrada = venta;
    }

    public pizzeria.model.Venta getUltimaVentaRegistrada() {
        return ultimaVentaRegistrada;
    }
    
    public void setVentaSeleccionadaConsulta(pizzeria.model.Venta venta) {
        this.ventaSeleccionadaConsulta = venta;
    }

    public pizzeria.model.Venta getVentaSeleccionadaConsulta() {
        return ventaSeleccionadaConsulta;
    }
    
    public pizzeria.controller.GestorReserva getGestorReserva() {
        return gestorReserva;
    }
    
    public void setUltimaReservaRegistrada(pizzeria.model.Reserva reserva) {
        this.ultimaReservaRegistrada = reserva;
    }

    public pizzeria.model.Reserva getUltimaReservaRegistrada() {
        return ultimaReservaRegistrada;
    }
}