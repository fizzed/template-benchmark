package com.mitchellbosecke.benchmark;

import groovy.lang.Writable;
import groovy.text.Template;
import groovy.text.markup.MarkupTemplateEngine;
import groovy.text.markup.TemplateConfiguration;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.io.*;
import java.util.Map;

public class Groovy extends BaseBenchmark {

    private Map<String, Object> context;

    private Template template;

    @Setup
    public void setup() {
        TemplateConfiguration config = new TemplateConfiguration();
        config.setAutoEscape(false);
        config.setUseDoubleQuotes(true);
        MarkupTemplateEngine engine = new MarkupTemplateEngine(config);
        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream("templates/stocks.groovy.tpl");
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }

        try {
            template = engine.createTemplate(reader);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }


        this.context = getContext();
    }

    @Benchmark
    public String benchmark() {
        Writer writer = new StringWriter();
        Writable output = template.make(context);
        try {
            output.writeTo(writer);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return writer.toString();
    }

}
