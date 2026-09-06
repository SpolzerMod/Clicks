package me.spolzer.clicks;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {
	@TempDir
	Path directory;
	private Path file;

	@BeforeEach
	void reset() throws Exception {
		file = directory.resolve("clicks.properties");
		load("");
	}

	@Test
	void savesCountsAndSettingsAcrossReloads() throws Exception {
		Config.addLeft();
		Config.addRight();
		Config.addRight();
		Config.leftGoal(100);
		Config.rightGoal(200);
		Config.goalsOn(true);
		Config.shown(false);
		Config.paused(true);
		Config.place(23, 45);
		Config.save(file);
		Config.wipe();
		Config.load(file);
		assertEquals(1, Config.left());
		assertEquals(2, Config.right());
		assertEquals(100, Config.leftGoal());
		assertEquals(200, Config.rightGoal());
		assertTrue(Config.goalsOn());
		assertTrue(Config.paused());
		assertFalse(Config.shown());
		assertEquals(23, Config.x());
		assertEquals(45, Config.y());
	}

	@Test
	void hiddenHudStillCountsButPauseDoesNot() {
		Config.shown(false);
		Config.addLeft();
		Config.addRight();
		Config.paused(true);
		Config.addLeft();
		Config.addRight();
		assertEquals(1, Config.left());
		assertEquals(1, Config.right());
	}

	@Test
	void validatesHandEditedValues() throws Exception {
		load("left=-12\nright=not-a-number\nx=4294967296\ny=-99\nleftGoal=9223372036854775807\nrightGoal=-10\n");
		assertEquals(0, Config.left());
		assertEquals(0, Config.right());
		assertEquals(-1, Config.x());
		assertEquals(4, Config.y());
		assertEquals(999_999_999_999_999L, Config.leftGoal());
		assertEquals(0, Config.rightGoal());
	}

	@Test
	void countersDoNotWrapAtLongLimit() throws Exception {
		load("left=9223372036854775807\nright=9223372036854775807\n");
		Config.addLeft();
		Config.addRight();
		assertEquals(Long.MAX_VALUE, Config.left());
		assertEquals(Long.MAX_VALUE, Config.right());
	}

	@Test
	void draggingPastTopLeftDoesNotRestoreDefaultAnchor() {
		Config.place(-20, -30);
		assertEquals(0, Config.x());
		assertEquals(0, Config.y());
	}

	@Test
	void resetPreservesGoalsAndPreferences() {
		Config.addLeft();
		Config.leftGoal(50);
		Config.paused(true);
		Config.wipe();
		assertEquals(0, Config.left());
		assertEquals(0, Config.right());
		assertEquals(50, Config.leftGoal());
		assertTrue(Config.paused());
	}

	@Test
	void failedReplacementCanBeRetriedWithoutLosingPreviousFile() throws Exception {
		Config.addLeft();
		Path blocked = Files.createDirectory(directory.resolve("blocked"));
		Path previous = blocked.resolve("previous");
		Files.write(previous, new byte[] { 42 });
		Config.save(blocked);
		assertArrayEquals(new byte[] { 42 }, Files.readAllBytes(previous));
		Config.save(file);
		Properties stored = new Properties();
		try (InputStream in = Files.newInputStream(file)) {
			stored.load(in);
		}
		assertEquals("1", stored.getProperty("left"));
		try (Stream<Path> children = Files.list(directory)) {
			assertFalse(children.anyMatch(path -> path.toString().endsWith(".tmp")));
		}
	}

	@Test
	void malformedPropertiesDoNotCrashOrReplaceCurrentState() throws Exception {
		Config.addLeft();
		Files.write(file, new byte[] { 'x', '=', '\\', 'u', 'Z', 'Z', 'Z', 'Z' });
		assertDoesNotThrow(() -> Config.load(file));
		assertEquals(1, Config.left());
	}

	private void load(String values) throws Exception {
		Files.write(file, values.getBytes(StandardCharsets.ISO_8859_1));
		Config.load(file);
	}
}
