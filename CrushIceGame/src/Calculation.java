import javax.swing.JButton;

public class Calculation {
	Calculation calculation = new Calculation();

	public static double lerp(double x0, double y0, double x1, double y1, double x) {
		return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
	}

	public static int calcDistance(double x0, double y0, double x1, double y1) {
		return (int) Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
	}

	public static void setButton(JButton jb) {
		jb.setBorderPainted(false);
		jb.setContentAreaFilled(false);
	}
}
