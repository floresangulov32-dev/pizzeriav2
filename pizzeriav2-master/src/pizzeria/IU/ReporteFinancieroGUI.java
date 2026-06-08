package pizzeria.IU;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pizzeria.controller.GestorFinanzas;

public class ReporteFinancieroGUI extends JPanel {
    
    private GestorFinanzas gestorFinanzas;
    private JLabel lblSaldo, lblDeudaPendiente, lblSaldoDisponible;
    private JButton btnCerrar;
    
    public ReporteFinancieroGUI() {
        gestorFinanzas = new GestorFinanzas();
        gestorFinanzas.cargarArchivos();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        
        cargarDatos();
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel lblTitulo = new JLabel("REPORTE FINANCIERO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(168, 27, 29));
        
        panel.add(lblTitulo);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
        
        // Tarjeta Saldo
        JPanel panelSaldo = crearTarjeta("SALDO ACTUAL", "Bs. 0.00", new Color(52, 152, 219));
        lblSaldo = (JLabel) panelSaldo.getComponent(1);
        
        // Tarjeta Deuda
        JPanel panelDeuda = crearTarjeta("DEUDA PENDIENTE", "Bs. 0.00", new Color(241, 196, 15));
        lblDeudaPendiente = (JLabel) panelDeuda.getComponent(1);
        
        // Tarjeta Saldo Disponible
        JPanel panelDisponible = crearTarjeta("SALDO DISPONIBLE", "Bs. 0.00", new Color(46, 204, 113));
        lblSaldoDisponible = (JLabel) panelDisponible.getComponent(1);
        
        panel.add(panelSaldo);
        panel.add(panelDeuda);
        panel.add(panelDisponible);
        
        return panel;
    }
    
    private JPanel crearTarjeta(String titulo, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(80, 80, 80));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValor.setForeground(color);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        btnCerrar = new JButton("Volver a Reportes");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(168, 27, 29));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> volverAReportes());
        
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void cargarDatos() {
        double saldo = gestorFinanzas.calcularSaldo();
        double deudaPendiente = gestorFinanzas.totalDeudaPendiente();
        double saldoDisponible = saldo - deudaPendiente;
        
        lblSaldo.setText(String.format("Bs. %.2f", saldo));
        lblDeudaPendiente.setText(String.format("Bs. %.2f", deudaPendiente));
        
        if (saldoDisponible >= 0) {
            lblSaldoDisponible.setText(String.format("Bs. %.2f", saldoDisponible));
            lblSaldoDisponible.setForeground(new Color(46, 204, 113));
        } else {
            lblSaldoDisponible.setText(String.format("Bs. %.2f (NEGATIVO)", saldoDisponible));
            lblSaldoDisponible.setForeground(new Color(231, 76, 60));
        }
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 889, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 548, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
