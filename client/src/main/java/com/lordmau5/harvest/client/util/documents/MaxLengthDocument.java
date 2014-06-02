package com.lordmau5.harvest.client.util.documents;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 12:46
 */
public class MaxLengthDocument extends PlainDocument {
    private int maxLength = Integer.MAX_VALUE;

    public MaxLengthDocument(final int maxLength) {
        this.maxLength = maxLength;
    }

    public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }

        int actualLength = this.getLength();
        if (actualLength + str.length() < this.maxLength) {
            super.insertString(offs, str, a);
        }
    }
}