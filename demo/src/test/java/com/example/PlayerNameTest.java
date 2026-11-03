package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for player names. */
class PlayerNameTest {
    @BeforeEach
    void resetDefaultNameSequences() {
        HumanPlayer.resetDefaultNameSequence();
        CpuPlayer.resetDefaultNameSequence();
    }

    @Test
    void humanPlayerHasDefaultNameAndCanChangeIt() {
        HumanPlayer player = new HumanPlayer('X', new Scanner(""));

        assertEquals("Player #1", player.getName());
        player.setName("Alice");
        assertEquals("Alice", player.getName());
    }

    @Test
    void humanPlayersReceiveSequentialDefaultNames() {
        HumanPlayer first = new HumanPlayer('X', new Scanner(""));
        HumanPlayer second = new HumanPlayer('O', new Scanner(""));

        assertEquals("Player #1", first.getName());
        assertEquals("Player #2", second.getName());
    }

    @Test
    void cpuPlayerHasDefaultNameAndCanChangeIt() {
        CpuPlayer player = new CpuPlayer('O');

        assertEquals("CPU #1", player.getName());
        player.setName("Computer");
        assertEquals("Computer", player.getName());
    }

    @Test
    void cpuPlayersReceiveSequentialDefaultNames() {
        CpuPlayer first = new CpuPlayer('X');
        CpuPlayer second = new CpuPlayer('O');

        assertEquals("CPU #1", first.getName());
        assertEquals("CPU #2", second.getName());
    }

    @Test
    void rejectsNullEmptyAndWhitespaceOnlyNames() {
        HumanPlayer player = new HumanPlayer('X', new Scanner(""));

        assertThrows(IllegalArgumentException.class, () -> player.setName(null));
        assertThrows(IllegalArgumentException.class, () -> player.setName(""));
        assertThrows(IllegalArgumentException.class, () -> player.setName(" \t\n "));
    }
}
