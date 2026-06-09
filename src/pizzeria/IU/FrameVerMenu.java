/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pizzeria.IU;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JLabel;
import java.util.ArrayList;

import pizzeria.model.Inventario;
import pizzeria.model.Menu;
import pizzeria.model.Producto;
import pizzeria.model.Combo;
import pizzeria.util.ArchivoMenu;

public class FrameVerMenu extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrameVerMenu.class.getName());
    private String nombreUsuario;
    private String rolUsuario;
    private javax.swing.JButton btnActivo = null;
    private Menu menu;
    private ArchivoMenu archivoMenu = new ArchivoMenu();
    private Inventario inventario = new Inventario();

    /**
     * Creates new form MenuGerente
     */
    public FrameVerMenu() {
    initComponents();
    setSize(1280, 720);
    setLocationRelativeTo(null);
    Encabezado.setPreferredSize(new java.awt.Dimension(1280, 100));
    PiePag.setPreferredSize(new java.awt.Dimension(1280, 47));

    cargarImagen(lblLogo, "resources/imagenes/logoCasaDelSabor.jpeg");
    cargarImagen(pizza1, "resources/imagenes/Pizza1.jpg");
    cargarImagen(pizza2, "resources/imagenes/Pizza2.jpg");
    cargarImagen(pizza3, "resources/imagenes/Pizza3.jpg");
    cargarImagen(lblrefresco, "resources/imagenes/Refrescos.jpg");
    cargarImagen(lblCombo1, "resources/imagenes/Combo1.jpg");
    cargarImagen(lblCombo2, "resources/imagenes/Combo2.jpg");
    
    archivoMenu = new ArchivoMenu();
    ArrayList<Producto> productos = archivoMenu.cargarProductos("resources/data/productos.txt");
    ArrayList<Combo> combos = archivoMenu.cargarCombos("resources/data/combos.txt", productos);
    menu = new Menu(productos, combos);
    inventario.cargarArchivo();

    cargarTablas();
}

// Constructor para llamar desde GestionMenu pasándole los datos ya cargados
public FrameVerMenu(String rol, String nombre, Menu menu, Inventario inventario, ArchivoMenu archivoMenu) {
    initComponents();
    setSize(1280, 720);
    setLocationRelativeTo(null);
    Encabezado.setPreferredSize(new java.awt.Dimension(1280, 100));
    PiePag.setPreferredSize(new java.awt.Dimension(1280, 47));
    this.rolUsuario   = rol;
    this.nombreUsuario = nombre;
    this.menu          = menu;
    this.inventario    = inventario;
    this.archivoMenu   = archivoMenu;
    mostrarUsuario();

    cargarImagen(lblLogo, "resources/imagenes/logoCasaDelSabor.jpeg");
    cargarImagen(pizza1, "resources/imagenes/Pizza1.jpg");
    cargarImagen(pizza2, "resources/imagenes/Pizza2.jpg");
    cargarImagen(pizza3, "resources/imagenes/Pizza3.jpg");
    cargarImagen(lblrefresco, "resources/imagenes/Refrescos.jpg");
    cargarImagen(lblCombo1, "resources/imagenes/Combo1.jpg");
    cargarImagen(lblCombo2, "resources/imagenes/Combo2.jpg");
    cargarTablas();
}

