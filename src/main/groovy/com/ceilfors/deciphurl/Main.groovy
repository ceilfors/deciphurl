/*
 * Copyright (c) 2013 Wisen Tanasa
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ceilfors.deciphurl

import groovy.text.GStringTemplateEngine
import groovy.util.logging.Slf4j

import java.awt.*

import static com.ceilfors.deciphurl.GroovyConfigLoader.loadRuleMap

/**
 * @author ceilfors
 */
@Slf4j
public class Main {

    static createAndPrepareTempDir() {
        def tempDir = new File(System.getProperty("java.io.tmpdir"), "com.ceilfors.deciphurl")
        tempDir.mkdirs()
        def cssFile = new File(tempDir, "bootstrap.min.css")
        if (!cssFile.exists()) {
            cssFile.write(this.getResource("template/bootstrap.min.css").text)
        }
        return tempDir
    }

    static getRuleMap() {
        def configFile = new File("../conf/config.groovy")
        if (configFile.exists()) {
            if (configFile.lastModified() != configFileLastModified) {
                configFileLastModified = configFile.lastModified()
                configFileRuleMap = loadRuleMap(configFile.toURI().toURL())
                return configFileRuleMap
            } else {
                return configFileRuleMap
            }
        } else {
            if (!jarRuleMap) {
                jarRuleMap = loadRuleMap(this.getResource("config.groovy"))
            }
            return jarRuleMap
        }
    }
    static configFileLastModified
    static configFileRuleMap
    static jarRuleMap
    static regexUrlInterpolator = new RegexUrlInterpolator()
    static exactUrlInterpolator = new ExactUrlInterpolator()
    static errorTemplate = new GStringTemplateEngine().createTemplate(
            this.getResource("template/error.template.html").toURI().toURL())

    public static void main(String[] args) {
        try {
            def input = args ? args[0] : "no input specified"
            log.info("Input given: {}", input)
            def ruleMap = getRuleMap()
            def rule = new SequentialRuleResolver(ruleMap: ruleMap).resolve(input)

            if (rule) {
                log.info("Rule pattern used: {}", rule.pattern)
                def interpolator = rule.regex ? regexUrlInterpolator : exactUrlInterpolator
                Desktop.getDesktop().browse(new URI(interpolator.interpolate(rule, URLEncoder.encode(input, "UTF-8"))))

            } else {
                log.info("Couldn't find a matching rule, creating error.html")
                def errorFile = new File(createAndPrepareTempDir(), "error.html")
                def writer = errorFile.newWriter()
                errorTemplate.make(
                        'message': "Rule not found", 'description': "Rule with the matching pattern was not found",
                        'ruleMap': ruleMap, 'input': input).writeTo(writer)
                writer.close()

                Desktop.getDesktop().browse(errorFile.toURI())
            }
        } catch (Exception e) {
            log.error("Unexpected error.", e)
        }
    }
}

