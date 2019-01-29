
package com.jtsec.mc.dev.moitor.syslog.parse;

import com.nesscomputing.syslog4j.SyslogRuntimeException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.nesscomputing.syslog4j.SyslogConstants.SEND_LOCAL_NAME_DEFAULT_VALUE;

/**
* SyslogUtility provides several common utility methods used within
* Syslog4j.
*
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
*
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: SyslogUtility.java,v 1.21 2010/11/28 01:38:08 cvs Exp $
*/
public final class SyslogUtility {
   private SyslogUtility() {
       //
   }

   public static InetAddress getInetAddress(String host) throws SyslogRuntimeException
   {
       try {
           return InetAddress.getByName(host);
       }
       catch (UnknownHostException uhe) {
           throw new SyslogRuntimeException(uhe);
       }
   }

   public static boolean isClassExists(String className) {
       try {
           Class.forName(className);
           return true;

       } catch (ClassNotFoundException cnfe) {
           return false;
       }
   }

   public static String getLocalName() {
       String localName = SEND_LOCAL_NAME_DEFAULT_VALUE;

       try {
           InetAddress addr = InetAddress.getLocalHost();
           return addr.getHostName();

       } catch (UnknownHostException uhe) {
           return localName;
       }
   }

   public static byte[] getBytes(SyslogCharSetIF syslogCharSet, String data)
   {
       return data.getBytes(syslogCharSet.getCharSet());
   }

   public static String newString(SyslogCharSetIF syslogCharSet, byte[] dataBytes) {
       String data = newString(syslogCharSet,dataBytes,dataBytes.length);

       return data;
   }

   public static String newString(SyslogCharSetIF syslogCharSet, byte[] dataBytes, int dataLength) {
       return new String(dataBytes, 0, dataLength,syslogCharSet.getCharSet());
   }


   public static void sleep(long duration) {
       try {
           Thread.sleep(duration);

       } catch (InterruptedException ie) {
           //
       }
   }
}
