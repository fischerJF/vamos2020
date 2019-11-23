// $Id: XMLTokenTableBase.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

package org.argouml.persistence;

import java.util.Hashtable;

//#if defined(LOGGING)
//@#$LPS-LOGGING:GranularityType:Import
import org.apache.log4j.Logger;
//#endif
import org.argouml.specifications.Configuration;

/**
 * @author Jim Holy
 */

abstract class XMLTokenTableBase {
    //#if defined(LOGGING)
    //@#$LPS-LOGGING:GranularityType:Field
    private static final Logger LOG = Logger.getLogger(XMLTokenTableBase.class);
    //#endif
    ////////////////////////////////////////////////////////////////
    // instance variables

    private  Hashtable tokens       = null;
    private  boolean   dbg          = false;
    private  String[]  openTags   = new String[100];
    private  int[]     openTokens = new int[100];
    private  int       numOpen      = 0;


    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     *
     * @param tableSize the size of the table
     */
    public XMLTokenTableBase(int tableSize) {
	tokens = new Hashtable(tableSize);
	setupTokens();
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @param s the string
     * @param push true if the token is to be pushed
     * @return the token
     */
    public final int toToken(String s, boolean push) {
	if (push) openTags[++numOpen] = s;
	else if (s.equals(openTags[numOpen])) {
	    //#if defined(LOGGING)
	    //@#$LPS-LOGGING:GranularityType:Statement
		 if(Configuration.LOGGING) {
	    LOG.debug("matched: " + s);
		 }
	    //#endif
	    return openTokens[numOpen--];
	}
	Integer i = (Integer) tokens.get(s);
	if (i != null) {
	    openTokens[numOpen] = i.intValue();
	    return openTokens[numOpen];
	}
	else return -1;
    }

    /**
     * @param d true if debugging
     */
    public void    setDbg(boolean d)       { dbg = d; }

    /**
     * @return true if debugging is turned on
     */
    public boolean getDbg()                { return dbg; }

    ////////////////////////////////////////////////////////////////
    // class methods

    /**
     * @param s the string represented by the token number
     * @param i the token number
     */
    protected void addToken(String s, Integer i) {
        boolean error = false;
	if (dbg) {
	    if (tokens.contains(i) || tokens.containsKey(s)) {
	        //#if defined(LOGGING)
	        //@#$LPS-LOGGING:GranularityType:Statement
	        //@#$LPS-LOGGING:Localization:BeforeReturn
	    	 if(Configuration.LOGGING) {
	    		 LOG.error("ERROR: token table already contains " + s);
	    	 }
	    	 //#endif
		error = true;
	    }
	}
	tokens.put(s, i);
	if (dbg && !error) {
	    //#if defined(LOGGING)
	    //@#$LPS-LOGGING:GranularityType:Statement
	    //@#$LPS-LOGGING:Localization:NestedStatement
		 if(Configuration.LOGGING) {
            LOG.debug("NOTE: added '" + s + "' to token table");
         }
		 //#endif
        }
    }

    /**
     * @param token the given token
     * @return true if the token is present
     */
    public boolean contains(String token) {
        return tokens.containsKey(token);
    }

    /**
     * This function shall set up all the tokens with the addToken function.
     */
    protected abstract void setupTokens();

} /* end class XMLTokenTableBase */
