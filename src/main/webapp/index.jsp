<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hospital Triage System</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f7f6; text-align: center; padding-top: 50px; }
        .menu-container { background: white; padding: 40px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); display: inline-block; }
        .btn { display: block; margin: 15px auto; padding: 15px 30px; background-color: #0056b3; color: white; text-decoration: none; border-radius: 5px; font-weight: bold; width: 200px; }
        .btn:hover { background-color: #004494; }
    </style>
</head>
<body>
    <div class="menu-container">
        <h1>🏥 Triage System</h1>
        <p>Select a module to access:</p>
        
        <a href="TriageController?action=waitingRoom" class="btn">Reception & Waiting Room</a>
        
        <a href="doctorPanel.jsp" class="btn">Doctor Panel</a>
    </div>
</body>
</html>