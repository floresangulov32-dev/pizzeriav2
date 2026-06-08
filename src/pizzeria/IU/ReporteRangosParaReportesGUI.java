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
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
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

import pizzeria.controller.GestorFinanzas;
import pizzeria.model.MovimientoCaja;
import pizzeria.model.TipoMovimiento;

public class ReporteRangosParaReportesGUI extends JPanel {
    
    private GestorFinanzas gestorFinanzas;
    private JTextArea txtReporte;
    //private JTextField txtFechaInicio, txtFechaFin;
    //private JLabel lblTotalIngresos, lblTotalEgresos, lblBalance, lblPromedioDiario;
    private JButton btnGenerar;
    
    private DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter formatterFechaCorta = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public ReporteRangosParaReportesGUI() {
        gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        // Establecer semana actual por defecto
        cargarSemanaActual();
    }
    
    private void cargarSemanaActual() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
        LocalDate finSemana = inicioSemana.plusDays(6);
        
        txtFechaInicio.setText(inicioSemana.toString());
        txtFechaFin.setText(finSemana.toString());
        generarReporte(inicioSemana, finSemana);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Titulo
        JLabel lblTitulo = new JLabel("REPORTE ENTRE RANGOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        // Panel de filtro
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setOpaque(false);
        
        JLabel lblFechaInicio = new JLabel("Fecha Inicio (YYYY-MM-DD):");
        lblFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtFechaInicio = new JTextField(10);
        txtFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel lblFechaFin = new JLabel("Fecha Fin (YYYY-MM-DD):");
        lblFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtFechaFin = new JTextField(10);
        txtFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGenerar.setBackground(new Color(52, 152, 219));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(e -> generarReporteDesdeInput());
        
        btnSemanaActual = new JButton("Fecha Actual");
        btnSemanaActual.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSemanaActual.setBackground(new Color(46, 204, 113));
        btnSemanaActual.setForeground(Color.WHITE);
        btnSemanaActual.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnSemanaActual.setFocusPainted(false);
        btnSemanaActual.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSemanaActual.addActionListener(e -> cargarSemanaActual());
        
        panelFiltro.add(lblFechaInicio);
        panelFiltro.add(txtFechaInicio);
        panelFiltro.add(lblFechaFin);
        panelFiltro.add(txtFechaFin);
        panelFiltro.add(btnGenerar);
        panelFiltro.add(btnSemanaActual);
        
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
            "Reporte Entre Rangos",
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
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        lblTotalIngresos = new JLabel("Total Ingresos: Bs. 0.00");
        lblTotalEgresos = new JLabel("Total Egresos: Bs. 0.00");
        lblBalance = new JLabel("Balance: Bs. 0.00");
        lblPromedioDiario = new JLabel("Promedio Diario: Bs. 0.00");
        
        lblTotalIngresos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalEgresos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPromedioDiario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        lblTotalIngresos.setForeground(new Color(46, 204, 113));
        lblTotalEgresos.setForeground(new Color(231, 76, 60));
        lblBalance.setForeground(new Color(52, 152, 219));
        lblPromedioDiario.setForeground(new Color(155, 89, 182));
        
        panelResumen.add(lblTotalIngresos);
        panelResumen.add(lblTotalEgresos);
        panelResumen.add(lblBalance);
        panelResumen.add(lblPromedioDiario);
        
        panel.add(panelReporte, BorderLayout.CENTER);
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
    
    private void volverAReportes() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReportesGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void generarReporteDesdeInput() {
        try {
            String inicioStr = txtFechaInicio.getText().trim();
            String finStr = txtFechaFin.getText().trim();
            LocalDate inicio = LocalDate.parse(inicioStr, formatterInput);
            LocalDate fin = LocalDate.parse(finStr, formatterInput);
            
            if (fin.isBefore(inicio)) {
                JOptionPane.showMessageDialog(this,
                    "La fecha fin no puede ser anterior a la fecha inicio.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            generarReporte(inicio, fin);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Formato de fecha inválido. Use YYYY-MM-DD",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generarReporte(LocalDate inicio, LocalDate fin) {
        LocalDateTime desde = inicio.atStartOfDay();
        LocalDateTime hasta = fin.plusDays(1).atStartOfDay().minusNanos(1);
        
        double totalIngresos = gestorFinanzas.sumaIngresos(desde, hasta);
        double totalEgresos = gestorFinanzas.sumaEgresos(desde, hasta);
        double balance = totalIngresos - totalEgresos;
        double promedioDiario = balance / 7;
        
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
        
        lblPromedioDiario.setText(String.format("Promedio Diario: Bs. %.2f", promedioDiario));
        
        // Obtener movimientos del período
        List<MovimientoCaja> movimientos = gestorFinanzas.getMovimientosPeriodo(desde, hasta);
        
        // Agrupar movimientos por día
        Map<LocalDate, List<MovimientoCaja>> movimientosPorDia = movimientos.stream()
            .collect(Collectors.groupingBy(m -> m.getFecha().toLocalDate()));
        
        // Construir el reporte
        StringBuilder reporte = new StringBuilder();
        reporte.append("=".repeat(80)).append("\n");
        reporte.append(String.format("REPORTE ENTRE RANGOS - Del %s al %s\n", 
            inicio.format(formatterFechaCorta), fin.format(formatterFechaCorta)));
        reporte.append("=".repeat(80)).append("\n\n");
        
        reporte.append("RESUMEN DEL PERÍODO\n");
        reporte.append("-".repeat(80)).append("\n");
        reporte.append(String.format("Ingresos totales:    Bs. %12.2f\n", totalIngresos));
        reporte.append(String.format("Egresos totales:     Bs. %12.2f\n", totalEgresos));
        reporte.append(String.format("Balance del período: Bs. %12.2f\n", balance));
        reporte.append(String.format("Promedio diario:     Bs. %12.2f\n", promedioDiario));
        reporte.append("\n");
        
        reporte.append("DETALLE POR DÍA\n");
        reporte.append("-".repeat(80)).append("\n");
        
        LocalDate fechaActual = inicio;
        while (!fechaActual.isAfter(fin)) {
            List<MovimientoCaja> movsDia = movimientosPorDia.getOrDefault(fechaActual, new ArrayList<>());
            
            double ingresosDia = movsDia.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
                .mapToDouble(MovimientoCaja::getMonto)
                .sum();
            
            double egresosDia = movsDia.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.EGRESO)
                .mapToDouble(MovimientoCaja::getMonto)
                .sum();
            
            double balanceDia = ingresosDia - egresosDia;
            
            String balanceStr = balanceDia >= 0 ? 
                String.format("Bs. %10.2f", balanceDia) : 
                String.format("Bs. %10.2f (NEGATIVO)", balanceDia);
            
            reporte.append(String.format("%s:\n", fechaActual.format(formatterFechaCorta)));
            reporte.append(String.format("  Ingresos: Bs. %10.2f | Egresos: Bs. %10.2f | Balance: %s\n", 
                ingresosDia, egresosDia, balanceStr));
            reporte.append("\n");
            
            fechaActual = fechaActual.plusDays(1);
        }
        
        reporte.append("DETALLE DE MOVIMIENTOS\n");
        reporte.append("-".repeat(80)).append("\n");
        reporte.append(String.format("%-20s %-12s %-10s %-35s\n", "FECHA", "TIPO", "MONTO", "DESCRIPCION"));
        reporte.append("-".repeat(80)).append("\n");
        
        if (movimientos.isEmpty()) {
            reporte.append("No hay movimientos registrados en este período.\n");
        } else {
            // Ordenar por fecha
            movimientos.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));
            
            for (MovimientoCaja m : movimientos) {
                String tipo = m.getTipo() == TipoMovimiento.INGRESO ? "INGRESO" : "EGRESO";
                String montoStr = String.format("%.2f", m.getMonto());
                String fechaStr = m.getFecha().format(formatterFecha);
                reporte.append(String.format("%-20s %-12s Bs.%8s %-35s\n", 
                    fechaStr, tipo, montoStr, m.getDescripcion()));
            }
        }
        
        reporte.append("\n").append("=".repeat(80)).append("\n");
        reporte.append("Fin del reporte\n");
        
        txtReporte.setText(reporte.toString());
        txtReporte.setCaretPosition(0);
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
        lblTitulo = new javax.swing.JLabel();
        PanelFiltro = new javax.swing.JPanel();
        lblFechaInicio = new javax.swing.JLabel();
        txtFechaInicio = new javax.swing.JTextField();
        lblFechaFin = new javax.swing.JLabel();
        txtFechaFin = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnSemanaActual = new javax.swing.JButton();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelResumen = new javax.swing.JPanel();
        lblTotalIngresos = new javax.swing.JLabel();
        lblTotalEgresos = new javax.swing.JLabel();
        lblBalance = new javax.swing.JLabel();
        lblPromedioDiario = new javax.swing.JLabel();

        lblTitulo.setText("jLabel1");

        lblFechaInicio.setText("jLabel1");

        txtFechaInicio.setText("jTextField1");

        lblFechaFin.setText("jLabel1");

        txtFechaFin.setText("jTextField1");

        jButton1.setText("jButton1");

        btnSemanaActual.setText("jButton2");

        javax.swing.GroupLayout PanelFiltroLayout = new javax.swing.GroupLayout(PanelFiltro);
        PanelFiltro.setLayout(PanelFiltroLayout);
        PanelFiltroLayout.setHorizontalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(lblFechaInicio)
                .addGap(62, 62, 62)
                .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130)
                .addComponent(lblFechaFin)
                .addGap(98, 98, 98)
                .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(btnSemanaActual)
                .addGap(17, 17, 17))
        );
        PanelFiltroLayout.setVerticalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaInicio)
                    .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFechaFin)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(btnSemanaActual))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelSuperior2Layout = new javax.swing.GroupLayout(PanelSuperior2);
        PanelSuperior2.setLayout(PanelSuperior2Layout);
        PanelSuperior2Layout.setHorizontalGroup(
            PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperior2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PanelFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        PanelSuperior2Layout.setVerticalGroup(
            PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperior2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperior2Layout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(PanelFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        btnCerrar.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(119, 119, 119))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(18, 18, 18))
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

        lblPromedioDiario.setText("jLabel1");

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
                .addGap(139, 139, 139)
                .addComponent(lblPromedioDiario)
                .addContainerGap(174, Short.MAX_VALUE))
        );
        PanelResumenLayout.setVerticalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalIngresos)
                    .addComponent(lblTotalEgresos)
                    .addComponent(lblBalance)
                    .addComponent(lblPromedioDiario))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelFiltro;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelResumen;
    private javax.swing.JPanel PanelSuperior2;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnSemanaActual;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblFechaFin;
    private javax.swing.JLabel lblFechaInicio;
    private javax.swing.JLabel lblPromedioDiario;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalEgresos;
    private javax.swing.JLabel lblTotalIngresos;
    private javax.swing.JTextField txtFechaFin;
    private javax.swing.JTextField txtFechaInicio;
    // End of variables declaration//GEN-END:variables
}
