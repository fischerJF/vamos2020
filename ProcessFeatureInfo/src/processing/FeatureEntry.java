package processing;

import java.util.HashSet;
import java.util.Set;

class FeatureInfo {
	String classs;
	String method;
	String constructor;
	String block;
}

public class FeatureEntry {

	private String system;
	private String feature;

	Set<String> classes = new HashSet<String>();
	Set<String> constructors = new HashSet<String>();
	Set<String> methods = new HashSet<String>();
	Set<String> blocks = new HashSet<String>(); // TODO block range (javaparser) may be enough
	private long qtdLOCs = 0;

	public FeatureEntry(String system, String feature) {
		this.system = system;
		this.feature = feature;
	}

	long qtdClasses() { return classes.size(); };
	long qtdConstructors() { return constructors.size(); };
	long qtdMethods() { return methods.size(); };
	long qtdBlocks() { return blocks.size(); };
	long getQtdLOCs() { return qtdLOCs; }

	void updateClasses(String classs) {
		if (classs != null)
			this.classes.add(classs);
	}

	void updateConstructors(String constructor) {
		if (constructor != null)
			this.constructors.add(constructor);
	}

	void updateMethods(String method) {
		if (method != null)
			this.methods.add(method);
	}

	void updateBlocks(String block) {
		if (block != null)
			this.blocks.add(block);
	}

	void updateLOCs(int loc) {
		qtdLOCs += loc;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof FeatureEntry) {
			FeatureEntry oc = (FeatureEntry)obj;

			return this.system.equals(oc.system) &&
					this.feature.equals(oc.feature);
		}
		return false;
	}
	
	public String generateCSV() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.system).append(',')
				.append(this.feature).append(',')
				.append(this.qtdClasses()).append(',')
				.append(this.qtdConstructors()).append(',')
				.append(this.qtdMethods()).append(',')
				.append(this.qtdBlocks()).append(',')
				.append(this.getQtdLOCs());
		return sb.toString();
	}
	
	@Override
	public String toString() {
		
		return this.generateCSV();
	}
}
