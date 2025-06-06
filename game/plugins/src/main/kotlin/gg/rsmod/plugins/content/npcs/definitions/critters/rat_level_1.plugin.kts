package gg.rsmod.plugins.content.npcs.definitions.critters

import gg.rsmod.plugins.content.drops.DropTableFactory

val ids = intArrayOf(Npcs.RAT, Npcs.RAT_4415, Npcs.RAT_4396)

val table = DropTableFactory
val rat =
    table.build {
        guaranteed {
            obj(Items.BONES)
        }
    }

table.register(rat, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    p.playSound(Sfx.RAT_DEATH)
}

on_npc_death(*ids) {
    table.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}

ids.forEach {
    set_combat_def(it) {
        configs {
            attackSpeed = 4
            respawnDelay = 1
        }
        stats {
            hitpoints = 20
            attack = 1
            strength = 1
            defence = 1
            magic = 1
            ranged = 1
        }
        bonuses {
            attackStab = -47
            attackCrush = -53
            defenceStab = -42
            defenceSlash = -42
            defenceCrush = -42
            defenceMagic = -42
            defenceRanged = -42
        }
        anims {
            attack = Anims.RAT_ATTACK
            death = Anims.RAT_DEATH
            block = Anims.RAT_BLOCK
        }
    }
}
