package pizzeria.view;

import java.util.Scanner;

public class Consola {
    private static final Scanner sc = new Scanner(System.in);


    public static String leerTexto(String mensaje) {
        String entrada;
        do {
            System.out.print(" " + mensaje);
            entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println(" El campo no puede estar vacío. Intente de nuevo.");
            }
        } while (entrada.isEmpty());
        return entrada;
    }

    public static String leerTextoOpcional(String mensaje) {
        System.out.print(" " + mensaje);
        return sc.nextLine().trim();
    }

    public static String leerTextoCancelable(String mensaje) {
        while (true) {
            System.out.print(" " + mensaje + " (o escriba CANCELAR): ");
            String entrada = sc.nextLine().trim();

            if (entrada.equalsIgnoreCase("CANCELAR")) {
                if (confirmar("¿Está seguro de cancelar?")) {
                    return null;
                }
                continue;
            }

            if (entrada.isEmpty()) {
                System.out.println(" El campo no puede estar vacío. Intente de nuevo.");
                continue;
            }

            return entrada;
        }
    }

    public static String leerTextoOpcionalCancelable(String mensaje) {
        while (true) {
            System.out.print(" " + mensaje + " (Enter para dejar vacío, o escriba CANCELAR): ");
            String entrada = sc.nextLine().trim();

            if (entrada.equalsIgnoreCase("CANCELAR")) {
                if (confirmar("¿Está seguro de cancelar?")) {
                    return null;
                }
                continue;
            }

            return entrada;
        }
    }

    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(" " + mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(" Debe ingresar un número entero válido.");
            }
        }
    }
   
    public static Integer leerEnteroCancelable(String mensaje) {
        while (true) {
            System.out.print(" " + mensaje + " (o escriba CANCELAR): ");
            String entrada = sc.nextLine().trim();

            if (entrada.equalsIgnoreCase("CANCELAR")) {
                if (confirmar("¿Está seguro de cancelar?")) {
                    return null;
                }
                continue;
            }

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(" Debe ingresar un número entero válido.");
            }
        }
    }

    public static int leerEnteroRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf(" Ingrese un número entero entre %d y %d.%n", min, max);
        }
    }

    public static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(" " + mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println(" Debe ingresar un número decimal válido.");
            }
        }
    }

    public static Double leerDoubleCancelable(String mensaje) {
        while (true) {
            System.out.print(" " + mensaje + " (o escriba CANCELAR): ");
            String entrada = sc.nextLine().trim();

            if (entrada.equalsIgnoreCase("CANCELAR")) {
                if (confirmar("¿Está seguro de cancelar?")) {
                    return null;
                }
                continue;
            }

            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println(" Debe ingresar un número decimal válido.");
            }
        }
    }

    public static boolean confirmar(String mensaje) {
        while (true) {
            System.out.print(" " + mensaje + " (S/N): ");
            String r = sc.nextLine().trim().toUpperCase();

            if (r.equals("S")) {
                return true;
            }
            if (r.equals("N")) {
                return false;
            }

            System.out.println(" Respuesta inválida. Escriba S o N.");
        }
    }

    public static void pausar() {
        System.out.println();
        System.out.print(" Presione Enter para continuar...");
        sc.nextLine();
    }

    public static void separador() {
        System.out.println(" " + "-".repeat(70));
    }

    public static void titulo(String texto) {
        int total = 70;
        int padding = (total - texto.length() - 2) / 2;
        String pad = "=".repeat(Math.max(padding, 1));

        System.out.println();
        System.out.println(" " + pad + " " + texto + " " + pad);
    }

    public static Scanner getScanner() {
        return sc;
    }
}