public FrameVerMenu(String rol, String nombre) {
    initComponents();
    setSize(1280, 720);
    setLocationRelativeTo(null);
    Encabezado.setPreferredSize(new java.awt.Dimension(1280, 100));
    PiePag.setPreferredSize(new java.awt.Dimension(1280, 47));
    this.rolUsuario   = rol;
    this.nombreUsuario = nombre;
    archivoMenu = new ArchivoMenu();
    ArrayList<Producto> productos = archivoMenu.cargarProductos("resources/data/productos.txt");
    
    ArrayList<Combo> combos = archivoMenu.cargarCombos("resources/data/combos.txt", productos);
    menu = new Menu(productos, combos);
    inventario.cargarArchivo();
    
    mostrarUsuario();

    cargarImagen(lblLogo, "resources/imagenes/logoCasaDelSabor.jpeg");
    cargarImagen(pizza1, "resources/imagenes/Pizza1.jpg");
    cargarImagen(pizza2, "resources/imagenes/Pizza2.jpg");
    cargarImagen(pizza3, "resources/imagenes/Pizza3.jpg");
    cargarImagen(lblrefresco, "resources/imagenes/Refrescos.jpg");
    cargarImagen(lblCombo1, "resources/imagenes/Combo1.jpg");
    cargarImagen(lblCombo2, "resources/imagenes/Combo2.jpg");
    cargarTablas();
}
    
    
public void cargarTablas() {
    cargarTablaPizzas();
    cargarTablaRefrescos();
    cargarTablaCombos();
}

private void cargarTablaPizzas() {
    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    for (Producto p : menu.getProductos()) {
        if (p.getTipo() == pizzeria.model.TipoProducto.PRODUCTO) {
            model.addRow(new Object[]{
                p.getID(),
                p.getNombre(),
                String.format("%.2f Bs.", p.getPrecio())
            });
        }
    }
    jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable1.getColumnModel().getColumn(1).setPreferredWidth(250);
    jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
}

private void cargarTablaRefrescos() {
    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel) jTable2.getModel();
    model.setRowCount(0);

    for (Producto p : menu.getProductos()) {
        if (p.getTipo() == pizzeria.model.TipoProducto.REFRESCO) {
            model.addRow(new Object[]{
                p.getID(),
                p.getNombre(),
                String.format("%.2f Bs.", p.getPrecio())
            });
        }
    }
    jTable2.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable2.getColumnModel().getColumn(1).setPreferredWidth(250);
    jTable2.getColumnModel().getColumn(2).setPreferredWidth(100);
}   

