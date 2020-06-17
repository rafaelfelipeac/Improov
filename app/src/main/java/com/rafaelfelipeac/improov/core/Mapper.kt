package com.rafaelfelipeac.improov.core

interface Mapper<InType, OutType> {
    fun map(param: InType): OutType

    fun mapList(param: List<InType>): List<OutType> = param.map(::map)
}

interface TwoWayMapper<InType, OutType> : Mapper<InType, OutType> {
    fun mapReverse(param: OutType): InType

    fun mapListReverse(param: List<OutType>): List<InType> = param.map(::mapReverse)
}
