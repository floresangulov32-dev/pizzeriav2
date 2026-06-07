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
import javax.swing.table.JTableHeader;

import pizzeria.controller.GestorUsuarios;
import pizzeria.model.Rol;
import pizzeria.model.Usuario;

public class GestionUsuariosGUI extends JPanel {
    
    private GestorUsuarios gestorUsuarios;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    
    private int paginaActual = 1;
    private int filasPorPagina = 10;
    private java.util.List<Usuario> usuariosMostrados;
    
    public GestionUsuariosGUI() {
        gestorUsuarios = new GestorUsuarios();
        gestorUsuarios.cargarDesdeArchivo();

        setLayout(new BorderLayout());
        setOpaque(false); 
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior
        add(crearPanelSuperior(), BorderLayout.NORTH);

        // Panel central (tabla)
        add(crearPanelCentral(), BorderLayout.CENTER);

        // Panel inferior
        add(crearPanelInferior(), BorderLayout.SOUTH);

        cargarDatos();
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);        
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Título
        JLabel lblTitulo = new JLabel("GESTIÓN DE USUARIOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        // Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setOpaque(false);
        
        JLabel lblBuscar = new JLabel("🔍 Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
        
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        
        // Panel de estadísticas
        JPanel panelStats = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        lblTotal = new JLabel("Total: 0");
        lblEmpleados = new JLabel("| Empleados: 0");
        lblClientes = new JLabel("| Clientes: 0");
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEmpleados.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblClientes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panelStats.add(lblTotal);
        panelStats.add(lblEmpleados);
        panelStats.add(lblClientes);
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelBusqueda, BorderLayout.CENTER);
        panel.add(panelStats, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);         
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Lista de Usuarios",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
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

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaUsuarios.setRowHeight(28);
        tablaUsuarios.setOpaque(false); 
        tablaUsuarios.setBackground(new Color(255, 255, 255, 200));
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaUsuarios.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaUsuarios.getTableHeader().setOpaque(true);
        tablaUsuarios.setShowGrid(true);
        tablaUsuarios.setGridColor(new Color(230, 230, 230));

        // Deshabilitar movimiento de columnas
        tablaUsuarios.getTableHeader().setReorderingAllowed(false);

        // Fijar ancho de columnas
        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaUsuarios.getColumnModel().getColumn(0).setMaxWidth(50);
        tablaUsuarios.getColumnModel().getColumn(0).setMinWidth(50);

        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaUsuarios.getColumnModel().getColumn(1).setMinWidth(150);

        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaUsuarios.getColumnModel().getColumn(2).setMinWidth(100);

        tablaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaUsuarios.getColumnModel().getColumn(3).setMinWidth(80);

        // Selección de tabla
        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean seleccionado = tablaUsuarios.getSelectedRow() != -1;
                btnEditar.setEnabled(seleccionado);
                btnEliminar.setEnabled(seleccionado);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false); 
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(255, 255, 255, 0));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Panel de navegación (izquierda)
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNavegacion.setOpaque(false);
        
        btnAnterior = crearBoton("◄ Anterior", new Color(240, 240, 240), Color.BLACK);
        btnAnterior.addActionListener(e -> paginaAnterior());
        
        lblPagina = new JLabel("Página 1");
        lblPagina.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPagina.setBorder(new EmptyBorder(0, 10, 0, 10));
        
        btnSiguiente = crearBoton("Siguiente ►", new Color(240, 240, 240), Color.BLACK);
        btnSiguiente.addActionListener(e -> paginaSiguiente());
        
        panelNavegacion.add(btnAnterior);
        panelNavegacion.add(lblPagina);
        panelNavegacion.add(btnSiguiente);
        
        // Panel de botones (derecha) - BOTONES MÁS LARGOS
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);
        
        btnNuevoCliente = crearBotonLargo(" Nuevo Cliente", new Color(40, 40, 40), Color.WHITE);
        btnNuevoCliente.addActionListener(e -> nuevoCliente());
        
        btnNuevoEmpleado = crearBotonLargo(" Nuevo Empleado", new Color(168, 27, 29), Color.WHITE);
        btnNuevoEmpleado.addActionListener(e -> nuevoEmpleado());
        
        btnEditar = crearBotonLargo(" Editar", new Color(52, 152, 219), Color.WHITE);
        btnEditar.addActionListener(e -> editarUsuario());
        
        btnEliminar = crearBotonLargo("🗑️ Eliminar", new Color(231, 76, 60), Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarUsuario());
        
        panelBotones.add(btnNuevoCliente);
        panelBotones.add(btnNuevoEmpleado);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        
        // Inicialmente deshabilitar
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        
        panel.add(panelNavegacion, BorderLayout.WEST);
        panel.add(panelBotones, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        boton.setBackground(bg);
        boton.setForeground(fg);
        boton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
    
    private JButton crearBotonLargo(String texto, Color bg, Color fg) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        boton.setBackground(bg);
        boton.setForeground(fg);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));  // Más padding horizontal y vertical
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Tamaño preferido más grande
        boton.setPreferredSize(new Dimension(140, 35));
        boton.setMinimumSize(new Dimension(140, 35));
        
        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(boton.getBackground().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(bg);
            }
        });
        
        return boton;
    }
    
    private void cargarDatos() {
        actualizarEstadisticas();
        filtrar();
    }
    
    private void filtrar() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        
        usuariosMostrados = new java.util.ArrayList<>();
        for (Usuario u : gestorUsuarios.getListaUsuarios()) {
            if (busqueda.isEmpty() ||
                String.valueOf(u.getId()).contains(busqueda) ||
                u.getUsuario().toLowerCase().contains(busqueda) ||
                u.getNombre().toLowerCase().contains(busqueda)) {
                usuariosMostrados.add(u);
            }
        }
        
        paginaActual = 1;
        actualizarTabla();
    }
    
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        
        int inicio = (paginaActual - 1) * filasPorPagina;
        int fin = Math.min(inicio + filasPorPagina, usuariosMostrados.size());
        
        for (int i = inicio; i < fin; i++) {
            Usuario u = usuariosMostrados.get(i);
            modeloTabla.addRow(new Object[]{
                u.getId(),
                u.getNombre(),
                u.getUsuario(),
                u.getRol().name()
            });
        }
        
        int totalPaginas = (int) Math.ceil((double) usuariosMostrados.size() / filasPorPagina);
        lblPagina.setText("Página " + paginaActual + " de " + Math.max(1, totalPaginas));
        
        btnAnterior.setEnabled(paginaActual > 1);
        btnSiguiente.setEnabled(paginaActual < totalPaginas);
    }
    
    private void actualizarEstadisticas() {
        int total = gestorUsuarios.getListaUsuarios().size();
        long empleados = gestorUsuarios.getListaUsuarios().stream()
                .filter(u -> u.getRol() != Rol.CLIENTE).count();
        long clientes = total - empleados;
        
        lblTotal.setText("Total: " + total);
        lblEmpleados.setText("| Empleados: " + empleados);
        lblClientes.setText("| Clientes: " + clientes);
    }
    
    private void paginaAnterior() {
        if (paginaActual > 1) {
            paginaActual--;
            actualizarTabla();
        }
    }
    
    private void paginaSiguiente() {
        int totalPaginas = (int) Math.ceil((double) usuariosMostrados.size() / filasPorPagina);
        if (paginaActual < totalPaginas) {
            paginaActual++;
            actualizarTabla();
        }
    }
    
    private void nuevoCliente() {
        nuevoUsuario(Rol.CLIENTE, "Cliente");
    }
    
    private void nuevoEmpleado() {
        Object[] opciones = {"GERENTE", "CAJERO", "COCINA"};
        int seleccion = JOptionPane.showOptionDialog(this,
            "Seleccione el rol del empleado:",
            "Nuevo Empleado",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        if (seleccion >= 0) {
            Rol rol = switch (seleccion) {
                case 0 -> Rol.GERENTE;
                case 1 -> Rol.CAJERO;
                default -> Rol.COCINA;
            };
            nuevoUsuario(rol, "Empleado");
        }
    }
    
    private void nuevoUsuario(Rol rol, String tipo) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField txtNombre = new JTextField(15);
        JTextField txtUsuario = new JTextField(15);
        JTextField txtPassword = new JTextField(15);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
                "Nuevo " + tipo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String password = txtPassword.getText().trim();
            
            if (nombre.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (gestorUsuarios.existeUsuario(usuario)) {
                JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (gestorUsuarios.agregar(nombre, usuario, password, rol)) {
                gestorUsuarios.guardarEnArchivo();
                JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            }
        }
    }
    
    private void editarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) return;
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Usuario u = gestorUsuarios.buscarPorId(id);
        if (u == null) return;
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField txtNombre = new JTextField(u.getNombre(), 15);
        JTextField txtUsuario = new JTextField(u.getUsuario(), 15);
        JTextField txtPassword = new JTextField(15);
        txtPassword.setToolTipText("Dejar vacío para no cambiar");
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Nueva contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Editar Usuario",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String password = txtPassword.getText().trim();
            password = password.isEmpty() ? null : password;
            
            if (nombre.isEmpty() || usuario.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y usuario son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Usuario existente = gestorUsuarios.buscarPorNombreUsuario(usuario);
            if (existente != null && existente.getId() != id) {
                JOptionPane.showMessageDialog(this, "El nombre de usuario ya está en uso.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (gestorUsuarios.modificar(id, nombre, usuario, password, null)) {
                gestorUsuarios.guardarEnArchivo();
                JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            }
        }
    }
    
    private void eliminarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) return;
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar al usuario \"" + nombre + "\"?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (gestorUsuarios.eliminar(id)) {
                gestorUsuarios.guardarEnArchivo();
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            }
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

        GestionUsuariosGUI = new javax.swing.JPanel();
        PanelSuperior = new javax.swing.JPanel();
        lblTitulo = new java.awt.Label();
        txtBuscar = new javax.swing.JTextField();
        panelEstadisticas = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();
        lblEmpleados = new javax.swing.JLabel();
        lblClientes = new javax.swing.JLabel();
        PanelCentral1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        PanelInferior = new javax.swing.JPanel();
        panelNavegacion = new javax.swing.JPanel();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        lblPagina = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnNuevoCliente = new javax.swing.JButton();
        btnNuevoEmpleado = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        GestionUsuariosGUI.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        GestionUsuariosGUI.setName("GestionUsuariosGUI"); // NOI18N
        GestionUsuariosGUI.setPreferredSize(new java.awt.Dimension(1298, 690));

        lblTitulo.setName("GESTIÓN DE USUARIOS"); // NOI18N
        lblTitulo.setText("GESTIÓN DE USUARIOS");

        txtBuscar.setText("jTextField1");

        panelEstadisticas.setBackground(new java.awt.Color(204, 255, 51));

        lblTotal.setText("jLabel1");

        lblEmpleados.setText("jLabel1");

        lblClientes.setText("jLabel1");

        javax.swing.GroupLayout panelEstadisticasLayout = new javax.swing.GroupLayout(panelEstadisticas);
        panelEstadisticas.setLayout(panelEstadisticasLayout);
        panelEstadisticasLayout.setHorizontalGroup(
            panelEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEstadisticasLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(lblTotal)
                .addGap(75, 75, 75)
                .addComponent(lblEmpleados)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(lblClientes)
                .addGap(55, 55, 55))
        );
        panelEstadisticasLayout.setVerticalGroup(
            panelEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEstadisticasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal)
                    .addComponent(lblEmpleados)
                    .addComponent(lblClientes))
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout PanelSuperiorLayout = new javax.swing.GroupLayout(PanelSuperior);
        PanelSuperior.setLayout(PanelSuperiorLayout);
        PanelSuperiorLayout.setHorizontalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addGroup(PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelSuperiorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelEstadisticas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelSuperiorLayout.setVerticalGroup(
            PanelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
            .addComponent(panelEstadisticas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout PanelCentral1Layout = new javax.swing.GroupLayout(PanelCentral1);
        PanelCentral1.setLayout(PanelCentral1Layout);
        PanelCentral1Layout.setHorizontalGroup(
            PanelCentral1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelCentral1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2))
        );
        PanelCentral1Layout.setVerticalGroup(
            PanelCentral1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCentral1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNavegacion.setBackground(new java.awt.Color(255, 0, 0));

        btnAnterior.setText("jButton1");

        btnSiguiente.setText("jButton2");

        lblPagina.setText("jLabel1");

        javax.swing.GroupLayout panelNavegacionLayout = new javax.swing.GroupLayout(panelNavegacion);
        panelNavegacion.setLayout(panelNavegacionLayout);
        panelNavegacionLayout.setHorizontalGroup(
            panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavegacionLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(lblPagina, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        panelNavegacionLayout.setVerticalGroup(
            panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNavegacionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPagina))
                .addGap(14, 14, 14))
        );

        btnNuevoCliente.setText("jButton1");

        btnNuevoEmpleado.setText("jButton2");

        btnEditar.setText("jButton1");

        btnEliminar.setText("jButton1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addComponent(btnNuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(btnNuevoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelInferiorLayout = new javax.swing.GroupLayout(PanelInferior);
        PanelInferior.setLayout(PanelInferiorLayout);
        PanelInferiorLayout.setHorizontalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInferiorLayout.createSequentialGroup()
                .addComponent(panelNavegacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelInferiorLayout.setVerticalGroup(
            PanelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelNavegacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout GestionUsuariosGUILayout = new javax.swing.GroupLayout(GestionUsuariosGUI);
        GestionUsuariosGUI.setLayout(GestionUsuariosGUILayout);
        GestionUsuariosGUILayout.setHorizontalGroup(
            GestionUsuariosGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GestionUsuariosGUILayout.createSequentialGroup()
                .addGroup(GestionUsuariosGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PanelSuperior, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1298, Short.MAX_VALUE)
                    .addComponent(PanelCentral1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1298, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, GestionUsuariosGUILayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(PanelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        GestionUsuariosGUILayout.setVerticalGroup(
            GestionUsuariosGUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GestionUsuariosGUILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelCentral1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1298, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(GestionUsuariosGUI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(GestionUsuariosGUI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel GestionUsuariosGUI;
    private javax.swing.JPanel PanelCentral;
    private javax.swing.JPanel PanelCentral1;
    private javax.swing.JPanel PanelInferior;
    private javax.swing.JPanel PanelSuperior;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnNuevoCliente;
    private javax.swing.JButton btnNuevoEmpleado;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblClientes;
    private javax.swing.JLabel lblEmpleados;
    private javax.swing.JLabel lblPagina;
    private java.awt.Label lblTitulo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel panelEstadisticas;
    private javax.swing.JPanel panelNavegacion;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
