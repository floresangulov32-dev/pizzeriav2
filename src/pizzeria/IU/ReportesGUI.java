package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import pizzeria.controller.GestorFinanzas;
import pizzeria.controller.GestorUsuarios;

public class ReportesGUI extends JPanel {
    
    private GestorFinanzas gestorFinanzas;
    private GestorUsuarios gestorUsuarios;
    
    public ReportesGUI() {
        gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();

        gestorUsuarios = new GestorUsuarios();
        gestorUsuarios.cargarDesdeArchivo();

        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);

        // Ocultar el botón después de que initComponents lo haya creado
        if (btnListaUsuarios != null) {
            btnListaUsuarios.setVisible(false);
        }
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("REPORTES DEL SISTEMA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        JLabel lblDescripcion = new JLabel("Seleccione el tipo de reporte que desea generar");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDescripcion.setForeground(new Color(100, 100, 100));
        
        JPanel panelDesc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDesc.setOpaque(false);
        panelDesc.add(lblDescripcion);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelDesc, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 8, 8));
        panel.setOpaque(false);
        
        panel.add(crearPanelVentas());
        panel.add(crearPanelReservas());
        panel.add(crearPanelGenerales());
        panel.add(crearPanelFinanzas());
        panel.add(crearPanelInventario());
        panel.add(crearPanelUsuarios());
        
        return panel;
    }
    
    private JPanel crearPanelVentas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "VENTAS",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(52, 152, 219)
        ));
        
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 5, 5));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        JButton btnVentasDiarias = crearBoton("Ventas Diarias", new Color(52, 152, 219));
        btnVentasDiarias.addActionListener(e -> reporteVentasDiarias());
        panelBotones.add(btnVentasDiarias);
        
        JButton btnVentasPeriodo = crearBoton("Ventas por Período", new Color(52, 152, 219));
        btnVentasPeriodo.addActionListener(e -> reporteVentasPeriodo());
        panelBotones.add(btnVentasPeriodo);
        
        JButton btnTopProductos = crearBoton("Top Productos", new Color(52, 152, 219));
        btnTopProductos.addActionListener(e -> reporteTopProductos());
        panelBotones.add(btnTopProductos);
        
        panel.add(panelBotones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelReservas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "RESERVAS",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(241, 196, 15)
        ));
        
        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 5, 5));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        JButton btnReservasDiario = crearBoton("Reservas Diario", new Color(241, 196, 15));
        btnReservasDiario.addActionListener(e -> reporteReservasDiario());
        panelBotones.add(btnReservasDiario);
        
        JButton btnReservasPeriodo = crearBoton("Reservas por Período", new Color(241, 196, 15));
        btnReservasPeriodo.addActionListener(e -> reporteReservasPeriodo());
        panelBotones.add(btnReservasPeriodo);
        
        panel.add(panelBotones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelGenerales() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "GENERALES",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(46, 204, 113)
        ));
        
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 5, 5));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        JButton btnReporteSemanal = crearBoton("Reporte Semanal", new Color(46, 204, 113));
        btnReporteSemanal.addActionListener(e -> reporteGeneralSemanal());
        panelBotones.add(btnReporteSemanal);
        
        JButton btnReporteMensual = crearBoton("Reporte Mensual", new Color(46, 204, 113));
        btnReporteMensual.addActionListener(e -> reporteGeneralMensual());
        panelBotones.add(btnReporteMensual);
        
        JButton btnReportePeriodo = crearBoton("Reporte por Período", new Color(46, 204, 113));
        btnReportePeriodo.addActionListener(e -> reporteGeneralPeriodo());
        panelBotones.add(btnReportePeriodo);
        
        panel.add(panelBotones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelFinanzas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "FINANZAS",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(155, 89, 182)
        ));
        
        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 5, 5));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        JButton btnReporteFinanzas = crearBoton("Reporte Financiero", new Color(155, 89, 182));
        btnReporteFinanzas.addActionListener(e -> reporteFinanciero());
        panelBotones.add(btnReporteFinanzas);
        
        JButton btnHistorialMovimientos = crearBoton("Historial Movimientos", new Color(155, 89, 182));
        btnHistorialMovimientos.addActionListener(e -> historialMovimientos());
        panelBotones.add(btnHistorialMovimientos);
        
        panel.add(panelBotones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelInventario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "INVENTARIO",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(230, 126, 34)
        ));
        
        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 5, 5));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        JButton btnStockActual = crearBoton("Stock Actual", new Color(230, 126, 34));
        btnStockActual.addActionListener(e -> reporteStockActual());
        panelBotones.add(btnStockActual);
        
        JButton btnStockBajo = crearBoton("Stock Bajo", new Color(230, 126, 34));
        btnStockBajo.addActionListener(e -> reporteProductosBajoStock());
        panelBotones.add(btnStockBajo);
        
        panel.add(panelBotones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "USUARIOS",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(52, 73, 94)
        ));

        // Cambiar GridLayout de 2 filas a 1 fila
        JPanel panelBotones = new JPanel(new GridLayout(1, 1, 5, 5));  // Antes era (2, 1)
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Comentar o eliminar btnListaUsuarios
        // JButton btnListaUsuarios = crearBoton("Lista de Usuarios", new Color(52, 73, 94));
        // btnListaUsuarios.addActionListener(e -> reporteListaUsuarios());
        // panelBotones.add(btnListaUsuarios);

        // Solo mantener btnUsuariosPorRol
        JButton btnUsuariosPorRol = crearBoton("Usuarios por Rol", new Color(52, 73, 94));
        btnUsuariosPorRol.addActionListener(e -> reporteUsuariosPorRol());
        panelBotones.add(btnUsuariosPorRol);

        panel.add(panelBotones, BorderLayout.CENTER);

        return panel;
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(160, 35));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(boton.getBackground().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    // MÉTODOS DE REPORTES 
    
    private void reporteVentasDiarias() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteVentasDiariasGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteVentasPeriodo() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteVentasPeriodoGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteTopProductos() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteTopProductosGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteReservasDiario() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteReservasDiarioGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteReservasPeriodo() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteReservasPeriodoGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteGeneralSemanal() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteSemanalParaReportesGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteGeneralMensual() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteMensualParaReportesGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteGeneralPeriodo() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteRangosParaReportesGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteFinanciero() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteFinancieroGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void historialMovimientos() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteHistorialMovimientosGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteStockActual() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteStockActualGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteProductosBajoStock() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteStockBajoGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    //private void reporteListaUsuarios() {
    //    JPanel parent = (JPanel) getParent();
    //    if (parent != null) {
    //        parent.removeAll();
    //        parent.add(new ReporteListaUsuariosGUI(), BorderLayout.CENTER);
    //        parent.revalidate();
    //        parent.repaint();
    //    }
    //}
    
    private void reporteUsuariosPorRol() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteUsuariosPorRolGUI(), BorderLayout.CENTER);
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
        lblDescripcion = new javax.swing.JLabel();
        PanelCentral = new javax.swing.JPanel();
        PanelVentas = new javax.swing.JPanel();
        btnVentasDiarias = new javax.swing.JButton();
        btnVentasPeriodo = new javax.swing.JButton();
        btnTopProductos = new javax.swing.JButton();
        PanelReservas = new javax.swing.JPanel();
        btnReservasDiario = new javax.swing.JButton();
        btnReservasPeriodo = new javax.swing.JButton();
        PanelGenerales = new javax.swing.JPanel();
        btnReporteSemanal = new javax.swing.JButton();
        btnReporteMensual = new javax.swing.JButton();
        btnReportePeriodo = new javax.swing.JButton();
        PanelFinanzas = new javax.swing.JPanel();
        btnReporteFinanzas = new javax.swing.JButton();
        btnHistorialMovimientos = new javax.swing.JButton();
        PanelInventario = new javax.swing.JPanel();
        btnStockActual = new javax.swing.JButton();
        btnProductosBajoStock = new javax.swing.JButton();
        btnMovimientosInventario = new javax.swing.JButton();
        PanelUsuarios = new javax.swing.JPanel();
        btnUsuariosPorRol = new javax.swing.JButton();
        btnListaUsuarios = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        lblTitulo.setText("jLabel1");

        lblDescripcion.setText("jLabel1");

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblTitulo)
                .addGap(396, 396, 396)
                .addComponent(lblDescripcion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitulo)
                    .addComponent(lblDescripcion))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnVentasDiarias.setText("jButton1");

        btnVentasPeriodo.setText("jButton1");

        btnTopProductos.setText("jButton1");

        javax.swing.GroupLayout PanelVentasLayout = new javax.swing.GroupLayout(PanelVentas);
        PanelVentas.setLayout(PanelVentasLayout);
        PanelVentasLayout.setHorizontalGroup(
            PanelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelVentasLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(PanelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTopProductos)
                    .addComponent(btnVentasPeriodo)
                    .addComponent(btnVentasDiarias))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        PanelVentasLayout.setVerticalGroup(
            PanelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelVentasLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(btnVentasDiarias)
                .addGap(41, 41, 41)
                .addComponent(btnVentasPeriodo)
                .addGap(32, 32, 32)
                .addComponent(btnTopProductos)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnReservasDiario.setText("jButton1");

        btnReservasPeriodo.setText("jButton1");

        javax.swing.GroupLayout PanelReservasLayout = new javax.swing.GroupLayout(PanelReservas);
        PanelReservas.setLayout(PanelReservasLayout);
        PanelReservasLayout.setHorizontalGroup(
            PanelReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelReservasLayout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(PanelReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnReservasPeriodo)
                    .addComponent(btnReservasDiario))
                .addGap(27, 27, 27))
        );
        PanelReservasLayout.setVerticalGroup(
            PanelReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelReservasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnReservasDiario)
                .addGap(57, 57, 57)
                .addComponent(btnReservasPeriodo)
                .addGap(44, 44, 44))
        );

        btnReporteSemanal.setText("jButton1");

        btnReporteMensual.setText("jButton1");

        btnReportePeriodo.setText("jButton1");

        javax.swing.GroupLayout PanelGeneralesLayout = new javax.swing.GroupLayout(PanelGenerales);
        PanelGenerales.setLayout(PanelGeneralesLayout);
        PanelGeneralesLayout.setHorizontalGroup(
            PanelGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneralesLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(PanelGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnReportePeriodo)
                    .addComponent(btnReporteMensual)
                    .addComponent(btnReporteSemanal))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        PanelGeneralesLayout.setVerticalGroup(
            PanelGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGeneralesLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnReporteSemanal)
                .addGap(36, 36, 36)
                .addComponent(btnReporteMensual)
                .addGap(36, 36, 36)
                .addComponent(btnReportePeriodo)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        btnReporteFinanzas.setText("jButton1");

        btnHistorialMovimientos.setText("jButton1");

        javax.swing.GroupLayout PanelFinanzasLayout = new javax.swing.GroupLayout(PanelFinanzas);
        PanelFinanzas.setLayout(PanelFinanzasLayout);
        PanelFinanzasLayout.setHorizontalGroup(
            PanelFinanzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelFinanzasLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(PanelFinanzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnHistorialMovimientos)
                    .addComponent(btnReporteFinanzas))
                .addGap(18, 18, 18))
        );
        PanelFinanzasLayout.setVerticalGroup(
            PanelFinanzasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFinanzasLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btnReporteFinanzas)
                .addGap(60, 60, 60)
                .addComponent(btnHistorialMovimientos)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnStockActual.setText("jButton1");

        btnProductosBajoStock.setText("jButton1");

        btnMovimientosInventario.setText("jButton1");

        javax.swing.GroupLayout PanelInventarioLayout = new javax.swing.GroupLayout(PanelInventario);
        PanelInventario.setLayout(PanelInventarioLayout);
        PanelInventarioLayout.setHorizontalGroup(
            PanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStockActual)
                    .addComponent(btnProductosBajoStock)
                    .addComponent(btnMovimientosInventario))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        PanelInventarioLayout.setVerticalGroup(
            PanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInventarioLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(btnStockActual)
                .addGap(26, 26, 26)
                .addComponent(btnProductosBajoStock)
                .addGap(42, 42, 42)
                .addComponent(btnMovimientosInventario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnUsuariosPorRol.setText("jButton1");

        btnListaUsuarios.setText("jButton1");

        javax.swing.GroupLayout PanelUsuariosLayout = new javax.swing.GroupLayout(PanelUsuarios);
        PanelUsuarios.setLayout(PanelUsuariosLayout);
        PanelUsuariosLayout.setHorizontalGroup(
            PanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelUsuariosLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(PanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnListaUsuarios)
                    .addComponent(btnUsuariosPorRol))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        PanelUsuariosLayout.setVerticalGroup(
            PanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelUsuariosLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(btnListaUsuarios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnUsuariosPorRol)
                .addGap(62, 62, 62))
        );

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(PanelVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(PanelReservas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(PanelGenerales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelFinanzas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PanelVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelReservas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelGenerales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelFinanzas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(133, Short.MAX_VALUE))
        );

        btnCerrar.setText("jButton1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(92, 92, 92))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnCerrar)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelFinanzas;
    private javax.swing.JPanel PanelGenerales;
    private javax.swing.JPanel PanelInventario;
    private javax.swing.JPanel PanelReservas;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JPanel PanelUsuarios;
    private javax.swing.JPanel PanelVentas;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnHistorialMovimientos;
    private javax.swing.JButton btnListaUsuarios;
    private javax.swing.JButton btnMovimientosInventario;
    private javax.swing.JButton btnProductosBajoStock;
    private javax.swing.JButton btnReporteFinanzas;
    private javax.swing.JButton btnReporteMensual;
    private javax.swing.JButton btnReportePeriodo;
    private javax.swing.JButton btnReporteSemanal;
    private javax.swing.JButton btnReservasDiario;
    private javax.swing.JButton btnReservasPeriodo;
    private javax.swing.JButton btnStockActual;
    private javax.swing.JButton btnTopProductos;
    private javax.swing.JButton btnUsuariosPorRol;
    private javax.swing.JButton btnVentasDiarias;
    private javax.swing.JButton btnVentasPeriodo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblTitulo;
    // End of variables declaration//GEN-END:variables
}
