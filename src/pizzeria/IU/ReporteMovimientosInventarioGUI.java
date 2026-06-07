package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
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

import pizzeria.model.Inventario;
import pizzeria.model.MovimientoInventario;

public class ReporteMovimientosInventarioGUI extends JPanel {
    
    private Inventario inventario;
    private JTable tablaMovimientos;
    private DefaultTableModel modeloTabla;
    //private JTextField txtFechaInicio, txtFechaFin;
    //private JButton btnGenerar, btnUltimaSemana, btnUltimoMes, btnCerrar;
    //private JLabel lblTotalMovimientos, lblTotalEntradas, lblTotalSalidas;
    
    private DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public ReporteMovimientosInventarioGUI() {
        inventario = new Inventario();
        inventario.cargarArchivo("resources/data/Insumos.txt");
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        // Establecer fechas por defecto (últimos 7 días)
        LocalDate hoy = LocalDate.now();
        txtFechaInicio.setText(hoy.minusDays(6).toString());
        txtFechaFin.setText(hoy.toString());
        generarReporte();
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("REPORTE DE MOVIMIENTOS DE INVENTARIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setOpaque(false);
        
        JLabel lblFechaInicio = new JLabel("Fecha Inicio (YYYY-MM-DD):");
        lblFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        txtFechaInicio = new JTextField(10);
        txtFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel lblFechaFin = new JLabel("Fecha Fin (YYYY-MM-DD):");
        lblFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        txtFechaFin = new JTextField(10);
        txtFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnGenerar.setBackground(new Color(52, 152, 219));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(e -> generarReporte());
        
        btnUltimaSemana = new JButton("Última Semana");
        btnUltimaSemana.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnUltimaSemana.setBackground(new Color(46, 204, 113));
        btnUltimaSemana.setForeground(Color.WHITE);
        btnUltimaSemana.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnUltimaSemana.setFocusPainted(false);
        btnUltimaSemana.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUltimaSemana.addActionListener(e -> cargarUltimaSemana());
        
        btnUltimoMes = new JButton("Último Mes");
        btnUltimoMes.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnUltimoMes.setBackground(new Color(46, 204, 113));
        btnUltimoMes.setForeground(Color.WHITE);
        btnUltimoMes.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnUltimoMes.setFocusPainted(false);
        btnUltimoMes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUltimoMes.addActionListener(e -> cargarUltimoMes());
        
        panelFiltro.add(lblFechaInicio);
        panelFiltro.add(txtFechaInicio);
        panelFiltro.add(lblFechaFin);
        panelFiltro.add(txtFechaFin);
        panelFiltro.add(btnGenerar);
        panelFiltro.add(btnUltimaSemana);
        panelFiltro.add(btnUltimoMes);
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelFiltro, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Movimientos de Inventario",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        ));
        
        String[] columnas = {"FECHA", "INSUMO", "TIPO MOVIMIENTO", "CANTIDAD", "STOCK RESULTANTE", "USUARIO", "OBSERVACIÓN"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaMovimientos = new JTable(modeloTabla);
        tablaMovimientos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tablaMovimientos.setRowHeight(24);
        tablaMovimientos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tablaMovimientos.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaMovimientos.getTableHeader().setReorderingAllowed(false);
        
        // Anchos de columnas
        tablaMovimientos.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaMovimientos.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaMovimientos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaMovimientos.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaMovimientos.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaMovimientos.getColumnModel().getColumn(5).setPreferredWidth(100);
        tablaMovimientos.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(tablaMovimientos);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de resumen
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 8));
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(8, 0, 0, 0));
        
        lblTotalMovimientos = new JLabel("Total Movimientos: 0");
        lblTotalEntradas = new JLabel("Entradas: 0");
        lblTotalSalidas = new JLabel("Salidas: 0");
        
        lblTotalMovimientos.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTotalEntradas.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTotalSalidas.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        lblTotalMovimientos.setForeground(new Color(52, 152, 219));
        lblTotalEntradas.setForeground(new Color(46, 204, 113));
        lblTotalSalidas.setForeground(new Color(231, 76, 60));
        
        panelResumen.add(lblTotalMovimientos);
        panelResumen.add(lblTotalEntradas);
        panelResumen.add(lblTotalSalidas);
        
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
    
    private void cargarUltimaSemana() {
        LocalDate hoy = LocalDate.now();
        txtFechaInicio.setText(hoy.minusDays(6).toString());
        txtFechaFin.setText(hoy.toString());
        generarReporte();
    }
    
    private void cargarUltimoMes() {
        LocalDate hoy = LocalDate.now();
        txtFechaInicio.setText(hoy.minusDays(29).toString());
        txtFechaFin.setText(hoy.toString());
        generarReporte();
    }
    
