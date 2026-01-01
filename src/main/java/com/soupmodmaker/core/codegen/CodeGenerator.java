package com.soupmodmaker.core.codegen;

import com.soupmodmaker.core.workspace.WorkspaceElement;

/**
 * Interface for code generators.
 * Implementations generate Java code from workspace elements.
 */
public interface CodeGenerator {

    /**
     * Generate code for a workspace element.
     *
     * @param element The element to generate code for
     * @param context The code generation context
     * @return The generated code result
     */
    GeneratedCode generate(WorkspaceElement element, CodeContext context);

    /**
     * Check if this generator supports the given element type.
     *
     * @param elementType The element type to check
     * @return true if supported, false otherwise
     */
    boolean supports(String elementType);
}
