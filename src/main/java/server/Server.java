/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dugo
 */
public class Server {

    private static final int PORT = 9876; // Puerto que usará el server.
    private static final int BUFFER_SIZE = 1024; // Tamaño de buffer.
    private static List<InetSocketAddress> clients = new ArrayList<>(); // Lista de direcciones sockets conectadas.

    /**
     * Inicio del servidor.
     */
    public static void initServer() {
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            mostrarMensajeBienvenida();

            while (true) {
                recibirMensaje(socket);
            }

        } catch (IOException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static void recibirMensaje(DatagramSocket socket) throws IOException {
        // Configurar la estructura para recibir datos del cliente
        DatagramPacket receivePacket = configurarPacketRecepcion(socket);

        // Obtener información del cliente
        InetSocketAddress clientSocketAddress = obtenerDireccionCliente(receivePacket);

        // Agregar el cliente si no está presente en la lista
        agregarClienteSiNoEstaPresente(clientSocketAddress);

        // Crear un hilo para gestionar al cliente
        Thread clientThread = new Thread(new HiloCliente(socket, receivePacket, clients));
        clientThread.start();
    }

    private static DatagramPacket configurarPacketRecepcion(DatagramSocket socket) throws IOException {
        byte[] receiveData = new byte[BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        return receivePacket;
    }
    
    /**
     * Método que obtiene la dirección del cliente a través del paquete recibido.
     * @param receivePacket paquete recibido.
     * @return la dirección del cliente 
     */
    private static InetSocketAddress obtenerDireccionCliente(DatagramPacket receivePacket) {
        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();
        return new InetSocketAddress(clientAddress, clientPort);
    }
    
    /**
     * Método que comprueba si la dirección ya esta siendo gestionada por el servidor.
     * @param clientSocketAddress dirección del cliente que comprobamos.
     */
    private static void agregarClienteSiNoEstaPresente(InetSocketAddress clientSocketAddress) {
        if (!clients.contains(clientSocketAddress)) {
            clients.add(clientSocketAddress);
        }
    }

    /**
     * Mensaje de bienvenida
     */
    private static void mostrarMensajeBienvenida() {
        System.out.println("╔══════════════════════╗");
        System.out.println("║   Rubén y Miguel      ║");
        System.out.println("║   Bienvenido al Chat  ║");
        System.out.println("║          UDP          ║");
        System.out.println("║ Servidor iniciado en  ║");
        System.out.println("║   el puerto " + PORT + "║");
        System.out.println("╚══════════════════════╝");
    }

}
