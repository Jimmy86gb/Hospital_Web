package co.edu.udistrital.controller;

import co.edu.udistrital.service.TriageService;
import co.edu.udistrital.model.MedicalTurn;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet Front Controller (Controlador Frontal).
 * Se encarga exclusivamente de recibir peticiones HTTP, delegar el trabajo lógico 
 * a la capa de Servicio, y redirigir al usuario a la vista JSP correspondiente.
 */
@WebServlet(name = "TriageController", urlPatterns = {"/TriageController"})
public class TriageController extends HttpServlet {

    private TriageService service;

    /**
     * Ciclo de vida del Servlet: Inicializa la capa de servicio cuando el servidor arranca.
     */
    @Override
    public void init() throws ServletException {
        this.service = new TriageService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        routeRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        routeRequest(request, response);
    }

    /**
     * Enrutador central. Usa el patrón de parámetro 'action' para evitar crear 
     * múltiples archivos Servlet.
     */
    private void routeRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Define la codificación para leer caracteres especiales en español
        request.setCharacterEncoding("UTF-8"); 
        
        String action = request.getParameter("action");
        if (action == null) action = "waitingRoom";

        switch (action) {
            case "register":
                handleRegistration(request, response);
                break;
            case "nextPatient":
                handleNextPatient(request, response);
                break;
            case "waitingRoom":
                handleWaitingRoomView(request, response);
                break;
            case "history":
                handleHistoryView(request, response);
                break;
            default:
                handleWaitingRoomView(request, response);
                break;
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String doc = request.getParameter("document");
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        int level = Integer.parseInt(request.getParameter("triageLevel"));
        String symptoms = request.getParameter("symptoms");

        service.processNewRegistration(doc, name, age, level, symptoms);
        
        // Redirección POST-Get para evitar duplicación de datos al recargar la página
        response.sendRedirect("TriageController?action=waitingRoom");
    }

    private void handleNextPatient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MedicalTurn current = service.callNextPatient();
        request.setAttribute("currentPatient", current);
        request.getRequestDispatcher("doctorPanel.jsp").forward(request, response);
    }

    private void handleWaitingRoomView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("queue", service.getWaitingQueue());
        request.getRequestDispatcher("waitingRoom.jsp").forward(request, response);
    }
    
    private void handleHistoryView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("lastAttended", service.getLastAttended());
        request.getRequestDispatcher("doctorPanel.jsp").forward(request, response);
    }
}