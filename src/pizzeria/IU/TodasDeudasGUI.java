package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import pizzeria.controller.GestorFinanzas;
import pizzeria.model.Deuda;

public class TodasDeudasGUI extends JPanel {
    
    private GestorFinanzas gestorFinanzas;
    private JTable tablaDeudas;
    private DefaultTableModel modeloTabla;
//    private JButton btnTodas, btnPendientes, btnPagadas, btnSalir, btnCerrar;
    private JLabel lblTotalDeudas, lblTotalPendientes, lblTotalPagadas;
    
    private List<Deuda> deudasOriginales;
    private List<Deuda> deudasFiltradas;
    private String filtroActual = "TODAS";
    
    public TodasDeudasGUI() {
        gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();
        deudasOriginales = gestorFinanzas.getTodasLasDeudas();
        deudasFiltradas = new ArrayList<>(deudasOriginales);
        
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
        
        // Titulo
        JLabel lblTitulo = new JLabel("TODAS LAS DEUDAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setOpaque(false);
        
        btnTodas = crearBotonFiltro("Todas", new Color(52, 152, 219));
        btnTodas.addActionListener(e -> filtrar("TODAS"));
        
        btnPendientes = crearBotonFiltro("Pendientes", new Color(241, 196, 15));
        btnPendientes.addActionListener(e -> filtrar("PENDIENTES"));
        
        btnPagadas = crearBotonFiltro("Pagadas", new Color(46, 204, 113));
        btnPagadas.addActionListener(e -> filtrar("PAGADAS"));
        
        panelFiltros.add(btnTodas);
        panelFiltros.add(btnPendientes);
        panelFiltros.add(btnPagadas);
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelFiltros, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton crearBotonFiltro(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Panel de tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Lista de Deudas",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        ));

        // Configurar tabla
        String[] columnas = {"ID", "TIPO", "DESCRIPCION", "MONTO (Bs.)", "PROVEEDOR", "FECHA VENCIMIENTO", "ESTADO"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDeudas = new JTable(modeloTabla);
        tablaDeudas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaDeudas.setRowHeight(28);
        tablaDeudas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaDeudas.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaDeudas.setShowGrid(true);
        tablaDeudas.setGridColor(new Color(230, 230, 230));

        // ========== LINEA IMPORTANTE ==========
        // Evita que las columnas se puedan mover
        tablaDeudas.getTableHeader().setReorderingAllowed(false);
        // ======================================

        // Anchos de columnas
        tablaDeudas.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaDeudas.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaDeudas.getColumnModel().getColumn(0).setMinWidth(40);

        tablaDeudas.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaDeudas.getColumnModel().getColumn(2).setPreferredWidth(180);
        tablaDeudas.getColumnModel().getColumn(3).setPreferredWidth(90);
        tablaDeudas.getColumnModel().getColumn(4).setPreferredWidth(120);
        tablaDeudas.getColumnModel().getColumn(5).setPreferredWidth(120);
        tablaDeudas.getColumnModel().getColumn(6).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(tablaDeudas);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de resumen (abajo de la tabla)
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        lblTotalDeudas = new JLabel("Total Deudas: Bs. 0.00");
        lblTotalPendientes = new JLabel("Pendientes: Bs. 0.00");
        lblTotalPagadas = new JLabel("Pagadas: Bs. 0.00");
        
        lblTotalDeudas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalPendientes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalPagadas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        lblTotalDeudas.setForeground(new Color(52, 152, 219));
        lblTotalPendientes.setForeground(new Color(241, 196, 15));
        lblTotalPagadas.setForeground(new Color(46, 204, 113));
        
        panelResumen.add(lblTotalDeudas);
        panelResumen.add(lblTotalPendientes);
        panelResumen.add(lblTotalPagadas);
        
        panel.add(panelTabla, BorderLayout.CENTER);
        panel.add(panelResumen, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(168, 27, 29));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> cerrar());
        
        panel.add(btnCerrar);
        
        return panel;
    }
    
    public void filtrar(String tipo) {
        filtroActual = tipo;
        
        deudasFiltradas.clear();
        
        if (tipo.equals("TODAS")) {
            deudasFiltradas.addAll(deudasOriginales);
        } else if (tipo.equals("PENDIENTES")) {
            for (Deuda d : deudasOriginales) {
                if (d.esPendiente()) {
                    deudasFiltradas.add(d);
                }
            }
        } else if (tipo.equals("PAGADAS")) {
            for (Deuda d : deudasOriginales) {
                if (!d.esPendiente()) {
                    deudasFiltradas.add(d);
                }
            }
        }
        
        cargarTabla();
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        
        double totalDeudas = 0;
        double totalPendientes = 0;
        double totalPagadas = 0;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Deuda d : deudasFiltradas) {
            String estado = d.esPendiente() ? "PENDIENTE" : "PAGADA";
            String fechaVencimiento = d.getFechaCompromiso().format(formatter);
            
            modeloTabla.addRow(new Object[]{
                d.getId(),
                d.getTipo(),
                d.getDescripcion(),
                String.format("%.2f", d.getMontoTotal()),
                d.getProveedor(),
                fechaVencimiento,
                estado
            });
            
            totalDeudas += d.getMontoTotal();
            if (d.esPendiente()) {
                totalPendientes += d.getMontoTotal();
            } else {
                totalPagadas += d.getMontoTotal();
            }
        }
        
        lblTotalDeudas.setText(String.format("Total Deudas: Bs. %.2f", totalDeudas));
        lblTotalPendientes.setText(String.format("Pendientes: Bs. %.2f", totalPendientes));
        lblTotalPagadas.setText(String.format("Pagadas: Bs. %.2f", totalPagadas));
        
        // Resaltar botón activo
        btnTodas.setBackground(new Color(52, 152, 219));
        btnPendientes.setBackground(new Color(241, 196, 15));
        btnPagadas.setBackground(new Color(46, 204, 113));
        
        if (filtroActual.equals("TODAS")) {
            btnTodas.setBackground(btnTodas.getBackground().darker());
        } else if (filtroActual.equals("PENDIENTES")) {
            btnPendientes.setBackground(btnPendientes.getBackground().darker());
        } else if (filtroActual.equals("PAGADAS")) {
            btnPagadas.setBackground(btnPagadas.getBackground().darker());
        }
    }
    
    private void cerrar() {
        // Volver al panel de finanzas
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new GestionFinanzasGUI(), BorderLayout.CENTER);
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

        PanelSuperior2 = new javax.swing.JPanel();
        lblTitulo2 = new javax.swing.JLabel();
        PanelFiltros2 = new javax.swing.JPanel();
        btnTodas = new javax.swing.JButton();
        btnPendientes = new javax.swing.JButton();
        btnPagadas = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        lblTitulo2.setText("jLabel1");

        btnTodas.setText("jButton1");

        btnPendientes.setText("jButton1");

        btnPagadas.setText("jButton1");
        btnPagadas.addActionListener(this::btnPagadasActionPerformed);

        btnSalir.setText("jButton1");

        javax.swing.GroupLayout PanelFiltros2Layout = new javax.swing.GroupLayout(PanelFiltros2);
        PanelFiltros2.setLayout(PanelFiltros2Layout);
        PanelFiltros2Layout.setHorizontalGroup(
            PanelFiltros2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltros2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(btnTodas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addComponent(btnPendientes)
                .addGap(239, 239, 239)
                .addComponent(btnPagadas)
                .addGap(144, 144, 144)
                .addComponent(btnSalir)
                .addGap(29, 29, 29))
        );
        PanelFiltros2Layout.setVerticalGroup(
            PanelFiltros2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltros2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(PanelFiltros2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTodas)
                    .addComponent(btnPendientes)
                    .addComponent(btnPagadas)
                    .addComponent(btnSalir))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelSuperior2Layout = new javax.swing.GroupLayout(PanelSuperior2);
        PanelSuperior2.setLayout(PanelSuperior2Layout);
        PanelSuperior2Layout.setHorizontalGroup(
            PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperior2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperior2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(PanelFiltros2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTitulo2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelSuperior2Layout.setVerticalGroup(
            PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperior2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelFiltros2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 929, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        btnCerrar.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(334, 334, 334))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelSuperior2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelSuperior2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelCentral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnPagadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagadasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPagadasActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelFiltros;
    private javax.swing.JPanel PanelFiltros1;
    private javax.swing.JPanel PanelFiltros2;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JPanel PanelSuperior1;
    private javax.swing.JPanel PanelSuperior2;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEgresos;
    private javax.swing.JButton btnEgresos1;
    private javax.swing.JButton btnIngresos;
    private javax.swing.JButton btnIngresos1;
    private javax.swing.JButton btnPagadas;
    private javax.swing.JButton btnPendientes;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnTodas;
    private javax.swing.JButton btnTodos;
    private javax.swing.JButton btnTodos1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblTitulo2;
    // End of variables declaration//GEN-END:variables
}
