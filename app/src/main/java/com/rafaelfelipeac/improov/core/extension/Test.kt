package com.rafaelfelipeac.improov.core.extension

import junit.framework.TestCase

// O que isso tá fazendo no código de produção?
infix fun <T> T.equalTo(expected: T?): T = this.apply {
    TestCase.assertEquals(
        expected,
        this
    )
}
