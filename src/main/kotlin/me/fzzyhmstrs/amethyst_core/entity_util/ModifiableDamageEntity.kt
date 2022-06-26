package me.fzzyhmstrs.amethyst_core.entity_util

import me.fzzyhmstrs.amethyst_core.modifier_util.AugmentEffect

interface ModifiableDamageEntity {

    var entityEffects: AugmentEffect

    fun passEffects(ae: AugmentEffect, level: Int){
        entityEffects.setConsumers(ae)
    }

}