private void cargarTablaCombos() {
    javax.swing.table.DefaultTableModel model =
        (javax.swing.table.DefaultTableModel) jTable3.getModel();
    model.setRowCount(0);

    for (Combo c : menu.getCombos()) {
        // Construye descripción con los nombres de los productos
        StringBuilder productos = new StringBuilder();
        for (int i = 0; i < c.getCombo().size(); i++) {
            productos.append(c.getCombo().get(i).getNombre());
            if (i < c.getCombo().size() - 1) productos.append(", ");
        }

        model.addRow(new Object[]{
            "Combo #" + c.getNroCombo(),
            productos.toString(),
            String.format("%.2f Bs.", c.getPrecio())
        });
    }
    jTable3.getColumnModel().getColumn(0).setPreferredWidth(80);
    jTable3.getColumnModel().getColumn(1).setPreferredWidth(250);
    jTable3.getColumnModel().getColumn(2).setPreferredWidth(70);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Fondo = new javax.swing.JPanel();
        Encabezado = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Rol = new javax.swing.JLabel();
        PiePag = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Interfaz = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        pnlPizzas = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pizza1 = new javax.swing.JLabel();
        pizza2 = new javax.swing.JLabel();
        pizza3 = new javax.swing.JLabel();
        pnlRefrescos = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        lblrefresco = new javax.swing.JLabel();
        pnlCombos = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        lblCombo1 = new javax.swing.JLabel();
        lblCombo2 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Fondo.setBackground(new java.awt.Color(255, 255, 255));

        Encabezado.setBackground(new java.awt.Color(168, 27, 29));
        Encabezado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));

        lblTitulo.setBackground(new java.awt.Color(255, 255, 255));
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 0, 35)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("LA CASA DEL SABOR");

        lblLogo.setText("Logo");
        lblLogo.setPreferredSize(new java.awt.Dimension(94, 81));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(217, 217, 217));
        jLabel4.setText("PIZZERIA");

        Rol.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        Rol.setForeground(new java.awt.Color(255, 255, 255));
        Rol.setText("Rol: Usuario");

        javax.swing.GroupLayout EncabezadoLayout = new javax.swing.GroupLayout(Encabezado);
        Encabezado.setLayout(EncabezadoLayout);
        EncabezadoLayout.setHorizontalGroup(
            EncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(EncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Rol, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        EncabezadoLayout.setVerticalGroup(
            EncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EncabezadoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(Rol, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(EncabezadoLayout.createSequentialGroup()
                .addGroup(EncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(EncabezadoLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, EncabezadoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PiePag.setBackground(new java.awt.Color(0, 0, 0));
        PiePag.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Powered by StarTech");

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Logo");

        javax.swing.GroupLayout PiePagLayout = new javax.swing.GroupLayout(PiePag);
        PiePag.setLayout(PiePagLayout);
        PiePagLayout.setHorizontalGroup(
            PiePagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PiePagLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PiePagLayout.setVerticalGroup(
            PiePagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PiePagLayout.createSequentialGroup()
                .addGroup(PiePagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        Interfaz.setBackground(new java.awt.Color(255, 255, 255));
        Interfaz.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(168, 27, 29));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Menú - La Casa del Sabor");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(168, 27, 29));
        jLabel3.setText("Pizzas:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(168, 27, 29));
        jLabel6.setText("Refrescos:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(168, 27, 29));
        jLabel7.setText("Combos:");

        btnVolver.setBackground(new java.awt.Color(106, 68, 68));
        btnVolver.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnVolver.setForeground(new java.awt.Color(255, 255, 255));
        btnVolver.setText("Volver");
        btnVolver.addActionListener(this::btnVolverActionPerformed);

        pnlPizzas.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Pizza", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRequestFocusEnabled(false);
        jTable1.setRowHeight(30);
        jScrollPane4.setViewportView(jTable1);

        pizza1.setText("Pizza1");
        pizza1.setPreferredSize(new java.awt.Dimension(120, 114));

        pizza2.setText("Pizza2");
        pizza2.setPreferredSize(new java.awt.Dimension(120, 114));

        pizza3.setText("Pizza3");
        pizza3.setPreferredSize(new java.awt.Dimension(120, 114));

        javax.swing.GroupLayout pnlPizzasLayout = new javax.swing.GroupLayout(pnlPizzas);
        pnlPizzas.setLayout(pnlPizzasLayout);
        pnlPizzasLayout.setHorizontalGroup(
            pnlPizzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPizzasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlPizzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPizzasLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(pnlPizzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pizza1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pizza3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlPizzasLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pizza2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        pnlPizzasLayout.setVerticalGroup(
            pnlPizzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPizzasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPizzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPizzasLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(pizza1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(pizza2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(pizza3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlRefrescos.setBackground(new java.awt.Color(255, 255, 255));

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Refresco", "Precio"
            }
        ));
        jTable2.setRowHeight(30);
        jScrollPane5.setViewportView(jTable2);

        lblrefresco.setText("Refrecos");
        lblrefresco.setPreferredSize(new java.awt.Dimension(122, 120));

        javax.swing.GroupLayout pnlRefrescosLayout = new javax.swing.GroupLayout(pnlRefrescos);
        pnlRefrescos.setLayout(pnlRefrescosLayout);
        pnlRefrescosLayout.setHorizontalGroup(
            pnlRefrescosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRefrescosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(lblrefresco, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        pnlRefrescosLayout.setVerticalGroup(
            pnlRefrescosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRefrescosLayout.createSequentialGroup()
                .addGroup(pnlRefrescosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRefrescosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRefrescosLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(lblrefresco, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pnlCombos.setBackground(new java.awt.Color(255, 255, 255));

        jTable3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nro", "Descripción", "Precio"
            }
        ));
        jTable3.setRowHeight(30);
        jScrollPane6.setViewportView(jTable3);

        lblCombo1.setText("Combo1");
        lblCombo1.setPreferredSize(new java.awt.Dimension(96, 94));

        lblCombo2.setText("Combo2");
        lblCombo2.setPreferredSize(new java.awt.Dimension(96, 94));

        javax.swing.GroupLayout pnlCombosLayout = new javax.swing.GroupLayout(pnlCombos);
        pnlCombos.setLayout(pnlCombosLayout);
        pnlCombosLayout.setHorizontalGroup(
            pnlCombosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCombosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCombosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        pnlCombosLayout.setVerticalGroup(
            pnlCombosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCombosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlCombosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCombosLayout.createSequentialGroup()
                        .addComponent(lblCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(57, 57, 57))
        );

        javax.swing.GroupLayout InterfazLayout = new javax.swing.GroupLayout(Interfaz);
        Interfaz.setLayout(InterfazLayout);
        InterfazLayout.setHorizontalGroup(
            InterfazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InterfazLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InterfazLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(501, 501, 501))
            .addGroup(InterfazLayout.createSequentialGroup()
                .addComponent(pnlPizzas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(InterfazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InterfazLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(InterfazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlRefrescos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlCombos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(InterfazLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InterfazLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(538, 538, 538))
        );
        InterfazLayout.setVerticalGroup(
            InterfazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InterfazLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InterfazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InterfazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPizzas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(InterfazLayout.createSequentialGroup()
                        .addComponent(pnlRefrescos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlCombos, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout FondoLayout = new javax.swing.GroupLayout(Fondo);
        Fondo.setLayout(FondoLayout);
        FondoLayout.setHorizontalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Encabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(FondoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(Interfaz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(FondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PiePag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        FondoLayout.setVerticalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addComponent(Encabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Interfaz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PiePag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        this.dispose();
    
        if (rolUsuario == null || rolUsuario.isEmpty()) {
            // Vino desde PantallaInicial
            new PantallaInicial().setVisible(true);

        } else if (rolUsuario.equalsIgnoreCase("CLIENTE")) {
            // Vino desde InterfazCliente
            new InterfazCliente(rolUsuario, nombreUsuario).setVisible(true);

        } else if (rolUsuario.equalsIgnoreCase("GERENTE")){
            // Vino desde GestionMenu (Gerente u otro rol)
            new GestionMenu(rolUsuario, nombreUsuario).setVisible(true);
        }else {
            new VentasGUI(rolUsuario, nombreUsuario).setVisible(true);
        }
    }//GEN-LAST:event_btnVolverActionPerformed
    
    private void mostrarUsuario() {
    
        Rol.setText(rolUsuario + ": " + nombreUsuario);
    }
    
    
       
    
    
    private void cargarImagen(JLabel label, String ruta){
     label.setText("");
    try {
        java.io.File archivo = new java.io.File(ruta);
        if (!archivo.exists()) {
            System.err.println("Imagen no encontrada: " + ruta);
            return;
        }
        ImageIcon icono = new ImageIcon(archivo.getAbsolutePath());
        Image imgEscalada = icono.getImage().getScaledInstance(
            label.getPreferredSize().width,
            label.getPreferredSize().height,
            Image.SCALE_SMOOTH
        );
        label.setIcon(new ImageIcon(imgEscalada));
    } catch (Exception e) {
        System.err.println("Error cargando imagen: " + e.getMessage());
    }
    }
 
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrameVerMenu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Encabezado;
    private javax.swing.JPanel Fondo;
    private javax.swing.JPanel Interfaz;
    private javax.swing.JPanel PiePag;
    private javax.swing.JLabel Rol;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel lblCombo1;
    private javax.swing.JLabel lblCombo2;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblrefresco;
    private javax.swing.JLabel pizza1;
    private javax.swing.JLabel pizza2;
    private javax.swing.JLabel pizza3;
    private javax.swing.JPanel pnlCombos;
    private javax.swing.JPanel pnlPizzas;
    private javax.swing.JPanel pnlRefrescos;
    // End of variables declaration//GEN-END:variables
}
