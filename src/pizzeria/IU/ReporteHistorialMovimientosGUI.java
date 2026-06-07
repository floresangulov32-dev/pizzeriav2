package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
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
import pizzeria.model.MovimientoCaja;
import pizzeria.model.TipoMovimiento;

public class ReporteHistorialMovimientosGUI extends JPanel {
    
    private GestorFinanzas gestorFinanzas;
    private JTable tablaMovimientos;
    private DefaultTableModel modeloTabla;
    private JLabel lblSaldo;
//    private JButton btnCerrar;
    
    private DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public ReporteHistorialMovimientosGUI() {
        gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        cargarDatos();
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("HISTORIAL DE MOVIMIENTOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        panel.add(lblTitulo, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Lista de Movimientos",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        ));
        
        String[] columnas = {"FECHA", "TIPO", "MONTO (Bs.)", "CATEGORIA", "DESCRIPCION"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaMovimientos = new JTable(modeloTabla);
        tablaMovimientos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaMovimientos.setRowHeight(28);
        tablaMovimientos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaMovimientos.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaMovimientos.setShowGrid(true);
        tablaMovimientos.setGridColor(new Color(230, 230, 230));
        tablaMovimientos.getTableHeader().setReorderingAllowed(false);
        
        tablaMovimientos.getColumnModel().getColumn(0).setPreferredWidth(140);
        tablaMovimientos.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaMovimientos.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaMovimientos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaMovimientos.getColumnModel().getColumn(4).setPreferredWidth(250);
        
        JScrollPane scrollPane = new JScrollPane(tablaMovimientos);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de resumen
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        lblTotalIngresos = new JLabel("Total Ingresos: Bs. 0.00");
        lblTotalEgresos = new JLabel("Total Egresos: Bs. 0.00");
        lblSaldo = new JLabel("Saldo: Bs. 0.00");
        
        lblTotalIngresos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalEgresos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        lblTotalIngresos.setForeground(new Color(46, 204, 113));
        lblTotalEgresos.setForeground(new Color(231, 76, 60));
        lblSaldo.setForeground(new Color(52, 152, 219));
        
        panelResumen.add(lblTotalIngresos);
        panelResumen.add(lblTotalEgresos);
        panelResumen.add(lblSaldo);
        
        panel.add(panelTabla, BorderLayout.CENTER);
        panel.add(panelResumen, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btnCerrar = new JButton("Volver a Reportes");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(168, 27, 29));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> volverAReportes());
        
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        
        double totalIngresos = 0;
        double totalEgresos = 0;
        
        List<MovimientoCaja> movimientos = gestorFinanzas.getTodosLosMovimientos();
        
        for (MovimientoCaja m : movimientos) {
            String tipo = m.getTipo() == TipoMovimiento.INGRESO ? "INGRESO" : "EGRESO";
            String montoStr = String.format("%.2f", m.getMonto());
            String fechaStr = m.getFecha().format(formatterFecha);
            
            modeloTabla.addRow(new Object[]{
                fechaStr, tipo, montoStr, m.getCategoria(), m.getDescripcion()
            });
            
            if (m.getTipo() == TipoMovimiento.INGRESO) {
                totalIngresos += m.getMonto();
            } else {
                totalEgresos += m.getMonto();
            }
        }
        
        double saldo = totalIngresos - totalEgresos;
        
        lblTotalIngresos.setText(String.format("Total Ingresos: Bs. %.2f", totalIngresos));
        lblTotalEgresos.setText(String.format("Total Egresos: Bs. %.2f", totalEgresos));
        lblSaldo.setText(String.format("Saldo: Bs. %.2f", saldo));
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
        PanelFiltros = new javax.swing.JPanel();
        btnTodos = new javax.swing.JButton();
        btnIngresos = new javax.swing.JButton();
        btnEgresos = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelResumen = new javax.swing.JPanel();
        lblTotalIngresos = new javax.swing.JLabel();
        lblTotalEgresos = new javax.swing.JLabel();
        lblSaldoFinal = new javax.swing.JLabel();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        lblTitulo.setText("jLabel1");

        btnTodos.setText("jButton1");

        btnIngresos.setText("jButton1");

        btnEgresos.setText("jButton1");
        btnEgresos.addActionListener(this::btnEgresosActionPerformed);

        btnSalir.setText("jButton1");

        javax.swing.GroupLayout PanelFiltrosLayout = new javax.swing.GroupLayout(PanelFiltros);
        PanelFiltros.setLayout(PanelFiltrosLayout);
        PanelFiltrosLayout.setHorizontalGroup(
            PanelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltrosLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(btnTodos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addComponent(btnIngresos)
                .addGap(239, 239, 239)
                .addComponent(btnEgresos)
                .addGap(144, 144, 144)
                .addComponent(btnSalir)
                .addGap(29, 29, 29))
        );
        PanelFiltrosLayout.setVerticalGroup(
            PanelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltrosLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(PanelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTodos)
                    .addComponent(btnIngresos)
                    .addComponent(btnEgresos)
                    .addComponent(btnSalir))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(PanelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTitulo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        lblTotalIngresos.setText("jLabel1");

        lblTotalEgresos.setText("jLabel1");

        lblSaldoFinal.setText("jLabel1");

        javax.swing.GroupLayout PanelResumenLayout = new javax.swing.GroupLayout(PanelResumen);
        PanelResumen.setLayout(PanelResumenLayout);
        PanelResumenLayout.setHorizontalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(lblTotalIngresos)
                .addGap(239, 239, 239)
                .addComponent(lblTotalEgresos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 275, Short.MAX_VALUE)
                .addComponent(lblSaldoFinal)
                .addGap(212, 212, 212))
        );
        PanelResumenLayout.setVerticalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalIngresos)
                    .addComponent(lblTotalEgresos)
                    .addComponent(lblSaldoFinal))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addGroup(PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 929, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelCentralLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
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
            .addComponent(PanelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(PanelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelCentral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEgresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEgresosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEgresosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelFiltros;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelResumen;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEgresos;
    private javax.swing.JButton btnIngresos;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnTodos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblSaldoFinal;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalEgresos;
    private javax.swing.JLabel lblTotalIngresos;
    // End of variables declaration//GEN-END:variables
}
