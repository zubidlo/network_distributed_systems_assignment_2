package assignment_2.helperClasses;

import javax.swing.text.*;

/**
 * Document adjustment
 * Created by martin on 15/04/2015.
 */
public class JTextFieldLimiter extends PlainDocument {

    private final int limit;

    public JTextFieldLimiter(final int limit) {
        super();
        this.limit = limit;
    }

    public void insertString( int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;
        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
