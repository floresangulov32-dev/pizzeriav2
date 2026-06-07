package pizzeria.model;



public class Usuario{
    
    private int id;
    private String nombre;
    private String usuario;
    private String contraseña;
    private Rol rol;
    
    public Usuario(int id, String nombre, String usuario, String contraseña, Rol rol){
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
    }
    
    
    public String escribirTexto(){
        return id + "," + nombre + "," + usuario +"," + contraseña + ","+ rol.name();
    }
    
    public static Usuario leerTexto(String linea){
        try{
            String[] partes = linea.split(",");
            if(partes.length != 5){
                return null;
            }
            
            int id = Integer.parseInt(partes[0].trim());
            String nombre = partes[1].trim();
            String usuario = partes[2].trim();
            String contraseña = partes[3].trim();
            Rol rol = Rol.valueOf(partes[4].trim().toUpperCase());
            
            return new Usuario(id, nombre, usuario, contraseña, rol);
        }catch(Exception e){
            return null;
        }
    }
    
    
    
    public int getId(){
        return id;
    }
    public String getNombre(){
        return nombre;
    } 
    public String getUsuario(){
        return usuario;
    }
    public String getContraseña(){
        return contraseña;
    }
    public Rol getRol(){
        return rol;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setUsuario(String usuario){
        this.usuario = usuario;
    }
    public void setContraseña(String contraseña){
        this.contraseña = contraseña;
    }
    public void setRol(Rol rol){
        this.rol = rol;
    }
    
    @Override
    public String toString(){
        return String.format("ID: %-4d | Nombre: %-20s | Usuario: %-15s | Rol: %s",
                id, nombre, usuario, rol.name());
    }
}
