package pizzeria.view;

import pizzeria.model.Menu;
import pizzeria.model.Combo;
import pizzeria.util.ArchivoMenu;
import pizzeria.model.TipoProducto;
import pizzeria.model.Inventario;
import pizzeria.model.Insumo;

import pizzeria.model.Producto;
import java.util.ArrayList;

public class SuperMenu{

    private final Menu        menu;
    private final ArchivoMenu archivoMenu;
    private final Inventario        inventario;
    
    public SuperMenu(Menu menu, ArchivoMenu archivoMenu, Inventario inventario){
        this.menu        = menu;
        this.archivoMenu = archivoMenu;
        this.inventario  = inventario;  
    }


    public void mostrar(){
        boolean enMenu = true;
        while(enMenu){
            Consola.titulo("MENÚ Y COMBOS");
            System.out.println(" 1. Ver menú");
            System.out.println(" 2. Agregar producto");
            System.out.println(" 3. Agregar ingrediente a un producto");
            System.out.println(" 4. Eliminar producto");
            System.out.println(" 5. Crear combo");
            System.out.println(" 6. Eliminar combo");
            System.out.println(" 7. Ver productos");
            System.out.println(" 8. Ver Combos");
            System.out.println(" 0. Volver al menú principal");
            Consola.separador();

            int opcion = Consola.leerEnteroRango("Seleccione una opción: ", 0, 8);

            switch(opcion){
                case 1 -> verMenu();
                case 2 -> agregarProducto();
                case 3 -> agregarIngrediente();
                case 4 -> eliminarProducto();
                case 5 -> agregarCombo();
                case 6 -> eliminarCombo();
                case 7 -> verProductos();
                case 8 -> verCombos();
                case 0 -> enMenu = false;
            }
        }
    }

    private void verMenu(){
        Consola.titulo("MENÚ COMPLETO");
        System.out.println(menu.mostrarMenu());
        Consola.pausar();
    }
    
    private void verProductos(){
        Consola.titulo("PRODUCTOS REGISTRADOS");

        ArrayList<Producto> productos = menu.getProductos();
        if(productos.isEmpty()){
            System.out.println(" No hay productos registrados.");
            Consola.pausar();
            return;
        }

        for(Producto p : productos){
            System.out.println();
            System.out.printf("  [%02d] %s  —  Bs. %.2f%n", p.getID(), p.getNombre(), p.getPrecio());
            System.out.printf("       Descripción: %s%n",
                p.getDescripcion().isEmpty() ? "(sin descripción)" : p.getDescripcion());

            ArrayList<Integer> ids = p.getIngredientes();
            if(ids.isEmpty()){
                System.out.println("       Ingredientes: (ninguno registrado)");
            } else {
                System.out.print("       Ingredientes: ");
                StringBuilder sb = new StringBuilder();
                for(int idIng : ids){
                    String nombre = "(ID " + idIng + ")";
                    if(inventario != null){
                        Insumo ins = inventario.buscarId(idIng);
                        if(ins != null) nombre = ins.getNombre();
                    }
                    if(sb.length() > 0) sb.append(", ");
                    sb.append(nombre);
                }
                System.out.println(sb);
            }
            Consola.separador();
        }
        Consola.pausar();
    }

    private void verCombos(){
        Consola.titulo("COMBOS REGISTRADOS");
        System.out.println(menu.mostrarCombos());
        Consola.pausar();
    }
    
    private void agregarProducto(){
        Consola.titulo("AGREGAR PRODUCTO");

        String nombre      = Consola.leerTexto("Nombre del producto: ");
        String descripcion = Consola.leerTexto("Descripción del producto: ");
        double precio      = leerPrecio("Precio del producto (Bs.): ");

        System.out.println(" Tipo de producto:");
        System.out.println(" 1. " + TipoProducto.PRODUCTO.getNombre());
        System.out.println(" 2. " + TipoProducto.REFRESCO.getNombre());
        int opTipo = Consola.leerEnteroRango("Seleccione el tipo: ", 1, 2);
        TipoProducto tipo = (opTipo == 2) ? TipoProducto.REFRESCO : TipoProducto.PRODUCTO;

        menu.agregarProducto(nombre, descripcion, precio, tipo);
        Producto nuevo = menu.getProductos().get(menu.getProductos().size() - 1);
        System.out.printf(" Producto '%s' agregado con ID [%02d].%n", nuevo.getNombre(), nuevo.getID());

        // Paso 2: agregar ingredientes (insumos) al producto recién creado
        if(inventario != null && !inventario.getInsumos().isEmpty()){
            if(Consola.confirmar("¿Desea agregar ingredientes (insumos) a este producto?")){
                boolean agregando = true;
                while(agregando){
                    System.out.println();
                    System.out.println(" Insumos disponibles:");
                    System.out.println(" " + "-".repeat(68));
                    for(Insumo ins : inventario.getInsumos()){
                        System.out.printf("  [%02d] %-20s  (%s)%n",
                            ins.getId(), ins.getNombre(), ins.getUnidad());
                    }
                    System.out.println(" " + "-".repeat(68));

                    // Mostrar ingredientes ya agregados
                    if(!nuevo.getIngredientes().isEmpty()){
                        System.out.print(" Ya agregados: ");
                        StringBuilder sb = new StringBuilder();
                        for(int idIng : nuevo.getIngredientes()){
                            Insumo ins = inventario.buscarId(idIng);
                            if(sb.length() > 0) sb.append(", ");
                            sb.append(ins != null ? ins.getNombre() : "(ID " + idIng + ")");
                        }
                        System.out.println(sb);
                    }

                    System.out.println(" (0 para terminar)");
                    int idIng = Consola.leerEntero("ID del insumo a agregar: ");

                    if(idIng == 0){
                        agregando = false;
                    } else {
                        Insumo ins = inventario.buscarId(idIng);
                        if(ins == null){
                            System.out.printf(" No existe un insumo con ID %d.%n", idIng);
                        } else if(nuevo.getIngredientes().contains(idIng)){
                            System.out.printf(" '%s' ya está agregado.%n", ins.getNombre());
                        } else {
                            nuevo.agregarIngrediente(idIng);
                            System.out.printf(" Ingrediente '%s' agregado.%n", ins.getNombre());
                        }
                    }
                }
            }
        } else {
            System.out.println(" (No hay insumos en el inventario para asignar como ingredientes)");
        }

        archivoMenu.guardarProductos(menu.getProductos());
        System.out.println(" Producto guardado correctamente.");
        Consola.pausar();
        
        
    }

