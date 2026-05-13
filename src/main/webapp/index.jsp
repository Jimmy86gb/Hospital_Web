<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hospital Triage System</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <div class="glass-panel" style="max-width: 500px;">
        <h1>🏥 Triage System</h1>
        <p style="text-align: center; margin-bottom: 30px;">Select a module to access the system.</p>
        <a href="TriageController?action=waitingRoom" class="btn">Reception & Waiting Room</a>
        <a href="doctorPanel.jsp" class="btn btn-danger">Doctor's Panel</a>
    </div>
</body>
</html>