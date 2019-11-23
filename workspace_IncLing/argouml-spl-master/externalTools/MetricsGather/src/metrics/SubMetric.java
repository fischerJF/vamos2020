package metrics;

import java.util.HashMap;
import java.util.Map;

public class SubMetric {
	/**
	 * Tipo da medi��o efetuada para a m�trica.
	 */
	private Map<String, Integer> metricSubType;	
	
	public SubMetric() {
		this.metricSubType = new HashMap<String, Integer>();
	}
	
	/**
	 * Insere subm�trica com valor
	 * @param subMetric subm�trica da m�trica.
	 */
	public void addValue(String subMetric, Integer value) {
		metricSubType.put(subMetric, value);		
	}
	
	public Map<String, Integer> getValues() {
		return this.metricSubType; 
	}
		
}
