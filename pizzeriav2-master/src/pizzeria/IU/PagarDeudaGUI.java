package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
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
import pizzeria.model.Deuda;
import pizzeria.model.MovimientoCaja;

public class PagarDeudaGUI extends JPanel {
    
    private GestorFinanzas gestorFinanzas;
    private JTable tablaDeudas;
    private DefaultTableModel modeloTabla;
//    private JButton btnPagar, btnCancelar;
    private JLabel  lblMontoValor;
    
    private Deuda deudaSeleccionada;
    
    public PagarDeudaGUI() {
        gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        cargarTabla();
        actualizarInfoSeleccion(null);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Titulo
        JLabel lblTitulo = new JLabel("PAGAR DEUDA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        // Instruccion
        JLabel lblInstruccion = new JLabel("Seleccione una deuda pendiente de la lista y confirme el pago");
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInstruccion.setForeground(new Color(80, 80, 80));
        
        JPanel panelInstruccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInstruccion.setOpaque(false);
        panelInstruccion.add(lblInstruccion);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelInstruccion, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Panel de tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Deudas Pendientes",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        ));
        
        // Configurar tabla
        String[] columnas = {"ID", "TIPO", "DESCRIPCION", "MONTO (Bs.)", "PROVEEDOR", "FECHA VENCIMIENTO"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDeudas = new JTable(modeloTabla);
        tablaDeudas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaDeudas.setRowHeight(28);
        tablaDeudas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaDeudas.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaDeudas.setShowGrid(true);
        tablaDeudas.setGridColor(new Color(230, 230, 230));
        
        // Evitar mover columnas
        tablaDeudas.getTableHeader().setReorderingAllowed(false);
        
        // Anchos de columnas
        tablaDeudas.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaDeudas.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaDeudas.getColumnModel().getColumn(0).setMinWidth(40);
        
        tablaDeudas.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaDeudas.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaDeudas.getColumnModel().getColumn(3).setPreferredWidth(90);
        tablaDeudas.getColumnModel().getColumn(4).setPreferredWidth(120);
        tablaDeudas.getColumnModel().getColumn(5).setPreferredWidth(120);
        
        // Evento de seleccion
        tablaDeudas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaDeudas.getSelectedRow();
                if (fila != -1) {
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    deudaSeleccionada = gestorFinanzas.buscarDeudaPorId(id);
                    actualizarInfoSeleccion(deudaSeleccionada);
                    btnPagar.setEnabled(true);
                } else {
                    actualizarInfoSeleccion(null);
                    btnPagar.setEnabled(false);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaDeudas);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de informacion de la deuda seleccionada
        JPanel panelInfo = crearPanelInfo();
        
        panel.add(panelTabla, BorderLayout.CENTER);
        panel.add(panelInfo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelInfo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Titulo de la seccion
        JLabel lblSeccionTitulo = new JLabel("DEUDA SELECCIONADA");
        lblSeccionTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSeccionTitulo.setForeground(new Color(168, 27, 29));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblSeccionTitulo, gbc);
        
        // ID
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        lblIdValor = new JLabel("-");
        lblIdValor.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblIdValor, gbc);
        
        // Descripcion
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Descripcion:"), gbc);
        gbc.gridx = 1;
        lblDescripcionValor = new JLabel("-");
        panel.add(lblDescripcionValor, gbc);
        
        // Monto
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Monto:"), gbc);
        gbc.gridx = 1;
        lblMontoValor = new JLabel("-");
        lblMontoValor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMontoValor.setForeground(new Color(231, 76, 60));
        panel.add(lblMontoValor, gbc);
        
        // Proveedor
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Proveedor/Empleado:"), gbc);
        gbc.gridx = 1;
        lblProveedorValor = new JLabel("-");
        panel.add(lblProveedorValor, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btnPagar = new JButton("PAGAR DEUDA");
        btnPagar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPagar.setBackground(new Color(46, 204, 113));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnPagar.setFocusPainted(false);
        btnPagar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPagar.setEnabled(false);
        btnPagar.addActionListener(e -> pagarDeuda());
        
        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(168, 27, 29));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> cerrar());
        
        panel.add(btnPagar);
        panel.add(btnCancelar);
        
        return panel;
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Deuda d : gestorFinanzas.getDeudasPendientes()) {
            modeloTabla.addRow(new Object[]{
                d.getId(),
                d.getTipo(),
                d.getDescripcion(),
                String.format("%.2f", d.getMontoTotal()),
                d.getProveedor(),
                d.getFechaCompromiso().format(formatter)
            });
        }
        
