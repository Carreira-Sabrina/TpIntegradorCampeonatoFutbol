package com.carreira.sabrina.negocio;

import com.carreira.sabrina.modelos.Equipo;
import com.carreira.sabrina.modelos.TipoResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.*;

/* NOTA: EL SCRIPT DE LA BASE DE DATOS SE ENCUENTRA EN EL DIRECTORIO resources/BD de este proyecto */

public class TorneoFutbol {

    private static EntityManagerFactory fabricaGestorEntidades;
    private static EntityManager gestorEntidades;
    private static EntityTransaction gestorTransacciones;

    public static void main(String[] args) {
        //Preparaciòn persistencia
        fabricaGestorEntidades = Persistence.createEntityManagerFactory("unidadPersistenciaTorneo");
        gestorEntidades = fabricaGestorEntidades.createEntityManager();

        //Para el ingreso de datos por consola
        Scanner entradaDatos = new Scanner(System.in);

        //Imprime mensaje de bienvenida
        presentacionTorneo();

        //Da curso a la primera etapa: clasificaciòn automàtica (ver comentario en mètodo)
        primeraEtapa(gestorEntidades);

        //Se piden datos por consola para inscribir un nuevo equipo
        inscribirEquipo(entradaDatos,gestorEntidades);

        //Se realiza una baja, hay un equipo que ya no va a participar
        eliminarEquipo(gestorEntidades);

        //Los equipos están listos para empezar el torneo !
        //Se definen que equipos jugarán en la primera y segunda fecha
        //Cada elemento de la "fecha" contiene una array de dos elemetos, siemdo cada umo de estos
        //los equipos que se enfrentarán en un partido
        List<Equipo[]> equiposPorFecha = sorteoEquiposPorFecha(gestorEntidades);

        int numeroFecha = 1;

        //Se juegan los partidos !!!
        for(int i =numeroFecha; i<3; i++){
            fechaDelTorneo(i,equiposPorFecha,entradaDatos);
        }

        System.out.println("****                                                                                                        ****");
        System.out.println("****                                                                                                        ****");
        System.out.println("****           MUCHAS GRACIAS A TODOS POR PARTICIPAR DEL TORNEO ! HASTA EL AÑO QUE VIENE !                  ****");


        //Liberar recursos

        gestorEntidades.close();
        fabricaGestorEntidades.close();
        entradaDatos.close();
    }//main

    // =================== MÈTODOS AUXILIARES ============================================================
    public static void presentacionTorneo(){
        //Mensaje de bienvenida
        System.out.println("*****************************************************************************************************************");
        System.out.println("**** Bienvenidos al Sistema de Gestiòn de Torneos de Futbol de la Sociedad de Fomento Claridad de Ciudadela ****");
        System.out.println("****                                                                                                        ****");

    }

    public static void primeraEtapa(EntityManager gestorEntidades){
        //Se asume que el campeon y subcampeon del torneo del año anterior clasificaron automáticamente para el torneo de este año
        System.out.println("**** Las autoridades de esta instituciòn han decidido por votación que tanto el campeòn como el subcampeón  ****");
        System.out.println("**** del torneo anterior se clasifiquen automáticamente para el torneo de este año, razón por la cual ambos ****");
        System.out.println("**** equipos ya han sido inscriptos en el torneo. A continuación se presentan sus datos:                    ****");
        System.out.println("****                                                                                                        ****");

        //Se leen las entidades presentes en la tabla
        List<Equipo> listaEquipos = leerEquiposTabla(gestorEntidades);

        //Se seleccionan solo los dos primeros equipos de la tabla, que son el campeón y subcampeón del torneo anterior
        int cantEquiposEnTabla = listaEquipos.size();
        List<Equipo> equiposYaClasificados = listaEquipos.subList(0,2);

        //Imprimir por pantalla los datos de los equipos ya clasificados
        for(Equipo e:equiposYaClasificados){
            System.out.println(e);
        }
        System.out.println("**** Durante el día de hoy se han inscripto además, los siguientes equipos:                                 ****");
        System.out.println("****                                                                                                        ****");
        List<Equipo> equiposInscriptosHoy = listaEquipos.subList(2,cantEquiposEnTabla);
        for(Equipo e:equiposInscriptosHoy){
            System.out.println(e);
        }
        System.out.println("****                                                                                                        ****");
        System.out.println("****                                                                                                        ****");
    }//

    public static void inscribirEquipo(Scanner entradaDatos, EntityManager gestorEntidades){
        System.out.println("**** Falta solamente un equipo por inscribir, a continuaciòn se le pedirà que ingrese los datos:            ****");
        System.out.println("****                                                                                                        ****");
        String eleccion = "";
        String nombre = "";
        String directorTecnico;
        int cantTitulares;
        int cantSuplentes;
        int puntos;
        int partidosJugados;

        System.out.println("Se le pediran los datos para crear un equipo");
        do {
            System.out.println("Ingrese nombre del equipo a inscribir: ");
            nombre = entradaDatos.nextLine();
            System.out.println("Ingrese el nombre del Director Técnico: ");
            directorTecnico = entradaDatos.nextLine();
            cantTitulares = 11;
            System.out.println("Ingrese Cantidad de Supenlentes: ");
            cantSuplentes = entradaDatos.nextInt();
            entradaDatos.nextLine();
            puntos = 0;
            partidosJugados = 0;

            System.out.println("El equipo se llama " + nombre + " el DT es " +
                    directorTecnico + " y la cantidad de suplentes es " + cantSuplentes );

            System.out.println("Es correcto? si/no");
            eleccion = entradaDatos.nextLine();
            System.out.println("La opcion elegida fue: " + eleccion);

        }while (eleccion.equals("no"));

        //Se crea una instancia de equipo con los datos que se ingresaron
        Equipo equipo = new Equipo();
        equipo.setNombre(nombre);
        equipo.setDirectorTecnico(directorTecnico);
        equipo.setCantTitulares(cantTitulares);
        equipo.setCantSuplentes(cantSuplentes);

        //Se persiste el nuevo equipo en la base de datos
        gestorEntidades.getTransaction().begin();
        gestorEntidades.persist(equipo);
        gestorEntidades.getTransaction().commit(); //guarda cambios en BD

    }//

