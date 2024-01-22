/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dugo
 */
public class HiloCliente implements Runnable{

    private final DatagramSocket socket;
    private DatagramPacket receivePacket;
    private final List<InetSocketAddress> clients;
    
    /**
     * Constructor.
     * @param socket
     * @param receivePacket
     * @param clients 
     */
    public HiloCliente(DatagramSocket socket, DatagramPacket receivePacket, List<InetSocketAddress> clients) {
        this.socket = socket;
        this.receivePacket = receivePacket;
        this.clients = clients;
    }
              
    @Override
    public void run() {
        try {
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Mensaje recibido de " + receivePacket.getAddress() + ":" + receivePacket.getPort() + " - " + message);

            // Reenviar el mensaje a todos los clientes conectados
            byte[] sendData = message.getBytes();
            for (InetSocketAddress clientAddress : clients) {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress.getAddress(), clientAddress.getPort());
                socket.send(sendPacket);
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
