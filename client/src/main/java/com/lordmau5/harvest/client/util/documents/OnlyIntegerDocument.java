package com.lordmau5.harvest.client.util.documents;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 12:51
 */
public class OnlyIntegerDocument extends PlainDocument {
    public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }

        try {
            if (getLength() < 5 && str.length() < 5 && getLength() + str.length() <= 5) {
                if (Integer.parseInt(str) != 0) {
                    super.insertString(offs, str, a);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}