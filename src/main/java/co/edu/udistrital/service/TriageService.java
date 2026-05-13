package co.edu.udistrital.service;

import co.edu.udistrital.data.TurnRepository;
import co.edu.udistrital.model.MedicalTurn;
import co.edu.udistrital.model.Patient;
import java.sql.Timestamp;
import java.util.PriorityQueue;

/**
 * Capa de Servicio para operaciones del Triage.
 * Aplica SOLID aislando las reglas de negocio del Controlador. El Servlet no debe saber
 * cómo se guardan los datos, solo debe delegar esta responsabilidad al Servicio.
 */
public class TriageService {
    
    private final TurnRepository repository;

    /**
     * Inicializa el servicio conectándolo con la instancia Singleton del Repositorio.
     */
    public TriageService() {
        this.repository = TurnRepository.getInstance();
    }

    /**
     * Procesa los parámetros crudos de la web, crea las entidades y solicita su persistencia.
     *
     * @param document El número de cédula.
     * @param name     Nombre completo ingresado.
     * @param age      Edad del paciente.
     * @param level    Gravedad numérica evaluada en recepción.
     * @param symptoms Los síntomas descritos.
     */
    public void processNewRegistration(String document, String name, int age, int level, String symptoms) {
        Patient patient = new Patient(0, document, name, age);
        MedicalTurn turn = new MedicalTurn(0, patient, level, symptoms, new Timestamp(System.currentTimeMillis()));
        repository.saveTurn(turn);
    }

    /**
     * Extrae al paciente de mayor prioridad, actualiza su estado en base de datos
     * y lo ingresa a la Pila (Stack) de historial.
     *
     * @return El objeto MedicalTurn de mayor prioridad, o null si la cola está vacía.
     */
    public MedicalTurn callNextPatient() {
        PriorityQueue<MedicalTurn> queue = repository.getQueue();
        MedicalTurn nextTurn = queue.poll(); // Extrae el elemento raíz (Mayor prioridad)
        
        if (nextTurn != null) {
            nextTurn.setStatus("ATTENDED");
            repository.updateTurnStatus(nextTurn);
            repository.getHistory().push(nextTurn); // Inserta en la Pila LIFO
        }
        return nextTurn;
    }

    /**
     * Obtiene el último paciente atendido consultando la cima de la Pila.
     *
     * @return El objeto MedicalTurn más reciente del historial, o null si está vacía.
     */
    public MedicalTurn getLastAttended() {
        if (!repository.getHistory().isEmpty()) {
            return repository.getHistory().peek();
        }
        return null;
    }

    public PriorityQueue<MedicalTurn> getWaitingQueue() {
        return repository.getQueue();
    }
}