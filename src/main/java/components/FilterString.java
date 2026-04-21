package components;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

//this class filters numbers and symbols out of strings, mainly for names
public class FilterString extends DocumentFilter{

    //remove all foreign characters/symbols
    private String filterLetters(String text) {
        return text.replaceAll("[^\\p{L}'\\- ,]", "");
    }

    //insert filtered string into text
    public void insertString(FilterBypass f, int offset, int length,
            String str, AttributeSet attr) throws BadLocationException {
        super.insertString(f, offset, filterLetters(str), attr);
    }

    //replace old string with new filtered text
    @Override
    public void replace(FilterBypass f, int offset, int length,
            String str, AttributeSet attr)
            throws BadLocationException {
                super.replace(f, offset, length, filterLetters(str), attr);
            }
}
