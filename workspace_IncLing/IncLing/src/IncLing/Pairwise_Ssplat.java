//package sampling;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import splat.VarAssignment;
//import splat.Variables;
//
//public class Pairwise /*implements Sampling*/{
//	
//	Variables vars;
//	
//	public Pairwise(Variables vars){
//		this.vars = vars;
//	    computeRequirements(vars.getAllVarValues());
//	}
//
//	public boolean check(Variables vars) {
//		boolean shouldSample = false;
//		List<VarAssignment> varValues = vars.getVarsAccessed();
//		if(isUseful(varValues, false)){
//			shouldSample = true;
//			addTest(varValues, false/*debug*/);
////			System.out.printf("Satisfied reqs: %d \n", satisfiedRequirements.size());
////			System.out.println("All reqs: " + requirements.size());
//			System.out.printf("coverage = %.2f%% \n", coverage());
//		}
//		return shouldSample;
//	}
//	
//	  Set<Requirement> requirements;
//	  Set<Requirement> satisfiedRequirements = new HashSet<Requirement>();
//	  
//	  /************** compute requirements **************/
//
//	  /* 
//	   * This class denotes the domains of variables for the 
//	   * problem.  For example, considering a problem with 2 
//	   * variables x and y, with x ranging from 1 to 3 and y 
//	   * ranging between 0 and 1, lo=[1,0] and hi=[3,1].
//	   */
//	  static class VarDomains {
//	    int[] lo;
//	    int[] hi;
//
//	    VarDomains(int[] lo, int[] hi) {
//	      this.lo = lo;
//	      this.hi = hi;
//	    }
//	    
//	  }
//	  
////	  /*
////	   * This class denotes a particular assignment of variable
////	   * to value.  An assignment of x to 1 is modeled with 
////	   * VarAssignment(0,1).  Note that we assume a particular
////	   * index associated to a variable.  See class above.
////	   */
////	  static class VarAssignment {
////	    int var; int val;
////	    VarAssignment(int var, int val) {
////	      this.var = var;
////	      this.val = val;
////	    }
////	    @Override
////	    public String toString() {
////	      return String.format("(%d,%d)", var, val);
////	    }
////	  }
//	  
//	  
//	  /*
//	   * This class denotes the test requirement for pairwise coverage.
//	   * 
//	   * A requirement for pairwise coverage consists of 
//	   *    variable index_1 with value val_1 AND 
//	   *    variable index_2 with val_2. 
//	   */
//	  static class Requirement {
//	    String index_1; String val_1;
//	    String index_2; String val_2;
//	    Requirement(String index_1, String val_1, String index_2, String val_2) {
//	      this.index_1 = index_1;
//	      this.val_1 = val_1;
//	      this.index_2 = index_2;
//	      this.val_2 = val_2;
//	    }
//	    @Override
//	    public String toString() {
//	      return String.format("(%s,%s)-(%s,%s)", index_1, val_1, index_2, val_2);
//	    }
//	    /** this is important... using hash sets. **/
//	    @Override
//	    public boolean equals(Object obj) {
//	      if (obj == null) return false;
//	      Requirement tmp = (Requirement) obj;
//	      return (index_1.equals(tmp.index_1) && val_1.equals(tmp.val_1) 
//	    	&& index_2.equals(tmp.index_2) && val_2.equals(tmp.val_2));
//	    }
//	    @Override
//	    public int hashCode() { 
//	      //TODO: this is a horrible/slow hashcode function.  assuming efficiency is not critical.
//	      return (index_1+""+ val_1+ index_2+ val_2).hashCode();
//	    }
//	  }
//
//	  void computeRequirements(List<VarAssignment> allVarVals) {
//	    Set<Requirement> result = new HashSet<Requirement>();
//	    
//	    // computing all pairs ((x,a),(y,b)) for x!=y
//	    for (int var1 = 0; var1 < allVarVals.size(); var1++) {
//	      VarAssignment vv1 = allVarVals.get(var1);
//	      for (int var2 = 0; var2 < allVarVals.size(); var2++) {
//	        VarAssignment vv2 = allVarVals.get(var2);
//	        /**
//	         * don't want pairs involving the same variables => (vv1.var == vv2.var)
//	         * don't want duplicated pairs => (vv1.var > vv2.var) 
//	         */
//	        if (vv1.getVar().equals(vv2.getVar())) continue;
//	        result.add(new Requirement(vv1.getVar(), vv1.getVal(), vv2.getVar(), vv2.getVal()));
//	      }
//	    }
//	    requirements = result;
//	  }
//	  
//	  /************** update test suite **************/
//	  void addTest(List<VarAssignment> testInput, boolean debug) {
//	    satisfiedRequirements.addAll(listReqs(testInput, debug));
//	  }
//	  
//	  List<Requirement> listReqs(List<VarAssignment> testInput, boolean debug) {
//	    List<Requirement> res = new ArrayList<Requirement>();
//	    for (int i = 0; i < testInput.size(); i++) {
//	      VarAssignment vv1 = testInput.get(i);
//	      for (int j = i+1; j < testInput.size(); j++) {
//	        VarAssignment vv2 = testInput.get(j);
//	        Requirement req = new Requirement(vv1.getVar(), vv1.getVal(), vv2.getVar(), vv2.getVal());
//	        if (debug) System.out.println(req);
//	        res.add(req);
//	      } 
//	    }
//	    return res; /* number of new requirements in this test input */
//	  }
//	  
//	  /************** check if test helps improve coverage **************/
//	  boolean isUseful(List<VarAssignment> testInput, boolean debug) {
//	     List<Requirement> l = listReqs(testInput, debug);
//	     /**
//	      * Call l.retainAll(c) retains in list l only the elements from c;
//	      * it discarding elements not in c.  The method retainAll returns
//	      * a boolean indicating whether or not the list has changed.  
//	      * 
//	      * The list l can only change after if there are elements in l 
//	      * which are not in satisfiedRequirements. 
//	      */
//	     return l.retainAll(satisfiedRequirements); 
//	  }
//
//	  /************** coverage ****************/
//	  double coverage() {
//	    return 100*((double)satisfiedRequirements.size())/requirements.size();
//	  }
// 
//}