    private void agregarIngrediente(){
        Consola.titulo("AGREGAR INGREDIENTE A PRODUCTO");

        if(menu.getProductos().isEmpty()){
            System.out.println(" No hay productos registrados.");
            Consola.pausar();
            return;
        }

        // Mostrar lista de productos
        System.out.println(" Productos disponibles:");
        System.out.println(menu.mostrarProductos());
        int idProducto = Consola.leerEntero("ID del producto: ");

        Producto prod = menu.buscarProducto(idProducto);
        if(prod == null){
            System.out.printf(" No existe un producto con ID %d.%n", idProducto);
            Consola.pausar();
            return;
        }

        // Mostrar lista de insumos disponibles
        if(inventario != null && !inventario.getInsumos().isEmpty()){
            System.out.println();
            System.out.println(" Insumos disponibles:");
            System.out.println(" " + "-".repeat(68));
            for(Insumo ins : inventario.getInsumos()){
                System.out.printf("  [%02d] %-20s  (%s)%n",
                    ins.getId(), ins.getNombre(), ins.getUnidad());
            }
            System.out.println(" " + "-".repeat(68));
        } else {
            System.out.println(" (No hay insumos cargados en el inventario)");
        }

        int idIngrediente = Consola.leerEntero("ID del insumo a agregar como ingrediente: ");
        if(idIngrediente <= 0){
            System.out.println(" ID de ingrediente inválido.");
            Consola.pausar();
            return;
        }

        // Verificar que el insumo exista si hay inventario
        if(inventario != null){
            Insumo ins = inventario.buscarId(idIngrediente);
            if(ins == null){
                System.out.printf(" No existe un insumo con ID %d en el inventario.%n", idIngrediente);
                Consola.pausar();
                return;
            }
        }

        prod.agregarIngrediente(idIngrediente);
        String nombreIns = "(ID " + idIngrediente + ")";
        if(inventario != null){
            Insumo ins = inventario.buscarId(idIngrediente);
            if(ins != null) nombreIns = ins.getNombre();
        }
        System.out.printf(" Ingrediente '%s' agregado al producto '%s'.%n",
                nombreIns, prod.getNombre());
        archivoMenu.guardarProductos(menu.getProductos());
        Consola.pausar();
    }

    private void eliminarProducto(){
        Consola.titulo("ELIMINAR PRODUCTO");

        if(menu.getProductos().isEmpty()){
            System.out.println(" No hay productos registrados.");
            Consola.pausar();
            return;
        }

        System.out.println(menu.mostrarProductos());
        
        int idEliminar = Consola.leerEntero("ID del producto a eliminar (0 = cancelar): ");

        if(idEliminar == 0){
            return;    
        }

        if(idEliminar < 0){
            System.out.println(" ID inválido.");
            Consola.pausar();
            return;
        }

        Producto prod = menu.buscarProducto(idEliminar);
        if(prod == null){
            System.out.printf(" No existe un producto con ID %d.%n", idEliminar);
            Consola.pausar();
            return;
        }

        System.out.printf(" Producto a eliminar: %s%n", prod.getNombre());
        if(!Consola.confirmar("¿Confirmar eliminación?")){
            System.out.println(" Operación cancelada.");
            Consola.pausar();
            return;
        }

        menu.eliminarProducto(idEliminar);
        System.out.println(" Producto eliminado.");
        archivoMenu.guardarProductos(menu.getProductos());
        Consola.pausar();
    }


