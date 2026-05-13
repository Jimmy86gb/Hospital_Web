<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Waiting Room & Reception</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f6; padding: 20px; display: flex; gap: 20px; }
        .panel { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 50%; }
        input, select, textarea { width: 100%; padding: 8px; margin: 8px 0; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        button { background-color: #28a745; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; width: 100%; }
        button:hover { background-color: #218838; }
        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #0056b3; color: white; }
        .triage-1 { background-color: #ffcccc; color: #cc0000; font-weight: bold; } /* Critical */
        .triage-5 { background-color: #ccffcc; color: #006600; } /* Non-urgent */
    </style>
</head>
<body>

    <div class="panel">
        <h2>📝 Register Patient</h2>
        <form action="TriageController" method="POST">
            <input type="hidden" name="action" value="register">
            
            <label>Document Number:</label>
            <input type="text" name="document" required>
            
            <label>Full Name:</label>
            <input type="text" name="name" required>
            
            <label>Age:</label>
            <input type="number" name="age" required>
            
            <label>Triage Level (1 = Critical, 5 = Non-urgent):</label>
            <select name="triageLevel" required>
                <option value="1">1 - Resuscitation (Immediate)</option>
                <option value="2">2 - Emergent (15 mins)</option>
                <option value="3">3 - Urgent (30 mins)</option>
                <option value="4">4 - Less Urgent (1 hour)</option>
                <option value="5">5 - Non Urgent (120 mins)</option>
            </select>
            
            <label>Symptoms:</label>
            <textarea name="symptoms" rows="3" required></textarea>
            
            <button type="submit">Register to Queue</button>
        </form>
    </div>

    <div class="panel">
        <h2>⏱️ Live Waiting Room</h2>
        <table>
            <thead>
                <tr>
                    <th>Priority (Level)</th>
                    <th>Patient Name</th>
                    <th>Arrival Time</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty queue}">
                        <tr><td colspan="3" style="text-align: center;">No patients in queue.</td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="turn" items="${queue}">
                            <tr class="triage-${turn.triageLevel}">
                                <td>Level ${turn.triageLevel}</td>
                                <td>${turn.patient.fullName}</td>
                                <td>${turn.arrivalTime}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
        <br>
        <a href="index.jsp" style="color: #0056b3;">← Back to Menu</a>
    </div>

</body>
</html>