package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

import pizzeria.controller.GestorVenta;
import pizzeria.model.Venta;

public class ReporteVentasPeriodoGUI extends JPanel {
    
    private GestorVenta gestorVenta;
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    //private JTextField txtFechaInicio, txtFechaFin;
    //private JButton btnGenerar, btnUltimaSemana, btnUltimoMes, btnCerrar;
    //private JLabel lblTotalVentas, lblMontoTotal, lblPromedioDiario;
    
    private DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public ReporteVentasPeriodoGUI() {
        // Obtener el GestorVenta desde el Contexto
        gestorVenta = ContextoVentasGUI.getInstancia().getGestorVenta();
        
        // Verificar que se cargaron ventas
        System.out.println("=== REPORTE VENTAS PERÍODO ===");
        System.out.println("Ventas cargadas en gestor: " + gestorVenta.getListaVenta().size());
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        // Fechas por defecto: últimos 7 días
        LocalDate hoy = LocalDate.now();
        txtFechaInicio.setText(hoy.minusDays(6).toString());
        txtFechaFin.setText(hoy.toString());
        
        // Generar reporte inicial
        generarReporte();
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("REPORTE DE VENTAS POR PERÍODO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltro.setOpaque(false);
        
        JLabel lblFechaInicio = new JLabel("Inicio (YYYY-MM-DD):");
        lblFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        txtFechaInicio = new JTextField(10);
        txtFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel lblFechaFin = new JLabel("Fin (YYYY-MM-DD):");
        lblFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        txtFechaFin = new JTextField(10);
        txtFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnGenerar.setBackground(new Color(52, 152, 219));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(e -> generarReporte());
        
        btnUltimaSemana = new JButton("Última Semana");
        btnUltimaSemana.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnUltimaSemana.setBackground(new Color(46, 204, 113));
        btnUltimaSemana.setForeground(Color.WHITE);
        btnUltimaSemana.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        btnUltimaSemana.setFocusPainted(false);
        btnUltimaSemana.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUltimaSemana.addActionListener(e -> {
            LocalDate hoy = LocalDate.now();
            txtFechaInicio.setText(hoy.minusDays(6).toString());
            txtFechaFin.setText(hoy.toString());
            generarReporte();
        });
        
        btnUltimoMes = new JButton("Último Mes");
        btnUltimoMes.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnUltimoMes.setBackground(new Color(46, 204, 113));
        btnUltimoMes.setForeground(Color.WHITE);
        btnUltimoMes.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        btnUltimoMes.setFocusPainted(false);
        btnUltimoMes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUltimoMes.addActionListener(e -> {
            LocalDate hoy = LocalDate.now();
            txtFechaInicio.setText(hoy.minusDays(29).toString());
            txtFechaFin.setText(hoy.toString());
            generarReporte();
        });
        
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
            "Lista de Ventas",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        ));
        
        String[] columnas = {"ID", "FECHA", "CLIENTE", "TOTAL (Bs.)", "MÉTODO PAGO", "ESTADO"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaVentas = new JTable(modeloTabla);
        tablaVentas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tablaVentas.setRowHeight(24);
        tablaVentas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tablaVentas.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaVentas.getTableHeader().setReorderingAllowed(false);
        
        tablaVentas.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaVentas.getColumnModel().getColumn(1).setPreferredWidth(130);
        tablaVentas.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaVentas.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaVentas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaVentas.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de resumen
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(8, 0, 0, 0));
        
        lblTotalVentas = new JLabel("Total Ventas: 0");
        lblMontoTotal = new JLabel("Monto Total: Bs. 0.00");
        lblPromedioDiario = new JLabel("Promedio Diario: Bs. 0.00");
        
        lblTotalVentas.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMontoTotal.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPromedioDiario.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        lblTotalVentas.setForeground(new Color(52, 152, 219));
        lblMontoTotal.setForeground(new Color(46, 204, 113));
        lblPromedioDiario.setForeground(new Color(155, 89, 182));
        
