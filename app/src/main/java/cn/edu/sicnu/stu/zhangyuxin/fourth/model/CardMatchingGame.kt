package cn.edu.sicnu.stu.zhangyuxin.fourth.model

class CardMatchingGame private constructor(private val cards: MutableList<Card>, @Suppress("UNUSED_PARAMETER") owned: Boolean) : java.io.Serializable {
    constructor(count: Int) : this(deal(count), true)
    constructor(testCards: List<Card>) : this(testCards.toMutableList(), true)
    companion object {
        private fun deal(count: Int): MutableList<Card> {
            require(count in 1..52)
            val deck = Deck()
            return MutableList(count) { requireNotNull(deck.drawRandomCard()) }
        }
    }
    val cardCount: Int get() = cards.size
    val matchedCount: Int get() = cards.count { it.isMatched }
    val isFinished: Boolean get() {
        val remaining = cards.filter { !it.isMatched }
        return remaining.indices.none { i -> (i + 1 until remaining.size).any { j -> remaining[i].match(arrayOf(remaining[j])) > 0 } }
    }
    var score = 0
        private set

    fun cardAtIndex(index: Int): Card {
        return cards.get(index)
    }

    val MISMATCH_PENALTY = 2
    val MATCH_BONUS = 4
    val COST_TO_CHOOSE = 1

    fun chooseCardAtIndex(index: Int) {
        val card= cardAtIndex(index)
        if (!card.isMatched) {
            if (card.isChosen) {
                card.isChosen = false
            } else {
                for (otherCard in cards) {
                    if (otherCard.isChosen && !otherCard.isMatched) {
                        val matchScore = card.match(arrayOf(otherCard))
                        if (matchScore > 0) {
                            score += matchScore * MATCH_BONUS
                            otherCard.isMatched = true
                            card.isMatched = true
                        } else {
                            score -= MISMATCH_PENALTY
                            otherCard.isChosen = false
                        }
                        break
                    }
                }
                score -= COST_TO_CHOOSE
                card.isChosen = true
            }
        }
    }
}
