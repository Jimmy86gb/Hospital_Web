<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Recepción y Sala de Espera</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body style="align-items: flex-start; padding-top: 40px;">

    <div class="glass-panel grid-2">
        <div>
            <h2>📝 Registrar Paciente</h2>
            <form action="TriageController" method="POST">
                <input type="hidden" name="action" value="register">
                
                <div class="form-group">
                    <label>Número de Documento:</label>
                    <input type="text" name="document" placeholder="Ej. 1002345678" required>
                </div>
                <div class="form-group">
                    <label>Nombre Completo:</label>
                    <input type="text" name="name" placeholder="Nombres y Apellidos" required>
                </div>
                <div class="form-group">
                    <label>Edad:</label>
                    <input type="number" name="age" required min="0">
                </div>
                <div class="form-group">
                    <label>Nivel de Triage (Clasificación):</label>
                    <select name="triageLevel" required>
                        <option value="1">Nivel 1 - Reanimación (Inmediata)</option>
                        <option value="2">Nivel 2 - Emergencia (15 min)</option>
                        <option value="3">Nivel 3 - Urgencia (30 min)</option>
                        <option value="4">Nivel 4 - Prioridad Menor (60 min)</option>
                        <option value="5">Nivel 5 - No Urgente (120 min)</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Síntomas / Motivo de consulta:</label>
                    <textarea name="symptoms" rows="3" required></textarea>
                </div>
                <button type="submit" class="btn btn-success">Registrar en la Cola</button>
            </form>
        </div>

        <div>
            <h2>⏱️ Sala de Espera en Vivo</h2>
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Gravedad</th>
                            <th>Paciente</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty queue}">
                                <tr><td colspan="3" style="text-align: center; color: #7f8c8d;">No hay pacientes en espera.</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="turn" items="${queue}">
                                    <tr class="triage-${turn.triageLevel}">
                                        <td><strong>Nivel ${turn.triageLevel}</strong></td>
                                        <td>${turn.patient.fullName}</td>
                                        <td style="color: #e67e22; font-weight: bold;">En Espera</td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
            <a href="index.jsp" class="btn btn-secondary" style="margin-top: 30px;">← Volver al Menú Principal</a>
        </div>
    </div>

</body>
</html>