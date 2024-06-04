package com.example.shinyhunt_android.FireBase;

public class HuntPokemonDB {
    private String tempo;
    private String contagem;
    private String data;
    private String hora;

    private String statusHunt;

    public HuntPokemonDB(){

    }

    public HuntPokemonDB(String tempo, String contagem, String data, String hora,String statusHunt) {
        this.tempo = tempo;
        this.contagem = contagem;
        this.data = data;
        this.hora = hora;
        this.statusHunt = statusHunt;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getContagem() {
        return contagem;
    }

    public void setContagem(String contagem) {
        this.contagem = contagem;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getStatusHunt() {
        return statusHunt;
    }

    public void setStatusHunt(String statusHunt) {
        this.statusHunt = statusHunt;
    }
}
