package pizzeria.view;


import pizzeria.model.Inventario;
import pizzeria.view.Consola;
import pizzeria.model.Insumo;

public class ConsolaInventario{

    private final Inventario inv;

    public ConsolaInventario(){
        inv = new Inventario();
    }

    public void iniciar(){
        boolean enMenu = true;
        while(enMenu){
            Consola.titulo("INVENTARIO DE INSUMOS");
            System.out.println(" 1. Ver todos los insumos");
            System.out.println(" 2. Registrar nuevo insumo");
            System.out.println(" 3. Buscar insumo por ID");
            System.out.println(" 4. Modificar insumo");
            System.out.println(" 5. Actualizar stock (entrada / salida)");
            System.out.println(" 6. Ver insumos bajo stock mínimo");
            System.out.println(" 7. Eliminar insumo");
            System.out.println(" 8. Guardar cambios");
            System.out.println(" 0. Volver");
            Consola.separador();

            int opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 8);

            switch(opcion){
                case 1 -> verTodos();
                case 2 -> registrarInsumo();
                case 3 -> buscarInsumo();
                case 4 -> modificarInsumo();
                case 5 -> actualizarStock();
                case 6 -> verStockBajo();
                case 7 -> eliminarInsumo();
                case 8 -> guardar();
                case 0 -> enMenu = false;
            }
        }
    }

    public Inventario getInventario(){
        return inv;
    }

    private void verTodos(){
        Consola.titulo("LISTA DE INSUMOS");
        inv.verTodo();
        Consola.pausar();
    }

    private void registrarInsumo(){
        Consola.titulo("REGISTRAR NUEVO INSUMO");

        String nombre;
        while(true){
            nombre = Consola.leerTexto("Nombre del insumo: ");
            if(!inv.existeNombre(nombre)){
                break;    
            }
            System.out.printf(" [!] Ya existe un insumo con el nombre '%s'. Ingrese otro.%n",
                    nombre);
        }

        String unidad = seleccionarUnidad();

        double stockActual  = leerPositivo("Stock inicial: ");
        double stockMinimo  = leerPositivo("Stock mínimo de alerta: ");
        double precio       = leerPositivo("Precio de compra (Bs.): ");
        double cantPorPizza = leerPositivo("Cantidad que usa una pizza/plato: ");

        Insumo nuevo = inv.agregar(nombre, unidad, stockActual, stockMinimo,
                precio, cantPorPizza);

        if(nuevo != null){
            System.out.printf(" [OK] Insumo registrado con ID %d.%n", nuevo.getId());
        }else{
            System.out.println(" [!] No se pudo registrar el insumo.");
        }
        Consola.pausar();
    }

    private void buscarInsumo(){
        Consola.titulo("BUSCAR INSUMO");
        inv.verTodo();
        int id = Consola.leerEntero("ID del insumo a buscar: ");
        Insumo encontrado = inv.buscarId(id);
        if(encontrado != null){
            System.out.println();
            System.out.println(" " + encontrado);
        }else{
            System.out.printf(" [!] No se encontró un insumo con ID %d.%n", id);
        }
        Consola.pausar();
    }

    private void modificarInsumo(){
        Consola.titulo("MODIFICAR INSUMO");

        if(inv.getInsumos().isEmpty()){
            System.out.println(" No hay insumos registrados.");
            Consola.pausar();
            return;
        }

        inv.verTodo();
        int id = Consola.leerEntero("ID del insumo a modificar (0 = cancelar): ");
        if(id == 0){
            return;
        }

        Insumo ins = inv.buscarId(id);
        if(ins == null){
            System.out.printf(" [!] No se encontró el insumo con ID %d.%n", id);
            Consola.pausar();
            return;
        }

        System.out.println();
        System.out.println(" Insumo seleccionado: " + ins);
        System.out.println(" (Presione Enter sin escribir nada para conservar el valor actual)");
        System.out.println();

        boolean cambioHecho = false;

        System.out.printf(" 1. Nombre actual       : %s%n", ins.getNombre());
        if(Consola.confirmar(" ¿Desea cambiar el nombre?")){
            String nuevoNombre;
            while(true){
                nuevoNombre = Consola.leerTexto("   Nuevo nombre: ");
                if(nuevoNombre.equalsIgnoreCase(ins.getNombre())){
                    break;    
                }
                if(!inv.existeNombre(nuevoNombre)){
                    break;
                }
                System.out.printf("   [!] Ya existe '%s'. Ingrese otro.%n", nuevoNombre);
            }
            inv.modificarNombre(id, nuevoNombre);
            cambioHecho = true;
        }

        System.out.printf("%n 2. Unidad actual        : %s%n", ins.getUnidad());
        if(Consola.confirmar("   ¿Desea cambiar la unidad?")){
            inv.modificarUnidad(id, seleccionarUnidad());
            cambioHecho = true;
        }

        System.out.printf("%n 3. Stock mínimo actual  : %.3f%n", ins.getStockMinimo());
        if(Consola.confirmar("   ¿Desea cambiar el stock mínimo?")){
            inv.modificarStockMinimo(id, leerPositivo("   Nuevo stock mínimo: "));
            cambioHecho = true;
        }

        System.out.printf("%n 4. Precio de compra     : Bs. %.2f%n", ins.getPrecioCompra());
        if(Consola.confirmar("   ¿Desea cambiar el precio?")){
            inv.modificarPrecio(id, leerPositivo("   Nuevo precio (Bs.): "));
            cambioHecho = true;
        }

        System.out.printf("%n 5. Cantidad por pizza   : %.3f%n", ins.getCantidadPorPizza());
        if(Consola.confirmar("   ¿Desea cambiar la cantidad por pizza/plato?")){
            inv.modificarUsoPorPizza(id, leerPositivo("   Nueva cantidad: "));
            cambioHecho = true;
        }

        System.out.println();
        if(cambioHecho){
            System.out.println(" [OK] Insumo actualizado.");
            System.out.println(" Estado actual: " + inv.buscarId(id));
        }else{
            System.out.println(" No se realizaron cambios.");
        }
        Consola.pausar();
    }

    private void actualizarStock(){
        Consola.titulo("ACTUALIZAR STOCK");
        if(inv.getInsumos().isEmpty()){
            System.out.println(" No hay insumos registrados.");
            Consola.pausar();
            return;
        }

        inv.verTodo();
        int id = Consola.leerEntero("ID del insumo (0 = cancelar): ");
        if(id == 0){
            return;
        }

        Insumo ins = inv.buscarId(id);
        if(ins == null){
            System.out.printf(" [!] No se encontró el insumo con ID %d.%n", id);
            Consola.pausar();
            return;
        }

        System.out.printf(" Insumo: %s  |  Stock actual: %.3f %s%n",
                ins.getNombre(), ins.getStockActual(), ins.getUnidad());

        double cantidad = leerPositivo("Cantidad a mover: ");

        System.out.println(" Tipo de movimiento:");
        System.out.println("  1. Entrada (sumar al stock)");
        System.out.println("  2. Salida  (restar del stock)");
        int tipo = Consola.leerEnteroRango("  Seleccione: ", 1, 2);

        boolean sumar = (tipo == 1);
        inv.actualizarStock(id, cantidad, sumar);

        System.out.printf(" [OK] Stock actualizado. Nuevo stock: %.3f %s%n",
                ins.getStockActual(), ins.getUnidad());

        if(ins.stockBajo()){
            System.out.printf(" [!] AVISO: el stock de '%s' está por debajo del mínimo (%.3f).%n",
                    ins.getNombre(), ins.getStockMinimo());
        }
        Consola.pausar();
    }

    private void verStockBajo(){
        Consola.titulo("INSUMOS CON STOCK BAJO");
        inv.verStockBajo();
        Consola.pausar();
    }

    private void eliminarInsumo(){
        Consola.titulo("ELIMINAR INSUMO");

        if(inv.getInsumos().isEmpty()){
            System.out.println(" No hay insumos registrados.");
            Consola.pausar();
            return;
        }

        inv.verTodo();
        int id = Consola.leerEntero("ID del insumo a eliminar (0 = cancelar): ");
        if (id == 0){
            return;    
        }

        Insumo ins = inv.buscarId(id);
        if(ins == null){
            System.out.printf(" [!] No se encontró el insumo con ID %d.%n", id);
            Consola.pausar();
            return;
        }

        System.out.println(" Insumo a eliminar: " + ins);
        if(!Consola.confirmar("¿Confirmar eliminación?")){
            System.out.println(" Operación cancelada.");
            Consola.pausar();
            return;
        }

        inv.eliminarInsumo(id);
        System.out.println(" [OK] Insumo eliminado.");
        Consola.pausar();
    }

    private void guardar(){
        inv.guardarArchivo();
        Consola.pausar();
    }
    
    private String seleccionarUnidad(){
        System.out.println();
        System.out.println(" Unidades disponibles:");
        String[] unidades = Insumo.UNIDADES_VALIDAS;
        for(int i = 0; i < unidades.length; i++){
            System.out.printf("  [%2d] %s%n", i + 1, unidades[i]);
        }
        System.out.printf("  [%2d] Otra (ingresar manualmente)%n", unidades.length + 1);
        Consola.separador();

        int op = Consola.leerEnteroRango("  Seleccione unidad: ", 1, unidades.length + 1);

        if(op <= unidades.length){
            return unidades[op - 1];
        }else{
            return Consola.leerTexto("  Ingrese la unidad: ");
        }
    }

    private double leerPositivo(String mensaje){
        while(true){
            double valor = Consola.leerDouble(mensaje);
            if(valor > 0){
                return valor;    
            }
            System.out.println(" [!] El valor debe ser mayor que cero.");
        }
    }
}