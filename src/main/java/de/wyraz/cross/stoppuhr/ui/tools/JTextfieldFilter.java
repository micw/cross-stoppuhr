package de.wyraz.cross.stoppuhr.ui.tools;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JTextfieldFilter extends PlainDocument {
	public static final String NUMERIC = "0123456789";

	protected final String acceptedChars;

	public JTextfieldFilter(String acceptedChars) {
		this.acceptedChars = acceptedChars;
	}

	public void insertString(int offset, String str, AttributeSet attr)
			throws BadLocationException {
		if (str == null)
			return;

		for (int i = 0; i < str.length(); i++) {
			if (acceptedChars.indexOf(str.valueOf(str.charAt(i))) == -1)
				return;
		}

		super.insertString(offset, str, attr);
	}
}