<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Doctor's Panel</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #e9ecef; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        .call-btn { background-color: #dc3545; color: white; font-size: 18px; padding: 15px 30px; border: none; border-radius: 5px; cursor: pointer; display: block; margin: 0 auto 20px; font-weight: bold; text-decoration: none; text-align: center; }
        .call-btn:hover { background-color: #c82333; }
        .patient-card { background: #f8f9fa; border-left: 5px solid #0056b3; padding: 20px; margin-bottom: 20px; border-radius: 4px; }
        .history-card { background: #e2e3e5; border-left: 5px solid #6c757d; padding: 15px; border-radius: 4px; }
    </style>
</head>
<body>

    <div class="container">
        <h1 style="text-align: center;">👨‍⚕️ Specialist Panel</h1>
        
        <a href="TriageController?action=nextPatient" class="call-btn">Call Next Patient</a>

        <c:if test="${not empty currentPatient}">
            <div class="patient-card">
                <h2>Currently Attending</h2>
                <p><strong>Name:</strong> ${currentPatient.patient.fullName} (Age: ${currentPatient.patient.age})</p>
                <p><strong>Document:</strong> ${currentPatient.patient.documentNumber}</p>
                <p><strong>Triage Level:</strong> ${currentPatient.triageLevel}</p>
                <p><strong>Symptoms:</strong> ${currentPatient.symptoms}</p>
                <p><strong>Arrival:</strong> ${currentPatient.arrivalTime}</p>
            </div>
        </c:if>

        <c:if test="${empty currentPatient && param.action == 'nextPatient'}">
            <p style="text-align: center; color: green; font-weight: bold;">No patients waiting. The queue is empty.</p>
        </c:if>

        <hr style="margin: 30px 0;">

        <h3>Session History (LIFO Stack)</h3>
        <a href="TriageController?action=history" style="color: #0056b3;">Verify Last Attended Patient</a>
        
        <c:if test="${not empty lastAttended}">
            <div class="history-card" style="margin-top: 15px;">
                <h4>Last Patient Processed</h4>
                <p>${lastAttended.patient.fullName} - Triage Level ${lastAttended.triageLevel}</p>
                <p>Status: ${lastAttended.status}</p>
            </div>
        </c:if>
        
        <br><br>
        <a href="index.jsp" style="color: #0056b3;">← Back to Menu</a>
    </div>

</body>
</html>