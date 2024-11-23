/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Soporte-Sistemas
 */
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ControladorRepuesto {
    private List<Repuesto> repuestos;
    private final String archivoRepuestos = "repuestos.txt";  // Nombre del archivo en la raíz del proyecto

    public ControladorRepuesto() {
        this.repuestos = new ArrayList<>();
        cargarRepuestosDeArchivo();
    }

    public void agregarRepuesto(Repuesto repuesto) {
        repuestos.add(repuesto);
        guardarRepuestosEnArchivo();
    }

    public boolean eliminarRepuesto(String id) {
        Repuesto repuestoAEliminar = buscarRepuestoPorId(id);
        if (repuestoAEliminar != null) {
            repuestos.remove(repuestoAEliminar);
            guardarRepuestosEnArchivo();
            return true;
        }
        return false;
    }

    
  public boolean modificarRepuesto(String id, String nuevoNombre, BigDecimal nuevoPrecio, int nuevaCantidad, String nuevaClase) {
    // Buscar el repuesto por ID
    for (Repuesto repuesto : repuestos) {
        if (repuesto.getId().equals(id)) {
            // Modificar los valores del repuesto
            repuesto.setNombre(nuevoNombre);
            repuesto.setPrecio(nuevoPrecio);
            repuesto.setCantidad(nuevaCantidad);
            repuesto.setClase(nuevaClase);
            
            // Guardar los cambios en el archivo o la lista según corresponda
            guardarRepuestosEnArchivo();
            return true; // Repuesto modificado con éxito
        }
    }
    return false; // Si no se encuentra el repuesto con el ID dado
}
  
    
    
    
    public Repuesto buscarRepuestoPorId(String id) {
        for (Repuesto repuesto : repuestos) {
            if (repuesto.getId().equals(id)) {
                return repuesto;
            }
        }
        return null;
    }

    public List<Repuesto> buscarRepuestosPorClase(String clase) {
        return repuestos.stream().filter(repuesto -> repuesto.getClase().equals(clase)).collect(Collectors.toList());
    }

    public List<Repuesto> listarRepuestos() {
        return repuestos;
    }

    private void cargarRepuestosDeArchivo() {
        File archivo = new File(archivoRepuestos);
        if (!archivo.exists()) {
            System.out.println("El archivo de repuestos no existe. Creando uno nuevo.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    String id = datos[0];
                    String nombre = datos[1];
                    BigDecimal precio = new BigDecimal(datos[2]);
                    int cantidad = Integer.parseInt(datos[3]);
                    String clase = datos[4];
                    Repuesto repuesto = new Repuesto(id, nombre, precio, cantidad, clase);
                    repuestos.add(repuesto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarRepuestosEnArchivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivoRepuestos))) {
            for (Repuesto repuesto : repuestos) {
                writer.println(repuesto.getId() + "," +
                        repuesto.getNombre() + "," +
                        repuesto.getPrecio() + "," +
                        repuesto.getCantidad() + "," +
                        repuesto.getClase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


