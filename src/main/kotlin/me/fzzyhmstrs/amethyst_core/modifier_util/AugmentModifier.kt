package me.fzzyhmstrs.amethyst_core.modifier_util

import me.fzzyhmstrs.amethyst_core.item_util.AcceptableItemStacks
import me.fzzyhmstrs.amethyst_core.scepter_util.*
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.ScepterAugment
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import java.util.function.Consumer
import java.util.function.Predicate

open class AugmentModifier(
    modifierId: Identifier = ModifierDefaults.BLANK_ID,
    open var levelModifier: Int = 0,
    open var cooldownModifier: Double = 0.0,
    open var manaCostModifier: Double = 0.0
    ): AbstractModifier<AugmentModifier>(modifierId) {

    protected val effects: AugmentEffect = AugmentEffect()
    protected val xpModifier: XpModifiers = XpModifiers()
    private var secondaryEffect: ScepterAugment? = null

    private var hasSecondEffect: Boolean = false
    private var hasDesc: Boolean = false

    override fun plus(other: AugmentModifier): AugmentModifier {
        levelModifier += other.levelModifier
        cooldownModifier += other.cooldownModifier
        manaCostModifier += other.manaCostModifier
        xpModifier.plus(other.getXpModifiers())
        effects.plus(other.getEffectModifier())
        return this
    }

    fun hasSpellToAffect(): Boolean{
        return hasObjectToAffect()
    }
    fun checkSpellsToAffect(id: Identifier): Boolean{
        return checkObjectsToAffect(id)
    }
    fun hasSecondaryEffect(): Boolean{
        return hasSecondEffect
    }
    fun getSecondaryEffect(): ScepterAugment?{
        return secondaryEffect
    }
    fun getEffectModifier(): AugmentEffect {
        return effects
    }
    fun getXpModifiers(): XpModifiers {
        return xpModifier
    }

    fun withDamage(damage: Float = 0.0F, damagePerLevel: Float = 0.0F, damagePercent: Float = 0.0F): AugmentModifier {
        effects.plus(ModifierDefaults.BLANK_EFFECT.withDamage(damage, damagePerLevel, damagePercent))
        return this
    }
    fun withAmplifier(amplifier: Int = 0, amplifierPerLevel: Int = 0, amplifierPercent: Int = 0): AugmentModifier {
        effects.plus(ModifierDefaults.BLANK_EFFECT.withAmplifier(amplifier, amplifierPerLevel, amplifierPercent))
        return this
    }
    fun withDuration(duration: Int = 0, durationPerLevel: Int = 0, durationPercent: Int = 0): AugmentModifier {
        effects.plus(ModifierDefaults.BLANK_EFFECT.withDuration(duration, durationPerLevel, durationPercent))
        return this
    }
    fun withRange(range: Double = 0.0, rangePerLevel: Double = 0.0, rangePercent: Double = 0.0): AugmentModifier {
        effects.plus(ModifierDefaults.BLANK_EFFECT.withRange(range, rangePerLevel, rangePercent))
        return this
    }
    fun withSecondaryEffect(effect: AugmentEffect): AugmentModifier {
        effects.plus(effect)
        return this
    }
    fun withXpMod(type: SpellType, xpMod: Int): AugmentModifier {
        val xpMods = when(type){
                SpellType.FURY ->{
                    ModifierDefaults.BLANK_XP_MOD.withFuryMod(xpMod)}
                SpellType.WIT ->{
                    ModifierDefaults.BLANK_XP_MOD.withWitMod(xpMod)}
                SpellType.GRACE ->{
                    ModifierDefaults.BLANK_XP_MOD.withGraceMod(xpMod)}
                else -> return this
            }
        xpModifier.plus(xpMods)
        return this
    }
    fun withSpellToAffect(predicate: Predicate<Identifier>): AugmentModifier {
        addObjectToAffect(predicate)
        return this
    }
    fun withSecondaryEffect(augment: ScepterAugment): AugmentModifier {
        secondaryEffect = augment
        hasSecondEffect = true
        return this
    }
    fun withConsumer(consumer: Consumer<List<LivingEntity>>, type: AugmentConsumer.Type): AugmentModifier {
        effects.withConsumer(consumer,type)
        return this
    }
    fun withConsumer(augmentConsumer: AugmentConsumer): AugmentModifier {
        effects.withConsumer(augmentConsumer.consumer,augmentConsumer.type)
        return this
    }
    fun withDescendant(modifier: AugmentModifier): AugmentModifier {
        addDescendant(modifier)
        return this
    }

    override fun acceptableItemStacks(): MutableList<ItemStack>{
        return AcceptableItemStacks.scepterAcceptableItemStacks(1)
    }

    override fun compiler(): Compiler {
        return Compiler(mutableListOf(), AugmentModifier())
    }

    override fun compile(modifiers: List<AugmentModifier>, compiledData: AugmentModifier): CompiledModifiers {
        return CompiledModifiers(modifiers, compiledData)
    }
}