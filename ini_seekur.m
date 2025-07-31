%Establecer conexiones de los subscribers y publishers del robot
%DECLARACION DE SUBSCRIBERS
%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Odometria
sub_odom=rossubscriber('/pose');
%Laser
sub_laser = rossubscriber('/scan', rostype.sensor_msgs_LaserScan);
%Sonars
%realsense
sub_person=rossubscriber('/person_tracking_output');
%status
sub_status=rossubscriber('/status');




%DECLARACION DE PUBLISHERS
%%%%%%%%%%%%%%%%%%%%%%%%%%
%Velocidad
pub_vel=rospublisher('/cmd_vel','geometry_msgs/Twist');
pub_enable=rospublisher('/cmd_motor_state','std_msgs/Int32');

%GENERACION DE MENSAJES
%%%%%%%%%%%%%%%%%%%%%%%
msg_vel=rosmessage(pub_vel);
msg_enable_motor=rosmessage(pub_enable);


msg_laser=rosmessage(sub_laser);
msg_laser = sub_laser.LatestMessage;

msg_status=sub_status.LatestMessage;




%figure(fig_laser);
 

%CREAMOS UN OBJETO VFH
%%%%%%%%%%%%%%%%%%%%%%
 VFH=robotics.VectorFieldHistogram;
% %Ajustamos sus propiedades
% %%%%%%%%%%%%%%%%%%%%%%%%%%
 VFH.NumAngularSectors=180;
VFH.DistanceLimits=[0.7 2.5];
VFH.RobotRadius=0.70;
VFH.SafetyDistance=0.30;
VFH.MinTurningRadius=0.2;
VFH.TargetDirectionWeight=3;
VFH.CurrentDirectionWeight=1;
VFH.PreviousDirectionWeight=1;
VFH.HistogramThresholds=[0.8 2];
targetDir = 0;

 
% 
% 
% %Figuras distintas para el l�ser y el VFH
% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%fig_laser=figure();
fig_vfh=figure();
% 
% %Definimos la periodicidad del bucle
% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
r=robotics.Rate(10);
% 
% %Nos aseguramos de recibir un mensaje relacionado con el robot
% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% while (strcmp(sub_odom.LatestMessage.ChildFrameId,'robot0')~=1)
%      sub_odom.LatestMessage
% end
% 

%Activar motores enviando enable_motor = 1
msg_enable_motor.Data=1;
send(pub_enable,msg_enable_motor);

%mensaje persona
msg_person=sub_person.LatestMessage;

%identificacion usuario a seguir
id_a_seguir = 0;
parar=true;

disp('Inicializacion finalizada correctamente');
