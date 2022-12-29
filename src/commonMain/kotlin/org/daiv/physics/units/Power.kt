package org.daiv.physics.units

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

enum class EnergyUnit {
    kWh, Wh
}

enum class PowerUnit {
    W, kW
}

/**
 * uses [DEFAULT] for e.g. Euro, and [CENTUM] for e.g. EuroCent. (Centum as latin for hundredth)
 */
enum class MoneyUnit {
    DEFAULT, CENTUM
}

@Serializable
data class Money private constructor(private val value: Double) : UnitOperator<Money>, NormUnit<Money>, Unitable {

    constructor(value: Double, unit: MoneyUnit) : this(
        when (unit) {
            MoneyUnit.DEFAULT -> value * 100.0
            MoneyUnit.CENTUM -> value
        }
    )

    fun asMain() = value / 100.0
    fun asCentum() = value
    override fun norm() = asCentum()
    override fun Double.toNorm() = MoneyCentum()
}

@Serializable
data class PercentValue(private val value: Double) {
    fun as_fromZeroToOne() = value / 100.0
    fun as_Percent() = value
}

interface NormUnit<T : NormUnit<T>> {
    fun norm(): Double
    fun Double.toNorm(): T
}

interface UnitOperator<T : NormUnit<T>> : NormUnit<T> {
    operator fun minus(t: T): T {
        return (this.norm() - t.norm()).toNorm()
    }

    operator fun plus(t: T): T {
        return (this.norm() + t.norm()).toNorm()
    }

    operator fun times(d: Double): T {
        return (this.norm() * d).toNorm()
    }

    operator fun div(d: Double): T {
        return (this.norm() / d).toNorm()
    }

    operator fun div(d: T): Double {
        return (this.norm() / d.norm())
    }

    operator fun compareTo(other: T):Int{
        return norm().compareTo(other.norm())
    }
}

interface Unitable {
    fun Double.kW() = PowerValue(this, PowerUnit.kW)
    fun Double.W() = PowerValue(this, PowerUnit.W)
    fun Double.kWh() = EnergyValue(this, EnergyUnit.kWh)
    fun Double.Wh() = EnergyValue(this, EnergyUnit.Wh)
    fun Double.Money() = Money(this, MoneyUnit.DEFAULT)
    fun Double.MoneyCentum() = Money(this, MoneyUnit.CENTUM)
}

@Serializable
data class PowerValue private constructor(private val value: Double) : Unitable, UnitOperator<PowerValue> {

    constructor(value: Double, unit: PowerUnit) : this(
        when (unit) {
            PowerUnit.kW -> value * 1000.0
            PowerUnit.W -> value
        }
    )

    override fun norm() = as_W()
    override fun Double.toNorm() = W()

    fun as_W() = value
    fun as_kW() = value / 1000.0
}

@Serializable
data class EnergyValue private constructor(private val value: Double) : Unitable, UnitOperator<EnergyValue> {

    constructor(value: Double, unit: EnergyUnit) : this(
        when (unit) {
            EnergyUnit.kWh -> value * 1000.0
            EnergyUnit.Wh -> value
        }
    )

    override fun norm() = as_Wh()
    override fun Double.toNorm() = Wh()

    fun as_Wh() = value
    fun as_kWh() = value / 1000.0
}

