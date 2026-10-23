package cn.edu.sicnu.stu.zhangyuxin.fourth.model

import org.junit.Assert.*
import org.junit.Test
import java.io.*

class GameModelTest {
    @Test fun rankMatchHasPriorityOverSuit() {
        assertEquals(4, Card("♥", "A").match(arrayOf(Card("♣", "A"))))
        assertEquals(1, Card("♥", "A").match(arrayOf(Card("♥", "K"))))
        assertEquals(0, Card("♥", "A").match(arrayOf(Card("♣", "K"))))
    }
    @Test fun deckContains52UniqueCardsAndExhausts() {
        val deck = Deck()
        val drawn = (1..52).map { requireNotNull(deck.drawRandomCard()).toString() }
        assertEquals(52, drawn.toSet().size)
        assertNull(deck.drawRandomCard())
    }
    @Test fun rankPairScores14AndLocksCards() {
        val game = CardMatchingGame(listOf(Card("♥", "A"), Card("♣", "A")))
        game.chooseCardAtIndex(0); assertEquals(-1, game.score)
        game.chooseCardAtIndex(1); assertEquals(14, game.score)
        assertEquals(2, game.matchedCount)
        game.chooseCardAtIndex(0); assertEquals(14, game.score)
        assertTrue(game.isFinished)
    }
    @Test fun suitPairScores2() {
        val game = CardMatchingGame(listOf(Card("♥", "A"), Card("♥", "K")))
        game.chooseCardAtIndex(0); game.chooseCardAtIndex(1)
        assertEquals(2, game.score)
    }
    @Test fun mismatchScoresMinus4AndTurnsOldCardBack() {
        val game = CardMatchingGame(listOf(Card("♥", "A"), Card("♣", "K")))
        game.chooseCardAtIndex(0); game.chooseCardAtIndex(1)
        assertEquals(-4, game.score)
        assertFalse(game.cardAtIndex(0).isChosen)
        assertTrue(game.cardAtIndex(1).isChosen)
        assertEquals(0, game.matchedCount)
        assertTrue(game.isFinished)
    }
    @Test fun unchooseDoesNotChargeAgain() {
        val game = CardMatchingGame(16)
        game.chooseCardAtIndex(0); game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertFalse(game.cardAtIndex(0).isChosen)
    }
    @Test fun freshGameResetsScoreAndCards() {
        val game = CardMatchingGame(16)
        assertEquals(16, game.cardCount); assertEquals(0, game.score)
        assertEquals(0, game.matchedCount)
        assertEquals(16, (0..15).map { game.cardAtIndex(it).toString() }.toSet().size)
    }
    @Test(expected = IllegalArgumentException::class) fun invalidCountRejected() { CardMatchingGame(53) }
    @Test fun serializationPreservesTurnAndScore() {
        val game = CardMatchingGame(16); game.chooseCardAtIndex(0)
        val bytes = ByteArrayOutputStream()
        ObjectOutputStream(bytes).use { it.writeObject(game) }
        val restored = ObjectInputStream(ByteArrayInputStream(bytes.toByteArray())).use { it.readObject() as CardMatchingGame }
        assertEquals(-1, restored.score)
        assertTrue(restored.cardAtIndex(0).isChosen)
        assertEquals(game.cardAtIndex(0).toString(), restored.cardAtIndex(0).toString())
    }
}
