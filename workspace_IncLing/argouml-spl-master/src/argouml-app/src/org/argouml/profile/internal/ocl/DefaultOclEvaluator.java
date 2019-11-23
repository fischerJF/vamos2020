// $Id: DefaultOclEvaluator.java 127 2010-09-25 22:23:13Z marcusvnac $
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.profile.internal.ocl;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.Map;

//#if defined(LOGGING)
//@#$LPS-LOGGING:GranularityType:Import
import org.apache.log4j.Logger;
//#endif
import org.argouml.specifications.Configuration;

import tudresden.ocl.parser.OclParser;
import tudresden.ocl.parser.lexer.Lexer;
import tudresden.ocl.parser.node.Start;

/**
 * Evaluates ocl expressions
 *
 * @author maurelio1234
 */
public class DefaultOclEvaluator implements OclExpressionEvaluator {
    //#if defined(LOGGING)
    //@#$LPS-LOGGING:GranularityType:Field
    /**
     * Logger.
     */
    private static final Logger LOG = Logger
            .getLogger(DefaultOclEvaluator.class);
    //#endif
    private static OclExpressionEvaluator instance = null;
    
    /**
     * @return unique instance
     */
    public static OclExpressionEvaluator getInstance() {
        if (instance == null) {
            instance = new DefaultOclEvaluator();
        }
        return instance;
    }
    
    /*
     * @see org.argouml.profile.internal.ocl.OclExpressionEvaluator#evaluate(java.util.HashMap, org.argouml.profile.internal.ocl.ModelInterpreter, java.lang.String)
     */
    public Object evaluate(Map<String, Object> vt, ModelInterpreter mi,
            String ocl) throws InvalidOclException {
        // XXX this seems to be a bug of the parser, 
        // it always requires a context
        //#if defined(LOGGING)
        //@#$LPS-LOGGING:GranularityType:Statement
        //@#$LPS-LOGGING:Localization:StartMethod
    	if(Configuration.LOGGING) {
        LOG.debug("OCL: " + ocl);
    	}
        //#endif
        if (ocl.contains("ore")) {
            // TODO: Convert this to some sensible logging
            System.out.println("VOILA!");
        }
        Lexer lexer = new Lexer(new PushbackReader(new StringReader(
                "context X inv: " + ocl), 2));
        OclParser parser = new OclParser(lexer);
        Start tree = null;
        
        try {
            tree = parser.parse();
        } catch (Exception e) {
            throw new InvalidOclException(ocl);
        }
        
        EvaluateExpression ee = new EvaluateExpression(vt, mi);
        tree.apply(ee);
        return ee.getValue();
    }

}
