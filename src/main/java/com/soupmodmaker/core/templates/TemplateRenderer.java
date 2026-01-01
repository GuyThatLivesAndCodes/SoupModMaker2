package com.soupmodmaker.core.templates;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Renders FreeMarker templates with context data.
 * Central template rendering engine for code generation.
 */
public class TemplateRenderer {
    private static final Logger logger = LoggerFactory.getLogger(TemplateRenderer.class);
    private final Configuration freemarkerConfig;

    public TemplateRenderer() {
        this.freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32);

        // Templates are in src/main/resources/templates
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
        freemarkerConfig.setDefaultEncoding("UTF-8");
        freemarkerConfig.setLogTemplateExceptions(false);
        freemarkerConfig.setWrapUncheckedExceptions(true);
    }

    /**
     * Render a template with the given data model.
     *
     * @param templatePath Path to template (e.g., "forge-1.12.2/MainMod.java.ftl")
     * @param dataModel Data to pass to the template
     * @return Rendered string
     * @throws IOException if template cannot be loaded
     * @throws TemplateException if template rendering fails
     */
    public String render(String templatePath, Map<String, Object> dataModel) throws IOException, TemplateException {
        logger.debug("Rendering template: {}", templatePath);

        Template template = freemarkerConfig.getTemplate(templatePath);
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);

        return writer.toString();
    }

    /**
     * Check if a template exists.
     *
     * @param templatePath Path to template
     * @return true if template exists, false otherwise
     */
    public boolean templateExists(String templatePath) {
        try {
            freemarkerConfig.getTemplate(templatePath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
