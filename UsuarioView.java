/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Paolo Mancini - Lapt
 */

import javax.swing.*;
import java.awt.*;

class UsuarioView extends JFrame {
    private JButton btnGestionRepuestos;
    private JButton btnSalir;
    private ControladorRepuesto controladorRepuesto;
    private Usuario usuarioActual;

    public UsuarioView(ControladorRepuesto controladorRepuesto, Usuario usuarioActual) {
        this.controladorRepuesto = controladorRepuesto;
        this.usuarioActual = usuarioActual;

        setTitle("Vista de Usuario");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Gestión de Repuestos", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        btnGestionRepuestos = new JButton("Gestión de Repuestos");
        btnGestionRepuestos.setFont(new Font("Arial", Font.PLAIN, 14));
        panelBotones.add(btnGestionRepuestos);

        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 14));
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);

      btnGestionRepuestos.addActionListener(e -> {
    new VistaRepuestos(controladorRepuesto, usuarioActual).setVisible(true);
    dispose();
});

        btnSalir.addActionListener(e -> dispose());
    }
}
