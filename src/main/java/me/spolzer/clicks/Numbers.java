package me.spolzer.clicks;

public final class Numbers {
	private Numbers() {
	}

	public static String format(long value) {
		StringBuilder text = new StringBuilder(Long.toString(value));

		for (int i = text.length() - 3; i > 0; i -= 3) {
			text.insert(i, ' ');
		}

		return text.toString();
	}

	public static String digits(String raw, int limit) {
		StringBuilder kept = new StringBuilder();

		for (int i = 0; i < raw.length() && kept.length() < limit; i++) {
			char letter = raw.charAt(i);

			if (letter >= '0' && letter <= '9') {
				kept.append(letter);
			}
		}

		while (kept.length() > 1 && kept.charAt(0) == '0') {
			kept.deleteCharAt(0);
		}

		return kept.toString();
	}
}
