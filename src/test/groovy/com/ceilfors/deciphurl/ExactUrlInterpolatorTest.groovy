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
class ExactUrlInterpolatorTest extends Specification {

    @Shared
    def interpolator = new ExactUrlInterpolator()

    def "Should interpolate variable"() {
        def exactRule = new Expando()
        exactRule.pattern = /(\d)(\d)/
        exactRule.url = "{1}url/{3.3}{2.3}{1.3}{0.3}{1}"
        exactRule.regex = false

        when:
        def result = interpolator.interpolate(exactRule, input)

        then:
        result == expected

        where:
        input | expected
        "1"   | "1url/11111"
        "6"   | "6url/66666"
    }
}
