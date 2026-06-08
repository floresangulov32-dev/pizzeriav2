package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import pizzeria.model.Inventario;
import pizzeria.model.Insumo;

public class ReporteStockActualGUI extends JPanel {
    
    private Inventario inventario;
    private JTable tablaInsumos;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    //private JTextField txtBuscar;
    //private JButton btnTodos, btnSoloBajoStock, btnCerrar;
    //private JLabel lblTotalInsumos, lblStockBajo, lblValorInventario;
    
    private List<Insumo> insumosOriginales;
    private String filtroActual = "TODOS";
    
    public ReporteStockActualGUI() {
        inventario = new Inventario();
        inventario.cargarArchivo("resources/data/Insumos.txt");
        insumosOriginales = inventario.getInsumos();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        cargarTabla();
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("REPORTE DE STOCK ACTUAL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setOpaque(false);
        
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        txtBuscar = new JTextField(15);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        
        // Panel de opciones
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelOpciones.setOpaque(false);
        
        btnTodos = new JButton("Todos");
        btnTodos.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnTodos.setBackground(new Color(52, 152, 219));
        btnTodos.setForeground(Color.WHITE);
        btnTodos.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        btnTodos.setFocusPainted(false);
        btnTodos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTodos.addActionListener(e -> {
            filtroActual = "TODOS";
            btnTodos.setBackground(new Color(52, 152, 219).darker());
            btnSoloBajoStock.setBackground(new Color(230, 126, 34));
            filtrar();
        });
        
        btnSoloBajoStock = new JButton("Solo Stock Bajo");
        btnSoloBajoStock.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnSoloBajoStock.setBackground(new Color(230, 126, 34));
        btnSoloBajoStock.setForeground(Color.WHITE);
        btnSoloBajoStock.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        btnSoloBajoStock.setFocusPainted(false);
        btnSoloBajoStock.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSoloBajoStock.addActionListener(e -> {
            filtroActual = "STOCK_BAJO";
            btnSoloBajoStock.setBackground(new Color(230, 126, 34).darker());
            btnTodos.setBackground(new Color(52, 152, 219));
            filtrar();
        });
        
        panelOpciones.add(btnTodos);
        panelOpciones.add(btnSoloBajoStock);
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelBusqueda, BorderLayout.CENTER);
        panel.add(panelOpciones, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Lista de Insumos",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        ));
        
        String[] columnas = {"ID", "NOMBRE", "UNIDAD", "STOCK ACTUAL", "STOCK MÍNIMO", "ESTADO"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaInsumos = new JTable(modeloTabla);
        tablaInsumos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tablaInsumos.setRowHeight(24);
        tablaInsumos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tablaInsumos.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaInsumos.getTableHeader().setReorderingAllowed(false);
        
        // Anchos de columnas
        tablaInsumos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaInsumos.getColumnModel().getColumn(1).setPreferredWidth(180);
        tablaInsumos.getColumnModel().getColumn(2).setPreferredWidth(70);
        tablaInsumos.getColumnModel().getColumn(3).setPreferredWidth(90);
        tablaInsumos.getColumnModel().getColumn(4).setPreferredWidth(90);
        tablaInsumos.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        // Configurar sorter para búsqueda
        sorter = new TableRowSorter<>(modeloTabla);
        tablaInsumos.setRowSorter(sorter);
        
        JScrollPane scrollPane = new JScrollPane(tablaInsumos);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de resumen
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 8));
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(8, 0, 0, 0));
        
        lblTotalInsumos = new JLabel("Total Insumos: 0");
        lblStockBajo = new JLabel("Stock Bajo: 0");
        lblValorInventario = new JLabel("Valor Inventario: Bs. 0.00");
        
        lblTotalInsumos.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblStockBajo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblValorInventario.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        lblTotalInsumos.setForeground(new Color(52, 152, 219));
        lblStockBajo.setForeground(new Color(231, 76, 60));
        lblValorInventario.setForeground(new Color(46, 204, 113));
        
        panelResumen.add(lblTotalInsumos);
        panelResumen.add(lblStockBajo);
        panelResumen.add(lblValorInventario);
        
        panel.add(panelTabla, BorderLayout.CENTER);
        panel.add(panelResumen, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btnCerrar = new JButton("Volver a Reportes");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnCerrar.setBackground(new Color(168, 27, 29));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> volverAReportes());
        
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void cargarTabla() {
        actualizarResumen();
        filtrar();
    }
    
