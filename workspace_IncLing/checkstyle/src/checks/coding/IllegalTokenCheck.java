////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2015 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////
package checks.coding;

//import gov.nasa.jpf.annotation.Conditional;
import specifications.Configuration;

import api.Check;
import api.DetailAST;
import api.TokenTypes;

import java.util.Set;

/**
 * <p>
 * Checks for illegal tokens.
 * </p>
 * <p>
 * Rational: Certain language features are often lead to hard to
 * maintain code or are non-obvious to novice developers. Others
 * may be discouraged in certain frameworks, such as not having
 * native methods in EJB components.
 * </p>
 * <p>
 * An example of how to configure the check is:
 * </p>
 * <pre>
 * &lt;module name="IllegalToken"/&gt;
 * </pre>
 * <p> An example of how to configure the check to forbid
 * a {@link TokenTypes#LITERAL_NATIVE LITERAL_NATIVE} token is:
 * </p>
 * <pre>
 * &lt;module name="IllegalToken"&gt;
 *     &lt;property name="tokens" value="LITERAL_NATIVE"/&gt;
 * &lt;/module&gt;
 * </pre>
 * @author <a href="mailto:simon@redhillconsulting.com.au">Simon Harris</a>
 * @author Rick Giles
 */
public class IllegalTokenCheck
    extends Check
{
//	@Conditional
//	private static boolean IllegalToken = true;
	
	@Override
	public boolean isEnabled() {
		return Configuration.IllegalToken;
	}
    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "illegal.token";

    @Override
    public int[] getDefaultTokens()
    {
        return new int[] {
            TokenTypes.LITERAL_SWITCH,
            TokenTypes.POST_INC,
            TokenTypes.POST_DEC,
        };
    }

    @Override
    public int[] getAcceptableTokens()
    {
        // Any tokens set by property 'tokens' are acceptable
        int[] tokensToCopy = getDefaultTokens();
        final Set<String> tokenNames = getTokenNames();
        if (!tokenNames.isEmpty()) {
            tokensToCopy = new int[tokenNames.size()];
            int i = 0;
            for (String name : tokenNames) {
                tokensToCopy[i] = TokenTypes.getTokenId(name);
                i++;
            }
        }
        final int[] copy = new int[tokensToCopy.length];
        System.arraycopy(tokensToCopy, 0, copy, 0, tokensToCopy.length);
        return copy;
    }

    @Override
    public void visitToken(DetailAST ast)
    {
        log(
            ast.getLineNo(),
            ast.getColumnNo(),
            MSG_KEY,
            ast.getText());
    }

}
