package pizzeria.IU;

import javax.swing.UIManager;

public final class ConfiguracionIdiomaSwing {

    private ConfiguracionIdiomaSwing() {
        // Evita crear objetos de esta clase.
    }

    /*
     * Traduce al español los botones predeterminados
     * de los cuadros de diálogo de Swing.
     */
    public static void aplicarEspanol() {
        UIManager.put(
                "OptionPane.yesButtonText",
                "Sí"
        );

        UIManager.put(
                "OptionPane.noButtonText",
                "No"
        );

        UIManager.put(
                "OptionPane.okButtonText",
                "Aceptar"
        );

        UIManager.put(
                "OptionPane.cancelButtonText",
                "Cancelar"
        );
    }
}