    private void generarReporte() {
        try {
            String inicioStr = txtFechaInicio.getText().trim();
            String finStr = txtFechaFin.getText().trim();
            
            if (inicioStr.isEmpty() || finStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese ambas fechas.",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate inicio = LocalDate.parse(inicioStr, formatterInput);
            LocalDate fin = LocalDate.parse(finStr, formatterInput);
            
            if (fin.isBefore(inicio)) {
                JOptionPane.showMessageDialog(this,
                    "La fecha fin no puede ser anterior a la fecha inicio.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<MovimientoInventario> movimientos = inventario.getMovimientosPeriodo(inicio, fin);
            
            modeloTabla.setRowCount(0);
            
            int totalEntradas = 0;
            int totalSalidas = 0;
            
            for (MovimientoInventario m : movimientos) {
                String tipo = m.getTipoMovimiento();
                String cantidadStr = String.format("%.3f", m.getCantidad());
                String stockStr = String.format("%.3f", m.getStockResultante());
                
                modeloTabla.addRow(new Object[]{
                    m.getFechaFormateada(),
                    m.getNombreInsumo(),
                    tipo,
                    cantidadStr,
                    stockStr,
                    m.getUsuario(),
                    m.getObservacion()
                });
                
                if (tipo.equals("ENTRADA")) {
                    totalEntradas++;
                } else {
                    totalSalidas++;
                }
            }
            
            lblTotalMovimientos.setText("Total Movimientos: " + movimientos.size());
            lblTotalEntradas.setText("Entradas: " + totalEntradas);
            lblTotalSalidas.setText("Salidas: " + totalSalidas);
            
            if (movimientos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay movimientos de inventario registrados en este período.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Formato de fecha inválido. Use YYYY-MM-DD",
                "Error",
                JOptionPane.ERROR_MESSAGE);
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
        lblFechaInicio = new javax.swing.JLabel();
        txtFechaInicio = new javax.swing.JTextField();
        lblFechaFin = new javax.swing.JLabel();
        txtFechaFin = new javax.swing.JTextField();
        btnGenerar = new javax.swing.JButton();
        btnUltimaSemana = new javax.swing.JButton();
        btnUltimoMes = new javax.swing.JButton();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelResumen = new javax.swing.JPanel();
        lblTotalMovimientos = new javax.swing.JLabel();
        lblTotalEntradas = new javax.swing.JLabel();
        lblTotalSalidas = new javax.swing.JLabel();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        lblTitulo.setText("jLabel1");

        lblFechaInicio.setText("jLabel1");

        txtFechaInicio.setText("jTextField1");

        lblFechaFin.setText("jLabel1");

        txtFechaFin.setText("jTextField1");

        btnGenerar.setText("jButton1");

        btnUltimaSemana.setText("jButton1");

        btnUltimoMes.setText("jButton1");

        javax.swing.GroupLayout PanelFiltroLayout = new javax.swing.GroupLayout(PanelFiltro);
        PanelFiltro.setLayout(PanelFiltroLayout);
        PanelFiltroLayout.setHorizontalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFechaInicio)
                .addGap(62, 62, 62)
                .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(lblFechaFin)
                .addGap(42, 42, 42)
                .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btnGenerar)
                .addGap(45, 45, 45)
                .addComponent(btnUltimaSemana)
                .addGap(51, 51, 51)
                .addComponent(btnUltimoMes)
                .addContainerGap(96, Short.MAX_VALUE))
        );
        PanelFiltroLayout.setVerticalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaInicio)
                    .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFechaFin)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerar)
                    .addComponent(btnUltimaSemana)
                    .addComponent(btnUltimoMes))
                .addContainerGap(14, Short.MAX_VALUE))
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

        lblTotalMovimientos.setText("jLabel1");

        lblTotalEntradas.setText("jLabel1");

        lblTotalSalidas.setText("jLabel1");

        javax.swing.GroupLayout PanelResumenLayout = new javax.swing.GroupLayout(PanelResumen);
        PanelResumen.setLayout(PanelResumenLayout);
        PanelResumenLayout.setHorizontalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblTotalMovimientos)
                .addGap(194, 194, 194)
                .addComponent(lblTotalEntradas)
                .addGap(259, 259, 259)
                .addComponent(lblTotalSalidas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelResumenLayout.setVerticalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalMovimientos)
                    .addComponent(lblTotalEntradas)
                    .addComponent(lblTotalSalidas))
                .addContainerGap(21, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        btnCerrar.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(50, 50, 50))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnCerrar)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(PanelCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelFiltro;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelResumen;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnUltimaSemana;
    private javax.swing.JButton btnUltimoMes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblFechaFin;
    private javax.swing.JLabel lblFechaInicio;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalEntradas;
    private javax.swing.JLabel lblTotalMovimientos;
    private javax.swing.JLabel lblTotalSalidas;
    private javax.swing.JTextField txtFechaFin;
    private javax.swing.JTextField txtFechaInicio;
    // End of variables declaration//GEN-END:variables
}
