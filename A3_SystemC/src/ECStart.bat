%ECHO OFF
%ECHO Starting ECS System
PAUSE
%ECHO Starting Temperature Controller Console
START "TEMPERATURE CONTROLLER CONSOLE" /MIN /NORMAL java DevicePackage/TemperatureController %1
%ECHO Starting Humidity Sensor Console
START "HUMIDITY CONTROLLER CONSOLE" /MIN /NORMAL java DevicePackage/HumidityController %1
START "TEMPERATURE SENSOR CONSOLE" /MIN /NORMAL java DevicePackage/TemperatureSensor %1
%ECHO Starting Humidity Sensor Console
START "HUMIDITY SENSOR CONSOLE" /MIN /NORMAL java DevicePackage/HumiditySensor %1
%ECHO ECS Monitoring Console
START "MUSEUM ENVIRONMENTAL CONTROL SYSTEM CONSOLE" /NORMAL java ECSConsole %1