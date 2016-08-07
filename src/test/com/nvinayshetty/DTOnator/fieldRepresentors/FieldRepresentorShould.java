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

package test.com.nvinayshetty.DTOnator.fieldRepresentors;


import com.nvinayshetty.DTOnator.FeedValidator.KeywordClassifier;
import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldRepresentors.*;
import com.nvinayshetty.DTOnator.NameConventionCommands.CamelCase;
import com.nvinayshetty.DTOnator.NameConventionCommands.FieldNameParser;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolver;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolverCommand;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by vinay on 1/8/15.
 */
public class FieldRepresentorShould {
    private FieldNameParser fieldNameParser;
    private FieldRepresentor fieldRepresentor;
    private HashSet<NameConflictResolverCommand> nameConflictResolverCommands;
    private HashSet<NameParserCommand> nameParserCommands;
    private NameConflictResolver nameConflictResolver;
    private KeywordClassifier keywordClassifier;
    private CamelCase camelCase;
    private String ObjectName;
    private String validSimpleFieldName;
    private String nameNotInCamelCase;
    private String nameInCamelCase;
    private String invalidIdentifier;


    @Before
    public void setUp() throws Exception {
        validSimpleFieldName = "valid";
        ObjectName = "Valid";
        nameNotInCamelCase="VALID_FIELD_NAME";
        nameInCamelCase="validFieldName";
        invalidIdentifier="invalid identifier";
        nameParserCommands = new HashSet<>();
        nameConflictResolverCommands = new HashSet<>();
        keywordClassifier = new KeywordClassifier();
        fieldNameParser = new FieldNameParser(nameParserCommands);
        nameConflictResolver = new NameConflictResolver(nameConflictResolverCommands);
        camelCase = new CamelCase();
    }


