package pizzeria.controller;

import pizzeria.model.Usuario;
import pizzeria.model.Rol;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios{
    
    private static final String ARCHIVO = "resources/data/usuarios.txt";
    private static final int MAX_INTENTOS = 3;
    
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private int proximoId = 1;
    
    public Usuario login(String nombreUsuario, String contraseña){
        Usuario usuario = null;
        for(Usuario u : listaUsuarios){
            if(u.getUsuario().equals(nombreUsuario) && u.getContraseña().equals(contraseña)){
                return u;
            }
        }
        return null;
    }
    
    public boolean agregar(String nombre, String nombreUsuario, String contraseña, Rol rol){
        boolean res = true;
        if(existeUsuario(nombreUsuario)){
            res = false;
        }else{
            Usuario nuevo = new Usuario(proximoId++, nombre, nombreUsuario, contraseña, rol);
            listaUsuarios.add(nuevo);    
        }
        return res;
    }
    
    public boolean eliminar(int id){
        return listaUsuarios.removeIf(u -> u.getId() == id);
    }
    
    public boolean modificar(int id, String nuevoNombre, String nuevoNombreUsuario, String nuevaContraseña, Rol nuevoRol){
        Usuario u = buscarPorId(id);
        
        if(u == null){
            return false;
        }
        
        if(nuevoNombre != null && !nuevoNombre.isBlank() ){
            u.setNombre(nuevoNombre);               
        }

        if(nuevoNombreUsuario != null && !nuevoNombreUsuario.isBlank()){
            Usuario existente = buscarPorNombreUsuario(nuevoNombreUsuario);  
            if(existente != null && existente.getId() != id){
                return false;
            }
            u.setUsuario(nuevoNombreUsuario);
        }
            
        if(nuevaContraseña != null && !nuevaContraseña.isBlank()){
            u.setContraseña(nuevaContraseña); 
        }
            
        if(nuevoRol != null){
            u.setRol(nuevoRol);
        }
        
        return true;
    }
    
    
    public Usuario buscarPorId(int id){
        Usuario res = null;
        for(Usuario u : listaUsuarios){
            if(u.getId() == id){
                res = u;
            }
        }
        return res;
    }
    
    public Usuario buscarPorNombreUsuario(String nombreUsuario){
        Usuario res = null;
        for(Usuario u : listaUsuarios){
            if(u.getUsuario().equalsIgnoreCase(nombreUsuario)){
                res = u;
            }
        }
        return res;
    }
    
    public boolean existeUsuario(String nombreUsuario){
        return buscarPorNombreUsuario(nombreUsuario) != null;
    }
    
    public List<Usuario> getListaUsuarios(){
        return new ArrayList<>(listaUsuarios);
    }
    
    public void verUsuarios(){
        if(listaUsuarios.isEmpty()){
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.println();
        System.out.println(" " + "-".repeat(70));
        System.out.printf(" %4s %-20s %-15s %-10s%n",
                            "ID", "NOMBRE", "USUARIO", "ROL");
        System.out.println(" " + "-".repeat(70)); 
        for(Usuario u : listaUsuarios){
            System.out.printf(" %4s %-20s %-15s %-10s%n",
                    u.getId(), u.getNombre(), u.getUsuario(), u.getRol().name());
        }
        System.out.println(" " + "-".repeat(70));
        System.out.printf("Total: %d usuario(s)%n%n", listaUsuarios.size());
    }
    
    public void cargarDesdeArchivo(){
        File archivo = new File(ARCHIVO);
        if(!archivo.exists()){
            System.out.println("No se encontro usuarios.txt. Se iniciara con lista vacia.");
            return;
        }
        
        listaUsuarios.clear();
        int maxId = 0;
        
        try(BufferedReader br = new BufferedReader(new FileReader(archivo))){
            
            String linea;
            int numLinea = 0;
            
            while((linea = br.readLine()) != null){
                numLinea++;
                linea = linea.trim();
                
                if(linea.isEmpty() || linea.startsWith("#")){
                    continue;
                }
                
                Usuario u = Usuario.leerTexto(linea);
                if(u != null){
                    listaUsuarios.add(u);
                    if(u.getId() > maxId){
                        maxId = u.getId();
                    }
                }else{
                    System.out.printf("Linea %d ignorada (formato invalido): %s%n)",
                                        numLinea, linea);
                }
            }
            
            proximoId = maxId + 1;
            System.out.printf("%d usuario(s) cargado(s) desde %s%n",
                    listaUsuarios.size(), ARCHIVO);
            
        }catch(IOException e){
            System.out.println("Error. No se pudo leer" + ARCHIVO +": " + e.getMessage());
        }
        
    }
    
    public void guardarEnArchivo(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))){
             bw.write("# Sistema Pizzeria - Archivo de usuarios");       
             bw.newLine();
             bw.write("# Formato: id, nombre, usuario, contraseña, ROL");
             bw.newLine();
             for(Usuario u : listaUsuarios){
                 bw.write(u.escribirTexto());
                 bw.newLine();
             }
             System.out.printf(" %d usuario(s) guardado(s) en %s%n",
                                listaUsuarios.size(), ARCHIVO);
        }catch(IOException e){
            System.out.println(" Error. No se pudo guardar " + ARCHIVO + ": " + e.getMessage());
        }
    }
    
    
    
}
