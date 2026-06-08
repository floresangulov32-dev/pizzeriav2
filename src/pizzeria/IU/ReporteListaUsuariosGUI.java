package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import pizzeria.controller.GestorUsuarios;
import pizzeria.model.Rol;
import pizzeria.model.Usuario;

public class ReporteListaUsuariosGUI extends JPanel {
    
    private GestorUsuarios gestorUsuarios;
    private DefaultTableModel modeloTabla;
    //private JTable jTable1;
    private JTextField txtBuscar;
    private JLabel lblTotal, lblEmpleados, lblClientes;
    private JButton btnCerrar;
    
    public ReporteListaUsuariosGUI() {
        gestorUsuarios = new GestorUsuarios();
        gestorUsuarios.cargarDesdeArchivo();
        
        // Configurar el panel principal
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Panel Superior (Norte)
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        // Título
        JLabel lblTitulo = new JLabel("LISTA DE USUARIOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelBusqueda.setOpaque(false);
        
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        txtBuscar = new JTextField(10);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        
        // Panel de estadísticas
        JPanel panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelEstadisticas.setOpaque(false);
        panelEstadisticas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));
        
        lblTotal = new JLabel("Total: 0");
        lblEmpleados = new JLabel("Empleados: 0");
        lblClientes = new JLabel("Clientes: 0");
        
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblEmpleados.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblClientes.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        panelEstadisticas.add(lblTotal);
        panelEstadisticas.add(lblEmpleados);
        panelEstadisticas.add(lblClientes);
        
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);
        panelSuperior.add(panelEstadisticas, BorderLayout.EAST);
        
        // Panel Central - Tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Usuarios Registrados",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 11),
            new Color(80, 80, 80)
        ));
        
        // Configurar tabla
        String[] columnas = {"ID", "NOMBRE", "USUARIO", "ROL"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        jTable1 = new JTable(modeloTabla);
        jTable1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        jTable1.setRowHeight(22);
        jTable1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        jTable1.getTableHeader().setBackground(new Color(240, 240, 240));
        jTable1.getTableHeader().setReorderingAllowed(false);
        
        // Anchos de columnas
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(35);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(160);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(90);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(70);
        
        JScrollPane scrollPane = new JScrollPane(jTable1);
        scrollPane.setBorder(null);
        
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        
        // Panel Inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        btnCerrar = new JButton("Volver a Reportes");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnCerrar.setBackground(new Color(168, 27, 29));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> volverAReportes());
        
        panelInferior.add(btnCerrar);
        
        // Agregar todos los paneles
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private void cargarDatos() {
        actualizarTabla();
        actualizarEstadisticas();
    }
    
    private void filtrar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        modeloTabla.setRowCount(0);
        
        for (Usuario u : gestorUsuarios.getListaUsuarios()) {
            if (texto.isEmpty() ||
                String.valueOf(u.getId()).contains(texto) ||
                u.getNombre().toLowerCase().contains(texto) ||
                u.getUsuario().toLowerCase().contains(texto)) {
                modeloTabla.addRow(new Object[]{
                    u.getId(), u.getNombre(), u.getUsuario(), u.getRol().name()
                });
            }
        }
        
        actualizarEstadisticas();
    }
    
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Usuario u : gestorUsuarios.getListaUsuarios()) {
            modeloTabla.addRow(new Object[]{
                u.getId(), u.getNombre(), u.getUsuario(), u.getRol().name()
            });
        }
    }
    
    private void actualizarEstadisticas() {
        int total = gestorUsuarios.getListaUsuarios().size();
        long empleados = gestorUsuarios.getListaUsuarios().stream()
                .filter(u -> u.getRol() != Rol.CLIENTE).count();
        long clientes = total - empleados;
        
        lblTotal.setText("Total: " + total);
        lblEmpleados.setText("Empleados: " + empleados);
        lblClientes.setText("Clientes: " + clientes);
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
        PanelBusqueda = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar2 = new javax.swing.JTextField();
        PanelEstadisticas = new javax.swing.JPanel();
        lblTotal1 = new javax.swing.JLabel();
        lblEmpleados2 = new javax.swing.JLabel();
        lblClientes2 = new javax.swing.JLabel();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelInferior = new javax.swing.JPanel();
        btnCerrar2 = new javax.swing.JButton();

        lblTitulo.setText("jLabel1");

        lblBuscar.setText("jLabel1");

        txtBuscar2.setText("jTextField1");
        txtBuscar2.addActionListener(this::txtBuscar2ActionPerformed);

        javax.swing.GroupLayout PanelBusquedaLayout = new javax.swing.GroupLayout(PanelBusqueda);
        PanelBusqueda.setLayout(PanelBusquedaLayout);
        PanelBusquedaLayout.setHorizontalGroup(
            PanelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBusquedaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                .addComponent(txtBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        PanelBusquedaLayout.setVerticalGroup(
            PanelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBusquedaLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PanelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscar))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        lblTotal1.setText("jLabel2");

        lblEmpleados2.setText("jLabel3");

        lblClientes2.setText("jLabel4");

        javax.swing.GroupLayout PanelEstadisticasLayout = new javax.swing.GroupLayout(PanelEstadisticas);
        PanelEstadisticas.setLayout(PanelEstadisticasLayout);
        PanelEstadisticasLayout.setHorizontalGroup(
            PanelEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelEstadisticasLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTotal1)
                .addGap(120, 120, 120)
                .addComponent(lblEmpleados2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addComponent(lblClientes2)
                .addGap(35, 35, 35))
        );
        PanelEstadisticasLayout.setVerticalGroup(
            PanelEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelEstadisticasLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(PanelEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal1)
                    .addComponent(lblEmpleados2)
                    .addComponent(lblClientes2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(PanelBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelEstadisticas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTitulo)
                    .addComponent(PanelBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelEstadisticas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
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

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnCerrar2.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar2)
                .addGap(50, 50, 50))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addComponent(btnCerrar2)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscar2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelBusqueda;
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelEstadisticas;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JButton btnCerrar2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblClientes2;
    private javax.swing.JLabel lblEmpleados2;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotal1;
    private javax.swing.JTextField txtBuscar2;
    // End of variables declaration//GEN-END:variables
}
