package metrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import util.Log;

public class GatherMetrics {

	/**
	 * Diret�rio raiz (onde se inicia a varredura).
	 * */
	private String rootDir;
		
	/**
	 * Processador de m�tricas.
	 */
	private MetricsProcessor metricsProcessor;
	
	/**
	 * Contador de pacotes Java
	 */
	private static Integer PACKAGE_COUNTER;
	
	/** 
	 * Informa se o diret�rio possui arquivos Java v�lidos, para ser usado na contabiliza��o de pacotes. 
	 */
	private static Boolean HasValidJavaFile;
	
	/**
	 * Informa se � para processar apenas m�tricas b�sicas: LOC, AND, OR, SD.
	 */
	private static Boolean OnlyBasicMetrics;
	
	/**
	 * Filtro de diret�rios.
	 * */
	FilenameFilter dirFilter = new FilenameFilter() { 
		public boolean accept(File dir, String name) {
			name = name.toLowerCase();
			return (!name.startsWith(".") && !name.startsWith("build")
					&& !name.endsWith(".properties") && !name.endsWith(".xml")
					&& !name.endsWith(".launch") && !name.endsWith(".log") && !name.endsWith(".txt")
					&& !name.endsWith(".ico") && !name.endsWith(".bat") && !name.endsWith(".sh")
					&& !name.endsWith(".mf") && !name.endsWith(".ini") && !name.endsWith(".class")
					&& !name.endsWith(".java") && !name.endsWith(".html") && !name.endsWith(".gif") 
					&& !name.equals("meta-inf")
					&& !name.equals("lib") && !name.equals("bin") && !name.equals("templates")
					&& !name.equals("staging") && !name.equals("tests") && !name.equals("argouml-build")
					&& !name.equals("argouml-core-tools") && !name.equals("argouml-core-infra") 
					&& !name.startsWith("argouml-core-model")
					);
			} 
		};	 

	/**
	 * Filtro para listar apenas os arquivos .java
	 * */
	FilenameFilter javaFileFilter = new FilenameFilter() { 
		public boolean accept(File dir, String name) {
			return name.endsWith(".java");
			} 
		};	 
		
	/**
	 * Construtor padr�o
	 * @param rootDir Diret�rio raiz (onde se inicia a varredura).
	 */
	public GatherMetrics(String rootDir) {
		this.rootDir = rootDir.replace("\\", File.separator);
		metricsProcessor = new MetricsProcessor();
		PACKAGE_COUNTER = 0;
		HasValidJavaFile = false;
		OnlyBasicMetrics = false;
	}

	/**
	 * Construtor padr�o
	 * @param rootDir Diret�rio raiz (onde se inicia a varredura)
	 * @param onlyBasicMetrics Informa se � para processar apenas m�tricas b�sicas.
	 */
	public GatherMetrics(String rootDir, Boolean onlyBasicMetrics) {
		this(rootDir);
		OnlyBasicMetrics = onlyBasicMetrics;
	}	
	
	/**
	 * Colhe as m�tricas e as salva em arquivo.
	 * @param filename Nome do arquivo que conter� as m�tricas
	 */
	public void gatherMetrics(String filename) {
		this.listDir(new File(rootDir));
		metricsProcessor.insertMetric(MetricType.PACKAGE_NUMBER, PACKAGE_COUNTER);
		metricsProcessor.processGatheredMetrics(OnlyBasicMetrics);
		metricsProcessor.saveGatheredMetrics(filename);
	}
	
	/**
	 * Lista os diret�rios e subdiret�rios. 
	 * @param dir
	 */
	private void listDir(File dir) {
		if (dir.isDirectory()) {			
			listDirFiles(dir);
			String[] children = dir.list(dirFilter);  
		    for (int i=0; i<children.length; i++) {  
		    	listDir(new File(dir, children[i]));  
		    }
		}		
	}
	
	/**
	 * Lista os arquivos .java de um diret�rio.
	 * @param dir Diret�rio a ser lido.
	 */
	private void listDirFiles(File dir) {
		Log.debug(dir.toString());
		String[] children = dir.list(javaFileFilter);
		int i;
	    for (i=0; i<children.length; i++) {  
	    	Log.debug(children[i]);
	    	processFile(new File(dir, children[i]));
	    }
	    // Se foi processado algum arquivo do diret�rio, considerar o pacote
	    if (HasValidJavaFile && (i > 0)) {
	    	PACKAGE_COUNTER++;
	    }
	    HasValidJavaFile = false;
	}
	
	/**
	 * Efetua a leitura dos arquivos .java 
	 * @param file Arguivo Java a ser lido.
	 */
	private void processFile(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			Boolean hasData = false;
			while (br.ready()) {
				String line = br.readLine().trim();
				if (!line.isEmpty()) {
					metricsProcessor.insertMetric(line);
					HasValidJavaFile = true;
				}								
			}
			br.close();
		} catch (FileNotFoundException e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		
	}	
}
 