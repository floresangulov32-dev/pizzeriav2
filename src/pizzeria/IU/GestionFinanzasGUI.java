package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import pizzeria.controller.GestorFinanzas;
import pizzeria.model.MovimientoCaja;
import pizzeria.model.Deuda;

public class GestionFinanzasGUI extends JPanel {
    
    private GestorFinanzas gestorFinanzas;
    /*
    // Labels para valores
    private JLabel lblSaldoValor;
    private JLabel lblIngresosValor;
    private JLabel lblEgresosValor;
    private JLabel lblDeudaValor;
    private JLabel lblDisponibleValor;
    
    // Botones
    private JButton btnVerHistorial;
    private JButton btnRegistrarEgreso;
    private JButton btnRegistrarDeuda;
    private JButton btnVerDeudasPendientes;
    private JButton btnPagarDeuda;
    private JButton btnVerTodasDeudas;
    private JButton btnReporteDiario;
    private JButton btnReporteSemanal;
    private JButton btnReporteMensual;
    private JButton btnReporteRangos;*/
    
    public GestionFinanzasGUI() {
        gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel superior - Tarjetas de resumen
        add(crearPanelResumen(), BorderLayout.NORTH);
        
        // Panel central - Botones de acción
        add(crearPanelAcciones(), BorderLayout.CENTER);
        
        // Panel inferior
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        actualizarResumen();
    }
    
    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Tarjeta Saldo
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(crearTarjeta(" SALDO ACTUAL", "Bs. 0.00", new Color(52, 152, 219)), gbc);
        
        // Tarjeta Ingresos del día
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(crearTarjeta(" INGRESOS DEL DÍA", "Bs. 0.00", new Color(46, 204, 113)), gbc);
        
        // Tarjeta Egresos del día
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(crearTarjeta(" EGRESOS DEL DÍA", "Bs. 0.00", new Color(231, 76, 60)), gbc);
        
        // Tarjeta Deuda Pendiente
        gbc.gridx = 3; gbc.gridy = 0;
        panel.add(crearTarjeta(" DEUDA PENDIENTE", "Bs. 0.00", new Color(241, 196, 15)), gbc);
        
        // Tarjeta Saldo Disponible
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(crearTarjeta(" SALDO DISPONIBLE", "Bs. 0.00", new Color(155, 89, 182)), gbc);
        
