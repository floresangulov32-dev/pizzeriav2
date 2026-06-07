package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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

import pizzeria.controller.GestorReserva;
import pizzeria.model.Reserva;
import pizzeria.model.EstadoReserva;

public class ReporteReservasDiarioGUI extends JPanel {
    
    private GestorReserva gestorReserva;
    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    //private JTextField txtFecha;
    //private JButton btnGenerar, btnHoy, btnCerrar;
    //private JLabel lblTotalReservas, lblMontoTotal, lblPendientes, lblEntregadas;
    
    private DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter formatterFechaCorta = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public ReporteReservasDiarioGUI() {
        gestorReserva = new GestorReserva();
        gestorReserva.cargarArchivo("resources/data/reservas.txt");
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        // Cargar reporte del día actual por defecto
        LocalDate hoy = LocalDate.now();
        txtFecha.setText(hoy.toString());
        generarReporte(hoy);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("REPORTE DE RESERVAS DIARIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setOpaque(false);
        
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        txtFecha = new JTextField(10);
        txtFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnGenerar.setBackground(new Color(52, 152, 219));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(e -> generarDesdeInput());
        
        btnHoy = new JButton("Ver Hoy");
        btnHoy.setFont(new Font("Segoe UI", Font.BOLD, 11));
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
        
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Reservas del Día",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        ));
        
        String[] columnas = {"ID", "CLIENTE", "TELÉFONO", "FECHA", "ESTADO", "TOTAL (Bs.)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaReservas = new JTable(modeloTabla);
        tablaReservas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tablaReservas.setRowHeight(24);
        tablaReservas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tablaReservas.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaReservas.getTableHeader().setReorderingAllowed(false);
        
        // Anchos de columnas
        tablaReservas.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaReservas.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaReservas.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaReservas.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaReservas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaReservas.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(tablaReservas);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de resumen
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(8, 0, 0, 0));
        
        lblTotalReservas = new JLabel("Total Reservas: 0");
        lblMontoTotal = new JLabel("Monto Total: Bs. 0.00");
        lblPendientes = new JLabel("Pendientes: 0");
        lblEntregadas = new JLabel("Entregadas: 0");
        
        lblTotalReservas.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMontoTotal.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPendientes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEntregadas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        lblTotalReservas.setForeground(new Color(52, 152, 219));
        lblMontoTotal.setForeground(new Color(46, 204, 113));
        lblPendientes.setForeground(new Color(241, 196, 15));
        lblEntregadas.setForeground(new Color(155, 89, 182));
        
        panelResumen.add(lblTotalReservas);
        panelResumen.add(lblMontoTotal);
        panelResumen.add(lblPendientes);
        panelResumen.add(lblEntregadas);
        
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
    
    private void generarDesdeInput() {
        try {
            String fechaStr = txtFecha.getText().trim();
            if (fechaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese una fecha.",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
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
        List<Reserva> reservas = gestorReserva.getListaReservas().stream()
            .filter(r -> r.getFechaReserva().toLocalDate().equals(fecha))
            .collect(Collectors.toList());
        
        modeloTabla.setRowCount(0);
        
        double montoTotal = 0;
        int pendientes = 0;
        int entregadas = 0;
        
        for (Reserva r : reservas) {
            double totalReserva = r.calcularTotal();
            
            modeloTabla.addRow(new Object[]{
                r.getId(),
                r.getNombreCliente(),
                r.getTelefono(),
                r.getFechaReserva().format(formatterFecha),
                getEstadoString(r.getEstado()),
                String.format("%.2f", totalReserva)
            });
            
            montoTotal += totalReserva;
            
            if (r.getEstado() == EstadoReserva.PENDIENTE) {
                pendientes++;
            } else if (r.getEstado() == EstadoReserva.ENTREGADA) {
                entregadas++;
            }
        }
        
        lblTotalReservas.setText("Total Reservas: " + reservas.size());
        lblMontoTotal.setText(String.format("Monto Total: Bs. %.2f", montoTotal));
        lblPendientes.setText("Pendientes: " + pendientes);
        lblEntregadas.setText("Entregadas: " + entregadas);
        
        if (reservas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay reservas registradas en esta fecha.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private String getEstadoString(EstadoReserva estado) {
        switch (estado) {
            case PENDIENTE: return "PENDIENTE";
            case EN_COCINA: return "EN COCINA";
            case LISTA: return "LISTA";
            case ENTREGADA: return "ENTREGADA";
            case CANCELADA: return "CANCELADA";
            default: return estado.name();
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
        lblFecha = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        btnGenerar = new javax.swing.JButton();
        btnHoy = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelResumen = new javax.swing.JPanel();
        lblTotalReservas = new javax.swing.JLabel();
        lblMontoTotal = new javax.swing.JLabel();
        lblPendientes = new javax.swing.JLabel();
        lblEntregadas = new javax.swing.JLabel();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        lblTitulo.setText("jLabel1");

        lblFecha.setText("jLabel1");

        txtFecha.setText("jTextField1");

        btnGenerar.setText("jButton1");

        btnHoy.setText("jButton1");

        javax.swing.GroupLayout PanelFiltroLayout = new javax.swing.GroupLayout(PanelFiltro);
        PanelFiltro.setLayout(PanelFiltroLayout);
        PanelFiltroLayout.setHorizontalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblFecha)
                .addGap(60, 60, 60)
                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98)
                .addComponent(btnGenerar)
                .addGap(148, 148, 148)
                .addComponent(btnHoy)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelFiltroLayout.setVerticalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGroup(PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelFiltroLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFecha)
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PanelFiltroLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGenerar)
                            .addComponent(btnHoy))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(15, 15, 15)
                .addComponent(lblTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addComponent(PanelFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 21, Short.MAX_VALUE))
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

        lblTotalReservas.setText("jLabel1");

        lblMontoTotal.setText("jLabel1");

        lblPendientes.setText("jLabel1");

        lblEntregadas.setText("jLabel1");

        javax.swing.GroupLayout PanelResumenLayout = new javax.swing.GroupLayout(PanelResumen);
        PanelResumen.setLayout(PanelResumenLayout);
        PanelResumenLayout.setHorizontalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTotalReservas)
                .addGap(84, 84, 84)
                .addComponent(lblMontoTotal)
                .addGap(197, 197, 197)
                .addComponent(lblPendientes)
                .addGap(181, 181, 181)
                .addComponent(lblEntregadas)
                .addContainerGap(227, Short.MAX_VALUE))
        );
        PanelResumenLayout.setVerticalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalReservas)
                    .addComponent(lblMontoTotal)
                    .addComponent(lblPendientes)
                    .addComponent(lblEntregadas))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        btnCerrar.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(370, 370, 370)
                .addComponent(btnCerrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(btnCerrar)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 13, Short.MAX_VALUE))
                    .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelFiltro;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelResumen;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton btnHoy;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblEntregadas;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblMontoTotal;
    private javax.swing.JLabel lblPendientes;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalReservas;
    private javax.swing.JTextField txtFecha;
    // End of variables declaration//GEN-END:variables
}
