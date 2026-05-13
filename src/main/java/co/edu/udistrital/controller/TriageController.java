package co.edu.udistrital.controller;

import co.edu.udistrital.data.TurnRepository;
import co.edu.udistrital.model.MedicalTurn;
import co.edu.udistrital.model.Patient;

import java.io.IOException;
import java.sql.Timestamp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Main Servlet controller managing the triage system requests.
 */
@WebServlet(name = "TriageController", urlPatterns = {"/TriageController"})
public class TriageController extends HttpServlet {

    private TurnRepository repository;

    @Override
    public void init() throws ServletException {
        // Initialize the Singleton repository so it persists in Tomcat memory
        repository = TurnRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Processes requests for both HTTP GET and POST methods.
     * Uses the "action" parameter routing pattern.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "waitingRoom"; // Default view
        }

        switch (action) {
            case "register":
                registerPatient(request, response);
                break;
            case "nextPatient":
                callNextPatient(request, response);
                break;
            case "waitingRoom":
                showWaitingRoom(request, response);
                break;
            case "history":
                showHistory(request, response);
                break;
            default:
                showWaitingRoom(request, response);
                break;
        }
    }

    private void registerPatient(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve form data
        String doc = request.getParameter("document");
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        int triageLevel = Integer.parseInt(request.getParameter("triageLevel"));
        String symptoms = request.getParameter("symptoms");

        // Create models (In real app, save Patient to DB first to get generated ID)
        Patient patient = new Patient(1, doc, name, age);
        MedicalTurn turn = new MedicalTurn(1, patient, triageLevel, symptoms, new Timestamp(System.currentTimeMillis()));

        // Add to priority queue and DB
        repository.registerTurn(turn);

        // Redirect to prevent form resubmission
        response.sendRedirect("TriageController?action=waitingRoom");
    }

    private void callNextPatient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MedicalTurn nextTurn = repository.getNextPatient();
        request.setAttribute("currentPatient", nextTurn);
        request.getRequestDispatcher("doctorPanel.jsp").forward(request, response);
    }

    private void showWaitingRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Send the queue to the JSP for real-time visualization
        request.setAttribute("queue", repository.getTurnQueue());
        request.getRequestDispatcher("waitingRoom.jsp").forward(request, response);
    }
    
    private void showHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get last attended using the Stack
        request.setAttribute("lastAttended", repository.getLastAttendedPatient());
        request.getRequestDispatcher("historyPanel.jsp").forward(request, response);
    }
}