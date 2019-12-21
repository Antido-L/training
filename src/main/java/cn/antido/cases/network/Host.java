package cn.antido.cases.network;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Host {
  public static void main(String[] args) throws Exception {
    if (args != null && args.length > 0) {
      System.out.println("get target: " + args[0]);
      NetworkInterface networkInterfaceByTarget = getNetworkInterfaceByTarget(args[0]);
      if (networkInterfaceByTarget != null) {
        System.out.println(networkInterfaceByTarget + ":" + getMac(networkInterfaceByTarget));
      } else {
        System.out.println("null");
      }
    }

    System.out.println("get local");
    Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
    while (e.hasMoreElements()) {
      NetworkInterface anInterface = e.nextElement();
      System.out.println(anInterface + ":" + getMac(anInterface));
    }

  }

  private static String getMac(NetworkInterface anInterface) throws SocketException {
    byte[] mac = anInterface.getHardwareAddress();
    if (mac == null) {
      return "null";
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < mac.length; i++) {
      stringBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
    }
    return stringBuilder.toString();
  }

  private static NetworkInterface getNetworkInterfaceByTarget(String target) {
    try (DatagramSocket socket = new DatagramSocket()) {
      InetAddress targetAddress = InetAddress.getByName(target);
      socket.connect(targetAddress, 0);
      return NetworkInterface.getByInetAddress(socket.getLocalAddress());
    } catch (SocketException | UnknownHostException e) {
      System.out.println(e);
    }
    return null;
  }
}
