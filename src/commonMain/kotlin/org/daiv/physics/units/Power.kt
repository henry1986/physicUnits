package org.daiv.physics.units

import kotlinx.serialization.Serializable

enum class EnergyUnit {
    kWh, Wh
}

enum class PowerUnit {
    W, kW
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
}

interface Unitable {
    fun Double.kW() = PowerValue(this, PowerUnit.kW)
    fun Double.W() = PowerValue(this, PowerUnit.W)
    fun Double.kWh() = EnergyValue(this, EnergyUnit.kWh)
    fun Double.Wh() = EnergyValue(this, EnergyUnit.Wh)
}

@Serializable
data class PowerValue(private val value: Double, private val unit: PowerUnit) : Unitable, UnitOperator<PowerValue> {

    override fun norm() = as_W()
    override fun Double.toNorm() = W()

    fun as_W() = when (unit) {
        PowerUnit.kW -> value * 1000.0
        PowerUnit.W -> value
    }

    fun as_kW() = when (unit) {
        PowerUnit.kW -> value
        PowerUnit.W -> value / 1000.0
    }
}

@Serializable
data class EnergyValue(private val value: Double, private val unit: EnergyUnit) : Unitable, UnitOperator<EnergyValue> {

    override fun norm() = as_Wh()
    override fun Double.toNorm() = Wh()

    fun as_Wh() = when (unit) {
        EnergyUnit.kWh -> value * 1000.0
        EnergyUnit.Wh -> value
    }

    fun as_kWh() = when (unit) {
        EnergyUnit.kWh -> value
        EnergyUnit.Wh -> value / 1000.0
    }
}

