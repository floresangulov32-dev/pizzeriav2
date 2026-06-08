package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class PantallaMenuPublico {

    public static void mostrar(java.awt.Window ventanaPadre) {
        JDialog dialogMenu = new JDialog(
                ventanaPadre instanceof Frame ? (Frame) ventanaPadre : null,
                "Menú - La Casa del Sabor",
                true
        );

        dialogMenu.setSize(750, 600);
        dialogMenu.setLocationRelativeTo(ventanaPadre);
        dialogMenu.setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(28, 28, 28));
        panelPrincipal.setBorder(BorderFactory.createLineBorder(new Color(168, 27, 29), 2));

        JLabel lblTitulo = new JLabel("MENÚ LA CASA DEL SABOR", SwingConstants.CENTER);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(168, 27, 29));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JTextArea areaMenu = new JTextArea();
        areaMenu.setEditable(false);
        areaMenu.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaMenu.setBackground(new Color(18, 18, 18));
        areaMenu.setForeground(new Color(230, 230, 230));
        areaMenu.setCaretColor(Color.WHITE);
        areaMenu.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        try {
            pizzeria.model.Menu menu = ContextoVentasGUI.getInstancia().getMenu();

            if (menu != null && menu.getProductos() != null && !menu.getProductos().isEmpty()) {
                areaMenu.setText(menu.mostrarMenu());
            } else {
                areaMenu.setText("=== MENÚ DE LA CASA DEL SABOR ===\n\n"
                        + "No hay productos disponibles actualmente.");
            }

        } catch (Exception e) {
            areaMenu.setText("=== MENÚ DE LA CASA DEL SABOR ===\n\n"
                    + "No se pudo cargar el menú.\n"
                    + "Revise que existan los archivos:\n"
                    + "resources/data/productos.txt\n"
                    + "resources/data/combos.txt");
        }

        areaMenu.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(areaMenu);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        scrollPane.getViewport().setBackground(new Color(18, 18, 18));

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(28, 28, 28));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));

        JButton btnCerrar = new JButton("CERRAR");
        btnCerrar.setBackground(new Color(168, 27, 29));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 30, 8, 30));
        btnCerrar.addActionListener(e -> dialogMenu.dispose());

        panelBoton.add(btnCerrar);

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        dialogMenu.setContentPane(panelPrincipal);
        dialogMenu.setVisible(true);
    }
}