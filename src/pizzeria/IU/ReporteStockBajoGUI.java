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

public class ReporteStockBajoGUI extends JPanel {
    
    private Inventario inventario;
    private JTable tablaInsumos;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    //private JTextField txtBuscar;    
    //private JLabel lblTotalInsumos, lblUrgentes, lblValorReponer;
    
    private List<Insumo> insumosStockBajo;
    
    public ReporteStockBajoGUI() {
        inventario = new Inventario();
        inventario.cargarArchivo("resources/data/Insumos.txt");
        
        // Filtrar solo insumos con stock bajo
        insumosStockBajo = inventario.getInsumos().stream()
            .filter(Insumo::stockBajo)
            .collect(Collectors.toList());
        
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
        
        JLabel lblTitulo = new JLabel("REPORTE DE INSUMOS CON STOCK BAJO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setOpaque(false);
        
        JLabel lblBuscar = new JLabel("Buscar por nombre o ID:");
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
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelBusqueda, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Insumos con Stock por Debajo del Mínimo",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(231, 76, 60)
        ));
        
        String[] columnas = {"ID", "NOMBRE", "UNIDAD", "STOCK ACTUAL", "STOCK MÍNIMO", "% DISPONIBLE", "PRIORIDAD"};
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
        tablaInsumos.getColumnModel().getColumn(2).setPreferredWidth(60);
        tablaInsumos.getColumnModel().getColumn(3).setPreferredWidth(90);
        tablaInsumos.getColumnModel().getColumn(4).setPreferredWidth(90);
        tablaInsumos.getColumnModel().getColumn(5).setPreferredWidth(80);
        tablaInsumos.getColumnModel().getColumn(6).setPreferredWidth(80);
        
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
        
        lblTotalInsumos = new JLabel("Total Insumos con Stock Bajo: 0");
        lblUrgentes = new JLabel("Urgentes (stock < 50%): 0");
        lblValorReponer = new JLabel("Valor Reposición: Bs. 0.00");
        
        lblTotalInsumos.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUrgentes.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblValorReponer.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        lblTotalInsumos.setForeground(new Color(231, 76, 60));
        lblUrgentes.setForeground(new Color(241, 196, 15));
        lblValorReponer.setForeground(new Color(46, 204, 113));
        
        panelResumen.add(lblTotalInsumos);
        panelResumen.add(lblUrgentes);
        panelResumen.add(lblValorReponer);
        
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
    
    private void filtrar() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        
        List<Insumo> insumosFiltrados = new ArrayList<>(insumosStockBajo);
        
        if (!busqueda.isEmpty()) {
            insumosFiltrados = insumosFiltrados.stream()
                .filter(i -> String.valueOf(i.getId()).contains(busqueda) ||
                            i.getNombre().toLowerCase().contains(busqueda))
                .collect(Collectors.toList());
        }
        
        actualizarTabla(insumosFiltrados);
    }
    
    private void cargarTabla() {
        actualizarTabla(insumosStockBajo);
    }
    
    private void actualizarTabla(List<Insumo> insumos) {
        modeloTabla.setRowCount(0);
        
        double valorReposicion = 0;
        int urgentes = 0;
        
        for (Insumo ins : insumos) {
            double porcentaje = (ins.getStockActual() / ins.getStockMinimo()) * 100;
            String prioridad;
            Color colorPrioridad;
            
            if (porcentaje < 30) {
                prioridad = "CRÍTICA";
                colorPrioridad = Color.RED;
                urgentes++;
            } else if (porcentaje < 60) {
                prioridad = "ALTA";
                colorPrioridad = new Color(241, 196, 15);
            } else {
                prioridad = "MEDIA";
                colorPrioridad = new Color(52, 152, 219);
            }
            
            modeloTabla.addRow(new Object[]{
                ins.getId(),
                ins.getNombre(),
                ins.getUnidad(),
                String.format("%.3f", ins.getStockActual()),
                String.format("%.3f", ins.getStockMinimo()),
                String.format("%.1f%%", porcentaje),
                prioridad
            });
            
            // Calcular valor necesario para reponer hasta el mínimo
            double necesidad = ins.getStockMinimo() - ins.getStockActual();
            if (necesidad > 0) {
                valorReposicion += necesidad * ins.getPrecioCompra();
            }
        }
        
        lblTotalInsumos.setText("Total Insumos con Stock Bajo: " + insumos.size());
        lblUrgentes.setText("Urgentes (stock < 50%): " + urgentes);
        lblValorReponer.setText(String.format("Valor Reposición: Bs. %.2f", valorReposicion));
        
        if (insumos.isEmpty()) {
            // No hay insumos con stock bajo
            modeloTabla.addRow(new Object[]{"-", "No hay insumos con stock bajo", "-", "-", "-", "-", "-"});
        }
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
        PanelFiltro = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelResumen = new javax.swing.JPanel();
        lblTotalInsumos = new javax.swing.JLabel();
        lblUrgentes = new javax.swing.JLabel();
        lblValorReponer = new javax.swing.JLabel();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        lblTitulo.setText("jLabel1");

        lblBuscar.setText("jLabel1");

        txtBuscar.setText("jTextField1");

        javax.swing.GroupLayout PanelFiltroLayout = new javax.swing.GroupLayout(PanelFiltro);
        PanelFiltro.setLayout(PanelFiltroLayout);
        PanelFiltroLayout.setHorizontalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(lblBuscar)
                .addGap(123, 123, 123)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelFiltroLayout.setVerticalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscar)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addComponent(PanelFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitulo)
                    .addComponent(PanelFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        lblUrgentes.setText("jLabel1");

        lblValorReponer.setText("jLabel1");

        javax.swing.GroupLayout PanelResumenLayout = new javax.swing.GroupLayout(PanelResumen);
        PanelResumen.setLayout(PanelResumenLayout);
        PanelResumenLayout.setHorizontalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(lblTotalInsumos)
                .addGap(273, 273, 273)
                .addComponent(lblUrgentes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 366, Short.MAX_VALUE)
                .addComponent(lblValorReponer)
                .addGap(75, 75, 75))
        );
        PanelResumenLayout.setVerticalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalInsumos)
                    .addComponent(lblValorReponer)
                    .addComponent(lblUrgentes))
                .addContainerGap(22, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCerrar.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(203, 203, 203))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelCentral, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelSuperior, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(PanelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelFiltro;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelResumen;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalInsumos;
    private javax.swing.JLabel lblUrgentes;
    private javax.swing.JLabel lblValorReponer;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
