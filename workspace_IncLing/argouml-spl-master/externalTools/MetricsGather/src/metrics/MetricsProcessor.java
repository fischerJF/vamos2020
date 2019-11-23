package metrics;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import util.Log;


public class MetricsProcessor {	
	/**
	 * Identificador de anota��o de m�trica.
	 */
	public static final String IDENTIFIER = "//@#$LPS-";
	
	/**
	 * Informa se iniciou-se um coment�rio.
	 */
	private boolean startComment;
	
	/**
	 * Map para armazenar as m�tricas de granularidade.
	 */
	private Map<String, Integer> granMetrics;
	/**
	 * Map para armazenar as m�tricas de localiza��o.
	 */
	private Map<String, Integer> localMetrics;	
	/**
	 * Map para armazenar outras m�tricas.
	 */
	private Map<String, Integer> otherMetrics;
	
	/**
	 * Vetor que armazena as m�tricas processadas
	 */
	private Vector<Metric> metrics;
	
	/** 
	 * Armazena m�trica LOC.
	 */
	private Integer locMetric;
	
	/**
	 * Contador de classes
	 */
	private static Integer CLASS_COUNTER;
	
	/**
	 * String para cria��o de m�tricas que n�o est�o mapeadas diretamente nas classes.
	 */
	private static final String FAKEMETRICIDENTIFIER = "//@#$LPS-%s:%s:N/A";
		
	/**
	 * Construtor padr�o.
	 */
	public MetricsProcessor(){
		granMetrics = new HashMap<String, Integer>();
		localMetrics = new HashMap<String, Integer>();
		otherMetrics = new HashMap<String, Integer>();
		metrics = new Vector<Metric>();
		locMetric = 0;
		startComment = false;
		CLASS_COUNTER = 0;
	}
	
	/**
	 * Insere uma m�trica j� contabilizada
	 * @param line Indentificador da linha, para processamento posterior
	 * @param metricType Tipo da m�trica
	 * @param value Valor calculado
	 */
	private void insertMetric(String line, MetricType metricType, Integer value) {		
		otherMetrics.put(line, value);
	}
	
	/**
	 * Contabilizar as m�tricas por tipo.
	 * @param line linha lida da classe Java.
	 * @param metricType Tipo de m�trica.
	 */
	private void insertMetric(String line, MetricType metricType) {
		if (MetricType.LOC.equals(metricType)) {
			locMetric++;
		} else {		
			Map<String, Integer> metricMap;
			if (MetricType.GRANULARITY.equals(metricType)) {
				metricMap = granMetrics;
			} else if(MetricType.LOCALIZATION.equals(metricType)) {
				metricMap = localMetrics;
			} else {
				metricMap = otherMetrics;
			}

			Integer value = 1;
			if (metricMap.containsKey(line)) {
				value = metricMap.get(line);
				value++;						
			}
			metricMap.put(line, value);
		}
	}	
	
	/**
	 * Verifica se a linha � um coment�rio ou linha em branco
	 * @param line linha a ser verificada
	 * @return <code>true</code> se a linha for coment�rio ou branco,
	 * <code>false</code> caso contr�rio.
	 */
	private boolean isCommentOrBlankLine(String line) {
		if (line.startsWith("/*")) {
			if (line.endsWith("*/")) {
				return true;
			} else if (!line.contains("*/")) {
				startComment = true;
				return true;	
			}			
		} else if (startComment && line.endsWith("*/")) {
			startComment = false;
			return true;
		} else if (startComment) {
			return true;
		} else {
			return (line.startsWith("//") || line.startsWith("*") || line.isEmpty());
		}
		return false;
	}
	
	/**
	 * Insere uma m�trica que n�o depende do conte�do do arquivo Java. 
	 * @param metricType Tipo da m�trica.
	 * @param line Valor da m�trica
	 */
	public void insertMetric(MetricType metricType, Integer value) {
		insertMetric(String.format(FAKEMETRICIDENTIFIER, "TODAS", metricType.getIdentifier()), metricType, value);
	}
	
	/**
	 * Contabiliza a m�trica encontrada na linha do arquivo Java. 
	 * @param value linha lida da classe Java.
	 */
	public void insertMetric(String value) {
		// Common Metrics
		if (value.contains(MetricsProcessor.IDENTIFIER)) {
			if (value.contains(MetricType.GRANULARITY.getIdentifier())) {			
				insertMetric(value, MetricType.GRANULARITY);
			} else if (value.contains(MetricType.LOCALIZATION.getIdentifier())) {
				insertMetric(value, MetricType.LOCALIZATION);
			} else{
				Log.info("Identificador inv�lido. Dados: "  + value);
			}
		}
		// AND, OR e SD Metrics
		else if (value.matches("//#if defined\\(.*\\).*")) {
			String feature1 = value.substring(value.indexOf("(")+1, value.indexOf(")"));
			// AND e OR
			if (value.matches("//#if defined\\(.*\\) (and|or) defined\\(.*\\)")) {
				String feature2 = value.substring(value.lastIndexOf("(")+1, value.lastIndexOf(")"));
				MetricType type;
				if (value.toLowerCase().contains(MetricType.OR.getIdentifier().toLowerCase())) {
					type = MetricType.OR; 
				} else {
					type = MetricType.AND;
				}
				// Inserir m�tricas AND e OR
				insertMetric(String.format(FAKEMETRICIDENTIFIER, feature1, type.getIdentifier()), type);
				insertMetric(String.format(FAKEMETRICIDENTIFIER, feature2, type.getIdentifier()), type);		
				// Inserir m�trica SD
				insertMetric(String.format(FAKEMETRICIDENTIFIER, feature2, MetricType.SD.getIdentifier()), MetricType.SD);
			}
			// Inserir m�trica SD
			insertMetric(String.format(FAKEMETRICIDENTIFIER, feature1, MetricType.SD.getIdentifier()), MetricType.SD);
		}		
		// LOC Metric
		else if (!isCommentOrBlankLine(value)) {
			insertMetric(value, MetricType.LOC);
			// Contabilizar classes
			if (value.matches("(public|protected|private|static|abstract|final|native|synchronized|transient|volatile|strictfp| )*class .*")) {
				CLASS_COUNTER++;
			}
		}
	}
	
