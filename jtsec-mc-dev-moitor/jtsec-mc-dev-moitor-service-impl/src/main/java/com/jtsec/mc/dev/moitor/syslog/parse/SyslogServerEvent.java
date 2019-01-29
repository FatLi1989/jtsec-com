package com.jtsec.mc.dev.moitor.syslog.parse;

import com.google.common.base.Charsets;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * SyslogServerEvent provides an implementation of the SyslogServerEventIF interface.
 *
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: SyslogServerEvent.java,v 1.9 2011/01/11 06:21:15 cvs Exp $
 */
public class SyslogServerEvent implements SyslogCharSetIF {
	private static final Logger LOG = Logger.getLogger(SyslogServerEvent.class);

	public static final String DATE_FORMAT = "MMM dd HH:mm:ss yyyy";

	protected Charset charSet = Charsets.UTF_8;
	protected String rawString = null;
	protected byte[] rawBytes = null;
	protected int rawLength = -1;
	protected Date date = null;
	protected SyslogLevel level = SyslogLevel.INFO;
	protected SyslogFacility facility = SyslogFacility.user;
	protected String host = null;
	protected boolean isHostStrippedFromMessage = false;
	protected String message = null;
	protected InetAddress inetAddress = null;

	private int subLength = 0;
	private String Ident = null;
	private String hostName = null;

	public SyslogServerEvent(final String message, InetAddress inetAddress) {
		initialize(message,inetAddress);
	}

	public SyslogServerEvent(final byte[] message, int length, InetAddress inetAddress) {
		initialize(message,length,inetAddress);
	}

	protected void initialize(final String message, InetAddress inetAddress) {
		this.rawString = message;
		this.rawLength = message.length();
		this.inetAddress = inetAddress;

		this.message = message;

		parse();
	}

	protected void initialize(final byte[] message, int length, InetAddress inetAddress) {
		this.rawBytes = message;
		this.rawLength = length;
		this.inetAddress = inetAddress;

		//       this.message = SyslogUtility.newString(this,this.rawBytes,this.rawLength);

		this.message = new String(message);


		parse();
	}

	
	protected void parseIdent(){
		int i = this.message.indexOf(' ');
		if (i > -1) {
			String identTmp = StringUtils.trimToEmpty(this.message.substring(0,i));
			if(identTmp.endsWith(":")){
				this.Ident = identTmp.substring(0, identTmp.length() - 1);
				this.message = this.message.substring(i+1);
				this.subLength += (i + 1);
			}
		}
	}
	
	public String getIdent() {
		return Ident;
	}

	protected void parseHost() {
		int i = this.message.indexOf(' ');

		if (i > -1) {
			String hostAddress = null;
			String hostName = null;

			String providedHost = StringUtils.trimToEmpty(this.message.substring(0,i));

			hostAddress = this.inetAddress.getHostAddress();

			if (providedHost.equalsIgnoreCase(hostAddress)) {
				this.host = hostAddress;
				this.message = this.message.substring(i+1);

				this.subLength += (i + 1);

				isHostStrippedFromMessage = true;
			}

			if (this.host == null) {
				hostName = this.inetAddress.getHostName();

				if (!hostName.equalsIgnoreCase(hostAddress)) {
					if (providedHost.equalsIgnoreCase(hostName)) {
						this.host = hostName;
						this.message = this.message.substring(i+1);
						this.subLength += (i + 1);
						isHostStrippedFromMessage = true;
					}

					if (this.host == null) {
						int j = hostName.indexOf('.');

						if (j > -1) {
							hostName = hostName.substring(0,j);
						}

						if (providedHost.equalsIgnoreCase(hostName)) {
							this.host = hostName;
							this.message = this.message.substring(i+1);
							this.subLength += (i + 1);
							isHostStrippedFromMessage = true;
						}
					}
				}else{
					if(providedHost.indexOf(":") < 0){
						this.message = this.message.substring(i+1);
						this.subLength += (i + 1);
					}
					this.hostName = providedHost;
				}
			}

			if (this.host == null) {
				this.host = (hostName != null) ? hostName : hostAddress;
			}
		}
	}

	public String getHostName() {
		return hostName;
	}

	protected void parseDate() {
		if (this.message.length() >= 16 && this.message.charAt(3) == ' ' && this.message.charAt(6) == ' ') {
			String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

			String originalDate = this.message.substring(0,15) + " " + year;
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
			try {
				this.date = dateFormat.parse(originalDate);
				this.message = this.message.substring(16);
				this.subLength += 16;
			} catch (ParseException pe) {
				dateFormat = new SimpleDateFormat(DATE_FORMAT,Locale.CHINESE);
				try {
					this.date = dateFormat.parse(originalDate);
					this.message = this.message.substring(16);
					this.subLength += 16;
				} catch (ParseException pe1) {
					this.date = new Date();
				}
			}
		}
	}

	protected void parsePriority() {
		if (this.message.charAt(0) == '<') {
			int i = this.message.indexOf(">");

			if (i <= 4 && i > -1) {
				String priorityStr = this.message.substring(1,i);

				int priority = 0;
				try {
					priority = Integer.parseInt(priorityStr);
					this.facility = SyslogFacility.forValue(priority & ~7);
					this.level = SyslogLevel.values()[priority & 7];

					this.message = this.message.substring(i+1);
					this.subLength += (i + 1);
				} catch (NumberFormatException nfe) {
					LOG.trace("While parsing priority", nfe);
				}
			}
		}
	}
	
	protected void parse()
	{
		parsePriority();
		parseDate();
		parseHost();
		parseIdent();
	}

	public SyslogFacility getFacility() {
		return this.facility;
	}

	public void setFacility(SyslogFacility facility) {
		this.facility = facility;
	}

	public int getRawLength() {
		return this.rawLength;
	}
	
	public void setRawLength(int rawLength) {
		this.rawLength = rawLength;
	}

	@SuppressFBWarnings("EI_EXPOSE_REP")
	public Date getDate() {
		return this.date;
	}

	@SuppressFBWarnings ("EI_EXPOSE_REP2")
	public void setDate(Date date) {
		this.date = date;
	}

	public SyslogLevel getLevel() {
		return this.level;
	}

	public void setLevel(SyslogLevel level) {
		this.level = level;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isHostStrippedFromMessage() {
		return isHostStrippedFromMessage;
	}

	public byte[] getRawBytes() {
		return rawBytes;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Charset getCharSet() {
		return this.charSet;
	}

	@Override
	public void setCharSet(Charset charSet) {
		this.charSet = charSet;
	}

	public int getSubLength() {
		return subLength;
	}
	
	public void setSublength(int subLength){
		this.subLength = subLength;
	}
}

