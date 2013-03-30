package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;

import Serveur.Ville.Activite;

public class Connexion {
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;

	public Connexion(Socket s) {
		this.s = s;
	}

	public void openConnection() {
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void closeConnection() {
		try {
			in.close();
			out.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String s) {
		out.println(s);
		out.flush();
	}

	public String read() {
		try {
			if(in.ready()) return in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected class UDPReceive extends Thread {
		private DatagramSocket socketUDP;
		private DatagramPacket bufferUDP;
		
		public UDPReceive(int port) {
			try {
				socketUDP = new DatagramSocket(port);
				bufferUDP = new DatagramPacket(new byte[1024], 1024);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		
		private String receive() {
			try {
				socketUDP.receive(bufferUDP);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String str = new String(bufferUDP.getData(), bufferUDP.getOffset(), bufferUDP.getLength());
			return str;
			
		}
		
		public void close() {
			socketUDP.disconnect();
			socketUDP.close();
		}
		
		public void run() {
			while(true) {
				// Intruction bloquante
				System.out.println(receive());
			}
		}
	}	
}