package com.soupmodmaker.core.codegen;

/**
 * Represents generated code with metadata.
 */
public class GeneratedCode {
    private final String className;
    private final String packageName;
    private final String sourceCode;
    private final String filePath;

    public GeneratedCode(String className, String packageName, String sourceCode, String filePath) {
        this.className = className;
        this.packageName = packageName;
        this.sourceCode = sourceCode;
        this.filePath = filePath;
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFullyQualifiedName() {
        return packageName + "." + className;
    }

    @Override
    public String toString() {
        return "GeneratedCode{" +
                "className='" + className + '\'' +
                ", packageName='" + packageName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
