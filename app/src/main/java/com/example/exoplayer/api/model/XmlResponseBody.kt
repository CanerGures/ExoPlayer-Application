package com.example.exoplayer.api.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


data class DASH(
    @Attribute(required = true)
    val Name: String?,
    @Attribute(required = true)
    val DRM: String?,
    @Attribute(required = true)
    val StreamLink: String?
)

@Root(name = "GroupList", strict = false)
data class XmlGroupList(
    @field:Element(name = "DASH", required = false) @param:Element(name = "DASH")
    var DASH: DASH

)


