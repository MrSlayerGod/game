package gg.rsmod.plugins.content.magic.teleports

import gg.rsmod.game.model.collision.ObjectType
import gg.rsmod.plugins.content.magic.*
import gg.rsmod.plugins.content.magic.MagicSpells.on_magic_spell_button

private val SOUNDAREA_ID = 200
private val SOUNDAREA_RADIUS = 10
private val SOUNDAREA_VOLUME = 1

TeleportSpell.values.forEach { teleport ->
    if (teleport.spriteId == null) {
        on_magic_spell_button(teleport.spellName) { metadata ->
            player.teleport(teleport, metadata)
        }
    } else {
        val metadata = MagicSpells.getMetadata(teleport.spriteId)!!
        on_button(metadata.interfaceId, metadata.component) {
            player.teleport(teleport, metadata)
        }
    }
}

fun Player.teleport(
    spell: TeleportSpell,
    data: SpellMetadata,
) {
    val endTile = findValidTile(spell)
    teleport(spell.type, endTile, spell.xp, data)
}

fun Player.findValidTile(spell: TeleportSpell): Tile {
    var tile = spell.endArea.randomTile
    while (world.getObject(tile, ObjectType.INTERACTABLE) != null) {
        tile = spell.endArea.randomTile
    }
    return tile
}

fun Player.teleport(
    type: TeleportType,
    endTile: Tile,
    xp: Double,
    data: SpellMetadata,
) {
    if (!MagicSpells.canCast(this, data.lvl, data.runes)) {
        return
    }

    if (canTeleport(type)) {
        MagicSpells.removeRunes(this, data.runes, data.sprite)
        teleport(endTile, type)
        addXp(Skills.MAGIC, xp, checkBrawlingGloves = true)
        world.spawn(AreaSound(tile, SOUNDAREA_ID, SOUNDAREA_RADIUS, SOUNDAREA_VOLUME))
    }
}
