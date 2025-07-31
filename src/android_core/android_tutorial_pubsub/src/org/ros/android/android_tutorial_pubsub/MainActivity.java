/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.android.android_tutorial_pubsub;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import android.widget.Toast;
import org.ros.rosjava_tutorial_pubsub.Talker;

import std_msgs.String;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MainActivity extends RosActivity {

    public static java.lang.String statusdoor;
    public static java.lang.String statusaccion;
    public static java.lang.String statusid;
    public static java.lang.String Door = "DOOR_CLOSED";
    public static java.lang.String Accion = "STOP";
    public static java.lang.String Identificador = "Persona";
    public static java.lang.String cerradura;
    public static Button Sigueme;
    private Button Stop;
    private Button Persona0;
    private Button Persona1;
    public static ImageButton imagen;
    public static ImageView imagen2;
    public static ImageView imagen3;


    //VARIABLES PUBLISHERS AND SUBSCRIBERS
    private RosTextView<std_msgs.String> rosTextViewstatus;
    public static RosTextView<std_msgs.String> rosTextViewdoor;
    private Talker_accion talker_accion;
    private Talker_door talker_door;
    private Talker_id talker_id;

    public MainActivity() {
    // The RosActivity constructor configures the notification title and ticker
    // messages.
    super("Pubsub Tutorial", "Pubsub Tutorial"); //Pubsub Tutorial
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    imagen=(ImageButton) findViewById(R.id.imageButton2);
    imagen2=(ImageView) findViewById(R.id.semaforoView);
    imagen3=(ImageView) findViewById(R.id.peatonView);

    Sigueme =(Button)findViewById(R.id.button3);
    Stop =(Button)findViewById(R.id.button4);
    Persona0 =(Button)findViewById(R.id.button5);
    Persona1 =(Button)findViewById(R.id.button6);

    //Create Subscriber
    rosTextViewstatus = (RosTextView<std_msgs.String>) findViewById(R.id.text3);
    rosTextViewstatus.setTopicName("status");
    rosTextViewstatus.setMessageType(std_msgs.String._TYPE);
    rosTextViewstatus.setMessageToStringCallable(new MessageCallable<java.lang.String,String>() {
      @Override
      public java.lang.String call(std_msgs.String message) {
        return message.getData();
      }
    });

    rosTextViewdoor = (RosTextView<std_msgs.String>) findViewById(R.id.text);
    rosTextViewdoor.setTopicName("pub_cerradura");
    rosTextViewdoor.setMessageType(std_msgs.String._TYPE);
    rosTextViewdoor.setMessageToStringCallable(new MessageCallable<java.lang.String,String>() {

        @Override
        public java.lang.String call(std_msgs.String message) {
            cerradura= message.getData();
         return message.getData();

        }
    });

    //Deshabilito botones
    Stop.setEnabled(false);

  }

    @Override
  protected void init(NodeMainExecutor nodeMainExecutor){

      talker_accion = new Talker_accion("status");
      talker_door = new Talker_door("door");
      talker_id = new Talker_id("idcartero");

      //PUBLISHERS
      NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
      nodeConfiguration.setMasterUri(getMasterUri());
      nodeConfiguration.setNodeName("status");
      nodeMainExecutor.execute(talker_accion, nodeConfiguration);

      NodeConfiguration nodeConfiguration2 = NodeConfiguration.newPublic(getRosHostname());
      nodeConfiguration2.setMasterUri(getMasterUri());
      nodeConfiguration2.setNodeName("door");
      nodeMainExecutor.execute(talker_door, nodeConfiguration2);

      NodeConfiguration nodeConfiguration3 = NodeConfiguration.newPublic(getRosHostname());
      nodeConfiguration3.setMasterUri(getMasterUri());
      nodeConfiguration3.setNodeName("idcartero");
      nodeMainExecutor.execute(talker_id, nodeConfiguration3);


      // The RosTextView is also a NodeMain that must be executed in order to
      // start displaying incoming messages.
      //READING THE STATUS ROBOT
      NodeConfiguration nodeConfiguration4 = NodeConfiguration.newPublic(getRosHostname());
      nodeConfiguration4.setMasterUri(getMasterUri());
      nodeConfiguration4.setNodeName("sub_status");
      nodeMainExecutor.execute(rosTextViewstatus, nodeConfiguration4);

      //READING THE STATUS DOOR
      NodeConfiguration nodeConfiguration5 = NodeConfiguration.newPublic(getRosHostname());
      nodeConfiguration5.setMasterUri(getMasterUri());
      nodeConfiguration5.setNodeName("sub_door");
      nodeMainExecutor.execute(rosTextViewdoor, nodeConfiguration5);
    }

    public static java.lang.String cambiodoor(java.lang.String statusdoor) {
      MainActivity.statusdoor = Door;
      return MainActivity.statusdoor;
    }

    public static java.lang.String cambioaccion(java.lang.String statusaccion) {
        MainActivity.statusaccion = Accion;
        return MainActivity.statusaccion;
    }

    public static java.lang.String cambioid(java.lang.String statusid) {
        MainActivity.statusid = Accion;
        return MainActivity.statusid;
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageButton2: //Boton apertura

                Door = "DOOR_OPEN";
                //imagen.setImageResource(R.drawable.unlock);
                Toast.makeText(MainActivity.this, "Puerta abierta", Toast.LENGTH_SHORT).show();

                break;
            case R.id.button3: //Boton seguimiento

                if(cerradura.equals("1")){
                    Accion = "SIGUEME";
                    Sigueme.setEnabled(false);
                    Stop.setEnabled(true);
                    imagen.setEnabled(false);//Dehabilita apertura puerta
                    imagen2.setImageResource(R.drawable.amarillo);
                    imagen3.setImageResource(R.drawable.peaton1);
                    Toast.makeText(MainActivity.this, "Robot Repartiendo", Toast.LENGTH_SHORT).show();
                }

                else if(cerradura.equals("0")){
                    Toast.makeText(MainActivity.this, "Puerta abierta. Cierre antes de iniciar reparto", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.button4: //Boton parada

                Accion = "STOP";
                Sigueme.setEnabled(true);
                Stop.setEnabled(false);
                imagen.setEnabled(true);//Habilita apertura puerta
                imagen2.setImageResource(R.drawable.verde);
                imagen3.setImageResource(R.drawable.peaton0);
                Toast.makeText(MainActivity.this, "Robot en espera", Toast.LENGTH_SHORT).show();


                break;
            case R.id.button5: //Botones identificacion

                Identificador = "Persona0";

                break;

            case R.id.button6:

                Identificador = "Persona1";

                break;

            default:
                break;
        }
    }

}

