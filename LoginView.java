//old


import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private ControladorUsuario controladorUsuario;
    private ControladorRepuesto controladorRepuesto;

    public LoginView(ControladorUsuario controladorUsuario, ControladorRepuesto controladorRepuesto) {
        this.controladorUsuario = controladorUsuario;
        this.controladorRepuesto = controladorRepuesto;

        setTitle("Login - Sistema ERP PM");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Iniciar Sesión", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Usuario:"), gbc);

        
        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        panelFormulario.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        panelFormulario.add(txtPassword, gbc);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        btnLogin.setPreferredSize(new Dimension(150, 30));
        panelBotones.add(btnLogin);

        add(panelBotones, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            Usuario usuario = controladorUsuario.autenticar(username, password);
            if (usuario != null) {
                String mensaje = "Bienvenido, " + usuario.getRole() + " " + usuario.getUsername() + "!\n" +
                                 "Última entrada: " + usuario.getLastLogin() + "\n" +
                                 "Duración acumulada: " + usuario.getTotalDuration() + " segundos.";

                JOptionPane.showMessageDialog(this, mensaje, "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);

                String inicioSesion = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                controladorUsuario.actualizarUltimaEntrada(usuario);

                long startTime = System.currentTimeMillis();

                // Redirige según el rol del usuario
                if (usuario.getRole().equals("admin")) {
                    new AdminView(controladorUsuario, controladorRepuesto, usuario).setVisible(true);
                } else {
                    new UsuarioView(controladorRepuesto, usuario).setVisible(true);
                }

                dispose();

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    long endTime = System.currentTimeMillis();
                    long duration = (endTime - startTime) / 1000; // Duración en segundos
                    controladorUsuario.agregarDuracion(usuario, duration);
                }));
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
