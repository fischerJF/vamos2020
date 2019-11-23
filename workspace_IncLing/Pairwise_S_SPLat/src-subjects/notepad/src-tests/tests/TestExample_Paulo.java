package tests;

import static org.fest.assertions.Assertions.assertThat;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.fixture.ColorFixture;
import org.fest.swing.fixture.ContainerFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.junit.After;
import org.junit.Test;

import splat.CompaniesVariables;

public class TestExample_Paulo extends NotepadTest{

  @Override
  protected void configure() {
 // set mandatory features
    super.configure();
    CompaniesVariables.getSINGLETON().setGUI___(true);
    if (testName == null) {
      throw new RuntimeException();
    }
    String mName = testName.getMethodName();
    /**
     * set specific features for each test
     */
//    if (mName.equals("testBasic")) {
//      NotepadVariables.getSINGLETON().setBASE___(true);
//    } else if (mName.equals("testEditToolBar")) {  
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASETOOLBAR___(true);
//      NotepadVariables.getSINGLETON().setEDITTOOLBAR___(true);
////      NotepadVariables.getSINGLETON().setUNDOREDOTOOLBAR___(false);
//    } else if (mName.equals("testFormatToolBar")) {  
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASETOOLBAR___(true);
//      NotepadVariables.getSINGLETON().setFORMATTOOLBAR___(true);
//    } else if (mName.equals("testPersistenceToolBar")) { 
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASETOOLBAR___(true);
//      NotepadVariables.getSINGLETON().setPERSISTENCETOOLBAR___(true);
//    } else if (mName.equals("testPrintToolBar")) {
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASETOOLBAR___(true);
//      NotepadVariables.getSINGLETON().setPRINTTOOLBAR___(true);
//    } else if (mName.equals("testSearchToolBar")) { 
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASETOOLBAR___(true);
//      NotepadVariables.getSINGLETON().setSEARCHTOOLBAR___(true);
//    } else if (mName.equals("testWordCountToolBar")) { 
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASETOOLBAR___(true);
//      NotepadVariables.getSINGLETON().setWORDCOUNTTOOLBAR___(true);
//    } else if (mName.equals("testEditMenuBar")) {  
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASEMENUBAR___(true);
//      NotepadVariables.getSINGLETON().setEDITMENUBAR___(true);
//    } else if (mName.equals("testFormatMenuBar")) {  
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASEMENUBAR___(true);
//      NotepadVariables.getSINGLETON().setFORMATMENUBAR___(true);
//    } else if (mName.equals("testPersistenceMenuBar")) { 
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASEMENUBAR___(true);
//      NotepadVariables.getSINGLETON().setPERSISTENCEMENUBAR___(true);
//    } else if (mName.equals("testPrintMenuBar")) {
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASEMENUBAR___(true);
//      NotepadVariables.getSINGLETON().setPRINTMENUBAR___(true);
//    } else if (mName.equals("testSearchMenuBar")) { 
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASEMENUBAR___(true);
//      NotepadVariables.getSINGLETON().setSEARCHMENUBAR___(true);
//    } else if (mName.equals("testWordCountMenuBar")) { 
//      NotepadVariables.getSINGLETON().setBASE___(true);
//      NotepadVariables.getSINGLETON().setBASEMENUBAR___(true);
//      NotepadVariables.getSINGLETON().setWORDCOUNTMENUBAR___(true);
//    }
  }

  @Test
  public void testBasic() {
    JTextComponentFixture textArea = window.textBox();
    textArea.requireEditable();
    ColorFixture color = textArea.background();
    assertThat(color).isNotNull();
    color.requireEqualTo(Color.WHITE);
    String text = "Hello";
    textArea.enterText(text);
    assertThat(textArea.text()).contains(text);
    assertThat(textArea).isNotNull();
    textArea.deleteText();
    assertThat(textArea.text()).contains("");
  }
  
  @Test
  public void testEditToolBar() {
    JTextComponentFixture textArea = window.textBox();
    String text = "Hello";
    String texttext = "HelloHello";
    textArea.enterText(text);
    JButtonFixture cutButton = getButtonByTooltip("Cut",window);
    cutButton.requireVisible();
    cutButton.requireEnabled();
    textArea.selectAll();
    cutButton.click();
    assertThat(textArea.text()).contains("");
    JButtonFixture pasteButton = getButtonByTooltip("Paste",window);
    pasteButton.click();
    pasteButton.click();
    assertThat(textArea.text()).contains(texttext);
  }
  
  @Test
  public void testFormatToolBar() {
    JTextComponentFixture textArea = window.textBox();
    String text = "Hello";
    textArea.enterText(text);
    JButtonFixture fontButton = getButtonByTooltip("Font",window);
    fontButton.requireVisible();
    fontButton.requireEnabled();
    textArea.selectAll();
    textArea.font().requireSize(13);
    assertThat(textArea.text()).contains(text);
    fontButton.click();
    GenericTypeMatcher<JButton> buttonMatcher = new GenericTypeMatcher<JButton>(
        JButton.class) {
      @Override
      protected boolean isMatching(JButton button) {
        return button.getText().equals("OK");
      }
    };
    window.dialog().button(buttonMatcher).click();
    textArea.font().requireSize(8);
  }
  
  @Test
  public void testPersistenceToolBar() {
    JTextComponentFixture textArea = window.textBox();
    String text = "Hello";
    textArea.enterText(text);
    JButtonFixture saveAs = getButtonByTooltip("Save As",window);
    saveAs.requireVisible();
    saveAs.requireEnabled();
  }
  
