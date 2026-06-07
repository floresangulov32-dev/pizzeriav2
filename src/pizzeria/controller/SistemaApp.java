package pizzeria.controller;

import pizzeria.model.Menu;
import pizzeria.model.Rol;

import pizzeria.util.ArchivoMenu;
import pizzeria.view.SuperMenu;
import pizzeria.view.MenuReportes;
import pizzeria.view.MenuFinanzas;
import pizzeria.view.MenuUsuarios;
import pizzeria.view.Consola;
import pizzeria.view.ConsolaGestorCocina;
import pizzeria.view.ConsolaInventario;
import pizzeria.view.ConsolaGestorReservas;


//import pizzeria.controller.GestorReserva;
//import pizzeria.controller.GestorReportes;
//import pizzeria.controller.GestorCocina;
//import pizzeria.controller.GestorFinanzas;
//import pizzeria.controller.GestorVenta;
//import pizzeria.controller.GestorUsuarios;
import pizzeria.model.Producto;
import pizzeria.model.Usuario;
import java.util.ArrayList;

public class SistemaApp{

    private final GestorUsuarios gestorUsuarios;
    private final GestorFinanzas gestorFinanzas;
    private final Menu menu;
    private final ArchivoMenu archivoMenu;
    private final GestorVenta gestorVenta;
    private final GestorReserva gestorReserva;
    private final GestorCocina gestorCocina;
    private final GestorReportes gestorReportes;

    private final MenuUsuarios menuUsuarios;
    private final MenuFinanzas menuFinanzas;
    private final SuperMenu superMenu;
    private final MenuReportes menuReportes;

    private final ConsolaInventario consolaInventario;
    private final ConsolaGestorReservas consolaGestorReservas;
    private final ConsolaGestorCocina consolaGestorCocina;

    private static final int MAX_INTENTOS_LOGIN = 3;
    private static final String NOMBRE_LOCAL = "Pizzeria la Casa del Sabor";
    private static final String VERSION = "v5.2 - Ventas - Reservas - Cocina - Inventario - Reportes ";

    private boolean datosCargados = false;

    public SistemaApp(){
        gestorUsuarios = new GestorUsuarios();
        menuUsuarios = new MenuUsuarios(gestorUsuarios);

        gestorFinanzas = new GestorFinanzas();
        menuFinanzas = new MenuFinanzas(gestorFinanzas);

        archivoMenu = new ArchivoMenu();

        ArrayList<Producto> productos = archivoMenu.cargarProductos("resources/data/productos.txt");
        menu = new Menu(productos, archivoMenu.cargarCombos("resources/data/combos.txt", productos));
        
        consolaInventario = new ConsolaInventario();  
        
        superMenu = new SuperMenu(menu, archivoMenu, consolaInventario.getInventario());
        
        gestorReserva = new GestorReserva();
        gestorCocina = new GestorCocina(consolaInventario.getInventario(), menu);
        gestorVenta = new GestorVenta(menu, consolaInventario.getInventario(), gestorFinanzas, gestorReserva, gestorCocina);

        consolaGestorReservas = new ConsolaGestorReservas(gestorReserva, gestorCocina, gestorFinanzas, menu);
        consolaGestorCocina = new ConsolaGestorCocina(gestorCocina, gestorReserva, gestorVenta);
        
        gestorReportes = new GestorReportes(gestorVenta, gestorReserva, gestorFinanzas);
        menuReportes = new MenuReportes(gestorReportes, gestorFinanzas, menuFinanzas);
    }

