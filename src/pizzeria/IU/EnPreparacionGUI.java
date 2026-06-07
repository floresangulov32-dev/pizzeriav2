package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;

import pizzeria.controller.GestorCocina;
import pizzeria.model.PedidoCocina;
import pizzeria.model.DetalleVenta;

public class EnPreparacionGUI extends JPanel {
    
    private GestorCocina gestorCocina;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private JTextArea txtDetalle;
    
    private DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public EnPreparacionGUI() {
        gestorCocina = ContextoVentasGUI.getInstancia().getGestorCocina();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        cargarDatos();
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("PEDIDOS EN PREPARACIÓN");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInfo.setOpaque(false);
        
        lblCantidad = new JLabel("Pedidos en preparación: 0");
        lblCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCantidad.setForeground(new Color(100, 100, 100));
        
        panelInfo.add(lblCantidad);
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelInfo, BorderLayout.EAST);
        
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
            "Lista de Pedidos en Preparación",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        ));
        
        String[] columnas = {"ID", "TIPO", "CLIENTE", "FECHA", "ITEMS", "TOTAL (Bs.)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tablaPedidos.setRowHeight(24);
        tablaPedidos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        tablaPedidos.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaPedidos.getTableHeader().setReorderingAllowed(false);
        
        // Anchos de columnas
        tablaPedidos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaPedidos.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaPedidos.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaPedidos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaPedidos.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaPedidos.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        // Evento de selección
        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetallePedido();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaPedidos);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de detalle
        JPanel panelDetalle = new JPanel(new BorderLayout());
        panelDetalle.setOpaque(false);
        panelDetalle.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "DETALLE DEL PEDIDO",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(80, 80, 80)
        ));
        panelDetalle.setPreferredSize(new Dimension(0, 150));
        
        txtDetalle = new JTextArea();
        txtDetalle.setEditable(false);
        txtDetalle.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtDetalle.setBackground(new Color(250, 250, 250));
        txtDetalle.setForeground(Color.BLACK);
        txtDetalle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        scrollDetalle.setBorder(null);
        
        panelDetalle.add(scrollDetalle, BorderLayout.CENTER);
        
        panel.add(panelTabla, BorderLayout.CENTER);
        panel.add(panelDetalle, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(e -> cargarDatos());
        
        panel.add(btnActualizar);
        
        return panel;
    }
    
    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        
        java.util.List<PedidoCocina> enPreparacion = gestorCocina.getEnPreparacion();
        
        for (PedidoCocina pedido : enPreparacion) {
            modeloTabla.addRow(new Object[]{
                pedido.getIdPedidoCocina(),
                pedido.getTipoOrigen(),
                pedido.getNombreCliente(),
                pedido.getFechaIngreso().format(formatterFecha),
                pedido.getItems().size(),
                String.format("%.2f", pedido.calcularTotal())
            });
        }
        
        lblCantidad.setText("Pedidos en preparación: " + enPreparacion.size());
        
        if (enPreparacion.isEmpty()) {
            txtDetalle.setText("No hay pedidos en preparación.");
        } else {
            tablaPedidos.setRowSelectionInterval(0, 0);
            mostrarDetallePedido();
        }
    }
    
    private void mostrarDetallePedido() {
        int fila = tablaPedidos.getSelectedRow();
        if (fila == -1) {
            txtDetalle.setText("Seleccione un pedido para ver los detalles.");
            return;
        }
        
        int idPedido = (int) modeloTabla.getValueAt(fila, 0);
        
        PedidoCocina pedidoSeleccionado = null;
        for (PedidoCocina p : gestorCocina.getEnPreparacion()) {
            if (p.getIdPedidoCocina() == idPedido) {
                pedidoSeleccionado = p;
                break;
            }
        }
        
        if (pedidoSeleccionado == null) {
            txtDetalle.setText("No se encontró el detalle del pedido.");
            return;
        }
        
        StringBuilder detalle = new StringBuilder();
        detalle.append("═".repeat(60)).append("\n");
        detalle.append(String.format("PEDIDO #%d\n", pedidoSeleccionado.getIdPedidoCocina()));
        detalle.append(String.format("Origen: %s\n", pedidoSeleccionado.getTipoOrigen()));
        detalle.append(String.format("Cliente: %s\n", pedidoSeleccionado.getNombreCliente()));
        detalle.append(String.format("Fecha ingreso: %s\n", pedidoSeleccionado.getFechaIngreso().format(formatterFecha)));
        detalle.append("═".repeat(60)).append("\n\n");
        detalle.append("ITEMS DEL PEDIDO:\n");
        detalle.append("-".repeat(60)).append("\n");
        
        for (DetalleVenta det : pedidoSeleccionado.getItems()) {
            detalle.append(String.format("  • %s x%d = Bs. %.2f\n",
                det.getProducto().getNombre(),
                det.getCantidad(),
                det.getSubTotal()));
        }
        
        detalle.append("-".repeat(60)).append("\n");
        detalle.append(String.format("TOTAL: Bs. %.2f\n", pedidoSeleccionado.calcularTotal()));
        
        txtDetalle.setText(detalle.toString());
        txtDetalle.setCaretPosition(0);
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
        PanelInfo = new javax.swing.JPanel();
        lblCantidad = new javax.swing.JLabel();
        PanelCentral = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PanelDetalle = new javax.swing.JPanel();
        lblDetalleTitulo = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        PanelInferior = new javax.swing.JPanel();
        btnActualizar = new javax.swing.JButton();

        lblTitulo.setText("jLabel1");

        lblCantidad.setText("jLabel1");

        javax.swing.GroupLayout PanelInfoLayout = new javax.swing.GroupLayout(PanelInfo);
        PanelInfo.setLayout(PanelInfoLayout);
        PanelInfoLayout.setHorizontalGroup(
            PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInfoLayout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(lblCantidad)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelInfoLayout.setVerticalGroup(
            PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInfoLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(lblCantidad)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblTitulo)
                .addGap(31, 31, 31)
                .addComponent(PanelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lblTitulo))
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(PanelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
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

        lblDetalleTitulo.setText("jLabel1");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout PanelDetalleLayout = new javax.swing.GroupLayout(PanelDetalle);
        PanelDetalle.setLayout(PanelDetalleLayout);
        PanelDetalleLayout.setHorizontalGroup(
            PanelDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDetalleLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblDetalleTitulo)
                .addGap(84, 84, 84)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 733, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        PanelDetalleLayout.setVerticalGroup(
            PanelDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDetalleLayout.createSequentialGroup()
                .addGroup(PanelDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDetalleLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(lblDetalleTitulo))
                    .addGroup(PanelDetalleLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelCentralLayout = new javax.swing.GroupLayout(PanelCentral);
        PanelCentral.setLayout(PanelCentralLayout);
        PanelCentralLayout.setHorizontalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(PanelDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelCentralLayout.setVerticalGroup(
            PanelCentralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        btnActualizar.setText("jButton1");

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInferiorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnActualizar)
                .addGap(127, 127, 127))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(btnActualizar)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelCentral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(27, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelDetalle;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelInfo;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblDetalleTitulo;
    private javax.swing.JLabel lblTitulo;
    // End of variables declaration//GEN-END:variables
}
