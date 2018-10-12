package net.subnano.kx;

public class KxSubscriberDemo {

    public static void main(String[] args) {
//        KxConnection kx = new DefaultKxConnection("kdb.uat.b2c2.net", 5001);
        KxConnection kx = new DefaultKxConnection("localhost", 5001);
        kx.connect();
        String table = "quote";
        System.out.printf("Subscribing to %s ..\n", table);
        KxTableReader tableReader = kx.subscribe(table);
        Object nextObject;
        while ((nextObject = tableReader.next()) != null) {
            System.out.println("Received: " + nextObject);
        }
        kx.close();
    }
}