        if (gestorFinanzas.getDeudasPendientes().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay deudas pendientes para pagar.", 
                "Informacion", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void actualizarInfoSeleccion(Deuda deuda) {
        if (deuda != null) {
            lblIdValor.setText(String.valueOf(deuda.getId()));
            lblDescripcionValor.setText(deuda.getDescripcion());
            lblMontoValor.setText(String.format("Bs. %.2f", deuda.getMontoTotal()));
            lblProveedorValor.setText(deuda.getProveedor());
        } else {
            lblIdValor.setText("-");
            lblDescripcionValor.setText("-");
            lblMontoValor.setText("-");
            lblProveedorValor.setText("-");
        }
    }
    
    private void pagarDeuda() {
        if (deudaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una deuda para pagar.", 
                "Advertencia", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        double saldoActual = gestorFinanzas.calcularSaldo();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Detalles del pago:\n\n" +
            "ID Deuda: " + deudaSeleccionada.getId() + "\n" +
            "Descripcion: " + deudaSeleccionada.getDescripcion() + "\n" +
            "Monto a pagar: Bs. " + String.format("%.2f", deudaSeleccionada.getMontoTotal()) + "\n\n" +
            "Saldo actual en caja: Bs. " + String.format("%.2f", saldoActual) + "\n\n" +
            (saldoActual >= deudaSeleccionada.getMontoTotal() ? 
                "¿Confirmar el pago?" : 
                "ADVERTENCIA: Saldo insuficiente. ¿Desea pagar de todas formas?"),
            "Confirmar Pago",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // CORREGIDO: el metodo devuelve MovimientoCaja, no boolean
            MovimientoCaja movimiento = gestorFinanzas.pagarDeuda(deudaSeleccionada.getId());
            boolean exito = (movimiento != null);

            if (exito) {
                gestorFinanzas.guardarArchivos();
                JOptionPane.showMessageDialog(this,
                    "Deuda pagada exitosamente.\n\n" +
                    "Nuevo saldo: Bs. " + String.format("%.2f", gestorFinanzas.calcularSaldo()),
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE);

                // Volver al panel de finanzas
                cerrar();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al pagar la deuda.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
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
        lblInstruccion = new javax.swing.JLabel();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelInfo = new javax.swing.JPanel();
        lblSeleccionTitulo = new javax.swing.JLabel();
        lblIdValor = new javax.swing.JLabel();
        lblDescripcionValor = new javax.swing.JLabel();
        blMontoValor = new javax.swing.JLabel();
        lblProveedorValor = new javax.swing.JLabel();
        PanelInferior = new javax.swing.JPanel();
        btnPagar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        lblTitulo2.setText("jLabel1");

        lblInstruccion.setText("jLabel1");

        javax.swing.GroupLayout PanelSuperior2Layout = new javax.swing.GroupLayout(PanelSuperior2);
        PanelSuperior2.setLayout(PanelSuperior2Layout);
        PanelSuperior2Layout.setHorizontalGroup(
            PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperior2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTitulo2)
                .addGap(379, 379, 379)
                .addComponent(lblInstruccion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelSuperior2Layout.setVerticalGroup(
            PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperior2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelSuperior2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitulo2)
                    .addComponent(lblInstruccion))
                .addContainerGap(72, Short.MAX_VALUE))
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

        lblSeleccionTitulo.setText("jLabel1");

        lblIdValor.setText("jLabel1");

        lblDescripcionValor.setText("jLabel1");

        blMontoValor.setText("jLabel1");

        lblProveedorValor.setText("jLabel2");

        javax.swing.GroupLayout PanelInfoLayout = new javax.swing.GroupLayout(PanelInfo);
        PanelInfo.setLayout(PanelInfoLayout);
        PanelInfoLayout.setHorizontalGroup(
            PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInfoLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(lblSeleccionTitulo)
                .addGap(142, 142, 142)
                .addComponent(lblIdValor)
                .addGap(234, 234, 234)
                .addComponent(lblDescripcionValor)
                .addGap(139, 139, 139)
                .addComponent(blMontoValor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(lblProveedorValor)
                .addGap(43, 43, 43))
        );
        PanelInfoLayout.setVerticalGroup(
            PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInfoLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSeleccionTitulo)
                    .addComponent(lblIdValor)
                    .addComponent(lblDescripcionValor)
                    .addComponent(blMontoValor)
                    .addComponent(lblProveedorValor))
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
                .addComponent(PanelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        btnPagar.setText("jButton1");

        btnCancelar.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPagar)
                .addGap(140, 140, 140)
                .addComponent(btnCancelar)
                .addGap(119, 119, 119))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPagar)
                    .addComponent(btnCancelar))
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
                .addGap(18, 18, 18)
                .addComponent(PanelCentral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelInfo;
    private javax.swing.JPanel PanelSuperior2;
    private javax.swing.JLabel blMontoValor;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnPagar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblDescripcionValor;
    private javax.swing.JLabel lblIdValor;
    private javax.swing.JLabel lblInstruccion;
    private javax.swing.JLabel lblProveedorValor;
    private javax.swing.JLabel lblSeleccionTitulo;
    private javax.swing.JLabel lblTitulo2;
    // End of variables declaration//GEN-END:variables
}
