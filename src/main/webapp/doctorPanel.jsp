<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Doctor's Panel</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>

    <div class="glass-panel" style="max-width: 600px;">
        <h1>👨‍⚕️ Specialist Panel</h1>
        
        <a href="TriageController?action=nextPatient" class="btn btn-danger" style="margin-bottom: 30px;">Call Next Patient</a>

        <c:if test="${not empty currentPatient}">
            <div class="glass-panel triage-${currentPatient.triageLevel}" style="padding: 20px; box-shadow: none; border: 1px solid #ddd;">
                <h2>Currently Attending</h2>
                <p><strong>Name:</strong> ${currentPatient.patient.fullName} (Age: ${currentPatient.patient.age})</p>
                <p><strong>Document:</strong> ${currentPatient.patient.documentNumber}</p>
                <p><strong>Triage Level:</strong> ${currentPatient.triageLevel}</p>
                <p><strong>Symptoms:</strong> ${currentPatient.symptoms}</p>
            </div>
        </c:if>

        <c:if test="${empty currentPatient && param.action == 'nextPatient'}">
            <p style="text-align: center; color: #27ae60; font-weight: bold;">No patients waiting. The queue is empty.</p>
        </c:if>

        <hr>

        <h3>Session History (LIFO)</h3>
        <a href="TriageController?action=history" class="btn" style="background-color: #34495e;">Verify Last Attended</a>
        
        <c:if test="${not empty lastAttended}">
            <div class="glass-panel" style="margin-top: 20px; padding: 20px; background: rgba(0,0,0,0.05); box-shadow: none;">
                <h4>Last Processed: ${lastAttended.patient.fullName}</h4>
                <p>Triage Level: ${lastAttended.triageLevel} | Status: ${lastAttended.status}</p>
            </div>
        </c:if>
        
        <a href="index.jsp" class="btn" style="margin-top: 30px; background-color: #7f8c8d;">← Back to Menu</a>
    </div>

</body>
</html>