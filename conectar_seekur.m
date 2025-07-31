%Conexión con el ROS_MASTER_URI

clear all
close all
%conectar con Seekur_AP
setenv('ROS_MASTER_URI','http://192.168.0.100:11311'); %ros master en Jetson TX2
setenv('ROS_IP','192.168.0.102'); %primer ordenador remoto que se conecta

rosinit()
