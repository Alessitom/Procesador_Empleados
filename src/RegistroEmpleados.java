import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RegistroEmpleados {
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        String inputFileName = "./archivos/empleados.txt";
        String outputFileName = "./archivos/baseEmpleados.txt";

        Map<String, Empleado> empleados = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, "#");
                if (tokenizer.countTokens() == 5) {
                    String idEmpleado = tokenizer.nextToken();
                    String nombre = tokenizer.nextToken();
                    String apellidos = tokenizer.nextToken();
                    LocalDate fecha = LocalDate.parse(tokenizer.nextToken(), FORMATTER);
                    double horasTrabajadas = Double.parseDouble(tokenizer.nextToken());

                    Empleado empleado = empleados.get(idEmpleado);
                    if (empleado == null) {
                        empleado = new Empleado(idEmpleado, nombre, apellidos);
                        empleados.put(idEmpleado, empleado);
                    }
                    empleado.agregarRegistro(fecha, horasTrabajadas);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (Empleado empleado : empleados.values()) {
                writer.write("Empleado: " + empleado.getId() + "\n");
                writer.write("---------------------\n");
                writer.write("Nombre: " + empleado.getNombre() + "\n");
                writer.write("Apellidos: " + empleado.getApellidos() + "\n");

                double totalHoras = 0;
                for (Map.Entry<LocalDate, Double> registro : empleado.getRegistros().entrySet()) {
                    writer.write("Fecha Trabajada: " + registro.getKey().format(FORMATTER) +
                                 " | Horas Trabajadas: " + registro.getValue() + "\n");
                    totalHoras += registro.getValue();
                }

                writer.write("Horas Totales Trabajadas: " + totalHoras + "\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Empleado {
        private final String id;
        private final String nombre;
        private final String apellidos;
        private final Map<LocalDate, Double> registros;

        public Empleado(String id, String nombre, String apellidos) {
            this.id = id;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.registros = new HashMap<>();
        }

        public void agregarRegistro(LocalDate fecha, double horasTrabajadas) {
            registros.put(fecha, horasTrabajadas);
        }

        public String getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellidos() {
            return apellidos;
        }

        public Map<LocalDate, Double> getRegistros() {
            return registros;
        }
    }
}
