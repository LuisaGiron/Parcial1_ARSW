package edu.eci.arsw.blacklistvalidator;

public class Semaforo {
    private boolean bandera;

    private synchronized  boolean getBandera(){
        return bandera;
    }

    synchronized void setBandera(boolean bandera){
        this.bandera = bandera;
    }

}