	/**
	 * Retorna o �ndice da feature no Vetor de metricas. 
	 * Caso a feature n�o exista no vetor, ela ser� inserida. 
	 * @param feature Nome da feature
	 * @return �ndice da feature no vetor de metricas
	 */
	private Integer getFeatureMetricIndex(String feature) {
		Integer index = -1;
		for (int i = 0; i < metrics.size(); i++) {
			Metric m = (Metric) metrics.get(i);
			if (m.getFeature().equals(feature)) {
				index = i;
			}			
		}
		if (index == -1) {
			metrics.add(new Metric(feature));
			index = metrics.size()-1;
		}
		return index;
	}
	
	/**
	 * Processa as m�tricas colhidas.
	 * @param onlyBasicMetrics Informa se � para processar apenas m�tricas b�sicas.
	 * @return <code>true</code> se processamento correto, <false> caso contr�rio.
	 */
	public boolean processGatheredMetrics(Boolean onlyBasicMetrics) {
		// Inserir m�trica que contabiliza o n�mero de classes
		insertMetric(MetricType.CLASS_NUMBER, CLASS_COUNTER);
		// Processar demais m�tricas.
		boolean result = this.processGatheredMetrics(otherMetrics);
		if (!onlyBasicMetrics) {
			result = result && this.processGatheredMetrics(granMetrics);
			result = result && this.processGatheredMetrics(localMetrics);
		}
		return result;
	}
	
	/**
	 * Processa as m�tricas colhidas.
	 * @param metricMap Map contendo as m�tricas
	 * @return <code>true</code> se processamento correto, <false> caso contr�rio.
	 */
	private boolean processGatheredMetrics(Map<String, Integer> metricMap) {
		try {
			Set<String> keySet = metricMap.keySet();				
			 for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
				 String key = iterator.next();  
				 if(key != null) {
					 Log.debug(key + " - " + metricMap.get(key));		
					 
					 String feature = key.substring(key.indexOf("-")+1, key.indexOf(":"));
					 String[] metricsTypeAndSubType = key.substring(key.indexOf(":")+1).split(":");				 
					 Integer featureIndex = getFeatureMetricIndex(feature);				 
					 Metric metric = metrics.get(featureIndex);
					 metric.storeMetric(MetricType.getByIdentifier(metricsTypeAndSubType[0]), 
							 metricsTypeAndSubType[1], metricMap.get(key));
				 }
			 }
			 return true;
		} catch (Exception e) {
			Log.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Salva as m�tricas em arquivo, no formado CSV.
	 * @param filename nome do arquivo.
	 */
	public void saveGatheredMetrics(String filename) {
		String separator = ",";
		try {
			 PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			 pw.println("FEATURE" + separator + "TIPO_METRICA" + separator + "METRICA" + separator + "VALOR");
			 // Gravar LOC
			 StringBuilder textOutput = new StringBuilder();			
			 textOutput.append("TODAS");
			 textOutput.append(separator);
			 textOutput.append(MetricType.LOC.getIdentifier());
			 textOutput.append(separator);
			 textOutput.append("N/A");
			 textOutput.append(separator);
			 textOutput.append(this.locMetric);
			 pw.println(textOutput);

			 for (Iterator<Metric> it = metrics.iterator(); it.hasNext();) {
				 Metric metric = (Metric) it.next();
				 for (int i=0; i<MetricType.values().length-1; i++) {
					 Set<String> keySet = metric.getSubMetric(MetricType.values()[i]).getValues().keySet();
					 for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
						 String key = iterator.next();  
						 if(key != null) {
							 textOutput = new StringBuilder();			
							 textOutput.append(metric.getFeature());
							 textOutput.append(separator);
							 textOutput.append(MetricType.values()[i].getIdentifier());
							 textOutput.append(separator);
							 textOutput.append(key);
							 textOutput.append(separator);
							 textOutput.append(metric.getSubMetric(MetricType.values()[i]).getValues().get(key));
							 pw.println(textOutput);
						 }
					 }
					 pw.flush();
				 }
			 }
			 pw.close();

		} catch (FileNotFoundException e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
