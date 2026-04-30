package Modelo;

public class Usuario {

    private int id_usuario;
    private String dpi;
    private String nombre;
    private String usuario;
    private String area;
    private String puesto;
    private int turno_actual_id;
    private String turno;
    private String correo;
    private String password;
    private String estado;
    private Integer motivo_inactivo_id;
    private int rol_id;
    private String rol;

    public Usuario() {
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public int getTurno_actual_id() {
        return turno_actual_id;
    }

    public void setTurno_actual_id(int turno_actual_id) {
        this.turno_actual_id = turno_actual_id;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getMotivo_inactivo_id() {
        return motivo_inactivo_id;
    }

    public void setMotivo_inactivo_id(Integer motivo_inactivo_id) {
        this.motivo_inactivo_id = motivo_inactivo_id;
    }

    public int getRol_id() {
        return rol_id;
    }

    public void setRol_id(int rol_id) {
        this.rol_id = rol_id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