//
package IncLing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Pairwise_Ssplat {
  
  Set<Requirement> requirements;
  Set<Requirement> satisfiedRequirements = new HashSet<Requirement>();
  
  /************** compute requirements **************/

  /* 
   * This class denotes the domains of variables for the 
   * problem.  For example, considering a problem with 2 
   * variables x and y, with x ranging from 1 to 3 and y 
   * ranging between 0 and 1, lo=[1,0] and hi=[3,1].
   */
  static class VarDomains {
    int[] lo;
    int[] hi;

    VarDomains(int[] lo, int[] hi) {
      this.lo = lo;
      this.hi = hi;
    }
    
  }
  
  /*
   * This class denotes a particular assignment of variable
   * to value.  An assignment of x to 1 is modeled with 
   * VarAssignment(0,1).  Note that we assume a particular
   * index associated to a variable.  See class above.
   */
  static class VarAssignment {
    int var; int val;
    VarAssignment(int var, int val) {
      this.var = var;
      this.val = val;
    }
    @Override
    public String toString() {
      return String.format("(%d,%d)", var, val);
    }
  }
  
  
  /*
   * This class denotes the test requirement for pairwise coverage.
   * 
   * A requirement for pairwise coverage consists of 
   *    variable index_1 with value val_1 AND 
   *    variable index_2 with val_2. 
   */
  static class Requirement {
    int index_1; int val_1;
    int index_2; int val_2;
    Requirement(int index_1, int val_1, int index_2, int val_2) {
      this.index_1 = index_1;
      this.val_1 = val_1;
      this.index_2 = index_2;
      this.val_2 = val_2;
    }
    @Override
    public String toString() {
      return String.format("(%d,%d)-(%d,%d)", index_1, val_1, index_2, val_2);
    }
    /** this is important... using hash sets. **/
    @Override
    public boolean equals(Object obj) {
      if (obj == null) return false;
      Requirement tmp = (Requirement) obj;
      return (index_1 == tmp.index_1 && val_1 == tmp.val_1 && index_2 == tmp.index_2 && val_2 == tmp.val_2);
    }
    @Override
    public int hashCode() { 
      //TODO: this is a horrible/slow hashcode function.  assuming efficiency is not critical.
      return (index_1+""+ val_1+ index_2+ val_2).hashCode();
    }
  }

  void computeRequirements(VarDomains problem) {
    Set<Requirement> result = new HashSet<Requirement>();
    // computing all var-vals
    List<VarAssignment> allVarVals = new ArrayList<VarAssignment>();
    for (int var = 0; var < problem.lo.length; var++) {
      // iterating through variable var
      for (int val = problem.lo[var]; val <= problem.hi[var]; val++) {
        allVarVals.add(new VarAssignment(var, val));
      }
    }
    // computing all pairs ((x,a),(y,b)) for x!=y
    for (int var1 = 0; var1 < allVarVals.size(); var1++) {
      VarAssignment vv1 = allVarVals.get(var1);
      for (int var2 = 0; var2 < allVarVals.size(); var2++) {
        VarAssignment vv2 = allVarVals.get(var2);
        /**
         * don't want pairs involving the same variables => (vv1.var == vv2.var)
         * don't want duplicated pairs => (vv1.var > vv2.var) 
         */
        if (vv1.var >= vv2.var) continue;
        result.add(new Requirement(vv1.var, vv1.val, vv2.var, vv2.val));
      }
    }
    requirements = result;
  }
  
  /************** update test suite **************/
  void addTest(List<VarAssignment> testInput, boolean debug) {
    satisfiedRequirements.addAll(listReqs(testInput, debug));
  }
  
  List<Requirement> listReqs(List<VarAssignment> testInput, boolean debug) {
    List<Requirement> res = new ArrayList<Requirement>();
    for (int i = 0; i < testInput.size(); i++) {
      VarAssignment vv1 = testInput.get(i);
      for (int j = i+1; j < testInput.size(); j++) {
        VarAssignment vv2 = testInput.get(j);
        Requirement req = new Requirement(vv1.var, vv1.val, vv2.var, vv2.val);
        if (debug) System.out.println(req);
        res.add(req);
      } 
    }
    return res; /* number of new requirements in this test input */
  }
  
  /************** check if test helps improve coverage **************/
  boolean isUseful(List<VarAssignment> testInput, boolean debug) {
     List<Requirement> l = listReqs(testInput, debug);
     /**
      * Call l.retainAll(c) retains in list l only the elements from c;
      * it discarding elements not in c.  The method retainAll returns
      * a boolean indicating whether or not the list has changed.  
      * 
      * The list l can only change after if there are elements in l 
      * which are not in satisfiedRequirements. 
      */
     return l.retainAll(satisfiedRequirements); 
  }

  /************** coverage ****************/
  double coverage() {
    return 100*((double)satisfiedRequirements.size())/requirements.size();
  }
  
  /**
   * Intent of this main function is to document/explain.
   */
  public static void main(String[] args) {
    Pairwise_Ssplat p = new Pairwise_Ssplat();
    
    // Consider a problem with 3 variables, with the following numeric domains:
    // x: [0,1] -- boolean
    // y: [0,2]
    // z: [0,3]
    
    p.computeRequirements(new VarDomains(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{1,1,1,1,1,1,1,1,1,1,1,1}));
    
    // There are 26 pairs (= 2x3 + 2x4 + 3x4).  They are:
    // (x=0, y=0) (x=0, y=1) (x=0, y=2)
    // (x=1, y=0) (x=1, y=1) (x=1, y=2) 
    // -- (2 x 3) part
    // (x=0, z=0) (x=0, z=1) (x=0, z=2) (x=0, z=3) 
    // (x=1, z=0) (x=1, z=1) (x=1, z=2) (x=1, z=3)
    // -- (2 x 4) part
    // (y=0, z=0) (y=0, z=1) (y=0, z=2) (y=0, z=3)
    // (y=1, z=0) (y=1, z=1) (y=1, z=2) (y=1, z=3)
    // (y=2, z=0) (y=2, z=1) (y=2, z=2) (y=2, z=3)
    // -- (3 x 4) part
//    System.out.printf("%d ...should be %d\n", p.requirements.size(), 26);
    
    int c=0;
    for ( Requirement r : p.requirements) {
		System.out.println(r);
		c++;
	}
    System.out.println("contador"+c);
    // Build a test case (to check how many requirements/pairs it satisfies).
    // Test case is for (x=0, y=1, z=1)
    VarAssignment a1 = new VarAssignment(0, 0);
    VarAssignment a2 = new VarAssignment(1, 1);
    VarAssignment a3 = new VarAssignment(2, 1);
    List<VarAssignment> test = new ArrayList<VarAssignment>();
    test.add(a1);
    test.add(a2);
    test.add(a3);
    // This test case should satisfy 3 requirements (/pairs).
    // The requirements are: (x=0, y=1) (x=0, z=1) (y=1, z=1)    
    p.addTest(test, true/*debug*/);
//    System.out.printf("%d ...should be %d\n", p.satisfiedRequirements.size(), 3);
    
    
    // compute coverage.
//    System.out.printf("coverage = %.2f%% ...should be %.2f\n", p.coverage(), 11.54f);
    
    // create another test (x=0, y=2, z=1)
    VarAssignment a4 = new VarAssignment(0, 0);
    VarAssignment a5 = new VarAssignment(1, 2);
    VarAssignment a6 = new VarAssignment(2, 1);
    test = new ArrayList<VarAssignment>();
    test.add(a4);
    test.add(a5);
    test.add(a6);
    if (p.isUseful(test, false)) {
      // should enter here as there are two new pairs: (x=0, y=2) and (y=2, z=1)
      p.addTest(test, true/*debug*/);
//      System.out.printf("coverage %.2f%% ...should be %.2f\n", p.coverage(), 19.23f /* =5/26*/); 
    }
    
    // create another test (x=0, y=1, z=1)
    VarAssignment a7 = new VarAssignment(0, 0);
    VarAssignment a8 = new VarAssignment(1, 1);
    VarAssignment a9 = new VarAssignment(2, 1);
    test = new ArrayList<VarAssignment>();
    test.add(a7);
    test.add(a8);
    test.add(a9);
    if (p.isUseful(test, false)) { 
      // should ***NOT*** enter here, there are no new pairs.
      p.addTest(test, true/*debug*/);
//      System.out.printf("coverage %.2f%% ...should be %.2f\n", p.coverage(), 19.23f); 
    }
  }

}