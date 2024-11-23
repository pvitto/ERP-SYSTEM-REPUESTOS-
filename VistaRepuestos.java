//* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 

/**
 *
 * @author Soporte-Sistemas viejo
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


public class VistaRepuestos extends JFrame {
    private ControladorRepuesto controlador;
    private Usuario usuarioActual; // Usuario logueado****
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private JTable tableRepuestos;
    private DefaultTableModel tableModel;

    // Componentes para el panel Modificar
    private JTextField txtBuscarIdModificar;
    private JLabel lblNombreModificar;
    private JTextField txtPrecioModificar;
    private JTextField txtCantidadModificar;
    private JLabel lblClaseModificar;
    private boolean hasChanges = false; // Variable para detectar si hay cambios sin guardar

    // Componentes para el panel Agregar
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtCantidad;
    private JComboBox<String> comboClase;

    // Componentes para el panel Listar
    private JTextField txtBuscarPorIdListar;
    private JComboBox<String> comboClaseBuscarListar;

    VistaRepuestos(ControladorRepuesto controlador, Usuario usuarioActual) {
        this.controlador = controlador;
        this.usuarioActual = usuarioActual;

        // Configurar la ventana principal
        setTitle("IN de Repuestos de Maquinaria Pesada");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Usaremos un CardLayout para cambiar entre las secciones
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        // Agregar los paneles para cada sección
        panelPrincipal.add(crearPanelMenuPrincipal(), "Menu");
        panelPrincipal.add(crearPanelAgregar(), "Agregar");
        panelPrincipal.add(crearPanelModificar(), "Modificar");
        panelPrincipal.add(crearPanelEliminar(), "Eliminar");
        panelPrincipal.add(crearPanelListar(), "Listar");

        add(panelPrincipal, BorderLayout.CENTER);
        
         configurarPermisos();
    }

    private void configurarPermisos() {
        if (!usuarioActual.tienePermiso("Editar")) {
            JOptionPane.showMessageDialog(this, "Bienvenido.", "Bienvenido al Sistema ERP PM", JOptionPane.WARNING_MESSAGE);
            cardLayout.show(panelPrincipal, "Menu");
        }
    }

    // Crear el menú principal con 5 botones
    private JPanel crearPanelMenuPrincipal() {
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(5, 1, 10, 10));

        JButton btnAgregar = new JButton("Añadir Repuesto");
        JButton btnModificar = new JButton("Modificar Repuesto");
        JButton btnEliminar = new JButton("Eliminar Repuesto");
        JButton btnListar = new JButton("Listar Repuestos");
        JButton btnSalir = new JButton("Salir");
    
        
// para que los botones no sean clicables cuando el usuario no tiene permiso
      
//btnAgregar.setEnabled(usuarioActual.tienePermiso("IngresarRe") || usuarioActual.tienePermiso("Todo"));
//btnModificar.setEnabled(usuarioActual.tienePermiso("ModificarRe") || usuarioActual.tienePermiso("Todo"));
//btnEliminar.setEnabled(usuarioActual.tienePermiso("EliminarRe") || usuarioActual.tienePermiso("Todo"));
//btnListar.setEnabled(usuarioActual.tienePermiso("ListarRe") || usuarioActual.tienePermiso("Todo"));

  

        // Acciones de los botones
        // Acciones de los botones
btnAgregar.addActionListener(e -> {
    if (usuarioActual.tienePermiso("IngresarRe") || usuarioActual.tienePermiso("Todo")) {
        limpiarCamposAgregar();
        cardLayout.show(panelPrincipal, "Agregar");
    } else {
        JOptionPane.showMessageDialog(this, "No tienes permisos para añadir repuestos.", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
    }
});

btnModificar.addActionListener(e -> {
    if (usuarioActual.tienePermiso("ModificarRe") || usuarioActual.tienePermiso("Todo")) {
        cardLayout.show(panelPrincipal, "Modificar");
    } else {
        JOptionPane.showMessageDialog(this, "No tienes permisos para modificar repuestos.", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
    }
});

btnEliminar.addActionListener(e -> {
    if (usuarioActual.tienePermiso("EliminarRe") || usuarioActual.tienePermiso("Todo")) {
        cardLayout.show(panelPrincipal, "Eliminar");
    } else {
        JOptionPane.showMessageDialog(this, "No tienes permisos para eliminar repuestos.", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
    }
});

btnListar.addActionListener(e -> {
    if (usuarioActual.tienePermiso("ListarRe") || usuarioActual.tienePermiso("Todo")) {
        cardLayout.show(panelPrincipal, "Listar");
    } else {
        JOptionPane.showMessageDialog(this, "No tienes permisos para listar repuestos.", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
    }
});


btnSalir.addActionListener(e -> {
    JOptionPane.showMessageDialog(null, "Gracias por usar el sistema RCatGap de Paolo", "Hasta pronto", JOptionPane.INFORMATION_MESSAGE);
    System.exit(0);
});

// Añadir botones al panel
panelMenu.add(btnAgregar);
panelMenu.add(btnModificar);
panelMenu.add(btnEliminar);
panelMenu.add(btnListar);
panelMenu.add(btnSalir);

return panelMenu;

    }

// Panel para Agregar Repuestos
private JPanel crearPanelAgregar() {
    JPanel panelAgregar = new JPanel();
    panelAgregar.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;

    JLabel lblId = new JLabel("ID del repuesto:");
    panelAgregar.add(lblId, gbc);

    gbc.gridx = 1;
    txtId = new JTextField(15);
    panelAgregar.add(txtId, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    JLabel lblNombre = new JLabel("Nombre del repuesto:");
    panelAgregar.add(lblNombre, gbc);

    gbc.gridx = 1;
    txtNombre = new JTextField(15);
    panelAgregar.add(txtNombre, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    JLabel lblPrecio = new JLabel("Precio del repuesto:");
    panelAgregar.add(lblPrecio, gbc);

    gbc.gridx = 1;
    txtPrecio = new JTextField(15);
    panelAgregar.add(txtPrecio, gbc);

    // Añadir un KeyListener para formateo en tiempo real del precio
    txtPrecio.addKeyListener(new KeyAdapter() {
        private boolean isFormatting = false;

        @Override
        public void keyReleased(KeyEvent e) {
            if (isFormatting) {
                return;
            }

            isFormatting = true;

            SwingUtilities.invokeLater(() -> {
                try {
                    String texto = txtPrecio.getText().replaceAll("[^\\d.,]", ""); 
                    if (!texto.isEmpty()) {
                        BigDecimal precio = new BigDecimal(texto.replace(",", ""));
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                        DecimalFormat formato = new DecimalFormat("#,###.##", symbols);
                        txtPrecio.setText(formato.format(precio));
                        txtPrecio.setCaretPosition(txtPrecio.getText().length());
                    } else {
                        txtPrecio.setText("");
                    }
                    hasChanges = true; // Indicar que hay cambios
                } catch (NumberFormatException ex) {
                    // Manejar excepciones si es necesario
                } finally {
                    isFormatting = false;
                }
            });
        }
    });

    gbc.gridx = 0;
    gbc.gridy = 3;
    JLabel lblCantidad = new JLabel("Cantidad disponible:");
    panelAgregar.add(lblCantidad, gbc);

    gbc.gridx = 1;
    txtCantidad = new JTextField(15);
    panelAgregar.add(txtCantidad, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    JLabel lblClase = new JLabel("Clase del repuesto:");
    panelAgregar.add(lblClase, gbc);

    gbc.gridx = 1;
    comboClase = new JComboBox<>(new String[]{"A", "B", "C"});
    panelAgregar.add(comboClase, gbc);

    // Botones
    gbc.gridx = 1;
    gbc.gridy = 5;
    JButton btnAgregar = new JButton("Agregar Repuesto");
    panelAgregar.add(btnAgregar, gbc);

    gbc.gridx = 2;
    JButton btnAtras = new JButton("Atrás");
    panelAgregar.add(btnAtras, gbc);

    gbc.gridx = 3;
    JButton btnSalir = new JButton("Salir");
    panelAgregar.add(btnSalir, gbc);

  /**
 * Este es el fragmento modificado para la funcionalidad "No permitir agregar un item si ya existe por ID".
 * Implementa la validación directamente en el botón "Agregar Repuesto".
 */

