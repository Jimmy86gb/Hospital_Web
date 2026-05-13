<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel del Médico</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>

    <div class="glass-panel" style="max-width: 600px;">
        <h1>👨‍⚕️ Panel del Especialista</h1>
        
        <a href="TriageController?action=nextPatient" class="btn btn-danger" style="margin-bottom: 30px;">Llamar al Siguiente Paciente</a>

        <c:if test="${not empty currentPatient}">
            <div class="glass-panel triage-${currentPatient.triageLevel}" style="padding: 25px; box-shadow: none; border: 1px solid rgba(0,0,0,0.1); border-radius: 12px;">
                <h2 style="margin-bottom: 15px; color: #2c3e50;">Paciente en Consulta</h2>
                <p><strong>Nombre:</strong> ${currentPatient.patient.fullName} (Edad: ${currentPatient.patient.age})</p>
                <p><strong>Documento:</strong> ${currentPatient.patient.documentNumber}</p>
                <p><strong>Clasificación:</strong> Nivel de Triage ${currentPatient.triageLevel}</p>
                <p style="margin-top: 10px;"><strong>Motivo de Consulta:</strong><br> ${currentPatient.symptoms}</p>
            </div>
        </c:if>

        <c:if test="${empty currentPatient && param.action == 'nextPatient'}">
            <div style="background: rgba(46, 204, 113, 0.2); border: 1px solid #2ecc71; padding: 15px; border-radius: 8px; text-align: center;">
                <p style="color: #27ae60; font-weight: bold; margin: 0;">¡Excelente! No hay pacientes en espera en este momento.</p>
            </div>
        </c:if>

        <hr>

        <h3 style="margin-bottom: 10px;">Historial de Sesión (Pila LIFO)</h3>
        <a href="TriageController?action=history" class="btn btn-secondary" style="background-color: #34495e; padding: 10px;">Verificar Último Paciente Atendido</a>
        
        <c:if test="${not empty lastAttended}">
            <div class="glass-panel" style="margin-top: 20px; padding: 20px; background: rgba(0,0,0,0.03); box-shadow: none; border: 1px dashed #bdc3c7;">
                <h4 style="margin-bottom: 10px;">Último Registro Procesado</h4>
                <p><strong>Paciente:</strong> ${lastAttended.patient.fullName}</p>
                <p><strong>Gravedad Inicial:</strong> Nivel ${lastAttended.triageLevel}</p>
                <p><strong>Estado en Sistema:</strong> Atendido</p>
            </div>
        </c:if>
        
        <a href="index.jsp" class="btn btn-secondary" style="margin-top: 30px;">← Volver al Menú Principal</a>
    </div>

</body>
</html>