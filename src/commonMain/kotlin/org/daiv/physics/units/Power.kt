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


@Serializable
data class PowerValue(private val value: Double, private val unit: PowerUnit) {
    fun as_W() = when (unit) {
        PowerUnit.kW -> value * 1000.0
        PowerUnit.W -> value
    }

    fun as_kW() = when (unit) {
        PowerUnit.kW -> value
        PowerUnit.W -> value / 1000.0
    }

    companion object {
        fun Double.kW() = PowerValue(this, PowerUnit.kW)
        fun Double.W() = PowerValue(this, PowerUnit.W)
    }
}

@Serializable
data class EnergyValue(private val value: Double, private val unit: EnergyUnit) {

    operator fun minus(other: EnergyValue): EnergyValue {
        return (this.as_Wh() - other.as_Wh()).Wh()
    }

    fun as_Wh() = when (unit) {
        EnergyUnit.kWh -> value * 1000.0
        EnergyUnit.Wh -> value
    }

    fun as_kWh() = when (unit) {
        EnergyUnit.kWh -> value
        EnergyUnit.Wh -> value / 1000.0
    }

    companion object {
        fun Double.kWh() = EnergyValue(this, EnergyUnit.kWh)
        fun Double.Wh() = EnergyValue(this, EnergyUnit.Wh)
    }
}