// Añadir un FocusListener al campo de texto ID
txtId.addFocusListener(new FocusAdapter() {
    @Override
    public void focusLost(FocusEvent e) {
        String id = txtId.getText().trim();
        if (!id.isEmpty()) {
            if (controlador.buscarRepuestoPorId(id) != null) {
                JOptionPane.showMessageDialog(null, "El ID ya está en uso. Por favor, elige otro ID.", "Error", JOptionPane.ERROR_MESSAGE);
                txtId.requestFocus(); // Regresar el foco al campo ID
            }
        }
    }
});
// Acción del botón "Agregar Repuesto"
btnAgregar.addActionListener(e -> {
    try {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim().replace(",", ""); 
        String cantidadStr = txtCantidad.getText().trim();
        String clase = (String) comboClase.getSelectedItem();

        if (id.isEmpty() || nombre.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar si el ID ya existe
        if (controlador.buscarRepuestoPorId(id) != null) {
            JOptionPane.showMessageDialog(null, "El ID ya está en uso. Por favor, elige otro ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal precio = new BigDecimal(precioStr);
        int cantidad = Integer.parseInt(cantidadStr);

        Repuesto repuesto = new Repuesto(id, nombre, precio, cantidad, clase);
        controlador.agregarRepuesto(repuesto);

        JOptionPane.showMessageDialog(null, "¡Repuesto agregado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        hasChanges = false; // Resetear cambios
        limpiarCamposAgregar();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Error: Asegúrate de ingresar valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
    }
});



    // Acción del botón "Atrás"
    btnAtras.addActionListener(e -> {
        verificarCambiosYSalir(() -> {
            limpiarCamposAgregar();
            cardLayout.show(panelPrincipal, "Menu");
        });
    });

    // Acción del botón "Salir"
    btnSalir.addActionListener(e -> {
        verificarCambiosYSalir(() -> {
            JOptionPane.showMessageDialog(null, "Gracias por usar el sistema", "Hasta pronto", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        });
    });

    return panelAgregar;
}

// Método para limpiar los campos
private void limpiarCamposAgregar() {
    txtId.setText("");
    txtNombre.setText("");
    txtPrecio.setText("");
    txtCantidad.setText("");
    comboClase.setSelectedIndex(0);
}

// Método genérico para verificar cambios y ejecutar acción al salir
private void verificarCambiosYSalir(Runnable onSuccess) {
    boolean hayCambios = !txtId.getText().trim().isEmpty() ||
                         !txtNombre.getText().trim().isEmpty() ||
                         !txtPrecio.getText().trim().isEmpty() ||
                         !txtCantidad.getText().trim().isEmpty() ||
                         comboClase.getSelectedIndex() != 0;

    if (hayCambios && hasChanges) {
        int response = JOptionPane.showOptionDialog(
            null,
            "Tienes cambios sin guardar, ¿estás seguro de que quieres salir?",
            "Advertencia",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[]{"Sí", "No"},
            "No"
        );
        if (response == JOptionPane.YES_OPTION) {
            onSuccess.run();
        }
    } else {
        onSuccess.run();
    }
}


    // Panel para Modificar Repuestos
    private JPanel crearPanelModificar() {
        JPanel panelModificar = new JPanel();
        panelModificar.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblIdBuscar = new JLabel("ID del repuesto a modificar:");
        panelModificar.add(lblIdBuscar, gbc);

        gbc.gridx = 1;
        txtBuscarIdModificar = new JTextField(15);
        panelModificar.add(txtBuscarIdModificar, gbc);

        // Botón para buscar el repuesto
        gbc.gridx = 2;
        JButton btnBuscarRepuesto = new JButton("Cargar Repuesto");
        panelModificar.add(btnBuscarRepuesto, gbc);

        // Agregar un label para el nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblNombre = new JLabel("Nombre del repuesto:");
        panelModificar.add(lblNombre, gbc);

        gbc.gridx = 1;
        lblNombreModificar = new JLabel();
        panelModificar.add(lblNombreModificar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblPrecio = new JLabel("Nuevo precio:");
        panelModificar.add(lblPrecio, gbc);

        gbc.gridx = 1;
        txtPrecioModificar = new JTextField(15);
        panelModificar.add(txtPrecioModificar, gbc);

        // Formateo en tiempo real en la vista de modificar
        txtPrecioModificar.addKeyListener(new KeyAdapter() {
            private boolean isFormatting = false;

            @Override
            public void keyReleased(KeyEvent e) {
                if (isFormatting) {
                    return;
                }

                isFormatting = true;

                SwingUtilities.invokeLater(() -> {
                    try {
                        String texto = txtPrecioModificar.getText().replaceAll("[^\\d.,]", "");
                        if (!texto.isEmpty()) {
                            BigDecimal precio = new BigDecimal(texto.replace(",", ""));
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                            DecimalFormat formato = new DecimalFormat("#,###.##", symbols);
                            txtPrecioModificar.setText(formato.format(precio));
                            txtPrecioModificar.setCaretPosition(txtPrecioModificar.getText().length());
                        } else {
                            txtPrecioModificar.setText("");
                        }
                    } catch (NumberFormatException ex) {
                        // No hacer nada si hay un error
                    } finally {
                        isFormatting = false;
                    }
                });
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblCantidad = new JLabel("Nueva cantidad:");
        panelModificar.add(lblCantidad, gbc);

        gbc.gridx = 1;
        txtCantidadModificar = new JTextField(15);
        panelModificar.add(txtCantidadModificar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblClase = new JLabel("Clase:");
        panelModificar.add(lblClase, gbc);

        gbc.gridx = 1;
        lblClaseModificar = new JLabel();
        panelModificar.add(lblClaseModificar, gbc);

        // Botón para guardar cambios
        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton btnGuardar = new JButton("Guardar Cambios");
        panelModificar.add(btnGuardar, gbc);

        // Botón para regresar al menú
        gbc.gridx = 2;
        JButton btnAtras = new JButton("Atrás");
        panelModificar.add(btnAtras, gbc);

        // Botón para salir
        gbc.gridx = 3;
        JButton btnSalir = new JButton("Salir");
        panelModificar.add(btnSalir, gbc);

        // Acción del botón "Cargar Repuesto"
        btnBuscarRepuesto.addActionListener(e -> {
            String id = txtBuscarIdModificar.getText().trim();

            // Buscar repuesto por ID
            Repuesto repuesto = controlador.buscarRepuestoPorId(id);

            if (repuesto != null) {
                lblNombreModificar.setText(repuesto.getNombre());

                // Formatear el precio con dos decimales
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat formato = new DecimalFormat("#,###.00", symbols);
                txtPrecioModificar.setText(formato.format(repuesto.getPrecio()));

                txtCantidadModificar.setText(String.valueOf(repuesto.getCantidad()));
                lblClaseModificar.setText(repuesto.getClase());
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un repuesto con el ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción del botón "Guardar Cambios"
        btnGuardar.addActionListener(e -> {
            String id = txtBuscarIdModificar.getText().trim();
            String nuevoPrecioStr = txtPrecioModificar.getText().trim().replace(",", ""); // Eliminar comas
            String nuevaCantidadStr = txtCantidadModificar.getText().trim();

            if (id.isEmpty() || nuevoPrecioStr.isEmpty() || nuevaCantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BigDecimal nuevoPrecio = new BigDecimal(nuevoPrecioStr);
                int nuevaCantidad = Integer.parseInt(nuevaCantidadStr);
                String nuevaClase = lblClaseModificar.getText();

                // Llamar al controlador para modificar el repuesto
                if (controlador.modificarRepuesto(id, lblNombreModificar.getText(), nuevoPrecio, nuevaCantidad, nuevaClase)) {
                    JOptionPane.showMessageDialog(null, "¡Repuesto modificado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Limpiar campos después de modificar
                    limpiarCamposModificar();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar el repuesto.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Asegúrate de ingresar valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción del botón "Atrás" con mensaje de advertencia si no se guardaron cambios
        btnAtras.addActionListener(e -> {
            if (!hasChanges) {
                int response = JOptionPane.showOptionDialog(
                        null,
                        "No realizaste ningún cambio, ¿estás seguro de salir?",
                        "Advertencia",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Sí", "No"},
                        "No"
                );
                if (response == JOptionPane.YES_OPTION) {
                    limpiarCamposModificar();
                    cardLayout.show(panelPrincipal, "Menu");
                }
            } else {
                limpiarCamposModificar();
                cardLayout.show(panelPrincipal, "Menu");
            }
        });

        // Acción del botón "Salir" con mensaje de advertencia si no se guardaron cambios
        btnSalir.addActionListener(e -> {
            if (!hasChanges) {
                int response = JOptionPane.showOptionDialog(
                        null,
                        "No realizaste ningún cambio, ¿estás seguro de salir?",
                        "Advertencia",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Sí", "No"},
                        "No"
                );
                if (response == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Gracias por usar el sistema RCatGap de Paolo", "Hasta pronto", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Gracias por usar el sistema RCatGap de Paolo", "Hasta pronto", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });

        return panelModificar;
    }

    // Método para limpiar los campos en la vista de modificar
    private void limpiarCamposModificar() {
        txtBuscarIdModificar.setText("");
        lblNombreModificar.setText("");
        txtPrecioModificar.setText("");
        txtCantidadModificar.setText("");
        lblClaseModificar.setText("");
    }

    // Panel para Eliminar Repuestos
    private JPanel crearPanelEliminar() {
        JPanel panelEliminar = new JPanel();
        panelEliminar.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblIdEliminar = new JLabel("ID del repuesto a eliminar:");
        panelEliminar.add(lblIdEliminar, gbc);

        gbc.gridx = 1;
        JTextField txtIdEliminar = new JTextField(15);
        panelEliminar.add(txtIdEliminar, gbc);

        // Botones
        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton btnEliminar = new JButton("Eliminar Repuesto");
        panelEliminar.add(btnEliminar, gbc);

        gbc.gridx = 2;
        JButton btnAtras = new JButton("Atrás");
        panelEliminar.add(btnAtras, gbc);

        gbc.gridx = 3;
        JButton btnSalir = new JButton("Salir");
        panelEliminar.add(btnSalir, gbc);

        // Acción del botón "Eliminar Repuesto"
        btnEliminar.addActionListener(e -> {
            String id = txtIdEliminar.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese el ID del repuesto a eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean eliminado = controlador.eliminarRepuesto(id);

            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Repuesto eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "El repuesto con ID " + id + " no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            txtIdEliminar.setText("");
        });

        // Acción del botón "Atrás"
        btnAtras.addActionListener(e -> cardLayout.show(panelPrincipal, "Menu"));

        // Acción del botón "Salir"
        btnSalir.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Gracias por usar el sistema RCatGap de Paolo", "Hasta pronto", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        });

        return panelEliminar;
    }

    // Panel para Listar Repuestos
    private JPanel crearPanelListar() {
        JPanel panelListar = new JPanel();
        panelListar.setLayout(new BorderLayout());

        // Crear tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Cantidad", "Clase"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactivar edición de celdas
            }
        };
        tableRepuestos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableRepuestos);

        // Panel superior con filtros
        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new GridLayout(1, 7));

        // Crear JComboBox para seleccionar el orden
        JComboBox<String> comboOrden = new JComboBox<>(new String[]{"Ascendente", "Descendente"});

        txtBuscarPorIdListar = new JTextField();
        comboClaseBuscarListar = new JComboBox<>(new String[]{"Todas", "A", "B", "C"});

        // Botón para buscar por ID
        JButton btnBuscarPorId = new JButton("Buscar por ID");

        // Añadir los filtros al panel
        panelFiltros.add(new JLabel("Ordenar por ID:"));
        panelFiltros.add(comboOrden);  // Añadir el JComboBox de orden
        panelFiltros.add(new JLabel("Buscar por ID:"));
        panelFiltros.add(txtBuscarPorIdListar);
        panelFiltros.add(btnBuscarPorId);  // Añadir botón de búsqueda por ID
        panelFiltros.add(new JLabel("Buscar por Clase:"));
        panelFiltros.add(comboClaseBuscarListar);

        // Acción del botón de "Buscar por ID"
        btnBuscarPorId.addActionListener(e -> {
            String idBuscado = txtBuscarPorIdListar.getText().trim();

            // Obtener todos los repuestos
            List<Repuesto> repuestosFiltrados = controlador.listarRepuestos();

            // Filtrar por ID si no está vacío
            if (!idBuscado.isEmpty()) {
                repuestosFiltrados = repuestosFiltrados.stream()
                        .filter(repuesto -> repuesto.getId().equals(idBuscado))
                        .collect(Collectors.toList());
            }

            // Cargar los repuestos filtrados en la tabla
            cargarRepuestosEnTabla(repuestosFiltrados);
        });

        // Añadir un ActionListener al ComboBox de Clase para que filtre automáticamente por clase
        comboClaseBuscarListar.addActionListener(e -> {
            String claseSeleccionada = (String) comboClaseBuscarListar.getSelectedItem();

            // Obtener todos los repuestos
            List<Repuesto> repuestosFiltrados = controlador.listarRepuestos();

            // Filtrar por clase seleccionada si no es "Todas"
            if (!claseSeleccionada.equals("Todas")) {
                repuestosFiltrados = repuestosFiltrados.stream()
                        .filter(repuesto -> repuesto.getClase().equalsIgnoreCase(claseSeleccionada))
                        .collect(Collectors.toList());
            }

            // Cargar los repuestos filtrados en la tabla
            cargarRepuestosEnTabla(repuestosFiltrados);
        });

        
        // Añadir un ActionListener al ComboBox de Orden para que ordene automáticamente
        comboOrden.addActionListener(e -> {
    // Obtener el orden seleccionado
    String ordenSeleccionado = (String) comboOrden.getSelectedItem();

    // Obtener todos los repuestos
    List<Repuesto> repuestosFiltrados = controlador.listarRepuestos();

    // Aplicar el orden seleccionado por ID
    if (ordenSeleccionado.equals("Ascendente")) {
        repuestosFiltrados.sort((r1, r2) -> r1.getId().compareTo(r2.getId())); // Orden alfabético ascendente
    } else if (ordenSeleccionado.equals("Descendente")) {
        repuestosFiltrados.sort((r1, r2) -> r2.getId().compareTo(r1.getId())); // Orden alfabético descendente
    }

    // Cargar los repuestos ordenados en la tabla
    cargarRepuestosEnTabla(repuestosFiltrados);
});

        // Botones inferiores
        JPanel panelBotones = new JPanel();
        JButton btnAtras = new JButton("Atrás");
        JButton btnSalir = new JButton("Salir");

        // Acción del botón "Atrás"
        btnAtras.addActionListener(e -> cardLayout.show(panelPrincipal, "Menu"));

        // Acción del botón "Salir"
        btnSalir.addActionListener(e -> {
    int confirm = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de que deseas salir?",
        "Confirmar salida",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );

    if (confirm == JOptionPane.YES_OPTION) {
        JOptionPane.showMessageDialog(null, "Gracias por usar el sistema", "Hasta pronto", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0); // Cierra el programa
    }
});

        panelBotones.add(btnAtras);
        panelBotones.add(btnSalir);
        cargarRepuestosEnTabla(controlador.listarRepuestos());

        panelListar.add(panelFiltros, BorderLayout.NORTH);
        panelListar.add(scrollPane, BorderLayout.CENTER);
        panelListar.add(panelBotones, BorderLayout.SOUTH);

        return panelListar;
    }

    // Método para cargar los repuestos en la tabla
    private void cargarRepuestosEnTabla(List<Repuesto> repuestos) {
        tableModel.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos
        DecimalFormat formato = new DecimalFormat("#,###.00", new DecimalFormatSymbols(Locale.US));

        for (Repuesto repuesto : repuestos) {
            tableModel.addRow(new Object[]{
                    repuesto.getId(),
                    repuesto.getNombre(),
                    formato.format(repuesto.getPrecio()),
                    repuesto.getCantidad(),
                    repuesto.getClase()
            });
        }
    }
}
