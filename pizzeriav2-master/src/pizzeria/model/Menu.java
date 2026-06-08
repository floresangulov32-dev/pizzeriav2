package pizzeria.model;

import pizzeria.model.Combo;
import pizzeria.util.ArchivoMenu;
import pizzeria.model.Producto;
import java.util.ArrayList;
import pizzeria.model.Combo;
import pizzeria.model.Producto;
import pizzeria.model.TipoProducto;

public class Menu{
    private ArrayList <Producto> productos;
    private ArrayList <Combo> combos;
    private int IdProducto = 1;
    private int NroCombo = 1;
    private ArchivoMenu archivoMenu;
    
    public Menu(ArrayList <Producto> pizzas, ArrayList <Combo> combos ){
        this.productos = pizzas;
        this.combos = combos;
    }
    
    public Menu(){
        productos = new ArrayList<>();
        combos = new ArrayList<>();
    }
    
    public void agregarProducto(String nombre, String descripcion, double precio, TipoProducto tipo){
        int maxId = 0;
        for(Producto p : productos){
            if(p.getID() > maxId) maxId = p.getID();
        }
        
        Producto p = new Producto(maxId + 1, tipo, nombre, descripcion, precio);
        productos.add(p);
    }
    
    public boolean eliminarProducto(int id){
        boolean res = false;
        for(int i = 0; i < productos.size(); i++){
            if(productos.get(i).getID() == id){
                productos.remove(i);
                for(Combo c : combos){
                    c.eliminarProducto(id);
                }
                res = true;
                break;
            }
        }
        return res;
    }
    
    public Producto buscarProducto(int id){
        for(Producto p : productos){
            if(p.getID() == id){
                return p;
            }
        }
        return null;
    }
    
     public ArrayList<Producto> getProductosPorTipo(TipoProducto tipo){
        ArrayList<Producto> filtrados = new ArrayList<>();
        for(Producto p : productos){
            if(p.getTipo() == tipo){
                filtrados.add(p);
            }
        }
        return filtrados;
    }
    
     public ArrayList<Producto> getProductos(){
        return productos;
    }
    
    public boolean agregarIngredienteAProducto(int idProducto, int idIngrediente){
        Producto p = buscarProducto(idProducto);
        if(p != null){
            p.agregarIngrediente(idIngrediente);
            return true;
        }
        return false;
    }
    
    public void agregarCombo(ArrayList<Producto> productosCombo, double precio){
        Combo c = new Combo(precio, productosCombo);
        combos.add(c);
    }
    
    public boolean eliminarCombo(int nro){
        boolean res = false;
        for(int i = 0; i < combos.size(); i++){
            if(combos.get(i).getNroCombo() == nro){
                combos.remove(i);
                res = true;
                break;
            }
        }
        return res;
    }
    
    public Combo buscarCombo(int nro){
        for(Combo c : combos){
            if(c.getNroCombo() == nro){
                return c;
            }
        }
        return null;
    }
    
    public ArrayList<Combo> getCombos(){
        return combos;
    }
    
    public String mostrarMenu(){
        StringBuilder res = new StringBuilder();

         ArrayList<Producto> soloProductos = getProductosPorTipo(TipoProducto.PRODUCTO);
        res.append("PRODUCTOS\n");
        res.append("----------------------------------------\n");
        if(soloProductos.isEmpty()){
            res.append("No hay productos disponibles\n");
        } else {
            for(Producto p : soloProductos){
                res.append(p.verProducto()).append("\n");
            }
        }
        res.append("\n");
 
        ArrayList<Producto> refrescos = getProductosPorTipo(TipoProducto.REFRESCO);
        res.append("REFRESCOS\n");
        res.append("----------------------------------------\n");
        if(refrescos.isEmpty()){
            res.append("No hay refrescos disponibles\n");
        } else {
            for(Producto p : refrescos){
                res.append(p.verProducto()).append("\n");
            }
        }
        res.append("\n");

        res.append("COMBOS\n");
        res.append("----------------------------------------\n");
        if (combos.isEmpty()) {
        res.append("No hay combos disponibles\n");
        } else {
            for (Combo c : combos) {
                res.append(c.verCombo()).append("\n");
            }
        }

        res.append("========================================");

        return res.toString();
    }
    
    public String mostrarProductos(){
        String res = "";
        for(Producto p : productos){
            res+= p.verProducto()+"\n";
        }
        return res;
    }
    
    public String mostrarCombos(){
        String res = "";
        for(Combo c : combos){
            res+= c.verCombo()+"\n";
        }
        return res;
    }
    
    
}