    private void agregarCombo(){
        Consola.titulo("CREAR COMBO");

        if(menu.getProductos().isEmpty()){
            System.out.println(" No hay productos para armar un combo.");
            Consola.pausar();
            return;
        }

        ArrayList<Producto> productosCombo = new ArrayList<>();

        boolean seleccionando = true;
        while(seleccionando){

            System.out.println();
            System.out.println(" Productos disponibles:");
            System.out.println(menu.mostrarProductos());

            if(!productosCombo.isEmpty()){
                System.out.println(" Productos ya en el combo:");
                for(Producto p : productosCombo){
                    System.out.printf("   + [%d] %s%n", p.getID(), p.getNombre());
                }
            }

            System.out.println(" (0 para terminar la selección)");
            Consola.separador();

            int id = Consola.leerEntero("ID del producto a agregar: ");

            if(id == 0){
                if(productosCombo.isEmpty()){
                    System.out.println(" El combo no tiene ningún producto.");
                    if(Consola.confirmar("¿Desea cancelar la creación del combo?")){
                        System.out.println(" Creación de combo cancelada.");
                        Consola.pausar();
                        return;
                    }
                    continue;
                }
                System.out.println();
                System.out.println(" Productos seleccionados para el combo:");
                double precioSugerido = 0;
                for(Producto p : productosCombo){
                    System.out.printf("   + %-22s  Bs. %.2f%n", p.getNombre(), p.getPrecio());
                    precioSugerido += p.getPrecio();
                }
                System.out.printf(" Precio sugerido (suma): Bs. %.2f%n", precioSugerido);

                if(Consola.confirmar("¿Confirmar creación del combo?")){
                    seleccionando = false;
                } else {
                    System.out.println(" Siga agregando productos o presione 0 para cancelar.");
                }
                continue;
            }
            if(id < 0){
                System.out.println(" ID inválido. Ingrese un número positivo.");
                continue;
            }            
            Producto prod = menu.buscarProducto(id);
            if(prod == null){
                System.out.printf(" No existe un producto con ID %d.%n", id);
                continue;
            }
            boolean yaEsta = false;
            for(Producto p : productosCombo){
                if(p.getID() == prod.getID()){
                    yaEsta = true;
                    break;
                }
            }

            if(yaEsta){
                System.out.printf(" '%s' ya está en el combo.%n", prod.getNombre());
                continue;
            }

            productosCombo.add(prod);
            System.out.printf(" '%s' agregado al combo.%n", prod.getNombre());
        }
        double precioSumaProductos = 0;
        for (Producto p : productosCombo){
            precioSumaProductos += p.getPrecio();
        }

        System.out.printf("%n Precio sugerido (suma de productos): Bs. %.2f%n", precioSumaProductos);
        double precioFinal;

        if(Consola.confirmar("¿Usar ese precio? (N para ingresar precio especial)")){
            precioFinal = precioSumaProductos;
        }else{
            precioFinal = leerPrecio("Precio especial del combo (Bs.): ");
        }
        
        menu.agregarCombo(productosCombo, precioFinal);

        Combo nuevo = menu.getCombos().get(menu.getCombos().size() - 1);
        System.out.println();
        System.out.println(" Combo creado exitosamente:");
        System.out.println(nuevo.verCombo());
        archivoMenu.guardarCombos(menu.getCombos());
        Consola.pausar();
    }

    private void eliminarCombo(){
        Consola.titulo("ELIMINAR COMBO");

        if(menu.getCombos().isEmpty()){
            System.out.println(" No hay combos registrados.");
            Consola.pausar();
            return;
        }

        System.out.println(" Combos disponibles:");
        for(Combo c : menu.getCombos()){
            System.out.printf("  [%d] %d producto(s)  —  Bs. %.2f%n",
                    c.getNroCombo(), c.getCombo().size(), c.getPrecio());
        }
        Consola.separador();

        int idCombo = Consola.leerEntero("Nro. de combo a eliminar (0 = cancelar): ");
        if(idCombo == 0){
            return;    
        }

        if(idCombo < 0){
            System.out.println(" Número de combo inválido.");
            Consola.pausar();
            return;
        }

        Combo combo = menu.buscarCombo(idCombo);
        if(combo == null){
            System.out.printf(" No existe el combo #%d.%n", idCombo);
            Consola.pausar();
            return;
        }

        System.out.println(" Combo a eliminar:");
        System.out.println(combo.verCombo());

        if(!Consola.confirmar("¿Confirmar eliminación?")){
            System.out.println(" Operación cancelada.");
            Consola.pausar();
            return;
        }

        menu.eliminarCombo(idCombo);
        System.out.println(" Combo eliminado.");
        archivoMenu.guardarProductos(menu.getProductos());
        Consola.pausar();
    }

    private void guardarArchivo(){
        archivoMenu.guardarProductos(menu.getProductos());
        archivoMenu.guardarCombos(menu.getCombos());
        System.out.println(" Productos y combos guardados en archivo.");
        Consola.pausar();
    }

    private double leerPrecio(String mensaje){
        while(true){
            double valor = Consola.leerDouble(mensaje);
            if(valor > 0){
                return valor;    
            }
            System.out.println(" El precio debe ser mayor que cero.");
        }
    }
}
