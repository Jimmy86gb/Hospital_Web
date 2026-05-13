<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Triage Hospitalario</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <div class="glass-panel" style="max-width: 500px;">
        <h1>🏥 Sistema de Triage</h1>
        <p style="text-align: center; margin-bottom: 30px; color: #7f8c8d;">Seleccione el módulo al que desea ingresar.</p>
        
        <a href="TriageController?action=waitingRoom" class="btn btn-primary">Recepción y Sala de Espera</a>
        <a href="doctorPanel.jsp" class="btn btn-danger">Consultorio del Especialista</a>
    </div>
</body>
</html>