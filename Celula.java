package com.mycompany.tela;

public class Celula {
    public boolean Mina;
    public boolean aberta;
    public int numero;

    public Celula() {
        this.Mina = false;
        this.aberta = false;
        this.numero = 0;
    }

    public boolean isMina() {
        return Mina;
    }

    public void setMina(boolean Mina) {
        this.Mina = Mina;
    }

    public boolean isAberta() {
        return aberta;
    }

    public void setAberta(boolean aberta) {
        this.aberta = aberta;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

}
