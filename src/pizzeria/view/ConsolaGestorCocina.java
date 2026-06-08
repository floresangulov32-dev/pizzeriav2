package pizzeria.view;


import pizzeria.model.PedidoCocina;
import pizzeria.view.Consola;
import pizzeria.controller.GestorReserva;
import pizzeria.controller.GestorCocina;
import pizzeria.controller.GestorVenta;

public class ConsolaGestorCocina{

    private final GestorCocina gestorCocina;
    private final GestorReserva gestorReserva;
    private final GestorVenta gestorVenta;

    public ConsolaGestorCocina(GestorCocina gestorCocina, GestorReserva gestorReserva, GestorVenta gestorVenta){
        this.gestorCocina = gestorCocina;
        this.gestorReserva = gestorReserva;
        this.gestorVenta = gestorVenta;
    }

    public void mostrar(){
        boolean continuar = true;

        while(continuar){
            Consola.titulo("GESTIÓN DE COCINA");
            System.out.println("  1. Ver pedidos pendientes");
            System.out.println("  2. Tomar siguiente pedido");
            System.out.println("  3. Ver pedidos en preparación");
            System.out.println("  4. Marcar pedido como listo");
            System.out.println("  5. Ver pedidos listos");
            System.out.println("  6. Marcar pedido como entregado");
            System.out.println("  0. Volver");
            Consola.separador();

            int opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 6);

            switch(opcion){
                case 1 -> verPendientes();
                case 2 -> tomarSiguientePedido();
                case 3 -> verEnPreparacion();
                case 4 -> marcarComoListo();
                case 5 -> verListos();
                case 6 -> marcarComoEntregado();
                case 0 -> continuar = false;
            }
        }
    }

    private void verPendientes(){
        Consola.titulo("PEDIDOS PENDIENTES");
        gestorCocina.verPendientes();
        Consola.pausar();
    }

    private void tomarSiguientePedido(){
        Consola.titulo("TOMAR SIGUIENTE PEDIDO");

        PedidoCocina pedido = gestorCocina.tomarSiguientePedido();

        if(pedido == null){
            System.out.println(" No hay pedidos pendientes para tomar.");
        }else{
            System.out.println(" Pedido tomado correctamente:");
            System.out.println(pedido);
            gestorReserva.guardarArchivo("resources/data/reservas.txt");
            gestorVenta.guardarArchivo();
        }

        Consola.pausar();
    }

    private void verEnPreparacion(){
        Consola.titulo("PEDIDOS EN PREPARACIÓN");
        gestorCocina.verEnPreparacion();
        Consola.pausar();
    }

    private void marcarComoListo(){
        Consola.titulo("MARCAR PEDIDO COMO LISTO");

        int idPedido = Consola.leerEntero("Ingrese el ID del pedido de cocina: ");
        boolean ok = gestorCocina.marcarComoListo(idPedido);

        if(ok){
            System.out.println(" Pedido marcado como LISTO.");
            gestorReserva.guardarArchivo("resources/data/reservas.txt");
            gestorVenta.guardarArchivo();
        }else{
            System.out.println(" No se encontró un pedido en preparación con ese ID.");
        }

        Consola.pausar();
    }

    private void verListos(){
        Consola.titulo("PEDIDOS LISTOS");
        gestorCocina.verListos();
        Consola.pausar();
    }

    private void marcarComoEntregado(){
        Consola.titulo("MARCAR PEDIDO COMO ENTREGADO");

        int idPedido = Consola.leerEntero("Ingrese el ID del pedido de cocina: ");
        boolean ok = gestorCocina.marcarComoEntregado(idPedido);

        if(ok){
            System.out.println(" Pedido marcado como ENTREGADO.");
            gestorReserva.guardarArchivo("resources/data/reservas.txt");
            gestorVenta.guardarArchivo();
        }else{
            System.out.println(" No se encontró un pedido listo con ese ID.");
        }

        Consola.pausar();
    }
}