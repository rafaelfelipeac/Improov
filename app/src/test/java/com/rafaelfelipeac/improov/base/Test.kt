package com.rafaelfelipeac.improov.base

import junit.framework.TestCase.assertEquals

infix fun <T> T.equalTo(expected: T?): T = this.apply {
    assertEquals(
        expected,
        this
    )
}
