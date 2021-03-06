package experiment;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import baseline.Feature;
import baseline.FeatureType;
import baseline.PowerSet;
import baseline.SPL;
import report.Record;
import report.RunListernerReport;
import specifications.Configuration;
import tests.BoardManagerTest;
import tests.BoardTest;
import tests.FieldTest;
import tests.SudokuFacadeTest;
import tests.SudokuGeneratorTest;
import tests.SudokuTest;
import tests.TestMAB;
import tests.TestMain;

public class BaselineAdapter {
     PowerSet powerset;
     
	public BaselineAdapter() {
		 powerset= new PowerSet();
		
	}
	
	
	
	public void baselineRun() {
		Feature f1 = new Feature();
		f1.setType(FeatureType.MANDATORY);

		Feature f2 = new Feature();
		f2.setType(FeatureType.OPTIONAL);

		Feature f3 = new Feature();
		f3.setType(FeatureType.OPTIONAL);

		Feature f4 = new Feature();
		f4.setType(FeatureType.OPTIONAL);

		Feature f5 = new Feature();
		f5.setType(FeatureType.OPTIONAL);

		Feature f6 = new Feature();
		f6.setType(FeatureType.OPTIONAL);

		Feature f7 = new Feature();
		f7.setType(FeatureType.OPTIONAL);
				
		
		
		SPL spl = new SPL();		
		spl.addOthersFeature(f2);
		spl.addOthersFeature(f3);
		spl.addOthersFeature(f4);
		spl.addOthersFeature(f5);
		spl.addOthersFeature(f6);
		spl.addOthersFeature(f7);
		
		makeProduct(spl);
		
	}
	
	private void makeProduct (SPL spl) {
		 List<Integer> list = new ArrayList<Integer>();
		for (int cont =0; cont<spl.getOthersFeatureList().size(); cont++) {
			list.add(cont);
		}
		
		int cont =0;
		Record record = new Record();
		//System.out.println(powerset.getSubsetUsingBitMap(list));
		for (ArrayList<Integer> integer : powerset.getSubsetUsingBitMap(list)) {
			
			starFeature(); 
			for (Integer integer2 : integer) {
				
				if(integer2 == 0) Configuration.STATES=true;
				if(integer2 == 1) Configuration.UNDO=true;
				if(integer2 == 2) Configuration.COLOR=true;
				if(integer2 == 3) Configuration.SOLVER=true;
				if(integer2 == 4) Configuration.GENERATOR =true;
				if(integer2 == 5) Configuration.EXTENDEDSUDOKU =true;
				

				//System.out.print(integer2+" ");
				
			}
			
		      
			
			if(Configuration.validProduct() ) {
				cont++;
				
				//COLOR, EXTENDEDSUDOKU, GENERATOR, SOLVER, STATES, UNDO
				//COLOR, GENERATOR, SOLVER, STATES, UNDO
				//EXTENDEDSUDOKU, STATES, UNDO
				//EXTENDEDSUDOKU, GENERATOR, SOLVER, STATES, UNDO
				//EXTENDEDSUDOKU, STATES
				//COLOR, EXTENDEDSUDOKU
				//EXTENDEDSUDOKU, SOLVER, STATES, UNDO
				//COLOR, EXTENDEDSUDOKU, SOLVER, STATES, UNDO
				//EXTENDEDSUDOKU, STATES
				//GENERATOR, SOLVER, STATES, UNDO
				//COLOR, GENERATOR, SOLVER, STATES, UNDO
				//COLOR, SOLVER, STATES, UNDO
				//SOLVER, STATES, UNDO
				//  COLOR, EXTENDEDSUDOKU, GENERATOR, SOLVER, STATES, UNDO
//				if(   Configuration.COLOR && 
//				      Configuration.SOLVER && 
//				      Configuration.STATES && 
//				      Configuration.UNDO  &&
//				      Configuration.EXTENDEDSUDOKU &&
//				      !Configuration.GENERATOR &&
//				      Configuration.BASE
//				    
//				    ) {
				Configuration.UNDO=true;
				Configuration.STATES=true;
				Configuration.SOLVER=true;
				Configuration.GENERATOR=false;
				Configuration.EXTENDEDSUDOKU=true;
				Configuration.COLOR=false;
				Configuration.BASE=true;
				Configuration.productPrint();
//			/* Chama a blibioteca core do junit para rodar a suite de testes */
			JUnitCore junit = new JUnitCore();
			junit.addListener(new RunListernerReport(Configuration.returnProduct(), record));
			 org.junit.runner.Result result = junit.run(
					 BoardManagerTest.class,
					 BoardTest.class,
					 FieldTest.class,
					 SudokuFacadeTest.class,
					 SudokuGeneratorTest.class,
					 TestMAB.class,
					 TestMain.class
					 );
			/* fim core junit */
			 System.err.println("cont: "+cont+"((( apos os testes))) ");
			 Configuration.productPrint();
			 System.out.println("\n\n ----------------------- \n\n");
		
//				}
			
			}else {
			//	System.err.println("Inv�lido");
			}
		}
		try {
			record.record2();
		}catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Contador de produtos:" + cont);
	}
	
	private void starFeature() {
		Configuration.BASE=true;
		Configuration.STATES=false;
		Configuration.UNDO=false;
		Configuration.COLOR=false;
		Configuration.SOLVER=false;
		Configuration.GENERATOR =false;
		Configuration.EXTENDEDSUDOKU =false;
		

	}
	
}
