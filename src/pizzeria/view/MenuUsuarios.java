package pizzeria.view;


import pizzeria.controller.GestorUsuarios;
import pizzeria.model.Usuario;
import pizzeria.model.Rol;

public class MenuUsuarios{
    
    private final GestorUsuarios gestorUsuarios;
    
    public MenuUsuarios(GestorUsuarios gestorUsuarios){
        this.gestorUsuarios = gestorUsuarios;
    }
    
    public void mostrar(){
        boolean enMenu = true;
        while(enMenu){
            Consola.titulo("Gestion de Usuarios");
            System.out.println(" 1. Ver todos los usuarios");
            System.out.println(" 2. Agregar usuario");
            System.out.println(" 3. Agregar cliente");
            System.out.println(" 4. Modificar usuario");
            System.out.println(" 5. Eliminar usuario");
            System.out.println(" 6. Buscar usuario por ID");
            System.out.println(" 0. Volver al menu principal");
            Consola.separador();
            
            int opcion = Consola.leerEnteroRango("Seleccione una opcion: ", 0, 6);
        
            switch(opcion){
                case 1 -> verUsuarios();
                case 2 -> agregarUsuario();
                case 3 -> agregarCliente();                
                case 4 -> modificarUsuario();
                case 5 -> eliminarUsuario();
                case 6 -> buscarUsuario();
                case 0 -> enMenu = false;
            }
        }
    }
    
    private void verUsuarios(){
        Consola.titulo("Lista de Usuarios");
        gestorUsuarios.verUsuarios();
        Consola.pausar();
    }
    
    private void agregarCliente(){
        Consola.titulo("Agregar nuevo cliente");    
        
        String nombre = Consola.leerTexto("Nombre completo del cliente: ");
        String usuario = Consola.leerTexto ("Nombre de usuario (para login): ");
        
        if(gestorUsuarios.existeUsuario(usuario)){
            System.out.println(" Ya existe un usuario con ese nombre de usuario. Operacion cancelada");
            Consola.pausar();
            return;
        }
        
        String contraseña = Consola.leerTexto("Contraseña: ");
        Rol rol = Rol.CLIENTE;
        
        System.out.println();
        System.out.println(" Resumen del nuevo cliente:");
        System.out.printf(" Nombre   : %s%n", nombre);
        System.out.printf(" Usuario  : %s%n", usuario);
        System.out.printf(" Rol      : %s%n", rol.name());
        
        if(!Consola.confirmar("¿Confimar registro?")){
            System.out.println(" Opcion cancelada.");
            Consola.pausar();
            return;
        }
        
        boolean ok = gestorUsuarios.agregar(nombre,usuario, contraseña, rol);
        
        if(ok){
            System.out.println("\n Usuario registrado correctamente."); 
        }else{
            System.out.println("\n No se pudo registrar el usuario (nombre de usuario duplicado)."); 
        }
        Consola.pausar();
    }
    
    private void agregarUsuario(){
        Consola.titulo("Agregar nuevo usuario");    
        
        String nombre = Consola.leerTexto("Nombre completo del empleado: ");
        String usuario = Consola.leerTexto ("Nombre de usuario (para login): ");
        
        if(gestorUsuarios.existeUsuario(usuario)){
            System.out.println(" Ya existe un usuario con ese nombre de usuario. Operacion cancelada");
            Consola.pausar();
            return;
        }
        
        String contraseña = Consola.leerTexto("Contraseña: ");
        Rol rol = leerRol();
        
        System.out.println();
        System.out.println(" Resumen del nuevo usuario:");
        System.out.printf(" Nombre   : %s%n", nombre);
        System.out.printf(" Usuario  : %s%n", usuario);
        System.out.printf(" Rol      : %s%n", rol.name());
        
        if(!Consola.confirmar("¿Confimar registro?")){
            System.out.println(" Opcion cancelada.");
            Consola.pausar();
            return;
        }
        
        boolean ok = gestorUsuarios.agregar(nombre,usuario, contraseña, rol);
        
        if(ok){
            System.out.println("\n Usuario registrado correctamente."); 
        }else{
            System.out.println("\n No se pudo registrar el usuario (nombre de usuario duplicado)."); 
        }
        Consola.pausar();
    }
    
