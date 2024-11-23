import java.util.HashMap;
import java.util.Map;

class Usuario {
    private String username;
    private String password;
    private String role;
    private String lastLogin;
    private long totalDuration;
    private Map<String, Boolean> permisos;

    public Usuario(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.lastLogin = "Nunca";
        this.totalDuration = 0;
        this.permisos = new HashMap<>();
    }

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public Map<String, Boolean> getPermisos() {
        return permisos;
    }

    public void setPermisos(Map<String, Boolean> permisos) {
        this.permisos = permisos;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void updateLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void addDuration(long duration) {
        this.totalDuration += duration;
    }

    public boolean tienePermiso(String permiso) {
        return permisos.getOrDefault(permiso, false);
    }

    public void setPermiso(String permiso, boolean valor) {
        permisos.put(permiso, valor);
    }
}
