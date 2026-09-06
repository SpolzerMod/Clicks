package me.spolzer.clicks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.fabricmc.loader.api.FabricLoader;

public final class Config {
	private static final Logger LOGGER = Logger.getLogger("Clicks");
	public static final int GOAL_DIGITS = 15;
	private static final long MAX_GOAL = 999_999_999_999_999L;

	private static long left;
	private static long right;
	private static long leftGoal;
	private static long rightGoal;
	private static int x = -1;
	private static int y = 4;
	private static boolean shown = true;
	private static boolean paused;
	private static boolean goals;

	private static boolean dirty;

	private Config() {
	}

	public static void load() {
		load(file());
	}

	static void load(Path file) {
		Properties values = new Properties();

		try (InputStream in = Files.newInputStream(file)) {
			values.load(in);
		} catch (NoSuchFileException e) {
			return;
		} catch (IOException | IllegalArgumentException e) {
			LOGGER.log(Level.WARNING, "Could not read " + file, e);
			return;
		}

		left = Math.max(0, number(values.getProperty("left"), 0));
		right = Math.max(0, number(values.getProperty("right"), 0));
		leftGoal = goal(number(values.getProperty("leftGoal"), 0));
		rightGoal = goal(number(values.getProperty("rightGoal"), 0));
		x = coordinate(values.getProperty("x"), -1);
		y = coordinate(values.getProperty("y"), 4);
		shown = !"false".equals(values.getProperty("shown"));
		paused = "true".equals(values.getProperty("paused"));
		goals = "true".equals(values.getProperty("goals"));
		dirty = false;
	}

	public static void save() {
		save(file());
	}

	static void save(Path file) {
		if (!dirty) {
			return;
		}

		Properties values = new Properties();
		values.setProperty("left", Long.toString(left));
		values.setProperty("right", Long.toString(right));
		values.setProperty("leftGoal", Long.toString(leftGoal));
		values.setProperty("rightGoal", Long.toString(rightGoal));
		values.setProperty("x", Integer.toString(x));
		values.setProperty("y", Integer.toString(y));
		values.setProperty("shown", Boolean.toString(shown));
		values.setProperty("paused", Boolean.toString(paused));
		values.setProperty("goals", Boolean.toString(goals));

		Path pending = null;
		try {
			Path destination = file.toAbsolutePath();
			Files.createDirectories(destination.getParent());
			pending = Files.createTempFile(destination.getParent(), "clicks-", ".tmp");

			try (OutputStream out = Files.newOutputStream(pending)) {
				values.store(out, null);
			}

			// Keep the previous counters intact until the replacement is fully written.
			try {
				Files.move(pending, destination, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
			} catch (AtomicMoveNotSupportedException e) {
				Files.move(pending, destination, StandardCopyOption.REPLACE_EXISTING);
			}
			dirty = false;
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Could not save " + file, e);
		} finally {
			if (pending != null) {
				try {
					Files.deleteIfExists(pending);
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Could not remove " + pending, e);
				}
			}
		}
	}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("clicks.properties");
	}

	private static long goal(long value) {
		return Math.max(0, Math.min(value, MAX_GOAL));
	}

	private static int coordinate(String raw, int fallback) {
		long value = number(raw, fallback);
		return value < -1 || value > Integer.MAX_VALUE ? fallback : (int) value;
	}

	private static long number(String raw, long fallback) {
		if (raw == null) {
			return fallback;
		}

		try {
			return Long.parseLong(raw.trim());
		} catch (NumberFormatException e) {
			return fallback;
		}
	}

	public static void addLeft() {
		if (paused || left == Long.MAX_VALUE) {
			return;
		}

		left++;
		dirty = true;
	}

	public static void addRight() {
		if (paused || right == Long.MAX_VALUE) {
			return;
		}

		right++;
		dirty = true;
	}

	public static long left() {
		return left;
	}

	public static long right() {
		return right;
	}

	public static void wipe() {
		left = 0;
		right = 0;
		dirty = true;
	}

	public static long leftGoal() {
		return leftGoal;
	}

	public static void leftGoal(long value) {
		leftGoal = goal(value);
		dirty = true;
	}

	public static long rightGoal() {
		return rightGoal;
	}

	public static void rightGoal(long value) {
		rightGoal = goal(value);
		dirty = true;
	}

	public static boolean goalsOn() {
		return goals;
	}

	public static void goalsOn(boolean value) {
		goals = value;
		dirty = true;
	}

	public static boolean paused() {
		return paused;
	}

	public static void paused(boolean value) {
		paused = value;
		dirty = true;
	}

	public static boolean shown() {
		return shown;
	}

	public static void shown(boolean value) {
		shown = value;
		dirty = true;
	}

	public static int x() {
		return x;
	}

	public static int y() {
		return y;
	}

	public static void place(int newX, int newY) {
		newX = Math.max(0, newX);
		newY = Math.max(0, newY);
		if (x == newX && y == newY) {
			return;
		}

		x = newX;
		y = newY;
		dirty = true;
	}
}
