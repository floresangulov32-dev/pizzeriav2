package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.awt.Dimension;

import pizzeria.controller.GestorFinanzas;
import pizzeria.model.MovimientoCaja;
import pizzeria.model.TipoMovimiento;

public class ReporteDiarioGUI extends JPanel {
    
    private GestorFinanzas gestorFinanzas;
    private JTextArea txtReporte;
    //private JTextField txtFecha;
    //private JLabel lblTotalIngresos, lblTotalEgresos, lblBalance;
    private JButton btnGenerar;
    
    private DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public ReporteDiarioGUI() {
        gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        // Generar reporte del día actual por defecto
        LocalDate hoy = LocalDate.now();
        txtFecha.setText(hoy.toString());
        generarReporte(hoy);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Titulo
        JLabel lblTitulo = new JLabel("REPORTE DIARIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        // Panel de filtro
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setOpaque(false);
        
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtFecha = new JTextField(12);
        txtFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGenerar.setBackground(new Color(52, 152, 219));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(e -> generarReporteDesdeInput());
        
        btnHoy = new JButton("Ver Hoy");
        btnHoy.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHoy.setBackground(new Color(46, 204, 113));
        btnHoy.setForeground(Color.WHITE);
        btnHoy.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnHoy.setFocusPainted(false);
        btnHoy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHoy.addActionListener(e -> {
            LocalDate hoy = LocalDate.now();
            txtFecha.setText(hoy.toString());
            generarReporte(hoy);
        });
        
        panelFiltro.add(lblFecha);
        panelFiltro.add(txtFecha);
        panelFiltro.add(btnGenerar);
        panelFiltro.add(btnHoy);
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelFiltro, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Panel del reporte
        JPanel panelReporte = new JPanel(new BorderLayout());
        panelReporte.setOpaque(false);
        panelReporte.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Reporte del Día",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        ));
        
        txtReporte = new JTextArea();
        txtReporte.setEditable(false);
        txtReporte.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtReporte.setBackground(new Color(250, 250, 250));
        txtReporte.setForeground(Color.BLACK);
        txtReporte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(txtReporte);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        panelReporte.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de resumen
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        lblTotalIngresos = new JLabel("Total Ingresos: Bs. 0.00");
        lblTotalEgresos = new JLabel("Total Egresos: Bs. 0.00");
        lblBalance = new JLabel("Balance: Bs. 0.00");
        
        lblTotalIngresos.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalEgresos.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        lblTotalIngresos.setForeground(new Color(46, 204, 113));
        lblTotalEgresos.setForeground(new Color(231, 76, 60));
        lblBalance.setForeground(new Color(52, 152, 219));
        
        panelResumen.add(lblTotalIngresos);
        panelResumen.add(lblTotalEgresos);
        panelResumen.add(lblBalance);
        
        panel.add(panelReporte, BorderLayout.CENTER);
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
    
    private void generarReporteDesdeInput() {
        try {
            String fechaStr = txtFecha.getText().trim();
            LocalDate fecha = LocalDate.parse(fechaStr, formatterInput);
            generarReporte(fecha);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Formato de fecha inválido. Use YYYY-MM-DD",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generarReporte(LocalDate fecha) {
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.plusDays(1).atStartOfDay().minusNanos(1);
        
        double totalIngresos = gestorFinanzas.sumaIngresos(inicioDia, finDia);
        double totalEgresos = gestorFinanzas.sumaEgresos(inicioDia, finDia);
        double balance = totalIngresos - totalEgresos;
        
        // Actualizar resumen
        lblTotalIngresos.setText(String.format("Total Ingresos: Bs. %.2f", totalIngresos));
        lblTotalEgresos.setText(String.format("Total Egresos: Bs. %.2f", totalEgresos));
        
        if (balance >= 0) {
            lblBalance.setText(String.format("Balance: Bs. %.2f", balance));
            lblBalance.setForeground(new Color(46, 204, 113));
        } else {
            lblBalance.setText(String.format("Balance: Bs. %.2f (NEGATIVO)", balance));
            lblBalance.setForeground(new Color(231, 76, 60));
        }
        
        // Obtener movimientos del día
        List<MovimientoCaja> movimientos = gestorFinanzas.getMovimientosPeriodo(inicioDia, finDia);
        
        // Construir el reporte
        StringBuilder reporte = new StringBuilder();
        reporte.append("=".repeat(70)).append("\n");
        reporte.append(String.format("REPORTE DIARIO - %s\n", fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        reporte.append("=".repeat(70)).append("\n\n");
        
        reporte.append("RESUMEN DEL DÍA\n");
        reporte.append("-".repeat(70)).append("\n");
        reporte.append(String.format("Ingresos totales:   Bs. %10.2f\n", totalIngresos));
        reporte.append(String.format("Egresos totales:    Bs. %10.2f\n", totalEgresos));
        reporte.append(String.format("Balance del día:    Bs. %10.2f\n", balance));
        reporte.append("\n");
        
        reporte.append("DETALLE DE MOVIMIENTOS\n");
        reporte.append("-".repeat(70)).append("\n");
        reporte.append(String.format("%-20s %-12s %-10s %-25s\n", "FECHA", "TIPO", "MONTO", "DESCRIPCION"));
        reporte.append("-".repeat(70)).append("\n");
        
        if (movimientos.isEmpty()) {
            reporte.append("No hay movimientos registrados en este día.\n");
        } else {
            // Ordenar por fecha
            movimientos.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));
            
            for (MovimientoCaja m : movimientos) {
                String tipo = m.getTipo() == TipoMovimiento.INGRESO ? "INGRESO" : "EGRESO";
                String montoStr = String.format("%.2f", m.getMonto());
                String fechaStr = m.getFecha().format(formatterFecha);
                reporte.append(String.format("%-20s %-12s Bs.%8s %-25s\n", 
                    fechaStr, tipo, montoStr, m.getDescripcion()));
            }
        }
        
        reporte.append("\n").append("=".repeat(70)).append("\n");
        reporte.append("Fin del reporte\n");
        
        txtReporte.setText(reporte.toString());
        txtReporte.setCaretPosition(0);
    }
    
    private void cerrar() {
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
        PanelFiltro = new javax.swing.JPanel();
        lblFecha = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        btnGenera = new javax.swing.JButton();
        btnHoy = new javax.swing.JButton();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelResumen = new javax.swing.JPanel();
        lblTotalIngresos = new javax.swing.JLabel();
        lblTotalEgresos = new javax.swing.JLabel();
        lblBalance = new javax.swing.JLabel();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        lblTitulo2.setText("jLabel1");

        lblFecha.setText("jLabel1");

        txtFecha.setText("jTextField1");

        btnGenera.setText("jButton1");

        btnHoy.setText("jButton1");

        javax.swing.GroupLayout PanelFiltroLayout = new javax.swing.GroupLayout(PanelFiltro);
        PanelFiltro.setLayout(PanelFiltroLayout);
        PanelFiltroLayout.setHorizontalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(lblFecha)
                .addGap(117, 117, 117)
                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(btnGenera)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHoy)
                .addGap(85, 85, 85))
        );
        PanelFiltroLayout.setVerticalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFecha)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenera)
                    .addComponent(btnHoy))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelSuperior2Layout = new javax.swing.GroupLayout(PanelSuperior2);
        PanelSuperior2.setLayout(PanelSuperior2Layout);
        PanelSuperior2Layout.setHorizontalGroup(
            PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperior2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTitulo2)
                .addGap(18, 18, 18)
                .addComponent(PanelFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelSuperior2Layout.setVerticalGroup(
            PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperior2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitulo2)
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

        lblTotalIngresos.setText("jLabel1");

        lblTotalEgresos.setText("jLabel1");

        lblBalance.setText("jLabel1");

        javax.swing.GroupLayout PanelResumenLayout = new javax.swing.GroupLayout(PanelResumen);
        PanelResumen.setLayout(PanelResumenLayout);
        PanelResumenLayout.setHorizontalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(lblTotalIngresos)
                .addGap(142, 142, 142)
                .addComponent(lblTotalEgresos)
                .addGap(234, 234, 234)
                .addComponent(lblBalance)
                .addContainerGap(350, Short.MAX_VALUE))
        );
        PanelResumenLayout.setVerticalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalIngresos)
                    .addComponent(lblTotalEgresos)
                    .addComponent(lblBalance))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 929, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCentralLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
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
                .addGap(31, 31, 31)
                .addComponent(PanelCentral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelFiltro;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelResumen;
    private javax.swing.JPanel PanelSuperior2;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnGenera;
    private javax.swing.JButton btnHoy;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblTitulo2;
    private javax.swing.JLabel lblTotalEgresos;
    private javax.swing.JLabel lblTotalIngresos;
    private javax.swing.JTextField txtFecha;
    // End of variables declaration//GEN-END:variables
}
