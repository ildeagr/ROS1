%identificacion usuario a seguir
id_a_seguir = 0;
msg_status=sub_status.LatestMessage;
if (strcmp(msg_status.Data,'0'))
    comando=0;
else
    comando=1;
end
msg_person=sub_person.LatestMessage;
if (msg_person.NumberOfUsers > 0)
    for usuario = 1:1:msg_person.NumberOfUsers
        %avanzar con el VFH
        if (msg_person.UsersData(usuario).UserInfo.Id == id_a_seguir)
            x_objetivo=msg_person.UsersData(usuario).CenterOfMassWorld.X
            z_objetivo=msg_person.UsersData(usuario).CenterOfMassWorld.Z
            % targetDir=0;
            targetDir=-1.0*atan(x_objetivo/z_objetivo);
            if (z_objetivo > 2.5)
                disp('Persona identificada: SIGUIENDO');
                parar=false;
            else
                disp('Persona identificada: ALCANZADA-PARADO');
                parar=true;
            end
        end
    end
else
    parar=true;
end

%Bucle de control infinito
while(1)
    msg_status=sub_status.LatestMessage;
    if (strcmp(msg_status.Data,'0'))
        comando=0;
    else
        comando=1;
    end
    if (comando==1)
        msg_person=sub_person.LatestMessage;
        if (msg_person.NumberOfUsers > 0)
            for usuario = 1:1:msg_person.NumberOfUsers
                %avanzar con el VFH
                if (msg_person.UsersData(usuario).UserInfo.Id == id_a_seguir)
                    disp('Persona identificada: SIGUIENDO');
                    x_objetivo=msg_person.UsersData(usuario).CenterOfMassWorld.X
                    z_objetivo=msg_person.UsersData(usuario).CenterOfMassWorld.Z
                    % targetDir=0;
                    targetDir=-1.0*atan(x_objetivo/z_objetivo);
                    if (z_objetivo > 2.5)
                        disp('Persona identificada: SIGUIENDO');
                        parar=false;
                    else
                        disp('Persona identificada: ALCANZADA-PARADO');
                        parar=true;
                    end
                end
            end
        else
            parar=true;
        end

        if (parar==false)
            % Get laser scan data
            laserScan = receive(sub_laser);
            ranges = double(laserScan.Ranges(150:400,1));
            angles = double(laserScan.readScanAngles);
            % Call VFH object to computer steering direction
            steerDir = step(VFH, ranges, angles(150:400,1), double(targetDir));  
            show(VFH);
            % Calculate velocities
            if ~isnan(steerDir) % If steering direction is valid
                desiredV = 0.5;
                w = exampleHelperComputeAngularVelocity(steerDir, 0.2);
            else % Stop and search for valid direction
                desiredV = 0.0;
                w = 0.1;
            end
        else
            desiredV = 0.0;
            w=0.0;
            disp('Persona no identificada: PARADO');
        end
        % Assign and send velocity commands
        msg_vel.Linear.X = desiredV;
        msg_vel.Angular.Z = w;
        %velPub.send(velMsg);
        send(pub_vel,msg_vel);
    elseif (comando==0)
        disp('Robot en estado parado');
        % Assign and send velocity commands
        msg_vel.Linear.X = 0;
        msg_vel.Angular.Z = 0;
        %velPub.send(velMsg);
        send(pub_vel,msg_vel);
    end
    waitfor(r);
    
end