        panelResumen.add(lblTotalVentas);
        panelResumen.add(lblMontoTotal);
        panelResumen.add(lblPromedioDiario);
        
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
            
            LocalDateTime desde = inicio.atStartOfDay();
            LocalDateTime hasta = fin.plusDays(1).atStartOfDay().minusNanos(1);
            
            // Filtrar ventas por fecha
            List<Venta> ventas = gestorVenta.getListaVenta().stream()
                .filter(v -> v != null)
                .filter(v -> v.getFecha() != null)
                .filter(v -> !v.getFecha().isBefore(desde))
                .filter(v -> !v.getFecha().isAfter(hasta))
                .collect(Collectors.toList());
            
            // Limpiar tabla
            modeloTabla.setRowCount(0);
            
            double montoTotal = 0;
            long dias = java.time.temporal.ChronoUnit.DAYS.between(inicio, fin) + 1;
            
            for (Venta v : ventas) {
                String cliente = v.getNombreCliente() == null || v.getNombreCliente().isEmpty() 
                    ? "Sin nombre" 
                    : v.getNombreCliente();
                
                String fechaStr = v.getFecha().format(formatterFecha);
                String totalStr = String.format("%.2f", v.getTotal());
                
                modeloTabla.addRow(new Object[]{
                    v.getId(),
                    fechaStr,
                    cliente,
                    totalStr,
                    v.getMetodoPago().getNombre(),
                    v.getEstado().getNombre()
                });
                
                montoTotal += v.getTotal();
            }
            
            double promedioDiario = montoTotal / dias;
            
            lblTotalVentas.setText("Total Ventas: " + ventas.size());
            lblMontoTotal.setText(String.format("Monto Total: Bs. %.2f", montoTotal));
            lblPromedioDiario.setText(String.format("Promedio Diario: Bs. %.2f", promedioDiario));
            
            if (ventas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay ventas registradas en el período seleccionado.\n\n" +
                    "Total de ventas en el sistema: " + gestorVenta.getListaVenta().size(),
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Formato de fecha inválido.\nUse YYYY-MM-DD",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al generar reporte: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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
        lblTotalVentas = new javax.swing.JLabel();
        lblMontoTotal = new javax.swing.JLabel();
        lblPromedioDiario = new javax.swing.JLabel();
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
                .addGap(63, 63, 63)
                .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(lblFechaFin)
                .addGap(35, 35, 35)
                .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(btnGenerar)
                .addGap(34, 34, 34)
                .addComponent(btnUltimaSemana)
                .addGap(51, 51, 51)
                .addComponent(btnUltimoMes)
                .addContainerGap(69, Short.MAX_VALUE))
        );
        PanelFiltroLayout.setVerticalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaInicio)
                    .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFechaFin)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerar)
                    .addComponent(btnUltimaSemana)
                    .addComponent(btnUltimoMes))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTitulo)
                .addGap(55, 55, 55)
                .addComponent(PanelFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addComponent(PanelFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

        lblTotalVentas.setText("jLabel1");

        lblMontoTotal.setText("jLabel1");

        lblPromedioDiario.setText("jLabel1");

        javax.swing.GroupLayout PanelResumenLayout = new javax.swing.GroupLayout(PanelResumen);
        PanelResumen.setLayout(PanelResumenLayout);
        PanelResumenLayout.setHorizontalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblTotalVentas)
                .addGap(225, 225, 225)
                .addComponent(lblMontoTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPromedioDiario)
                .addGap(195, 195, 195))
        );
        PanelResumenLayout.setVerticalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalVentas)
                    .addComponent(lblMontoTotal)
                    .addComponent(lblPromedioDiario))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addGroup(PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(PanelCentralLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(PanelResumen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(116, 116, 116))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(25, 25, 25))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
    private javax.swing.JLabel lblMontoTotal;
    private javax.swing.JLabel lblPromedioDiario;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalVentas;
    private javax.swing.JTextField txtFechaFin;
    private javax.swing.JTextField txtFechaInicio;
    // End of variables declaration//GEN-END:variables
}
