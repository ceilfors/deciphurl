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

import spock.lang.Shared
import spock.lang.Specification

/**
 * @author ceilfors
 */
class RegexUrlInterpolatorTest extends Specification {

    @Shared
    def ruleMap = GroovyConfigLoader.loadRuleMap(this.class.getResource("config.conf"))

    @Shared
    def interpolator = new RegexUrlInterpolator()

    def "Should interpolate variable"() {
        when:
        def result = interpolator.interpolate(rule, input)

        then:
        result == expected

        where:
        rule          | input        | expected
        ruleMap.jira    | "GRADLE-123" | "http://my-jira/GRADLE-123"
        ruleMap.jira    | "HHH-123"    | "http://my-jira/HHH-123"
        ruleMap.jenkins | "#my123"     | "http://my-jenkins/123"
        ruleMap.jenkins | "#my999"     | "http://my-jenkins/999"
    }

    def "Should interpolate multi match variable"() {
        def multiMatchRule = new Expando()
        multiMatchRule.pattern = /(\d)(\d)/
        multiMatchRule.url = "multiMatch/{2.2}{2.1}{1.2}{1.1}{0.2}{0.1}{2}{1}"

        when:
        def result = interpolator.interpolate(multiMatchRule, input)

        then:
        result == expected

        where:
        input    | expected
        "123456" | "multiMatch/65432121"
        "654321" | "multiMatch/12345656"
    }

    def "Should skip interpolating when variable is not available"() {
        def failRule = new Expando()
        failRule.pattern = /(\d)(\d)/
        failRule.url = "{1}url/{3.3}{2.3}{1.3}{0.3}{1}"

        when:
        def result = interpolator.interpolate(failRule, input)

        then:
        result == expected

        where:
        input    | expected
        "123456" | "1url/{3.3}{2.3}{1.3}{0.3}1"
        "654321" | "6url/{3.3}{2.3}{1.3}{0.3}6"
    }
}
