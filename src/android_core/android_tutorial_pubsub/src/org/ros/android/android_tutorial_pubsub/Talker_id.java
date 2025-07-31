package org.ros.android.android_tutorial_pubsub;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;

import org.ros.node.topic.Publisher;

import static org.ros.android.android_tutorial_pubsub.MainActivity.statusid;

public class Talker_id extends AbstractNodeMain {

    private String topic_name;

    public Talker_id() { }

    public Talker_id(String topic) {
        this.topic_name = topic;
    }

    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava_tutorial_pubsub/talker");
    }


    public void onStart(ConnectedNode connectedNode) {

        final Publisher<std_msgs.String> publisher = connectedNode.newPublisher(this.topic_name, "std_msgs/String");
        connectedNode.executeCancellableLoop(new CancellableLoop() {

            std_msgs.String str = (std_msgs.String)publisher.newMessage();

            protected void loop() throws InterruptedException {

                MainActivity.cambioid(statusid);

                if(statusid == "Persona0") {
                    str.setData("0");
                }
                else if(statusid == "Persona1"){
                    str.setData("1");
                }
                publisher.publish(str);
                Thread.sleep(1000L);
            }
        });
    }
}
