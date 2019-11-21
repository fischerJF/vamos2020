////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2018 the original author or authors.
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

package testset.internal.testmodules;

import java.io.File;
import java.util.List;

import api.AbstractFileSetCheck;
import api.FileText;

/**
 * TestFileSetCheck.
 * @noinspection ClassOnlyUsedInOnePackage
 */
public class TestFileSetCheck extends AbstractFileSetCheck {

    private boolean called;

    
    protected void processFiltered(File file, FileText fileText) {
        called = true;
    }

    /**
     * Checks if {@link #processFiltered(File, FileText)} was called.
     * @return {@code true} if it was called.
     */
    public boolean wasCalled() {
        return called;
    }

    /**
     * Resets the check for testing.
     */
    public void resetCheck() {
        called = false;
    }

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void processFiltered(File file, List<String> lines) {
		// TODO Auto-generated method stub
		
	}

}