    private void filtrar() {
        List<Insumo> insumosFiltrados = new ArrayList<>(insumosOriginales);
        
        // Aplicar filtro de stock bajo si es necesario
        if (filtroActual.equals("STOCK_BAJO")) {
            insumosFiltrados = insumosFiltrados.stream()
                .filter(Insumo::stockBajo)
                .collect(Collectors.toList());
        }
        
        // Aplicar filtro de búsqueda
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (!busqueda.isEmpty()) {
            insumosFiltrados = insumosFiltrados.stream()
                .filter(i -> String.valueOf(i.getId()).contains(busqueda) ||
                            i.getNombre().toLowerCase().contains(busqueda))
                .collect(Collectors.toList());
        }
        
        // Actualizar tabla
        modeloTabla.setRowCount(0);
        
        double valorInventario = 0;
        int stockBajoCount = 0;
        
        for (Insumo ins : insumosFiltrados) {
            String estado = ins.stockBajo() ? "⚠ STOCK BAJO" : "NORMAL";
            Color colorEstado = ins.stockBajo() ? Color.RED : Color.BLACK;
            
            modeloTabla.addRow(new Object[]{
                ins.getId(),
                ins.getNombre(),
                ins.getUnidad(),
                String.format("%.3f", ins.getStockActual()),
                String.format("%.3f", ins.getStockMinimo()),
                estado
            });
            
            valorInventario += ins.getStockActual() * ins.getPrecioCompra();
            if (ins.stockBajo()) {
                stockBajoCount++;
            }
        }
        
        lblTotalInsumos.setText("Total Insumos: " + insumosFiltrados.size());
        lblStockBajo.setText("Stock Bajo: " + stockBajoCount);
        lblValorInventario.setText(String.format("Valor Inventario: Bs. %.2f", valorInventario));
        
        // Resaltar filas con stock bajo (opcional - se puede implementar con un renderer)
    }
    
    private void actualizarResumen() {
        // Este método se mantiene por si se necesita
    }
    
    private void volverAReportes() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReportesGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelSuperior = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        PanelBusqueda = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        PanelOpciones = new javax.swing.JPanel();
        btnTodos = new javax.swing.JButton();
        btnSoloBajoStock = new javax.swing.JButton();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelResumen = new javax.swing.JPanel();
        lblTotalInsumos = new javax.swing.JLabel();
        lblStockBajo = new javax.swing.JLabel();
        lblValorInventario = new javax.swing.JLabel();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        lblTitulo.setText("jLabel1");

        lblBuscar.setText("jLabel1");

        txtBuscar.setText("jTextField1");

        javax.swing.GroupLayout PanelBusquedaLayout = new javax.swing.GroupLayout(PanelBusqueda);
        PanelBusqueda.setLayout(PanelBusquedaLayout);
        PanelBusquedaLayout.setHorizontalGroup(
            PanelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBusquedaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblBuscar)
                .addGap(128, 128, 128)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelBusquedaLayout.setVerticalGroup(
            PanelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBusquedaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscar))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        btnTodos.setText("jButton1");

        btnSoloBajoStock.setText("jButton1");

        javax.swing.GroupLayout PanelOpcionesLayout = new javax.swing.GroupLayout(PanelOpciones);
        PanelOpciones.setLayout(PanelOpcionesLayout);
        PanelOpcionesLayout.setHorizontalGroup(
            PanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelOpcionesLayout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(btnTodos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSoloBajoStock)
                .addGap(232, 232, 232))
        );
        PanelOpcionesLayout.setVerticalGroup(
            PanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelOpcionesLayout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(PanelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTodos)
                    .addComponent(btnSoloBajoStock))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitulo)
                        .addGap(18, 18, 18)
                        .addComponent(PanelBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(PanelOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitulo))
                    .addComponent(PanelBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        lblTotalInsumos.setText("jLabel1");

        lblStockBajo.setText("jLabel1");

        lblValorInventario.setText("jLabel1");

        javax.swing.GroupLayout PanelResumenLayout = new javax.swing.GroupLayout(PanelResumen);
        PanelResumen.setLayout(PanelResumenLayout);
        PanelResumenLayout.setHorizontalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblTotalInsumos)
                .addGap(104, 104, 104)
                .addComponent(lblStockBajo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 345, Short.MAX_VALUE)
                .addComponent(lblValorInventario)
                .addGap(312, 312, 312))
        );
        PanelResumenLayout.setVerticalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalInsumos)
                    .addComponent(lblStockBajo)
                    .addComponent(lblValorInventario))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(PanelResumen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCerrar.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(355, 355, 355)
                .addComponent(btnCerrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(btnCerrar)
                .addContainerGap(102, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelCentral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelBusqueda;
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelOpciones;
    private javax.swing.JPanel PanelResumen;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnSoloBajoStock;
    private javax.swing.JButton btnTodos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblStockBajo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalInsumos;
    private javax.swing.JLabel lblValorInventario;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