  @Test
  public void testPrintToolBar() {
    JButtonFixture print = getButtonByTooltip("Print",window);
    print.requireVisible();
    print.requireEnabled();
  }
  
  @Test
  public void testSearchToolBar() {
    JTextComponentFixture textArea = window.textBox();
    String text = "HelloWorld";
    textArea.enterText(text);
    JButtonFixture search = getButtonByTooltip("Find",window);
    search.requireVisible();
    search.requireEnabled();
    search.click();
    
    window.dialog().textBox().enterText("nomatch");
    
    GenericTypeMatcher<JButton> buttonMatcher = new GenericTypeMatcher<JButton>(
        JButton.class) {
      @Override
      protected boolean isMatching(JButton button) {
        return button.getText().equals("OK");
      }
    };
    window.dialog().button(buttonMatcher).click();
    window.dialog().close();
    window.dialog().textBox().enterText("Hello");
    window.dialog().button(buttonMatcher).click();
    
  }
  
  @Test
  public void testWordCountToolBar() {
    String sentence = "This is my sentence";
    window.textBox().setText(sentence);
    JButtonFixture wordCountButton = getButtonByTooltip("Word count",window);
    wordCountButton.requireVisible();
    wordCountButton.requireEnabled();
    wordCountButton.requireToolTip("Word count");
    wordCountButton.click();
    window.optionPane().requireInformationMessage().requireMessage("Word count: 4");
  }
  
  
  
  @Test
  public void testEditMenuBar() {
    JTextComponentFixture textArea = window.textBox();
    String text = "Hello";
    String texttext = "HelloHello";
    textArea.enterText(text);
    JMenuItemFixture cutButton = getMenuItem("Cut",window);
    cutButton.requireVisible();
    cutButton.requireEnabled();
    textArea.selectAll();
    cutButton.click();
    assertThat(textArea.text()).contains("");
    JMenuItemFixture pasteButton = getMenuItem("Paste",window);
    pasteButton.click();
    pasteButton.click();
    assertThat(textArea.text()).contains(texttext);
  }
  
  @Test
  public void testFormatMenuBar() {
    JTextComponentFixture textArea = window.textBox();
    String text = "Hello";
    textArea.enterText(text);
    JMenuItemFixture fontButton = getMenuItem("Font",window);
    fontButton.requireVisible();
    fontButton.requireEnabled();
    textArea.selectAll();
    textArea.font().requireSize(13);
    assertThat(textArea.text()).contains(text);
    fontButton.click();
    GenericTypeMatcher<JButton> buttonMatcher = new GenericTypeMatcher<JButton>(
        JButton.class) {
      @Override
      protected boolean isMatching(JButton button) {
        return button.getText().equals("OK");
      }
    };
    window.dialog().button(buttonMatcher).click();
    textArea.font().requireSize(8);
  }
  
  @Test
  public void testPersistenceMenuBar() {
    JTextComponentFixture textArea = window.textBox();
    String text = "Hello";
    textArea.enterText(text);
    JMenuItemFixture saveAs = getMenuItem("Save As",window);
    saveAs.requireVisible();
    saveAs.requireEnabled();
  }
  
  @Test
  public void testPrintMenuBar() {
    JMenuItemFixture print = getMenuItem("Print",window);
    print.requireVisible();
    print.requireEnabled();
  }
  
  @Test
  public void testSearchMenuBar() {
    JTextComponentFixture textArea = window.textBox();
    String text = "HelloWorld";
    textArea.enterText(text);
    JMenuItemFixture search = getMenuItem("Find",window);
    search.requireVisible();
    search.requireEnabled();
    search.click();
    
    window.dialog().textBox().enterText("nomatch");
    
    GenericTypeMatcher<JButton> buttonMatcher = new GenericTypeMatcher<JButton>(
        JButton.class) {
      @Override
      protected boolean isMatching(JButton button) {
        return button.getText().equals("OK");
      }
    };
    window.dialog().button(buttonMatcher).click();
    window.dialog().close();
    window.dialog().textBox().enterText("Hello");
    window.dialog().button(buttonMatcher).click();
    
  }
  
  @Test
  public void testWordCountMenuBar() {
    String sentence = "This is my sentence";
    window.textBox().setText(sentence);
    JMenuItemFixture wordCountButton = getMenuItem("Word Count",window);
    wordCountButton.requireVisible();
    wordCountButton.requireEnabled();
    wordCountButton.click();
    window.optionPane().requireInformationMessage().requireMessage("Word count: 4");
  }
  
  
  private JButtonFixture getButtonByTooltip(final String msg, ContainerFixture frame) {
    GenericTypeMatcher<JButton> buttonMatcher = new GenericTypeMatcher<JButton>(
        JButton.class) {
      @Override
      protected boolean isMatching(JButton button) {
        return button.getToolTipText().equals(msg);
      }
    };
    return frame.button(buttonMatcher);
  }
  
  private JMenuItemFixture getMenuItem(final String msg, ContainerFixture frame) {
    GenericTypeMatcher<JMenuItem> buttonMatcher = new GenericTypeMatcher<JMenuItem>(
        JMenuItem.class) {
      @Override
      protected boolean isMatching(JMenuItem button) {
        return button.getText().equals(msg) && button.isShowing() == false;
      }
    };
    return frame.menuItem(buttonMatcher);
  }
  
  
  @After
  public void teardown() {
    window.cleanUp();
//    NotepadVariables.getSINGLETON().restore();
  }

}