    private void modificarUsuario(){
        Consola.titulo("Modificar Usuario");
        gestorUsuarios.verUsuarios();
        
        if(gestorUsuarios.getListaUsuarios().isEmpty()){
            Consola.pausar();
            return;
        }
        
        int id = Consola.leerEntero("ID del usuario a modificar (0 = cancelar): ");
        if(id == 0){
            return;
        }
        
        Usuario u = gestorUsuarios.buscarPorId(id);
        if(u == null){
            System.out.println("\n No se encontro un usuario con ese ID ");
            Consola.pausar();
            return;
        }
        
        System.out.println("\n Usuario encontrado: " + u);
        System.out.println(" (Deje en blanco los campos que NO desea cambiar)");
        System.out.println();
        
        String nuevoNombre = Consola.leerTextoOpcional("Nuevo nombre [" + u.getNombre() + "]: ");
        String nuevoUsuario = Consola.leerTextoOpcional("Nuevo usuario [" + u.getUsuario() + "]: ");
        String nuevaContra = Consola.leerTextoOpcional("Nueva contraseña (vacío = no cambiar): ");

        System.out.println("  Rol actual: " + u.getRol().name());
        Rol nuevoRol = null;
        
        if(Consola.confirmar("¿Desea cambiar el rol?")){
            nuevoRol = leerRol();            
        }
        
        if(!Consola.confirmar("¿Confirmar cambios?")){
            System.out.println(" Operacion cancelada");
            Consola.pausar();
            return;
        }
        
        boolean ok = gestorUsuarios.modificar(id, nuevoNombre.isEmpty() ? null : nuevoNombre,
                    nuevoUsuario.isEmpty() ? null : nuevoUsuario,
                    nuevaContra.isEmpty() ? null : nuevaContra,
                    nuevoRol);
                  
        if(ok){
            System.out.println("\n Usuario actualizado correctamente.");
        }else{
            System.out.println("\n No se puede actualizar (el nuevo nombre de usuario ya esta en uso).");
        }
        Consola.pausar();
    }
    
    public void eliminarUsuario(){
        Consola.titulo("Eliminar Usuario");
        gestorUsuarios.verUsuarios();
        
        if(gestorUsuarios.getListaUsuarios().isEmpty()){
            Consola.pausar();
            return;
        }
        
        int id = Consola.leerEntero("ID del usuario a eliminar (0 = cancelar): ");
        if(id == 0){
            return;
        }
        
        Usuario u = gestorUsuarios.buscarPorId(id);
        
        if(u == null){
            System.out.println("\n No se encontro un usuario con ese ID.");
            Consola.pausar();
            return; 
        }
        
        System.out.println("\n  Usuario a eliminar: " + u);
        
        if(!Consola.confirmar("¿Esta seguro de que desea elimar este usuario?")){
            System.out.println("Operacion cancelada");
            Consola.pausar();
            return;
        }
        
        boolean ok = gestorUsuarios.eliminar(id);
        if(ok){
            System.out.println("\n Usuario elimando correctamente.");
        }else{
            System.out.println("\n No se encontro el usuario");
        }
        Consola.pausar();
    }
    
    private void buscarUsuario(){
        Consola.titulo("Buscar Usuario");
        int id = Consola.leerEntero("ID del usuario a buscar: ");
        
        Usuario u = gestorUsuarios.buscarPorId(id);
        if(u == null){
            System.out.println("\n No se encontro un usuario con ID " + id + ".");   
        }else{
            System.out.println();
            System.out.println(" " + "-".repeat(60));
            System.out.printf("ID        : %d%n",u.getId());
            System.out.printf("Nombre    : %s%n",u.getNombre());
            System.out.printf("Usuario   : %s%n",u.getUsuario());
            System.out.printf("Rol       : %s%n",u.getRol().name());
            System.out.println(" " + "-".repeat(60));
        }
        Consola.pausar();
    }
    
    private Rol leerRol(){
        System.out.println();
        System.out.println("  Roles disponibles:");
        System.out.println("    1. GERENTE");
        System.out.println("    2. CAJERO");
        System.out.println("    3. COCINA");    
        int opcion = Consola.leerEnteroRango("  Seleccione el rol: ", 1, 3);
        return switch (opcion) {
            case 1 -> Rol.GERENTE;
            case 2 -> Rol.CAJERO;
            case 3 -> Rol.COCINA;
            default -> Rol.CLIENTE;
        };
    }
    
    
}
