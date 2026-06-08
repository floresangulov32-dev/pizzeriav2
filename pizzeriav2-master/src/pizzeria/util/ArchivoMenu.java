package pizzeria.util;

import pizzeria.model.Producto;
import pizzeria.model.Combo;
import pizzeria.model.TipoProducto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

public class ArchivoMenu{
    private final String archivoProductos = "resources/data/productos.txt";
    private final String archivoCombos = "resources/data/combos.txt";
    public ArrayList<Producto> cargarProductos(String archivo){
        ArrayList<Producto> productos = new ArrayList<>();

        try {
            File file = new File(archivo);
            if(!file.exists()) {
                System.out.println("Archivo de productos no encontrado. Se creará uno nuevo al guardar.");
                return productos; 
            }
            
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea = "";

            while((linea = br.readLine()) != null ){
                if(linea.trim().isEmpty() || linea.equals("PRODUCTOS")){
                    continue;
                }

                String[] datos = linea.split(",");

                if(datos.length < 5){
                    continue;
                }

                int id = Integer.parseInt(datos[0]);
                TipoProducto tipo = TipoProducto.fromString(datos[1]);
                String nombre = datos[2];
                String descripcion = datos[3];
                double precio = Double.parseDouble(datos[4]);
                
                Producto p = new Producto(id, tipo, nombre, descripcion, precio);
                
                if(datos.length >= 6 && !datos[4].isEmpty()){
                    String[] ids = datos[5].split(";");

                    for(String idTxt : ids){
                        int idIng = Integer.parseInt(idTxt);
                        p.agregarIngrediente(idIng);
                    }
                }

                productos.add(p);
                
                if(id >= Producto.getContadorId()){
                    Producto.ajustarContador(id);
                }
            }

            br.close();

        }catch(IOException e){
            System.out.println("Error al leer el archivo de productos: " + e.getMessage());
        }

        return productos;
    }
    
    public ArrayList<Combo> cargarCombos(String archivo, ArrayList<Producto> productos){
        ArrayList<Combo> combos = new ArrayList<>();

        try{
            File file = new File(archivo);
            if(!file.exists()){
                System.out.println("Archivo de combos no encontrado. Se creará uno nuevo al guardar.");
                return combos;
            }
            
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea = "";

            while((linea = br.readLine()) != null){
                if(linea.trim().isEmpty()|| linea.equals("COMBOS")){
                   continue; 
                }

                String[] datos = linea.split(",");
                if(datos.length < 3){
                    continue;
                }
                int nroCombo = Integer.parseInt(datos[0]);
                double precio = Double.parseDouble(datos[1]);

                String[] ids = datos[2].split("\\|");

                ArrayList<Producto> productosCombo = new ArrayList<>();

                for(String idtxt : ids){
                    idtxt = idtxt.trim(); 

                    if(idtxt.isEmpty()){
                        continue;     
                    }

                    int id = Integer.parseInt(idtxt);

                    for(Producto p : productos){
                        if(p.getID() == id){
                            productosCombo.add(p);
                            break;
                        }
                    }                    
                }

                Combo c = new Combo(nroCombo, precio, productosCombo);
                combos.add(c);
                
                if(nroCombo >= Combo.getContadorId()){
                    Combo.ajustarContador(nroCombo);
                }
            }

            br.close();

        }catch(IOException e){
            System.out.println("Error al leer el archivo de combos: " + e.getMessage());
        }

        return combos;
    }
    public void guardarProductos(ArrayList <Producto> productos){
        try{
            File directorio = new File(archivoProductos).getParentFile();
            if(directorio != null && !directorio.exists()){
                directorio.mkdirs();
            }
        
            BufferedWriter sc = new BufferedWriter(new FileWriter(archivoProductos));
            sc.write("PRODUCTOS");
            sc.newLine();
            for(Producto p : productos){
            
                String res = p.getID() + ","
                           + p.getTipo().name().toLowerCase() + ","
                           + p.getNombre() + ","
                           + p.getDescripcion() + ","
                           + p.getPrecio() + ",";
                ArrayList<Integer> ingredientes = p.getIngredientes();
            
                for(int i = 0; i < ingredientes.size(); i++){
                    res += ingredientes.get(i);

                    if(i < ingredientes.size() - 1){
                        res += ";";
                    }
                }
            
                sc.write(res);
                sc.newLine();
            }
        
            sc.close();
            System.out.println(" Productos guardados correctamente");
    
        }catch(IOException e){     
            System.out.println("Error al guardar productos: "+ e.getMessage());
        }
    }

    public void guardarCombos(ArrayList<Combo> combos){
        try{
            File directorio = new File(archivoCombos).getParentFile();
            if(directorio != null && !directorio.exists()){
                directorio.mkdirs();
            }
        
            BufferedWriter sc = new BufferedWriter(new FileWriter(archivoCombos));

            sc.write("COMBOS");
            sc.newLine();

            for(Combo c : combos){

                String linea = c.getNroCombo()+","+c.getPrecio() + ",";
                ArrayList<Producto> productos = c.getCombo();

                for(int i = 0; i < productos.size(); i++){
                    linea += productos.get(i).getID();
    
                    if(i < productos.size() - 1){
                        linea += "|";
                    }
                }

                sc.write(linea);
                sc.newLine();
            }

            sc.close();
            System.out.println(" Combos guardados correctamente");

        }catch(IOException e){
            System.out.println("Error al escribir combos: " + e.getMessage());
        }   
    }
}
