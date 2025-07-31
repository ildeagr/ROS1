%Lectura de sensores
%%%%%%%%%%%%%%%%%%%%

% msg_sonar0 = sub_sonar0.LatestMessage.Range_;
 %msg_sonar1 = sub_sonar1.LatestMessage.Range_;
 %msg_sonar2 = sub_sonar2.LatestMessage.Range_;
 %msg_sonar3 = sub_sonar3.LatestMessage.Range_;
 %msg_sonar4 = sub_sonar4.LatestMessage.Range_;
 %msg_sonar5 = sub_sonar5.LatestMessage.Range_;
 %msg_sonar6 = sub_sonar6.LatestMessage.Range_;
 %msg_sonar7 = sub_sonar7.LatestMessage.Range_;
 msg_laser = sub_laser.LatestMessage;
 pos=sub_odom.LatestMessage.Pose.Pose.Position;
 
 %Mostramos lecturas del sonar en pantalla
 %disp(sprintf('\tSONARES_1-8: %f %f %f %f %f %f %f %f',msg_sonar0,msg_sonar1,msg_sonar2,msg_sonar3,msg_sonar4,msg_sonar5,msg_sonar6,msg_sonar7));
 disp(sprintf('\nPosicion actual: X=%f,   Y=%f',pos.X,pos.Y));
    
 %Representacion grafica de los datos del laser. 
 plot(msg_laser,'MaximumRange',20);

    