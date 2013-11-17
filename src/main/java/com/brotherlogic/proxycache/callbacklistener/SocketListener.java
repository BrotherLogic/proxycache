package com.brotherlogic.proxycache.callbacklistener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class SocketListener {

	boolean processed;

	public Map<String, String> listenForWebRequest(final int port) {

		final Map<String, String> response = new TreeMap<String, String>();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					listenForWebRequest(port, new ListenerCallback() {
						@Override
						public void processResponse(Map<String, String> props) {
							response.putAll(props);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}

				processed = true;
			}
		});
		t.start();

		// Wait for the response to be processed
		while (!processed)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		return response;

	}

	public void listenForWebRequest(int port, ListenerCallback callback)
			throws IOException {

		Map<String, String> props = new TreeMap<String, String>();

		System.out.println("Reading on port " + port);
		ServerSocket server = new ServerSocket(port);
		Socket sock = server.accept();

		// Listen for an incoming call
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				sock.getInputStream()));
		for (String line = reader.readLine(); line != null; line = reader
				.readLine()) {
			System.out.println("LINE = " + line);
			if (line.startsWith("GET")) {
				processURL(line.substring(3));
				break;
			}
		}

		System.out.println("Nothing read");

		// Write out the close method
		PrintStream ps = new PrintStream(sock.getOutputStream());
		ps.println("CLOSE ME");
		ps.close();
		sock.close();
		server.close();

	}

	private Map<String, String> processURL(String url) {
		System.out.println("HERE: " + url);
		return new TreeMap<String, String>();
	}

	public static void main(String[] args) throws Exception {
		SocketListener listener = new SocketListener();
		listener.listenForWebRequest(8085, new ListenerCallback() {
			@Override
			public void processResponse(Map<String, String> props) {
				System.out.println("DONEDONE");
			}
		});
	}
}
