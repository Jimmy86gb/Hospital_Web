package co.edu.udistrital.service;

import co.edu.udistrital.data.TurnRepository;
import co.edu.udistrital.model.MedicalTurn;
import co.edu.udistrital.model.Patient;
import java.sql.Timestamp;
import java.util.PriorityQueue;

/**
 * Service Layer for Triage Operations.
 * Enforces business rules and abstracts data operations from the Controller.
 */
public class TriageService {
    
    private final TurnRepository repository;

    public TriageService() {
        this.repository = TurnRepository.getInstance();
    }

    /**
     * Processes raw input data to create entities and save them.
     */
    public void processNewRegistration(String document, String name, int age, int level, String symptoms) {
        Patient patient = new Patient(0, document, name, age);
        MedicalTurn turn = new MedicalTurn(0, patient, level, symptoms, new Timestamp(System.currentTimeMillis()));
        repository.saveTurn(turn);
    }

    /**
     * Polls the next patient based on priority and logs them into history.
     *
     * @return MedicalTurn of the next patient, or null if queue is empty.
     */
    public MedicalTurn callNextPatient() {
        PriorityQueue<MedicalTurn> queue = repository.getQueue();
        MedicalTurn nextTurn = queue.poll();
        
        if (nextTurn != null) {
            nextTurn.setStatus("ATTENDED");
            repository.updateTurnStatus(nextTurn);
            repository.getHistory().push(nextTurn);
        }
        return nextTurn;
    }

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