    public void iniciar(){
        cargarTodos();
        mostrarBienvenida();
        crearGerenteDefecto();
        
        boolean salir = false;
        while(!salir){
            Consola.titulo("BIENVENIDO");
            System.out.println(" 1. Ver Menú (sin iniciar sesión)");
            System.out.println(" 2. Iniciar Sesión");
            System.out.println(" 0. Salir");
            Consola.separador();
            
            int opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 2);
            
            switch(opcion){
                case 1 -> mostrarMenuPublico();
                case 2 -> {
                    Usuario usuarioActual = loginConReintentos();
                    if(usuarioActual == null){
                        System.out.println(" Demasiados intentos fallidos. Volviendo al menú principal.");
                    } else {
                        iniciarSesion(usuarioActual);
                    }
                }
                case 0 -> {
                    salir = true;
                    guardarTodos();
                    System.out.println(" ¡Hasta pronto!");
                }
            }
        }
    }
    
    
    //nuevo
    private void iniciarSesion(Usuario usuarioActual){
        System.out.printf("%n  Bienvenido/a, %s (%s)%n%n",
                usuarioActual.getNombre(), usuarioActual.getRol().name());
        
        if(usuarioActual.getRol() == Rol.GERENTE){
            mostrarAlertasGerente();
        }
        
        boolean sesionActiva = true;
        while(sesionActiva){
            sesionActiva = switch(usuarioActual.getRol()){
                case GERENTE -> menuGerente(usuarioActual);
                case CAJERO -> menuCajero(usuarioActual);
                case COCINA -> menuCocina(usuarioActual);
                case CLIENTE -> menuCliente(usuarioActual);
            };
        }
    }
    
    // Nuevo método muestra el menú sin necesidad de login
    private void mostrarMenuPublico(){
        Consola.titulo("MENÚ - Pizzería la Casa del Sabor");
        System.out.println(menu.mostrarMenu());
        Consola.pausar();
    }
    
    private boolean menuCliente(Usuario u){
        Consola.titulo("MENÚ CLIENTE — " + u.getNombre());
        System.out.println(" 1. Ver menú completo");
        System.out.println(" 2. Ver combos");
        System.out.println(" 0. Cerrar sesión");
        Consola.separador();
        
        int opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 3);
        
        switch(opcion){
            case 1 -> {
                System.out.println(menu.mostrarMenu());
                Consola.pausar();
            }
            case 2 -> {
                System.out.println(menu.mostrarCombos());
                Consola.pausar();
            }    
            case 0 -> {
                System.out.println(" Cerrando sesión...");
                return false;
            }
        }
        return true;
    }

    private boolean menuGerente(Usuario u){
        double saldo = gestorFinanzas.calcularSaldo();

        Consola.titulo("MENU GERENTE — " + u.getNombre());
        System.out.printf("  Saldo en caja: S/. %.2f%n", saldo);
        Consola.separador();

        System.out.println("  1. Gestión de usuarios");
        System.out.println("  2. Finanzas");
        System.out.println("  3. Menú y combos");
        System.out.println("  4. Inventario de insumos");
        System.out.println("  5. Reportes");
        System.out.println("  0. Cerrar sesión");
        Consola.separador();

        int opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 5);

        switch(opcion){
            case 1 -> menuUsuarios.mostrar();
            case 2 -> menuFinanzas.mostrar();
            case 3 -> superMenu.mostrar();
            case 4 -> consolaInventario.iniciar();
            case 5 -> menuReportes.mostrar();
            case 0 ->{
                return false;
            }
        }
        return true;
    }

    private boolean menuCajero(Usuario u){
        Consola.titulo("MENU CAJERO — " + u.getNombre());
        System.out.println("  1. Ventas y pedidos");
        System.out.println("  2. Ver menú");
        System.out.println("  3. Administrar reservas");
        System.out.println("  0. Cerrar sesión");
        Consola.separador();

        int opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 3);

        switch(opcion){
            case 1 -> gestorVenta.menuVentas(u.getId());
            case 2 ->{
                System.out.println(menu.mostrarMenu());
                Consola.pausar();
            }
            case 3 -> consolaGestorReservas.mostrar();
            case 0 ->{
                return false;
            }
        }
        return true;
    }

    private boolean menuCocina(Usuario u){
        Consola.titulo("MENU COCINA — " + u.getNombre());
        System.out.println("  1. Gestión de cocina");
        System.out.println("  0. Cerrar sesión");
        Consola.separador();

        int opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 1);

        switch(opcion){
            case 1 -> consolaGestorCocina.mostrar();
            case 0 ->{
                return false;
            }
        }
        return true;
    }

    private Usuario loginConReintentos(){
        int intentos = 0;

        while(intentos < MAX_INTENTOS_LOGIN){
            Consola.titulo("Iniciar Sesión");
            String nombreUsuario = Consola.leerTexto("Usuario: ");
            String contraseña = Consola.leerTexto("Contraseña: ");

            Usuario u = gestorUsuarios.login(nombreUsuario, contraseña);

            if(u != null){
                return u;
            }

            intentos++;
            int restantes = MAX_INTENTOS_LOGIN - intentos;
            System.out.println();

            if(restantes > 0){
                System.out.printf(" Credenciales incorrectas. Intentos restantes: %d%n", restantes);
            }else{
                System.out.println(" Credenciales incorrectas. No quedan más intentos.");
            }

            System.out.println();
        }

        return null;
    }

    private void mostrarAlertasGerente(){
        double saldo = gestorFinanzas.calcularSaldo();
        double deudaPendiente = gestorFinanzas.totalDeudaPendiente();

        System.out.printf(" Saldo en caja        : S/. %.2f%n", saldo);
        if(deudaPendiente > 0){
            System.out.printf(" Deuda pendiente total: S/. %.2f%n", deudaPendiente);
            if(deudaPendiente > saldo){
                System.out.println();
                System.out.println(" ADVERTENCIA: Las deudas pendientes superan el saldo actual.");
                System.out.printf(" Déficit: S/. %.2f%n", deudaPendiente - saldo);
            }
        }
        if(consolaInventario.getInventario().alertaStockBajo()){
            System.out.println("Insumo con stock Bajo, Favor de Revisar");
        }
        
        
        System.out.println();
    }

    private void cargarTodos(){
        System.out.println();
        System.out.println(" Cargando datos...");
        gestorUsuarios.cargarDesdeArchivo();
        gestorFinanzas.cargarArchivos();
        gestorVenta.cargarArchivo();
        consolaInventario.getInventario().cargarArchivo("resources/data/Insumos.txt");
        gestorReserva.cargarArchivo("resources/data/deudas.txt");

        gestorCocina.reconstruirDesdeEstados(gestorVenta.getListaVenta(), gestorReserva.getListaReservas());

        datosCargados = true;
        System.out.println();
    }

    private void guardarTodos(){
        if (!datosCargados) {
            return;
        }

        System.out.println();
        System.out.println(" Guardando datos...");
        gestorUsuarios.guardarEnArchivo();
        gestorFinanzas.guardarArchivos();
        gestorVenta.guardarArchivo();
        consolaInventario.getInventario().guardarArchivo("resources/data/Insumos.txt");
        gestorReserva.guardarArchivo("resources/data/deudas.txt");
        System.out.println();
    }

    private void mostrarBienvenida(){
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.printf("  ║  %-44s║%n", " " + NOMBRE_LOCAL);
        System.out.printf("  ║  %-44s║%n", " Sistema de Gestión Integral");
        System.out.printf("  ║  %-44s║%n", " " + VERSION);
        System.out.println("  ╚══════════════════════════════════════════════╝");
        System.out.println();
    }

    private void crearGerenteDefecto(){
        if(gestorUsuarios.getListaUsuarios().isEmpty()){
            System.out.println("  [INFO] Primer arranque detectado.");
            System.out.println("  [INFO] Se creó el usuario administrador por defecto:");
            System.out.println("           Usuario   : admin");
            System.out.println("           Contraseña: admin123");
            System.out.println("           Rol       : GERENTE");
            System.out.println("  [AVISO] Cambie esta contraseña en cuanto ingrese al sistema.");
            System.out.println();

            gestorUsuarios.agregar("Administrador", "admin", "admin123", Rol.GERENTE);
            gestorUsuarios.guardarEnArchivo();
        }
        
        if(gestorUsuarios.buscarPorNombreUsuario("cliente") == null){            
            System.out.println("  [INFO] Se creó el usuario Cliente por defecto:");
            System.out.println("           Usuario   : cliente");
            System.out.println("           Contraseña: cliente123");
            System.out.println("           Rol       : CLIENTE");
            System.out.println("  [AVISO] Cambie esta contraseña en cuanto ingrese al sistema.");
            System.out.println();
            gestorUsuarios.agregar("Cliente", "cliente", "cliente123", Rol.CLIENTE);
        }
    }
}