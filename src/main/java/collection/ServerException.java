package collection;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by puyuan.wlh on 16/12/31.
 */
public class ServerException extends Throwable {

	private String message;
	private int iValue;
	public ServerException(Throwable cause, String message, int iValue) {
		super(cause);
		this.message = message;
		this.iValue = iValue;
	}

	public int getiValue() {
		return iValue;
	}

	public void printStackTrace(PrintStream s) {
		synchronized (s) {
			printStackTrace(new PrintWriter(s));
		}
	}

	public void printStackTrace(PrintWriter s) {
		synchronized (s) {
			s.println(this);
			s.println("\t-------------------------------");
			s.println("\tmessage" + ":" + iValue);
			s.println("\t-------------------------------");
			StackTraceElement[] trace = getStackTrace();
			for (int i = 0; i < trace.length; i++)
				s.println("\tat " + trace[i]);

			Throwable ourCause = getCause();
			if (ourCause != null) {
				ourCause.printStackTrace(s);
			}
			s.flush();
		}
	}
}
