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

package testset.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import api.AuditEvent;
import api.SeverityLevel;
import api.SeverityLevelCounter;

public class SeverityLevelCounterTest {

//    @Test
//    public void testCtorException() {
//        try {
//            final Object test = new SeverityLevelCounter(null);
//            fail("exception expected but got " + test);
//        }
//        catch (IllegalArgumentException ex) {
//            assertEquals("Invalid exception message",
//                    "'level' cannot be null", ex.getMessage());
//        }
//    }

    @Test
    public void testAddException() {
        final SeverityLevelCounter counter = new SeverityLevelCounter(SeverityLevel.ERROR);
        final AuditEvent event = new AuditEvent(this, "ATest.java", null);
        assertEquals("Invalid severity level count", 0, counter.getCount());
        counter.addException(event, new IllegalStateException("Test IllegalStateException"));
        assertEquals("Invalid severity level count", 1, counter.getCount());
    }

    @Test
    public void testAddExceptionWarning() {
        final SeverityLevelCounter counter = new SeverityLevelCounter(SeverityLevel.WARNING);
        final AuditEvent event = new AuditEvent(this, "ATest.java", null);
        assertEquals("Invalid severity level count", 0, counter.getCount());
        counter.addException(event, new IllegalStateException("Test IllegalStateException"));
        assertEquals("Invalid severity level count", 0, counter.getCount());
    }

    @Test
    public void testAuditStartedClearsState() {
        final SeverityLevelCounter counter = new SeverityLevelCounter(SeverityLevel.ERROR);
        final AuditEvent event = new AuditEvent(this, "ATest.java", null);
        final AuditEvent secondEvent = new AuditEvent(this, "BTest.java", null);

        counter.auditStarted(event);
        assertEquals("Invalid severity level count", 0, counter.getCount());

        counter.addException(event, new IllegalStateException("Test IllegalStateException"));
        assertEquals("Invalid severity level count", 1, counter.getCount());

        counter.auditStarted(secondEvent);
        assertEquals("Invalid severity level count", 0, counter.getCount());
    }

}
