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
class SequentialRuleResolverTest extends Specification {

    @Shared
    def ruleMap = GroovyConfigLoader.loadRuleMap(this.class.getResource("config.conf"))

    @Shared
    def resolver = new SequentialRuleResolver(ruleMap: ruleMap)

    void "Should resolve regex rule"() {
        expect:
        resolver.resolve(input) == expected

        where:
        input       | expected
        "HHH-12345" | ruleMap.jira
        "ABC-1"     | ruleMap.jira
        "#my1234"   | ruleMap.jenkins
    }

    void "Should not resolve regex rule"() {
        expect:
        resolver.resolve(input) == expected

        where:
        input      | expected
        "H H-1234" | null
        "A1C-1"    | null
        "#yo1234"  | null
        "Call to extension: 23106" | null
    }

    void "Should resolve exact rule"() {
        expect:
        resolver.resolve(input) == expected

        where:
        input    | expected
        "google" | ruleMap.exactString
        12345    | ruleMap.exactInteger
    }
}
