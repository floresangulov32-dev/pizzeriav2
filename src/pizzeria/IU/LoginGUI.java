/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import pizzeria.controller.GestorUsuarios;
import pizzeria.model.Usuario;
import pizzeria.IU.InterfazCliente;

/**
 *
 * @author MY HP
 */
public class LoginGUI extends javax.swing.JFrame {
    
    private GestorUsuarios gestorUsuarios;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;
    private JButton btnCancelar;
    private Image fondoImagen;
    
    // Contador de intentos fallidos
    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;
    
    public LoginGUI(){
        initComponents();
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cambiado para no cerrar toda la app
        gestorUsuarios = new GestorUsuarios();
        gestorUsuarios.cargarDesdeArchivo();
        cargarImagenFondo();
        configurarApariencia();
    }
    
    private void cargarImagenFondo() {
        try {
            String rutaFondo = "resources/imagenes/FondoBlanco.png";
            File archivoFondo = new File(rutaFondo);
            if (archivoFondo.exists()) {
                ImageIcon fondoIcon = new ImageIcon(archivoFondo.getAbsolutePath());
                fondoImagen = fondoIcon.getImage();
                System.out.println("Fondo cargado desde: " + archivoFondo.getAbsolutePath());
            } else {
                System.out.println("No se encontró el fondo en: " + rutaFondo);
                fondoImagen = null;
            }
        } catch (Exception e) {
            System.out.println("Error cargando fondo: " + e.getMessage());
            fondoImagen = null;
        }
    }
    
