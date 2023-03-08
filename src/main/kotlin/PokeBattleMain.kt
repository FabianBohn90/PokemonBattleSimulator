import kotlin.random.Random

// Pokemon Klasse
open class Pokemon(val name: String, val type: String, var health: Int, private var level: Int) {
    fun attack(enemy: Pokemon) {
        val damage = Random.nextInt(5, 15) + level * 5
        enemy.health -= damage
        println("$name attacked ${enemy.name} and dealt $damage damage.")
    }
}

// Trainer Klasse, die von Pokemon Klasse erbt
open class Trainer(name: String, var pokemon: Pokemon, private var potions: MutableList<Potion>) :
    Pokemon(name, "Trainer", 100, 1) {

    fun usePotion() {
        if (potions.isNotEmpty()) {
            val potion = potions[0]
            pokemon.health += potion.healingPower
            potions.removeAt(0)
            println("$name used a ${potion.name} and healed ${potion.healingPower} health for ${pokemon.name}.")
        } else {
            println("$name has no potions left.")
        }
    }
}

class KITrainer(name: String, pokemon: Pokemon, private var kiPotions: MutableList<Potion>, private var enemy: Pokemon) :
    Trainer(name, pokemon, mutableListOf()) {

    fun performRandomAction() {
        val actions = listOf("Attack", "Use Potion")
        val randomActionIndex = Random.nextInt(actions.size)

        when (actions[randomActionIndex]) {
            "Attack" -> {
                pokemon.attack(enemy)
            }
            "Use Potion" -> {
                if (kiPotions.isNotEmpty()) {
                    val potion = kiPotions[0]
                    pokemon.health += potion.healingPower
                    kiPotions.removeAt(0)
                    println("$name used a ${potion.name} and healed ${potion.healingPower} health for ${pokemon.name}.")
                }else {
                    performRandomAction() // try again without using potions
                }
            }
        }
    }
}

// Potion Klasse
class Potion(val name: String, val healingPower: Int)

fun main() {
    val pokemonList = mutableListOf(
        Pokemon("Charmander", "Fire", 100, 1),
        Pokemon("Squirtle", "Water", 100, 1),
        Pokemon("Bulbasaur", "Grass", 100, 1)
    )

    // Potion-Listen für Trainer und KI-Trainer
    val trainerPotions = mutableListOf(
        Potion("Potion", 20),
        Potion("Super Potion", 50),
        Potion("Hyper Potion", 100)
    )

    val kiTrainerPotions = mutableListOf(
        Potion("KI Potion", 30),
        Potion("KI Super Potion", 60),
        Potion("KI Hyper Potion", 120)
    )

    println("Welcome to the Pokemon Battle Simulator!")
    println("Choose your Pokemon:")

    for ((index, pokemon) in pokemonList.withIndex()) {
        println("${index + 1}. ${pokemon.name} (${pokemon.type}) - ${pokemon.health} HP")
    }

    val chosenPokemonIndex = readlnOrNull()?.toInt() ?: 1
    val chosenPokemon = pokemonList[chosenPokemonIndex - 1]
    var kiChosenPokemon = 0

    when (chosenPokemonIndex) {
        1 -> { kiChosenPokemon = 1 }
        2 -> { kiChosenPokemon = 2 }
        3 -> { kiChosenPokemon = 0 }
    }

    println("You chose ${chosenPokemon.name}!")

    val trainer = Trainer("Ash", chosenPokemon, trainerPotions)
    val kiTrainer = KITrainer("Misty", pokemonList[kiChosenPokemon], kiTrainerPotions, chosenPokemon)

    println("${kiTrainer.name} challenges you to a battle!")


    while (trainer.pokemon.health > 0 && kiTrainer.pokemon.health > 0) {
        println("${trainer.name}'s ${trainer.pokemon.name}: ${trainer.pokemon.health} HP")
        println("${kiTrainer.name}'s ${kiTrainer.pokemon.name}: ${kiTrainer.pokemon.health} HP")
        println("What do you want to do?")
        println("1. Attack")
        println("2. Use Potion")

        when (readlnOrNull()?.toInt() ?: 1) {
            1 -> {
                trainer.pokemon.attack(kiTrainer.pokemon)
                if (kiTrainer.pokemon.health > 0) {
                    kiTrainer.performRandomAction()
                }
            }

            2 -> trainer.usePotion()
        }
    }

    if (trainer.pokemon.health <= 0) {
        println("${trainer.name}'s ${trainer.pokemon.name} fainted. ${kiTrainer.name} wins!")
    } else {
        println("${kiTrainer.name}'s ${kiTrainer.pokemon.name} fainted. ${trainer.name} wins!")
    }
}
