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

import groovy.util.logging.Slf4j

/**
 * @author ceilfors
 */
@Slf4j
class GroovyConfigLoader {

    static GroovyClassLoader classLoader = new GroovyClassLoader();

    /**
     * Solve PermGen leak issue when ConfigSlurper is called for multiple times.
     * http://groovy.329449.n5.nabble.com/ConfigSlurper-GroovyClassLoader-memory-leak-td364279.html
     */
    private static loadConfigObject(URL path, Closure closure) {
        Script script = (Script) classLoader.parseClass(path.text).newInstance()
        def config = new ConfigSlurper().parse(script)
        def result = closure(config)
        GroovySystem.getMetaClassRegistry().removeMetaClass(script.getClass())
        classLoader.clearCache()
        return result
    }

    static def loadRuleMap(URL path) {
        loadConfigObject(path) { config ->
            def rules = [:]
            config.rule.each {
                if (!it.value.containsKey("pattern")) {
                    log.error("Rule '{}' doesn't contain 'pattern' property, ignoring rule", it.key)
                    return
                }
                if (!it.value.containsKey("url")) {
                    log.error("Rule '{}' doesn't contain 'url' property, ignoring rule", it.key)
                    return
                }
                if (!it.value.containsKey("regex")) {
                    it.value.regex = true
                }
                rules[it.key] = it.value
            }
            return rules
        }
    }
}
