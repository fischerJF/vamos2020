package foo;

import util.Log;
import metrics.GatherMetrics;


public class Main {

	/**
	 * Inicializa o sistema.
	 * @param args par�metros de execu��o. 
	 * 				1� par�metro: diret�rio raiz 
	 * 				2� par�metro: arquivo de sa�da. 
	 * 				3� par�metro: Apenas m�tricas b�sicas
	 */
	public static void main(String[] args) {
		Log.info("Starting gattering metrics");
		if (args.length > 1) {
			GatherMetrics rd;
			if (args.length == 2) {
				rd = new GatherMetrics(args[0]);
			} else {
				rd = new GatherMetrics(args[0], Boolean.parseBoolean(args[2]));
			}
			rd.gatherMetrics(args[1]);
			Log.info("End of gattering metrics. File " + args[1] + " generated.");
		} else {
			Log.info("No args found.");
		}		
	}
}