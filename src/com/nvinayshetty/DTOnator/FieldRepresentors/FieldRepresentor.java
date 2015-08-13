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

package com.nvinayshetty.DTOnator.FieldRepresentors;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.nvinayshetty.DTOnator.FeedValidator.KeywordClasifier;
import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.NameConventionCommands.FieldNameParser;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolver;

import javax.swing.*;

/**
 * Created by vinay on 6/6/15.
 */
public abstract class FieldRepresentor {
    protected static final String GSON_ANNOTATION_PREFIX = "@com.google.gson.annotations.SerializedName(\"";
    protected static final String ANNOTATION_SUFFIX = "\")\n";
    private static final String SPACE = " ";
    private static final String CLASS_FIELD_SUFFIX = ";\n";
    Project project;
    private KeywordClasifier keywordClasifier = new KeywordClasifier();

    protected static String suffix(String key) {
        return new StringBuilder().append(SPACE).append(key).append(CLASS_FIELD_SUFFIX).toString();
    }

    private static String getGsonAnnotationFor(String key) {
        return GSON_ANNOTATION_PREFIX + key + ANNOTATION_SUFFIX;
    }

    public final String simpleFieldCreationTemplate(AccessModifier AccessModifier, String key, FieldNameParser parser, NameConflictResolver nameConflictResolver, KeywordClasifier keywordClasifier) {
        String parsedFieldName = parse(key, parser, nameConflictResolver, keywordClasifier);
        return getSimpleFieldRepresentationFor(AccessModifier, parsedFieldName);
    }

    public final String gsonFieldRepresentationTemplate(AccessModifier AccessModifier, String key, FieldNameParser parser, NameConflictResolver nameConflictResolver) {
        return getGsonAnnotationFor(key) + simpleFieldCreationTemplate(AccessModifier, key, parser, nameConflictResolver, keywordClasifier) + "\n\n";
    }

    protected abstract String getSimpleFieldRepresentationFor(AccessModifier AccessModifier, String key);

    public String parse(final String key, FieldNameParser parser, NameConflictResolver nameConflictResolver, KeywordClasifier keywordClasifier) {
        final String parsedField = parser.parseField(key.trim());
        String fieldRepresentation = parsedField;

        if (isVallidIdentifier(key) && !isVallidIdentifier(parsedField)) {
            fieldRepresentation = key;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final Notification processingNotification = new Notification("DtoGenerator", "Invalid Java Keyword", " DTOnator is unable to parse the key" + "\"" + key + "\"" + "as it becomes an invalid identifier " + "\"" + parsedField + "\"" + "after parsing", NotificationType.ERROR);
                    processingNotification.notify(project);
                }
            });
        } else if (!isVallidIdentifier(key)) {
            fieldRepresentation = nameConflictResolver.resolveNamingConflict(key);
        }
        if (!keywordClasifier.isValidJavaIdentifier(fieldRepresentation)) {
            fieldRepresentation = "THIS_IA_AN_INVALID_JAVA_IDENTIFIER_MANUALLY_RESOLVE_THE_ISSUE" + "//" + key;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final Notification processingNotification = new Notification("DtoGenerator", "Invalid Java Keyword", "\"" + key + "\"" + " is an invalid java identifier,DTonator  just stamped it an ugly name,please choose a better name!", NotificationType.ERROR);
                    processingNotification.notify(project);
                }
            });
        }
        return fieldRepresentation;
    }

    public boolean isVallidIdentifier(String key) {
        return (keywordClasifier.isValidJavaIdentifier(key));
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
