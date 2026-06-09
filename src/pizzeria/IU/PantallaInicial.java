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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import pizzeria.model.Menu;
import pizzeria.model.Producto;
import pizzeria.util.ArchivoMenu;
import java.util.ArrayList;

public class PantallaInicial extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PantallaInicial.class.getName());
    
    private Menu menu;
    private ArchivoMenu archivoMenu;
    private Image fondoImagen;
    private Image logoImagen;
    
    public PantallaInicial() {
        initComponents();
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        cargarMenu();
        cargarImagenes();
        configurarApariencia();
    }
    
    private void cargarMenu() {
        archivoMenu = new ArchivoMenu();
        ArrayList<Producto> productos = archivoMenu.cargarProductos("resources/data/productos.txt");
        menu = new Menu(productos, archivoMenu.cargarCombos("resources/data/combos.txt", productos));
    }
    
    private void cargarImagenes() {
        // Cargar imagen de fondo
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
        
        // Cargar logo
        try {
            String rutaLogoPng = "resources/imagenes/LogoPizzeria.png";
            String rutaLogoJpg = "resources/imagenes/LogoPizzeria.jpg";
            File archivoLogoPng = new File(rutaLogoPng);
            File archivoLogoJpg = new File(rutaLogoJpg);

            if (archivoLogoPng.exists()) {
                ImageIcon logoIcon = new ImageIcon(archivoLogoPng.getAbsolutePath());
                logoImagen = logoIcon.getImage();
                System.out.println("Logo PNG cargado desde: " + archivoLogoPng.getAbsolutePath());
            } else if (archivoLogoJpg.exists()) {
                ImageIcon logoIcon = new ImageIcon(archivoLogoJpg.getAbsolutePath());
                logoImagen = logoIcon.getImage();
                System.out.println("Logo JPG cargado desde: " + archivoLogoJpg.getAbsolutePath());
            } else {
                System.out.println("No se encontró el logo en ninguna de las rutas esperadas.");
                logoImagen = null;
            }
        } catch (Exception e) {
            System.out.println("Error cargando logo: " + e.getMessage());
            logoImagen = null;
        }
    }
    
    private void configurarApariencia() {
        // Panel personalizado que dibuja la imagen de fondo
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
        
        // Hacer transparentes los paneles originales
        jPanel1.setOpaque(false);
        jPanel2.setOpaque(false);
        
        // Ajustar tamaños
        jPanel1.setPreferredSize(new Dimension(1280, 250));
        jPanel2.setPreferredSize(new Dimension(1280, 470));
        
        configurarCabecera();
        configurarBotones();
        
        panelFondo.add(jPanel1, BorderLayout.NORTH);
        panelFondo.add(jPanel2, BorderLayout.CENTER);
        
        setContentPane(panelFondo);
    }
    
    private void configurarCabecera() {
        jPanel1.removeAll();
        jPanel1.setLayout(new BorderLayout());
        jPanel1.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel panelCabecera = new JPanel();
        panelCabecera.setOpaque(false);
        panelCabecera.setLayout(new BoxLayout(panelCabecera, BoxLayout.Y_AXIS));
        panelCabecera.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (logoImagen != null) {
            // Logo de 300x170
            Image logoEscalado = logoImagen.getScaledInstance(300, 170, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCabecera.add(lblLogo);
        }

        // Títulos eliminados (solo se muestra el logo)
        jPanel1.add(panelCabecera, BorderLayout.CENTER);
    }
    
    private void configurarBotones() {
        jPanel2.removeAll();
        jPanel2.setLayout(new GridBagLayout());
        jPanel2.setBorder(null);
        jPanel2.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título MENÚ PRINCIPAL
        JLabel lblMenuPrincipal = new JLabel("MENÚ PRINCIPAL", SwingConstants.CENTER);
        lblMenuPrincipal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblMenuPrincipal.setForeground(Color.BLACK);
        lblMenuPrincipal.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Botones más grandes
        Font btnFont = new Font("Segoe UI", Font.BOLD, 20);
        Dimension btnSize = new Dimension(320, 70);

        // Botón Ver Menú - Color FF1010
        jButton1.setBackground(new Color(0xFF, 0x10, 0x10));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFont(btnFont);
        jButton1.setPreferredSize(btnSize);
        jButton1.setMinimumSize(btnSize);
        jButton1.setMaximumSize(btnSize);
        jButton1.setFocusPainted(false);
        jButton1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jButton1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton1.setText("VER MENÚ");

        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1.setBackground(new Color(0xFF, 0x40, 0x40));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1.setBackground(new Color(0xFF, 0x10, 0x10));
            }
        });

        // Botón Iniciar Sesión - Color FF1010
        jButton2.setBackground(new Color(0xFF, 0x10, 0x10));
        jButton2.setForeground(Color.WHITE);
        jButton2.setFont(btnFont);
        jButton2.setPreferredSize(btnSize);
        jButton2.setMinimumSize(btnSize);
        jButton2.setMaximumSize(btnSize);
        jButton2.setFocusPainted(false);
        jButton2.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jButton2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton2.setText("INICIAR SESIÓN");

        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2.setBackground(new Color(0xFF, 0x40, 0x40));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton2.setBackground(new Color(0xFF, 0x10, 0x10));
            }
        });

        // Botón Salir - Blanco con borde negro
        jButton3.setBackground(Color.WHITE);
        jButton3.setForeground(Color.BLACK);
        jButton3.setFont(btnFont);
        jButton3.setPreferredSize(btnSize);
        jButton3.setMinimumSize(btnSize);
        jButton3.setMaximumSize(btnSize);
        jButton3.setFocusPainted(false);
        jButton3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        jButton3.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton3.setText("SALIR");

        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3.setBackground(new Color(240, 240, 240));
                jButton3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton3.setBackground(Color.WHITE);
                jButton3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        });
        
        gbc.gridy = 0;
        jPanel2.add(lblMenuPrincipal, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 30, 10, 30);
        jPanel2.add(jButton1, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 30, 10, 30);
        jPanel2.add(jButton2, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 30, 20, 30);
        jPanel2.add(jButton3, gbc);
    }
    
    private void mostrarMenuPublico() {
        JDialog dialogMenu = new JDialog(this, "Menú - La Casa del Sabor", true);
        dialogMenu.setSize(550, 450);
        dialogMenu.setLocationRelativeTo(this);
                
        JPanel panelFondoDialogo = new JPanel(new BorderLayout());
        panelFondoDialogo.setBackground(new Color(30, 30, 30));
        
        JTextArea areaMenu = new JTextArea();
        areaMenu.setEditable(false);
        areaMenu.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaMenu.setBackground(new Color(20, 20, 20));
        areaMenu.setForeground(Color.LIGHT_GRAY);
        areaMenu.setCaretColor(Color.WHITE);
        
        if (menu != null && menu.getProductos() != null && !menu.getProductos().isEmpty()) {
            areaMenu.setText(menu.mostrarMenu());
        } else {
            areaMenu.setText("=== MENÚ DE LA CASA DEL SABOR ===\n\n" +
                           "No hay productos disponibles actualmente.\n\n" +
                           "Por favor, inicie sesión como gerente\n" +
                           "para agregar productos al menú.");
        }
        
        JScrollPane scrollPane = new JScrollPane(areaMenu);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(new Color(20, 20, 20));
        
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(30, 30, 30));
        JButton btnCerrar = new JButton("CERRAR");
        btnCerrar.setBackground(new Color(180, 0, 0));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dialogMenu.dispose());
        panelBoton.add(btnCerrar);
        
        panelFondoDialogo.add(scrollPane, BorderLayout.CENTER);
        panelFondoDialogo.add(panelBoton, BorderLayout.SOUTH);
        
        dialogMenu.setContentPane(panelFondoDialogo);
        dialogMenu.setVisible(true);
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("La Casa del Sabor - Sistema de Gestión");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(450, 100));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Sistema de Gestión Integral");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(263, 263, 263)
                .addComponent(jLabel1)
                .addContainerGap(299, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Menu Principal"));

        jButton1.setBackground(new java.awt.Color(60, 179, 113));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Ver Menu");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setBackground(new java.awt.Color(70, 130, 180));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Iniciar Sesion");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setBackground(new java.awt.Color(255, 51, 51));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Salir");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE))
                .addContainerGap(202, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(87, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 730, 380));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        FrameVerMenu ventana = new FrameVerMenu();
        ventana.setSize(this.getSize());
        ventana.setLocation(this.getLocation());
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        LoginGUI login = new LoginGUI();
        login.setVisible(true);
        this.dispose();        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea salir del sistema?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }//GEN-LAST:event_jButton3ActionPerformed
        
    /**
     * @param args the command line arguments
     */
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
        
        java.awt.EventQueue.invokeLater(() -> new PantallaInicial().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}
