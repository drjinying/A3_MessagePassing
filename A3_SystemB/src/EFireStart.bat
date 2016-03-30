%ECHO OFF
%ECHO Starting FireSystem
PAUSE
%ECHO Starting Fire Controller Console
START "FIRE CONTROLLER CONSOLE" /MIN /NORMAL java FireController %1
%ECHO Starting Fire Controller Console
START "SPRINKLER CONTROLLER CONSOLE" /MIN /NORMAL java SprinklerController %1
%ECHO Starting Fire Sensor Console
START "FIRE SENSOR CONSOLE" /MIN /NORMAL java FireSensor %1
%ECHO Security Monitoring Console
START "MUSEUM SECURITY CONTROL SYSTEM CONSOLE" /NORMAL java SecurityConsole %1
%ECHO Security Simulation Console
START "MUSEUM SECURITY SIMULATION SYSTEM CONSOLE" /NORMAL java SecuritySimulationConsole %1