    public static void eliminarEquipo(EntityManager gestorEntidades){
        //Se asume que sucede algun evento fortuito que causa que un equipo se retire del campeonato
        //El equipo es elegido mediante Random

        System.out.println("**** Lamentamos informar que debido a causas de fuerza mayor, uno de los equipos registrados no puede       ****");
        System.out.println("**** participar en el campeonato.                                                                           ****");

        Random aleatorio = new Random();

        //Se obtienen los equipos que hay actualmente mediante el método auxiliar
        List<Equipo> listaEquipos = leerEquiposTabla(gestorEntidades);

        int numAleatorio = aleatorio.nextInt(1,listaEquipos.size());
        //Buscar el elemento elegido aleatoriamente
        Equipo equipoAEliminar = gestorEntidades.find(Equipo.class,numAleatorio);
        System.out.println("**** Se trata del equipo " + equipoAEliminar.getNombre() + "           ****");
        gestorEntidades.getTransaction().begin();
        gestorEntidades.remove(equipoAEliminar);
        gestorEntidades.getTransaction().commit();
        System.out.println("**** Les deseamos a " + equipoAEliminar.getNombre() + " mucha suerte para el año próximo ! ****");
        System.out.println("****                                                                                                        ****");
    }

    public static  List<Equipo> leerEquiposTabla(EntityManager gestorEntidades){
        //Devuelve los equipos que existen en la tabla
        List<Equipo> listaEquipos = (List<Equipo>)gestorEntidades.createQuery("FROM Equipo").getResultList();

        return listaEquipos;
    }//

    public static List<Equipo[]> sorteoEquiposPorFecha(EntityManager gestorEntidades){
        //Para simplificar se eligen las fechas "a mano"
        //Se leen los equipos que estàn en la tabla
        List<Equipo> equipos = leerEquiposTabla(gestorEntidades);

        Equipo[] equiposPrimeraFecha = {equipos.get(3),equipos.get(1)};

        Equipo[] equiposSegundaFecha = {equipos.get(0),equipos.get(2)};

        //Se devuelve un List de arrays de Equipos
        List<Equipo[]> partidosPorFecha = new ArrayList<>();
        partidosPorFecha.add(equiposPrimeraFecha);
        partidosPorFecha.add(equiposSegundaFecha);

        return partidosPorFecha;

    }//

    public static void fechaDelTorneo(int numeroDeFecha,List<Equipo[]>equiposPorFecha, Scanner entradaDatos){
        Equipo equipo1 = equiposPorFecha.get(numeroDeFecha -1)[0];
        Equipo equipo2 = equiposPorFecha.get(numeroDeFecha -1)[1];


        System.out.println(" HOY SE JUEGA LA FECHA NÙMERO " + numeroDeFecha + " DEL CAMPEONATO !");
        System.out.println(" SE ENFRENTAN " + equipo1.getNombre().toUpperCase() + " VS " +
                            equipo2.getNombre().toUpperCase()) ;
        //Se obtienen los goles de cada equipo
        int golesEquipo1 = anotarGoles(entradaDatos,equipo1);
        int golesEquipo2 = anotarGoles(entradaDatos,equipo2);

        //Estas variables se pasan a las instacias de los equipos para que calculen sus puntos
        TipoResultado resultadoEq1 = compararCantGoles(golesEquipo1,golesEquipo2);
        TipoResultado resultadoEq2 = compararCantGoles(golesEquipo2,golesEquipo1);

        //Se llama al template method de las instancias de equipo
        equipo1.participarDeCampeonato(resultadoEq1,gestorEntidades );
        equipo2.participarDeCampeonato(resultadoEq2, gestorEntidades);

    }//

    public static int anotarGoles(Scanner entradaDatos, Equipo equipo){
        //Devuelve la cantidad de goles que el equipo hizo en el partido
        int golesEsteEquipo = 0;
        System.out.println("Por favor, ingrese cuantos goles anotó " + equipo.getNombre());
        while(!entradaDatos.hasNextInt()){
            entradaDatos.next();
            System.out.println("Por favor ingrese sólo nùmeros enteros");
        }
        golesEsteEquipo+= entradaDatos.nextInt();

        System.out.println(" ---- El equipo " + equipo.getNombre().toUpperCase() + " marcó " + golesEsteEquipo + " goles " );
        System.out.println();

        return golesEsteEquipo;
    }

    public static TipoResultado compararCantGoles(int golesEq1, int golesEq2){
        //Devuelve el resultado GANO, PERDIO,EMPATO del enum
        TipoResultado resultado;
        if(golesEq1 > golesEq2){
            resultado = TipoResultado.GANO;
        } else if (golesEq1 < golesEq2) {
            resultado = TipoResultado.PERDIO;
        }else {
            resultado = TipoResultado.EMPATO;
        }
        return resultado;
    }

}