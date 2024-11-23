import javax.swing.*;
import java.awt.*;
import java.util.List;

class PermisosView extends JFrame {
    public PermisosView(ControladorUsuario controladorUsuario) {
        setTitle("Administrar Permisos");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Administrar Permisos de Usuarios", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BorderLayout());
        add(panelCentral, BorderLayout.CENTER);

        // Obtener lista de usuarios
        List<Usuario> usuarios = controladorUsuario.listarUsuarios();
        String[] usernames = usuarios.stream().map(Usuario::getUsername).toArray(String[]::new);

        // JComboBox para seleccionar usuario
        JComboBox<String> comboUsuarios = new JComboBox<>(usernames);
        panelCentral.add(comboUsuarios, BorderLayout.NORTH);

        // Checkboxes para permisos
        JPanel panelPermisos = new JPanel(new GridLayout(0, 1, 10, 10));
        JCheckBox chkIngresarRe = new JCheckBox("Permiso para Añadir");
        JCheckBox chkModificarRe = new JCheckBox("Permiso para Modificar");
        JCheckBox chkListarRe = new JCheckBox("Permiso para Listar");
        JCheckBox chkEliminarRe = new JCheckBox("Permiso para Eliminar");
        JCheckBox chkTodo = new JCheckBox("Permiso para Todo");

        panelPermisos.add(chkIngresarRe);
        panelPermisos.add(chkModificarRe);
        panelPermisos.add(chkListarRe);
        panelPermisos.add(chkEliminarRe);
        panelPermisos.add(chkTodo);
        panelPermisos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelCentral.add(panelPermisos, BorderLayout.CENTER);

        // Botón para guardar cambios
        JButton btnGuardar = new JButton("Guardar Cambios");
        add(btnGuardar, BorderLayout.SOUTH);

        // Acción para cargar permisos del usuario seleccionado
        comboUsuarios.addActionListener(e -> {
            String username = (String) comboUsuarios.getSelectedItem();
            Usuario usuarioSeleccionado = usuarios.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

            if (usuarioSeleccionado != null) {
                chkIngresarRe.setSelected(usuarioSeleccionado.getPermisos().getOrDefault("IngresarRe", false));
                chkModificarRe.setSelected(usuarioSeleccionado.getPermisos().getOrDefault("ModificarRe", false));
                chkListarRe.setSelected(usuarioSeleccionado.getPermisos().getOrDefault("ListarRe", false));
                chkEliminarRe.setSelected(usuarioSeleccionado.getPermisos().getOrDefault("EliminarRe", false));
                chkTodo.setSelected(usuarioSeleccionado.getPermisos().getOrDefault("Todo", false));
            }
        });

        // Acción para guardar cambios
        btnGuardar.addActionListener(e -> {
    String username = (String) comboUsuarios.getSelectedItem();
    Usuario usuarioSeleccionado = usuarios.stream()
        .filter(u -> u.getUsername().equals(username))
        .findFirst()
        .orElse(null);

    if (usuarioSeleccionado != null) {
        usuarioSeleccionado.setPermiso("IngresarRe", chkIngresarRe.isSelected());
        usuarioSeleccionado.setPermiso("ModificarRe", chkModificarRe.isSelected());
        usuarioSeleccionado.setPermiso("ListarRe", chkListarRe.isSelected());
        usuarioSeleccionado.setPermiso("EliminarRe", chkEliminarRe.isSelected());
        usuarioSeleccionado.setPermiso("Todo", chkTodo.isSelected());

        // Guarda los usuarios en el archivo
        controladorUsuario.guardarUsuariosEnArchivo();
        JOptionPane.showMessageDialog(this, "Permisos actualizados con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
});


        // Cargar permisos del primer usuario al iniciar
        if (usernames.length > 0) {
            comboUsuarios.setSelectedIndex(0);
        }
    }
}
