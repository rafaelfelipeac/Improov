package com.rafaelfelipeac.improov.base

import junit.framework.TestCase

infix fun <T> T.equal(expected: T?): T = this.apply {
    TestCase.assertEquals(
        expected,
        this
    )
}
