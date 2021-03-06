package com.zhqydot.framework.easyrouter.compiler;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

    private static final String PACKAGE_NAME = "com.zhqydot.framework.easyrouter.core";
    private static final String LOADER_CLASS_NAME_PREFIX = "RouteLoader";
    private static final String PACKAGE_DELIMITER = ".";
    private static final String CLASS_DELIMITER = "$";
    private static final String PATH_REGEX = "/[a-zA-Z0-9]+/[a-zA-Z0-9]+";

    private Filer mFiler;
    private Elements mElements;
    private Types mTypes;
    private String mModuleName;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Route.class.getCanonicalName());
        return annotations;
    }

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElements = processingEnv.getElementUtils();
        mTypes = processingEnv.getTypeUtils();
        mModuleName = ModuleUtils.getModuleName(mFiler).toLowerCase();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> routerElements = roundEnvironment.getElementsAnnotatedWith(Route.class);
        Map<String, TypeElement> routeMap = new HashMap<>();
        TypeMirror typeActivity = mElements.getTypeElement("android.app.Activity").asType();
        Pattern pattern = Pattern.compile(PATH_REGEX);
        for (Element element : routerElements) {
            if (!mTypes.isSubtype(element.asType(), typeActivity)) {
                throw new RuntimeException("Unsupported class type, type is [" + element.asType().toString() + "], only support Activity now.");
            }
            String path = element.getAnnotation(Route.class).path();
            Matcher matcher = pattern.matcher(path);
            if (!matcher.matches()) {
                throw new RuntimeException("Can not resolve the path [" + path + "].");
            }
            routeMap.put(element.getAnnotation(Route.class).path(), (TypeElement) element);
        }
        createFile(routeMap);
        return true;
    }

    private void createFile(Map<String, TypeElement> routeMap) {
        try {
            createJavaFile(getLoaderClassFullName(), routeMap, map -> {
                StringBuilder builder = new StringBuilder();
                builder.append("package com.zhqydot.framework.easyrouter.core;\n\n");
                builder.append("import com.zhqydot.framework.easyrouter.core.common.IRouteLoader;\n");
                builder.append("import com.zhqydot.framework.easyrouter.core.common.EasyRouter;\n");
                for (Map.Entry<String, TypeElement> entry : routeMap.entrySet()) {
                    builder.append("import ").append(entry.getValue().getQualifiedName()).append(";\n");
                }
                appendComment(builder);
                builder.append("public class ").append(getLoaderClassSimpleName()).append(" implements IRouteLoader { \n\n");
                builder.append("\t@Override\n");
                builder.append("\tpublic void load() { \n");
                for (Map.Entry<String, TypeElement> entry : routeMap.entrySet()) {
                    builder.append("\t\t");
                    builder.append(String.format("EasyRouter.register(\"%s\", %s.class);", entry.getKey(), entry.getValue().getSimpleName()));
                    builder.append("\n");
                }
                builder.append("\t}\n");
                builder.append("}");
                return builder.toString();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createJavaFile(String fileName, Map<String, TypeElement> routeMap, CodeBuilder builder) throws IOException {
        JavaFileObject jfo = mFiler.createSourceFile(fileName);
        Writer writer = jfo.openWriter();
        writer.write(builder.build(routeMap));
        writer.flush();
        writer.close();
    }

    private void appendComment(StringBuilder builder) {
        builder.append("\n/**\n");
        builder.append(" * This class was generated by EasyRouter\n");
        builder.append(" * do not modify it\n");
        builder.append(" * or it will cause EasyRouter run with exception\n");
        builder.append(" */\n\n");
    }

    private String getLoaderClassSimpleName() {
        return LOADER_CLASS_NAME_PREFIX + CLASS_DELIMITER + mModuleName;
    }

    private String getLoaderClassFullName() {
        return PACKAGE_NAME + PACKAGE_DELIMITER + getLoaderClassSimpleName();
    }

    private interface CodeBuilder {
        String build(Map<String, TypeElement> routeMap);
    }

}