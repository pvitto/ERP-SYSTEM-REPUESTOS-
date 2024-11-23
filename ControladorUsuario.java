import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class ControladorUsuario {
    private List<Usuario> usuarios;
    private final String archivoUsuarios = "usuarios.txt";

    public ControladorUsuario() {
        this.usuarios = new ArrayList<>();
        cargarUsuariosDesdeArchivo();
        if (usuarios.isEmpty()) {
            usuarios.add(new Usuario("admin", "admin123", "admin"));
            guardarUsuariosEnArchivo();
        }
    }

    public Usuario autenticar(String username, String password) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }

    public void agregarUsuario(String username, String password, String role) {
        usuarios.add(new Usuario(username, password, role));
        guardarUsuariosEnArchivo();
    }

    public void cambiarClave(String username, String nuevaClave) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                usuario.setPassword(nuevaClave);
                guardarUsuariosEnArchivo();
                break;
            }
        }
    }

    public void actualizarUltimaEntrada(Usuario usuario) {
        String fechaHoraActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        usuario.updateLastLogin(fechaHoraActual);
        guardarUsuariosEnArchivo();
    }

    public void agregarDuracion(Usuario usuario, long duracion) {
        usuario.addDuration(duracion);
        guardarUsuariosEnArchivo();
    }

    public List<Usuario> listarUsuarios() {
        return usuarios;
    }

public void guardarUsuariosEnArchivo() {
    if (usuarios.isEmpty()) {
        System.out.println("No hay usuarios para guardar. Se evitó sobrescribir un archivo vacío.");
        return;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoUsuarios))) {
        for (Usuario usuario : usuarios) {
            // Convierte el mapa de permisos a una representación en cadena
            StringBuilder permisosBuilder = new StringBuilder();
            usuario.getPermisos().forEach((permiso, valor) -> {
                permisosBuilder.append(permiso).append(":").append(valor).append(";");
            });

            // Escribe los datos del usuario en el archivo
            writer.write(usuario.getUsername() + "," +
                         usuario.getPassword() + "," +
                         usuario.getRole() + "," +
                         usuario.getLastLogin() + "," +
                         usuario.getTotalDuration() + "," +
                         permisosBuilder.toString());
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


  private void cargarUsuariosDesdeArchivo() {
    File archivo = new File(archivoUsuarios);
    if (!archivo.exists()) {
        // Si el archivo no existe, crea el usuario administrador por defecto
        usuarios.add(new Usuario("admin", "admin123", "admin"));
        guardarUsuariosEnArchivo(); // Guarda el archivo con el usuario admin
        return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length >= 6) {
                String username = datos[0];
                String password = datos[1];
                String role = datos[2];
                String lastLogin = datos[3];
                long totalDuration = Long.parseLong(datos[4]);

                // Reconstruir el mapa de permisos
                Map<String, Boolean> permisos = new HashMap<>();
                if (datos.length > 5) {
                    String[] permisosArray = datos[5].split(";");
                    for (String permiso : permisosArray) {
                        String[] permisoDatos = permiso.split(":");
                        if (permisoDatos.length == 2) {
                            permisos.put(permisoDatos[0], Boolean.parseBoolean(permisoDatos[1]));
                        }
                    }
                }

                // Crear el usuario con los datos y permisos
                Usuario usuario = new Usuario(username, password, role);
                usuario.updateLastLogin(lastLogin);
                usuario.addDuration(totalDuration);
                usuario.setPermisos(permisos);
                usuarios.add(usuario);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
