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
import com.nvinayshetty.DTOnator.FeedValidator.KeywordClassifier;
import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldCreator.LanguageType;
import com.nvinayshetty.DTOnator.NameConventionCommands.FieldNameParser;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolver;
import com.sun.org.apache.bcel.internal.generic.SWITCH;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by vinay on 6/6/15.
 */
public abstract class FieldRepresentor {
    public static final String JACKSON_ANNOTAION_PREFIX = "@com.fasterxml.jackson.annotation.JsonProperty(\"";
    public static final String JACKSON_ANNOTAION_SHORT_PREFIX = "@JsonProperty(\"" ;
    protected static final String GSON_ANNOTATION_PREFIX = "@com.google.gson.annotations.SerializedName(\"";
    protected static final String GSON_ANNOTATION_PREFIX_SHORT = "@SerializedName(\"";
    protected static final String ANNOTATION_SUFFIX = "\")\n";
    private static final String SPACE = " ";
    private static final String CLASS_FIELD_SUFFIX = ";";
    private Project project;
    private KeywordClassifier keywordClassifier = new KeywordClassifier();


    protected static String suffix(String key) {
        return new StringBuilder().append(SPACE).append(key).append(CLASS_FIELD_SUFFIX).toString();
    }

    private static String getGsonAnnotationFor(String key, LanguageType languageType) {
        switch (languageType){
            case JAVA:
                return GSON_ANNOTATION_PREFIX + key + ANNOTATION_SUFFIX;
            default:
                return GSON_ANNOTATION_PREFIX_SHORT + key + "\")\n";
        }

    }

    private static String getJacksonAnnotationFor(String key, LanguageType languageType) {
        switch (languageType){
            case JAVA:
                return JACKSON_ANNOTAION_PREFIX + key + ANNOTATION_SUFFIX;
            default:
                return JACKSON_ANNOTAION_SHORT_PREFIX + key +  "\")\n";

        }
    }

    public final String fieldCreationTemplate(LanguageType languageType, AccessModifier accessModifier, String key, FieldNameParser parser, NameConflictResolver nameConflictResolver, KeywordClassifier keywordClassifier) {
        String parsedFieldName = parse(key, parser, nameConflictResolver, keywordClassifier);
        switch (languageType) {
            case JAVA:
                return getFieldRepresentationFor(accessModifier, parsedFieldName);
            case KOTLIN_VAL:
                return getKotlinValFieldRepresentationFor(accessModifier, parsedFieldName);
            case KOTLIN_VAR:
                return getKotlinVarFieldRepresentationFor(accessModifier, parsedFieldName);
            default:
                return getFieldRepresentationFor(accessModifier, parsedFieldName);
        }

    }

    public final String gsonFieldRepresentationTemplate(LanguageType languageType, AccessModifier AccessModifier, String key, FieldNameParser parser, NameConflictResolver nameConflictResolver) {

        return getGsonAnnotationFor(key,languageType) + fieldCreationTemplate(languageType, AccessModifier, key, parser, nameConflictResolver, keywordClassifier);
    }

    public final String gsonFieldWithExposeAnnotationTemplate(LanguageType languageType, AccessModifier AccessModifier, String key, FieldNameParser parser, NameConflictResolver nameConflictResolver) {
        return getExposeAnnotation(languageType) + "\n" + getGsonAnnotationFor(key, languageType) + fieldCreationTemplate(languageType, AccessModifier, key, parser, nameConflictResolver, keywordClassifier);
    }

    public final String jacksonFieldRepresentationTemplate(LanguageType languageType, AccessModifier AccessModifier, String key, FieldNameParser parser, NameConflictResolver nameConflictResolver) {
        return getJacksonAnnotationFor(key,languageType) + fieldCreationTemplate(languageType, AccessModifier, key, parser, nameConflictResolver, keywordClassifier);
    }

    public final String customFieldRepresentationTemplate(LanguageType languageType, AccessModifier AccessModifier, String key, FieldNameParser parser, NameConflictResolver nameConflictResolver,String customFieldAnnotation) {
        return getCustomFieldAnnotation(customFieldAnnotation+"\n",key) + fieldCreationTemplate(languageType, AccessModifier, key, parser, nameConflictResolver, keywordClassifier);
    }

    private String getCustomFieldAnnotation(String customFieldAnnotation, String key) {
       return String.format(customFieldAnnotation,key);
    }


    @NotNull
    private String getExposeAnnotation(LanguageType languageType) {
        switch (languageType){
            case JAVA:
                return "@com.google.gson.annotations.Expose";
             default:return "@Expose";
        }

    }

    protected abstract String getFieldRepresentationFor(AccessModifier accessModifier, String key);

    protected abstract String getKotlinValFieldRepresentationFor(AccessModifier accessModifier, String key);

    protected abstract String getKotlinVarFieldRepresentationFor(AccessModifier accessModifier, String key);



    private String parse(final String key, FieldNameParser parser, NameConflictResolver nameConflictResolver, KeywordClassifier keywordClassifier) {
        if (parser == null)
            return key;
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
        if (!keywordClassifier.isValidJavaIdentifier(fieldRepresentation)) {
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
        return (keywordClassifier.isValidJavaIdentifier(key));
    }

    public void setProject(Project project) {
        this.project = project;
    }


    public String getAutoVlaueField(LanguageType languageType, AccessModifier accessModifier, String key, FieldNameParser parser, NameConflictResolver nameConflictResolver) {
      //Todo: move to each data type
       return getGsonAnnotationFor(key,languageType)+fieldCreationTemplate(languageType, accessModifier, key, parser, nameConflictResolver, keywordClassifier).replace(CLASS_FIELD_SUFFIX,"()")+";";
    }
}
