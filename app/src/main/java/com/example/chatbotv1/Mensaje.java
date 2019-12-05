package com.example.chatbotv1;

public class Mensaje {

    private String mensaje;
    private boolean origen;

    public Mensaje(String mensaje, boolean origen) {
        this.mensaje = mensaje;
        this.origen = origen;
    }

    public Mensaje() {
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean getOrigen() {
        return origen;
    }

    public void setOrigen(boolean origen) {
        this.origen = origen;
    }
}
