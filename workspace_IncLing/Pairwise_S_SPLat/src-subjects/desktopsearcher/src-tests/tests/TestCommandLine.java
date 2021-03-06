package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import splat.DesktopSearcherVariables;
import desktopsearcher.Commandline;
import desktopsearcher.MrPinkMain;

public class TestCommandLine extends DesktopSearcherTest {

	static final String OUTPUT_PATH = homeDir + "/src-tests/out.txt";
	static final String VALID_INDEX_COMMAND = "index " + homeDir + "/files";
	static final String INVALID_INDEX_COMMAND = "index";
	static final String SEARCH_COMMAND = "search";
	static final String SEARCHED_STRING1 = "test";
	static final String SEARCHED_STRING2 = "feature";
	static final String QUIT_COMMAND = "quit";

	@Before
	public void setup() throws IOException {
		configure();
		writeOutput(OUTPUT_PATH);
	}

	@Test
	public void testIndexNoFile() throws IOException {
//		DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(true);
//		DesktopSearcherVariables.getSINGLETON().setTXT(false);
//		DesktopSearcherVariables.getSINGLETON().setHTML(false);
//		DesktopSearcherVariables.getSINGLETON().setLATEX(false);

		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);

		assertNotSame(-1, cmd.parseCommand(VALID_INDEX_COMMAND));
		//
//		assertFalse(getOutput(OUTPUT_PATH).contains(
//				"0 documents have been indexed."));
		//
		assertTrue(getOutput(OUTPUT_PATH).contains(
				"0 documents have been indexed."));
		assertTrue(getNumberFilesIndexed(OUTPUT_PATH) == 0);
	}

	@Test
	public void testCommands() throws IOException {
//		DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(true);
		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);

		assertEquals(0, cmd.parseCommand(INVALID_INDEX_COMMAND));
		assertTrue(getOutput(OUTPUT_PATH).contains(
				"to few arguments for command index!"));

		assertEquals(0, cmd.parseCommand(SEARCH_COMMAND));
		assertTrue(getOutput(OUTPUT_PATH).contains(
				"to few arguments for command search!"));

		assertEquals(-1, cmd.parseCommand(QUIT_COMMAND));
		assertTrue(getOutput(OUTPUT_PATH).contains("Exiting..."));
	}

	@Test
	public void testIndexTXT() throws IOException {
//		DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(true);
//		DesktopSearcherVariables.getSINGLETON().setTXT(true);
		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);
		assertNotSame(-1, cmd.parseCommand(VALID_INDEX_COMMAND));
		assertTrue(getNumberFilesIndexed(OUTPUT_PATH) >= 5);
		// assertTrue(getOutput(OUTPUT_PATH).contains(
		// "5 documents have been indexed."));
	}

	@Test
	public void testIndexHTML() throws IOException {
//		DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(true);
//		DesktopSearcherVariables.getSINGLETON().setHTML(true);
		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);
		assertNotSame(-1, cmd.parseCommand(VALID_INDEX_COMMAND));
		assertTrue(getNumberFilesIndexed(OUTPUT_PATH) >= 3);
		// assertTrue(getOutput(OUTPUT_PATH).contains(
		// "3 documents have been indexed."));
	}

	@Test
	public void testIndexLATEX() throws IOException {
//		DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(true);
//		DesktopSearcherVariables.getSINGLETON().setLATEX(true);
		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);
		assertNotSame(-1, cmd.parseCommand(VALID_INDEX_COMMAND));
		assertTrue(getNumberFilesIndexed(OUTPUT_PATH) >= 2);
		// assertTrue(getOutput(OUTPUT_PATH).contains(
		// "2 documents have been indexed."));
	}

	@Test
	public void testErrorMessage() throws IOException {
//		DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(true);
//		DesktopSearcherVariables.getSINGLETON().setUSER_INTERFACE(true);
		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);
		assertNotSame(-1,
				cmd.parseCommand(SEARCH_COMMAND + " " + SEARCHED_STRING1));
		assertTrue(getOutput(OUTPUT_PATH)
				.contains(
						"An Error has Occured! Error message: Bisher wurde noch kein Pfad indexiert"));
	}

	@Test
	public void testSearch() throws IOException {
//		DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(true);
//		DesktopSearcherVariables.getSINGLETON().setUSER_INTERFACE(true);
		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);
		assertNotSame(-1, cmd.parseCommand(VALID_INDEX_COMMAND));
		assertNotSame(-1,
				cmd.parseCommand(SEARCH_COMMAND + " " + SEARCHED_STRING1));
//		assertTrue(getNumberMatchingDocs(OUTPUT_PATH, SEARCHED_STRING1) == 0);
//		 assertTrue(getOutput(OUTPUT_PATH).contains(
//		 "Searching for 'test'... found 0 matching documents."));
	}

	@Test
	public void testSearchTXT() throws IOException {
//		DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(true);
//		DesktopSearcherVariables.getSINGLETON().setTXT(true);
//		DesktopSearcherVariables.getSINGLETON().setUSER_INTERFACE(true);

		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);
		assertNotSame(-1, cmd.parseCommand(VALID_INDEX_COMMAND));
		assertNotSame(-1,
				cmd.parseCommand(SEARCH_COMMAND + " " + SEARCHED_STRING1));
		assertTrue(getNumberMatchingDocs(OUTPUT_PATH, SEARCHED_STRING1) >= 3);
		// assertTrue(getOutput(OUTPUT_PATH).contains(
		// "Searching for 'test'... found 3 matching documents."));
	}

	@Test
	public void testSearchHTML() throws IOException {
//		DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(true);
//		DesktopSearcherVariables.getSINGLETON().setUSER_INTERFACE(true);
//		DesktopSearcherVariables.getSINGLETON().setHTML(true);
		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);
		assertNotSame(-1, cmd.parseCommand(VALID_INDEX_COMMAND));
		assertNotSame(-1,
				cmd.parseCommand(SEARCH_COMMAND + " " + SEARCHED_STRING2));
		assertTrue(getNumberMatchingDocs(OUTPUT_PATH, SEARCHED_STRING2) >= 2);
		// assertTrue(getOutput(OUTPUT_PATH).contains(
		// "Searching for 'feature'... found 2 matching documents."));
	}

	@Test
	public void testSearchLATEX() throws IOException {
//		DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(true);
//		DesktopSearcherVariables.getSINGLETON().setUSER_INTERFACE(true);
//		DesktopSearcherVariables.getSINGLETON().setLATEX(true);
		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);
		assertNotSame(-1, cmd.parseCommand(VALID_INDEX_COMMAND));
		assertNotSame(-1,
				cmd.parseCommand(SEARCH_COMMAND + " " + SEARCHED_STRING2));
		assertTrue(getNumberMatchingDocs(OUTPUT_PATH, SEARCHED_STRING2) >= 1);
		// assertTrue(getOutput(OUTPUT_PATH).contains(
		// "Searching for 'feature'... found 1 matching documents."));
	}

	@Test
	public void test_substituteWhiteSpaces() throws IOException {
//		 DesktopSearcherVariables.getSINGLETON().setCOMMAND_LINE(false);
		MrPinkMain mpm = new MrPinkMain();
		Commandline cmd = new Commandline(mpm);
		assertEquals(
				"",
				cmd.substituteWhiteSpacesinSingleQuotedStringArea("index /desktopsearcher/files"));
	}

	@After
	public void teardown() {
		System.out.println(DesktopSearcherVariables.getSINGLETON().toString());
//		DesktopSearcherVariables.getSINGLETON().restore();
//		deleteDirectory(new File(homeDir + "/index"));
	}

}
