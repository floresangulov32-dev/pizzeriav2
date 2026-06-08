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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import pizzeria.model.DetalleVenta;

public class ReporteTopProductosGUI extends JPanel {
    
    private GestorVenta gestorVenta;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    //private JTextField txtFechaInicio, txtFechaFin;
    //private JComboBox<String> cmbCantidad;
    //private JButton btnGenerar, btnUltimaSemana, btnUltimoMes, btnCerrar;
    //private JLabel lblTotalProductos, lblProductoMasVendido;
    
    private DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public ReporteTopProductosGUI() {
        gestorVenta = ContextoVentasGUI.getInstancia().getGestorVenta();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        LocalDate hoy = LocalDate.now();
        txtFechaInicio.setText(hoy.minusDays(6).toString());
        txtFechaFin.setText(hoy.toString());
        cmbCantidad.setSelectedIndex(0);
        
        generarReporte();
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        JLabel lblTitulo = new JLabel("TOP PRODUCTOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panelFiltro.setOpaque(false);
        
        JLabel lblFechaInicio = new JLabel("Inicio:");
        lblFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        txtFechaInicio = new JTextField(8);
        txtFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        JLabel lblFechaFin = new JLabel("Fin:");
        lblFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        txtFechaFin = new JTextField(8);
        txtFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        JLabel lblCantidad = new JLabel("Mostrar:");
        lblCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        String[] cantidades = {"5", "10", "15", "20"};
        cmbCantidad = new JComboBox<>(cantidades);
        cmbCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        cmbCantidad.setPreferredSize(new Dimension(50, 22));
        
        btnGenerar = new JButton("Gen");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnGenerar.setBackground(new Color(52, 152, 219));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.addActionListener(e -> generarReporte());
        
        btnUltimaSemana = new JButton("7d");
        btnUltimaSemana.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnUltimaSemana.setBackground(new Color(46, 204, 113));
        btnUltimaSemana.setForeground(Color.WHITE);
        btnUltimaSemana.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        btnUltimaSemana.setFocusPainted(false);
        btnUltimaSemana.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUltimaSemana.addActionListener(e -> {
            LocalDate hoy = LocalDate.now();
            txtFechaInicio.setText(hoy.minusDays(6).toString());
            txtFechaFin.setText(hoy.toString());
            generarReporte();
        });
        
        btnUltimoMes = new JButton("30d");
        btnUltimoMes.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnUltimoMes.setBackground(new Color(46, 204, 113));
        btnUltimoMes.setForeground(Color.WHITE);
        btnUltimoMes.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
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
        panelFiltro.add(lblCantidad);
        panelFiltro.add(cmbCantidad);
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
            "Ranking",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 11),
            new Color(80, 80, 80)
        ));
        
        String[] columnas = {"#", "ID", "PRODUCTO", "CANT", "TOTAL"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        tablaProductos.setRowHeight(20);
        tablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 10));
        tablaProductos.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(30);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(35);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(180);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(50);
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(60);
        
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 3));
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(3, 0, 0, 0));
        
        lblTotalProductos = new JLabel("Total: 0");
        lblProductoMasVendido = new JLabel("Top: -");
        
        lblTotalProductos.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblProductoMasVendido.setFont(new Font("Segoe UI", Font.BOLD, 10));
        
        lblTotalProductos.setForeground(new Color(52, 152, 219));
        lblProductoMasVendido.setForeground(new Color(46, 204, 113));
        
        panelResumen.add(lblTotalProductos);
        panelResumen.add(lblProductoMasVendido);
        
        panel.add(panelTabla, BorderLayout.CENTER);
        panel.add(panelResumen, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        btnCerrar = new JButton("Volver");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnCerrar.setBackground(new Color(168, 27, 29));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
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
                JOptionPane.showMessageDialog(this, "Ingrese ambas fechas.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate inicio = LocalDate.parse(inicioStr, formatterInput);
            LocalDate fin = LocalDate.parse(finStr, formatterInput);
            
            if (fin.isBefore(inicio)) {
                JOptionPane.showMessageDialog(this, "Fecha fin no puede ser anterior.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            LocalDateTime desde = inicio.atStartOfDay();
            LocalDateTime hasta = fin.plusDays(1).atStartOfDay().minusNanos(1);
            
            List<Venta> ventas = gestorVenta.getListaVenta().stream()
                .filter(v -> v != null && v.getFecha() != null)
                .filter(v -> !v.getFecha().isBefore(desde))
                .filter(v -> !v.getFecha().isAfter(hasta))
                .collect(Collectors.toList());
            
            Map<String, ProductoVendido> contadorProductos = new HashMap<>();
            
            for (Venta v : ventas) {
                for (DetalleVenta detalle : v.getItems()) {
                    int id = detalle.getProducto().getID();
                    String nombre = detalle.getProducto().getNombre();
                    int cantidad = detalle.getCantidad();
                    double subtotal = detalle.getSubTotal();
                    
                    ProductoVendido pv = contadorProductos.getOrDefault(nombre, 
                        new ProductoVendido(id, nombre, 0, 0.0));
                    pv.cantidad += cantidad;
                    pv.total += subtotal;
                    contadorProductos.put(nombre, pv);
                }
                
                for (var combo : v.getCombos()) {
                    String nombre = combo.getDescripcion();
                    int cantidad = combo.getCantidad();
                    double subtotal = combo.getSubTotal();
                    
                    ProductoVendido pv = contadorProductos.getOrDefault(nombre,
                        new ProductoVendido(0, nombre, 0, 0.0));
                    pv.cantidad += cantidad;
                    pv.total += subtotal;
                    contadorProductos.put(nombre, pv);
                }
            }
            
            List<ProductoVendido> topProductos = new ArrayList<>(contadorProductos.values());
            topProductos.sort((a, b) -> Integer.compare(b.cantidad, a.cantidad));
            
            int limite = Integer.parseInt((String) cmbCantidad.getSelectedItem());
            if (topProductos.size() > limite) {
                topProductos = topProductos.subList(0, limite);
            }
            
            modeloTabla.setRowCount(0);
            
            int totalVendidos = 0;
            String productoMasVendido = "-";
            int maxCantidad = 0;
            
            for (int i = 0; i < topProductos.size(); i++) {
                ProductoVendido pv = topProductos.get(i);
                modeloTabla.addRow(new Object[]{
                    i + 1,
                    pv.id,
                    pv.nombre,
                    pv.cantidad,
                    String.format("%.0f", pv.total)
                });
                
                totalVendidos += pv.cantidad;
                if (pv.cantidad > maxCantidad) {
                    maxCantidad = pv.cantidad;
                    productoMasVendido = pv.nombre;
                }
            }
            
            lblTotalProductos.setText("Total: " + totalVendidos);
            lblProductoMasVendido.setText("Top: " + productoMasVendido);
            
            if (topProductos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay ventas en este período.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato fecha inválido. Use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private static class ProductoVendido {
        int id;
        String nombre;
        int cantidad;
        double total;
        
        ProductoVendido(int id, String nombre, int cantidad, double total) {
            this.id = id;
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.total = total;
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
        lblCantidad = new javax.swing.JLabel();
        cmbCantidad = new javax.swing.JComboBox<>();
        btnGenerar = new javax.swing.JButton();
        btnUltimaSemana = new javax.swing.JButton();
        btnUltimoMes = new javax.swing.JButton();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelResumen = new javax.swing.JPanel();
        lblTotalProductos = new javax.swing.JLabel();
        lblProductoMasVendido = new javax.swing.JLabel();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        lblTitulo.setText("jLabel1");

        lblFechaInicio.setText("jLabel1");

        txtFechaInicio.setText("jTextField1");

        lblFechaFin.setText("jLabel1");

        txtFechaFin.setText("jTextField1");

        lblCantidad.setText("jLabel1");

        cmbCantidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnGenerar.setText("jButton1");

        btnUltimaSemana.setText("jButton1");

        btnUltimoMes.setText("jButton1");

        javax.swing.GroupLayout PanelFiltroLayout = new javax.swing.GroupLayout(PanelFiltro);
        PanelFiltro.setLayout(PanelFiltroLayout);
        PanelFiltroLayout.setHorizontalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblFechaInicio)
                .addGap(18, 18, 18)
                .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(lblFechaFin)
                .addGap(18, 18, 18)
                .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(lblCantidad)
                .addGap(34, 34, 34)
                .addComponent(cmbCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnGenerar)
                .addGap(31, 31, 31)
                .addComponent(btnUltimaSemana)
                .addGap(30, 30, 30)
                .addComponent(btnUltimoMes)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        PanelFiltroLayout.setVerticalGroup(
            PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFiltroLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(PanelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaInicio)
                    .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFechaFin)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCantidad)
                    .addComponent(cmbCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerar)
                    .addComponent(btnUltimaSemana)
                    .addComponent(btnUltimoMes))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitulo))
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

        lblTotalProductos.setText("jLabel1");

        lblProductoMasVendido.setText("jLabel1");

        javax.swing.GroupLayout PanelResumenLayout = new javax.swing.GroupLayout(PanelResumen);
        PanelResumen.setLayout(PanelResumenLayout);
        PanelResumenLayout.setHorizontalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTotalProductos)
                .addGap(91, 91, 91)
                .addComponent(lblProductoMasVendido)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelResumenLayout.setVerticalGroup(
            PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelResumenLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(PanelResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalProductos)
                    .addComponent(lblProductoMasVendido))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(PanelResumen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 849, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelResumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        btnCerrar.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(46, 46, 46))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btnCerrar)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
    private javax.swing.JComboBox<String> cmbCantidad;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblFechaFin;
    private javax.swing.JLabel lblFechaInicio;
    private javax.swing.JLabel lblProductoMasVendido;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalProductos;
    private javax.swing.JTextField txtFechaFin;
    private javax.swing.JTextField txtFechaInicio;
    // End of variables declaration//GEN-END:variables
}
