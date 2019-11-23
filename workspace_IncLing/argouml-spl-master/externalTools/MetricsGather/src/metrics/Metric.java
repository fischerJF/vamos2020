package metrics;

/**
 * Representa uma m�trica, contendo suas subm�tricas e valores
 * @author Marcus
 */
public class Metric {
	
	/**
	 * Nome da feature a qual se refere a metrica;
	 */
	private final String feature;
	
	/**
	 * Array contendo o tipo de m�trica: Granularidade ou Localiza��o.
	 */
	private SubMetric[] subMetrics;	
	
	/**
	 * Retorna o nome da feature
	 * @return Nome da feature.
	 */
	public String getFeature() {
		return feature;
	}

	/**
	 * Construtor padr�o.
	 * @param feature nome da Feature
	 */
	public Metric(String feature) {
		this.subMetrics = new SubMetric[MetricType.values().length-1];
		for (int i=0; i<this.subMetrics.length; i++) {
			this.subMetrics[i] = new SubMetric();
		}
		this.feature = feature;
	}
	
	/**
	 * Retorna a subm�trica.
	 * @param typeEnum tipo da m�trica a retornar.
	 * @return subm�troca encontada.
	 */
	public SubMetric getSubMetric(MetricType typeEnum) {
		return this.subMetrics[typeEnum.ordinal()];
	}	
	
	/**
	 * Armazena uma subm�trica para a m�trica.
	 * @param metricType Tipo da m�trica.
	 * @param subMetric Tipo da subm�trica.
	 * @param value Valor da subm�trica.
	 */
	public void storeMetric(MetricType metricType, String subMetric, Integer value) {
		this.subMetrics[metricType.ordinal()].addValue(subMetric, value);
		
	}
}
