package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class InterfazCliente extends JFrame {
    
    private String nombreUsuario;
    private String rolUsuario;
    private JButton btnActivo = null;
    private Image logoImagen;
    private Image fondoImagen;
    
    // Componentes
    private JPanel Encabezado;
    private JPanel BarraNav;
    private JPanel Interfaz;
    private JPanel PiePag;
    private JButton btnVerMenu;
    private JButton btnCerrarSesion;
    
    // Componentes de la cabecera
    private JLabel jLabel1;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel Rol;
    
    public InterfazCliente(String rol, String nombre) {
        this.rolUsuario = rol;
        this.nombreUsuario = nombre;

        setTitle("La Casa del Sabor - Cliente");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cargarImagenes();
        initUI();

        aplicarColoresPersonalizados();

        javax.swing.SwingUtilities.invokeLater(() -> {
            aplicarColoresPersonalizados();
            repaint();
        });
    }
    
    private void cargarImagenes() {
        try {
            String rutaLogoPng = "resources/imagenes/LogoPizzeria.png";
            String rutaLogoJpg = "resources/imagenes/LogoPizzeria.jpg";
            File archivoLogoPng = new File(rutaLogoPng);
            File archivoLogoJpg = new File(rutaLogoJpg);

            if (archivoLogoPng.exists()) {
                ImageIcon logoIcon = new ImageIcon(archivoLogoPng.getAbsolutePath());
                logoImagen = logoIcon.getImage();
                System.out.println("Logo cargado desde: " + archivoLogoPng.getAbsolutePath());
            } else if (archivoLogoJpg.exists()) {
                ImageIcon logoIcon = new ImageIcon(archivoLogoJpg.getAbsolutePath());
                logoImagen = logoIcon.getImage();
                System.out.println("Logo cargado desde: " + archivoLogoJpg.getAbsolutePath());
            } else {
                logoImagen = null;
            }
        } catch (Exception e) {
            System.out.println("Error cargando logo: " + e.getMessage());
            logoImagen = null;
        }
        
        try {
            String rutaFondo = "resources/imagenes/FondoBlanco.png";
            File archivoFondo = new File(rutaFondo);
            if (archivoFondo.exists()) {
                ImageIcon fondoIcon = new ImageIcon(archivoFondo.getAbsolutePath());
                fondoImagen = fondoIcon.getImage();
                System.out.println("Fondo cargado desde: " + archivoFondo.getAbsolutePath());
            } else {
                fondoImagen = null;
            }
        } catch (Exception e) {
            System.out.println("Error cargando fondo: " + e.getMessage());
            fondoImagen = null;
        }
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        crearEncabezado();
        crearBarraNav();
        crearPanelCentral();
        crearPiePagina();
        
        add(Encabezado, BorderLayout.NORTH);
        add(BarraNav, BorderLayout.WEST);
        add(Interfaz, BorderLayout.CENTER);
        add(PiePag, BorderLayout.SOUTH);
        
        configurarHover();
        activarBoton(btnVerMenu);
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            aplicarColoresPersonalizados();
        });
    }
    
    private void crearEncabezado() {
        Encabezado = new JPanel(new BorderLayout());
        Encabezado.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 0, 0)));
        Encabezado.setPreferredSize(new Dimension(1280, 100));
        
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.X_AXIS));
        panelIzquierdo.setBorder(new EmptyBorder(10, 20, 10, 10));
        
        jLabel3 = new JLabel();
        if (logoImagen != null) {
            Image logoEscalado = logoImagen.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            jLabel3.setIcon(new ImageIcon(logoEscalado));
        }
        jLabel3.setText("");
        jLabel3.setPreferredSize(new Dimension(70, 70));
        panelIzquierdo.add(jLabel3);
        panelIzquierdo.add(Box.createRigidArea(new Dimension(15, 0)));
        
        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));
        
        jLabel1 = new JLabel("LA CASA DEL SABOR");
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        jLabel4 = new JLabel("PIZZERÍA");
        jLabel4.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        jLabel4.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelTitulos.add(jLabel1);
        panelTitulos.add(Box.createRigidArea(new Dimension(0, 5)));
        panelTitulos.add(jLabel4);
        
        JPanel panelDerecho = new JPanel();
        panelDerecho.setOpaque(false);
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(new EmptyBorder(10, 10, 10, 20));
        panelDerecho.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        Rol = new JLabel();
        Rol.setFont(new Font("Segoe UI", Font.BOLD, 13));
        Rol.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        panelDerecho.add(Box.createVerticalGlue());
        panelDerecho.add(Rol);
        panelDerecho.add(Box.createVerticalGlue());
        
        Encabezado.add(panelIzquierdo, BorderLayout.WEST);
        Encabezado.add(panelTitulos, BorderLayout.CENTER);
        Encabezado.add(panelDerecho, BorderLayout.EAST);
    }
    
    private void crearBarraNav() {
        BarraNav = new JPanel();
        BarraNav.setPreferredSize(new Dimension(280, 570));
        BarraNav.setLayout(new BoxLayout(BarraNav, BoxLayout.Y_AXIS));

        // Solo Ver Menú
        btnVerMenu = new JButton("Ver Menú");
        btnVerMenu.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnVerMenu.setHorizontalAlignment(SwingConstants.LEFT);
        btnVerMenu.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 15));
        btnVerMenu.setFocusPainted(false);
        btnVerMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerMenu.setMaximumSize(new Dimension(280, 50));
        btnVerMenu.setMinimumSize(new Dimension(280, 50));
        btnVerMenu.setPreferredSize(new Dimension(280, 50));
        btnVerMenu.addActionListener(e -> mostrarMenuCompleto());
        BarraNav.add(btnVerMenu);

        BarraNav.add(Box.createVerticalGlue());

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnCerrarSesion.setHorizontalAlignment(SwingConstants.LEFT);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 15));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setMaximumSize(new Dimension(280, 50));
        btnCerrarSesion.setMinimumSize(new Dimension(280, 50));
        btnCerrarSesion.setPreferredSize(new Dimension(280, 50));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        BarraNav.add(btnCerrarSesion);

        BarraNav.revalidate();
        BarraNav.repaint();
    }
    
    private void crearPanelCentral() {
        Interfaz = new JPanel(new BorderLayout());
        
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
        
        Interfaz.add(panelFondo, BorderLayout.CENTER);
    }
    
    private void crearPiePagina() {
        PiePag = new JPanel(new BorderLayout());
        PiePag.setBackground(Color.WHITE);
        PiePag.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(217, 217, 217)));
        PiePag.setPreferredSize(new Dimension(1280, 50));
        
        JLabel lblEquipo = new JLabel("Equipo StarTech - Desarrollo de Software");
        lblEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEquipo.setForeground(new Color(100, 100, 100));
        lblEquipo.setBorder(new EmptyBorder(0, 20, 0, 0));
        
        JLabel lblVersion = new JLabel("Versión: 1.0.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblVersion.setForeground(new Color(100, 100, 100));
        lblVersion.setBorder(new EmptyBorder(0, 0, 0, 20));
        lblVersion.setHorizontalAlignment(SwingConstants.RIGHT);
        
        PiePag.add(lblEquipo, BorderLayout.WEST);
        PiePag.add(lblVersion, BorderLayout.EAST);
    }
    
    private void aplicarColoresPersonalizados() {
        System.out.println("Aplicando colores personalizados a InterfazCliente...");
        
        Encabezado.setBackground(new Color(125, 0, 2));
        Encabezado.setOpaque(true);
        
        if (jLabel1 != null) {
            jLabel1.setForeground(Color.WHITE);
        }
        if (jLabel4 != null) {
            jLabel4.setForeground(new Color(255, 200, 200));
        }
        
        if (Rol != null) {
            Rol.setForeground(new Color(255, 200, 200));
            Rol.setText("ROL: " + (rolUsuario != null ? rolUsuario.toUpperCase() : "CLIENTE") + " - " + 
                       (nombreUsuario != null ? nombreUsuario : "Cliente"));
        }
        
        Component[] components = Encabezado.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                panel.setBackground(Color.WHITE);
                panel.setOpaque(true);
                break;
            }
        }
        
        BarraNav.setBackground(Color.BLACK);
        BarraNav.setOpaque(true);
        
        JButton[] botones = {btnVerMenu, btnCerrarSesion};
        for (JButton boton : botones) {
            if (boton != null) {
                boton.setBackground(Color.WHITE);
                if (boton == btnCerrarSesion) {
                    boton.setForeground(new Color(168, 27, 29));
                } else {
                    boton.setForeground(Color.BLACK);
                }
                boton.setOpaque(true);
                boton.setContentAreaFilled(true);
                boton.setBorderPainted(false);
            }
        }
        
        Encabezado.revalidate();
        Encabezado.repaint();
        BarraNav.revalidate();
        BarraNav.repaint();
    }
    
    private void configurarHover() {
        JButton[] botones = {btnVerMenu, btnCerrarSesion};
        
        for (JButton b : botones) {
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (b != btnActivo) {
                        if (b == btnCerrarSesion) {
                            b.setBackground(new Color(245, 245, 245));
                            b.setForeground(new Color(200, 0, 0));
                        } else {
                            b.setBackground(new Color(200, 200, 200));
                            b.setForeground(new Color(168, 27, 29));
                        }
                    }
                }
                
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (b != btnActivo) {
                        if (b == btnCerrarSesion) {
                            b.setBackground(Color.WHITE);
                            b.setForeground(new Color(168, 27, 29));
                        } else {
                            b.setBackground(Color.WHITE);
                            b.setForeground(Color.BLACK);
                        }
                    }
                }
            });
        }
    }
    
    private void activarBoton(JButton boton) {
        if (btnActivo != null) {
            btnActivo.setBackground(Color.WHITE);
            btnActivo.setForeground(Color.BLACK);
            btnActivo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        }
        
        boton.setBackground(new Color(168, 27, 29));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnActivo = boton;
    }
    
    private void cargarPanel(JPanel panel) {
        JPanel panelFondo = (JPanel) Interfaz.getComponent(0);
        panelFondo.removeAll();
        panelFondo.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panelFondo.add(panel, BorderLayout.CENTER);
        panelFondo.revalidate();
        panelFondo.repaint();
    }
    
    private void mostrarMenuCompleto() {
        activarBoton(btnVerMenu);
        FrameVerMenu ventana = new FrameVerMenu(rolUsuario, nombreUsuario);
        ventana.setVisible(true);
        this.dispose();
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new PantallaInicial().setVisible(true);
        }
    }
    
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
        
        java.awt.EventQueue.invokeLater(() -> new InterfazCliente("CLIENTE", "Cliente Demo").setVisible(true));
    }
}