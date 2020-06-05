package processing;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

/**
 * @author jpaulo
 */
public class SpreadsheetFromFeatureIDE {

	 // TODO include local path
	static String localPathToFeatureIDEProjects = "/Users/ti/Desktop/fischer/jss/Sistemas_formato_FeatureIDE";
	static final String featuresCodeFolder = "features/";
	
	static String currentSystem;
	static String currentFeature;
	
	static Map<String, FeatureEntry> allFeaturesInfo = new LinkedHashMap<>();

	public static void main(String[] args) {
		
		File systemsFolder = new File(localPathToFeatureIDEProjects);
		
		for (File f : systemsFolder.listFiles()) {
			if (f.isDirectory()) {
				currentSystem = f.getName();
				String path = f.getAbsolutePath() + File.separatorChar + featuresCodeFolder;
//				if (!f.getName().equals("ZipMe2")) continue;
				processSystem(path);
			}
		}

//		System.out.println(allFeaturesInfo);

		IO.generateSpreadsheet(allFeaturesInfo);
	}

	public static void processSystem(String pathToSource) {
		
		System.out.println("\n\n#### System " + currentSystem + " ####\n");

		for (File f : new File(pathToSource).listFiles()) {
			if (!f.isDirectory()) continue;
			
			currentFeature = f.getName();
			System.out.println("## Feature " + currentFeature + " ##");
			
			List<File> javaFiles = IO.allJavaFilesIn(f.getAbsolutePath());
			for (File javaFile : javaFiles) {
				System.out.println("*** File " + javaFile + " ***");
				processSystemClass(javaFile);
				System.out.println();
			}
		}
	}

	public static void processSystemClass(File inputFile) {
		
		CompilationUnit inputAST = IO.getCompilationUnitFromFile(inputFile);
		
		if (inputAST == null) return;

		int beginLine = inputAST.getBegin().get().line;
		int endLine = inputAST.getEnd().get().line;
		int countLOC = endLine - beginLine + 1; 

		List<TypeDeclaration> classes = inputAST.findAll(TypeDeclaration.class);
		List<MethodDeclaration> methods = inputAST.findAll(MethodDeclaration.class);
		List<ConstructorDeclaration> constructors = inputAST.findAll(ConstructorDeclaration.class);

		if (classes.size() > 1) {
			System.out.println("TODO check # of classes: " + inputFile);
		}

		for (TypeDeclaration classOrInterface : classes) {

			retrieveFeatureEntry().updateClasses(classOrInterface.getNameAsString());
		}

		for (MethodDeclaration method : methods) {
			
			Optional<BlockStmt> blockStmt = method.getBody();
			if (!blockStmt.isPresent()) {
				continue;
			}

			String methodInfo = method.findAncestor(TypeDeclaration.class).get().getNameAsString()
					+ "." + method.getDeclarationAsString();
			retrieveFeatureEntry().updateMethods(methodInfo);

//			BlockStmt body = blockStmt.get();			
//			int beginLine = body.getBegin().get().line;
//			int endLine = body.getEnd().get().line;
//			countLOC += endLine - beginLine + 1; 
		}
		
		for (ConstructorDeclaration constructor : constructors) {
			
			String constructorInfo = constructor.findAncestor(TypeDeclaration.class).get().getNameAsString()
					+ "." + constructor.getDeclarationAsString();
			retrieveFeatureEntry().updateConstructors(constructorInfo);

//			BlockStmt body = constructor.getBody();			
//			int beginLine = body.getBegin().get().line;
//			int endLine = body.getEnd().get().line;			
//			countLOC += endLine - beginLine + 1; 
		}

		retrieveFeatureEntry().updateLOCs(countLOC);
	}

	private static FeatureEntry retrieveFeatureEntry() {

		String key = currentSystem + "." + currentFeature;
		FeatureEntry general = allFeaturesInfo.get(key);
		
		if (general == null) {
			general = new FeatureEntry(currentSystem, currentFeature);
			allFeaturesInfo.put(key, general);
		}

		return general;
	}	
}