    private void configurarApariencia() {        
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fondoImagen != null) {
                    g.drawImage(fondoImagen, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panelFondo.setLayout(new BorderLayout());        
    
        JPanel panelFormulario = crearPanelLogin();
        panelFormulario.setOpaque(false);
        
        panelFondo.add(crearCabecera(), BorderLayout.NORTH);
        panelFondo.add(panelFormulario, BorderLayout.CENTER);
        panelFondo.add(crearPie(), BorderLayout.SOUTH);
        
        setContentPane(panelFondo);
    }
    
    private JPanel crearCabecera() {
        JPanel panelCabecera = new JPanel();
        panelCabecera.setOpaque(false);
        panelCabecera.setLayout(new BoxLayout(panelCabecera, BoxLayout.Y_AXIS));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
        panelCabecera.setAlignmentX(Component.CENTER_ALIGNMENT);
             
        JLabel lblTitulo = new JLabel("INICIAR SESIÓN", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
                
        JLabel lblSubtitulo = new JLabel("Ingrese sus credenciales para acceder al sistema", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(Color.DARK_GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCabecera.add(lblTitulo);
        panelCabecera.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCabecera.add(lblSubtitulo);
        
        return panelCabecera;
    }
    
    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Label USUARIO
        JLabel lblUsuario = new JLabel("USUARIO");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUsuario.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;         
        panel.add(lblUsuario, gbc);
        
        // Campo USUARIO - Blanco con borde negro
        txtUsuario = new JTextField(25);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setForeground(Color.BLACK);
        txtUsuario.setCaretColor(Color.BLACK);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(txtUsuario, gbc);
        
        // Label CONTRASEÑA
        JLabel lblPassword = new JLabel("CONTRASEÑA");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPassword.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblPassword, gbc);
        
        // Campo CONTRASEÑA - Blanco con borde negro
        txtPassword = new JPasswordField(25);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setCaretColor(Color.BLACK);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(txtPassword, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        
        // Botón INICIAR SESIÓN - Color FF1010 con letras blancas
        btnIniciarSesion = new JButton("INICIAR SESIÓN");
        btnIniciarSesion.setBackground(new Color(0xFF, 0x10, 0x10));
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnIniciarSesion.setPreferredSize(new Dimension(280, 65));
        btnIniciarSesion.setMinimumSize(new Dimension(280, 65));
        btnIniciarSesion.setMaximumSize(new Dimension(280, 65));
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIniciarSesion.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        btnIniciarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIniciarSesion.setBackground(new Color(0xFF, 0x40, 0x40));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIniciarSesion.setBackground(new Color(0xFF, 0x10, 0x10));
            }
        });
        btnIniciarSesion.addActionListener(e -> iniciarSesion());
        
        // Botón CANCELAR - Blanco con borde negro y letras rojas
        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(new Color(0xFF, 0x10, 0x10));
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnCancelar.setPreferredSize(new Dimension(280, 65));
        btnCancelar.setMinimumSize(new Dimension(280, 65));
        btnCancelar.setMaximumSize(new Dimension(280, 65));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(new Color(240, 240, 240));
                btnCancelar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(Color.WHITE);
                btnCancelar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        });
        btnCancelar.addActionListener(e -> volverPantallaInicial());
        
        panelBotones.add(btnIniciarSesion);
        panelBotones.add(Box.createRigidArea(new Dimension(25, 0)));
        panelBotones.add(btnCancelar);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelBotones, gbc);
        
        return panel;
    }
    
    private JPanel crearPie() {
        JPanel panelPie = new JPanel();
        panelPie.setOpaque(false);
        panelPie.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        
        JLabel lblInfo = new JLabel("La Casa del Sabor - Sistema de Gestión Integral");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelPie.add(lblInfo);
        return panelPie;
    }
    
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, complete todos los campos",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario user = gestorUsuarios.login(usuario, password);

        if (user != null) {
            // Login exitoso - resetear contador de intentos
            intentosFallidos = 0;
            
            JOptionPane.showMessageDialog(this,
                "Bienvenido/a " + user.getNombre() + "\nRol: " + user.getRol().name(),
                "Login Exitoso",
                JOptionPane.INFORMATION_MESSAGE);

            dispose();

            // Verificar el rol y abrir la interfaz correspondiente
            switch (user.getRol().name()) {
                case "GERENTE":
                    try {
                        InterfazGerenteP1 gerenteFrame = new InterfazGerenteP1(user.getRol().name(), user.getNombre());
                        gerenteFrame.setVisible(true);
                    } catch (Exception e) {
                        System.out.println("Error al abrir InterfazGerenteP1: " + e.getMessage());
                        mostrarMensajeEnDesarrollo("Módulo de Gerente");
                        new PantallaInicial().setVisible(true);
                    }
                    break;
                case "CLIENTE":
                    try {
                        System.out.println("=== DEBUG CLIENTE ===");
                        System.out.println("Rol recibido: " + user.getRol().name());
                        System.out.println("Nombre: " + user.getNombre());
                        System.out.println("Creando InterfazCliente...");

                        InterfazCliente clienteFrame = new InterfazCliente(user.getRol().name(), user.getNombre());

                        System.out.println("InterfazCliente creada exitosamente");
                        clienteFrame.setVisible(true);
                        System.out.println("InterfazCliente visible");

                    } catch (Exception e) {
                        System.out.println("ERROR DETALLADO: ");
                        e.printStackTrace();  // Esto imprimirá el error completo en consola
                        mostrarMensajeEnDesarrollo("Módulo de Cliente");
                        new PantallaInicial().setVisible(true);
                    }
                    break;
                case "CAJERO":
                    try {
                        VentasGUI ventasFrame = new VentasGUI(user.getRol().name(), user.getNombre());
                        ventasFrame.setVisible(true);
                    } catch (Exception e) {
                        System.out.println("Error al abrir VentasGUI: " + e.getMessage());
                        JOptionPane.showMessageDialog(this,
                            "No se pudo abrir el módulo de ventas.\n" + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        new PantallaInicial().setVisible(true);
                    }
                    break;
                case "COCINA":
                    try {
                        InterfazCocina cocinaFrame = new InterfazCocina(user.getRol().name(), user.getNombre());
                        cocinaFrame.setVisible(true);
                    } catch (Exception e) {
                        System.out.println("Error al abrir InterfazCocina: " + e.getMessage());
                        mostrarMensajeEnDesarrollo("Módulo de Cocina");
                        new PantallaInicial().setVisible(true);
                    }
                    break;
                default:
                    mostrarMensajeEnDesarrollo("Módulo para " + user.getRol().name());
                    new PantallaInicial().setVisible(true);
                    break;
            }
        } else {
            // Login fallido - incrementar contador
            intentosFallidos++;
            int intentosRestantes = MAX_INTENTOS - intentosFallidos;
            
            if (intentosFallidos >= MAX_INTENTOS) {
                // Superó los 3 intentos - cerrar la aplicación
                JOptionPane.showMessageDialog(this,
                    "Ha superado los " + MAX_INTENTOS + " intentos permitidos.\nEl programa se cerrará.",
                    "Demasiados intentos fallidos",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(0); // Cierra completamente la aplicación
            } else {
                // Mostrar mensaje con intentos restantes
                JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos\n" +
                    "Intentos restantes: " + intentosRestantes,
                    "Error de Autenticación",
                    JOptionPane.ERROR_MESSAGE);
                
                // Limpiar campos para nuevo intento
                txtPassword.setText("");
                txtUsuario.requestFocus();
            }
        }
    }

    private void mostrarMensajeEnDesarrollo(String modulo) {
        JOptionPane.showMessageDialog(null,
            modulo + " en desarrollo.\nSerá implementado próximamente.",
            "Información",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void abrirInterfazGerente(Usuario user) {
        try {
            InterfazGerenteP1 gerenteFrame = new InterfazGerenteP1(user.getRol().name(), user.getNombre());
            gerenteFrame.setVisible(true);
        } catch (Exception e) {
            System.out.println("Error al abrir InterfazGerenteP1: " + e.getMessage());
            mostrarMensajeEnDesarrollo("Módulo de Gerente");
            volverPantallaInicial();
        }
    }
    
    
    private void volverPantallaInicial() {
        this.dispose();
        new PantallaInicial().setVisible(true);
    }    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextField1.setText("jTextField1");

        jPasswordField1.setText("jPasswordField1");

        jButton1.setText("jButton1");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("jButton2");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 266, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(73, 73, 73))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(163, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed


    
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(() -> new LoginGUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
