package Modelo;

public class MTurno {

    private int id;
    private int id_usuario;
    private String fecha_inicio;
    private String turnoInicial;
    private String nuevoTurno;
    private String nuevaFecha;
    private String motivo;

    public MTurno() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getTurnoInicial() {
        return turnoInicial;
    }

    public void setTurnoInicial(String turnoInicial) {
        this.turnoInicial = turnoInicial;
    }

    public String getNuevoTurno() {
        return nuevoTurno;
    }

    public void setNuevoTurno(String nuevoTurno) {
        this.nuevoTurno = nuevoTurno;
    }

    public String getNuevaFecha() {
        return nuevaFecha;
    }

    public void setNuevaFecha(String nuevaFecha) {
        this.nuevaFecha = nuevaFecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}

