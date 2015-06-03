package com.nvinayshetty.DTOnator.Factory;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.ClassAdder.PackageClassAdder;
import com.nvinayshetty.DTOnator.DtoGenerators.FeedType;
import com.nvinayshetty.DTOnator.FeedParser.JsonDtoGenerator;
import com.nvinayshetty.DTOnator.FieldCreator.simpleFieldCreator;
import org.json.JSONObject;

/**
 * Created by vinay on 17/5/15.
 */
public class DtoGenerationFactory {
    public static Void getDtoGeneratorFor(FeedType type, Project project, PsiFile psiFile, JSONObject validFeed, PsiClass psiClass) {
        switch (type) {
            case JsonObject:
                new JsonDtoGenerator(project, psiFile, validFeed, psiClass, new simpleFieldCreator(), new PackageClassAdder(psiFile.getContainingDirectory())).execute();
        }
        return null;
    }
}
