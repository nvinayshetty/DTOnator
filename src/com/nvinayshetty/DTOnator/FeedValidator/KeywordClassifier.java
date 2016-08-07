/*
 * Copyright (C) 2015 Vinaya Prasad N
 *
 *         This program is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         This program is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License
 *         along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nvinayshetty.DTOnator.FeedValidator;

import java.util.HashSet;

/**
 * Created by vinay on 27/7/15.
 */
public class KeywordClassifier {
    private HashSet<String> javaKeywords = new HashSet<String>();

    public void initKeywords() {
        javaKeywords.add("abstract");
        javaKeywords.add("continue");
        javaKeywords.add("for");
        javaKeywords.add("new");
        javaKeywords.add("switch");
        javaKeywords.add("assert");
        javaKeywords.add("default");
        javaKeywords.add("goto");
        javaKeywords.add("package");
        javaKeywords.add("synchronized");
        javaKeywords.add("boolean");
        javaKeywords.add("do");
        javaKeywords.add("if");
        javaKeywords.add("private");
        javaKeywords.add("this");
        javaKeywords.add("byte");
        javaKeywords.add("break");
        javaKeywords.add("double");
        javaKeywords.add("implements");
        javaKeywords.add("protected");
        javaKeywords.add("throw");
        javaKeywords.add("else");
        javaKeywords.add("import");
        javaKeywords.add("public");
        javaKeywords.add("throws");
        javaKeywords.add("case");
        javaKeywords.add("enum");
        javaKeywords.add("instanceof");
        javaKeywords.add("return");
        javaKeywords.add("transient");
        javaKeywords.add("catch");
        javaKeywords.add("char");
        javaKeywords.add("extends");
        javaKeywords.add("int");
        javaKeywords.add("short");
        javaKeywords.add("try");
        javaKeywords.add("final");
        javaKeywords.add("interface");
        javaKeywords.add("static");
        javaKeywords.add("void");
        javaKeywords.add("class");
        javaKeywords.add("finally");
        javaKeywords.add("long");
        javaKeywords.add("strictfp");
        javaKeywords.add("volatile");
        javaKeywords.add("const");
        javaKeywords.add("float");
        javaKeywords.add("native");
        javaKeywords.add("super");
        javaKeywords.add("while");
        javaKeywords.add("true");
        javaKeywords.add("false");
        javaKeywords.add("null");
    }

    public boolean isValidJavaIdentifier(String string) {
        initKeywords();
        if (string.isEmpty()) {
            return false;
        }
        if (!Character.isJavaIdentifierStart(string.charAt(0))) {
            return false;
        }
        for (int i = 1; i < string.length(); i++) {
            if (!Character.isJavaIdentifierPart(string.charAt(i))) {
                return false;
            }
        }
        return !javaKeywords.contains(string);
    }


}
