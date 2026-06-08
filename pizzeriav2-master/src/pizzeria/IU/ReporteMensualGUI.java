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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class ReporteMensualGUI extends JPanel {
    
    private GestorFinanzas gestorFinanzas;
    private JTextArea txtReporte;
//    private JComboBox<String> cmbMes;
//    private JTextField txtAnio;
    private JButton btnMesActual;
//    private JLabel lblTotalIngresos, lblTotalEgresos, lblBalance, lblPromedioDiario;
    
    private int añoActual;
    private int mesActual;
    
    private DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DateTimeFormatter formatterFechaCorta = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Nombres de los meses en español
    private String[] nombresMeses = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };
    
    public ReporteMensualGUI() {
        gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();
        
        initUI();
        
        // Establecer mes y año actual por defecto
        LocalDate hoy = LocalDate.now();
        añoActual = hoy.getYear();
        mesActual = hoy.getMonthValue() - 1; // 0 = Enero
        
        cmbMes.setSelectedIndex(mesActual);
        txtAnio.setText(String.valueOf(añoActual));
        generarReporteMensual();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Titulo
        JLabel lblTitulo = new JLabel("REPORTE MENSUAL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        // Panel de filtro
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setOpaque(false);
        
        JLabel lblMes = new JLabel("Mes:");
        lblMes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        cmbMes = new JComboBox<>(nombresMeses);
        cmbMes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbMes.setPreferredSize(new Dimension(120, 25));
        
        JLabel lblAnio = new JLabel("Año:");
        lblAnio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtAnio = new JTextField(6);
        txtAnio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGenerar.setBackground(new Color(52, 152, 219));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(e -> generarDesdeInput());
        
        btnMesActual = new JButton("Mes Actual");
        btnMesActual.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMesActual.setBackground(new Color(46, 204, 113));
        btnMesActual.setForeground(Color.WHITE);
        btnMesActual.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnMesActual.setFocusPainted(false);
        btnMesActual.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMesActual.addActionListener(e -> cargarMesActual());
        
        btnMesAnterior = new JButton("◄ Mes Anterior");
        btnMesAnterior.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMesAnterior.setBackground(new Color(46, 204, 113));
        btnMesAnterior.setForeground(Color.WHITE);
        btnMesAnterior.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnMesAnterior.setFocusPainted(false);
        btnMesAnterior.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMesAnterior.addActionListener(e -> mesAnterior());
        
        btnMesSiguiente = new JButton("Mes Siguiente ►");
        btnMesSiguiente.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMesSiguiente.setBackground(new Color(46, 204, 113));
        btnMesSiguiente.setForeground(Color.WHITE);
        btnMesSiguiente.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnMesSiguiente.setFocusPainted(false);
        btnMesSiguiente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMesSiguiente.addActionListener(e -> mesSiguiente());
        
        panelFiltro.add(lblMes);
        panelFiltro.add(cmbMes);
        panelFiltro.add(lblAnio);
        panelFiltro.add(txtAnio);
        panelFiltro.add(btnGenerar);
        panelFiltro.add(btnMesActual);
        panelFiltro.add(btnMesAnterior);
        panelFiltro.add(btnMesSiguiente);
        
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
        
        // Borde con título del mes (se actualizará dinámicamente)
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Mes",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        );
        panelReporte.setBorder(border);
        
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
    
    private void cargarMesActual() {
        LocalDate hoy = LocalDate.now();
        añoActual = hoy.getYear();
        mesActual = hoy.getMonthValue() - 1;
        
        cmbMes.setSelectedIndex(mesActual);
        txtAnio.setText(String.valueOf(añoActual));
        generarReporteMensual();
    }
    
    private void mesAnterior() {
        mesActual--;
        if (mesActual < 0) {
            mesActual = 11;
            añoActual--;
        }
        cmbMes.setSelectedIndex(mesActual);
        txtAnio.setText(String.valueOf(añoActual));
        generarReporteMensual();
    }
    
    private void mesSiguiente() {
        mesActual++;
        if (mesActual > 11) {
            mesActual = 0;
            añoActual++;
        }
        cmbMes.setSelectedIndex(mesActual);
        txtAnio.setText(String.valueOf(añoActual));
        generarReporteMensual();
    }
    
    private void generarDesdeInput() {
        try {
            int mes = cmbMes.getSelectedIndex();
            int año = Integer.parseInt(txtAnio.getText().trim());
            
            if (año < 2000 || año > 2100) {
                JOptionPane.showMessageDialog(this,
                    "Año inválido. Use un año entre 2000 y 2100.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            mesActual = mes;
            añoActual = año;
            generarReporteMensual();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Año inválido. Ingrese un número de 4 dígitos.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generarReporteMensual() {
        // Obtener primer y último día del mes
        LocalDate inicioMes = LocalDate.of(añoActual, mesActual + 1, 1);
        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());
        
        String nombreMes = nombresMeses[mesActual];
        
        // Actualizar el borde del panel central
        JPanel panelCentral = (JPanel) getComponent(1);
        JPanel panelReporte = (JPanel) panelCentral.getComponent(0);
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            String.format("%s %d - Del %s al %s", 
                nombreMes, añoActual,
                inicioMes.format(formatterFechaCorta), 
                finMes.format(formatterFechaCorta)),
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        );
        panelReporte.setBorder(border);
        
        LocalDateTime desde = inicioMes.atStartOfDay();
        LocalDateTime hasta = finMes.plusDays(1).atStartOfDay().minusNanos(1);
        
        double totalIngresos = gestorFinanzas.sumaIngresos(desde, hasta);
        double totalEgresos = gestorFinanzas.sumaEgresos(desde, hasta);
        double balance = totalIngresos - totalEgresos;
        int diasMes = finMes.getDayOfMonth();
        double promedioDiario = balance / diasMes;
        
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
        
        // Construir el reporte
        StringBuilder reporte = new StringBuilder();
        reporte.append("=".repeat(80)).append("\n");
        reporte.append(String.format("REPORTE MENSUAL - %s %d\n", nombreMes, añoActual));
        reporte.append(String.format("Período: del %s al %s\n", 
            inicioMes.format(formatterFechaCorta), finMes.format(formatterFechaCorta)));
        reporte.append("=".repeat(80)).append("\n\n");
        
        reporte.append("RESUMEN DEL MES\n");
        reporte.append("-".repeat(80)).append("\n");
        reporte.append(String.format("Ingresos totales:     Bs. %12.2f\n", totalIngresos));
        reporte.append(String.format("Egresos totales:      Bs. %12.2f\n", totalEgresos));
        reporte.append(String.format("Balance del mes:      Bs. %12.2f\n", balance));
        reporte.append(String.format("Promedio diario:      Bs. %12.2f\n", promedioDiario));
        reporte.append("\n");
        
        reporte.append("DETALLE POR DÍA\n");
        reporte.append("-".repeat(80)).append("\n");
        
        LocalDate fechaActual = inicioMes;
        while (!fechaActual.isAfter(finMes)) {
            final LocalDate dia = fechaActual;
            double ingresosDia = movimientos.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
                .filter(m -> m.getFecha().toLocalDate().equals(dia))
                .mapToDouble(MovimientoCaja::getMonto)
                .sum();
            
            double egresosDia = movimientos.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.EGRESO)
                .filter(m -> m.getFecha().toLocalDate().equals(dia))
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
            reporte.append("No hay movimientos registrados en este mes.\n");
        } else {
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

        PanelSuperior = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        PanelFiltro = new javax.swing.JPanel();
        lblMes = new javax.swing.JLabel();
        cmbMes = new javax.swing.JComboBox<>();
        lblAnio = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        btnGenerar = new javax.swing.JButton();
        btnMesActua = new javax.swing.JButton();
        btnMesAnterior = new javax.swing.JButton();
        btnMesSiguiente = new javax.swing.JButton();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelResumen = new javax.swing.JPanel();
        lblTotalIngresos = new javax.swing.JLabel();
        lblTotalEgresos = new javax.swing.JLabel();
        lblBalance = new javax.swing.JLabel();
        lblPromedioDiario = new javax.swing.JLabel();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        lblTitulo.setText("jLabel1");

        lblMes.setText("jLabel1");

        cmbMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblAnio.setText("jLabel1");

        txtAnio.setText("jTextField1");

        btnGenerar.setText("jButton1");

        btnMesActua.setText("jButton1");

        btnMesAnterior.setText("jButton1");

        btnMesSiguiente.setText("jButton1");

        javax.swing.GroupLayout PanelFiltroLayout = new javax.swing.GroupLayout(PanelFiltro);
        PanelFiltro.setLayout(PanelFiltroLayout);
        PanelFiltroLayout.setHorizontalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblMes)
                .addGap(45, 45, 45)
                .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(lblAnio)
                .addGap(44, 44, 44)
                .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btnGenerar)
                .addGap(29, 29, 29)
                .addComponent(btnMesActua)
                .addGap(30, 30, 30)
                .addComponent(btnMesAnterior)
                .addGap(18, 18, 18)
                .addComponent(btnMesSiguiente)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelFiltroLayout.setVerticalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMes)
                    .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAnio)
                    .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerar)
                    .addComponent(btnMesActua)
                    .addComponent(btnMesAnterior)
                    .addComponent(btnMesSiguiente))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblTitulo)
                .addGap(63, 63, 63)
                .addComponent(PanelFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
                .addGap(28, 28, 28)
                .addComponent(lblTotalIngresos)
                .addGap(106, 106, 106)
                .addComponent(lblTotalEgresos)
                .addGap(98, 98, 98)
                .addComponent(lblBalance)
                .addGap(111, 111, 111)
                .addComponent(lblPromedioDiario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelResumenLayout.setVerticalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalIngresos)
                    .addComponent(lblTotalEgresos)
                    .addComponent(lblBalance)
                    .addComponent(lblPromedioDiario))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        btnCerrar.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(417, 417, 417)
                .addComponent(btnCerrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(btnCerrar)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(PanelResumen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelCentral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private javax.swing.JButton btnMesActua;
    private javax.swing.JButton btnMesAnterior;
    private javax.swing.JButton btnMesSiguiente;
    private javax.swing.JComboBox<String> cmbMes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblMes;
    private javax.swing.JLabel lblPromedioDiario;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalEgresos;
    private javax.swing.JLabel lblTotalIngresos;
    private javax.swing.JTextField txtAnio;
    // End of variables declaration//GEN-END:variables
}
