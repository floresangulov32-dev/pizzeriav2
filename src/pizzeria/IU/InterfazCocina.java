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

public class InterfazCocina extends JFrame {
    
    private String nombreUsuario;
    private String rolUsuario;
    private JButton btnActivo = null;
    private Image logoImagen;
    private Image fondoImagen;
        
    
    public InterfazCocina(String rol, String nombre) {
        this.rolUsuario = rol;
        this.nombreUsuario = nombre;

        setTitle("La Casa del Sabor - Cocina");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cargarImagenes();
        initUI();

        // Aplicar colores inmediatamente
        aplicarColoresPersonalizados();

        // Forzar actualización después de que la ventana sea visible
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                aplicarColoresPersonalizados();
            }
        });

        // También forzar después de un pequeño delay
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
        activarBoton(btnPedidosPendientes);
    }
    
    
    /**
     * Aplica los colores personalizados a la interfaz
     */
    private void aplicarColoresPersonalizados() {
        System.out.println("Aplicando colores personalizados...");

        // CABECERA - Fondo rojo vino
        Encabezado.setBackground(new Color(125, 0, 2));
        Encabezado.setOpaque(true);

        // Panel izquierdo del logo - Fondo blanco (para que se vea bien el logo)
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setOpaque(true);

        // Títulos - Texto blanco
        lblTitulo.setForeground(Color.WHITE);
        lblSubtitulo.setForeground(new Color(255, 200, 200));

        // Rol y nombre - Texto blanco
        lblRol.setForeground(new Color(255, 200, 200));
        lblNombre.setForeground(Color.WHITE);
        lblRol.setText("ROL: " + (rolUsuario != null ? rolUsuario.toUpperCase() : "COCINA"));
        lblNombre.setText(nombreUsuario != null ? nombreUsuario : "Cocina");

        // BARRA DE NAVEGACIÓN - Fondo negro
        BarraNav.setBackground(Color.BLACK);
        BarraNav.setOpaque(true);

        // BOTONES - Todos blancos con texto negro
        if (btnPedidosPendientes != null) {
            btnPedidosPendientes.setBackground(Color.WHITE);
            btnPedidosPendientes.setForeground(Color.BLACK);
            btnPedidosPendientes.setOpaque(true);
            btnPedidosPendientes.setContentAreaFilled(true);
            btnPedidosPendientes.setBorderPainted(false);
        }

        if (btnTomarPedido != null) {
            btnTomarPedido.setBackground(Color.WHITE);
            btnTomarPedido.setForeground(Color.BLACK);
            btnTomarPedido.setOpaque(true);
            btnTomarPedido.setContentAreaFilled(true);
            btnTomarPedido.setBorderPainted(false);
        }

        if (btnEnPreparacion != null) {
            btnEnPreparacion.setBackground(Color.WHITE);
            btnEnPreparacion.setForeground(Color.BLACK);
            btnEnPreparacion.setOpaque(true);
            btnEnPreparacion.setContentAreaFilled(true);
            btnEnPreparacion.setBorderPainted(false);
        }

        if (btnMarcarListo != null) {
            btnMarcarListo.setBackground(Color.WHITE);
            btnMarcarListo.setForeground(Color.BLACK);
            btnMarcarListo.setOpaque(true);
            btnMarcarListo.setContentAreaFilled(true);
            btnMarcarListo.setBorderPainted(false);
        }

        if (btnPedidosListos != null) {
            btnPedidosListos.setBackground(Color.WHITE);
            btnPedidosListos.setForeground(Color.BLACK);
            btnPedidosListos.setOpaque(true);
            btnPedidosListos.setContentAreaFilled(true);
            btnPedidosListos.setBorderPainted(false);
        }

        if (btnMarcarEntregado != null) {
            btnMarcarEntregado.setBackground(Color.WHITE);
            btnMarcarEntregado.setForeground(Color.BLACK);
            btnMarcarEntregado.setOpaque(true);
            btnMarcarEntregado.setContentAreaFilled(true);
            btnMarcarEntregado.setBorderPainted(false);
        }

        // Botón Cerrar Sesión - Blanco con texto rojo
        if (btnCerrarSesion != null) {
            btnCerrarSesion.setBackground(Color.WHITE);
            btnCerrarSesion.setForeground(new Color(168, 27, 29)); // Rojo
            btnCerrarSesion.setOpaque(true);
            btnCerrarSesion.setContentAreaFilled(true);
            btnCerrarSesion.setBorderPainted(false);
        }

        // Forzar actualización visual
        Encabezado.revalidate();
        Encabezado.repaint();
        BarraNav.revalidate();
        BarraNav.repaint();

        System.out.println("Colores aplicados - Encabezado: " + Encabezado.getBackground());
        System.out.println("Colores aplicados - BarraNav: " + BarraNav.getBackground());
        System.out.println("Colores aplicados - jPanel1: " + panelIzquierdo.getBackground());
    }

    private void crearEncabezado() {
        Encabezado = new JPanel(new BorderLayout());
        Encabezado.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 0, 0)));
        Encabezado.setPreferredSize(new Dimension(1280, 100));

        // Panel izquierdo - Logo (con fondo blanco)
        panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(Color.WHITE); // Fondo blanco para el logo
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.X_AXIS));
        panelIzquierdo.setBorder(new EmptyBorder(10, 20, 10, 10));

        if (logoImagen != null) {
            Image logoEscalado = logoImagen.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoEscalado));
            panelIzquierdo.add(lblLogo);
            panelIzquierdo.add(Box.createRigidArea(new Dimension(15, 0)));
        }        
        
        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        lblTitulo = new JLabel("LA CASA DEL SABOR");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        // NO poner color aquí
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblSubtitulo = new JLabel("PIZZERÍA");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        // NO poner color aquí
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelTitulos.add(lblTitulo);
        panelTitulos.add(Box.createRigidArea(new Dimension(0, 5)));
        panelTitulos.add(lblSubtitulo);

        // Panel derecho - Rol de usuario
        JPanel panelDerecho = new JPanel();
        panelDerecho.setOpaque(false);
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(new EmptyBorder(10, 10, 10, 20));
        panelDerecho.setAlignmentX(Component.RIGHT_ALIGNMENT);

        lblRol = new JLabel("ROL: " + (rolUsuario != null ? rolUsuario.toUpperCase() : "COCINA"));
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 13));
        // NO poner color aquí
        lblRol.setAlignmentX(Component.RIGHT_ALIGNMENT);

        lblNombre = new JLabel(nombreUsuario != null ? nombreUsuario : "Cocina");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
        // NO poner color aquí
        lblNombre.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panelDerecho.add(Box.createVerticalGlue());
        panelDerecho.add(lblRol);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 5)));
        panelDerecho.add(lblNombre);
        panelDerecho.add(Box.createVerticalGlue());

        Encabezado.add(panelIzquierdo, BorderLayout.WEST);
        Encabezado.add(panelTitulos, BorderLayout.CENTER);
        Encabezado.add(panelDerecho, BorderLayout.EAST);
    }
    
    private void crearBarraNav() {
        BarraNav = new JPanel();
        // NO poner color aquí
        BarraNav.setPreferredSize(new Dimension(280, 570));
        BarraNav.setLayout(new BoxLayout(BarraNav, BoxLayout.Y_AXIS));

        btnPedidosPendientes = new JButton("Pedidos Pendientes");
        btnPedidosPendientes.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        // NO poner colores aquí
        btnPedidosPendientes.setHorizontalAlignment(SwingConstants.LEFT);
        btnPedidosPendientes.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 15));
        btnPedidosPendientes.setFocusPainted(false);
        btnPedidosPendientes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPedidosPendientes.setMaximumSize(new Dimension(280, 50));
        btnPedidosPendientes.setMinimumSize(new Dimension(280, 50));
        btnPedidosPendientes.setPreferredSize(new Dimension(280, 50));
        btnPedidosPendientes.addActionListener(e -> mostrarPedidosPendientes());
        BarraNav.add(btnPedidosPendientes);

      

        btnEnPreparacion = new JButton("En Preparación");
        btnEnPreparacion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnEnPreparacion.setHorizontalAlignment(SwingConstants.LEFT);
        btnEnPreparacion.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 15));
        btnEnPreparacion.setFocusPainted(false);
        btnEnPreparacion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnPreparacion.setMaximumSize(new Dimension(280, 50));
        btnEnPreparacion.setMinimumSize(new Dimension(280, 50));
        btnEnPreparacion.setPreferredSize(new Dimension(280, 50));
        btnEnPreparacion.addActionListener(e -> mostrarEnPreparacion());
        BarraNav.add(btnEnPreparacion);

       
        btnPedidosListos = new JButton("Pedidos Listos");
        btnPedidosListos.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnPedidosListos.setHorizontalAlignment(SwingConstants.LEFT);
        btnPedidosListos.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 15));
        btnPedidosListos.setFocusPainted(false);
        btnPedidosListos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPedidosListos.setMaximumSize(new Dimension(280, 50));
        btnPedidosListos.setMinimumSize(new Dimension(280, 50));
        btnPedidosListos.setPreferredSize(new Dimension(280, 50));
        btnPedidosListos.addActionListener(e -> mostrarPedidosListos());
        BarraNav.add(btnPedidosListos);

        
        // Espacio flexible
        BarraNav.add(Box.createVerticalGlue());

        // Botón Cerrar Sesión
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
    }
    
    private void crearPanelCentral() {
        Interfaz = new JPanel(new BorderLayout());
        Interfaz.setBackground(Color.WHITE);
        
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
    
    private void configurarHover() {
      JButton[] botones = {btnPedidosPendientes, btnEnPreparacion, 
                             btnPedidosListos, btnCerrarSesion};
        for (JButton b : botones) {
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (b != btnActivo) {
                        if (b == btnCerrarSesion) {
                            b.setBackground(new Color(245, 245, 245));
                            b.setForeground(new Color(200, 0, 0));
                        } else {
                            b.setBackground(new Color(200, 200, 200)); // Gris claro al hover
                            b.setForeground(new Color(168, 27, 29)); // Rojo característico
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

        if (boton == btnCerrarSesion) {
            boton.setBackground(new Color(168, 27, 29));
            boton.setForeground(Color.WHITE);
        } else {
            boton.setBackground(new Color(168, 27, 29)); // Rojo característico para activo
            boton.setForeground(Color.WHITE);
        }
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
    
    private void mostrarPedidosPendientes() {
        activarBoton(btnPedidosPendientes);
        PedidosPendientesGUI panel = new PedidosPendientesGUI();
        cargarPanel(panel);
    }
    
    
    private void mostrarEnPreparacion() {
        activarBoton(btnEnPreparacion);
        EnPreparacionGUI panel = new EnPreparacionGUI();
        cargarPanel(panel);
    }
    
    
    private void mostrarPedidosListos() {
        activarBoton(btnPedidosListos);
        PedidosListosGUI panel = new PedidosListosGUI();
        cargarPanel(panel);
    }
    
 
    private void cerrarSesion() {
       Object[] opciones = {"Sí", "No"};
int confirm = JOptionPane.showOptionDialog(this,
    "¿Está seguro de que desea cerrar sesión?",
    "Cerrar Sesión",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.QUESTION_MESSAGE,
    null,
    opciones,
    opciones[0]);
        
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
        
        java.awt.EventQueue.invokeLater(() -> new InterfazCocina("COCINA", "Cocinero Demo").setVisible(true));
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Encabezado = new javax.swing.JPanel();
        panelIzquierdo = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        panelTitulos = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        panelDerecho = new javax.swing.JPanel();
        lblRol = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        BarraNav = new javax.swing.JPanel();
        btnPedidosPendientes = new javax.swing.JButton();
        btnTomarPedido = new javax.swing.JButton();
        btnEnPreparacion = new javax.swing.JButton();
        btnMarcarListo = new javax.swing.JButton();
        btnPedidosListos = new javax.swing.JButton();
        btnMarcarEntregado = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();
        Interfaz = new javax.swing.JPanel();
        PiePag = new javax.swing.JPanel();
        lblEquipo = new javax.swing.JLabel();
        lblVersion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblLogo.setText("jLabel1");

        javax.swing.GroupLayout panelIzquierdoLayout = new javax.swing.GroupLayout(panelIzquierdo);
        panelIzquierdo.setLayout(panelIzquierdoLayout);
        panelIzquierdoLayout.setHorizontalGroup(
            panelIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIzquierdoLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblLogo)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelIzquierdoLayout.setVerticalGroup(
            panelIzquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIzquierdoLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblLogo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblTitulo.setText("jLabel1");

        lblSubtitulo.setText("jLabel1");

        javax.swing.GroupLayout panelTitulosLayout = new javax.swing.GroupLayout(panelTitulos);
        panelTitulos.setLayout(panelTitulosLayout);
        panelTitulosLayout.setHorizontalGroup(
            panelTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitulosLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(panelTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSubtitulo)
                    .addComponent(lblTitulo))
                .addContainerGap(604, Short.MAX_VALUE))
        );
        panelTitulosLayout.setVerticalGroup(
            panelTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitulosLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtitulo)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        lblRol.setText("jLabel1");

        lblNombre.setText("jLabel1");

        javax.swing.GroupLayout panelDerechoLayout = new javax.swing.GroupLayout(panelDerecho);
        panelDerecho.setLayout(panelDerechoLayout);
        panelDerechoLayout.setHorizontalGroup(
            panelDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDerechoLayout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(panelDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDerechoLayout.createSequentialGroup()
                        .addComponent(lblRol)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDerechoLayout.createSequentialGroup()
                        .addComponent(lblNombre)
                        .addGap(33, 33, 33))))
        );
        panelDerechoLayout.setVerticalGroup(
            panelDerechoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDerechoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRol)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNombre)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout EncabezadoLayout = new javax.swing.GroupLayout(Encabezado);
        Encabezado.setLayout(EncabezadoLayout);
        EncabezadoLayout.setHorizontalGroup(
            EncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelIzquierdo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTitulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelDerecho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        EncabezadoLayout.setVerticalGroup(
            EncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelIzquierdo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTitulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelDerecho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnPedidosPendientes.setText("jButton1");

        btnTomarPedido.setText("jButton1");

        btnEnPreparacion.setText("jButton1");

        btnMarcarListo.setText("jButton1");

        btnPedidosListos.setText("jButton1");

        btnMarcarEntregado.setText("jButton1");

        btnCerrarSesion.setText("jButton1");

        javax.swing.GroupLayout BarraNavLayout = new javax.swing.GroupLayout(BarraNav);
        BarraNav.setLayout(BarraNavLayout);
        BarraNavLayout.setHorizontalGroup(
            BarraNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BarraNavLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(BarraNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCerrarSesion)
                    .addComponent(btnMarcarEntregado)
                    .addComponent(btnPedidosListos)
                    .addComponent(btnMarcarListo)
                    .addComponent(btnEnPreparacion)
                    .addComponent(btnTomarPedido)
                    .addComponent(btnPedidosPendientes))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        BarraNavLayout.setVerticalGroup(
            BarraNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BarraNavLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnPedidosPendientes)
                .addGap(18, 18, 18)
                .addComponent(btnTomarPedido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEnPreparacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMarcarListo)
                .addGap(18, 18, 18)
                .addComponent(btnPedidosListos)
                .addGap(18, 18, 18)
                .addComponent(btnMarcarEntregado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(btnCerrarSesion)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout InterfazLayout = new javax.swing.GroupLayout(Interfaz);
        Interfaz.setLayout(InterfazLayout);
        InterfazLayout.setHorizontalGroup(
            InterfazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        InterfazLayout.setVerticalGroup(
            InterfazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lblEquipo.setText("jLabel1");

        lblVersion.setText("jLabel1");

        javax.swing.GroupLayout PiePagLayout = new javax.swing.GroupLayout(PiePag);
        PiePag.setLayout(PiePagLayout);
        PiePagLayout.setHorizontalGroup(
            PiePagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PiePagLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(PiePagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVersion)
                    .addComponent(lblEquipo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PiePagLayout.setVerticalGroup(
            PiePagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PiePagLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(lblEquipo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblVersion)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PiePag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BarraNav, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Interfaz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Encabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Encabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BarraNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Interfaz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PiePag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BarraNav;
    private javax.swing.JPanel Encabezado;
    private javax.swing.JPanel Interfaz;
    private javax.swing.JPanel PiePag;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnEnPreparacion;
    private javax.swing.JButton btnMarcarEntregado;
    private javax.swing.JButton btnMarcarListo;
    private javax.swing.JButton btnPedidosListos;
    private javax.swing.JButton btnPedidosPendientes;
    private javax.swing.JButton btnTomarPedido;
    private javax.swing.JLabel lblEquipo;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRol;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblVersion;
    private javax.swing.JPanel panelDerecho;
    private javax.swing.JPanel panelIzquierdo;
    private javax.swing.JPanel panelTitulos;
    // End of variables declaration//GEN-END:variables
}
