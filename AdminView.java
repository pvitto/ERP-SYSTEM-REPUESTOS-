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

class AdminView extends JFrame {
    public AdminView(ControladorUsuario controladorUsuario, ControladorRepuesto controladorRepuesto, Usuario usuarioActual) {
        setTitle("Vista de Admin");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Menú de Administrador", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnConfig = new JButton("Configuración");
        JButton btnGestionRepuestos = new JButton("Gestión de Repuestos");
        JButton btnSalir = new JButton("Salir");

        panelBotones.add(btnConfig);
        panelBotones.add(btnGestionRepuestos);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);

        // Acciones de los botones
        btnConfig.addActionListener(e -> new ConfigView(controladorUsuario).setVisible(true));
        btnGestionRepuestos.addActionListener(e -> {
            new VistaRepuestos(controladorRepuesto, usuarioActual).setVisible(true);
        });
        btnSalir.addActionListener(e -> System.exit(0));
    }
}