    @Test
    public void createBooleanFieldRepresentation() {
        fieldRepresentor = new BooleanFieldRepresentor();
        String actualBooleanField = fieldRepresentor.fieldCreationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver, keywordClassifier);
        String expectedBooleanField = "public boolean " + validSimpleFieldName + ";";
        assertEquals(expectedBooleanField, actualBooleanField);
    }

    @Test
    public void createBooleanFieldRepresentationWithGsonAnnotations() {
        fieldRepresentor = new BooleanFieldRepresentor();
        String actualBooleanField = fieldRepresentor.gsonFieldRepresentationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver);
        String expectedFieldWithAnnotation = getGsonAnnotation(validSimpleFieldName) + "public boolean " + validSimpleFieldName + ";";
        assertEquals(expectedFieldWithAnnotation, actualBooleanField);
    }

    @Test
    public void createBooleanFieldRepresentationInCamelCaseWithGsonAnnotations() {
        fieldRepresentor = new BooleanFieldRepresentor();
        nameParserCommands.add(new CamelCase());
        String actualBooleanField = fieldRepresentor.gsonFieldRepresentationTemplate(AccessModifier.PUBLIC, nameNotInCamelCase, fieldNameParser, nameConflictResolver);
        String expectedFieldWithAnnotation = getGsonAnnotation(nameNotInCamelCase) + "public boolean " + nameInCamelCase + ";";
        assertEquals(expectedFieldWithAnnotation, actualBooleanField);
    }

    @Test
    public void createBooleanFieldRepresentationShouldShowAnEroorNotification() {
        fieldRepresentor = new BooleanFieldRepresentor();
        String actualBooleanField = fieldRepresentor.gsonFieldRepresentationTemplate(AccessModifier.PUBLIC, invalidIdentifier, fieldNameParser, nameConflictResolver);
       // String expectedFieldWithAnnotation = getGsonAnnotation(invalidIdentifier) + "public boolean " + invalidIdentifier + ";";
        //assertEquals(expectedFieldWithAnnotation, actualBooleanField);
    }

    @Test
    public void createBooleanFieldRepresentationInCamelCaseWithGsonAnnotationsShouldShowAnError() {
        String invalidIdentifierAfterCamelCasing="Return";
        String restoredFieldAfterCamelCasing="Return";
        fieldRepresentor = new BooleanFieldRepresentor();
        nameParserCommands.add(new CamelCase());
        String actualBooleanField = fieldRepresentor.gsonFieldRepresentationTemplate(AccessModifier.PUBLIC, invalidIdentifierAfterCamelCasing, fieldNameParser, nameConflictResolver);
        String expectedFieldWithAnnotation = getGsonAnnotation(invalidIdentifierAfterCamelCasing) + "public boolean " + restoredFieldAfterCamelCasing + ";";
        assertEquals(expectedFieldWithAnnotation, actualBooleanField);
    }

    @NotNull
    private String getGsonAnnotation(String key) {
        return "@com.google.gson.annotations.SerializedName(\"" + key + "\")" + "\n";
    }

    @Test
    public void createDoubleFieldRepresentation() {
        fieldRepresentor = new DoubleFieldRepresentor();
        String actualDoubleField = fieldRepresentor.fieldCreationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver, keywordClassifier);
        String expectedDoubleField = "public double " + validSimpleFieldName + ";";
        assertEquals(expectedDoubleField, actualDoubleField);
    }

    @Test
    public void createDoubleFieldRepresentationWithGsonAnnotations() {
        fieldRepresentor = new DoubleFieldRepresentor();
        String actualDoubleField = fieldRepresentor.gsonFieldRepresentationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver);
        String expectedDoubleField = getGsonAnnotation(validSimpleFieldName) + "public double " + validSimpleFieldName + ";";
        assertEquals(expectedDoubleField, actualDoubleField);
    }

    @Test
    public void createIntegerFieldRepresentation() {
        fieldRepresentor = new IntegerFieldRepresentor();
        String actualIntegerField = fieldRepresentor.fieldCreationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver, keywordClassifier);
        String expectedIntegerField = "public int " + validSimpleFieldName + ";";
        assertEquals(expectedIntegerField, actualIntegerField);
    }

    @Test
    public void createIntegerFieldRepresentationWithGsonAnnotation() {
        fieldRepresentor = new IntegerFieldRepresentor();
        String actualIntegerField = fieldRepresentor.gsonFieldRepresentationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver);
        String expectedIntegerField = getGsonAnnotation(validSimpleFieldName) + "public int " + validSimpleFieldName + ";";
        assertEquals(expectedIntegerField, actualIntegerField);
    }

    @Test
    public void createStringFieldRepresentation() {
        fieldRepresentor = new StringFieldRepresentor();
        String actualStringField = fieldRepresentor.fieldCreationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver, keywordClassifier);
        String expectedStringField = "public String " + validSimpleFieldName + ";";
        assertEquals(expectedStringField, actualStringField);
    }


    @Test
    public void createStringFieldRepresentationWithGsonAnnotation() {
        fieldRepresentor = new StringFieldRepresentor();
        String actualStringField = fieldRepresentor.gsonFieldRepresentationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver);
        String expectedStringField = getGsonAnnotation(validSimpleFieldName) + "public String " + validSimpleFieldName + ";";
        assertEquals(expectedStringField, actualStringField);
    }


    @Test
    public void createJsonObjectFieldRepresentation() {
        fieldRepresentor = new JsonObjectRepresentor();
        String actualJsonObjectField = fieldRepresentor.fieldCreationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver, keywordClassifier);
        String expectedJsonObjectField = "public " + ObjectName + " " + validSimpleFieldName + ";";
        assertEquals(expectedJsonObjectField, actualJsonObjectField);
    }

    @Test
    public void createJsonObjectFieldRepresentationWithGsonAnnotation() {
        fieldRepresentor = new JsonObjectRepresentor();
        String actualJsonObjectField = fieldRepresentor.gsonFieldRepresentationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver);
        String expectedJsonObjectField = getGsonAnnotation(validSimpleFieldName) + "public " + ObjectName + " " + validSimpleFieldName + ";";
        assertEquals(expectedJsonObjectField, actualJsonObjectField);
    }

    @Test
    public void createJsonArrayFieldRepresentation() {
        JsonArrayRepresentor jsonArrayRepresentor = new JsonArrayRepresentor();
        jsonArrayRepresentor.setDataType(ObjectName);
        fieldRepresentor = jsonArrayRepresentor;
        String actualJsonArrayField = fieldRepresentor.fieldCreationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver, keywordClassifier);
        String expectedJsonArrayField = "public java.util.List<" + ObjectName + ">" + " " + validSimpleFieldName + ";";
        assertEquals(expectedJsonArrayField, actualJsonArrayField);
    }

    @Test
    public void createJsonArrayFieldRepresentationWithGsonAnnotation() {
        JsonArrayRepresentor jsonArrayRepresentor = new JsonArrayRepresentor();
        jsonArrayRepresentor.setDataType(ObjectName);
        fieldRepresentor = jsonArrayRepresentor;
        String actualJsonArrayField = fieldRepresentor.gsonFieldRepresentationTemplate(AccessModifier.PUBLIC, validSimpleFieldName, fieldNameParser, nameConflictResolver);
        String expectedJsonArrayField = getGsonAnnotation(validSimpleFieldName) + "public java.util.List<" + ObjectName + ">" + " " + validSimpleFieldName + ";";
        assertEquals(expectedJsonArrayField, actualJsonArrayField);
    }

    @After
    public void tearDown() throws Exception {
        nameConflictResolverCommands.clear();
        nameConflictResolverCommands.clear();
    }
}
