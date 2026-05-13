<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Waiting Room & Reception</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body style="align-items: flex-start;">

    <div class="glass-panel grid-2">
        <div>
            <h2>📝 Register Patient</h2>
            <form action="TriageController" method="POST">
                <input type="hidden" name="action" value="register">
                
                <div class="form-group">
                    <label>Document Number:</label>
                    <input type="text" name="document" required>
                </div>
                <div class="form-group">
                    <label>Full Name:</label>
                    <input type="text" name="name" required>
                </div>
                <div class="form-group">
                    <label>Age:</label>
                    <input type="number" name="age" required min="0">
                </div>
                <div class="form-group">
                    <label>Triage Level:</label>
                    <select name="triageLevel" required>
                        <option value="1">1 - Resuscitation (Immediate)</option>
                        <option value="2">2 - Emergent (15 mins)</option>
                        <option value="3">3 - Urgent (30 mins)</option>
                        <option value="4">4 - Less Urgent (1 hour)</option>
                        <option value="5">5 - Non Urgent (120 mins)</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Symptoms:</label>
                    <textarea name="symptoms" rows="3" required></textarea>
                </div>
                <button type="submit" class="btn btn-success">Register to Queue</button>
            </form>
        </div>

        <div>
            <h2>⏱️ Live Waiting Room</h2>
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Level</th>
                            <th>Patient Name</th>
                            <th>Status</th>
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
                                        <td><strong>Level ${turn.triageLevel}</strong></td>
                                        <td>${turn.patient.fullName}</td>
                                        <td>Waiting</td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
            <a href="index.jsp" class="btn" style="margin-top: 30px; background-color: #7f8c8d;">← Back to Menu</a>
        </div>
    </div>

</body>
</html>