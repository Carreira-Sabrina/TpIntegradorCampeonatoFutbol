package com.carreira.sabrina.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity(name ="Equipo")
@Table(name = "equipos")
public class Equipo extends EquipoAbstracto implements Serializable {

    public Equipo() {
    }

    //Al momento de la creacion, la cantidad de partidos jugados/ganados/empatados y puntos son 0
    public Equipo(String nombre, String directorTecnico, int cantTitulares, int cantSuplentes) {
        super(nombre, directorTecnico, cantTitulares, cantSuplentes);
    }


    @Override
    public void jugarPartido(TipoResultado resultado,EntityManager gestorEntidades) {
        this.setPartidosJugados(getPartidosJugados()+1);
        //TEST
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + this.getNombre() + " jugo " + this.getPartidosJugados()+ "partidos");
        switch (resultado){
            case GANO -> {
                this.setPartidosGanados((this.getPartidosGanados())+1);
                System.out.println("--- El equipo " + this.getNombre() + " GANó el partido \n");
                break;
            }
            case EMPATO -> {
                this.setPartidosEmpatados((this.getPartidosEmpatados())+1);
                System.out.println("--- El equipo " + getNombre() + "EMPATÓ el partido \n");
                break;
            }
            case PERDIO -> {
                System.out.println("--- El equipo " + getNombre() + " PERDIÓ el partido \n");
                break;
            }
        }
        //Guardar cambios en BD
        gestorEntidades.getTransaction().begin();
        gestorEntidades.merge(this);
        gestorEntidades.getTransaction().commit();
    }

    @Override
    public void asignarPuntos(EntityManager gestorEntidades) {
        //Se asignan 3 puntos por partido ganado, 1 por empatado y ninguno por perdido
        int puntosPartidoGanado = 3;
        this.setPuntos((this.getPartidosGanados()*puntosPartidoGanado) + this.getPartidosEmpatados());
        System.out.println(" @@@ El equipo " + this.getNombre().toUpperCase() + "alcanzó " + this.getPuntos() + " puntos ");
        gestorEntidades.getTransaction().begin();
        gestorEntidades.merge(this);
        gestorEntidades.getTransaction().commit();
    }



}