        return panel;
    }
    
    private JPanel crearTarjeta(String titulo, String valor, Color colorBorde) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBorde, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.setPreferredSize(new Dimension(200, 80));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitulo.setForeground(new Color(80, 80, 80));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValor.setForeground(colorBorde);
        
        // Guardar referencia
        if (titulo.contains("SALDO ACTUAL")) {
            lblSaldoValor = lblValor;
        } else if (titulo.contains("INGRESOS DEL DÍA")) {
            lblIngresosValor = lblValor;
        } else if (titulo.contains("EGRESOS DEL DÍA")) {
            lblEgresosValor = lblValor;
        } else if (titulo.contains("DEUDA PENDIENTE")) {
            lblDeudaValor = lblValor;
        } else if (titulo.contains("SALDO DISPONIBLE")) {
            lblDisponibleValor = lblValor;
        }
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Columna 1 - Caja
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(crearPanelCaja(), gbc);
        
        // Columna 2 - Deudas
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(crearPanelDeudas(), gbc);
        
        // Columna 3 - Reportes
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(crearPanelReportes(), gbc);
        
        return panel;
    }
    
    private JPanel crearPanelCaja() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "💵 CAJA",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(52, 152, 219)
        ));
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        
        btnVerHistorial = crearBotonAccion(" Ver Historial de movimientos", new Color(52, 152, 219));
        btnRegistrarEgreso = crearBotonAccion(" Registrar egreso directo", new Color(231, 76, 60));
        
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnVerHistorial);
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnRegistrarEgreso);
        panel.add(javax.swing.Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel crearPanelDeudas() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            " DEUDAS Y COMPROMISOS",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(241, 196, 15)
        ));
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        
        btnRegistrarDeuda = crearBotonAccion(" Registrar compromiso/Deuda", new Color(241, 196, 15));
        btnVerDeudasPendientes = crearBotonAccion(" Ver deudas pendientes", new Color(230, 126, 34));
        btnPagarDeuda = crearBotonAccion(" Pagar deuda", new Color(46, 204, 113));
        btnVerTodasDeudas = crearBotonAccion(" Ver todas las deudas", new Color(155, 89, 182));
        
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnRegistrarDeuda);
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnVerDeudasPendientes);
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnPagarDeuda);
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnVerTodasDeudas);
        panel.add(javax.swing.Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "📊 REPORTES",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(46, 204, 113)
        ));
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        
        btnReporteDiario = crearBotonAccion(" Reporte diario", new Color(46, 204, 113));
        btnReporteSemanal = crearBotonAccion(" Reporte semanal", new Color(46, 204, 113));
        btnReporteMensual = crearBotonAccion(" Reporte mensual", new Color(46, 204, 113));
        btnReporteRangos = crearBotonAccion(" Reporte entre rangos", new Color(46, 204, 113));
        
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnReporteDiario);
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnReporteSemanal);
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnReporteMensual);
        panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnReporteRangos);
        panel.add(javax.swing.Box.createVerticalGlue());
        
        return panel;
    }
    
    private JButton crearBotonAccion(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(300, 45));
        boton.setPreferredSize(new Dimension(280, 45));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(boton.getBackground().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        // Eventos
        if (texto.contains("Ver Historial")) {
            boton.addActionListener(e -> verHistorial());
        } else if (texto.contains("Registrar egreso")) {
            boton.addActionListener(e -> registrarEgresoDirecto());
        } else if (texto.contains("Registrar compromiso")) {
            boton.addActionListener(e -> registrarDeuda());
        } else if (texto.contains("Ver deudas pendientes")) {
            boton.addActionListener(e -> verDeudasPendientes());
        } else if (texto.contains("Pagar deuda")) {
            boton.addActionListener(e -> pagarDeuda());
        } else if (texto.contains("Ver todas las deudas")) {
            boton.addActionListener(e -> verTodasDeudas());
        } else if (texto.contains("Reporte diario")) {
            boton.addActionListener(e -> reporteDiario());
        } else if (texto.contains("Reporte semanal")) {
            boton.addActionListener(e -> reporteSemanal());
        } else if (texto.contains("Reporte mensual")) {
            boton.addActionListener(e -> reporteMensual());
        } else if (texto.contains("Reporte entre rangos")) {
            boton.addActionListener(e -> reporteRangos());
        }
        
        return boton;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JLabel lblInfo = new JLabel("by StarTech | Versión 1.0");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(120, 120, 120));
        
        panel.add(lblInfo);
        
        return panel;
    }
    
    private void actualizarResumen() {
        double saldo = gestorFinanzas.calcularSaldo();
        double deudaPendiente = gestorFinanzas.totalDeudaPendiente();
        double saldoDisponible = saldo - deudaPendiente;
        
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.plusDays(1).atStartOfDay().minusNanos(1);
        
        double ingresosDia = gestorFinanzas.sumaIngresos(inicioDia, finDia);
        double egresosDia = gestorFinanzas.sumaEgresos(inicioDia, finDia);
        
        lblSaldoValor.setText(String.format("Bs. %.2f", saldo));
        lblIngresosValor.setText(String.format("Bs. %.2f", ingresosDia));
        lblEgresosValor.setText(String.format("Bs. %.2f", egresosDia));
        lblDeudaValor.setText(String.format("Bs. %.2f", deudaPendiente));
        lblDisponibleValor.setText(String.format("Bs. %.2f", saldoDisponible));
    }
    
    private void verHistorial() {
        // Reemplazar el panel actual con el historial
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new HistorialMovimientosGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void registrarEgresoDirecto() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMonto = new JTextField(15);
        JTextField txtDescripcion = new JTextField(15);

        String[] categorias = {"CompraInsumo", "Salario", "Otro"};
        JComboBox<String> cbCategoria = new JComboBox<>(categorias);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Monto (Bs.):"), gbc);
        gbc.gridx = 1;
        panel.add(txtMonto, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        panel.add(cbCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDescripcion, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Egreso",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double monto = Double.parseDouble(txtMonto.getText().trim());
                String categoria = (String) cbCategoria.getSelectedItem();
                String descripcion = txtDescripcion.getText().trim();

                if (monto <= 0) {
                    JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese una descripción.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Obtener saldo actual
                double saldoActual = gestorFinanzas.calcularSaldo();

                // Verificar si el egreso supera el saldo
                if (monto > saldoActual) {
                    int advertencia = JOptionPane.showConfirmDialog(this,
                        "ADVERTENCIA: El monto del egreso (Bs. " + String.format("%.2f", monto) + 
                        ") supera el saldo actual (Bs. " + String.format("%.2f", saldoActual) + ").\n\n" +
                        "Si continúa, el saldo quedará negativo.\n\n¿Desea continuar de todas formas?",
                        "Saldo Insuficiente",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                    if (advertencia != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                // Ventana de confirmación con los datos
                String mensajeConfirmacion = 
                    "═══════════ CONFIRMAR EGRESO ═══════════\n\n" +
                    "Monto: Bs. " + String.format("%.2f", monto) + "\n" +
                    "Categoría: " + categoria + "\n" +
                    "Descripción: " + descripcion + "\n\n" +
                    "Saldo actual: Bs. " + String.format("%.2f", saldoActual) + "\n" +
                    "Saldo después: Bs. " + String.format("%.2f", saldoActual - monto) + "\n\n" +
                    "¿Confirmar el registro del egreso?";

                int confirmar = JOptionPane.showConfirmDialog(this,
                    mensajeConfirmacion,
                    "Confirmar Egreso",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (confirmar == JOptionPane.YES_OPTION) {
                    gestorFinanzas.registrarEgreso(monto, categoria, descripcion);
                    gestorFinanzas.guardarArchivos();
                    JOptionPane.showMessageDialog(this, "Egreso registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarResumen();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void registrarDeuda() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMonto = new JTextField(15);
        JTextField txtDescripcion = new JTextField(15);
        JTextField txtProveedor = new JTextField(15);

        // Campo para fecha de vencimiento
        JTextField txtFechaVencimiento = new JTextField(15);
        txtFechaVencimiento.setToolTipText("Formato: YYYY-MM-DD (ejemplo: 2026-05-30)");

        String[] tipos = {"CompraInsumo", "Salario", "Otro"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panel.add(cbTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Monto (Bs.):"), gbc);
        gbc.gridx = 1;
        panel.add(txtMonto, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Proveedor/Empleado:"), gbc);
        gbc.gridx = 1;
        panel.add(txtProveedor, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Fecha Vencimiento (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(txtFechaVencimiento, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Deuda",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double monto = Double.parseDouble(txtMonto.getText().trim());
                String tipo = (String) cbTipo.getSelectedItem();
                String descripcion = txtDescripcion.getText().trim();
                String proveedor = txtProveedor.getText().trim();
                String fechaStr = txtFechaVencimiento.getText().trim();

                if (monto <= 0) {
                    JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese una descripción.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (proveedor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese el proveedor/empleado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDateTime fechaVencimiento;
                LocalDate fechaActual = LocalDate.now();

                if (fechaStr.isEmpty()) {
                    fechaVencimiento = fechaActual.plusDays(30).atStartOfDay();
                } else {
                    LocalDate fechaIngresada = LocalDate.parse(fechaStr);

                    // VALIDACIÓN: La fecha no puede ser inferior a la actual
                    if (fechaIngresada.isBefore(fechaActual)) {
                        JOptionPane.showMessageDialog(this, 
                            "ERROR: La fecha de vencimiento (" + fechaIngresada + ") es anterior a la fecha actual (" + fechaActual + ").\n" +
                            "Ingrese una fecha válida (futura).",
                            "Fecha Inválida",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    fechaVencimiento = fechaIngresada.atStartOfDay();
                }

                // Obtener saldo actual
                double saldoActual = gestorFinanzas.calcularSaldo();
                double deudaPendienteTotal = gestorFinanzas.totalDeudaPendiente();
                double saldoDisponible = saldoActual - deudaPendienteTotal;

                // Verificar si la nueva deuda afectará significativamente el saldo
                if (monto > saldoDisponible && saldoDisponible > 0) {
                    int advertencia = JOptionPane.showConfirmDialog(this,
                        "ADVERTENCIA: La nueva deuda (Bs. " + String.format("%.2f", monto) + 
                        ") supera el saldo disponible (Bs. " + String.format("%.2f", saldoDisponible) + ").\n\n" +
                        "Saldo actual: Bs. " + String.format("%.2f", saldoActual) + "\n" +
                        "Deudas pendientes: Bs. " + String.format("%.2f", deudaPendienteTotal) + "\n\n" +
                        "¿Desea registrar la deuda de todas formas?",
                        "Advertencia de Endeudamiento",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                    if (advertencia != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                // Calcular días hasta vencimiento para mostrar en confirmación
                long diasHastaVencimiento = java.time.temporal.ChronoUnit.DAYS.between(fechaActual, fechaVencimiento.toLocalDate());
                String mensajeDias = (diasHastaVencimiento <= 7) ? 
                    " ATENCIÓN: Faltan " + diasHastaVencimiento + " días para el vencimiento ⚠️\n\n" : 
                    "Faltan " + diasHastaVencimiento + " días para el vencimiento.\n\n";

                // Ventana de confirmación con los datos
                String mensajeConfirmacion = 
                    "═══════════ CONFIRMAR DEUDA ═══════════\n\n" +
                    "Tipo: " + tipo + "\n" +
                    "Descripción: " + descripcion + "\n" +
                    "Monto: Bs. " + String.format("%.2f", monto) + "\n" +
                    "Proveedor/Empleado: " + proveedor + "\n" +
                    "Fecha Vencimiento: " + fechaVencimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                    mensajeDias +
                    "═══════════ SITUACIÓN FINANCIERA ═══════════\n\n" +
                    "Saldo actual en caja: Bs. " + String.format("%.2f", saldoActual) + "\n" +
                    "Deudas pendientes actuales: Bs. " + String.format("%.2f", deudaPendienteTotal) + "\n" +
                    "Nueva deuda: Bs. " + String.format("%.2f", monto) + "\n" +
                    "Total deudas después: Bs. " + String.format("%.2f", deudaPendienteTotal + monto) + "\n\n" +
                    "¿Confirmar el registro de la deuda?";

                int confirmar = JOptionPane.showConfirmDialog(this,
                    mensajeConfirmacion,
                    "Confirmar Deuda",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (confirmar == JOptionPane.YES_OPTION) {
                    gestorFinanzas.registrarDeuda(tipo, descripcion, monto, proveedor, fechaVencimiento);
                    gestorFinanzas.guardarArchivos();
                    JOptionPane.showMessageDialog(this, "Deuda registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarResumen();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Fecha inválida. Use el formato YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void verDeudasPendientes() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            TodasDeudasGUI panelDeudas = new TodasDeudasGUI();
            panelDeudas.filtrar("PENDIENTES");
            parent.removeAll();
            parent.add(panelDeudas, BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }

    private void pagarDeuda() { 
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new PagarDeudaGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void verTodasDeudas() {    
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new TodasDeudasGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }    
    private void reporteDiario() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteDiarioGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteSemanal() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteSemanalGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
     
    private void reporteMensual() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteMensualGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void reporteRangos() {
        JPanel parent = (JPanel) getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(new ReporteRangosGUI(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void mostrarTextoEnDialogo(String titulo, String texto) {
        JTextArea textArea = new JTextArea(texto);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelCentral = new javax.swing.JPanel();
        panelCaja = new javax.swing.JPanel();
        tituloCaja = new javax.swing.JLabel();
        btnVerHistorial = new javax.swing.JButton();
        btnRegistrarEgreso = new javax.swing.JButton();
        panelDeudas = new javax.swing.JPanel();
        tituloDeudas = new javax.swing.JLabel();
        btnRegistrarDeuda = new javax.swing.JButton();
        btnVerDeudasPendientes = new javax.swing.JButton();
        btnPagarDeuda = new javax.swing.JButton();
        btnVerTodasDeudas = new javax.swing.JButton();
        panelReportes = new javax.swing.JPanel();
        tituloDeudas1 = new javax.swing.JLabel();
        tituloReportes = new javax.swing.JButton();
        btnReporteDiario = new javax.swing.JButton();
        btnReporteSemanal = new javax.swing.JButton();
        btnReporteMensual = new javax.swing.JButton();
        btnReporteRangos = new javax.swing.JButton();
        panelSuperior = new javax.swing.JPanel();
        panelDisponible = new javax.swing.JPanel();
        lblDisponibleTitulo = new javax.swing.JLabel();
        lblDisponibleValor = new javax.swing.JLabel();
        panelDeuda = new javax.swing.JPanel();
        lblDeudaTitulo = new javax.swing.JLabel();
        lblDeudaValor = new javax.swing.JLabel();
        panelEgresos = new javax.swing.JPanel();
        lblEgresosTitulo = new javax.swing.JLabel();
        lblEgresosValor = new javax.swing.JLabel();
        panelIngresos = new javax.swing.JPanel();
        lblIngresosTitulo = new javax.swing.JLabel();
        lblIngresosValor = new javax.swing.JLabel();
        panelSaldo3 = new javax.swing.JPanel();
        lblSaldoTitulo3 = new javax.swing.JLabel();
        lblSaldoValor3 = new javax.swing.JLabel();
        PanelInferior = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();

        tituloCaja.setText("jLabel1");

        btnVerHistorial.setText("jButton1");

        btnRegistrarEgreso.setText("jButton1");

        javax.swing.GroupLayout panelCajaLayout = new javax.swing.GroupLayout(panelCaja);
        panelCaja.setLayout(panelCajaLayout);
        panelCajaLayout.setHorizontalGroup(
            panelCajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCajaLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(btnVerHistorial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 704, Short.MAX_VALUE)
                .addComponent(btnRegistrarEgreso)
                .addGap(679, 679, 679))
            .addGroup(panelCajaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(tituloCaja)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCajaLayout.setVerticalGroup(
            panelCajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCajaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tituloCaja)
                .addGap(18, 18, 18)
                .addGroup(panelCajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVerHistorial)
                    .addComponent(btnRegistrarEgreso))
                .addGap(0, 18, Short.MAX_VALUE))
        );

        tituloDeudas.setText("jLabel1");

        btnRegistrarDeuda.setText("jButton1");

        btnVerDeudasPendientes.setText("jButton1");

        btnPagarDeuda.setText("jButton1");

        btnVerTodasDeudas.setText("jButton1");

        javax.swing.GroupLayout panelDeudasLayout = new javax.swing.GroupLayout(panelDeudas);
        panelDeudas.setLayout(panelDeudasLayout);
        panelDeudasLayout.setHorizontalGroup(
            panelDeudasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeudasLayout.createSequentialGroup()
                .addGroup(panelDeudasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDeudasLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(btnRegistrarDeuda)
                        .addGap(192, 192, 192)
                        .addComponent(btnVerDeudasPendientes)
                        .addGap(165, 165, 165)
                        .addComponent(btnPagarDeuda)
                        .addGap(313, 313, 313)
                        .addComponent(btnVerTodasDeudas))
                    .addGroup(panelDeudasLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(tituloDeudas)))
                .addContainerGap(562, Short.MAX_VALUE))
        );
        panelDeudasLayout.setVerticalGroup(
            panelDeudasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeudasLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(tituloDeudas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(panelDeudasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegistrarDeuda)
                    .addComponent(btnVerDeudasPendientes)
                    .addComponent(btnPagarDeuda)
                    .addComponent(btnVerTodasDeudas))
                .addGap(18, 18, 18))
        );

        tituloDeudas1.setText("jLabel1");

        tituloReportes.setText("jButton1");

        btnReporteDiario.setText("jButton1");

        btnReporteSemanal.setText("jButton1");

        btnReporteMensual.setText("jButton1");

        btnReporteRangos.setText("jButton1");

        javax.swing.GroupLayout panelReportesLayout = new javax.swing.GroupLayout(panelReportes);
        panelReportes.setLayout(panelReportesLayout);
        panelReportesLayout.setHorizontalGroup(
            panelReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportesLayout.createSequentialGroup()
                .addGroup(panelReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelReportesLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(tituloDeudas1))
                    .addGroup(panelReportesLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(tituloReportes)
                        .addGap(197, 197, 197)
                        .addComponent(btnReporteDiario)
                        .addGap(163, 163, 163)
                        .addComponent(btnReporteSemanal)
                        .addGap(312, 312, 312)
                        .addComponent(btnReporteMensual)
                        .addGap(217, 217, 217)
                        .addComponent(btnReporteRangos)))
                .addContainerGap(269, Short.MAX_VALUE))
        );
        panelReportesLayout.setVerticalGroup(
            panelReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tituloDeudas1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(panelReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tituloReportes)
                    .addComponent(btnReporteDiario)
                    .addComponent(btnReporteSemanal)
                    .addComponent(btnReporteMensual)
                    .addComponent(btnReporteRangos))
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelDeudas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelReportes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addComponent(panelCaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(panelDeudas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(panelReportes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        lblDisponibleTitulo.setText("jLabel1");

        lblDisponibleValor.setText("jLabel1");

        javax.swing.GroupLayout panelDisponibleLayout = new javax.swing.GroupLayout(panelDisponible);
        panelDisponible.setLayout(panelDisponibleLayout);
        panelDisponibleLayout.setHorizontalGroup(
            panelDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDisponibleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDisponibleTitulo)
                    .addComponent(lblDisponibleValor))
                .addContainerGap(140, Short.MAX_VALUE))
        );
        panelDisponibleLayout.setVerticalGroup(
            panelDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDisponibleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDisponibleTitulo)
                .addGap(18, 18, 18)
                .addComponent(lblDisponibleValor)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        lblDeudaTitulo.setText("jLabel1");

        lblDeudaValor.setText("jLabel1");

        javax.swing.GroupLayout panelDeudaLayout = new javax.swing.GroupLayout(panelDeuda);
        panelDeuda.setLayout(panelDeudaLayout);
        panelDeudaLayout.setHorizontalGroup(
            panelDeudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeudaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDeudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDeudaTitulo)
                    .addComponent(lblDeudaValor))
                .addContainerGap(140, Short.MAX_VALUE))
        );
        panelDeudaLayout.setVerticalGroup(
            panelDeudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeudaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDeudaTitulo)
                .addGap(18, 18, 18)
                .addComponent(lblDeudaValor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblEgresosTitulo.setText("jLabel1");

        lblEgresosValor.setText("jLabel1");

        javax.swing.GroupLayout panelEgresosLayout = new javax.swing.GroupLayout(panelEgresos);
        panelEgresos.setLayout(panelEgresosLayout);
        panelEgresosLayout.setHorizontalGroup(
            panelEgresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEgresosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEgresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEgresosTitulo)
                    .addComponent(lblEgresosValor))
                .addContainerGap(140, Short.MAX_VALUE))
        );
        panelEgresosLayout.setVerticalGroup(
            panelEgresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEgresosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEgresosTitulo)
                .addGap(18, 18, 18)
                .addComponent(lblEgresosValor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblIngresosTitulo.setText("jLabel1");

        lblIngresosValor.setText("jLabel1");

        javax.swing.GroupLayout panelIngresosLayout = new javax.swing.GroupLayout(panelIngresos);
        panelIngresos.setLayout(panelIngresosLayout);
        panelIngresosLayout.setHorizontalGroup(
            panelIngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIngresosLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelIngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIngresosValor)
                    .addComponent(lblIngresosTitulo))
                .addContainerGap(130, Short.MAX_VALUE))
        );
        panelIngresosLayout.setVerticalGroup(
            panelIngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIngresosLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(lblIngresosTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblIngresosValor)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        panelSaldo3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblSaldoTitulo3.setText("jLabel1");

        lblSaldoValor3.setText("jLabel1");

        javax.swing.GroupLayout panelSaldo3Layout = new javax.swing.GroupLayout(panelSaldo3);
        panelSaldo3.setLayout(panelSaldo3Layout);
        panelSaldo3Layout.setHorizontalGroup(
            panelSaldo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSaldo3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelSaldo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSaldoValor3)
                    .addComponent(lblSaldoTitulo3))
                .addContainerGap(123, Short.MAX_VALUE))
        );
        panelSaldo3Layout.setVerticalGroup(
            panelSaldo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSaldo3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblSaldoTitulo3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSaldoValor3)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelSuperiorLayout = new javax.swing.GroupLayout(panelSuperior);
        panelSuperior.setLayout(panelSuperiorLayout);
        panelSuperiorLayout.setHorizontalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSuperiorLayout.createSequentialGroup()
                        .addComponent(panelSaldo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(panelIngresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(panelEgresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                        .addComponent(panelDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35))
                    .addGroup(panelSuperiorLayout.createSequentialGroup()
                        .addComponent(panelDeuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelSuperiorLayout.setVerticalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSaldo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelSuperiorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelEgresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelIngresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelDeuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lblInfo.setText("jLabel1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblInfo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblInfo)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(PanelCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(55, 55, 55))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(PanelCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JPanel PanelSuperior1;
    private javax.swing.JPanel PanelSuperior2;
    private javax.swing.JButton btnPagarDeuda;
    private javax.swing.JButton btnRegistrarDeuda;
    private javax.swing.JButton btnRegistrarEgreso;
    private javax.swing.JButton btnReporteDiario;
    private javax.swing.JButton btnReporteMensual;
    private javax.swing.JButton btnReporteRangos;
    private javax.swing.JButton btnReporteSemanal;
    private javax.swing.JButton btnVerDeudasPendientes;
    private javax.swing.JButton btnVerHistorial;
    private javax.swing.JButton btnVerTodasDeudas;
    private javax.swing.JLabel lblDeudaTitulo;
    private javax.swing.JLabel lblDeudaValor;
    private javax.swing.JLabel lblDisponibleTitulo;
    private javax.swing.JLabel lblDisponibleValor;
    private javax.swing.JLabel lblEgresosTitulo;
    private javax.swing.JLabel lblEgresosValor;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblIngresosTitulo;
    private javax.swing.JLabel lblIngresosValor;
    private javax.swing.JLabel lblSaldoTitulo;
    private javax.swing.JLabel lblSaldoTitulo1;
    private javax.swing.JLabel lblSaldoTitulo2;
    private javax.swing.JLabel lblSaldoTitulo3;
    private javax.swing.JLabel lblSaldoValor;
    private javax.swing.JLabel lblSaldoValor1;
    private javax.swing.JLabel lblSaldoValor2;
    private javax.swing.JLabel lblSaldoValor3;
    private javax.swing.JPanel panelCaja;
    private javax.swing.JPanel panelDeuda;
    private javax.swing.JPanel panelDeudas;
    private javax.swing.JPanel panelDisponible;
    private javax.swing.JPanel panelEgresos;
    private javax.swing.JPanel panelIngresos;
    private javax.swing.JPanel panelReportes;
    private javax.swing.JPanel panelSaldo;
    private javax.swing.JPanel panelSaldo1;
    private javax.swing.JPanel panelSaldo2;
    private javax.swing.JPanel panelSaldo3;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JLabel tituloCaja;
    private javax.swing.JLabel tituloDeudas;
    private javax.swing.JLabel tituloDeudas1;
    private javax.swing.JButton tituloReportes;
    // End of variables declaration//GEN-END:variables
}
