/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Paolo Mancini - Lapt
 */


import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;


class ConfigView extends JFrame {
    public ConfigView(ControladorUsuario controladorUsuario) {
        setTitle("Configuración de Usuarios");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Gestión de Usuarios", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnAgregarUsuario = new JButton("Agregar Usuario");
        JButton btnListarUsuarios = new JButton("Listar Usuarios");
        JButton btnCambiarClave = new JButton("Cambiar Clave");
        JButton btnGestionarPermisos = new JButton("Administrar Permisos");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnAgregarUsuario);
        panelBotones.add(btnListarUsuarios);
        panelBotones.add(btnCambiarClave);
        panelBotones.add(btnGestionarPermisos);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.CENTER);

        btnAgregarUsuario.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Ingrese el nombre de usuario:");
            String password = JOptionPane.showInputDialog(this, "Ingrese la contraseña:");
            String[] roles = {"admin", "usuario"};
            String role = (String) JOptionPane.showInputDialog(this, "Seleccione el rol:", "Rol", JOptionPane.QUESTION_MESSAGE, null, roles, roles[1]);

            if (username != null && password != null && role != null) {
                controladorUsuario.agregarUsuario(username, password, role);
                JOptionPane.showMessageDialog(this, "Usuario agregado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnListarUsuarios.addActionListener(e -> {
            List<Usuario> usuarios = controladorUsuario.listarUsuarios();
            StringBuilder lista = new StringBuilder("Usuarios:\n");
            for (Usuario usuario : usuarios) {
                lista.append(usuario.getUsername()).append(" - ").append(usuario.getRole()).append("\n");
            }
            JOptionPane.showMessageDialog(this, lista.toString(), "Lista de Usuarios", JOptionPane.INFORMATION_MESSAGE);
        });
btnGestionarPermisos.addActionListener(e -> {
    new PermisosView(controladorUsuario).setVisible(true);
});
        btnCambiarClave.addActionListener(e -> {
            List<Usuario> usuarios = controladorUsuario.listarUsuarios();
            String[] usernames = usuarios.stream().map(Usuario::getUsername).toArray(String[]::new);

            String usernameSeleccionado = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el usuario para cambiar la clave:",
                "Cambiar Clave",
                JOptionPane.QUESTION_MESSAGE,
                null,
                usernames,
                usernames[0]
            );

            if (usernameSeleccionado != null) {
                String nuevaClave = JOptionPane.showInputDialog(this, "Ingrese la nueva clave para el usuario: " + usernameSeleccionado);
                if (nuevaClave != null) {
                    controladorUsuario.cambiarClave(usernameSeleccionado, nuevaClave);
                    JOptionPane.showMessageDialog(this, "Clave cambiada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

      
        btnCerrar.addActionListener(e -> dispose());
    }
}
