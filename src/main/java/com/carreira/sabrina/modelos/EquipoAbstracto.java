package com.carreira.sabrina.modelos;

import java.io.Serializable;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class EquipoAbstracto implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "director_tecnico")
    private String directorTecnico;
    @Column(name = "cant_titulares")
    private  int cantTitulares;
    @Column(name = "cant_suplentes")
    private int cantSuplentes;
    @Column(name = "puntos")
    private int puntos;
    @Column(name = "partidos_jugados")
    private int partidosJugados;
    @Column(name = "partidos_ganados")
    private int partidosGanados;
    @Column(name = "partidos_empatados")
    private int partidosEmpatados;

    public EquipoAbstracto(){

    }

    //Al momento de la creacion, la cantidad de partidos jugados/ganados/empatados y puntos son 0
    public EquipoAbstracto(String nombre, String directorTecnico, int cantTitulares, int cantSuplentes) {
        this.nombre = nombre;
        this.directorTecnico = directorTecnico;
        this.cantTitulares = cantTitulares;
        this.cantSuplentes = cantSuplentes;
    }

    public int getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirectorTecnico() {
        return directorTecnico;
    }

    public void setDirectorTecnico(String directorTecnico) {
        this.directorTecnico = directorTecnico;
    }

    public int getCantTitulares() {
        return cantTitulares;
    }

    public void setCantTitulares(int cantTitulares) {
        this.cantTitulares = cantTitulares;
    }

    public int getCantSuplentes() {
        return cantSuplentes;
    }

    public void setCantSuplentes(int cantSuplentes) {
        this.cantSuplentes = cantSuplentes;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public int getPartidosGanados() {
        return partidosGanados;
    }

    public void setPartidosGanados(int partidosGanados) {
        this.partidosGanados = partidosGanados;
    }

    public int getPartidosEmpatados() {
        return partidosEmpatados;
    }

    public void setPartidosEmpatados(int partidosEmpatados) {
        this.partidosEmpatados = partidosEmpatados;
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", directorTecnico='" + directorTecnico + '\'' +
                ", cantTitulares=" + cantTitulares +
                ", cantSuplentes=" + cantSuplentes +
                ", puntos=" + puntos +
                ", partidosJugados=" + partidosJugados +
                '}';
    }

    //Template method
    public final void participarDeCampeonato(TipoResultado conclusion, EntityManager gestorEntidades){
        jugarPartido(conclusion, gestorEntidades);
        asignarPuntos(gestorEntidades);
    }

    //Metodos de los pasos

    //Acá deberian escribirse los resultados en la tabla
    public abstract void asignarPuntos(EntityManager gestorEntidades);

    public abstract void jugarPartido(TipoResultado resultado,EntityManager gestorEntidades);

}